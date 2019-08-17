package de.eagle.util.datatypes;

/**
 * @file FunctionDeskriptor.java
 * @author Raphael Straub
 * @version 2.0.0
 *
 * @brief Enthält eine Funktion und beschreibt diese
 * @see de.eagle.util.datatypes.FunctionCollector
 */

import java.util.function.Function;

/**
 * Enthält eine Funktion und beschreibt diese
 *
 * @author Raphael Straub
 * @version 2.0.0
 * @since 2.0.0
 *
 * @param <K> Parametertyp der Funktion
 * @param <V> Rückgabetyp der Funktion
 */
public class FunctionDeskriptor<K, V> {
    /// Name der Funktion
    public String name;

    /// Kurze beschreibung der Funktion
    public String brief;

    // Die gegebenen Funktion
    public Function<K, V> function;

    /**
     * Default Constructor
     *
     * @param name     Name der Funktion
     * @param brief    Kurzbeschreibung der Funktion
     * @param function die gegebene Funktion
     */
    public FunctionDeskriptor(String name, String brief, Function<K, V> function) {
        this.name = name;
        this.brief = brief;
        this.function = function;
    }
}
