package de.eagle.util.enumerations;
/**
 * @file eProgress_State.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Verwaltet den Stand der Entwicklung
 * @see de.eagle.lillyjakeframework.core.Definitions
 */

/**
 * Beschreibt den aktuellen Fortschritt eines Objekts
 */
public enum eProgress_State {
    /// Spiel befindet sich in der Entwicklung
    DEVELOPMENT,
    /// Spiel ist Veröffentlicht
    RELEASE,
    /// Spiel ist veraltet
    DEPRECATED
}
