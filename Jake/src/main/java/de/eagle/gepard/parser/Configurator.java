package de.eagle.gepard.parser;

/**
 * @file Configurator.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Verarbeitet die Konfigurationsdateien
 * @see Tokenizer
 */

import de.eagle.gepard.modules.Expandables;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.io.JakeWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import static de.eagle.util.io.JakeLogger.*;

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
                "^ *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+(?:[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+)*) *(=|\\+=) *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+(?: +[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+)*)",
                Pattern.MULTILINE));
    }

    /**
     * Konstruiert einen neuen Konfigurator auf Basis eines Dateipfades
     *
     * @param input_path Der Pfad zur Datei
     * @throws FileNotFoundException Wenn die Datei ungültig/beschädigt/nonexistent
     *                               ist
     */
    public Configurator(String input_path) throws FileNotFoundException {
        writeLoggerDebug2("Arbeite nun auf Pfad: " + input_path, "config");
        this._tokenizer = new Tokenizer(input_path, Pattern.compile(
            "^ *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+(?:[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+)*) *(=|\\+=) *([a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+(?: +[a-zA-Z0-9,\\-_ÄÖÜäöüß.:<>\\/#(){}$\";@%|\\[\\]&'~*?!\\\\]+)*)",
                Pattern.MULTILINE));
    }

    /**
     * Will Parse a File and Return 3-Columns: KEY VALUE [if different:] EXPANDED VALUE
     *
     * @param filePath Path to Config File
     * @return Parsed ArrayList
     */
    public static ArrayList<String[]> parseConfigFile(String filePath) throws IOException {
        ArrayList<String[]> ar = new ArrayList<>();
        Configurator configurator = new Configurator(filePath);
        Settings config = new Settings("Configured");
        configurator.parse_settings(config, true);
        // Apply Settings:
        CoreSettings.getSettings().softJoin(config);
        Settings exps = Expandables.getInstance().expandsCS();
        for (Map.Entry<String, SettingDeskriptor<String>> s : config) {
            String val = s.getValue().getValue();
            String eval = Expandables.expand(exps, val);
            ar.add(new String[]{s.getKey(), val, (!val.equals(eval)) ? eval : ""});
        }
        return ar;
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
                    JakeWriter.out.format("%sDie Operation: \"%s\" in: \"%s\" ist ungütig!%s%n",COL_ERROR,matchings[OPT],matchings[ALL],COL_RESET);
                        throw new RuntimeException("Aus Sicherheitsgründen kann eine solche unbekannte Operation nicht zugelassen werden!");
                    }
                } else{
                    JakeWriter.out.format("%sDie Zuordnung \"%s\" ist ungültig (\"%s\" ist kein gültiger Schlüssel)!%s%n" ,COL_ERROR,m.getStripped(),matchings[LHS],COL_RESET);
                    throw new RuntimeException("Aus Sicherheitsgründen kann eine solche ungültige Zuordnung nicht zugelassen werden!");
                }
            } else {
                JakeWriter.out.format("%sDie Zeile: %s ist ungültig!%s%n",COL_ERROR, m.getMatchings()[ALL],COL_RESET);
                throw new RuntimeException("Aus Sicherheitsgründen kann eine solche ungültige Konfiguration nicht zugelassen werden!");
            }
        }
        return 0;
    }
}