package de.eagle.lillyjakeframework.cmdline;

import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.*;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.io.JakeWriter;

import static de.eagle.util.io.JakeLogger.*;

/**
 * @file CommandLineParser.java
 * @author Florian Sihler
 * @version 1.0.10
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
 *           also die Befehlszeil name = A und eine Konfigurationsdatei name = B
 *           so wird name = A übernommen!
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @since 1.0.10
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

    /**
     * Lädt die Einstellungen auf Basis der Kommandozeilenargumente
     *
     * @param args     die Kommandozeilenargumente
     * @param settings die Einstellungen - sie werden überschrieben. Alle
     *                 Einstellungen die hier gesetzt werden werden automatisch auf
     *                 immutable gesetzt und lassen sich somit nichtmehr
     *                 überschreiben
     *
     * @return 0 wenn alles gut gelaufen ist
     */
    public static ReturnStatus parse_args(String[] args, Settings settings) {
        for (int current_arg = 0; current_arg < args.length; current_arg++) {
            writeLoggerDebug1("Parsing: " + args[current_arg], "CmdLP");
            current_arg = parse_next_arg(args, current_arg, settings);
        }

        return ReturnStatus.EXIT_SUCCESS;
    }

    public static ReturnStatus interpret_settings(String[] args) {
        String op = CoreSettings.requestValue("S_OPERATION");
        if(CoreFunctions.functions_t.containsKey(CoreSettings.requestValue("S_OPERATION"))){
            // Operation ist valide
            CoreFunctions.functions_t.get(op).function.apply(args);
        } else {
            JakeWriter.out.format("%sDie Operation (=%s) ist so nicht gültig, " +
                    "schreibe \"%s help\" um Informationen über die Unterstützten Operationen zu erhalten!%s%n",
                    ColorConstants.COL_ERROR, op, Definitions.PRG_NAME, ColorConstants.COL_RESET);
            return ReturnStatus.EXIT_FAILURE;
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
        if (carg.charAt(0) != ARGUMENT_PATTERN) { // einzelnes Argument
            if (carg.contains(DOCUMENT_PATTERN)) { // ist Dokument
                writeLoggerDebug2("Identifiziert: einzelnes Argument (Dokument)", "CmdLP");
                settings.set("operation", "file_compile");
                settings.set("file", carg);
                // TODO: teste auf Übungsblatt
            } else if (carg.contains(CONFIG_PATTERN)) { // Ist Konfigurationsdatei
                writeLoggerDebug2("Identifiziert: einzelnes Argument (Konfiguration)", "CmdLP");
                settings.set("operation", "config");
                settings.set("file", carg);
            } else { // normale operation
                writeLoggerDebug2("Identifiziert: einzelnes Argument (normal: \"" + carg + "\")", "CmdLP");
                settings.set("operation", carg);
            }
        } else if (carg.contains(ASSIGNMENT_PATTERN)) { // Zuweisung
            carg = carg.substring(1); // '-'
            writeLoggerDebug2("Identifiziert: Zuweisung (normal: \"" + carg + "\")", "CmdLP");

            if (current_arg == args.length - 1) {
                writeLoggerError("Die Option \"" + carg + "\" benötigt ein Argument! Dieses wurde nicht geliefert!",
                        "Parser");
                writeLoggerInfo("[Argument-Brief]: " + settings.get(strip_modifications(carg)).getBrief(), "Parser");
            }
            if (carg.contains(ADD_ASSIGN_PATTERN)) { // Ist eine Addition
                boolean b = settings.add(strip_modifications(carg), args[++current_arg]);
                writeLoggerDebug2("Weise zu: \"" + strip_modifications(carg) + "\" += \"" + args[current_arg]
                        + "\" (feedback: " + b + ")", "CmdLP");
            } else { // ist normale zuweisung
                boolean b = settings.set(strip_modifications(carg), args[++current_arg]);
                writeLoggerDebug2("Weise zu: \"" + strip_modifications(carg) + "\" = \"" + args[current_arg]
                        + "\" (feedback: " + b + ")", "CmdLP");
            }
        } else { // Es handelt sich um einen Switch
            carg = carg.substring(1); // '-'
            if (settings.containsKey(carg)) {
                SettingDeskriptor sarg = settings.get(carg);
                writeLoggerDebug2("Toggle boolesche Funktion! (alter Wert: \"" + sarg.getValue() + "\"", "CmdLP");
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
            }
        }
        return current_arg;
    }

}