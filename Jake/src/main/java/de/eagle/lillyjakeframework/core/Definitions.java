/**
 * @file Definitions.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Definiert globale Variablen für alle Teile von Jake
 * @see Jake
 */
package de.eagle.lillyjakeframework.core;

import de.eagle.gepard.modules.Projects;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eProgress_State;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

/**
 * @author Florian Sihler
 *         <p>
 *         Speichert alle Globalen Definitionen für Jake, die aus keiner Datei
 *         heraus erstellt werden und mit der spezifischen Jake Version
 *         einhergehen.
 * @version 2.1.0
 * @since 2.0.0
 */
public class Definitions {
    /// Kurzbeschreibung der Version
    public static final String PRG_BRIEF = "Jake 2.2.0 - Ein Regenbogenfarbpalast";
    /// Die Version als String
    public static final String JAKE_VERSION = "2.02.00";

    public static final String PRG_NAME = "jake";

    /// Startsignatur eines versteckten Arguments
    public static final char HIDDEN_ARG = '_';

    /// Signatur einer Hinzufügenden Zuweisung
    public static final String ASS_PATTERN = ":";

    public static final String S_TRUE = "true";
    public static final String S_FALSE = "false";

    public static final String DEFAULT_COMMENT_PATTERN = "![^!]*!";

    /**
     * @return Returns the relative working directory
     */
    public static String getRelativeWorkingDir() {
        return RELATIVE_WORKING_DIR;
    }

    /**
     * @param relativeWorkingDir Sets the relative working dir
     */
    public static void setRelativeWorkingDir(String relativeWorkingDir) {
        RELATIVE_WORKING_DIR = relativeWorkingDir;
    }

    public static String RELATIVE_WORKING_DIR = "";

    public static final int MAX_SETTINGS_REC = 12;

    public static DateTimeFormatter date_format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static DateTimeFormatter time_format = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static Settings projects() {
        try {
            return Projects.getInstance().getProjects(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Aktueller Stand dieser Version
     *
     * @see eProgress_State
     */
    public static final eProgress_State JAKE_STATE = eProgress_State.DEVELOPMENT;

    public static boolean GUI = false;

    // Pfad an dem Jake installiert ist. null wenn noch nicht installiert
    public static final String JAKE_INSTALLED_PATH = System.getenv("JAKE_INSTALLATION_PATH");

    /// Die aktuelle Version als Zahl
    public static final int JAKE_VERSION_NUM = 20200;

    public static final int B_NAME = 0;
    public static final int B_EXTRA = 1;
    public static final int B_INPUT = 2;
    public static final int B_TEXT = 3;

    public static final InputStream DEFAULT_CONFIG_STREAM = Definitions.class
            .getResourceAsStream("/configs/jake_default.conf");

    public static final String USER_CONFIG_PATH = System.getenv("LILLY_JAKE_CONFIG_PATH");

    public static final boolean MIRROR_LOG = true;
    public static final boolean MIRROR_CONSOLE = true;

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
}
