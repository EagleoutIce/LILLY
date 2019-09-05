package de.eagle.gepard.parser;

/**
 * @file Tokenizer.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Grundlegender Parser - jedes weitere Modul greift auf ihn zurück
 *
 * @see Configurator
 * @see GeneratorParser
 */

import java.io.*;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;

import static de.eagle.util.io.JakeLogger.*;

/**
 * Grundlegender Tokenizer für InputStreams
 *
 * @author Florian Sihler
 * @version 2.0.0
 * @since 2.0.0
 */
public class Tokenizer implements Iterable<TokenMatch> {

    /// Markiert das Ende einer Zeile - auf ein Zeichen begrenzt!
    private String _current_line, _current_original;
    private BufferedReader _input_reader;
    private Pattern _pattern;

    /* ========================================================================== */
    /* Konstruktoren */
    /* ========================================================================== */

    /**
     * Konstruiert den Tokenizer auf einer Datei
     *
     * @param input_path der Pfad zur Datei
     * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde
     */
    public Tokenizer(String input_path) throws FileNotFoundException {
        this(new FileInputStream(input_path),
                Pattern.compile("^ *([a-zA-Z0-9_\\-äöüßÄÖÜ!]+) *(=) *([a-zA-Z0-9_\\-äöüÄÖÜß! ]+)$", Pattern.MULTILINE), 0);
    }

    /**
     * Konstruiert den Tokenizer auf einem normalen InputStream
     *
     * @param inputStream ganz allgemeiner InputStream der bearbeitet werden soll
     * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde
     */
    public Tokenizer(InputStream inputStream) throws FileNotFoundException {
        this(inputStream,
                Pattern.compile("^ *([a-zA-Z0-9_\\-äöüßÄÖÜ!]+) *(=) *([a-zA-Z0-9_\\-äöüÄÖÜß! ]+)$", Pattern.MULTILINE), 0);
    }

    /**
     * Konstruiert den Tokenizer auf einer Datei
     *
     * @param input_path der Pfad zur Datei
     * @param pattern    Token-Pattern
     * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde
     */
    public Tokenizer(String input_path, Pattern pattern) throws FileNotFoundException {
        this(new FileInputStream(input_path), pattern, 0);
    }

    /**
     * Konstruiert den Tokenizer auf einem normalen InputStream
     *
     * @param inputStream der zu verarbeitende input Stream
     * @param pattern     Token-Pattern
     * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde
     */
    public Tokenizer(InputStream inputStream, Pattern pattern) {
        this(inputStream, pattern, 0);
    }

    /**
     * Konstruiert den Tokenizer auf einem normalen InputStream
     *
     * @param inputStream der zugrunde liegende Input Stream
     * @param pattern     Token-Selector
     * @param skip_lines  Wie viele Zeilen sollen zu Beginn übersprungen werden
     */
    public Tokenizer(InputStream inputStream, Pattern pattern, int skip_lines) {
        this._input_reader = new BufferedReader(new InputStreamReader(inputStream));
        this._pattern = pattern;
        // Skip lines:
        for (int i = 0; i < skip_lines; i++) {
            try {
                if ((_current_line = this._input_reader.readLine()) == null)
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* ========================================================================== */
    /* Hilfsmethoden */
    /* ========================================================================== */

    /**
     * Entfernt alle tabs
     *
     * @param input Eingabe die bearbeitet werden soll
     * @return String ohne tabs!
     */
    public static String erase_tabs(String input) {
        return input.replaceAll("\\t", "    ");
    }

    /**
     * Entfernt Kommentare aus der Eingabe
     *
     * @param input die Eingabe
     * @return die Eingabe ohne Kommentare
     */
    public String erase_comments(String input) {
        if(CoreSettings.settings==null)
            return input.replaceAll(Definitions.DEFAULT_COMMENT_PATTERN, ""); // As the Translator needs the Tokenizer zo resolve the Settings we have to set a Default Value
        else
            return input.replaceAll(CoreSettings.requestValue("S_COMMENT_PATTERN"), "");
    }

    /**
     * @return Liefert die aktuelle Zeile
     */
    public String get_current_line() {
        return _current_line;
    }

    /* ========================================================================== */
    /* interne Methoden */
    /* ========================================================================== */

    /**
     * Lädt die nächste Zeile und liefert 1 wenn dies fehlschlägt
     *
     * @return 1 wenn es keine Zeile mehr gibt, oder nur noch Kommentare
     */
    public int loadNext() throws IOException {
        String line = "";
        writeLoggerDebug1("Lade nächste Zeile für Verarbeitung", "token"); // TODO: linenumber
        while (line.matches("^\\s*$")) {
            line = _input_reader.readLine();
            if (line == null)
                return 1;

            // multiline
            while (line.length() > 2 && line.toString().endsWith("++\\")) {
                line = line.substring(0, line.length() - 3);
                String _mtl_tmp = _input_reader.readLine();
                if (_mtl_tmp == null)
                    return 1;
                line += _mtl_tmp.trim();
            }

            _current_original = line;
            line = erase_comments(erase_tabs(line));
            writeLoggerDebug2("Aktuelle Zeile: \"" + _current_original + "\" => \"" + line + "\"", "token");
        }

        this._current_line = line;
        return 0;
    }

    /**
     * Liefert die nächste Zeile.
     *
     * @return einen dementsprechend konfigurierten Match
     */
    public TokenMatch get() {
        String line = get_current_line();
        Matcher m = this._pattern.matcher(line);
        if (m.find()) {
            String[] arr_m = new String[m.groupCount() + 1];
            for (int i = 0; i < arr_m.length; i++) {
                arr_m[i] = m.group(i).trim();
            }
            return new TokenMatch(arr_m, _current_original, line);
        }
        return null; // null means failure
    }

    /* ========================================================================== */
    /* Iterator-Schnittstelle */
    /* ========================================================================== */

    @Override
    public Iterator<TokenMatch> iterator() {
        return new Iterator<TokenMatch>() {
            @Override
            public boolean hasNext() {
                try {
                    return loadNext() == 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public TokenMatch next() {
                TokenMatch next = get();
                return (next == null) ? new TokenMatch(new String[0], "", "") : next;
            }
        };
    }
}
