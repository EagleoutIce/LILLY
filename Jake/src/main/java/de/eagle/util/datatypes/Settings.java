package de.eagle.util.datatypes;

import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.blueprints.Translator;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.enumerations.eSetting_Type;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static de.eagle.lillyjakeframework.core.Definitions.S_FALSE;
import static de.eagle.lillyjakeframework.core.Definitions.S_TRUE;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug3;

/**
 * @file Settings.java
 * @author Florian Sihler
 * @version 2.0.0
 * @brief Die normalen Einstellungen für Jake und Co
 * @see Settings.SettingDeskriptorStringList
 * @see Settings.SettingDeskriptor
 * @see AbstractSettings
 */

public class Settings extends AbstractSettings<String, String> {
    private static final long serialVersionUID = 673856672235848508L;

    /**
     * Konstruiert die Einstellungen ohne irgendwelche Voreinstellungen
     *
     * @param name Name der Einstellungen
     */
    public Settings(String name) {
        this(name, true, null);
    }

    /**
     * Erstellt ein exemplarisches Einstellungsobjekt als Platzhalter, dieses Objket
     * sollte und muss durch Implementationen ersetzt werden!!!
     *
     *
     * @param name Name der Einstellung
     * @return eine neues Einstellungsobjekt mit entsprechendem namen
     */
    public static Settings createDummy(String name) {
        Settings s = new Settings("TestSettings");
        s.put("Hallo", SettingDeskriptor.create("Hallo", "Hallo Welt Einstellung"));
        s.put("TestMandatory",
                SettingDeskriptor.create("TestMandatory", "Verpflichtende Einstellung", eSetting_Type.IS_PATH, true));
        return s;
    }

    /**
     * Konstruiert die Einstellungen
     *
     * @param name          Name der Einstellungen
     * @param add_unknown   Wie soll mit unbekannten/neuen Einstellungen verfahren
     *                      werden?
     * @param init_settings Start-Einstellungen
     */
    public Settings(String name, boolean add_unknown, HashMap<String, SettingDeskriptor<String>> init_settings) {
        super(name, add_unknown, init_settings);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractSettings<String, String> clone() {
        return new Settings(this.getName(), this.getUnknownPolicy(),
                (HashMap<String, SettingDeskriptor<String>>) this._settings.clone());
    }

    /**
     * Erstellt neue Einstellungen auf der Basis des bestehenden Objekts
     * 
     * @return neue Einstellungen
     */
    @SuppressWarnings("unchecked")
    public Settings cloneSettings() {
        return new Settings(this.getName(), this.getUnknownPolicy(),
                (HashMap<String, SettingDeskriptor<String>>) this._settings.clone());
    }

    /**
     * Erweiterte eine Einstellung, sofern sie eine Liste ist
     *
     * @warning Im Falle eines nonexistenten Wertes wird das letzte Zeichen von
     *          new_value als separator gesetzt
     *
     * @param key       Bezeichner der Einstellung
     * @param new_value Neuer Wert der Einstellung
     * @return false, wenn die Einstellung gesperrt ist, oder es sich nicht um eine
     *         Liste handelt
     */
    public boolean add(String key, String new_value) {
        if (_settings.containsKey(key)) {
            SettingDeskriptor s = _settings.get(key);
            writeLoggerDebug3("Einstellung geladen: " + s, "Settings");
            if (!(s instanceof SettingDeskriptorStringList))
                return false;
            ((SettingDeskriptorStringList) s).addValue(new_value);
        } else if (add_unknown) {
            _settings.put(key, new SettingDeskriptorStringList(key.toString(), "Unknown Setting", false, new_value,
                    (new_value.isEmpty()) ? ' ' : new_value.charAt(new_value.length() - 1)));
        }
        return true;
    }

    public <T> boolean requestSwitch(Translator<T, String> ts, T key) {
        return this.requestValue(ts, key).equals(S_TRUE);
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
    public String expand(String str) {
        for (Map.Entry<String, SettingDeskriptor<String>> s : _settings.entrySet()) {
            str = str.replaceAll("\\$" + Pattern.quote(s.getKey()), s.getValue().getValue());
        }
        return str;
    }

    /**
     * Gibt die Einstellungen als String Array zurück
     *
     * @return String array, gefüllt mit allen Einstellungen in Settings
     * @throws IOException Wenn die angegebene Konfigurationsdatei nicht gefunden
     */
    public String[] dump() throws IOException {
        List<String> stringList = new LinkedList<>();
        if (this.get("file") != null && this.get("file").getValue().contains(".conf")) {
            writeLoggerDebug1("Da eine konfigurationsdatei angegeben wurde wird diese zuerst aufgelöst!",
                    "settings:dump");
            Configurator configurator = new Configurator(this.get("file").getValue());
            configurator.parse_settings(this, true);
        }
        try {
        Settings set = Expandables.getInstance().getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        this._settings.forEach((key,
                value) -> stringList.add(String.format("%s  %-25s: %s [%s]%s%s", ColorConstants.COL_RESET, key,
                        ColorConstants.STY_PARAM, value.getValue(),
                        (!(Expandables.expand(set, value.getValue())).equals(value.getValue()))
                                ? " => " + Expandables.expand(set, value.getValue())
                                : "",
                        ColorConstants.COL_RESET)));
        } catch (Exception ignore) {
            // Gepardfile not vaild/found? we will ignore for now
        }
        return stringList.toArray(new String[0]);
    }

    /**
     * Liefert den Wert einer gewissen Einstellung zurück - Kurzschreibweise mit
     * null check
     *
     * @param key der zu suchende Key
     * @return der gefundene Wert, null wenn nicht gefunden
     */
    @Override
    public String getValue(String key) {
        SettingDeskriptor<String> val = get(key);
        if (val != null && val.getValue() == null) {
            return "";
        } else if (val == null) {
            return null;
        }
        return val.getValue();
    }

    /**
     * Nachdem Java keine Methode anbietet um native zwei Hashmaps zu vergleichen
     * hier der Good-Enough ansatz :D
     *
     * @param set die zu vergleichende Einstellung
     * @return true wenn die Einstellungen identisch sind.
     *
     */
    protected boolean compareToSettings(HashMap<String, SettingDeskriptor<String>> set) {
        for (String s : set.keySet()) {
            if (!this._settings.containsKey(s))
                return false;
            if (!this._settings.get(s).equals(set.get(s)))
                return false;
        }
        return true;
    }

    /**
     * Fügt den Einstellungen die Daten einer Konfigurationsdatei hinzu.
     *
     * @see #joinConfigFile(InputStream)
     *
     * @param path Pfad zur Datei
     * @return {@link ReturnStatus}
     * @throws FileNotFoundException Wenn die Konfigurationsdatei nicht gefunden wurde.
     * @throws IOException Wenn es einen Fehler mit der Konfigurationsdatei gibt.
     */
    public ReturnStatus joinConfigFile(String path) throws FileNotFoundException, IOException {
        return joinConfigFile(new FileInputStream(path));
    }

    /**
     * Fügt den Einstellungen die Daten einer Konfigurationsdatei hinzu.
     *
     * Behandelt den Stream als Konfigurationsdatei und verarbeitet die Daten.
     *
     * @param configFile Die entsprechende Konfigurationsdatei.
     * @return {@link ReturnStatus}
     * @throws IOException Wenn es einen Fehler mit dem entsprechenden Stream gibt.
     */
    public ReturnStatus joinConfigFile(InputStream configFile) throws IOException {
        writeLoggerDebug1("Die Einstellung: \"" + this.getName() + "\" wird durch eine Konfigurationsdatei erweitert!",
                "Settings");
        // System.out.println(this._settings);
        Configurator conf_loader = new Configurator(configFile);
        conf_loader.parse_settings(this, false);
        // System.out.println(this._settings);
        return new ReturnStatus(0);
    }

    /**
     * Setzt analog zu {@link AbstractSettings#set(Serializable, Serializable)} wobei Werte übersetzt werden.
     *
     * @param key Schlüssel
     * @param val boolescher Wert
     * @return {@link AbstractSettings#set(Serializable, Serializable)}
     */
    public boolean set(String key, boolean val) {
        return set(key, (val)?S_TRUE:S_FALSE);
    }

    /**
     * Klassischer Vergleich zwischen zwei Einstellungsobjekten
     *
     * @implNote wird meist nur fürs Debugging verwendet :D
     *
     * @return Gleichheit der Objekte basierend auf der \em Spezifikation
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj.getClass() != this.getClass())
            return false;

        Settings set = (Settings) obj;
        return set.getName().equals(this.getName()) && set.getUnknownPolicy() == this.getUnknownPolicy()
                && compareToSettings(set._settings);
    }

    /**
     * Klassischer .toString() :D
     *
     * @return Die String-Repräsentation des Objekts.
     */
    @Override
    public String toString() {
        return "Settings [name=" + getName() + ", unknownPolicy=" + getUnknownPolicy() + ", settings=" + _settings
                + "]";
    }

}
