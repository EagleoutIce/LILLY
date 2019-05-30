package de.eagle.lillyjakeframework.util;

import de.eagle.lillyjakeframework.util.blueprints.AbstractSettings;
import de.eagle.lillyjakeframework.util.enumerations.eSetting_Type;
import de.eagle.lillyjakeframework.util.exceptions.MandatorySettingException;

import java.io.Serializable;

/**
 * Diese Klasse beschreibt den inhalt einer Einstellung er besteht aus
 * - Wert der Einstellung (value:T)
 * - Erklärung der Einstellung (brief:String)
 * - Typ der Einstellung (type:eSetting_Type)
 *
 * @author Florian Sihler
 *
 * @version 1.0.10
 * @since 1.0.10
 *
 * @see AbstractSettings
 */
public class Setting_Deskriptor<T extends Serializable> implements Serializable {
    /// Endgültiger Wert der Einstellung
    private T value = null;
    /// (interner) Name der Einstellung für Debugging Zwecke
    private String name;
    /// Erklärung zur Einstellung
    public final String brief;
    /// Typ der Einstellung
    public final eSetting_Type type;
    /// gibt an ob die entsprechende Einstellung verpflichtend ist
    public final boolean isMandatory;

    /**
     * Konstruiert den entsprechenden nicht verplfichtenden Deskriptor
     *
     * @param name der (interne) Name der Einstellung
     * @param brief Eine kurze Beschreibung der Einstellung
     * @param type Der Typ der Einstellung
     */
    public Setting_Deskriptor(String name, String brief , eSetting_Type type){
        this(name,brief,type,false,null);
    }

    /**
     * Konstruiert den entsprechenden Deskriptor
     *
     * @param brief Eine kurze Beschreibung der Einstellung
     * @param type Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     */
    public Setting_Deskriptor(String name, String brief , eSetting_Type type, boolean isMandatory){
        this(name,brief,type,isMandatory,null);
    }

    /**
     * Konstruiert den entsprechenden Deskriptor inklusive Startwert
     *
     * @param brief Eine kurze Beschreibung der Einstellung
     * @param type Der Typ der Einstellung
     * @param isMandatory Ist die Einstellung verpflichtend?
     * @param init Startwert
     */
    public Setting_Deskriptor(String name, String brief , eSetting_Type type,  boolean isMandatory, T init){
        this.brief = brief;             this.type = type;
        this.value = init;              this.isMandatory = isMandatory;
        this.setName(name);
    }

    /**
     * Liefert den Wert der Einstellung zurück
     *
     * @throws MandatorySettingException
     *
     * @return der Wert der Einstellung
     */
    public T getValue() {
        if(isMandatory && value == null)
            throw new MandatorySettingException("Die verpflichtende Einstellung: " + getName()
                                                + " wurde nicht mit einem Wert belegt!");
        return value;
    }

    /**
     * @return Liefert, ob die Einstellung verpflichtend ist
     */
    public boolean isMandatory() {
        return isMandatory;
    }

    /**
     * Setzt die Einstellung auf value
     *
     * @param value der neue Wert der Eintellung
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Klassisches .equals()
     *
     * @param obj zu vergleichendes Objekt
     * @return Gleichheit gemäß der Spezifikation
     */
    @Override
    public boolean equals(Object obj) {
        return (value==null) ? obj == null : value.equals(obj);
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
    public Setting_Deskriptor<T> setName(String name) {
        this.name = name;
        return this;
    }
}
