/**
 * @file Definitions.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Definiert globale Variablen für alle Teile von Jake
 * @see Jake
 */
package de.eagle.lillyjakeframework.core;

 // TODO: insert LogLevel 

import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.enumerations.eProgress_State;

/**
 * @author Florian Sihler
 * <p>
 * Speichert alle Globalen Definitionen für Jake, die aus keiner Datei
 * heraus erstellt werden und mit der spezifischen Jake Version
 * einhergehen.
 * @version 1.0.10
 * @since 1.0.10
 */
public class Definitions {
    /// Kurzbeschreibung der Version
    public static final String PRG_BRIEF = "Jake 1.0.10 - It's Java Time";
    /// Die Version als String
    public static final String JAKE_VERSION = "1.00.10";

    /// Startsignatur eines versteckten Arguments
    public static final char HIDDEN_ARG = '_';

    /// Signatur einer Hinzufügenden Zuweisung
    public static final String ASS_PATTERN = ":";

    public static final String S_TRUE = "true";

    public static final int MAX_SETTINGS_REC = 12;

    /**
     * Aktueller Stand dieser Version
     *
     * @see eProgress_State
     */
    public static final eProgress_State JAKE_STATE = eProgress_State.DEVELOPMENT;

    /// Die aktuelle Version als Zahl
    public static final int JAKE_VERSION_NUM = 10010;

    public static final int B_NAME = 0;
    public static final int B_EXTRA = 1;
    public static final int B_INPUT = 2;
    public static final int B_TEXT = 3;

}
