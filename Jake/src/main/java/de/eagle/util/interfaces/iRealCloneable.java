package de.eagle.util.interfaces;

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
