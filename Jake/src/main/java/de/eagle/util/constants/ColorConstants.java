package de.eagle.util.constants;

/**
 * @file ColorConstants.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Definiert Farben, die Jake für die Kommandozeilenausgabe verwendet
 */

public class ColorConstants {
    /// @brief Design-String zum Zurücksetzen der Farbe
    public static final String COL_RESET = "\033[0m";
    /// @brief Design-String zum Setzen der Fehler-Farbe in der Konsole
    public static final String COL_ERROR = "\033[38;2;255;32;82m";
    /// @brief Design-String zum Setzen der Erfolgs-Farbe in der Konsole
    public static final String COL_SUCCESS = "\033[38;2;102;250;0m";
    /// @brief Design-String für Parameter
    public static final String STY_PARAM = "\033[2;3;51m";
}