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
import static de.eagle.lillyjakeframework.core.Definitions.S_TRUE;
import static de.eagle.util.io.JakeLogger.writeLoggerInfo;

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
     * Initialisiert die Einstellungen mit den entsprechendne Standards
     *
     * @return true, wenn eine die Einstellungen generiert werden mussten.
     */
    public static boolean init() {
        if (settings != null) {
            // writeLoggerInfo("Es wurde unnötiger weise CoreSettings::Init aufgerufen!", "CoreS");
            // Dies könnte nicht gewollt sein
            return false;
        }
        writeLoggerInfo("Es werden neue Einstellungen für CoreSettings generiert (init)", "CoreS");
        SettingsTranslator st = getTranslator();
        settings = new Settings("<Jake> CoreSettings", true, new HashMap<>());

        settings.emplace(st, "S_VERSION", "Jake-Version", eSetting_Type.IS_TEXT, JAKE_VERSION);

        // Hier wurden schon einemal die für Buildrues wichtigen Einstellungen
        // eingefügt:

        settings.emplace(st, "S_LILLY_OUT", "Ausgabeordner der PDF-DATEI", eSetting_Type.IS_PATH, "./OUTPUT/");

        settings.emplace(st, "S_LILLY_NAMEPREFIX", "Standard-Namenspräfixs", eSetting_Type.IS_TEXT, "");
        settings.emplace(st, "S_LILLY_COMPLETE_NAME", "Präfix einer Complete-Version", eSetting_Type.IS_TEXT,
                "COMPLETE-");
        settings.emplace(st, "S_DOCTYPE", "Dokumenttyp", eSetting_Type.IS_TEXT, "Mitschrieb");
        settings.emplace(st, "S_LILLY_EXTERNAL_OUT", "Output-Ordner für tikzternal", eSetting_Type.IS_PATH, "extimg");
        settings.emplace(st, "S_LILLY_MODES", "Modi für den Kompiliervorgang", eSetting_Type.IS_TEXTLIST, "default");
        settings.emplace(st, "S_LILLY_BOXES", "Boxen für den Kompiliervorgang", eSetting_Type.IS_TEXTLIST, "DEFAULT");

        // S_LILLY_AUTOCLEAN
        settings.emplace(st, "S_LILLY_AUTOCLEAN", "Sollen beim Arbeiten entstandene Dateien am Ende entfernt werden?", eSetting_Type.IS_SWITCH, "true");
        // S_LILLY_CLEANS
        settings.emplace(st, "S_LILLY_CLEANS", "Welche Dateien sollen auf Basis von Autoclean entfernt werden?", eSetting_Type.IS_TEXTLIST, "log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock LEMME SATZE ZSM UB TOP listing upa ilg TOPIC DEFS");
        // S_LILLY_EXTERNAL
        settings.emplace(st, "S_LILLY_EXTERNAL", "Sollen tikzternal-Grafiken ausgelagert werden?", eSetting_Type.IS_SWITCH, "false");
        settings.emplace(st, "S_FILE", "Wie soll die Datei heißen?", eSetting_Type.IS_FILE, "dummy.tex");
        settings.emplace(st, "S_LILLY_IN", "Input-Pfad für Dateien", eSetting_Type.IS_PATH, ".");
        settings.emplace(st, "S_LILLY_COMPLETE", "Sollen die Varianten vollständig erstellt werden?", eSetting_Type.IS_SWITCH, "true");
        // S_GEPARDRULES_PATH
        settings.emplace(st, "S_GEPARDRULES_PATH", "Pfade für zusätzliche Regeln", eSetting_Type.IS_TEXT, ""); // TESTLIST? WITH ':' TODO!
        // S_LILLY_COMPILETIMES
        settings.emplace(st, "S_LILLY_COMPILETIMES", "Wie oft soll kompiliert werden", eSetting_Type.IS_NUM, "2"); // TESTLIST? WITH ':' TODO!
        // TODO: wird mit analzye  automatisch erklärt

        // S_LILLY_SHOW_BOX_NAME
        settings.emplace(st, "S_LILLY_SHOW_BOX_NAME", "Soll der Boxname angezeigt werden?", eSetting_Type.IS_SWITCH, "true");

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
        init();
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
        return settings.expand(str);
    }

    /**
     * Fragt einen Switch auf Wert ab
     * @param key der Name des Switches
     *
     * @implNote Wirft keinen fehler, wenn der Switch kein Switch ist!
     * @return
     */
    public static boolean requestSwitch(String key){
        init();
        return settings.requestSwitch(getTranslator(), key);
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
