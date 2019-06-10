package de.eagle.gepard.parser;
/**
 * @file Configurator.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Verarbeitet die Konfigurationsdateien
 * @see Tokenizer
 */

import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static de.eagle.util.logging.JakeLogger.*;

import static de.eagle.util.constants.ColorConstants.COL_ERROR;
import static de.eagle.util.constants.ColorConstants.COL_RESET;

/**
 * Verarbeitet Konfigurationsdateien welche der simplen Zuweisungsregel folgen:
 * a = b Er fügt gegenüber dem @see Tokenizer weitere Optionen wie die Liste
 * hinzu: a += b
 */
public class Configurator {

    /// Interner Tokenizer der verwaltet wird
    private Tokenizer _tokenizer = null;

    /* ======== Regex ======= */
    /// Der komplette Regex-Treffer
    private static final int ALL = 0;
    /// Left hand side der Operation (XXXX = )
    private static final int LHS = 1;
    /// Operation ( X=X )
    private static final int OPT = 2;
    /// Right hand side der Operation ( = XXXX)
    private static final int RHS = 3;

    /**
     * Konstruiert einen neuen Konfigurator auf Basis eines InputStreams
     *
     * @param inputStream die zu verwaltende Eingabe
     */
    public Configurator(InputStream inputStream) {
        this._tokenizer = new Tokenizer(inputStream, Pattern.compile(
                "^ *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?]+(?:[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?]+)*) *(=|\\+=) *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?]+(?: +[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?]+)*)",
                Pattern.MULTILINE));
    }

    /**
     * Kosntruiert einen neuen Konfigurator auf Basis eines Dateipfades
     *
     * @param input_path Der Pfad zur Datei
     * @throws FileNotFoundException Wenn die Datei ungültig/beschädigt/nonexistent
     *                               ist
     */
    public Configurator(String input_path) throws FileNotFoundException {
        writeLoggerDebug2("Arbeite nun auf Pfad: " + input_path, "config");
        this._tokenizer = new Tokenizer(input_path, Pattern.compile(
                "^ *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>/#(){}$\";@%|\\[\\]&'~*?\\\\]+(?:[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>/#(){}$\";@%|\\[\\]&'~*?\\\\]+)*) *(=|\\+=) *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>/#(){}$\";@%|\\[\\]&'~*?\\\\]+(?: +[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>/#(){}$\";@%|\\[\\]&'~*?\\\\]+)*)",
                Pattern.MULTILINE));
    }

    /**
     * Verarbeitet Einstellungen auf Basis der zuvor geladenen Daten
     * (input_path/inputStream)
     *
     * @param settings    die zu verwendeten Einstellungen sie werden
     *                    *überschrieben*
     * @param add_unknown mit true werden auch unbekannte Einstellungen hinzugefügt
     *                    dies ÜBERSCHREIBT die policy der übergebenen Settings
     * @throws IOException Wenn der Stream ungültig/beschädigt/nonexistent ist
     */
    public int parse_settings(Settings settings, boolean add_unknown) throws IOException {
        writeLoggerInfo("Starte Parsen", "config");
        settings.setUnknownPolicy(add_unknown);
        for (TokenMatch m : this._tokenizer) {
            if (m.failure())
                continue;
            if (m.getMatchings().length == 4) {
                String[] matchings = m.getMatchings();
                if (settings.containsKey(matchings[LHS]) || add_unknown) { // Test ansich redundant da Settings policy
                    writeLoggerDebug2(
                            "Zuweisung gefunden: " + matchings[LHS] + " !" + matchings[OPT] + "! " + matchings[RHS],
                            "config");

                    switch (matchings[OPT]) { // Unterscheide nach Operation
                    case "=":
                        settings.set(matchings[LHS], matchings[RHS]);
                        break;
                    case "+=":
                        String old = settings.getValue(matchings[LHS]);
                        if (old == null) {
                            settings.add(matchings[LHS], matchings[RHS]);
                            writeLoggerDebug1("Neue Liste erstellt: " + settings.get(matchings[LHS]).toString(),
                                    "config");
                        } else {
                            if (settings.get(matchings[LHS]).type.equals(eSetting_Type.IS_TEXTLIST)) {
                                writeLoggerDebug1("Listenaddition gefunden", "config");
                                settings.add(matchings[LHS], matchings[RHS]);
                            } else {
                                writeLoggerDebug1("Listenaddition gefunden, aber keine Liste -> normale konkatenation",
                                        "config");
                                settings.set(matchings[LHS], old + matchings[RHS]);
                            }

                        }
                        break;
                    default:
                        writeLoggerWarning(
                                "Die Operation: " + matchings[OPT] + " in: \"" + matchings[ALL] + "\" ist ungütig!",
                                "config");
                    }
                } else
                    writeLoggerWarning(COL_ERROR + "Die Zuordnung \"" + m.getStripped() + "\" ist ungültig" + COL_RESET,
                            "config");
            } else
                writeLoggerWarning(COL_ERROR + "Die Zeile: " + m.getMatchings()[ALL] + " ist ungültig! " + COL_RESET,
                        "config");

        }

        return 0;
    }
}