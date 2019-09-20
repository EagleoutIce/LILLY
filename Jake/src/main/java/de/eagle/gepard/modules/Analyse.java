package de.eagle.gepard.modules;

/**
 * @file Analyse.java
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * @brief Gepardmodul welches sich um die Analyse von Logfiles kümmert
 * @see de.eagle.gepard.modules.AbstractGepardModule
 * @see de.eagle.gepard.parser.GeneratorParser
 */

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.SettingDeskriptorStringList;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.io.JakeWriter;

import java.util.HashMap;
import java.util.regex.Pattern;

import static de.eagle.lillyjakeframework.core.Definitions.S_FALSE;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Analyse extends AbstractGepardModule {
    /**
     * Name der NameMap-Box
     */
    public static final String box_name = "analyse";

    private static Analyse tInstance = null;

    public static Analyse getInstance() {
        if(tInstance == null)
            tInstance = new Analyse();
        return tInstance;
    }

    /**
     * Konstruiert das GepardModul
     */
    private Analyse() {
        super(box_name);
    }

    public String[] analyse(String[] file_lines, Settings boxes) {
        // Current point of todo :D
        return null;
    };


    /**
     * @return die Blaupause für die Einstellungen
     */
    @Override
    public Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Analyse");
            // ---- Mandatories
            blueprint.put("name",
                    SettingDeskriptor.create("name", "Bezeichner des Fehlers", eSetting_Type.IS_TEXT, true));
            blueprint.put("pattern", SettingDeskriptor.create("pattern", "Pattern, das nach 'type' gesucht wird ",
                    eSetting_Type.IS_TEXT, true));
            blueprint.put("brief", SettingDeskriptor.create("brief", "Kurzbeschreibung für den Fehler",
                    eSetting_Type.IS_TEXT, true));

            // ---- optionals
            blueprint.put("type", SettingDeskriptor.create("type",
                    "Art und Weise, wie der im Log gefunden wird:", eSetting_Type.IS_TEXT, false, "REGEX")); //
            blueprint.put("is-fatal", SettingDeskriptor.create("is-fatal", "Ist der Fehler fatal",
                    eSetting_Type.IS_SWITCH, false, S_FALSE));
            blueprint.put("max-err-lines", SettingDeskriptor.create("max-err-lines", "wie viele Fehler sollen ausgegeben werden",
                    eSetting_Type.IS_NUM, false, "10"));
            blueprint.put("line-pattern", SettingDeskriptor.create("line-pattern", "TBD",
                    eSetting_Type.IS_TEXT, false, "l\\.\\d+"));
            blueprint.put("peek-doc",SettingDeskriptor.create("peek-doc","TBD",
                    eSetting_Type.IS_SWITCH,false, "false"));
            blueprint.put("hot-fix",SettingDeskriptor.create("hot-fix","soll der Fehler gefixt werden",
                    eSetting_Type.IS_SWITCH,false, "false"));

        }
        return blueprint;
    }
    /**
     * Entspricht expandables Settings
     */
    private static Settings blueprint = null;

    /**
     * Entspricht get_default_expandables
     *
     * @return Standardeinstellungen
     */
    @Override
    public Settings getDefaults() {
        //settings.emplace("default", "default", eSetting_Type.IS_TEXT,);
        return new Settings("<Default> Analyse", true, new HashMap<>());
    }


    /**
     * Parst eine einzelne Box
     *
     * @param box      die Box
     * @param ignored Wird hier nicht berücksichtigt.
     *
     * @return null, wenn es keine buildrule-Box war, sonst die entsprechende
     *         Einstellung
     */
    @Override
    public Settings parseBox(GeneratorParser.JObject box, boolean ignored) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Analyse");

        Settings settings = new Settings(box.getName());
        //Muster:
        //Key: Pattern. alle Patternarten werden entsprechend in ein Regex-Pattern umgewandelt :D
        //Value: [!] Fatal
        //       Name:Brief:[rest]
        String settings_name = "";
        String pattern = box.config.getValue("pattern");
        switch (box.config.getValue("type")) {
            case "REGEX":
                settings_name = pattern; break;
            case "PLAIN":
                settings_name = "^" + Pattern.quote(pattern) + "$"; break;
            case "PREFIX":
                settings_name = "^" + Pattern.quote(pattern); break;
            case "SUFFIX":
                settings_name = Pattern.quote(pattern) + "$"; break;
            case "CONTAINS":
                settings_name = Pattern.quote(pattern); break;
            default:
                JakeWriter.err.println("Der Boxtyp: " + box.config.getValue("type") + "ist nicht gültig. Akzeptiert werden: REGEX, PLAIN, PREFIX, SUFFIX und CONTAINS");
                throw new RuntimeException("Der Boxtyp: " + box.config.getValue("type") + "ist nicht gültig. Akzeptiert werden: REGEX, PLAIN, PREFIX, SUFFIX und CONTAINS");
        }
        settings.put(settings_name,
                SettingDeskriptor.create("", "List of values", eSetting_Type.IS_TEXTLIST, box.config.getValue("name")));

        for(String d : new String[]{"brief", "is-fatal", "max-err-lines", "line-pattern", "peek-doc", "hot-fix"}) {
            ((SettingDeskriptorStringList)settings.get(settings_name)).addValue(box.config.getValue(d));
        }
        return settings;
    }
}
