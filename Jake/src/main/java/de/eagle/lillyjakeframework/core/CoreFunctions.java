package de.eagle.lillyjakeframework.core;

import de.eagle.gepard.modules.Expandables;

/**
 * @file CoreFunctions.java
 * @author Raphael Straub
 * @version 2.0.0
 *
 * @brief Enthält die Funktionen der Kommandozeile
 */

import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.TokenMatch;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.lillyjakeframework.cmdline.Autocomplete;
import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.Cloner;
import de.eagle.util.helper.Executer;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.FunctionCollector;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import static de.eagle.util.io.JakeLogger.*;

/**
 * Enthält viele Funktionen
 *
 * @author Raphael Straub
 * @author Florian Sihler
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CoreFunctions {
    /// Wird noch nicht genutzt
    public static byte RECURSIVE_CALLCOUNTER = 0;

    /// "Map" für alle Funktionen der CoreFunctrions Klasse
    public static final FunctionCollector<String[], ReturnStatus> functions_t = new FunctionCollector<>(Map.ofEntries(
            Map.entry("help",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_help", "Zeigt diese Hilfe an",
                            CoreFunctions::fkt_help)),
            Map.entry("dump",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_dump", "Zeigt alle settings und ihre Werte an",
                            CoreFunctions::fkt_dump)),
            Map.entry("file_compile",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_compile",
                            "Erstellt ein makefile für settings[\"file\"]", CoreFunctions::fkt_compile)),
            Map.entry("install",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_install", "Versucht LILLY zu installieren",
                            CoreFunctions::fkt_install)),
            Map.entry("tokentest",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_tokentest",
                            "Testet den Tokenizer auf seine Funktionalität", CoreFunctions::fkt_tokentest)),
            Map.entry("config",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_config",
                            "Lädt die Einstellungen aus der Datei 'file'", CoreFunctions::fkt_config)),
            Map.entry("get",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_get",
                            "Sucht nach Grafiken die settings[\"what\"] enthalten!", CoreFunctions::fkt_get)),
            Map.entry("docs",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_docs",
                            "Zeigt die Dokumentation an.", CoreFunctions::fkt_docs)),
            Map.entry("update",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_update",
                            "Versucht Lilly & Jake zu aktualisieren", CoreFunctions::fkt_update)),
            Map.entry("_get", new FunctionDeskriptor<String[], ReturnStatus>("fkt_autoget",
                    "Interne Funktion, liefert Alles für die Autovervollständigung", CoreFunctions::fkt_autoget))));

    /**
     * Wird in der Zukunft alle aktuellen Settings ausgeben
     *
     * @param cmd Wird nicht genutzt, aber wird vom FunctionCollector so gefordert
     * @return ReturnStatus(0)
     */
    public static ReturnStatus fkt_dump(String[] cmd) {
        writeLoggerDebug1("Liefere die Konfigurationen (fkt_dump)", "func");

        try {
            JakeWriter.out.println("Settings Dump: ");
            JakeWriter.out.println(
                    "Information: Die '[' ']' gehören jeweils nicht zum Wert, sie dienen lediglich der Übersicht! Durch '=>' gekennzeichnet ergibt sich die erweiterte Variante des Ausdrucks");
            for (String s : CoreSettings.getSettings().dump()) {
                JakeWriter.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ReturnStatus.EXIT_SUCCESS;
    }

    /**
     * Gibt die Hilfe, also alle Funktionen in dieser Klasse und die dazugehörigen
     * Beschreibungen aus
     *
     * @param cmd Wird nicht genutzt, aber wird vom FunctionCollector so gefordert
     * @return ReturnStatus(0)
     */
    public static ReturnStatus fkt_help(String[] cmd) {
        writeLoggerDebug1("Gebe die Hilfe aus (fkt_help)", "func");
        JakeWriter.out.format("Benutzung:%n%n%s%s [options=help] [file]%s%n%n[options]:%n", ColorConstants.COL_GOLD,
                Definitions.PRG_NAME, ColorConstants.COL_RESET);
        functions_t.entrySet().stream()
                .filter(m -> m.getKey().length() > 0 && m.getKey().charAt(0) != Definitions.HIDDEN_ARG)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(m -> JakeWriter.out.format("  %-15s: %s%n", m.getKey(), m.getValue().brief));
        // JakeWriter.out.println();
        JakeWriter.out.format("  %-15s: %s%n", "GUI", "Startet Jake im GUI-Modus");
        JakeWriter.out.format("  %-15s: %s%n", "DEI", "Versucht Jake zu Deinstallieren");
        JakeWriter.out.format("  %-15s: %s%n", "REI", "Versucht Jake zu Reinstallieren (Entwicklerfunktion)");

        JakeWriter.out.println(
                "\n[file]:\nAngabe gemäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \\"
                        + " ein generelles makefile für \"xxx.tex\".");
        JakeWriter.out.println("\nnote:\nAllgemeine Einstellungen können über \"-key" + Definitions.ASS_PATTERN
                + "value\" gesetzt werden (\"-key\" für boolesche). " + "So setzt: \"-path" + Definitions.ASS_PATTERN
                + " /es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: \"/es/gibt/kuchen\". "
                + "Weiter ist es möglich mit '" + Definitions.ASS_PATTERN + "' values hinzuzufügen.");

        JakeWriter.out.println(
                ("\nBeispiel:\n  jake test.tex -lilly-modes: default -lilly-author: \"Detlef Dieter\" -lilly-boxes+: LIMERENCE"));
        return ReturnStatus.EXIT_SUCCESS;
    }

    /**
     *
     * @param cmd
     * @return
     */
    public static ReturnStatus fkt_install(String[] cmd) {
        try {
            Expandables.getInstance().expandsCS();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertiesProvider.getLillyInstaller(false).automatedInstall();
        // Hier wird nicht Jake sondern LILLY entsprechend installiert - JAY
        return ReturnStatus.EXIT_SUCCESS;
    }

    public static boolean customLoadConfig(String[] cmd) {
        if (!_use_config && CoreSettings.requestSwitch("S_AUTOCONF")) { // Automatically search and Pick a
                                                                              // Config-File
            JakeLogger.writeLoggerDebug1("Es wird automatisch eine Konfigurationsdatei gesucht, da Autoconf true ist",
                    "func");
            Configurator c = null;
            try {
                String p = "";
                String t = CoreSettings.requestValue("S_FILE").replaceFirst("[.][a-zA-Z0-9]{1,7}", ".conf");
                if (new File("./jake.conf").isFile())
                    p = "./jake.conf";
                else if (new File(t).isFile())
                    p = t;
                _use_config = true;
                if (p.equals(""))
                    return false;
                JakeWriter.out.println("Nutze Konfigurationsdatei: " + p);
                c = new Configurator(p);
                return (c.parse_settings(CoreSettings.settings, false) == 0);
                // return CommandLineParser.interpret_settings(cmd).success();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static ReturnStatus fkt_compile(String[] cmd) {
        writeLoggerDebug3("jetzt in: fkt_compile", "func");
        customLoadConfig(cmd);
        try {
            return JakeCompile.compile(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ReturnStatus fkt_tokentest(String[] cmd) {
        JakeWriter.out.println("Einzelne Gruppen werden mit \"~\" getrennt!");
        try {
            Tokenizer t = new Tokenizer(CoreSettings.requestValue("S_FILE"));
            for (TokenMatch tm : t) {
                JakeWriter.out.format("%s#%n", String.join("~", tm.getMatchings()));
            }
        } catch (FileNotFoundException ignored) {
        }

        return ReturnStatus.EXIT_SUCCESS;
    }

    /// @brief Markiert für die Übersicht, ob eine Konfigurationsdatei verwendet
    /// wird.
    public static boolean _use_config = false;

    public static ReturnStatus fkt_config(String[] cmd) {
        writeLoggerDebug3("jetzt in: fkt_config", "func");
        _use_config = true;
        if (RECURSIVE_CALLCOUNTER++ > Definitions.MAX_SETTINGS_REC) {
            JakeWriter.err.format(
                    "Du hast das Limit an Konfigurationsaufrufen (%d) erreicht! Mehr erscheint wirklich nicht sinnvoll!",
                    Definitions.MAX_SETTINGS_REC);
            throw new RuntimeException(
                    "Maximum recursive Config-Call-Depth! Assert that you've overwritten operation!");
        }
        Configurator c = null;
        try {
            c = new Configurator(CoreSettings.requestValue("S_FILE"));
            c.parse_settings(CoreSettings.settings, false);
            return CommandLineParser.interpret_settings(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * should (theoretically :P) copy the Documentation to the temp-dir and reveil it :D
     */
    public static ReturnStatus fkt_docs(String[] cmd) {

        String _path = PropertiesProvider.getTempPath()+ "/Lilly-Dokumentation.doc.pdf";
        // Does exist?, we DON'T CARE xD, it copies it freaking fast => maybe insert up to date check in the future?
        //if(!new File(_path).exists()){
            // Clone to target
            try {
                Cloner.cloneFileRessource("/Lilly-Dokumentation.doc.pdf", _path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        //}

        try {
            Desktop.getDesktop().open(new File(_path));
        } catch (IOException e) {
            JakeWriter.err.println("Opening failed for some reason, trying to open with 'xdg-open'");
            Executer.runBashCommand("xdg-open \"" + _path +"\"");
            //e.printStackTrace();
        }
        return ReturnStatus.EXIT_SUCCESS;
    }


    public static ReturnStatus fkt_get(String[] cmd) {
        JakeWriter.out.println("Zeige alle Grafiken die mit Lilly zur Verfügung stehen.");
        Settings expandables = new Settings("expandables");
        String lPath = "";
        try {
            expandables = Expandables.getInstance().expandsCS();
            lPath = Executer.runBashCommand("printf " + CoreSettings.requestValue("S_LILLY_PATH")).readLine();
        } catch (IOException e) {

        }

        JakeWriter.out.println("Öffne: \"" + lPath + "/source/Data/Graphics/all-OUT/all.pdf\"");

        // Multiplattform:
        // Desktop.getDesktop().open(myFile);
        try {
            Desktop.getDesktop().open(
                    new File(lPath + "/source/Data/Graphics/all-OUT/all.pdf"));
        } catch (IOException e) {
            JakeWriter.err.println("Opening failed for some reason, trying to open with 'xdg-open'");
            Executer.runBashCommand("xdg-open \"" + lPath + "/source/Data/Graphics/all-OUT/all.pdf" + "\"");
            //e.printStackTrace();
        }
        // String suff = "";
        // if(!CoreSettings.requestValue("S_WHAT").isEmpty())
        //     suff = "\":" + CoreSettings.requestValue("S_WHAT") + "\"";
        // Executer.runBashCommand("python3 " + CoreSettings.requestValue("S_LILLY_PATH") + "/source/Data/Graphics/GetAll.py " + suff);
        // JakeWriter.out.println("Die Erzeugung erfolgt im Hintergrund, das Ergebnis wird dann nach der Fertigstellung präsentiert.");
        return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus fkt_autoget(String[] cmd) {
        JakeWriter.out.println(Autocomplete.parse_cmd_autocomplete(cmd));
        return new ReturnStatus((0));
    }

    public static ReturnStatus fkt_update(String[] cmd) {
        JakeWriter.out.println("Bisher wird Lilly durch die aktualisierung von Jake automatisch aktualisiert. Eine neue Version von Jake kann über GitHub bezogen werdne: https://github.com/EagleoutIce/LILLY.");
        /* Placeholder */ return ReturnStatus.EXIT_SUCCESS;
    }
}
