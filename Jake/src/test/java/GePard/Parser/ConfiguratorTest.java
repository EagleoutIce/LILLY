package GePard.Parser;

import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;


@Tag("Configurator")
@DisplayName("Test des Configurators")
public class ConfiguratorTest {

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


    /**
     * Generiert Einstellungen, die nur die Hallo Box erlauben
     *
     * @return Einstellungen, welche nur die Hallo Box als Blueprint erlauben
     */
    Settings _generate_simple_settings() {
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
    @Test
    @Tag("Configurator")
    @Order(5)
    @DisplayName("[Configurator] Prüft das Parsen einer einzelnen Zeile")
    void _test_configurator_default_parse() throws IOException {

        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Tokens/TestSingleLine.tokens"));

        Settings settings = _generate_simple_settings();

        cfg.parse_settings(settings, false);

        Assertions.assertEquals("Welt", settings.getValue("Hallo"));
    }

    @Test
    @Tag("Configurator")
    @Order(6)
    @DisplayName("[Configurator] Prüft den '+=' Operator")
    void _test_configurator_plus_parse() throws IOException {
        Settings settings = _generate_simple_settings();

        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/TestPlus.conf"));
        settings.set("Hallo", "Welt");
        cfg.parse_settings(settings, false);
        Assertions.assertEquals("WeltWelt", settings.getValue("Hallo"));
    }

    @Test
    @Tag("Configurator")
    @Order(7)
    @DisplayName("[Configurator] Prüft die korrekte Bearbeitung von Fehlern in Dateien")
    void _test_configurator_error_parse() throws IOException {
        Settings settings = _generate_simple_settings();
        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/TestError.conf"));
        cfg.parse_settings(settings, false);
        Assertions.assertEquals("", settings.getValue("Hallo"));
    }

    @Test
    @Tag("Configurator")
    @Order(8)
    @DisplayName("[Configurator] Prüft die korrekte Erkennung von Kommentaren")
    void _test_configurator_comment_parse() throws IOException {
        Settings settings = _generate_simple_settings();
        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/TestComment.conf"));
        cfg.parse_settings(settings, false);
        Assertions.assertEquals("Welt", settings.getValue("Hallo"));
    }

    @Test
    @Order(9)
    @Tag("Configurator")
    @DisplayName("[Configurator] Prüft eine vollständige Konfigurationsdatei")
    void _test_configurator_working_example() throws IOException {
        Settings settings = new Settings("WorkingExampleSettings");
        Configurator cfg = new Configurator(this.getClass().getResourceAsStream("/Configs/WorkingExample.conf"));
        cfg.parse_settings(settings, true);
        String expected_output = "Settings [name=WorkingExampleSettings, unknownPolicy=true, settings={lilly-print-name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-print-name, type=IS_TEXT, value=Druck-], file=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=file, type=IS_TEXT, value=Mitschrieb-GDBS.tex], debug=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=debug, type=IS_TEXT, value=true], lilly-boxes=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-boxes, type=IS_TEXT, value=LIMERENCE], lilly-modes=SettingDeskriptorStringList [separator=t, brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=c_print c_default, isLocked=false], lilly-show-boxname=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-show-boxname, type=IS_TEXT, value=false], operation=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=operation, type=IS_TEXT, value=file_compile], lilly-external=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-external, type=IS_TEXT, value=true]}]";
        Assertions.assertEquals(expected_output, settings.toString());
    }
}
