package de.eagle.gepard.modules;

/**
 * @file Hooks.java
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * @brief Gepardmodul welches Hooks zur Verfügung stellt.
 * @see de.eagle.gepard.modules.AbstractGepardModule
 * @see de.eagle.gepard.parser.GeneratorParser
 */

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;

// Information:
// Alle "Entspricht ..." Kommentare gilt es zu ändern!

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Hooks extends AbstractGepardModule {
    /**
     * Name der NameMap-Box
     */
    public static final String box_name = "hook";

    private static Hooks tInstance = null;

    public static Hooks getInstance() {
        if(tInstance == null)
            tInstance = new Hooks();
        return tInstance;
    }

    /**
     * Konstruiert das GepardModul
     */
    private Hooks() {
        super(box_name);
    }

    /**
     * @return die Blaupause für die Einstellungen
     */
    public Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Hooks", true, new HashMap<>());
            // ---- Mandatories
            blueprint.put("name",
                    SettingDeskriptor.create("name", "Interner Name der Hook", eSetting_Type.IS_TEXT, true));
            blueprint.put("type",
                    SettingDeskriptor.create("type", "Hook-Typ (PRE, IN#, POST, ALL)", eSetting_Type.IS_TEXT, true));

            // ---- Optionals
            blueprint.put("body", SettingDeskriptor.create("body", "Inhalt der Hook", eSetting_Type.IS_SPECIAL, false));
            blueprint.put("on-success", SettingDeskriptor.create("on-success", "Ausgabe im Falle eines Erfolges",
                    eSetting_Type.IS_TEXT, false));
            blueprint.put("on-failure", SettingDeskriptor.create("on-failure",
                    "Ausgabe im Falle eines Scheiterns/Misserfolgs   ", eSetting_Type.IS_TEXT, false));
        }
        return blueprint;
    }

    /**
     * Entspricht hooks Settings
     */
    private static Settings blueprint = null;

    /**
     * Entspricht get_default_hooks
     *
     * @return Standardeinstellungen
     */
    public Settings getDefaults() {
        Settings settings = new Settings("<Default> Hooks", true, new HashMap<>());

        // Da Lilly nun in der Lage ist die Bibtex-Integration vollautomatisch zu
        // verwalten
        // entfällt die Integration der Bibtex-Hook:
        /*
         * if (settings[S_LILLY_SHOW_BOX_NAME]=="true") return {{"IN1:Bibtex-Compile",
         * "(bibtex $(basename ${1}`cat /tmp/lillytmp.bib.p`-${2}) >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1) && echo SUCCESS || echo FAILURE"
         * }}; else return {{"IN1:Bibtex-Compile",
         * "(bibtex $(basename ${1}${2}) >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1) && echo SUCCESS || echo FAILURE"
         * }};
         */

        /* cSpell:disable */
        int max = Integer.parseInt(CoreSettings.requestValue("S_LILLY_COMPILETIMES"));
        for (int i = 0; i < max; i++) {
            settings.emplace("IN" + i + ":compile-" + i, "IN-Hook für Iteration: " + i, eSetting_Type.IS_SPECIAL,
                    "echo Kompiliere " + (i + 1) + "/" + max + " für: $(FINALNAME)");
        }
        if (CoreSettings.requestSwitch("S_DEBUG"))
            settings.emplace("PRE:debug", "Debug-Hook, liefert verwendete Log-Datei", eSetting_Type.IS_SPECIAL,
                    "echo Verwende die Log-Datei: $(LOGFILE)");
        /* cSpell:enable */
        return settings;
    }


    /**
     * Bearbeitet eine komplette Datei
     *
     * @param rulefiles die Liste der rulefiles
     * @return Einstellungen bei der die Hooks konfiguriert sind
     * @throws IOException Im Falle eines Fails von
     *                     {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public Settings getHooks(String rulefiles) throws IOException {
        if (rulefiles.isEmpty())
            return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        return parseRules(gp.parseFile(Hooks.box_name, getBlueprint(), add_unknown), false);
    }

    /**
     * Parst eine einzelne Box
     *
     * @param box die Box
     * @param ignored Wird hier ignoriert.
     *
     * @return null, wenn es keine NameMap-Box war, sonst die entsprechende
     *         Einstellung
     */
    public Settings parseBox(GeneratorParser.JObject box, boolean ignored) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Hooks");

        // Teste, ob konfigurierter TYPE gültig ist:
        String t = box.config.getValue("type");
        if (!(t.equals("PRE") || t.equals("POST") || t.equals("ALL") || (t.startsWith("IN") && t.length() > 2))) {
            throw new RuntimeException(
                    "Für eine Hook sind für \"type\" nur die konfigurationen 'IN#' (wobei # hier eine Zahl entsprechend des jeweiligen Kompilierdurchgangs ist), 'PRE', 'POST' und 'ALL' zulässig!");
        }

        Settings settings = new Settings(box.getName());

        StringBuilder key = new StringBuilder();
        key.append(t).append(":").append(box.config.getValue("name"));

        StringBuilder body = new StringBuilder();
        body.append("(").append(box.config.getValue("body")).append(") > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo [")
                .append(box.config.getValue("on-success")).append("] || echo [")
                .append(box.config.getValue("on-failure")).append("]");

        settings.put(key.toString(), SettingDeskriptor.create("", "Hooks für Konfiguration (Gepard)", body.toString()));

        return settings;
    }

    public Settings getTagged(Settings rules, String tag) {
        Settings settings = new Settings("Tagged Hooks-Settings");
        for (var s : rules) {
            String k = s.getKey();
            if (k.contains(":")) {
                String sk = k.substring(0, k.indexOf(":"));
                if (sk.equals(tag)) {
                    settings.put(k.substring(k.indexOf(":") + 1), SettingDeskriptor.create("",
                            "Hook, gefiltert durch entsprechendes tag", s.getValue().getValue()));
                }
            }
        }
        return settings;
    }

    /**
     * Führt eine einzelne Hook aus
     *
     * @param hook Die auszuführende Hook (standartmäßig bash, TODO: sollte sich ans
     *             System anpassen)
     *
     * @return den entsprechenden ReturnStatus
     */
    public ReturnStatus executeHook(String hook) {
        Process p;
        int ret = -1;
        try {
            p = Runtime.getRuntime().exec(new String[] { "bash", "-c", hook });
            JakeLogger.writeLoggerDebug3("Hook Execute of [\"" + hook + "\"] returned: " + (ret = p.waitFor()),
                    "compile");
            new BufferedReader(new InputStreamReader(p.getInputStream())).lines()
                    .forEachOrdered(x -> JakeWriter.out.println(x));
        } catch (Exception ignored) {
        }
        return new ReturnStatus(ret);
    }

    // Write Hooks removed, bc no makefile support

    /**
     * Führt alle Hooks, die tag oder ALL entsprechen aus
     *
     * @param t_rules die Regeln auf deren Basis die hooks gesucht werden sollen
     * @param tag     der Tag der gesucht werden soll
     *
     * @return {@link ReturnStatus#EXIT_FAILURE} wenn der Stream beschädigt ist.
     */
    public ReturnStatus executeHooks(Settings t_rules, String tag) {
        Settings rules = t_rules.cloneSettings();
        try {
            Expandables.getInstance().expandSettings(rules);
        } catch (IOException ignored) {
        }
        for (var sd : rules) {
            String k = sd.getKey();
            if (k.contains(":")) {
                String rtag = k.substring(0, k.indexOf(":"));
                String rnam = k.substring(k.indexOf(":") + 1);
                if (rtag.equals(tag) || rtag.equals("ALL")) { // Tag gefunden
                    writeLoggerDebug1(
                            "Führe die " + rtag + "-Hook: \"" + rnam + "\" mit Body: + \"" + sd.getValue() + "\" aus!",
                            "Hooks");
                    writeLoggerDebug1("Execute: " + executeHook("echo \"" + ColorConstants.STY_PARAM + "Lilly " + rtag
                            + "-Hook[" + rnam + "] für " + tag + " evaluiert zu: $(" + sd.getValue().getValue() + ")"
                            + ColorConstants.COL_RESET + "\""), "Hooks");
                }
            }
        }
        return ReturnStatus.EXIT_SUCCESS;
    }

}
