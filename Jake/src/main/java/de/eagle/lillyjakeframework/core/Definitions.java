package de.eagle.lillyjakeframework.core;

import de.eagle.lillyjakeframework.util.enumerations.eProgress_State;

/**
 * @author Florian Sihler
 *
 * Speichert alle Globalen Definitionen für Jake, die aus keiner Datei heraus erstellt werden
 * und mit der spezifischen Jake Version einhergehen.
 *
 * @version 1.0.10
 * @since 1.0.10
 */
public class Definitions {
    /// Kurzbeschreibung der Version
    public static final String PRG_BRIEF = "Jake 1.0.10 - It's Java Time";
    /// Die Version als String
    public static final String JAKE_VERSION = "1.00.10";

    /// Startsignatur eines versteckten Arguments
    public static final String HIDDEN_ARG = "_";

    /**
     * Aktueller Stand dieser Version
     *
     * @see eProgress_State
     */
    public static final eProgress_State JAKE_STATE = eProgress_State.DEVELOPMENT;

    /// Die aktuelle Version als Zahl
    public static final int JAKE_VERSION_NUM = 10010;


}
