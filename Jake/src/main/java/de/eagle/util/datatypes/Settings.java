package de.eagle.util.datatypes;

/**
 * @file Settings.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Die normalen Einstellungen für Jake und Co
 * @see de.eagle.util.datatypes.SettingDeskriptorStringList
 * @see de.eagle.util.datatypes.SettingDeskriptor
 * @see de.eagle.util.blueprints.AbstractSettings
 */

import de.eagle.util.blueprints.AbstractSettings;

import java.util.HashMap;

import static de.eagle.util.logging.JakeLogger.writeLoggerDebug2;
import static de.eagle.util.logging.JakeLogger.writeLoggerDebug3;

public class Settings extends AbstractSettings<String, String> {
    private static final long serialVersionUID = 673856672235848508L;


    /**
     * Konstruiert die Einstellungen ohne irgendwelche Voreinstellungen
     *
     * @param name          Name der Einstellungen
     */
    public Settings(String name) {
        this(name,true,null);
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
     * @warning Im Falle eines nonexistenten Wertes wird das letzte Zeichen von new_value als separator gesetzt
     *
     * @param key       Bezeichner der Einstellung
     * @param new_value Neuer Wert der Einstellung
     * @return false, wenn die Einstellung gesperrt ist, oder es sich nicht um eine Liste handelt
     */
    public boolean add(String key, String new_value) {
        if (_settings.containsKey(key)){
            SettingDeskriptor s = _settings.get(key);
            writeLoggerDebug3("Einstellung geladen: " + s,"Settings");
            if(! (s instanceof SettingDeskriptorStringList)) return false;
            ((SettingDeskriptorStringList) s).addValue(new_value);
        }
        else if (add_unknown) {
            _settings.put(key, new SettingDeskriptorStringList(key.toString(), "Unknown Setting",
                    false, new_value,(new_value.isEmpty())?' ':new_value.charAt(new_value.length()-1)));
        }
        return true;
    }

    /**
     * Liefert den Wert einer gewissen Einstellung zurück - Kurzzschreibweise mit null check
     * 
     * @param key der zu suchende Key
     * @return der gefundene Wert, null wenn nicht gefunden
     */
    @Override
    public String getValue(String key) {
        SettingDeskriptor<String> val = get(key);
        if (val != null && val.getValue() == null) {
            return "";
        } else if (val == null){
            return null;
        }
        return val.getValue();
    }

    /**
     * Nachdem Java keine Methode anbietet um native zwei Hashmaps zu vergleichen hier der
     * Good-Enough ansatz :D
     * 
     * @param set die zu vergleichende Einstellung
     * @return true wenn die Einstellungen identisch sind.
     * 
     */
    protected boolean compareToSettings(HashMap<String, SettingDeskriptor<String>> set) {
            for(String s : set.keySet()){
                if(!this._settings.containsKey(s)) return false;
                if(!this._settings.get(s).equals(set.get(s)))
                    return false;
            }
            return true;
    }

    /**
     * Klassischer Vergleich zwischen zwei Einstellungsobjekten
     * 
     * @implNote wird meist nur fürs Debugging verwendet :D
     * 
     * @return Gleichheit der Objekte basierend auf der Spezifikation
     */
    @Override
    public boolean equals(Object obj){
        if(this==obj) return true; if(obj==null) return false;

        if (obj.getClass() != this.getClass()) return false;

        Settings set = (Settings)obj;
        return set.getName().equals(this.getName())
                && set.getUnknownPolicy() == this.getUnknownPolicy() 
                && compareToSettings(set._settings);
    }

    @Override
    public String toString() {
        return "Settings [name=" + getName() + ", unknownPolicy=" + getUnknownPolicy() + ", settings=" + _settings + "]"; 
    }

}
