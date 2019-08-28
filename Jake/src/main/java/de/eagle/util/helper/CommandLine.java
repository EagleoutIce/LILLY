package de.eagle.util.helper;

/**
 * @file CommandLine.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Einige vereinfachende Befehle für die Verwendung in der Kommandozeile.
 */

import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.io.JakeWriter;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Vereinfacht Tätigkeiten wie Abfragen in der Kommandozeile
 */
public class CommandLine {

    // https://stackoverflow.com/questions/8992100/test-if-a-string-contains-any-of-the-strings-from-an-array
    // cause im lazy
    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }

    /**
     * {@link #get_answer(String)}
     */
    public static String get_answer(String prompt){
        return get_answer(prompt, new String[] {"Y","N"});
    }

    /**
     * Fragt den Nutzer in der Kommandozeile nach einer Antwort auf eine Frage
     * @param prompt Deskription der Auswahlmöglichkeiten
     * @param options Gültige Optionen (insensitive)
     * @return Auswahl, die einer der gültigen Optionen entspricht. Diese sind nicht sensitiv
     */
    public static String get_answer(String prompt, String[] options){
        String tmp= CoreSettings.requestValue("S_ANSWER");
        if (tmp != null && !tmp.isEmpty())
            return tmp;

        tmp = "";
        Scanner s = new Scanner(JakeWriter.in);
        while(!stringContainsItemFromList(tmp.toUpperCase(), options)){
            JakeWriter.err.print(prompt);
            tmp = s.nextLine();
        }
        return tmp.toUpperCase();
    }

}
