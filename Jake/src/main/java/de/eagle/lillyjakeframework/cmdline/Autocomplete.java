package de.eagle.lillyjakeframework.cmdline;

import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.enumerations.eSetting_Type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

/**
 * Implementation der j_Autocomplete.cpp
 * Stellt die Daten für eine Autovervollständig zur Verfügung.
 * Unterstützt wird diese in Bash, zsh und auf MacOS-Bash.
 *
 * @author Florian Sihler
 *
 * @version 1.0.10
 * @since 1.0.10
 */
public class Autocomplete {

    /**
     * Schaut, ob bereits eine Funktion geben wurde
     * @param inside_here übergebene Werte
     * @return true, wenn ja, sonst false
     */
    public static boolean was_there_what(String[] inside_here) {
        for(Map.Entry<String,FunctionDeskriptor<String[], ReturnStatus>> fd : CoreFunctions.functions_t.entrySet()) {
            if(!fd.getKey().startsWith(String.valueOf(Definitions.HIDDEN_ARG))) {
                for (String s : inside_here) {
                    if(fd.getKey().equals(s) || s.endsWith(".tex") || s.endsWith(".conf"))
                        return true;
                }
            }
        }
        return false;
    }

    public static String print_settings() {
        StringBuilder str = new StringBuilder();
        for(var s : CoreSettings.settings) {
            if(s.getValue().type.equals(eSetting_Type.IS_SWITCH)) {
                str.append("-").append(s.getKey()).append("\t");
            } else {
                str.append("-").append(s.getKey()).append(":\t");
                if(s.getValue().type.equals(eSetting_Type.IS_SWITCH))
                    str.append("-").append(s.getKey()).append("+:\t");
            }
        }
        return str.toString();
    }

    /**
     * Gibt alle zur Verfügung stehenden Optionen aus
     * @return String für das Autocomplete Skript
     */
    public static String print_options() {
        StringBuilder str = new StringBuilder();
        for(var s : CoreFunctions.functions_t.entrySet()){
            if(!s.getKey().startsWith(String.valueOf(Definitions.HIDDEN_ARG)))
                str.append(s.getKey()).append("\t");
        }
        return str.toString();
    }

    /**
     * Liefert entsprechend der Eingaben alle möglichen Ausgaben.
     * Impliziert somit auch (zsh) Informationen für gewisse Argumente
     * @param cmd_line Die bisherigen Befehle
     * @return String für das Autocomplete Skript
     */
    public static String parse_cmd_autocomplete(String[] cmd_line){
        StringBuilder str = new StringBuilder();
        if(cmd_line == null || cmd_line.length == 0 || !was_there_what(cmd_line)) {
            // Gab es noch keine operation - so schlagen wir stehts optionen for
            str.append(print_options());
            try { // gibt alle tex und config dateien mit aus
                str.append(String.join("\t",Files.list(Paths.get("."))
                        .filter(x -> (x.endsWith(".tex") || x.endsWith(".conf")))
                        .map(Path::toString)
                        .toArray(String[]::new))
                );
            } catch (IOException ignored) {
                // e.printStackTrace();
            }
            return str.toString();
        }

        // Erhalte typ des letzten Argument:
        // std::cout << strip_mod(a[a.size()-2].substr(1)) << std::endl; // kann nicht scheitern, da mind > 1

        String arg = CommandLineParser.strip_modifications(cmd_line[cmd_line.length-1].substring(1));
        if(cmd_line[cmd_line.length-1].contains(Definitions.ASS_PATTERN) &&
            CoreSettings.settings.containsKey(arg)) {
            SettingDeskriptor t = CoreSettings.settings.get(arg);
            switch (t.type){
                case IS_FILE:
                    try {
                        str.append(String.join("\t",Files.list(Paths.get("."))
                                .filter(x -> (x.toFile().isFile()))
                                .map(Path::toString)
                                .toArray(String[]::new)));
                    } catch (IOException ignored) { }
                    break;
                case IS_NUM:
                    str.append("Erwarte: Zahl\tBitte gib eine Zahl ein!\t \tErklärung: ")
                            .append(t.getBrief()).append("\n");break;
                case IS_OPERATION:
                    str.append(print_options()); break;
                case IS_PATH:
                    return ""; // empty should force path show in autocomplete script
                case IS_SETTING:
                    str.append(print_settings()); break;
                case IS_SWITCH:
                    str.append("true\tfalse\t"); break;
                case IS_TEXT: case IS_TEXTLIST:
                    str.append("Erwarte: Text\tBitte gib Text ein!\t \tErklärung: ").append(t.brief).append("\n"); break;
                case IS_VLS:
                    str.append("LAII\tEIDI\tGDRA\tFG\tKNN\tANA1\tGDBS\tPDP\tPVS\t");
                    str.append("Erwarte: Vorlesung\tBitte gib den Bezeichner einer Vorlesung ein!\t \tErklärung: ")
                            .append(t.brief).append("\n"); break;
                default:
                    str.append("Unbekannte Konfiguration\tBitte melde diesen Fehler\t ");
            }
        }  else {
            str.append(print_settings());
        }


        /**
         * Ablauf:
         *  - Operation gesehen?
         *    Nein: Auflisten .tex, .conf und Operationen (wenn lilly-in spezifiziert: nutzen)
         *    Ja: Direkt Settings angeben
         * grade by settings? tanzen!
         */
        return str.toString();
    }
}
