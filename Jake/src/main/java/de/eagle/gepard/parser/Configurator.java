package de.eagle.gepard.parser;

import de.eagle.util.datatypes.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static de.eagle.lillyjakeframework.logging.JakeLogger.getLogger;
import static de.eagle.util.constants.ColorConstants.COL_ERROR;
import static de.eagle.util.constants.ColorConstants.COL_RESET;

public class Configurator {
    private Tokenizer _tokenizer = null;

    public Configurator(InputStream inputStream) {
        this._tokenizer = new Tokenizer(inputStream, Pattern.compile(
                "^ *([a-zA-Z0-9\\,\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\(\\)\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\[\\]\\&\\'\\~\\*\\?]+(?: [a-zA-Z0-9\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\(\\)\\[\\]\\,\\&\\'\\?]+)*) *(=|\\+=) *([a-zA-Z0-9\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\,\\(\\)\\[\\]\\&\\+\\=\\'\\?]+(?: +[a-zA-Z0-9\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\(\\)\\[\\]\\,\\&\\+\\=\\'\\?]+)*)",
                Pattern.MULTILINE));
    }

    public Configurator(String input_path) throws FileNotFoundException {
        this._tokenizer = new Tokenizer(input_path, Pattern.compile(
                "^ *([a-zA-Z0-9\\,\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\(\\)\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\[\\]\\&\\'\\~\\*\\?]+(?: [a-zA-Z0-9\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\(\\)\\[\\]\\,\\&\\'\\?]+)*) *(=|\\+=) *([a-zA-Z0-9\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\,\\(\\)\\[\\]\\&\\+\\=\\'\\?]+(?: +[a-zA-Z0-9\\-_ÄÖÜäöüßß\\.\\:\\<\\>\\/\\#\\{\\}\\$\\\"\\\"\\;\\@\\%\\|\\(\\)\\[\\]\\,\\&\\+\\=\\'\\?]+)*)",
                Pattern.MULTILINE));
    }

    public int parse_settings(Settings settings, boolean add_unknown) throws IOException {
        getLogger().info("config: Starte Parsen");
        for (TokenMatch m : this._tokenizer) {
            if (m.failure())
                continue;
            if (m.getMatchings().length == 4) {
                String[] matchings = m.getMatchings();
                if (settings.getValue(matchings[1]) != null || add_unknown) {
                    getLogger().info("config: Gefunden: " + matchings[1] + " !" + matchings[2] + "! " + matchings[3]);

                    if (matchings[2].equals("="))
                        settings.set(matchings[1], matchings[3]);
                    else
                        settings.set(matchings[1], matchings[1] + matchings[3]);
                } else
                    System.err
                            .println(COL_ERROR + "Die Zuordnung \"" + m.getStripped() + "\" ist ungültig" + COL_RESET);
            } else
                System.err.println(COL_ERROR + "Die Zeile: " + m.getMatchings()[0] + " ist ungültig! " + COL_RESET);

        }

        return 0;
    }
}