package de.eagle.util.blueprints;

/**
 * @file AbstractSettings.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Grundlage für Einstellungen
 * @see de.eagle.util.datatypes.Settings
 * @see de.eagle.util.datatypes.SettingDeskriptor
 */


import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.interfaces.iRealCloneable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Diese Klasse definiert alle notwendigen Eigenschaften von Einstellungen Es
 * wird hierbei grundlegend eine HashMap verwaltet die die nötigen
 * Key-Value-Paare enthält.
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @see java.util.HashMap
 * @since 1.0.10
 */
public abstract class AbstractSettings<K extends Serializable, V extends Serializable> implements Serializable,
        Iterable<HashMap.Entry<K, SettingDeskriptor<V>>>, iRealCloneable<AbstractSettings<K, V>> {
    private static final long serialVersionUID = 3197356410145487933L;
    /// Bezeichner der Einstellungen
    private final String name;
    /// Ist es erlaubt unbekannte Einstellungen hinzuzufügen?
    protected boolean add_unknown;
    /// Enthält die eigentlichen Einstellungen
    protected HashMap<K, SettingDeskriptor<V>> _settings;

    /**
     * Konstruiert die Basis aller Einstellungen ohne irgendwelche Voreinstellungen
     *
     * @param name          Name der Einstellungen
     */
    public AbstractSettings(String name) {
        this(name,true,null);
    }

    /**
     * Konstruiert die Basis aller Einstellungen
     *
     * @param name          Name der Einstellungen
     * @param add_unknown   Wie soll mit unbekannten/neuen Einstellungen verfahren
     *                      werden?
     * @param init_settings Start-Einstellungen
     */
    public AbstractSettings(String name, boolean add_unknown, HashMap<K, SettingDeskriptor<V>> init_settings) {
        this.name = name;
        this.add_unknown = add_unknown;
        _settings = Objects.requireNonNullElseGet(init_settings, HashMap::new);
    }

    public abstract AbstractSettings<K, V> clone();

    /**
     * @return Liefert den Namen der Einstellungen
     */
    public String getName() {
        return name;
    }

    /**
     * @return Wie soll mit unbekannten Einstellungen verfahren werden?
     */
    public boolean getUnknownPolicy() {
        return add_unknown;
    }

    /**
     * Liefert die Einstellung unter dem entsprechenden Key
     *
     * @param key zu Suchender Key
     * @return das gefundene Value
     */
    public SettingDeskriptor<V> get(K key) {
        return _settings.get(key);
    }

    /**
     * Liefert den Wert einer Einstellung
     *
     * @param key der Key der zugehörigen Einstellung
     * @return Den zum Key zugehörigen Wert
     */
    public V getValue(K key) {
        return get(key).getValue();
    }

    /**
     * Wenn add_uknown füge diese Einstellung neu hinzu sonst: versuche zu
     * überschreiben
     *
     * @param key       Key der Einstellung
     * @param new_value Wert der Einstellung
     * @return die alte Einstellung
     */
    public SettingDeskriptor<V> put(K key, SettingDeskriptor<V> new_value) {
        if (add_unknown || _settings.containsKey(key))
            return _settings.put(key, new_value.setName(key.toString()));
        return null;
    }

    /**
     * Setzt eine Einstellung
     *
     * @param key       Bezeichner der Einstellung
     * @param new_value Neuer Wert der Einstellung
     * @return false, wenn die Einstellung gesperrt ist
     */
    public boolean  set(K key, V new_value) {
        if (_settings.containsKey(key))
            return _settings.get(key).setValue(new_value);
        if (add_unknown) {
            _settings.put(key, SettingDeskriptor.create(key.toString(), "Unknown Setting", eSetting_Type.IS_TEXT,
                    false, new_value));
        }
        return true;
    }


    /**
     * @return true, wenn der Wert in den Einstellungen als Schlüssel präsent ist
     */
    public boolean containsKey(K key){
        return this._settings.containsKey(key);
    }
    /*
     * @return true, wenn der Wert in den Einstellungen als Wert präsent ist
     */
    public boolean containsValue(V value){
        return this._settings.containsValue(value);
    }

    public Iterator<Map.Entry<K, SettingDeskriptor<V>>> iterator() {
        return _settings.entrySet().iterator();
    }

    /**
     * 
     * Setzt ein neues Verfahren beim treffen auf unbekannte Einstellungen
     * 
     * @param newPolicy die neue Handhabung unbekannter Einstellungen
     * 
     * @return die Regel vor der Änderung 
     */
    public boolean setUnknownPolicy(boolean newPolicy) {
        boolean old = getUnknownPolicy();
        this.add_unknown = newPolicy;
        return old;
    }

}
