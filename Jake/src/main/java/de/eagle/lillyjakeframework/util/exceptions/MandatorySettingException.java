package de.eagle.lillyjakeframework.util.exceptions;

/**
 * Wird dann geworfen, wenn eine geforderte Einstellung nicht gesetzt wurde!
 */
public class MandatorySettingException extends RuntimeException{
    /**
     * Normaler "wurf"
     */
    public MandatorySettingException() { }

    /**
     * Wurf mit Nachricht
     *
     * @param message Nachricht
     */
    public MandatorySettingException(String message) { super(message); }

    /**
     * Wurf mit Grund
     *
     * @param cause Grund
     */
    public MandatorySettingException(Throwable cause) { super(cause); }

    /**
     * Wurf mit Grund und Nachricht
     *
     * @param message Nachricht
     * @param cause Grund
     */
    public MandatorySettingException(String message, Throwable cause) { super(message, cause); }
}
