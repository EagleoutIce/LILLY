package de.eagle.lillyjakeframework.core;

import de.eagle.lillyjakeframework.translators.SettingsTranslator;
import de.eagle.util.blueprints.Translator;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.*;
import de.eagle.util.enumerations.eSetting_Type;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static de.eagle.lillyjakeframework.core.Definitions.JAKE_VERSION;
import static de.eagle.util.logging.JakeLogger.writeLoggerInfo;

public class CoreSettings {

    public static Settings settings = null;

    private static SettingsTranslator translator = null;

    /**
     * @return die enthaltenen Einstellungen
     */
    public static Settings getSettings() {
        if (settings == null)
            init();
        return settings;
    }

    /**
     * Liefert den verwendeten Translator
     *
     * @return der verwendete Translator
     */
    public static SettingsTranslator getTranslator() {
        if (translator == null) {
            try {
                translator = new SettingsTranslator("CoreTranslator", "/translators/CoreSettings.trans");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return translator;
    }

    /**
     * Initialisiert die Einstellungen mit den entsprechendne Standarts
     *
     * @return true, wenn eine die Einstellungen generiert werden mussten.
     */
    public static boolean init() {
        if (settings != null) {
            writeLoggerInfo("Es wurde unnötiger weise CoreSettings::Init aufgerufen!", "CoreS");
            // Dies könnte nicht gewollt sein
            return false;
        }
        writeLoggerInfo("Es werden neue Einstellungen für CoreSettings generiert (init)", "CoreS");
        try {
            SettingsTranslator st = new SettingsTranslator("CoreSettings");
            settings = new Settings("<Jake> CoreSettings", true, new HashMap<>());

            settings.emplace(st, "S_VERSION", "Jake-Version", eSetting_Type.IS_TEXT, JAKE_VERSION);

            // Hier wurden schon einemal die für Buildrues wichtigen Einstellungen
            // eingefügt:

            settings.emplace(st, "S_LILLY_OUT", "Ausgabeordner der PDF-DATEI", eSetting_Type.IS_PATH, "./OUTPUT/");

            settings.emplace(st, "S_LILLY_NAMEPREFIX", "Standart-Namenspräfixs", eSetting_Type.IS_TEXT, "");
            settings.emplace(st, "S_LILLY_COMPLETE_NAME", "Präfix einer Complete-Version", eSetting_Type.IS_TEXT,
                    "COMPLETE-");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // -- Hier die anderen @SerpentSaviour

        return true;
    }

    /**
     * Platziert eine neue Einstellung mithilfe eines Translators
     *
     * @param key   der entsprechende Schlüssel
     * @param brief Erklärung zu Einstellung
     * @param type  Der Typ der Einstellung
     * @param init  Der initiale wert
     *
     * @see SettingDeskriptor#create(String, String, eSetting_Type, Serializable)
     *
     * @return das alte Objekt, wenn vorhanden
     */
    public static SettingDeskriptor<String> emplace(String key, String brief, eSetting_Type type, String init) {
        return settings.emplace(translator, key, brief, type, init);
    }

    /**
     * Platziert eine neue Einstellung mithilfe eines Translators
     *
     * @param key         der entsprechende Schlüssel
     * @param brief       Erklärung zu Einstellung
     * @param type        Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     *
     * @see SettingDeskriptor#create(String, String, eSetting_Type, Serializable)
     *
     * @return das alte Objekt, wenn vorhanden
     */
    public static SettingDeskriptor<String> emplace(String key, String brief, eSetting_Type type, boolean isMandatory) {
        return settings.emplace(translator, key, brief, type, isMandatory);
    }

    /**
     * Liefert eine Einstellung auf Basis eines mithilfe des
     * {@link SettingsTranslator} übersetzen Keys
     *
     * @see Settings#requestValue(Translator, Object)
     *
     * @param key der Key
     * @return den entsprechenden Wert
     */
    public static String requestValue(String key) {
        return settings.requestValue(translator, key);
    }

    /**
     * Expandiert alle Einstellungen in dieser Zeichenfolge. Es wird gesucht nach:
     * $<Settings> und dementsprechend expandiert. So wird zum Beispiel $Version zu
     * {@value Definitions#JAKE_VERSION}
     *
     * @note Bitte erweitern: CoreSettingsTest>_test_expand_basics
     *
     * @param str der zu expandierende String
     * @return der expandierte string
     */
    public static String expand(String str) {
        init();
        for (Map.Entry<String, SettingDeskriptor<String>> s : settings) {
            str = str.replaceAll("\\$" + Pattern.quote(s.getKey()), s.getValue().getValue());
        }
        return str;
    }

    /**
     *
     */
    public static String get(String key) {
        init();
        return settings.getValue(key);
    }

    public static boolean set(String key, String val) {
        init();
        return settings.set(key, val);
    }

}
