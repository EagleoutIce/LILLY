package de.eagle.gepard.modules;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug3;
import static de.eagle.util.io.JakeLogger.writeLoggerWarning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.io.JakeLogger;

// Information:
// Alle "Entspricht ..." Kommentare gilt es zu ändern!

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Expandables {
    /**
     * Name der Buildrule-Box
     */
    public static final String box_name = "expandable";

    public static String finalName = "";

    public static String get_number(String str) {
        Matcher m = Pattern.compile("(?<num>[0-9]+)", Pattern.MULTILINE).matcher(str);
        return (m.find()) ? m.group("num") : "42";
    }

    /**
     * @return die Blaupause für die Einstellungen
     */
    public static Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Expandables", true, new HashMap<>());
        }
        return blueprint;
    }

    /**
     * Entspricht expandables Settings
     */
    private static Settings blueprint = null;

    /**
     * Entspricht get_default_expandables
     *
     * @return Standardeinstellungen
     */
    public static Settings getDefaults() {
        Settings settings = new Settings("<Default> Expandables", true, new HashMap<>());

        // Basics
        settings.emplace("TEXFILE", "Expandiert zur TEXDatei", eSetting_Type.IS_FILE,
                CoreSettings.requestValue("S_FILE"));
        settings.emplace("BASENAME", "Expandiert zum Dateinamen", eSetting_Type.IS_TEXT, "!!"); // new
                                                                                                // File(CoreSettings.requestValue("S_FILE")).toString().replaceFirst("[.][^.]+",
                                                                                                // "")
        settings.emplace("FINALNAME", "Expandiert zum endgültigen Namen der Datei, sofern bekannt und Kontextrelevant",
                eSetting_Type.IS_TEXT, "!!");

        settings.emplace("LOGFILE", "Expandiert zum Pfad zur Log-Datei", eSetting_Type.IS_FILE, "!!");

        settings.emplace("PDFFILE", "Expandiert zur PDFDatei", eSetting_Type.IS_FILE, "!!"); // new
                                                                                             // File(CoreSettings.requestValue("S_FILE")).toString().replaceFirst("[.][^.]+",
                                                                                             // "") + ".pdf"

        settings.emplace("LATEXARGS", "Zu verwendende LatexArgumente", eSetting_Type.IS_TEXT,
                "-shell-escape -enable-write18 -interaction=batchmode"); // LOCKED => TODO: CHANGE AND MAKE
                                                                         // CUSTOMIZEABLE
        settings.emplace("OUTPUTDIR", "Expandiert zum Ausgabeordner", eSetting_Type.IS_PATH,
                CoreSettings.requestValue("S_LILLY_OUT") + "/");
        settings.emplace("INPUTDIR", "Expandiert zur Eingabeordner", eSetting_Type.IS_PATH,
                CoreSettings.requestValue("S_LILLY_IN") + "/");
        settings.emplace("BOXMODES", "Expandiert zu den Boxmodi", eSetting_Type.IS_TEXTLIST,
                CoreSettings.requestValue("S_LILLY_BOXES"));
        settings.emplace("CLEANTARGETS", "Expandiert zu den zu löschenden Dateien", eSetting_Type.IS_TEXTLIST,
                CoreSettings.requestValue("S_LILLY_CLEANS"));

        settings.emplace("SIGNATURECOL", "Expandiert zur Signaturfarbe", eSetting_Type.IS_TEXT,
                CoreSettings.requestValue("S_LILLY_SIGNATURE_COLOR"));

        settings.emplace("AUTHOR", "Expandiert zum Autor", eSetting_Type.IS_TEXT,
                CoreSettings.requestValue("S_LILLY_AUTHOR"));
        settings.emplace("AUTHORMAIL", "Expandiert zur Autor-Mail", eSetting_Type.IS_TEXT,
                CoreSettings.requestValue("S_LILLY_AUTHORMAIL"));

        settings.emplace("NAMEPREFIX", "Expandiert zum Namenspräfix aller Dateien", eSetting_Type.IS_TEXT,
                CoreSettings.requestValue("S_LILLY_NAMEPREFIX"));
        settings.emplace("SEMESTER", "Expandiert zur Semesterzahl", eSetting_Type.IS_NUM,
                CoreSettings.requestValue("S_LILLY_SEMESTER"));
        settings.emplace("VORLESUNG", "Expandiert zur Vorlesung", eSetting_Type.IS_TEXT,
                CoreSettings.requestValue("S_LILLY_VORLESUNG"));

        settings.emplace("N", "Expandiert zur Nummer (wie z.B. Übungsblatt)", eSetting_Type.IS_NUM,
                CoreSettings.requestValue("S_LILLY_N"));

        settings.emplace("JOBCOUNT", "Expandiert zur Maximalanzahl an Jobs (deprecated)", eSetting_Type.IS_NUM,
                CoreSettings.requestValue("S_LILLY_JOBCOUNT"));

        settings.emplace("_LILLYARGS", "Expandiert zu den Argumenten für Lilly", eSetting_Type.IS_TEXT,
                "\\\\providecommand{\\\\LILLYxDOCUMENTNAME}{" + CoreSettings.requestValue("S_FILE") // there should be a
                                                                                                    // method to create
                                                                                                    // those xD TODO\n"
                                                                                                    // +
                        + "}\\\\providecommand{\\\\LILLYxOUTPUTDIR}{" + CoreSettings.requestValue("S_LILLY_OUT")
                        + "}\\\\providecommand{\\\\LILLYxPATH}{" + CoreSettings.requestValue("S_LILLY_IN")
                        + "}\\\\providecommand{\\\\AUTHOR}{" + CoreSettings.requestValue("S_LILLY_AUTHOR")
                        + "}\\\\providecommand{\\\\AUTHORMAIL}{" + CoreSettings.requestValue("S_LILLY_AUTHORMAIL")
                        + "}\\\\providecommand{\\\\LILLYxSemester}{" + CoreSettings.requestValue("S_LILLY_SEMESTER")
                        + "}\\\\providecommand{\\\\LILLYxVorlesung}{" + CoreSettings.requestValue("S_LILLY_VORLESUNG")
                        + "}\\\\providecommand{\\\\Hcolor}{" + CoreSettings.requestValue("S_LILLY_SIGNATURE_COLOR")
                        + "}"
                        + ((!CoreSettings.requestValue("S_LILLY_BIBTEX").isEmpty())
                                ? ("\\\\providecommand{\\\\LILLYxBIBTEX}{" + CoreSettings.requestValue("S_LILLY_BIBTEX")
                                        + "}")
                                : "")
                        + "\\\\providecommand{\\\\lillyPathLayout}{\\\\LILLYxPATHxDATA/Layouts"
                        + CoreSettings.requestValue("S_LILLY_LAYOUT_LOADER") + "}"
                        + "\\\\providecommand{\\\\LILLYxEXTERNALIZE}{"
                        + (CoreSettings.requestSwitch("S_LILLY_EXTERNAL") ? "TRUE" : "FALSE") + "}");

        settings.emplace("_C", "Ein unverfängliches Komma ^^", eSetting_Type.IS_TEXT, ",");

        settings.emplace("HOME", "Das Home-Verzeichnis des Benutzers", eSetting_Type.IS_PATH,
                PropertiesProvider.getHomeDirectory());

        settings.emplace("TRUE", "Wird zu true", eSetting_Type.IS_TEXT, "true");
        settings.emplace("FALSE", "Wird zu false", eSetting_Type.IS_TEXT, "false");

        /// JAKE LAZYS
        settings.emplace("@JAKEVER", "Expandiert zur Jake Version", eSetting_Type.IS_TEXT, Definitions.JAKE_VERSION);

        settings.emplace("@JAKECDATE", "OLD C++ compile stamp - DEPRECATED", eSetting_Type.IS_TEXT, "");
        settings.emplace("@JAKECTIME", "OLD C++ compile stamp - DEPRECATED", eSetting_Type.IS_TEXT, "");

        settings.emplace("@GITHUB", "Expandiert zum Github-Repo-Link", eSetting_Type.IS_TEXT,
                "https://github.com/EagleoutIce/LILLY");

        settings.emplace("@CONFPATH", "Pfad zur Konfigurationsdatei", eSetting_Type.IS_FILE,
                System.getenv("LILLY_JAKE_CONFIG_PATH"));

        settings.emplace("@AUTONUM", "Expandiert Speziell :D", eSetting_Type.IS_NUM, "!!");

        settings.emplace("@WAFFLE", "Ein wichtiges expandable (test etc.)", eSetting_Type.IS_TEXT,
                "GIVE ME THAT WAFFLE");

        settings.emplace("@SELTEXF", "Versucht eine Tex-Datei im aktuellen verzeichnis zu finden",
                eSetting_Type.IS_FILE, "!!");
        settings.emplace("@SELCONF", "Versucht eine Config-Datei im aktuellen verzeichnis zu finden",
                eSetting_Type.IS_FILE, "!!");

        return settings;
    }

    /**
     * Bearbeitet eine komplette Datei
     *
     * @param rulefiles die Liste der rulefiles (durch -TODO: settings- ':'
     *                  getrennt)
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     * @throws IOException Im Falle eines Fails von
     *                     {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public static Settings getExpandables(String rulefiles) throws IOException {
        if (rulefiles.isEmpty())
            return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        return parseRules(gp.parseFile(Expandables.box_name, Expandables.getBlueprint(), false));
    }

    /**
     * Entspricht dem Parse Teil für Expandables (foreach)
     *
     * @param boxes Array aller gefundenen Boxen
     *
     * @implNote Die MetaInformationen sind jeweils in brief kodiert.
     *
     * @see AbstractSettings#softJoin(AbstractSettings)
     * @see AbstractSettings#hardJoin(AbstractSettings)
     *
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     */
    public static Settings parseRules(GeneratorParser.JObject[] boxes) {
        Settings ret = new Settings("Expandables Settings");
        ret.softJoin(getDefaults());
        for (GeneratorParser.JObject box : boxes) {
            ret.softJoin(parseBox(box));
        }
        return ret;
    }

    /**
     * Parst eine einzelne Box
     *
     * @param box die Box
     *
     * @return null, wenn es keine Expandables-Box war, sonst die entsprechende
     *         Einstellung
     */
    public static Settings parseBox(GeneratorParser.JObject box) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Expandables");

        Settings settings = new Settings(box.getName());
        for (Map.Entry<String, SettingDeskriptor<String>> sd : box.config) {
            if (sd.getKey().length() == 0 || (sd.getKey().startsWith("@") && sd.getKey().length() < 2)) {
                writeLoggerWarning("Expandable-Bezeichner: \"" + sd.getKey() + "\" ist zu kurz (skipping)", "expands");
                continue;
            }
            settings.put(sd.getKey(), sd.getValue());
            writeLoggerDebug2("Expandable: " + sd.getKey() + " = " + sd.getValue().getValue(), "expands");
        }
        /*
         * settings.put(box.config.getValue("name"), SettingDeskriptor.create("",
         * "Box-Deskriptor für Buildrule (Gepard)",
         * createRuleFromData(CoreSettings.getSettings(), CoreSettings.getTranslator(),
         * box.config.getValue("display-name"), box.config.getValue("lilly-mode"),
         * box.config.getValue("complete").equals(Definitions.S_TRUE),
         * box.config.getValue("lilly-loader"), box.config.getValue("nameprefix"),
         * box.config.getValue("lilly-complete-prefix"),
         * box.config.getValue("name-pattern")))); writeLoggerDebug2("erhalten: " +
         * settings.toString(), "Buildrules");
         */
        return settings;
    }

    public static int rec_exp_calls = 0;

    public static String replaceByPattern(String suffix, String string, Pattern p) {
        String s;
        try {
            Optional<Path> os = Files.list(Paths.get(".")).filter(r -> r.toString().endsWith(suffix)).findFirst();
            if (os.isPresent()) {
                s = os.get().toFile().getName();
            } else {
                throw new RuntimeException(
                        "@[SELTEXF] oder ähnliche Vertreter benötigen auch eine Datei, die sie finden können!");
            }
        } catch (IOException e) {
            s = "NONE";
        }
        string = string.replaceAll(p.pattern(), Matcher.quoteReplacement(s));
        return string;
    }

    public static Settings expandsCS() throws IOException {
        Settings expandables = getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        for (var n : CoreSettings.settings) {
            n.setValue(SettingDeskriptor.create(n.getKey(), "Expanded Setting",
                    expand(expandables, n.getValue().getValue())));
        }
        return expandables;
    }

    public static Settings expandSettings(Settings settings) throws IOException {
        Settings expandables = getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        for (var n : settings) {
            n.setValue(SettingDeskriptor.create(n.getKey(), "Expanded Setting",
                    expand(expandables, n.getValue().getValue())));
        }
        return expandables;
    }

    public static String expand(Settings expandables, String string) {
        if (rec_exp_calls > Definitions.MAX_SETTINGS_REC) {
            throw new RuntimeException("Maximale Expansionstiefe für Expandables erreicht! Der Request: \"" + string
                    + "\" führt zu einer zu tiefen Rekursion! In der Erweiterung eines Expandables darf dieses "
                    + "selbst nicht verwendet werden!");
        }
        rec_exp_calls++;
        String last = "";
        int counter = 0;
        Pattern p;
        do {
            last = string;
            for (Map.Entry<String, SettingDeskriptor<String>> sd : expandables) {
                if (sd.getKey().startsWith("@"))
                    p = Pattern.compile("@\\[" + sd.getKey().substring(1) + "]", Pattern.MULTILINE);
                else
                    p = Pattern.compile("\\$[{(]" + sd.getKey() + "[})]", Pattern.MULTILINE); // we don't care about
                                                                                              // unbalanced
                Matcher m = p.matcher(string);
                if (m.find()) {
                    writeLoggerDebug3("Expanding [" + m.group() + "] in: " + string + " /w " + sd.getValue().getValue(),
                            "expander");
                    // here well select specific cases
                    switch (sd.getKey()) {
                    case "@AUTONUM":
                        string = string.replaceAll("\\@\\[AUTONUM\\]",
                                Matcher.quoteReplacement(get_number(expand(expandables, "$(TEXFILE)"))));
                        break;
                    case "@SELTEXF":
                        string = replaceByPattern(".tex", string, p);
                        break;
                    case "@SELCONF":
                        string = replaceByPattern(".conf", string, p);
                        break;
                    case "BASENAME": // could be a method, but who cares? :D
                        string = string.replaceAll(p.pattern(), Matcher.quoteReplacement(
                                expand(expandables, "$(TEXFILE)").replaceFirst("[.][a-zA-Z0-9]{1,7}", "")));
                        break;
                    case "PDFFILE":
                        string = string.replaceAll(p.pattern(), Matcher.quoteReplacement(
                                expand(expandables, "$(TEXFILE)").replaceFirst("[.][a-zA-Z0-9]{1,7}", "") + ".pdf"));
                        break;
                    case "FINALNAME":
                        string = string.replaceAll(p.pattern(), Matcher.quoteReplacement(finalName));
                        break;
                    case "LOGFILE":
                        string = string.replaceAll(p.pattern(), JakeLogger.target);
                        break;
                    default:
                        if (string.startsWith("!!"))
                            JakeWriter.err.println("unsupported System based lazy eval! pls report this");
                        else {
                            string = string.replaceAll(p.pattern(), Matcher.quoteReplacement(sd.getValue().getValue()));
                        }
                    }
                    if (sd.getValue().getValue().equals("!!"))
                        writeLoggerDebug3("  =>  Lazy expand to: \"" + string + "\"", "expander");
                }
            }
            counter++;
        } while (!last.equals(string) && counter < 8);

        if (counter == 8) {
            JakeWriter.err.println("Recursive Expansion limit reached!");
        }
        rec_exp_calls = 0;
        return string;
    }
}
