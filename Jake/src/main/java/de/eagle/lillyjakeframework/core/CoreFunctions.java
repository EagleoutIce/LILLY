package de.eagle.lillyjakeframework.core;

/**
 * @file CoreFunctions.java
 * @author Raphael Straub
 * @version 1.0.10
 *
 * @brief Enthält viele Funktionen
 */

import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.TokenMatch;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.lillyjakeframework.cmdline.Autocomplete;
import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.lillyjakeframework.installer.LinuxInstaller;
import de.eagle.lillyjakeframework.installer.MacOSInstaller;
import de.eagle.lillyjakeframework.installer.WindowsInstaller;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.datatypes.FunctionCollector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import static de.eagle.util.io.JakeLogger.*;

/**
 * Enthält viele Funktionen
 *
 * @author Raphael Straub
 * @version 1.0.10
 * @since 1.0.10
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
                            "Erstellt ein makefile für settings[\\\"file\\\"]", CoreFunctions::fkt_compile)),
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
                            "Sucht nach Grafiken die settings[\\\"what\\\"] enthalten!", CoreFunctions::fkt_get)),
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
            JakeWriter.out.println("Information: Die '[' ']' gehören jeweils nicht zum Wert, sie dienen lediglich der Übersicht! Durch '=>' gekennzeichnet ergibt sich die erweiterte Variante des Ausdrucks");
            for (String s : CoreSettings.getSettings().dump()) {
                JakeWriter.out.println(s);
            }
        } catch (IOException e) {
            // e.printStackTrace();
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
        writeLoggerInfo("Benutzung:\n\n!{program} [options=help] [file]\n\n[options]:\n", "func");
        functions_t.entrySet().stream()
                .filter(m -> m.getKey().length() > 0 && m.getKey().charAt(0) != Definitions.HIDDEN_ARG)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(m -> JakeWriter.out.format("  %-15s: " + m.getValue().brief + "%n", m.getKey()));
        JakeWriter.out.println(
                "\n[file]:\nAngabe gemäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \\"
                        + " ein generelles makefile für \"xxx.tex\".");
        JakeWriter.out.println("\nnote:\nAllgemeine Einstellungen können über \"-key" + Definitions.ASS_PATTERN
                + "value\" gesetzt werden (\"-key\" für boolesche). " + "So setzt: \"-path" + Definitions.ASS_PATTERN
                + "/es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: \"/es/gibt/kuchen\". "
                + "Weiter ist es möglich mit '" + Definitions.ASS_PATTERN + "' values hinzuzufügen.");
        return ReturnStatus.EXIT_SUCCESS;
    }

    /**
     *
     * @param cmd
     * @return
     */
    public static ReturnStatus fkt_install(String[] cmd) {
        PropertiesProvider.getInstaller(false).automatedInstall();
        return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus fkt_compile(String[] cmd) {
        writeLoggerDebug3("jetzt in: fkt_compile", "func");
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

    public static ReturnStatus fkt_config(String[] cmd) {
        writeLoggerDebug3("jetzt in: fkt_config", "func");
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

    public static ReturnStatus fkt_get(String[] cmd) {
        /* Placeholder */ return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus fkt_autoget(String[] cmd) {
        JakeWriter.out.println(Autocomplete.parse_cmd_autocomplete(cmd));
        return new ReturnStatus((0));
    }

    public static ReturnStatus fkt_update(String[] cmd) {
        /* Placeholder */ return ReturnStatus.EXIT_SUCCESS;
    }
}
