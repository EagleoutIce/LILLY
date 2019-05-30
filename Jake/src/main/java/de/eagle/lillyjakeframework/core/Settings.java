package de.eagle.lillyjakeframework.core;

import de.eagle.lillyjakeframework.util.Setting_Deskriptor;
import de.eagle.lillyjakeframework.util.blueprints.AbstractSettings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Settings extends AbstractSettings<String,String> {
    /**
     * Konstruiert die Basis aller Einstellungen
     *
     * @param name Name der Einstellungen
     * @param add_unknown Wie soll mit unbekannten/neuen Einstellungen verfahren werden?
     * @param init_settings Start-Einstellungen
     */
    public Settings(String name, boolean add_unknown, HashMap<String, Setting_Deskriptor<String>> init_settings){
        super(name,add_unknown,init_settings);
    }
}
