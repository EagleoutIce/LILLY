package de.eagle.lillyjakeframework;

import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.lillyjakeframework.core.CoreSettings;

import java.io.IOException;

import static de.eagle.util.io.JakeLogger.*;

import static de.eagle.lillyjakeframework.core.Definitions.HIDDEN_ARG;
import static de.eagle.lillyjakeframework.core.Definitions.PRG_BRIEF;

/**
 * @author Florian Sihler
 * @version 1.0.9
 * 
 * @file Jake.java
 * @warning Die aktuelle Version ist der erste Versuch alles in Java zu portieren - Funktionalität kann nicht
 * gewährleistet werden!
 * @brief Hilfsprogramm im Umgang mit LILLY
 * @page Important Super stuff
 * @tableofcontents
 * @section Main Wichtiger Einstieg
 * Folgende Seiten sind unter Umständen interessant
 * - @ref Changelog
 * - @ref jake.cpp
 * @section README README.md
 * @include README.md
 */

public class Jake {

    // Move to settings
    public static String program;

    public static void main(String[] args) throws IOException {
        writeLoggerInfo("\"" + PRG_BRIEF + "\" beginnt nun mit seiner Arbeit","Jake");
        if (args.length < 2 || args[1].charAt(0) == HIDDEN_ARG) {
            // setup log-Deskriptor & ld_config by configparser
        }
        JakeCompile.compile("");
        // load settings & interpret settings otherwise exit with failure

    }
}
