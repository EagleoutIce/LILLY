package de.eagle.lillyjakeframework.core;

/**
 * @file CoreSettings.java
 * @author Florian Sihler
 * @version 1.0.9
 *
 * @since 2.0.0
 *
 * @brief enthält die Dokumentübergreifenden Einstellungen.
 */

import de.eagle.gepard.modules.Expandables;
import de.eagle.lillyjakeframework.translators.SettingsTranslator;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.helper.PropertiesProvider;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;


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

    public static Settings expandables = null;

    /**
     * Initialisiert die Einstellungen mit den entsprechendne Standards
     *
     * @return true, wenn eine die Einstellungen generiert werden mussten.
     */
    public static boolean init() {
        if (settings != null) {
            // writeLoggerInfo("Es wurde unnötiger weise CoreSettings::Init aufgerufen!",
            // "CoreS");
            // Dies könnte nicht gewollt sein
            return false;
        }
        writeLoggerInfo("Es werden neue Einstellungen für CoreSettings generiert (init)", "CoreS");
        SettingsTranslator st = getTranslator();
        settings = new Settings("<Jake> CoreSettings", true, new HashMap<>());

        settings.emplace(st, "S_VERSION", "Jake-Version", eSetting_Type.IS_TEXT, "@[JAKEVER]");

        // Hier wurden schon einemal die für Buildrues wichtigen Einstellungen
        // eingefügt:lilly

        switch (PropertiesProvider.getOS()) {
        case LINUX:
            settings.emplace(st, "S_INSTALL_PATH", "Wohin gilt es Lilly zu installieren?", eSetting_Type.IS_PATH,
                    "\"${HOME}/texmf\"");
            settings.emplace(st, "S_LILLY_PATH", "Wo liegt die Lilly.cls?", eSetting_Type.IS_PATH,
                    "\"$(dirname $(locate -e -q 'LILLY/Lilly/Lilly.cls' | grep -v -e \".Trash\" -e \"classes\" -e \".vim\" -i -e \"backup\" | head -1))\"");
            break;
        case MAC:
            settings.emplace(st, "S_INSTALL_PATH", "Wohin gilt es Lilly zu installieren?", eSetting_Type.IS_PATH,
                    "\"${HOME}/Library/texmf\"");
            settings.emplace(st, "S_LILLY_PATH", "Wo liegt die Lilly.cls?", eSetting_Type.IS_PATH,
                    "\"$(dirname $(mdfind Lilly.cls | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\"");
            break;
        case WINDOWS:
            settings.emplace(st, "S_INSTALL_PATH", "Wohin gilt es Lilly zu installieren?", eSetting_Type.IS_PATH,
                    "ERR:NOT SUPPORTED YET"); // TODO Windows pfade
            settings.emplace(st, "S_LILLY_PATH", "Wo liegt die Lilly.cls?", eSetting_Type.IS_PATH,
                    "ERR: NOT SUPPORTED YET");
            break;
        }

        settings.emplace(st, "S_LILLY_OUT", "Ausgabeordner der PDF-DATEI", eSetting_Type.IS_PATH, "./$(BASENAME)-OUT/");
        settings.emplace(st, "S_LILLY_NAMEPREFIX", "Standard-Namenspräfixs", eSetting_Type.IS_TEXT, "");
        settings.emplace(st, "S_LILLY_COMPLETE_NAME", "Präfix einer Complete-Version", eSetting_Type.IS_TEXT,
                "COMPLETE-");
        settings.emplace(st, "S_LILLY_PRINT_NAME", "Präfix einer Druck-Version", eSetting_Type.IS_TEXT, "PRINT-");
        settings.emplace(st, "S_LILLY_DOCTYPE", "Dokumenttyp", eSetting_Type.IS_TEXT, "Mitschrieb");
        settings.emplace(st, "S_LILLY_EXTERNAL_OUT", "Output-Ordner für tikzternal", eSetting_Type.IS_PATH, "extimg");
        settings.emplace(st, "S_LILLY_MODES", "Modi für den Kompiliervorgang", eSetting_Type.IS_TEXTLIST, "default");
        settings.emplace(st, "S_LILLY_BOXES", "Boxen für den Kompiliervorgang", eSetting_Type.IS_TEXTLIST, "DEFAULT");

        // S_LILLY_COMPRESS
        settings.emplace(st, "S_LILLY_COMPRESS", "Soll anschließend die PDF komprimiert werden?",
                eSetting_Type.IS_SWITCH, "false");
        settings.emplace(st, "S_LILLY_COMPRESS_TARGET", "Komprimierungsziel", eSetting_Type.IS_TEXT, "screen");

        // S_LILLY_AUTOCLEAN
        settings.emplace(st, "S_LILLY_AUTOCLEAN", "Sollen beim Arbeiten entstandene Dateien am Ende entfernt werden?",
                eSetting_Type.IS_SWITCH, "true");

        settings.emplace(st, "S_AUTOCONF",
                "Erlaubt es eine gleichnamige .conf Datei oder jake.conf zu verwenden sofern diese existiert.",
                eSetting_Type.IS_SWITCH, "false");

        // S_LILLY_KEEPS
        settings.emplace(st, "S_LILLY_KEEPS", "Welche Dateien sollen auf Basis von Autoclean behalten werden?",
                eSetting_Type.IS_TEXTLIST, "pdf tex conf config md txt sty cls png jpg jpeg gif svg sh gpd");
        settings.emplace(st, "S_LILLY_EXTERNAL", "Sollen tikzternal-Grafiken ausgelagert werden?",
                eSetting_Type.IS_SWITCH, "false");
        settings.emplace(st, "S_LILLY_COMPLETE", "Sollen die Varianten vollständig erstellt werden?",
                eSetting_Type.IS_SWITCH, "true");
        // S_GEPARDRULES_PATH
        settings.emplace(st, "S_GEPARDRULES_PATH", "Pfade für zusätzliche Regeln", eSetting_Type.IS_TEXT, ""); // TESTLIST?

        settings.emplace(st, "S_COMMENT_PATTERN", "Muster eines Kommentars", eSetting_Type.IS_TEXT, "![^!]*!");

        settings.emplace(st, "S_LILLY_IN", "Input-Pfad für Dateien", eSetting_Type.IS_PATH, "./");
        // S_LILLY_COMPILETIMES
        settings.emplace(st, "S_LILLY_COMPILETIMES", "Wie oft soll kompiliert werden", eSetting_Type.IS_NUM, "2"); // TESTLIST?
                                                                                                                   // WITH
                                                                                                                   // ':'
                                                                                                                   // TODO!
        // TODO: wird mit analyze automatisch erklärt

        // S_LILLY_SHOW_BOX_NAME
        settings.emplace(st, "S_LILLY_SHOW_BOX_NAME", "Soll der Boxname angezeigt werden?", eSetting_Type.IS_SWITCH,
                "true");

        // besondere Lilly-Einstellungen
        settings.emplace(st, "S_LILLY_VORLESUNG", "Um welche Vorlesung handelt es sich", eSetting_Type.IS_VLS, "NONE");

        settings.emplace(st, "S_LILLY_CONFIGS_PATH", "Pfad zu den Lilly-Konfigurationen", eSetting_Type.IS_PATH, "");
        settings.emplace(st, "S_LILLY_DATA_PATH", "Pfad zu generellen Daten wie Boxen...", eSetting_Type.IS_PATH, "");

        settings.emplace(st, "S_LILLY_N", "Um das wievielte Übungsblatt handelt es sich", eSetting_Type.IS_NUM, "42");
        settings.emplace(st, "S_LILLY_SEMESTER", "Das  wievielte Semester ist es", eSetting_Type.IS_NUM, "0");
        settings.emplace(st, "S_LILLY_EXTERNAL", "Soll versucht werden tikzternal Grafiken auszulagern?",
                eSetting_Type.IS_SWITCH, "false");
        settings.emplace(st, "S_LILLY_LAYOUT_LOADER", "Pfad über den Layouts geladen werden können",
                eSetting_Type.IS_PATH, "");

        settings.emplace(st, "S_LILLY_AUTHOR", "Wer ist der Autor des Dokuments", eSetting_Type.IS_TEXT,
                "Florian Sihler");
        settings.emplace(st, "S_LILLY_AUTHORMAIL", "E-mail adresse des Autors", eSetting_Type.IS_TEXT,
                "florian.sihler@uni-ulm.de");
        settings.emplace(st, "S_LILLY_BIBTEX", "Angabe der . bib Datei !OHNE ENDUNG!.", eSetting_Type.IS_TEXT, ""); // include
                                                                                                                    // check,
                                                                                                                    // if
                                                                                                                    // bibtex
                                                                                                                    // required
        settings.emplace(st, "S_LILLY_SIGNATURE_COLOR",
                "Welche Highlighting Farbe soll verwendet werden, per Hcolor (und HBcolor)", eSetting_Type.IS_LATEX,
                "Leaf");

        // Generelle Einstellungen
        settings.emplace(st, "S_OPERATION", "Was soll getan werden?", eSetting_Type.IS_OPERATION, "help");
        settings.emplace(st, "S_FILE", "Wie soll die Datei heißen?",
                eSetting_Type.IS_TEXT /* Da nicht wirklich existent, kann auch erstellt werden! */, "dummy.tex");
        settings.emplace(st, "S_DEBUG", "Sollen Meldungen ausgegeben werden?", eSetting_Type.IS_SWITCH, "false");
        settings.emplace(st, "S_DEBUG_FILTER", "Regex-Expression welche Debug-Nachrichten anzuzeigen sind!",
                eSetting_Type.IS_TEXT, ".*");
        settings.emplace(st, "S_PATH", "Zum Beispiel: Pfad zu LILLY", eSetting_Type.IS_PATH, "./");
        settings.emplace(st, "S_WHAT", "Spezifikator, um was gehts?", eSetting_Type.IS_TEXT, "");
        settings.emplace(st, "S_ANSWER", "42", eSetting_Type.IS_TEXT, "");

        // Makefile Regeln
        settings.emplace(st, "S_JOBCOUNT", "Wie viele Jobs für multithreaded-Kompilieren", eSetting_Type.IS_NUM, "2");

        settings.emplace(st, "S_ERRORCOUNT", "Wie viele Fehler sollen maximal angezeigt werden?", eSetting_Type.IS_NUM,
                "5");

        // Makefile Generierung
        settings.emplace(st, "S_MK_NAME", "Welchen Namen soll das Makefile haben", eSetting_Type.IS_TEXT, "Makefile");
        settings.emplace(st, "S_MK_PATH", "Wohin soll das Makefile", eSetting_Type.IS_PATH, "./");
        settings.emplace(st, "S_MK_USE", "Soll ein Makefile erstellt werden", eSetting_Type.IS_SWITCH, "false");
        try {
            expandables = Expandables.getInstance().getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        } catch (IOException ignored) {
        }
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
     * Will take the Key and pass it through the Translator before assigning
     *
     * @param key   the key (will be translated)
     * @param value the value (won't be translated)
     * @return {@link de.eagle.util.blueprints.AbstractSettings#set(Serializable, Serializable)}
     */
    public static boolean assignValue(String key, String value) {
        return settings.set(getTranslator().translate(key), value);
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
        String s = settings.requestValue(translator, key);
        return (s == null) ? "" : s;
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
     * Expandiert über einen entsprechenden Expandable sowie über das
     * {@see #expand(String)}
     *
     * @param str der zu expandierende String
     * @return das expandierte Ergebnis
     */
    public static String expandFull(String str) {
        init();
        return Expandables.expand(expandables, expand(str));
    }

    /**
     * Fragt einen Switch auf Wert ab
     *
     * @param key der Name des Switches
     *
     * @implNote Wirft keinen fehler, wenn der Switch kein Switch ist!
     * @return den booleschen Wert der jeweiligen Einstellung
     */
    public static boolean requestSwitch(String key) {
        init();
        return settings.requestSwitch(getTranslator(), key);
    }

    /**
     * Liefert den Wert einer Einstellung
     *
     * @param key Der Schlüssel desssen Wert geliefert werden soll
     *            {@link Settings#getValue(String)}
     *
     * @return den Wert der entsprechenden Einstellung
     */
    public static String get(String key) {
        init();
        return settings.getValue(key);
    }

    /**
     * Setzt den Wert einer Einstellung
     *
     * @param key Der Schlüssel der Einstellung die es zu setzen gilt
     * @param val Der neue Wert der Einstellung
     * @return {@link de.eagle.util.blueprints.AbstractSettings#set(Serializable, Serializable)}
     */
    public static boolean set(String key, String val) {
        init();
        return settings.set(key, val);
    }

    /**
     * Setzt den Wert einer Einstellung
     *
     * @param key Der Schlüssel der Einstellung die es zu setzen gilt
     * @param val Der neue Wert der Einstellung
     * @return {@link Settings#set(String, boolean)}
     */
    public static boolean set(String key, boolean val) {
        init();
        return settings.set(key, val);
    }
}
