package de.eagle.util.datatypes;

/**
 * @file SettingDeskriptorStringList.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Grundlage für eine Einstellung als Liste
 * @see de.eagle.util.datatypes.Settings
 * @see de.eagle.util.datatypes.SettingDeskriptor
 */

import de.eagle.util.enumerations.eSetting_Type;

import java.io.Serializable;

/**
 * Beschreibt eine bestimmte Einstellung, die eine Liste an Strings verwaltet
 *
 * @author Florian Sihler
 * @version 2.0.0
 * @since 2.0.0
 *
 * @see SettingDeskriptor
 */
public class SettingDeskriptorStringList extends SettingDeskriptor<String> implements Serializable {

    private char separator;

    /**
     * Konstruiert den entsprechenden nicht verpflichtenden Listen Deskriptor
     *
     * @param name      der (interne) Name der Einstellung
     * @param brief     Eine kurze Beschreibung der Einstellung
     * @param mandatory ist die Einstellung verpflichtend?
     * @param init      der Startwert der Einstellung
     * @param separator der Trenner für die Liste
     */
    protected SettingDeskriptorStringList(String name, String brief, boolean mandatory, String init, char separator) {
        super(name, brief, eSetting_Type.IS_TEXTLIST, mandatory, init);
        this.separator = separator;
    }

    /**
     * @return Liefert den Separator der Liste
     */
    public char getSeparator() {
        return this.separator;
    }

    /**
     * Fügt einen Wert der Liste hinzu - eingefügte Separatoren werden garantiert
     * 
     * @param addVal der hinzuzufügende Wert
     * @return false wenn das Objekt gesperrt ist
     *
     * @see SettingDeskriptor
     */
    public boolean addValue(String addVal) {
        if (this.isLocked)
            return false;
        // Hier gibt es nichts zu tun
        if (addVal == null || addVal.isEmpty())
            return true;
        // gilt es überhaupt etwas hinzuzufügen?
        if (value == null || value.isEmpty()) {
            if (addVal.charAt(0) == separator)
                return this.setValue(addVal.substring(1));
            else
                return this.setValue(addVal);
        }

        // ist das ende des bisherigen Werts der Separator?
        if (value.charAt(value.length() - 1) == separator) {
            // beginnt der addWert mit einem Separator?
            if (addVal.charAt(0) == separator)
                this.value += addVal.substring(1);
            else
                this.value += addVal;
        } else {
            // beginnt der addWert mit einem Separator?
            if (addVal.charAt(0) == separator)
                this.value += addVal;
            else
                this.value += this.separator + addVal;
        }
        return true;
    }

    /**
     * Klassisches .equals() inklusive des Separators
     *
     * @param obj zu vergleichendes Objekt
     * @return Gleichheit gemäß der Spezifikation
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && ((SettingDeskriptorStringList) obj).getSeparator() == this.getSeparator();
    }

    @Override
    public String toString() {
        return "SettingDeskriptorStringList [" + "separator=" + separator + ", brief='" + brief + '\'' + ", type="
                + type + ", isMandatory=" + isMandatory + ", value=" + value + ", isLocked=" + isLocked + ']';
    }
}
