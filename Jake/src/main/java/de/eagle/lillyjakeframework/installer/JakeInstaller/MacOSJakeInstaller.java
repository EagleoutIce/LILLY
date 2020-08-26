package de.eagle.lillyjakeframework.installer.JakeInstaller;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import de.eagle.lillyjakeframework.core.CoreSettings;

/**
 * @file MacOSJakeInstaller.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Installiert Jake auf einem MacOS Betriebssystem
 */

import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.util.datatypes.FunctionCollector;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.helper.Cloner;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;



public class MacOSJakeInstaller extends AutoInstaller {

    public MacOSJakeInstaller(boolean gui) {
        super("MacOs Jake Installer",gui);
        // test if lilly_jake is installed:
        steps = new FunctionCollector<>(Map.ofEntries(
            Map.entry("generate_cmd_line_exec",  new FunctionDeskriptor<String, String[]>("fkt_generate_cmd_line_exec","Generiert den Kommandozeilenstarter", MacOSJakeInstaller::fkt_generate_cmd_line_exec)),
            Map.entry("inject_path_extend",  new FunctionDeskriptor<String, String[]>("fkt_inject_path_extend","Erweitere Path-Variable", MacOSJakeInstaller::fkt_inject_path_extend))
        ));

        this.progress = 0; // TEMPORARY => CHANGE
        next = "generate_cmd_line_exec";
    }


    public static LinkedList<String> shells = new LinkedList<>(
            Arrays.asList(Paths.get(HOME, ".zshrc").toString(), Paths.get(HOME, ".bashrc").toString(),
                    Paths.get(HOME, ".bash_profile").toString(), Paths.get(HOME, ".kshrc").toString()));

    public static String getDesktopPath() {
        return Paths.get(HOME, ".local", "share", "applications").toString() + PropertiesProvider.getFileSeparator()
                + "jake.desktop";
    }

    public static String getCmdLinePath() {
        return PropertiesProvider.getFileSeparator() + Paths.get(HOME, ".local", "bin").toString()
                + PropertiesProvider.getFileSeparator() + "jake";
    }

    public static String[] fkt_generate_cmd_line_exec(String s) {
        // Erstelle command line path wenn noch nicht vorhanden:
        new File(getCmdLinePath()).getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(getCmdLinePath())) {
            pw.println("#!/bin/bash");
            pw.println("java ${JAKE_LAUNCHER_EXTRA_ARGS} -client -jar " + PropertiesProvider.getThisPath() + " \"$@\"");
        } catch (Exception ignored) {
        }
        new File(getCmdLinePath()).setExecutable(true, false);

        if (!new File(getCmdLinePath()).canExecute()) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Die Installation scheint fehlgeschlagen zu sein! (CmdLineInstall)", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return new String[] { "inject_path_extend", "Erweitere Path-Variable" };
    }

    public static String[] fkt_inject_path_extend(String s) {

        /*
            Generate the autocomplete script
        */
        Paths.get(HOME, ".local", "bash_completition").toFile().mkdirs(); // Anlegen des Verzeichnisses
        String autocompletePath = Paths.get(HOME, ".local", "bash_completition","_jake.complete").toString();
        try {
            Cloner.cloneFileRessource("/configs/jake_autocomplete.complete", autocompletePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        // source $(shell pwd)/_jake_autocomplete
        for (String f : shells) {
            File file = new File(f);
            JakeLogger.writeLoggerDebug3("Checking: " + file.getAbsolutePath(),"LinuxIns");

            if (file.canWrite()) {
                try {
                    String[] lines = Files.lines(Paths.get(file.getAbsolutePath())).filter(x -> !x.contains("# JAVA_JAKE")).toArray(String[]::new); // contains '# Java_Jake'
                    //lines.forEachOrdered(x-> System.out.println(x));
                    // Da die Datei überschrieben wird, scheitert die Stream-Operation danach
                    JakeLogger.writeLoggerInfo("Prepping: " + file.getAbsolutePath(),"LinuxIns");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file)); //, true
                    for (String l : lines)
                        pw.println(l);
                    pw.format("export PATH=$PATH:%s; %s%s%s # JAVA_JAKE%n",Paths.get(HOME, ".local", "bin").toString(),
                    (!CoreSettings.requestValue("S_PATH").equals("./")?"export LILLY_JAKE_CONFIG_PATH=\"" + CoreSettings.requestValue("S_PATH") +"\";":""), // wenn angegeben, dann übernommen
                    (f.contains(".zshrc")?"autoload bashcompinit &>/dev/null; bashcompinit &>/dev/null;":""),
                    "source " + autocompletePath);
                    pw.close();
                } catch (Exception ignored) {}
            }
        }

        return END;
    }

    /**
     * Validiert, ob alles korrekt installiert wurde
     *
     * @return true, wenn ja, sonst false
     */
    @Override
    public boolean validate() {
        return /* (new File(getDesktopPath()).canRead() || PropertiesProvider.isHeadless()) && */ new File(getCmdLinePath()).canExecute();
    }


    /**
     * Deinstalliert die Jake
     */
    @Override
    public boolean uninstall() {
        JakeLogger.writeLoggerInfo("Deinstalliere Jake (Linux)", "Uninst");

        // Entferne Eintrag in Shellrc:
        for (String f : shells) {
            File file = new File(f);
            JakeLogger.writeLoggerDebug3("Checking: " + file.getAbsolutePath(),"Uninst");

            if (file.canWrite()) {
                try {
                    String[] lines = Files.lines(Paths.get(file.getAbsolutePath())).filter(x -> !x.contains("# JAVA_JAKE")).toArray(String[]::new); // contains '# Java_Jake'
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                    for (String l : lines)
                        pw.println(l);
                    pw.close();
                    JakeLogger.writeLoggerDebug3("Removed injection","Uninst");
                } catch (Exception ignored) {

                }
            }
        }
        // Entferne launch-skript
        if(! new File(getCmdLinePath()).canRead()){
            JakeLogger.writeLoggerWarning("Die Datei: " + getCmdLinePath() + " kann nicht gefunden werden", "Uninst");
        } else {
            new File(getCmdLinePath()).delete();
            JakeLogger.writeLoggerInfo("Die Datei: " + getCmdLinePath() + " wurde entfernt", "Uninst");
        }

        // Entferne complete-skript (bash), das kann man besser machen, aber copy paste ist lustig
        String ptp = Paths.get(HOME, ".local", "bash_completition","_jake.complete").toString();
        if(! new File(ptp).canRead()){
            JakeLogger.writeLoggerWarning("Die Datei: " + ptp + " kann nicht gefunden werden", "Uninst");
        } else {
            new File(ptp).delete();
            JakeLogger.writeLoggerInfo("Die Datei: " + ptp + " wurde entfernt", "Uninst");
        }
        JakeWriter.out.println("Jake wurde deinstalliert, LOG-Datei dieser Sitzung: \"" + JakeLogger.getTarget() + "\"");
        return true;
    }

}
