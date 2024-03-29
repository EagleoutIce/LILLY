package de.eagle.lillyjakeframework.cmdline;

import de.eagle.gepard.modules.Projects;
import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.*;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.io.JakeWriter;

import static de.eagle.util.io.JakeLogger.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @file CommandLineParser.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Verarbeitet Befehlszeilenargumente
 */

/**
 * Dieser Parser kümmert sich darum, die Eingaben in der Befehlszeile
 * entsprechend aufzuarbeiten und zu verarbeiten Er besitzt als überarbeitung
 * der Cpp-Variante nun Kenntnis aller Einstellungen und kann auf der Basis eine
 * Plausibilitätsprüfung durchführen und Fehler früher offenbaren. Er wird
 * allerdings keine grundlegende Analyse (wie ist die Datei gültig?)
 * durchgeführt - dies obliegt dann der Interpretation der Einstellungen. Weiter
 * interessiert es den CommandLineParser auch nicht, ob die Einstellungen über
 * eine Datei weiter geladen werden, ihn beschäftigt nur, was über die
 * Kommandozeile kommt
 *
 * @implNote es gibt noch eine weitere Neuerung zum Cpp-Parser. während dieser
 *           CmdLine-Argumente durch Konfigurationen überschrieben hat so haben
 *           im Java modell die Kommandozeilen Argumente stets den Vorrang, sagt
 *           also die Befehlszeile name = A und eine Konfigurationsdatei name =
 *           B so wird name = A übernommen!
 *
 * @author Florian Sihler
 * @version 2.0.0
 * @since 2.0.0
 *
 * @see Settings
 */
public class CommandLineParser {

    /// Start-Segment eines Arguments
    public static final char ARGUMENT_PATTERN = '-';
    /// Verstecktes Argument
    public static final char HIDDEN_PATTERN = Definitions.HIDDEN_ARG;

    private static final CharSequence DOCUMENT_PATTERN = ".tex";
    private static final CharSequence CONFIG_PATTERN = ".conf";
    private static final String ASSIGNMENT_PATTERN = ":";
    private static final String ADD_ASSIGN_PATTERN = "+:";
    private static final String WHATSHORTCUT = ":";

    private static ArrayList<String> lazylocks;

    /**
     * Lädt die Einstellungen auf Basis der Kommandozeilenargumente
     *
     * @param args     die Kommandozeilenargumente
     * @param settings die Einstellungen - sie werden überschrieben. Alle
     *                 Einstellungen die hier gesetzt werden werden automatisch auf
     *                 immutable gesetzt und lassen sich somit nicht mehr
     *                 überschreiben
     *
     * @return 0 wenn alles gut gelaufen ist
     */
    public static ReturnStatus parse_args(String[] args, Settings settings) {
        lazylocks = new ArrayList<>();
        for (int current_arg = 0; current_arg < args.length; current_arg++) {
            writeLoggerDebug1("Parsing: " + args[current_arg], "CmdLP");
            current_arg = parse_next_arg(args, current_arg, settings);
            if(current_arg < 0) {
                JakeWriter.err.println("Abbruch!");
                return ReturnStatus.EXIT_FAILURE;
            }
        }
        writeLoggerInfo(
                "Sperre nun alle in der Kommandozeile modifizierten Argumente, damit keine Konfigurationsdatei dazwischenpfuscht :D",
                "CmdLP");
        for (String s : lazylocks)
            settings.get(s).lock();
        return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus interpret_settings(String[] args) {
        String op = CoreSettings.requestValue("S_OPERATION");
        if (CoreFunctions.functions_t.containsKey(op)) {
            // Operation ist valide
            CoreFunctions.functions_t.get(op).function.apply(args);
        } else {
            Settings projects = Definitions.projects();
            String[] names = Projects.getInstance().getAllProjectNames(projects);
            if(Arrays.asList(names).contains(op)){
                // apply
                // System.out.println("Applying: " + op);
                Projects.getInstance().executeProject(projects, op);
            } else {
                JakeWriter.out.format("%sDie Operation (=%s) ist so nicht gültig, "
                        + "schreibe \"%s help\" um Informationen über die unterstützten Operationen zu erhalten!%s%n",
                        ColorConstants.COL_ERROR, op, Definitions.PRG_NAME, ColorConstants.COL_RESET);
                return ReturnStatus.EXIT_FAILURE;
            }

        }
        return ReturnStatus.EXIT_SUCCESS;
    }

    /**
     * Entfernt Modifikatoren aus einem String (ASSIGN_PATTERN, ADD_ASSIGN_PATTERN)
     *
     * @param str Die zu bearbeitende Zeichenkette
     * @return Die bearbeitete Zeichenkette
     */
    public static String strip_modifications(String str) {
        if (str.contains(ADD_ASSIGN_PATTERN))
            str = str.substring(0, str.indexOf(ADD_ASSIGN_PATTERN));
        if (str.contains(ASSIGNMENT_PATTERN))
            str = str.substring(0, str.indexOf(ASSIGNMENT_PATTERN));
        return str;
    }

    /**
     * Bearbeitet die eingelesenen Argumente
     *
     * @param args        Liste der Argumente
     * @param current_arg index des aktuellen Arguments
     * @param settings    die Liste der zu bearbeitenden Einstellungen
     *
     * @return der Index des nächsten zu parsenden Argument (im Falle eines Skips)
     */
    protected static int parse_next_arg(String[] args, int current_arg, Settings settings) {
        String carg = args[current_arg]; // current arg
        if(carg.length()==0) return current_arg;
        String stripped = strip_modifications(carg);
        // At this point stripped will still contain the '-'
        if(carg.charAt(0) == ARGUMENT_PATTERN && !settings.containsKey(stripped.substring(1))){
            JakeWriter.err.println("Die Einstellung: \"" + stripped.substring(1) + "\" ist unbekannt!");
            return -1;
        }
        if (carg.charAt(0) != ARGUMENT_PATTERN) { // einzelnes Argument
            if (carg.contains(DOCUMENT_PATTERN)) { // ist Dokument
                writeLoggerDebug2("Identifiziert: einzelnes Argument (Dokument)", "CmdLP");
                settings.set("operation", "file_compile");
                settings.set("file", carg);
            } else if (carg.contains(CONFIG_PATTERN)) { // Ist Konfigurationsdatei
                writeLoggerDebug2("Identifiziert: einzelnes Argument (Konfiguration)", "CmdLP");
                settings.set("operation", "config");
                settings.set("file", carg);
            } else if (carg.startsWith(WHATSHORTCUT)){ // ist von der Form ':Hallo', wird direkt what zugewiesen als shortcut
                carg = carg.substring(1);
                writeLoggerDebug2("Identifiziert: Zuweisung (für what: \"" + carg + "\")", "CmdLP");
                settings.set("what", carg);
            } else { // normale operation
                writeLoggerDebug2("Identifiziert: einzelnes Argument (normal: \"" + carg + "\")", "CmdLP");
                settings.set("operation", carg);
            }
        } else if (carg.contains(ASSIGNMENT_PATTERN)) { // Zuweisung
            stripped = stripped.substring(1); // '-'
            writeLoggerDebug2("Identifiziert: Zuweisung (normal: \"" + carg + "\")", "CmdLP");

            if (current_arg == args.length - 1) {
                writeLoggerError("Die Option \"" + carg + "\" benötigt ein Argument! Dieses wurde nicht geliefert!",
                        "Parser");
                writeLoggerInfo("[Argument-Brief]: " + settings.get(stripped).getBrief(), "Parser");
            }
            if (carg.contains(ADD_ASSIGN_PATTERN)) { // Ist eine Addition
                if (current_arg >= args.length - 1)
                    throw new RuntimeException(
                            "Der Parameter: '" + args[current_arg] + "' benötigt ein nachfolgendes Argument!");
                boolean b = settings.add(stripped, args[++current_arg]);
                writeLoggerDebug2("Weise zu: \"" + stripped + "\" += \"" + args[current_arg]
                        + "\" (feedback: " + b + ")", "CmdLP");
                //settings.get(stripped).lock();
                if(!stripped.equals("file")) // this one has to be overwritten :D
                    lazylocks.add(stripped);
            } else { // ist normale zuweisung
                if (current_arg >= args.length - 1)
                    throw new RuntimeException(
                            "Der Parameter: '" + args[current_arg] + "' benötigt ein nachfolgendes Argument!");
                boolean b = settings.set(stripped, args[++current_arg]);
                writeLoggerDebug2("Weise zu: \"" + stripped + "\" = \"" + args[current_arg]
                        + "\" (feedback: " + b + ")", "CmdLP");
                //settings.get(stripped).lock();
                if(!stripped.equals("file")) // this one has to be overwritten :D
                    lazylocks.add(stripped);
            }
        } else { // Es handelt sich um einen Switch
            carg = carg.substring(1); // '-'
            if (settings.containsKey(carg)) {
                SettingDeskriptor sarg = settings.get(carg);
                writeLoggerDebug2("Toggle boolesche Funktion! (alter Wert: \"" + sarg.getValue() + "\")", "CmdLP");
                if (!sarg.type.equals(eSetting_Type.IS_SWITCH))
                    throw new IllegalArgumentException("Es handelt sich hier um keine boolesche Funktion!");
                // toggle:
                if (sarg.getValue().equals("false")) {
                    settings.set(carg, "true");
                } else if (sarg.getValue().equals(Definitions.S_TRUE)) {
                    settings.set(carg, "false");
                } else {
                    writeLoggerWarning("Die boolesche Option: \"" + carg + "\" ist falsch konfiguriert (\""
                            + sarg.getValue() + "\") setzte auf \"false\"", "Parser");
                }
                //writeLoggerDebug1("Sperre Switch", "Parser");
                //settings.get(carg).lock();
                lazylocks.add(carg);
            }
        }
        return current_arg;
    }

}