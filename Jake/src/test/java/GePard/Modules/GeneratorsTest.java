package GePard.Modules;

import de.eagle.gepard.modules.Generators;
import de.eagle.gepard.modules.Projects;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Florian Sihler
 *
 * @see GeneratorParser
 */
@Tag("Module")
@Tag("Generators")
public class GeneratorsTest {

    @ParameterizedTest
    @Tag("GePard")
    @Order(1)
    @DisplayName("[Generators] Überprüft erwartende Blueprint-Settings")
    @CsvSource({ "name,,true", "template-file,,true", "target-mode,,true", "brief,#,false"})
    void _test_gepard_projects_blueprint(String name, String expected, String mandatory) {
        SettingDeskriptor<String> s = Generators.getInstance().getBlueprint().get(name);
        Assertions.assertNotNull(s, "Die Einstellung " + name + " muss Existieren");
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
     */
    @Test
    @Tag("GePard")
    @Order(2)
    @DisplayName("[Generators] Überprüft erwartende Defaults")
    void _test_gepard_projects_defaults() {
        // There are no defaults right now
    }

    @Test
    @Tag("GePard")
    @Order(3)
    @DisplayName("[Generators] Überprüft das Parsen einer Regel")
    void _test_gepard_projects_rule() throws IOException {
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/GeneratorSimple.gpd").getFile());
        GeneratorParser.JObject[] j = gepard.parseFile(Generators.box_name, new Settings("Generatorsempty"), true);
        Assertions.assertEquals(1, j.length, "Tokenizer sollte ein obj liefern");
        String expected = "Settings [name=generator, unknownPolicy=true, settings={0example!another-var!I choose you, pikachuuu!VARIABLE_A!Bitte setze mich=SettingDeskriptor [brief=Box-Deskriptor für Project (Gepard), isMandatory=false, name=0example!another-var!I choose you, pikachuuu!VARIABLE_A!Bitte setze mich, type=IS_TEXT, value=/isch/bins/template]}]";
        Settings s = Generators.getInstance().parseBox(j[0], false);
        Assertions.assertEquals(expected, s.toString());
        // System.out.println(s);
    }

    /*@Test
    @Tag("GePard")
    @Order(4)
    @DisplayName("[Generators] Überprüft die Resolution der Namen")
    void _test_gepard_projects_get_all_project_names() throws IOException {
        GeneratorParser gepard = new GeneratorParser(
                this.getClass().getResource("/Gepard/ProjectMultiple.gpd").getFile());
        GeneratorParser.JObject[] j = gepard.parseFile(Projects.box_name, new Settings("Projectsempty"), true);
        Assertions.assertEquals(5, j.length, "Tokenizer sollte fünf Objekte liefern");
        Settings s = Projects.getInstance().parseRules(j, false);
        String[] names = Projects.getInstance().getAllProjectNames(s);
        Assertions.assertEquals(5, names.length, "5 Elemente erwartet");
        for (String r : new String[] {"example", "example2", "example3", "example4", "example5"}) {
            Assertions.assertTrue(Arrays.asList(names).contains(r), r + " Sollte als Name geliefert werden.");
        }
    }*/
}
