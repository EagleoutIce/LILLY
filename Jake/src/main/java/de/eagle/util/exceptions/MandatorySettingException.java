package de.eagle.util.exceptions;
/**
 * @file MandatorySettingException.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Fehler, der geworfen wird, wenn eine verpflichtende Einstellung nicht gesetzt ist
 * @see de.eagle.util.datatypes.Settings.SettingDeskriptor
 */

/**
 * Wird dann geworfen, wenn eine geforderte Einstellung nicht gesetzt wurde!
 */
public class MandatorySettingException extends RuntimeException {
    /**
     * Normaler "wurf"
     */
    public MandatorySettingException() {
    }

    /**
     * Wurf mit Nachricht
     *
     * @param message Nachricht
     */
    public MandatorySettingException(String message) {
        super(message);
    }

    /**
     * Wurf mit Grund
     *
     * @param cause Grund
     */
    public MandatorySettingException(Throwable cause) {
        super(cause);
    }

    /**
     * Wurf mit Grund und Nachricht
     *
     * @param message Nachricht
     * @param cause   Grund
     */
    public MandatorySettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
