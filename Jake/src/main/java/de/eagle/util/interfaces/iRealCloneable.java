package de.eagle.util.interfaces;

/**
 * @file iRealCloneable.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Alternatives Cloneable Interface
 * @see de.eagle.util.helper.Cloner
 */
/**
 * Versuch zumindest die clone Methode zu fordern ^^
 *
 * @param <T> Typ den es zu Klonen gilt
 */
public interface iRealCloneable<T> extends Cloneable {
    /**
     * Klon-Methode
     *
     * @return (Hoffentlich) eine unabhängige, neue Instanz des Objekts
     */
    T clone();
}
