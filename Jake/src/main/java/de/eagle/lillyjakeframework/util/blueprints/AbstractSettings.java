package de.eagle.lillyjakeframework.util.blueprints;

import de.eagle.lillyjakeframework.util.Setting_Deskriptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Diese Klasse definiert alle notwendigen Eigenschaften von Einstellungen
 * Es wird hierbei grundlegend eine HashMap verwaltet die die nötigen Key-Value-Paare
 * enthält.
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @since 1.0.10
 *
 * @see java.util.HashMap
 */
public abstract class AbstractSettings<K extends Serializable, V extends Serializable> implements Serializable,
                                            Iterable<HashMap.Entry<K,Setting_Deskriptor<V>>> {
    /// Enthält die eigentlichen Einstellungen
    private HashMap<K, Setting_Deskriptor<V>> _settings;
    ///Bezeichner der Einstellungen
    private final String name;
    /// Ist es erlaubt unbekannte Einstellungen hinzuzufügen?
    private final boolean add_unknown;

    /**
     * Konstruiert die Basis aller Einstellungen
     *
     * @param name Name der Einstellungen
     * @param add_unknown Wie soll mit unbekannten/neuen Einstellungen verfahren werden?
     * @param init_settings Start-Einstellungen
     */
    public AbstractSettings(String name, boolean add_unknown, HashMap<K,Setting_Deskriptor<V>> init_settings){
        this.name = name;
        this.add_unknown = add_unknown;
        _settings = init_settings;
    }

    /**
     * @return Liefert den Namen der Einstellungen
     */
    public String getName() {
        return name;
    }

    /**
     * @return Wie soll mit unbekannten Einstellungen verfahren werden?
     */
    public boolean getUnknownPolicy(){
        return add_unknown;
    }

    /**
     * Liefert die Einstellung unter dem entsprechenden Key
     * @param key
     * @return
     */
    public Setting_Deskriptor<V> get(K key){
        return _settings.get(key);
    }

    public V getValue(K key) {
        return get(key).getValue();
    }

    protected Setting_Deskriptor<V> put(K key, Setting_Deskriptor<V> new_value){
        if(add_unknown || _settings.containsKey(key))
            return _settings.put(key, new_value.setName(key.toString()));
        return null;
    }

    protected void set(K key, V new_value){
        if(_settings.containsKey(key))
            _settings.get(key).setValue(new_value);
    }

    public Iterator<Map.Entry<K, Setting_Deskriptor<V>>> iterator() {
        return _settings.entrySet().iterator();
    }


}
