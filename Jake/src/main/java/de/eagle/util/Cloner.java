package de.eagle.util;

import de.eagle.util.interfaces.iRealCloneable;

import java.lang.reflect.InvocationTargetException;

/**
 * Versucht mithilfe von Javas Reflection-Mechanismen ein Objekt zu Clonen, da
 * Javas Cloneable-Interface ja eine Katastrophe ist (insbesondere für Generics)
 *
 * @param <T> zu klonender Typ
 */
public class Cloner<T> implements iRealCloneable<T> {

    /// Zwischenspeicher der zu kopierenden Daten
    private T clone_data;

    /**
     * Konstruiert den entsprechenden Kopierer
     *
     * @param data zu kopierende Daten
     */
    public Cloner(T data) {
        clone_data = data;
    }

    /**
     * @return Eine (hoffentlich) unabhängige Kopie des Objekts
     */
    @SuppressWarnings("unchecked")
    public T clone() {
        if (clone_data == null)
            return null;

        Cloner<T> newT = new Cloner<T>(null);
        try {
            newT.clone_data = (T) clone_data.getClass().getMethod("clone").invoke(clone_data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // Versuche normale zuweisung
            // System.out.println("Cloner scheitert für: " + clone_data);
            try {
                newT = (Cloner<T>) super.clone();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        }
        return newT.clone_data;
    }
}
