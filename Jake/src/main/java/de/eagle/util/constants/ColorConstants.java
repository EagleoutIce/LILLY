package de.eagle.util.constants;

/**
 * @file ColorConstants.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Definiert Farben, die Jake für die Kommandozeilenausgabe verwendet
 */

public class ColorConstants {
    /// @brief Goldene Farbe
    public static final String COL_GOLD = "\033[38;2;255;191;0m";
    /// @brief Blaue Farbe
    public static final String COL_CYAN = "\033[38;5;39m";
    /// @brief Grüne Farbe
    public static final String COL_GREEN = "\033[38;5;106m";// "\033[38;2;101;200;25m";
    /// @brief Lila Farbe
    public static final String COL_PURPLE = "\033[38;5;127m";
    /// @brief Design-String zum Zurücksetzen der Farbe
    public static final String COL_RESET = "\033[0m";
    /// @brief Design-String zum Setzen der Fehler-Farbe in der Konsole
    public static final String COL_ERROR = "\033[38;2;255;32;82m";
    /// @brief Design-String zum Setzen der Erfolgs-Farbe in der Konsole
    public static final String COL_SUCCESS = COL_GREEN;
    /// @brief Design-String für Parameter
    public static final String STY_PARAM = "\033[2;3;51m";
}