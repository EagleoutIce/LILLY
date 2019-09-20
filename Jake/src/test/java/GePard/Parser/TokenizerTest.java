package GePard.Parser;

import de.eagle.gepard.parser.TokenMatch;
import org.junit.jupiter.api.*;

import java.io.IOException;


@Tag("Tokenizer")
@DisplayName("Test des Tokenizers")
public class TokenizerTest {
    /**
     * Testet ob die TokenMatch.failure Funktion wie gewünscht funktioniert.
     *
     * @see TokenMatch
     */
    @Test
    @Tag("TokenizerTest")
    @Order(1)
    @DisplayName("[TokenMatch] Prüft '.failure()' und '.valid()'")
    void _test_tokenizer_token_match_failure() {

        // failure soll solange anschlagen wie stripped nicht leer ist
        // im folgenden werden deswegen alle Relevanten kombinationen verwendet
        // isValid() wird ebenfalls geprüft!

        // Direkte Fehler:
        TokenMatch tm_fail = new TokenMatch(new String[0], "", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste leeres Match");
        tm_fail = new TokenMatch(new String[0], "original", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste nur original");
        tm_fail = new TokenMatch(new String[] { "hi" }, "", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste nur matchings");
        tm_fail = new TokenMatch(new String[] { "hi" }, "original", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste o und m");
        // isValid() Fehler:
        tm_fail = new TokenMatch(new String[0], "", "a");
        Assertions.assertTrue(tm_fail.failure() && !tm_fail.isValid(), "Teste nur Stripped");
        tm_fail = new TokenMatch(new String[0], "original", "a");
        Assertions.assertTrue(tm_fail.failure() && !tm_fail.isValid(), "Teste o und s");
        tm_fail = new TokenMatch(new String[] { "hi" }, "", "a");
        Assertions.assertTrue(tm_fail.failure() && !tm_fail.isValid(), "Teste m und s");

        // Korrekt (natürlich ist dies nicht korrekt aber ein derartiges
        // Mismatching aufzulösen ist im Rahmen der Nutzbarkeit von
        // Tokenmatch nicht nötig)
        tm_fail = new TokenMatch(new String[] { "hi" }, "original", "b");
        Assertions.assertTrue(!tm_fail.failure() && tm_fail.isValid(), "Teste m,o und s");
    }

    /**
     * Testet das bloße Einlesen (!= Parsen) einer Zeile
     *
     * @throws IOException Im Falle eines Fehlers des Tokenizers
     * @see de.eagle.gepard.parser.Tokenizer
     */
    @Test
    @Tag("TokenizerTest")
    @Order(2)
    @DisplayName("[TokenizerTest] Prüft das Einlesen einer einzelnen Zeile")
    void _test_tokenizer_read_single_line() throws IOException {
        de.eagle.gepard.parser.Tokenizer tknz = new de.eagle.gepard.parser.Tokenizer(this.getClass().getResourceAsStream("/Tokens/TestSingleLine.tokens"));
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertEquals("Hallo = Welt", tknz.get_current_line());
    }

    /**
     * Testet das bloße einlesen (!= Parsen) von multi-lines
     */
    @Test
    @Tag("TokenizerTest")
    @Order(3)
    @DisplayName("[TokenizerTest] Prüft das Einlesen mehrerer Zeilen")
    void _test_tokenizer_read_multi_line() throws IOException {
        de.eagle.gepard.parser.Tokenizer tknz = new de.eagle.gepard.parser.Tokenizer(this.getClass().getResourceAsStream("/Tokens/TestMultiLine.tokens"));
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertEquals("Hallo = Welt Geld", tknz.get_current_line());
    }

    /**
     * Testet das Parsen einer Zeile
     *
     * @throws IOException Im Falle eines Fehlers des Tokenizers
     * @see de.eagle.gepard.parser.Tokenizer
     */
    @Test
    @Tag("TokenizerTest")
    @Order(4)
    @DisplayName("[TokenizerTest] Prüft das Parsen einer einzelnen Zeile")
    void _test_tokenizer_parse_single_line() throws IOException {
        de.eagle.gepard.parser.Tokenizer tknz = new de.eagle.gepard.parser.Tokenizer(this.getClass().getResourceAsStream("/Tokens/TestSingleLine.tokens"));
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertArrayEquals(new String[] { "Hallo = Welt", "Hallo", "=", "Welt" }, tknz.get().getMatchings());
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertArrayEquals(new String[] { "name                   = default", "name", "=", "default" }, tknz.get().getMatchings());

    }
}
