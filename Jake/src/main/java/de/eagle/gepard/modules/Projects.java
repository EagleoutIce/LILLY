package de.eagle.gepard.modules;

/**
 * @file Projects.java
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.1.0
 *
 * @brief Gepardmodul welches Projekte zur Verfügung stellt.
 * @see de.eagle.gepard.modules.AbstractGepardModule
 * @see de.eagle.gepard.parser.GeneratorParser
 */

/*
 * Projekte, was sind das eigentlich?
 *
 * Das Ziel ist das folgende:
 * Über die GepardBox 'project' kann ein neues Projekt mit dem Namen 'name' erzeugt werden.
 * Ein Projekt kann direkt über Jake gestartet werden mithilfe von 'jake <projektname>', wobei
 * im Falle einer Doppeldeutigkeit zuerst die Dateien des aktuellen Verzeichnisses und
 * anschließend die CoreFunctions durchsucht werden.
 * Es ist möglich den Namen mit einem Unsterstrich anzuführen, um das Projekt in der
 * Autovervollständigung nicht anzeigen zu lassen
 *
 * Ein Projekt kann auf beliebig viele Konfigurationsdateien verweisen, für die jeweils
 * eine eigene 'jake' Instanz ausgeführt wird, was es auch ermöglicht so weitere Projekte
 * zu starten.
 *
 *
 */

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.helper.Executer;
import de.eagle.util.io.JakeWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Projects extends AbstractGepardModule {
    /**
     * Name der Generator-Box
     */
    public static final String box_name = "project";

    private static Projects tInstance = null;

    public static Projects getInstance() {
        if (tInstance == null)
            tInstance = new Projects();
        return tInstance;
    }

    /**
     * Konstruiert das GepardModul
     */
    private Projects() {
        super(box_name);
    }

    /**
     * Gespeicherte Blaupause, die es ermöglicht, die entsprechende Blaupause nur
     * einmal pro Klasse zu generieren.
     */
    protected static Settings blueprint = null;

    /**
     * @return die Blaupause für die Einstellungen
     */
    @Override
    public Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Projects");
            // ---- Mandatories
            blueprint.put("name",
                    SettingDeskriptor.create("name", "Interner Name der Regel", eSetting_Type.IS_TEXT, true));
            blueprint.put("display-name", SettingDeskriptor.create("display-name",
                    "Name der beim Ausführen angezeigt wird", eSetting_Type.IS_TEXT, true));
            blueprint.put("configfiles", SettingDeskriptor.create("configfiles",
                    "Kommaseparierte Liste an Konfigurationsdateien", eSetting_Type.IS_TEXT, true));
            // ------ optional
            blueprint.put("silence", SettingDeskriptor.create("silence", "Sollen die Ausgaben der einzelnen Operationen angezeigt werden?", eSetting_Type.IS_SWITCH, false, "false"));
        }
        return blueprint;
    }

    /**
     * @return Standardeinstellungen
     */
    @Override
    public Settings getDefaults() {
        Settings settings = new Settings("<Default> Projects", true, new HashMap<>());
        // settings.put("test:thereal", SettingDeskriptor.create("test", "hihi" ,
        // eSetting_Type.IS_TEXT, false));
        // Es sollte keine Standardprojekte geben
        return settings;
    }

    /**
     * Bearbeitet eine komplette Datei
     *
     * @param rulefiles die Liste der rulefiles
     *
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     * @throws IOException Im Falle eines Fails von
     *                     {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public Settings getProjects(String rulefiles) throws IOException {
        if (rulefiles.isEmpty())
            return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        GeneratorParser.JObject[] j = gp.parseFile(Projects.box_name, new Settings("Projects"), true);
        return parseRules(j, true);
    }

    public String[] getAllProjectNames(Settings projects) {
        if (projects == null || projects.size() == 0)
            return new String[] {};
        ArrayList<String> ret = new ArrayList<>(projects.size());
        for (var cfg : projects) {
            String key = cfg.getKey();
            ret.add(key.substring(1, key.lastIndexOf(":")));
        }
        return ret.toArray(new String[0]);
    }

    /**
     * Parst eine einzelne Box
     *
     * @param box      die Box
     * @param complete ingored
     *
     * @return null, wenn es keine projects-Box war, sonst die entsprechende
     *         Einstellung
     */
    public Settings parseBox(GeneratorParser.JObject box, boolean complete) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Projects");

        Settings settings = new Settings(box.getName());
        boolean silence = (box.config.getValue("silence") != null && !box.config.getValue("silence").equals("false"));
        settings.put((silence?"1":"0")+box.config.getValue("name") + ":" + box.config.getValue("display-name"), SettingDeskriptor
                .create("", "Box-Deskriptor für Project (Gepard)", box.config.getValue("configfiles")));
        writeLoggerDebug2("erhalten: " + settings.toString(), "Projects");
        return settings;
    }

    /**
     * Liefert das Projekt mit dem entsprechenden Namen
     * 
     * @param projects Samlung der Projekte
     * @param name     Name des gesuchten Projekts
     * @return das entsprechende Projekt
     */
    public static SettingDeskriptor<String> getProject(Settings projects, String name) {
        for (var p : projects) {
            String key = p.getKey();
            if (key.substring(1, key.lastIndexOf(":")).equals(name))
                return p.getValue();
        }
        return null;
    }

    /**
     * Führt die entsprechenden Konfigurationsdateien aus
     *
     * @param projects Satz an Einstellungen
     * @param name     Name des Projektes
     * @return {@link ReturnStatus}
     */
    public ReturnStatus executeProject(Settings projects, String name) {
        SettingDeskriptor<String> project = getProject(projects, name);
        String[] configfiles = project.getValue().split(",");
        if(configfiles.length == 0){
            JakeWriter.err.format("Das gewünschte Projekt (%s) gibt keine Konfigurationsziele an. Abbruch!%n",name);
            return ReturnStatus.EXIT_FAILURE;
        }
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var files = new LinkedList<Callable<String[]>>();
        Settings expandables = new Settings("expandables");
        try {
            expandables = Expandables.getInstance().expandsCS();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : configfiles) {
            String _pre_m = s.trim();
            // we will expand the Path to allow Expandables to work on it :D
            _pre_m = Expandables.expand(expandables, _pre_m);
            String m = new File(_pre_m.trim()).getAbsolutePath(); // Ignore pending whitespaces
            String g = project.getName();
            files.add(() -> {
                JakeWriter.out.println("Starte für Projekt \"" + g.substring(g.lastIndexOf(":") + 1)
                        + "\" die Konfigurationsdatei: " + m);
                BufferedReader br = Executer
                        .runBashCommand("cd \"" + new File(m).getParent() + "\" && jake config -file: \"" + m
                                + "\" || echo 'Error! The File [" + m + "] seems to be invalid'");
                if(g.charAt(0)=='1')
                    return new String[] {"Kompilieren von " + m + " wird im Hintergrund fortgesetzt!"};
                else
                    return br.lines().toArray(String[]::new);
            });
        }
        try {
            var values = executor.invokeAll(files);
            for (Future<String[]> _f : values) {
                String[] f = _f.get();
                for (String t : f) {
                    System.out.println(t);
                }
            }
            executor.shutdown();
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ReturnStatus.EXIT_SUCCESS;
    }

}
