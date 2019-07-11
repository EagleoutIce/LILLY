package de.eagle.lillyjakeframework;

import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.lillyjakeframework.gui.core.InstallJake;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeWriter;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

        if(!PropertiesProvider.isInstalled()) {
            if(args.length > 0 && args[0].equals("GUI")) {
                InstallJake.main(args);
            } else { // commandline based:
                for(String s : PropertiesProvider.getInstaller(false)){
                    System.out.println(s);
                }
                return;
            }
        }

        writeLoggerInfo("\"" + PRG_BRIEF + "\" beginnt nun mit seiner Arbeit","Jake");
        if (args.length < 2 || args[1].charAt(0) == HIDDEN_ARG) {
            // setup log-Deskriptor & ld_config by configparser
            //if()
        }

        if(args.length > 0 && args[0].equals("GUI")){
            Definitions.GUI = true;
            JOptionPane.showMessageDialog(new JFrame(), "Du bist im Gui - Modus, hier wird dich bald Jake begrüßen!", "INFO", JOptionPane.INFORMATION_MESSAGE);
            return;
        } // else no gui :D
        ReturnStatus rs ;
        if((rs = CommandLineParser.parse_args(args, CoreSettings.settings)).success()) {
            CommandLineParser.interpret_settings(args);
            writeLoggerInfo("Die Arbeit wurde Abgeschlossen","Jake");
        }
        writeLoggerError("Die Arbeit ist gescheitert. parse_args liefert: " + rs,"Jake");

        /*
        JakeWriter.out.println("Da es noch kein gescheites cmdline parsing gibt - grumpy - wird die datei dummy.tex hardgecoded.");
        JakeWriter.out.println("Ab jetzt auch JakeWriter anstelle von System.out, System.err und System.in verwenden!");
        JakeWriter.out.println("Dies ist dann für die GUI-Entwicklung nötig");
        JakeWriter.out.println("Wenn der Dialog getestet werden soll, einfach die dummy.tex löschen!");
        JakeCompile.compile(new String[]{""}); */
        // load settings & interpret settings otherwise exit with failure

    }
}
