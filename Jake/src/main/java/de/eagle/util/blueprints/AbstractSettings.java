package de.eagle.util.blueprints;

/**
 * @file AbstractSettings.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Grundlage für Einstellungen
 * @see de.eagle.util.datatypes
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
     * Wenn add_unknown füge diese Einstellung neu hinzu sonst: versuche zu
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
     * Platziert eine neue Einstellung mithilfe eines Translators
     * @param translator der zu verwendende Translator
     * @param key der entsprechende Schlüssel
     * @param brief Erklärung zu Einstellung
     * @param type Der Typ der Einstellung
     * @param init Der initiale wert
     *
     * @see SettingDeskriptor#create(String, String, eSetting_Type, Serializable)
     *
     * @return das alte Objekt, wenn vorhanden
     */
    public <T> SettingDeskriptor<V> emplace(Translator<T,K> translator, T key, String brief, eSetting_Type type, V init){
        K k  = translator.translate(key);
        if(k == null)
            throw new IllegalArgumentException("Der Key: " + key.toString() + " wird nicht vom Translator: " + translator.toString() + " unterstützt!");

        if(add_unknown || _settings.containsKey(k)){
            return _settings.put(k, SettingDeskriptor.create(k.toString(), brief, type,init));
        }
        return null;
    }

    /**
     * Platziert eine neue Einstellung
     * @param key der entsprechende Schlüssel
     * @param brief Erklärung zu Einstellung
     * @param type Der Typ der Einstellung
     * @param init Der initiale wert
     *
     * @see SettingDeskriptor#create(String, String, eSetting_Type, Serializable)
     *
     * @return das alte Objekt, wenn vorhanden
     */
    public SettingDeskriptor<V> emplace(K key, String brief, eSetting_Type type, V init){
        if(add_unknown || _settings.containsKey(key)){
            return _settings.put(key, SettingDeskriptor.create(key.toString(), brief, type,init));
        }
        return null;
    }


    /**
     * Platziert eine neue Einstellung mithilfe eines Translators
     * @param translator der zu verwendende Translator
     * @param key der entsprechende Schlüssel
     * @param brief Erklärung zu Einstellung
     * @param type Der Typ der Einstellung
     * @param isMandatory ist das Argument verpflichtend?
     *
     * @see SettingDeskriptor#create(String, String, eSetting_Type, boolean)
     *
     * @return das alte Objekt, wenn vorhanden
     */
    public <T> SettingDeskriptor<V> emplace(Translator<T,K> translator, T key, String brief, eSetting_Type type, boolean isMandatory){
        K k  = translator.translate(key);
        if(k == null)
            throw new IllegalArgumentException("Der Key: " + key.toString() + " wird nicht vom Translator: " + translator.toString() + " unterstützt!");
        if(add_unknown || _settings.containsKey(k)){
            return _settings.put(k, SettingDeskriptor.create(k.toString(), brief, type,isMandatory));
        }
        return null;
    }

    /**
     * Platziert eine neue Einstellung
     * @param key der entsprechende Schlüssel
     * @param brief Erklärung zu Einstellung
     * @param type Der Typ der Einstellung
     * @param isMandatory ist das Argument verpflichtend?
     *
     * @see SettingDeskriptor#create(String, String, eSetting_Type, Serializable)
     *
     * @return das alte Objekt, wenn vorhanden
     */
    public SettingDeskriptor<V> emplace(K key, String brief, eSetting_Type type, boolean isMandatory){
        if(add_unknown || _settings.containsKey(key)){
            return _settings.put(key, SettingDeskriptor.create(key.toString(), brief, type, isMandatory));
        }
        return null;
    }

    /**
     * Setzt eine Einstellung
     *
     * @param key       Bezeichner der Einstellung
     * @param new_value Neuer Wert der Einstellung
     * @return false, wenn die Einstellung gesperrt ist
     */
    public boolean set(K key, V new_value) {
        if (_settings.containsKey(key))
            return _settings.get(key).setValue(new_value);
        if (add_unknown) {
            _settings.put(key, SettingDeskriptor.create(key.toString(), "Unknown Setting", eSetting_Type.IS_TEXT,
                    false, new_value));
        }
        return true;
    }

    /**
     * @return Liefert die Größe der Einstellungen
     */
    public int size(){
        return _settings.size();
    }

    public <T> V requestValue(Translator<T,K> ts, T key){
        return getValue(ts.translate(key));
    }

    /**
     * Fügt die newSettings mithilfe von {@link #put(Serializable, SettingDeskriptor)} diesem Einstellungsobjekt hinzu.
     *
     * @note Dies überschreibt die Metadaten!
     *
     * @implNote Beachte bezüglich der Elemente die Konfiguration von {@link #add_unknown}
     *
     * @param newSettings die neuen Einstellungen
     * @return true, wenn {@link #put(Serializable, SettingDeskriptor)} nie null liefert.
     */
    public boolean hardJoin(AbstractSettings<K, V> newSettings){
        boolean ret = true;
        for(Map.Entry<K,SettingDeskriptor<V>> elem : newSettings){
            if(this.put(elem.getKey(), elem.getValue()) == null)
                ret = false;
        }
        return ret;
    }

    /**
     * Fügt die newSettings mithilfe von {@link #set(Serializable, Serializable)} diesem Einstellungsobjekt hinzu.
     *
     * @note Dies überschreibt die Metadaten <b>nicht</b>!
     *
     * @implNote Beachte bezüglich der Elemente die Konfiguration von {@link #add_unknown}
     *
     * @param newSettings die neuen Einstellungen
     * @return true, wenn {@link #set(Serializable, Serializable)} nie false liefert.
     */
    public boolean softJoin(AbstractSettings<K, V> newSettings){
        boolean ret = true;
        for(Map.Entry<K,SettingDeskriptor<V>> elem : newSettings){
            if(!this.set(elem.getKey(), elem.getValue().getValue()))
                ret = false;
        }
        return ret;
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
