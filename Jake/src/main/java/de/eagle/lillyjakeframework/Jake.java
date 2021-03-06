package de.eagle.lillyjakeframework;

import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.lillyjakeframework.gui.core.GUICompile;
import de.eagle.lillyjakeframework.gui.core.InstallJake;
import de.eagle.lillyjakeframework.installer.LillyInstaller.LinuxLillyInstaller;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.helper.ResourceControl;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import java.io.IOException;
import java.util.Arrays;

import static de.eagle.lillyjakeframework.core.Definitions.PRG_BRIEF;
import static de.eagle.util.io.JakeLogger.writeLoggerError;
import static de.eagle.util.io.JakeLogger.writeLoggerInfo;

/**
 * @author Florian Sihler
 * @version 1.0.9
 *
 * @file Jake.java
 * @warning Die aktuelle Version ist der erste Versuch alles in Java zu
 *          portieren - Funktionalität kann nicht gewährleistet werden!
 * @brief Hilfsprogramm im Umgang mit LILLY
 * @page Important Super stuff
 * @tableofcontents
 * @section Main Wichtiger Einstieg Folgende Seiten sind unter Umständen
 *          interessant - @ref Changelog - @ref jake.cpp
 * @section README README.md
 * @include README.md
 */

public class Jake {

    // Move to settings
    public static String program;

    public static void main(String[] args) throws IOException {

        JakeLogger.enabled = false;
        ReturnStatus rs = CommandLineParser.parse_args(args, CoreSettings.getSettings());

        // Disable Logging if '_get'
        if(CoreSettings.requestValue("S_OPERATION").equals("_get")){
            JakeLogger.enabled = false;
            JakeWriter.out = new JakeWriter.MirrorStream(false, true);
            JakeWriter.err = new JakeWriter.MirrorStream(false, false);
        } else {
            JakeLogger.enabled = true;
        }

        try {
            // We won't load the default config now as it will not point to any project.
            // CoreSettings.getSettings().joinConfigFile(Definitions.DEFAULT_CONFIG_STREAM);
            if (Definitions.USER_CONFIG_PATH != null && !Definitions.USER_CONFIG_PATH.isEmpty())
                CoreSettings.getSettings().joinConfigFile(Definitions.USER_CONFIG_PATH);
        } catch (IOException ignored) {
            writeLoggerError("Es gab einen Fehler beim Lesen der Nutzer-Konfiguration", "Jake");
        }

        if (!CoreSettings.requestValue("S_OPERATION").startsWith(String.valueOf(Definitions.HIDDEN_ARG))) {
            if (args.length > 0 && args[0].startsWith("DEI")) {
                PropertiesProvider.getJakeInstaller(false).uninstall();
                return;
            }

            if (!PropertiesProvider.isInstalled() || (args.length > 0 && args[0].startsWith("REI"))) {
                if (args.length > 0 && args[0].startsWith("GUI")) {
                    InstallJake.main(args);
                } else { // commandline based:
                    for (String s : PropertiesProvider.getJakeInstaller(false)) {
                        JakeWriter.out.println(s);
                    }
                    return;
                }
            }

            writeLoggerInfo("\"" + PRG_BRIEF + "\" beginnt nun mit seiner Arbeit", "Jake");
            // if (args.length < 2 || args[1].charAt(0) == HIDDEN_ARG) {
            // // setup log-Deskriptor & ld_config by configparser
            // //if()
            // }

            // Test, if there's an Update for the existing Lilly-Installation
            if(PropertiesProvider.getOS().equals(PropertiesProvider.OS.LINUX) && ResourceControl.LillyInJarIsNewer()) {
                // Installiere neue Version
                writeLoggerInfo("Es wird die Lilly-Version aktualisiert: " + LinuxLillyInstaller.installIncluded(), "Jake");
                JakeWriter.out.println(ColorConstants.COL_CYAN + "[Die Lilly-Installation wurde aktualisiert.]" + ColorConstants.COL_RESET);
            }

        }


        try {
            CoreSettings.getSettings().joinConfigFile(Definitions.DEFAULT_CONFIG_STREAM);
        } catch (IOException ignored) {
            writeLoggerError("Es gab einen Fehler beim Lesen der Standard-Konfiguration", "Jake");
        }


        if (args.length > 0 && args[0].equals("GUI")) {
            Definitions.GUI = true;
               // JOptionPane.showMessageDialog(new JFrame(), "Du bist im Gui - Modus, hier wird dich bald Jake begrüßen!",
               //         "INFO", JOptionPane.INFORMATION_MESSAGE);
            new GUICompile().setVisible(true);
            return;
        } // else no gui :D
        if (rs.success()) {
            CommandLineParser.interpret_settings(args);
            // if (args.length > 0 && args[1].charAt(0) != HIDDEN_ARG)
            // JakeWriter.out.println("Für diese Sitzung wurde diese Log-Datei verwendet:
            // \"" + JakeLogger.getTarget() + "\"");
            writeLoggerInfo("Die Arbeit wurde Abgeschlossen", "Jake");
            return;
        }
        writeLoggerError("Die Arbeit ist gescheitert. parse_args liefert: " + rs, "Jake");
    }
}
