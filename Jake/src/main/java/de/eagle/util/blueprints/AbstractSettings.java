package de.eagle.util.blueprints;

import de.eagle.util.Setting_Deskriptor;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.interfaces.iRealCloneable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        Iterable<HashMap.Entry<K, Setting_Deskriptor<V>>>, iRealCloneable<AbstractSettings<K, V>> {
    private static final long serialVersionUID = 3197356410145487933L;
    /// Bezeichner der Einstellungen
    private final String name;
    /// Ist es erlaubt unbekannte Einstellungen hinzuzufügen?
    private final boolean add_unknown;
    /// Enthält die eigentlichen Einstellungen
    protected HashMap<K, Setting_Deskriptor<V>> _settings;

    /**
     * Konstruiert die Basis aller Einstellungen
     *
     * @param name          Name der Einstellungen
     * @param add_unknown   Wie soll mit unbekannten/neuen Einstellungen verfahren
     *                      werden?
     * @param init_settings Start-Einstellungen
     */
    public AbstractSettings(String name, boolean add_unknown, HashMap<K, Setting_Deskriptor<V>> init_settings) {
        this.name = name;
        this.add_unknown = add_unknown;
        _settings = init_settings;
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
    public Setting_Deskriptor<V> get(K key) {
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
    public Setting_Deskriptor<V> put(K key, Setting_Deskriptor<V> new_value) {
        if (add_unknown || _settings.containsKey(key))
            return _settings.put(key, new_value.setName(key.toString()));
        return null;
    }

    /**
     * Setzt eine Einstellung
     *
     * @param key       Bezeichner der Einstellung
     * @param new_value Neuer Wert der Einstellung
     */
    public void set(K key, V new_value) {
        if (_settings.containsKey(key))
            _settings.get(key).setValue(new_value);
        else if (add_unknown) {
            _settings.put(key, new Setting_Deskriptor<V>(key.toString(), "Unknown Setting", eSetting_Type.IS_TEXT,
                    false, new_value));
        }
    }

    public Iterator<Map.Entry<K, Setting_Deskriptor<V>>> iterator() {
        return _settings.entrySet().iterator();
    }

}
