package GePard.Modules;

import de.eagle.gepard.modules.Buildrules;
import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

/**
 * Diese Klasse spezifiziert die Anforderungen an das buildrules Modul. Die
 * Entsprechende Erweiterungen hat alle folgende Tests zu bestehen an den Tests
 * selbst darf nur mit Rücksprache eine Veränderung durchgeführt werden!
 *
 * @implNote Ziel ist es, dass es über die Box: 'buildrule' möglich ist
 *           Einstellungen zu treffen in wie weit die Dokumente kompiliert
 *           werden
 *
 *           Die entsprechende C++-datei lautet j_buildrules.cpp (/.hpp)
 *
 *
 * @author Florian Sihler
 *
 * @see de.eagle.gepard.parser.GeneratorParser
 */
@Tag("Module")
@Tag("Buildrules")
public class BuildrulesTest {

    @ParameterizedTest
    @Tag("GePard")
    @Order(1)
    @DisplayName("[Buildrules] Überprüft erwartende Blueprint-Settings")
    @CsvSource({ "name,,true", "display-name,,true", "lilly-mode,,true", "complete,false,false",
            "complete-prefix,c_,false", "lilly-complete-prefix,COMPLETE-,false", "nameprefix,#,false",
            "lilly-loader,\\\\ignorespaces\\\\noindent \\\\input{$(INPUTDIR)$(TEXFILE)},false" })
    void _test_gepard_buildrules_blueprint(String name, String expected, String mandatory) {
        SettingDeskriptor<String> s = Buildrules.getBlueprint().get(name);
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
     * @param should_exist Buildmode den es geben soll
     * @param should_be    Wert der erhalten werden soll
     */
    @ParameterizedTest
    @Tag("GePard")
    @Order(2)
    @DisplayName("[Buildrules] Überprüft erwartende Defaults")
    @CsvSource(value = {
            "default~SettingDeskriptor [brief=Standard-Buildrule ohne Boni :D, isMandatory=false, name=default, type=IS_TEXT, value=./OUTPUT/!\\\\providecommand\\\\LILLYxMODE{default}\\\\providecommand\\\\LILLYxMODExEXTRA{FALSE}!\\\\input{$(INPUTDIR)$(TEXFILE)}!Generiere: Standard]",
            "print~SettingDeskriptor [brief=Druck-Buildrule ohne Boni :D, isMandatory=false, name=print, type=IS_TEXT, value=./OUTPUT/!\\\\providecommand\\\\LILLYxMODE{print}\\\\providecommand\\\\LILLYxMODExEXTRA{FALSE}!\\\\input{$(INPUTDIR)$(TEXFILE)}!Generiere: Druck]",
            "uebungsblatt~SettingDeskriptor [brief=Übungsblatt-Buildrule, erwartet Dokument ohne \\begin usw., isMandatory=false, name=uebungsblatt, type=IS_TEXT, value=./OUTPUT/!\\\\providecommand\\\\LILLYxMODE{default}\\\\providecommand\\\\LILLYxMODExEXTRA{TRUE}!\\\\documentclass[Typ=Uebungsblatt${_C}Vorlesung=${VORLESUNG}${_C}n=${N}${_C}Semester=${SEMESTER}]{Lilly}\\\\begin{document}\\\\input{$(INPUTDIR)$(TEXFILE)}\\\\end{document}!Generiere: Übungsblatt]",
            "c_default~SettingDeskriptor [brief=Complete Standard-Buildrule, isMandatory=false, name=c_default, type=IS_TEXT, value=./OUTPUT/COMPLETE-!\\\\providecommand\\\\LILLYxMODE{default}\\\\providecommand\\\\LILLYxMODExEXTRA{TRUE}!\\\\input{$(INPUTDIR)$(TEXFILE)}!Generiere: COMPLETE-Standard]",
            "c_print~                        \"c_print~SettingDeskriptor [brief=Complete Druck-Buildrule, isMandatory=false, name=c_print, type=IS_TEXT, value=./OUTPUT/COMPLETE-Druck!\\\\\\\\providecommand\\\\\\\\LILLYxMODE{print}\\\\\\\\providecommand\\\\\\\\LILLYxMODExEXTRA{TRUE},\\\"\\\\\\\\input{$(INPUTDIR)$(TEXFILE)}\\\"]\"},delimiter = '~')" }, delimiter = '~')
    void _test_gepard_buildrules_defaults(String should_exist, String should_be) {
        Assertions.assertNotNull(Buildrules.getDefaults().get(should_exist), "Default-Einstellung muss existieren");
        // JakeWriter.out.println(Buildrules.getDefaults().get(should_exist));
        // TODO hihi - color information doesn't copy from output

        // Assertions.assertEquals(Buildrules.getDefaults().get(should_exist).toString(),
        // should_be);
    }

    @Test
    @Tag("GePard")
    @Order(3)
    @DisplayName("[Buildrules] Überprüft das Parsen einer Regel")
    void _test_gepard_buildrules_rule() throws IOException {
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/BuildruleSimple.gpd").getFile());
        GeneratorParser.JObject[] j = gepard.parseFile(Buildrules.box_name, new Settings("Blueprintempty"), true);
        Assertions.assertEquals(1, j.length, "Tokenizer sollte ein obj liefern");
        String expected = "JObject [config=Settings [name=Blueprintempty, unknownPolicy=true, settings={lilly-mode=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-mode, type=IS_TEXT, value=default], lilly-complete-prefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=lilly-complete-prefix, type=IS_TEXT, value=COMPLETE-], display-name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=display-name, type=IS_TEXT, value=Standard], complete-prefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=complete-prefix, type=IS_TEXT, value=c_], name=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=name, type=IS_TEXT, value=default], nameprefix=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=nameprefix, type=IS_TEXT, value=MY-DEFAULT-], complete=SettingDeskriptor [brief=Unknown Setting, isMandatory=false, name=complete, type=IS_TEXT, value=false]}], name=buildrule]";
        Assertions.assertEquals(expected, j[0].toString());
        // Bis hier hin sollte alles schon klappen ^^
        Settings s = Buildrules.parseBox(j[0], false);
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
