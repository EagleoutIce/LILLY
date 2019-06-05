package de.eagle.util.datatypes;

/**
 * @file FunctionCollector.java
 * @author Raphael Straub
 * @version 1.0.10
 *
 * @brief Mapped String auf FunctionDeskriptor
 * @see de.eagle.util.datatypes.FunctionDeskriptor
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Mapped String auf FunctionDeskriptor
 *
 * @author Raphael Straub
 * @version 1.0.10
 * @since 1.0.10
 *
 * @param <K> Parametertyp der Funktion
 * @param <V> Rückgabetyp der Funktion
 */
public class FunctionCollector<K,V> extends HashMap<String, FunctionDeskriptor<K, V>> {

    /**
     * Konstruiert einen FunctionCollector aus einer bestehenden Map
     *
     * @param m Map, deren Daten übernommen werden sollen
     */
    public FunctionCollector(Map<? extends String, ? extends FunctionDeskriptor<K, V>> m) {
        super(m);
    }
}
