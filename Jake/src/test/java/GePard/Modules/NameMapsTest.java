package GePard.Modules;

import de.eagle.gepard.modules.Buildrules;
import de.eagle.gepard.modules.NameMaps;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

/**
 * Diese Klasse spezifiziert die Anforderungen an das Namemaps Modul. Die
 * entsprechende Erweiterungen hat alle folgende Tests zu bestehen an den Tests
 * selbst darf nur mit Rücksprache eine Veränderung durchgeführt werden!
 *
 * @implNote Ziel ist es, dass es über die Box: 'nameMaps' möglich ist
 *           Einstellungen zu treffen, die bei bestimmten Bezeichnern
 *           übernommen werden sollen
 *
 *           Die entsprechende C++-datei lautet j_nmaps.cpp (/.hpp)
 *
 *
 * @author Florian Sihler
 *
 * @see de.eagle.gepard.parser.GeneratorParser
 */
@Tag("Module")
@Tag("NameMaps")
public class NameMapsTest {

    @ParameterizedTest
    @Tag("GePard")
    @Order(1)
    @DisplayName("[NameMaps] Überprüft erwartende Blueprint-Settings")
    @CsvSource({ "name,,true", "patterns,,true" })
    void _test_gepard_nmaps_blueprint(String name, String expected, String mandatory) {
        SettingDeskriptor<String> s = NameMaps.getInstance().getBlueprint().get(name);
        Assertions.assertNotNull(s, "Die Einstellung muss Existieren");
        if (mandatory.equals(Definitions.S_TRUE)) {
            Assertions.assertTrue(s.isMandatory, "Die Einstellung soll verpflichtend sein!");
        } else {
            Assertions.assertEquals(expected.replace("#", ""), s.getValue(), "Die Einstellung soll den Wert haben");
        }
    }

    /**
     * Testet ob die jeweiligen Defaults existieren testet nicht den Inhalt, der
     * allerdings entsprechend sein sollte
     *
     * @param should_exist NameMap den es geben soll
     * @param should_be    Wert der erhalten werden soll
     */
    @ParameterizedTest
    @Tag("GePard")
    @Order(2)
    @DisplayName("[NameMaps] Überprüft erwartende Defaults")
    @CsvSource(value = {"PDP~#", "GDBS~#","ANA1~#","PVS~#","GDRA~#","EIDI~#","FG~#","LA~#"}, delimiter = '~')
    void _test_gepard_nmaps_defaults(String should_exist, String should_be) {
        Assertions.assertNotNull(NameMaps.getInstance().getDefaults().get(should_exist), "Default-Einstellung muss existieren");
        // JakeWriter.out.println(NameMaps.getDefaults().get(should_exist));
        // TODO hihi

        // Assertions.assertEquals(NameMaps.getDefaults().get(should_exist).toString(),
        // should_be);
    }

    @Test
    @Tag("GePard")
    @Order(3)
    @DisplayName("[NameMaps] Überprüft das Parsen einer Regel")
    void _test_gepard_nmaps_rule() throws IOException {
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/NameMapsSimple.gpd").getFile());
        GeneratorParser.JObject[] j = gepard.parseFile(NameMaps.box_name, new Settings("Blueprintempty"), true);
        Assertions.assertEquals(2, j.length, "Tokenizer sollte zwei obj liefern");
        String expected0 = "JObject [config=Settings [name=Blueprintempty, unknownPolicy=true, settings={lilly-author=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-author, type=IS_TEXT, value=Knödelblödel], patterns=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=patterns, type=IS_TEXT, value=Mitschrieb], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Ich-bin-doof], lilly-nameprefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-nameprefix, type=IS_TEXT, value=Waffel-]}], name=nmap]";
        String expected1 = "JObject [config=Settings [name=Blueprintempty, unknownPolicy=true, settings={lilly-author=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-author, type=IS_TEXT, value=Schlingelwingel], lilly-semester=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-semester, type=IS_TEXT, value=2], patterns=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=patterns, type=IS_TEXT, value=GDBS,pdp,PDP,PdP], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=Paradigmen-der-Programmierung], lilly-vorlesung=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-vorlesung, type=IS_TEXT, value=PDP]}], name=nmap]";
        Assertions.assertEquals(expected0, j[0].toString());
        Assertions.assertEquals(expected1, j[1].toString());

        // Bis hier hin sollte alles schon klappen ^^
        Settings s = NameMaps.getInstance().parseBox(j[0],false);
        Assertions.assertEquals("nmap", s.getName(), "Boxname sollte nmap sein");
        Assertions.assertEquals("Mitschrieb:lilly-author=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-author, type=IS_TEXT, value=Knödelblödel]\nlilly-nameprefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-nameprefix, type=IS_TEXT, value=Waffel-]\n", s.getValue("Ich-bin-doof"), "Box sollte entsprechend passen!");
        
        /**
         * Assertions.assertEquals("buildrule", s.getName(),"Name soll default sein");
         * Assertions.assertEquals("Standard", s.getValue("display-name"),"Anzeige-name
         * soll passen"); Assertions.assertEquals("MY-DEFAULT-",
         * s.getValue("nameprefix"),"Namenspräfix soll passen");
         */
        // this does work - just test with copying color information
        // TODO andere Daten
    }

    /**
     * TODO: Es wird gewünscht, dass GepardBox einen FunktionCollector unterhält der
     * einen Boxnamen auf die jeweilige ParseBox Methode abbildet. Gepard soll dann
     * mit Interpret die jeweilige Funktion aufrufen: Also soll zum Beispiel: BEGIN
     * buildrule: ... END; dafür sorgen, dass GePard direkt
     * {@link Buildrules#parseBox(GeneratorParser.JObject, boolean)} aufruft und das
     * Ergebnis verarbeitet. Beachte bei der Erkennung zudem:
     * {@link Buildrules#box_name}
     *
     */
}
