import de.eagle.gepard.parser.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

@Tag("Gepard")
public class GepardTest {

    /**
     * Testet das bloße Einlesen (!= Parsen) einer Zeile
     */
    @Test
    @Tag("Tokenizer")
    @Order(1)
    void _test_tokenizer_read_single_line(){
        try {
            Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Tokenizer/TestSingleLine.tokens"));
            Assertions.assertEquals(0,tknz.loadNext());
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
    void _test_tokenizer_read_multi_line(){
        try {
            Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Tokenizer/TestMultiLine.tokens"));
            Assertions.assertEquals(0,tknz.loadNext());
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
    void _test_tokenizer_parse_single_line(){
        try {
            Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Tokenizer/TestSingleLine.tokens"));
            Assertions.assertEquals(0,tknz.loadNext());
            Assertions.assertArrayEquals(new String[]{"Hallo", "=", "Welt"}, tknz.get().getMatchings());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
