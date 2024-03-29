package de.eagle.lillyjakeframework.core;

/**
 * @file CoreFunctions.java
 * @author Florian Sihler
 * @version 2.2.0
 *
 * @brief Enthält die Funktionen der Kommandozeile
 */

import de.eagle.gepard.modules.Generators;
import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.TokenMatch;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.lillyjakeframework.cmdline.Autocomplete;
import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.FunctionCollector;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.Cloner;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.Executer;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug3;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;

import de.eagle.gepard.modules.Expandables;

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
            Map.entry("generate",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_generate", "Startet den Generator in 'what'",
                            CoreFunctions::fkt_generator)),
            Map.entry("config",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_config",
                            "Lädt die Einstellungen aus der Datei 'file'", CoreFunctions::fkt_config)),
            Map.entry("get",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_get",
                            "Sucht nach Grafiken die settings[\"what\"] enthalten!", CoreFunctions::fkt_get)),
            Map.entry("docs",
                    new FunctionDeskriptor<String[], ReturnStatus>("fkt_docs", "Zeigt die Dokumentation an.",
                            CoreFunctions::fkt_docs)),
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
        PropertiesProvider.printMetadata();
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
        PropertiesProvider.printMetadata();
        JakeWriter.out.format("Benutzung:%n%n%s%s [operation=help] [file] [options]%s%n%noperations:%n", ColorConstants.COL_GOLD,
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
                "\nfile:\nAngabe gemäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und kompiliert damit die Datei. Es kann auch eine Konfigurationsdatei \"xxx.conf\" übergeben werden. In diesem Fall wird die Operation automatisch auf config gesetzt.");
        JakeWriter.out.println("\noptions:\nAllgemeine Einstellungen können über \"-key" + Definitions.ASS_PATTERN
                + "value\" gesetzt werden (\"-key\" für boolesche). " + "So setzt: \"-path" + Definitions.ASS_PATTERN
                + " /es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: \"/es/gibt/kuchen\". "
                + "Weiter ist es möglich mit '" + Definitions.ASS_PATTERN + "' values hinzuzufügen. Alle aktuellen Einstellungen können der Dokumentation entnommen werden (jake docs).");

        JakeWriter.out.println(
                ("\nBeispiel:\n  jake test.tex -lilly-modes: default -lilly-author: \"Detlef Dieter\" -lilly-boxes+: LIMERENCE\n\nÜbrigens: Die Konfiguration 'what' die zum Beispiel für den Generator gebraucht wird, kann auch durch einen Doppepunkt angeführt werden. So genügt: \n  jake generate :mitschrieb"));
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

    public static ReturnStatus fkt_generator(String[] cmd) {
        // Das Ziel wird durch What übermittelt
        writeLoggerDebug3("jetzt in: fkt_generator", "func");
        try {
            Settings generators = Generators.getInstance()
                    .getGenerators(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
            Generators.getInstance().launchGenerator(generators, CoreSettings.requestValue("S_WHAT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnStatus.EXIT_SUCCESS;
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

    public static byte[] getCheckSum(InputStream is) throws NoSuchAlgorithmException, IOException {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = is.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        return complete.digest();
    }

    public static String getMD5Checksum(InputStream is) throws NoSuchAlgorithmException, IOException {
        byte[] b = getCheckSum(is);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * should (theoretically :P) copy the Documentation to the temp-dir and reveil
     * it :D
     */
    public static ReturnStatus fkt_docs(String[] cmd) {

        String _path = PropertiesProvider.getTempPath() + "/Lilly-Dokumentation.doc.pdf";
        // Does exist?, we DON'T CARE xD, it copies it freaking fast => maybe insert up
        // to date check in the future?
        boolean doclone = true;
        File f;
        if ((f = new File(_path)).exists()) {
            // Check if Resource has changed:
            String intern = "", extern = "";
            try {
                intern = getMD5Checksum(
                        CoreFunctions.class.getResourceAsStream("/COMPACT-Lilly-Dokumentation.doc.pdf"));
                extern = getMD5Checksum(new FileInputStream(f));
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
            doclone = !intern.equals(extern);
        }
        // Clone to target
        if (doclone) {
            JakeWriter.out.println("Die Dokumentation wird kopiert...");
            try {
                Cloner.cloneFileRessource("/COMPACT-Lilly-Dokumentation.doc.pdf", _path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // }
        JakeWriter.out.println(
                "Information: Die mit Jake mitgelieferte Dokumentation ist komprimiert und enthält weniger Beispielgrafiken und Ausführungen. Für eine volle Dokumentation siehe hier: https://github.com/EagleoutIce/LILLY");
        try {
            Desktop.getDesktop().open(new File(_path));
        } catch (IOException e) {
            JakeWriter.err.println("Opening failed for some reason, trying to open with 'xdg-open'");
            Executer.runBashCommand("xdg-open \"" + _path + "\"");
            // e.printStackTrace();
        }
        return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus fkt_get(String[] cmd) {
        JakeWriter.out.println("Zeige alle Grafiken die mit Lilly zur Verfügung stehen.");
        // Settings expandables = new Settings("expandables");
        String lPath = "";
        try {
            /* expandables = */ Expandables.getInstance().expandsCS();
            lPath = Executer.runBashCommand("printf " + CoreSettings.requestValue("S_LILLY_PATH")).readLine();
        } catch (IOException e) {

        }
        // Note: this does only work if dirname, locate and printf will work this has to be assured on non-apt idstrs
        JakeWriter.out.println("Öffne: \"" + lPath + "/source/Data/Graphics/all-OUT/all.pdf\"");
        File target = new File(lPath + "/source/Data/Graphics/all-OUT/all.pdf");
        if (!target.isFile() || CoreSettings.requestValue("S_WHAT").equals("force")) {
            JakeWriter.out.format(
                    "%sEs scheint, als wären die Grafikressourcen bei dir noch nicht kompiliert wurden!%s%n",
                    ColorConstants.COL_GOLD, ColorConstants.COL_RESET);
            String result = CommandLine
                    .get_answer("Sollen die Grafiken erzeugt werden? Dies kann eine Weile dauern! [y/n]> ");
            if (result.equals("N")) {
                JakeWriter.out.println("Abbruch");
                return ReturnStatus.EXIT_FAILURE;
            }
            // finding the GetAll.py
            File getallpy = new File(lPath + "/source/Data/Graphics/GetAll.py");
            if (!getallpy.isFile()) {
                JakeWriter.err
                        .println("Die Datei " + getallpy.getAbsolutePath() + " scheint fehlerhaft zu sein. Abbruch!");
                return ReturnStatus.EXIT_FAILURE;
            }
            // Execute the Prerender:
            BufferedReader br = Executer.runBashCommand(
                    "cd " + getallpy.getParentFile().getAbsolutePath() + " && python3 GetAll.py prerender");
            if (br != null)
                br.lines().forEach(JakeWriter.out::println);
            JakeWriter.out.println("Prerender Abgeschlossen. Generiere die PDF...");
            br = Executer.runBashCommand("cd " + getallpy.getParentFile().getAbsolutePath() + " && python3 GetAll.py");
            if (br != null)
                br.lines().forEach(JakeWriter.out::println);
            JakeWriter.out.println("Generierung Abgeschlossen");
        }
        // Multiplattform:
        // Desktop.getDesktop().open(myFile);
        try {
            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            JakeWriter.err.println("Opening failed for some reason, trying to open with 'xdg-open'");
            Executer.runBashCommand("xdg-open \"" + lPath + "/source/Data/Graphics/all-OUT/all.pdf" + "\"");
            // e.printStackTrace();
        }
        // String suff = "";
        // if(!CoreSettings.requestValue("S_WHAT").isEmpty())
        // suff = "\":" + CoreSettings.requestValue("S_WHAT") + "\"";
        // Executer.runBashCommand("python3 " +
        // CoreSettings.requestValue("S_LILLY_PATH") + "/source/Data/Graphics/GetAll.py
        // " + suff);
        // JakeWriter.out.println("Die Erzeugung erfolgt im Hintergrund, das Ergebnis
        // wird dann nach der Fertigstellung präsentiert.");
        return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus fkt_autoget(String[] cmd) {
        JakeWriter.out.println(Autocomplete.parse_cmd_autocomplete(cmd));
        return new ReturnStatus((0));
    }

    public static ReturnStatus fkt_update(String[] cmd) {
        // JakeWriter.out.println("Bisher wird Lilly durch die aktualisierung von Jake
        // automatisch aktualisiert. Eine neue Version von Jake kann über GitHub bezogen
        // werdne: https://github.com/EagleoutIce/LILLY.");

        // init:
        URL url;
        JakeWriter.out.println("Information: Aktuell wird nicht überprüft ob die von dir aktuelle Installierte Version neuer als die Zielversion ist!");
        try {
            url = new URL("https://github.com/EagleoutIce/LILLY/releases/latest/download/jake.jar");
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            System.out.println("Target: " + PropertiesProvider.getThisPath());
            FileOutputStream fileOutputStream = new FileOutputStream(PropertiesProvider.getThisPath());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /* Placeholder */ return ReturnStatus.EXIT_SUCCESS;
    }
}
