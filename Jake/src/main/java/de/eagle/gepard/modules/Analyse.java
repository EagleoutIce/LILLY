package de.eagle.gepard.modules;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Analyse {
    /**
     * Name der NameMap-Box
     */
    public static final String box_name = "analyse";

    /**
     * @return die Blaupause für die Einstellungen
     */
    public static Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Analyse");
            // ---- Mandatories
            blueprint.put("name",
                    SettingDeskriptor.create("name", "Bezeichner des Fehlers", eSetting_Type.IS_TEXT, true));
            blueprint.put("type", SettingDeskriptor.create("type",
                    "Art und Weise, wie der im Log gefunden wird:", eSetting_Type.IS_TEXT, true));
            blueprint.put("pattern", SettingDeskriptor.create("pattern", "Pattern, das nach 'type' gesucht wird ",
                    eSetting_Type.IS_TEXT, true));
            blueprint.put("brief", SettingDeskriptor.create("brief", "Kurzbeschreibung für den Fehler",
                    eSetting_Type.IS_TEXT, true));

            blueprint.put("is-fatal", SettingDeskriptor.create("is-fatal", "Ist der Fehler fatal",
                    eSetting_Type.IS_SWITCH, true));
            blueprint.put("max-err-lines", SettingDeskriptor.create("max-err-lines", "wie viele Fehler sollen ausgegeben werden",
                    eSetting_Type.IS_NUM, true));
            blueprint.put("line-pattern", SettingDeskriptor.create("line-pattern", "TBD",
                    eSetting_Type.IS_TEXT, true));
            blueprint.put("peek-doc",SettingDeskriptor.create("peek-doc","TBD",
                    eSetting_Type.IS_SWITCH,true));
            blueprint.put("hot-fix",SettingDeskriptor.create("hot-fix","soll der Fehler gefixt werden",
                    eSetting_Type.IS_SWITCH,true));

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
    public static Settings getDefaults() {
        Settings settings = new Settings("<Default> Analyse", true, new HashMap<>());
        settings.emplace("default", "default", eSetting_Type.IS_TEXT,);

        return settings;
    }

    /**
     * Parst eine einzelne Box
     *
     * @param box      die Box
     *
     * @return null, wenn es keine buildrule-Box war, sonst die entsprechende
     *         Einstellung
     */
    public static Settings parseBox(GeneratorParser.JObject box) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Analyse");

        Settings settings = new Settings(box.getName());


        return settings;
    }
}
