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

import java.util.Objects;

/**
 * Grundlegenden Klasse für die rückgabe des Status
 *
 * @author Raphael Straub
 * @author Florian Sihler
 *
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

    /**
     * Repräsentiert eine geglückte Aktion
     */
    public static final ReturnStatus EXIT_SUCCESS = new ReturnStatus(0);
    /**
     * Repräsentiert eine gescheiterte Aktion
     */
    public static final ReturnStatus EXIT_FAILURE = new ReturnStatus(1);

    /**
     * @return true wenn geglückt
     */
    public boolean success(){
        return this.equals(EXIT_SUCCESS);
    }

    /**
     * @return true, wenn kein erfolg
     */
    public boolean failure(){
        return !success();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReturnStatus)) return false;
        ReturnStatus that = (ReturnStatus) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "ReturnStatus{" +
                "status=" + status +
                '}';
    }
}
