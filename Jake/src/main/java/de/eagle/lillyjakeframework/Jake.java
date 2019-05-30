package de.eagle.lillyjakeframework;

import static de.eagle.lillyjakeframework.logging.JakeLogger.getLogger;
import static de.eagle.lillyjakeframework.core.Definitions.*;

/**
 * @file jake.cpp
 * @author Florian Sihler
 * @version 1.0.9
 * @warning Die aktuelle Version ist der erste Versuch alles in Java zu portieren - Funktionalität kann nicht
 *          gewährleistet werden!
 *
 *
 * @brief Hilfsprogramm im Umgang mit LILLY
 *
 * @page Important Super stuff
 * @tableofcontents
 *
 * @section Main Wichtiger Einstieg
 * Folgende Seiten sind unter Umständen interessant
 * - @ref Changelog
 * - @ref jake.cpp
 *
 * @section README README.md
 * @include README.md
 *
 */

public class Jake {

    // Move to settings
    public static String program;

    public static void main(String[] args) {
        getLogger().info("\"" + PRG_BRIEF + "\" beginnt nun mit seiner Arbeit");
        if (args.length < 2 || args[1].startsWith(HIDDEN_ARG)) {
            // setup log-Deskriptor & ld_config by configparser
        }
        program = args[0];

        // load settings & interpret settings otherwise exit with failure

    }
}
