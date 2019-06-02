package de.eagle.util.datatypes;

/**
 * @file SettingDeskriptor.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Beschreibt eine einzelne Einstellung
 * @see de.eagle.util.datatypes.Settings
 * @see de.eagle.util.datatypes.SettingDeskriptorStringList
 */

import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.exceptions.MandatorySettingException;

import java.io.Serializable;

/**
 * Diese Klasse beschreibt den inhalt einer Einstellung er besteht aus - Wert
 * der Einstellung (value:T) - Erklärung der Einstellung (brief:String) - Typ
 * der Einstellung (type:eSetting_Type)
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @see AbstractSettings
 * @since 1.0.10
 */
public class SettingDeskriptor<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 7353042747573185893L;
    
    /// Erklärung zur Einstellung
    public final String brief;
    /// Typ der Einstellung
    public final eSetting_Type type;
    /// gibt an ob die entsprechende Einstellung verpflichtend ist
    public final boolean isMandatory;
    /// Endgültiger Wert der Einstellung
    protected T value = null;
    /// (interner) Name der Einstellung für Debugging Zwecke
    private String name;
    /// Verhindert, dass die Einstellung überschrieben werden kann
    protected boolean isLocked = false;

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief){
        return create(name, brief, eSetting_Type.IS_TEXT,false,null,' ');
    }

    /**
     * Konstruiert den entsprechenden ListDeskriptor inklusive Startwert
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param separator Trenner für die Liste
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief, char separator){
        return create(name, brief, eSetting_Type.IS_TEXTLIST,false,null, separator);
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     *
     * @param name        der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param init        Startwert
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief, T init){
        return create(name, brief, eSetting_Type.IS_TEXT,false,init,' ');
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     *
     * @param name        der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param separator   Trenner für die Liste
     * @param init        Startwert
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief, T init, char separator){
        return create(name, brief, eSetting_Type.IS_TEXT,false,init,separator);
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     * Macht einen ListDeskriptor, wenn type == IS_TEXTLIST
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief,eSetting_Type type){
        return create(name, brief, type,false,null,' ');
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     * Macht einen ListDeskriptor, wenn type == IS_TEXTLIST
     *
     * @param name        der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     * @param init        Startwert
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief, eSetting_Type type, T init){
        return create(name, brief, type,false,init,' ');
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     * Macht einen ListDeskriptor, wenn type == IS_TEXTLIST
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     * @param init        Startwert
     * @param separator   Der Trenner für Liste
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief,eSetting_Type type, T init, char separator){
        return create(name, brief, type,false,init,separator);
    }


    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     * Macht einen ListDeskriptor, wenn type == IS_TEXTLIST
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     * @param init        Startwert
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief,eSetting_Type type, boolean isMandatory, T init){
        return create(name, brief, type,isMandatory,init,' ');
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     * Macht einen ListDeskriptor, wenn type == IS_TEXTLIST
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     */
    public static  <T extends Serializable> SettingDeskriptor<T> create(String name, String brief,eSetting_Type type, boolean isMandatory){
        return create(name, brief, type,isMandatory,null,' ');
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     * Macht einen ListDeskriptor, wenn type == IS_TEXTLIST
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     * @param init        Startwert
     * @param separator   Der Trenner für Liste
     *
     * @throws IllegalArgumentException im Falle eines illegalen Arguments
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> SettingDeskriptor<T> create(String name, String brief,eSetting_Type type, boolean isMandatory, T init, char separator){
        if(type.equals(eSetting_Type.IS_TEXTLIST)){
            if(!(init instanceof String)) throw new IllegalArgumentException("der initiale Wert für eine Textlist muss ein String sein!");
            return (SettingDeskriptor<T>) new SettingDeskriptorStringList(name, brief, isMandatory, (String)init, separator);
        }
        return new SettingDeskriptor<T>(name, brief, type, isMandatory, (T) init);
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     *
     * @param name  der (interne) Name der Einstellung
     * @param brief       Eine kurze Beschreibung der Einstellung
     * @param type        Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     * @param init        Startwert
     */
    protected SettingDeskriptor(String name, String brief,
            eSetting_Type type, boolean isMandatory, T init) {
        this.brief = brief;
        this.type = type;
        this.value = init;
        this.isMandatory = isMandatory;
        this.setName(name);
    }

    /**
     * Liefert den Wert der Einstellung zurück
     *
     * @return der Wert der Einstellung
     * @throws MandatorySettingException Wenn die mand. Einstellung nicht gesetzt ist!
     */
    public T getValue() {
        if (isMandatory && value == null)
            throw new MandatorySettingException(
                    "Die verpflichtende Einstellung: " + getName() 
                        + " wurde nicht mit einem Wert belegt!");
        return value;
    }

    /**
     * @return true, wenn die Einstellung gesperrt wurde
     */
    public boolean isLocked(){
        return this.isLocked;
    }
    /**
     * Sperrt die Einstellung
     */
    public void lock(){
        this.isLocked = true;
    }

    /**
     * Setzt die Einstellung auf value
     *
     * @param value der neue Wert der Einstellung
     * @return true, wenn die Operation geklappt hat, false wenn locked;
     */
    public boolean setValue(T value) {
        if(isLocked) return false;
        this.value = value;
        return true;
    }

    /**
     * @return Liefert, ob die Einstellung verpflichtend ist
     */
    public boolean isMandatory() {
        return isMandatory;
    }

    /**
     * Klassisches .equals()
     *
     * @param obj zu vergleichendes Objekt
     * @return Gleichheit gemäß der Spezifikation
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass() != this.getClass()) return false;
        SettingDeskriptor<T> t = (SettingDeskriptor<T>)obj;
        if(this.getValue() == null) return t.getValue() == null; 
        return (t.getName().equals(this.getName())) 
                && (t.isMandatory()==this.isMandatory())
                && (t.getValue().equals(this.getValue()));
                // Notiz: Es ist gestattet, dass es sich bei einer Einstellung um
                // eine gesperrte Handelt, während die andere Änderungen zulässt
    }

    /**
     * @return Erhalte den Namen der Einstellung
     */
    public String getName() {
        return name;
    }

    /**
     * @param name neuer Name der Einstellung
     */
    public SettingDeskriptor<T> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "SettingDeskriptor [brief=" + brief + ", isMandatory=" 
                + isMandatory + ", name=" + name + ", type="
                + type + ", value=" + value + "]";
    }

    public String getBrief() {
        return this.brief;
    }
}
