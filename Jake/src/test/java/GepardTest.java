import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.util.Setting_Deskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

@Tag("Gepard")
public class GepardTest {

    /**
     * Testet das bloße Einlesen (!= Parsen) einer Zeile
     */
    @Test
    @Tag("Tokenizer")
    @Order(1)
    void _test_tokenizer_read_single_line() {
        try {
            Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Gepard/TestSingleLine.tokens"));
            Assertions.assertEquals(0, tknz.loadNext());
            Assertions.assertEquals("Hallo = Welt", tknz.get_current_line());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testet das bloße einlesen (!= Parsen) von multi-lines
     */
    @Test
    @Tag("Tokenizer")
    @Order(2)
    void _test_tokenizer_read_multi_line() {
        try {
            Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Gepard/TestMultiLine.tokens"));
            Assertions.assertEquals(0, tknz.loadNext());
            Assertions.assertEquals("Hallo = Welt Geld", tknz.get_current_line());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testet das Parsen einer Zeile
     */
    @Test
    @Tag("Tokenizer")
    @Order(3)
    void _test_tokenizer_parse_single_line() {
        try {
            Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Gepard/TestSingleLine.tokens"));
            Assertions.assertEquals(0, tknz.loadNext());
            Assertions.assertArrayEquals(new String[]{"Hallo = Welt", "Hallo", "=", "Welt"},
                    tknz.get().getMatchings());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testet Configurator
     */
    @Test
    @Tag("Configurator")
    @Order(4)
    void _test_configurator_default_parse() {
        try {
            HashMap<String, Setting_Deskriptor<String>> testSets = new HashMap<>();
            testSets.put("Hallo", new Setting_Deskriptor<>("Hallo", "Hallo Welt Einstellung"));
            Settings settings = new Settings("TestSettings", false, testSets);
            Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Gepard/TestSingleLine.tokens"));

            cfg.parse_settings(settings, false);

            Assertions.assertEquals("Welt", settings.getValue("Hallo"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO @Hanfjesus

    /* @formatter:off
     *
     * Teste den += Operator bei einer Einstellung
     *
     * Teste mit mehreren Einstellungen wobei:
     *  - eine ungültig
     *  - eine korrekt
     *  - eine leere Zeile (auch gerne Mehrere)
     *  - eine mit hässlichen Kommentaren: Hal! hi !l! oha!o ! XXX ! = Wel!oh mein Gott = 35!t
     *                      => Hallo = Welt
     *  - eine, die eine vorherige überschreibt
     *  - eine unbekannte die hinzugefügt werden soll
     *  - eine sehr lange mit etlichen multlines
     *
     * Teste die Datei WorkingExample.conf wobei die Einstellungen entsprechend vordefiniert sind
     * @formatter:on
     */

}
