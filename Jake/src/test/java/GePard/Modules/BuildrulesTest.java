package GePard.Modules;

import GePard.Parser.GepardTest;
import de.eagle.gepard.modules.Buildrules;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

/**
 * Diese Klasse spezifiziert die Anforderungen an
 * das buildrules Modul.
 * Die Entsprechende Erweiterungen hat alle folgende Tests zu bestehen
 * an den Tests selbst darf nur mit Rücksprache eine Veränderung
 * durchgeführt werden!
 *
 * @implNote Ziel ist es, dass es über die Box: 'buildrule' möglich ist
 *           Einstellungen zu treffen in wie weit die Dokumente
 *           kompiliert werden
 *
 * Die entsprechende C++-datei lautet j_buildrules.cpp (/.hpp)
 *
 *
 * @author Florian Sihler
 *
 * @see de.eagle.gepard.parser.GeneratorParser
 */
@Tag("Module")
@Tag("Buildrules")
public class BuildrulesTest {

    @ParameterizedTest @Tag("GePard") @Order(1)
    @DisplayName("[Buildrules] Überprüft erwartende Blueprint-Settings")
    @CsvSource({"name,,true", "display-name,,true","lilly-mode,,true",
                "complete,false,false", "complete-prefix,c_,false",
                "lilly-complete-prefix,COMPLETE-,false", "nameprefix,#,false",
                "lilly-loader,\\\\input{$(INPUTDIR)$(TEXFILE)},false"})
    void _test_gepard_buildrules_blueprint(String name, String expected, String mandatory) {
        SettingDeskriptor<String> s = Buildrules.getBlueprint().get(name);
        Assertions.assertNotNull(s,"Die Einstellung muss Existieren");
        if(mandatory.equals("true")){
            Assertions.assertTrue(s.isMandatory, "Die Einstellung soll verpflichtend sein!");
        } else {
            Assertions.assertEquals(expected.replace("#", ""), s.getValue(), "Die Einstellung soll den Wert haben");
        }
    }

    /**
     * Testet ob die jeweiligen Defaults existieren
     * testet nicht den Inhalt, der allerdings entsprechend sein sollte
     * @param shoud_exist Buildmode den es geben soll
     */
    @ParameterizedTest @Tag("GePard") @Order(2)
    @DisplayName("[Buildrules] Überprüft erwartende Defaults")
    @ValueSource(strings = {"default","print","uebungsblatt","c_default","c_print"})
    void _test_gepard_buildrules_defaults(String shoud_exist){
        Assertions.assertNotNull(Buildrules.getDefaults().get(shoud_exist),"Default-Einstellung muss existieren");
    }

    @Test @Tag("GePard") @Order(3)
    @DisplayName("[Buildrules] Überprüft das Parsen einer Regel")
    void _test_gepard_buildrules_rule() throws IOException {
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/BuildruleSimple.gpd").getFile());
        GeneratorParser.JObject[] j = gepard.parseFile(Buildrules.box_name, new Settings("Blueprintempty"),true );
        Assertions.assertEquals(1,j.length,"Tokenizer sollte ein obj liefern");
        String expected = "JObject [config=Settings [name=Blueprintempty, unknownPolicy=true, settings={lilly-mode=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-mode, type=IS_TEXT, value=default], lilly-complete-prefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-complete-prefix, type=IS_TEXT, value=COMPLETE-], display-name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=display-name, type=IS_TEXT, value=Standard], complete-prefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=complete-prefix, type=IS_TEXT, value=c_], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=default], nameprefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=nameprefix, type=IS_TEXT, value=MY-DEFAULT-], complete=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=complete, type=IS_TEXT, value=false]}], name=buildrule]";
        Assertions.assertEquals(expected,j[0].toString());
        // Bis hier hin sollte alles schon klappen ^^
        Settings s = Buildrules.parseBox(j[0],false);
        Assertions.assertEquals("default", s.getName(),"Name soll default sein");
        Assertions.assertEquals("Standard", s.getValue("dislay-name"),"Anzeige-name soll pasen");
        Assertions.assertEquals("MY-DEFAULT-", s.getValue("nameprefix"),"Namenspräfix soll passen");
        // TODO andere Daten
    }

    /**
     * TODO:
     *  Es wird gewünscht, dass GepardBox einen FunktionCollector unterhält der einen Boxnamen auf die jeweilige
     *  ParseBox Methode abbildet. Gepard soll dann mit Interpret die jeweilige Funktion aufrufen:
     *  Also soll zum Beispiel:
     *      BEGIN buildrule:
     *          ...
     *      END;
     *  dafür sorgen, dass GePard direkt {@link Buildrules#parseBox(GeneratorParser.JObject, boolean)}
     *  aufruft und das Ergebnis verarbeitet. Beachte bei der Erkennung zudem: {@link Buildrules#box_name}
     *
     */
}
