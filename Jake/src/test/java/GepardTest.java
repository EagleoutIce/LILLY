/**
 * @file GepardTest.java
 * @author Florian Sihler
 * @author Raphael Straub
 * 
 * @version 1.0.10
 * 
 * @brief Überprüft die von Gepard zu erwartenden Eigenschaften
 */

import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.gepard.parser.GeneratorParser.JObject;
import de.eagle.gepard.parser.TokenMatch;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;

@Tag("Gepard")
@DisplayName("Test des GepardParsers inklusive Tokenizer und Configurator")
public class GepardTest {

    /**
     * Testet ob die TokenMatch.failure Funktion wie gewünscht funktioniert.
     * 
     * @see TokenMatch
     */
    @Test @Tag("Tokenizer")
    @Order(1) @DisplayName("[TokenMatch] Prüft '.failure()' und '.valid()'")
    void _test_tokenizer_token_match_failure() {
        
        // failure soll solange anschlagen wie stripped nicht leer ist
        // im folgenden werden deswegen alle Relevanten kombinationen verwendet
        // isValid() wird ebenfalls geprüft!

        // Direkte Fehler:
        TokenMatch tm_fail = new TokenMatch(new String[0], "", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(),"Teste leeres Match");
        tm_fail = new TokenMatch(new String[0], "original", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste nur original");
        tm_fail = new TokenMatch(new String[]{"hi"}, "", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste nur matchings");
        tm_fail = new TokenMatch(new String[]{"hi"}, "original", "");
        Assertions.assertTrue(tm_fail.failure() && tm_fail.isValid(), "Teste o und m");
        // isValid() Fehler:
        tm_fail = new TokenMatch(new String[0], "", "a");
        Assertions.assertTrue(tm_fail.failure() && !tm_fail.isValid(), "Teste nur Stripped");
        tm_fail = new TokenMatch(new String[0], "original", "a");
        Assertions.assertTrue(tm_fail.failure() && !tm_fail.isValid(), "Teste o und s");
        tm_fail = new TokenMatch(new String[]{"hi"}, "", "a");
        Assertions.assertTrue(tm_fail.failure() && !tm_fail.isValid(), "Teste m und s");

        // Korrekt (natürlich ist dies nicht korrekt aber ein derartiges
        // Mismatching aufzulösen ist im Rahmen der Nutzubarkeit von
        // Tokenmatch nicht nötig)
        tm_fail = new TokenMatch(new String[]{"hi"}, "original", "b");
        Assertions.assertTrue(!tm_fail.failure() && tm_fail.isValid(), "Teste m,o und s");
    }

    /**
     * Testet das bloße Einlesen (!= Parsen) einer Zeile
     * 
     * @throws IOException Im Falle eines Fehlers des Tokenizers
     * @see Tokenizer
     */
    @Test @Tag("Tokenizer")
    @Order(2) @DisplayName("[Tokenizer] Prüft das Einlesen einer einzelnen Zeile")
    void _test_tokenizer_read_single_line() throws IOException {
        Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Tokens/TestSingleLine.tokens"));
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertEquals("Hallo = Welt", tknz.get_current_line());
    }

    /**
     * Testet das bloße einlesen (!= Parsen) von multi-lines
     */
    @Test @Tag("Tokenizer")
    @Order(3) @DisplayName("[Tokenizer] Prüft das Einlesen mehrerer Zeilen")
    void _test_tokenizer_read_multi_line() throws IOException{
        Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Tokens/TestMultiLine.tokens"));
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertEquals("Hallo = Welt Geld", tknz.get_current_line());
    }

    /**
     * Testet das Parsen einer Zeile
     * 
     * @throws IOException Im Falle eines Fehlers des Tokenizers
     * @see Tokenizer
     */
    @Test @Tag("Tokenizer")
    @Order(4) @DisplayName("[Tokenizer] Prüft das Parsen einer einzelnen Zeile")
    void _test_tokenizer_parse_single_line() throws IOException {
        Tokenizer tknz = new Tokenizer(this.getClass().getResourceAsStream("/Tokens/TestSingleLine.tokens"));
        Assertions.assertEquals(0, tknz.loadNext());
        Assertions.assertArrayEquals(new String[]{"Hallo = Welt", "Hallo", "=", "Welt"},
                tknz.get().getMatchings());
    }


    /**
     * Generiert Einstellungen, die nur die Hallo Box erlauben
     * @return Einstellungen, welche nur die Hallo Box als Blueprint erlauben
     */
    Settings _generate_simple_settings(){
        HashMap<String, SettingDeskriptor<String>> testSets = new HashMap<>();
        testSets.put("Hallo", SettingDeskriptor.create("Hallo", "Hallo Welt Einstellung"));

        return new Settings("TestSettings", false, testSets);
    }

    /**
     * Testet Configurator
     * 
     * @throws IOException Im Falle eines Fehlers des Configurators
     * @see Tokenizer
     * @see Configurator
     */
    @Test @Tag("Configurator")
    @Order(5) @DisplayName("[Configurator] Prüft das Parsen einer einzelnen Zeile")
    void _test_configurator_default_parse() throws IOException {

        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Tokens/TestSingleLine.tokens"));

        Settings settings = _generate_simple_settings();

        cfg.parse_settings(settings, false);

        Assertions.assertEquals("Welt", settings.getValue("Hallo"));
    }

    @Test @Tag("Configurator")
    @Order(6) @DisplayName("[Configurator] Prüft den '+=' Operator")
    void _test_configurator_plus_parse() throws IOException {
            Settings settings = _generate_simple_settings();

            Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/TestPlus.conf"));
            settings.set("Hallo", "Welt");
            cfg.parse_settings(settings, false);
            Assertions.assertEquals("WeltWelt", settings.getValue("Hallo"));
    }


    @Test
    @Tag("Configurator")
    @Order(7) @DisplayName("[Configurator] Prüft die korrekte Bearbeitung von Fehlern in Dateien")
    void _test_configurator_error_parse() throws IOException {
            Settings settings = _generate_simple_settings();
            Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/TestError.conf"));
            cfg.parse_settings(settings, false);
            Assertions.assertEquals("", settings.getValue("Hallo"));
    }


    @Test
    @Tag("Configurator")
    @Order(8) @DisplayName("[Configurator] Prüft die korrekte Erkennung von Kommentaren")
    void _test_configurator_comment_parse() throws IOException {
            Settings settings = _generate_simple_settings();
            Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/TestComment.conf"));
            cfg.parse_settings(settings, false);
            Assertions.assertEquals("Welt", settings.getValue("Hallo"));
    }

    @Test @Order(9)
    @Tag("Configurator") @DisplayName("[Configurator] Prüft eine vollständige Konfigurationsdatei")
    void _test_configurator_working_example() throws IOException {
        Settings settings = new Settings("WorkingExampleSettings");
        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/WorkingExample.conf"));
        cfg.parse_settings(settings, true);
        String expected_output = "Settings [name=WorkingExampleSettings, unknownPolicy=true, settings={lilly-print-name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-print-name, type=IS_TEXT, value=Druck-], file=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=file, type=IS_TEXT, value=Mitschrieb-GDBS.tex], debug=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=debug, type=IS_TEXT, value=true], lilly-boxes=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-boxes, type=IS_TEXT, value=LIMERENCE], lilly-modes=SettingDeskriptorStringList [separator=t, brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=c_print c_default, isLocked=false], lilly-show-boxname=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-show-boxname, type=IS_TEXT, value=false], operation=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=operation, type=IS_TEXT, value=file_compile], lilly-external=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-external, type=IS_TEXT, value=true]}]";
        Assertions.assertEquals(expected_output, settings.toString());
    }

    /* @formatter:off
     *
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

    // TODO CHECK IF TokenMatch.failure() is correct?

     /* =============================================================================== */
     /* GePard - Generelle Tests                                                        */
     /* =============================================================================== */

     /**
      * Testet den GepardParser mit einigen simplen Einstellungen
      * @throws IOException Im Falle eines Fehlers mit dem Tokenizer
      * 
      * @see Tokenizer 
      * @see Configurator
      * @see GeneratorParser
      */
     @Test @Tag("GePard")
     @Order(42) @DisplayName("[GePard] Prüft den Parser mit einer simplen TestBox")
     void _test_gepard_simple_box() throws IOException {
        //Hier wird noch nicht die Zeile oder ähnliches überpüft!
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/TestBoxSimple.gpd").getFile());
        Assertions.assertEquals(0, gepard.getLineCount());
        
        Settings settings = _generate_simple_settings();

        JObject[] jobj_arr = gepard.parseFile("" /* Alle Boxen */, settings, true);
        
        // Die Settings dürfen sich nicht verändert haben!
        Assertions.assertEquals(settings, _generate_simple_settings(), 
                "GePard.parseFile() darf die Einstellungen nicht mutieren! (blueprint)");

        // Es wird eine Box vom Typ x mit zwei Einträgen erwartet wobei diese auf Basis von add_unknown auch hinzugefügt werden sollen
        
        Assertions.assertEquals(1,jobj_arr.length,"Es wird genau eine Box erwartet");
        Assertions.assertEquals("x",jobj_arr[0].getName(),"Der Name muss x lauten");

        String expected_output = "JObject [config=Settings [name=TestSettings, unknownPolicy=true, settings={name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=TestBox], Hallo=SettingDeskriptor [brief=Hallo Welt Einstellung, isMandatory=false, name=Hallo, type=IS_TEXT, value=null], Better=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=Better, type=IS_TEXT, value=BetterBox], Inhalt=SettingDeskriptorStringList [separator=x, brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=Box, isLocked=false]}], name=x]";

        Assertions.assertEquals(expected_output, jobj_arr[0].toString());
     }

     @Test @Tag("GePard")
     @Order(43) @DisplayName("[GePard] Prüft das Filtern von Boxen auf Korrektheit")
    void _test_gepard_box_filter() throws IOException {
         GeneratorParser gepard = new GeneratorParser(
                 this.getClass().getResource("/Gepard/TestBoxFilter.gpd").getFile());
         Assertions.assertEquals(0, gepard.getLineCount());

         Settings settings = new Settings("TestBoxFilterSettings");

         // Filtere nach X(ylophon)
         JObject[] jobj_arr = gepard.parseFile("X.*" /* Boxen mit X */, settings, true);

         Assertions.assertEquals(1,jobj_arr.length,"Es wird genau eine Box erwartet");
         Assertions.assertEquals("Xylophon",jobj_arr[0].getName(),"Der Name muss Xylophon lauten");

         String expected_output = "JObject [config=Settings [name=TestBoxFilterSettings, unknownPolicy=true, settings={Better=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=Better, type=IS_TEXT, value=BetterBox], Inhalt=SettingDeskriptorStringList [separator=x, brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=Box, isLocked=false], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Xylophon]}], name=Xylophon]";
         Assertions.assertEquals(expected_output, jobj_arr[0].toString());

         // Filtere nach Yak
         jobj_arr = gepard.parseFile("Y.k" /* Boxen mit Y?k*/, settings, true);

         // System.out.println(jobj_arr[0]);
         // System.out.println(jobj_arr[1]);

         Assertions.assertEquals(2,jobj_arr.length,"Es wird genau eine Box erwartet");
         Assertions.assertEquals("Yak",jobj_arr[1].getName(),"Der Name muss Yak lauten");
         Assertions.assertEquals("Yök",jobj_arr[0].getName(),"Der Name muss Yök lauten");

         // Einfache auflösung, wenn List-Chaining nicht möchte - tihiihi :D
         Assertions.assertEquals("Box.BetterBox",jobj_arr[1].config.getValue("Inhalt"),"Die Liste muss korrekt verketten");
         Assertions.assertEquals("Box.Wetter.Box",jobj_arr[0].config.getValue("Inhalt"),"Die Liste muss korrekt verketten");

         expected_output = "JObject [config=Settings [name=TestBoxFilterSettings, unknownPolicy=true, settings={Inhalt=SettingDeskriptorStringList [separator=., brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=Box.Wetter.Box, isLocked=false], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Yök]}], name=Yök]";
         Assertions.assertEquals(expected_output, jobj_arr[0].toString());
         expected_output = "JObject [config=Settings [name=TestBoxFilterSettings, unknownPolicy=true, settings={Inhalt=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=Inhalt, type=IS_TEXT, value=Box.BetterBox], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Yak]}], name=Yak]";
         Assertions.assertEquals(expected_output, jobj_arr[1].toString());
     }


}
