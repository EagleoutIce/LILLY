package GePard.Parser;
/**
 * @file GePard.Parser.GepardTest.java
 * @author Florian Sihler
 * @author Yellow
 * 
 * @version 1.0.10
 * 
 * @brief Überprüft die von Gepard zu erwartenden Eigenschaften
 */

import de.eagle.gepard.parser.Configurator;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.gepard.parser.Tokenizer;
import de.eagle.gepard.parser.GeneratorParser.JObject;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

@Tag("Gepard")
@DisplayName("Test des GepardParsers")
public class GepardTest {

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

    /*
     * =============================================================================
     * ==
     */
    /* GePard - Generelle Tests */
    /*
     * =============================================================================
     * ==
     */

    /**
     * Testet den GepardParser mit einigen simplen Einstellungen
     * 
     * @throws IOException Im Falle eines Fehlers mit dem TokenizerTest
     * 
     * @see Tokenizer
     * @see Configurator
     * @see GeneratorParser
     */
    @Test
    @Tag("GePard")
    @Order(42)
    @DisplayName("[GePard] Prüft den Parser mit einer simplen TestBox")
    void _test_gepard_simple_box() throws IOException {
        // Hier wird noch nicht die Zeile oder ähnliches überprüft!
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/TestBoxSimple.gpd").getFile());
        Assertions.assertEquals(0, gepard.getLineCount());

        Settings settings = _generate_simple_settings();

        JObject[] jobj_arr = gepard.parseFile("" /* Alle Boxen */, settings, true);

        // Die Settings dürfen sich nicht verändert haben!
        Assertions.assertEquals(settings, _generate_simple_settings(),
                "GePard.parseFile() darf die Einstellungen nicht mutieren! (blueprint)");

        // Es wird eine Box vom Typ x mit zwei Einträgen erwartet wobei diese auf Basis
        // von add_unknown auch hinzugefügt werden sollen

        Assertions.assertEquals(1, jobj_arr.length, "Es wird genau eine Box erwartet");
        Assertions.assertEquals("x", jobj_arr[0].getName(), "Der Name muss x lauten");

        String expected_output = "JObject [config=Settings [name=TestSettings, unknownPolicy=true, settings={name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=TestBox], Hallo=SettingDeskriptor [brief=Hallo Welt Einstellung, isMandatory=false, name=Hallo, type=IS_TEXT, value=null], Better=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=Better, type=IS_TEXT, value=BetterBox], Inhalt=SettingDeskriptorStringList [separator=x, brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=Box, isLocked=false]}], name=x]";

        Assertions.assertEquals(expected_output, jobj_arr[0].toString());
    }

    @Test
    @Tag("GePard")
    @Order(43)
    @DisplayName("[GePard] Prüft das Filtern von Boxen auf Korrektheit")
    void _test_gepard_box_filter() throws IOException {
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/TestBoxFilter.gpd").getFile());
        Assertions.assertEquals(0, gepard.getLineCount());

        Settings settings = new Settings("TestBoxFilterSettings");

        // Filtere nach X(ylophon)
        JObject[] jobj_arr = gepard.parseFile("X.*" /* Boxen mit X */, settings, true);

        Assertions.assertEquals(1, jobj_arr.length, "Es wird genau eine Box erwartet");
        Assertions.assertEquals("Xylophon", jobj_arr[0].getName(), "Der Name muss Xylophon lauten");

        String expected_output = "JObject [config=Settings [name=TestBoxFilterSettings, unknownPolicy=true, settings={Better=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=Better, type=IS_TEXT, value=BetterBox], Inhalt=SettingDeskriptorStringList [separator=x, brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=Box, isLocked=false], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Xylophon]}], name=Xylophon]";
        Assertions.assertEquals(expected_output, jobj_arr[0].toString());

        // Filtere nach Yak
        jobj_arr = gepard.parseFile("Y.k" /* Boxen mit Y?k */, settings, true);

        // System.out.println(jobj_arr[0]);
        // System.out.println(jobj_arr[1]);

        Assertions.assertEquals(2, jobj_arr.length, "Es wird genau eine Box erwartet");
        Assertions.assertEquals("Yak", jobj_arr[1].getName(), "Der Name muss Yak lauten");
        Assertions.assertEquals("Yök", jobj_arr[0].getName(), "Der Name muss Yök lauten");

        // Einfache auflösung, wenn List-Chaining nicht möchte - tihiihi :D
        Assertions.assertEquals("Box.BetterBox", jobj_arr[1].config.getValue("Inhalt"),
                "Die Liste muss korrekt verketten");
        Assertions.assertEquals("Box.Wetter.Box", jobj_arr[0].config.getValue("Inhalt"),
                "Die Liste muss korrekt verketten");

        expected_output = "JObject [config=Settings [name=TestBoxFilterSettings, unknownPolicy=true, settings={Inhalt=SettingDeskriptorStringList [separator=., brief='Unknown Setting', type=IS_TEXTLIST, isMandatory=false, value=Box.Wetter.Box, isLocked=false], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Yök]}], name=Yök]";
        Assertions.assertEquals(expected_output, jobj_arr[0].toString());
        expected_output = "JObject [config=Settings [name=TestBoxFilterSettings, unknownPolicy=true, settings={Inhalt=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=Inhalt, type=IS_TEXT, value=Box.BetterBox], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Yak]}], name=Yak]";
        Assertions.assertEquals(expected_output, jobj_arr[1].toString());
    }

}
