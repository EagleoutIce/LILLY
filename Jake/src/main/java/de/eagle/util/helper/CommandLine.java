package de.eagle.util.helper;

import de.eagle.lillyjakeframework.core.CoreSettings;

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
        String tmp;
        if (!(tmp = CoreSettings.requestValue("S_ANSWER")).isEmpty())
            return tmp;

        tmp = "";
        Scanner s = new Scanner(System.in);
        while(!stringContainsItemFromList(tmp.toUpperCase(), options)){
            System.err.print(prompt);
            tmp = s.nextLine();
        }
        return tmp.toUpperCase();
    }

}