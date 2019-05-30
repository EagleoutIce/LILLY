package de.eagle.util.datatypes;

import de.eagle.util.Setting_Deskriptor;
import de.eagle.util.blueprints.AbstractSettings;

import java.util.HashMap;

public class Settings extends AbstractSettings<String, String> {
    private static final long serialVersionUID = 673856672235848508L;

    /**
     * Konstruiert die Basis aller Einstellungen
     *
     * @param name          Name der Einstellungen
     * @param add_unknown   Wie soll mit unbekannten/neuen Einstellungen verfahren
     *                      werden?
     * @param init_settings Start-Einstellungen
     */
    public Settings(String name, boolean add_unknown, HashMap<String, Setting_Deskriptor<String>> init_settings) {
        super(name, add_unknown, init_settings);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractSettings<String, String> clone() {
        return new Settings(this.getName(), this.getUnknownPolicy(),
                (HashMap<String, Setting_Deskriptor<String>>) this._settings.clone());
    }

    @SuppressWarnings("unchecked")
    public Settings cloneSettings() {
        return new Settings(this.getName(), this.getUnknownPolicy(),
                (HashMap<String, Setting_Deskriptor<String>>) this._settings.clone());
    }

    @Override
    public String getValue(String key) {
        Setting_Deskriptor<String> val = get(key);
        if (val != null && val.getValue() == null) {
            return "";
        }
        return val.getValue();
    }
}
