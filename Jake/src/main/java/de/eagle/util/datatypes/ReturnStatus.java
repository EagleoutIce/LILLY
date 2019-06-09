/**
 * @file ReturnStatus.java
 * @author Raphael Straub
 *
 * @version 1.0.10
 *
 * @brief Enthält bis jetzt nur einen Integer, der den Status der Zurückgegeben werden soll darstellt
 * @see de.eagle.lillyjakeframework.core.CoreFunctions
 */

package de.eagle.util.datatypes;

/**
 * Grundlegenden Klasse für die rückgabe des Status
 *
 * @author Raphael Straub
 * @version 1.0.10
 * @since 1.0.10
 */
public class ReturnStatus {
    /// Enthält einen Wert, der den Zurückgegebene Status darstellt
    public final int status;

    /**
     * Default constructor
     *
     * @param status der Status, der Zurückgegeben werden soll
     */
    public ReturnStatus(int status){
        this.status = status;
    }
}
