package GePard.Modules;

import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.Executer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

/**
 * Diese Klasse spezifiziert die Anforderungen an das Expandables Modul. Die
 * entsprechende Erweiterungen hat alle folgende Tests zu bestehen an den Tests
 * selbst darf nur mit Rücksprache eine Veränderung durchgeführt werden!
 *
 * @implNote Ziel ist es, dass es über die Box: 'expandable' möglich ist
 *           Einstellungen zu konfigurieren, die entsprechend in einem String
 *           ähnlich der in Bash oder anderen Shells aufgeführten Umgebungsvariablen
 *           zu Konfigurieren und expandieren zu lassen.
 *           Rekursive Expansionen werden durch {@link Definitions#MAX_SETTINGS_REC}
 *           abgefangen. Weiter/Tiefer verschachtelte Expansionen sind weder
 *           vorgehsehen noch zugelassen.
 *
 *
 *
 * @author Florian Sihler
 *
 * @see GeneratorParser
 */
@Tag("Module")
@Tag("Expandables")
public class ExpandablesTest {

    @Test
    @Tag("GePard")
    @Order(1)
    @DisplayName("[Expandables] Überprüft erwartende Blueprint-Settings")
    void _test_gepard_expandables_blueprint() {
        Assertions.assertTrue(true, "Die Blaupause für Expandables ist uninteressant");
    }

    /**
     * Testet ob die jeweiligen Defaults existieren testet nicht den Inhalt, der
     * allerdings entsprechend sein sollte
     *
     * @param should_exist Expandable den es geben soll
     */
    @ParameterizedTest
    @Tag("GePard")
    @Order(2)
    @DisplayName("[Expandables] Überprüft erwartende Defaults auf Existenz")
    @CsvSource(value = {"TEXFILE", "BASENAME", "FINALNAME", "LOGFILE", "PDFFILE", "LATEXARGS", "OUTPUTDIR", "INPUTDIR",
                "BOXMODES", "CLEANTARGETS", "SIGNATURECOL", "AUTHOR", "AUTHORMAIL", "NAMEPREFIX", "SEMESTER", "VORLESUNG",
                "LILLY_CONFIGS_PATH", "N", "JOBCOUNT", "_LILLYARGS", "HOME", "TRUE", "FALSE", "S_TRUE", "S_FALSE",
                "@JAKEVER", /* "@JAKECDATE", "@JAKECTIME",*/ "@GITHUB", "@CONFPATH", "@AUTONUM", "@WAFFLE" /* Sehr Wichtig! */,
                "@SELTEXF", "@SELCONF"})
    void _test_gepard_expandables_defaults(String should_exist) {
        Assertions.assertNotNull(Expandables.getInstance().getDefaults().get(should_exist), "Default-Einstellung (" + should_exist + ") muss existieren");
    }

    @Test
    @Tag("GePard")
    @Order(3)
    @DisplayName("[Expandables] Überprüft die Konstanten der Expandables Defaults")
    void _test_gepard_expandables_constants() throws IOException {
        Settings defaults = Expandables.getInstance().getDefaults();
        Assertions.assertEquals(Definitions.JAKE_VERSION,defaults.getValue("@JAKEVER"));
        Assertions.assertEquals(Definitions.S_TRUE,defaults.getValue("S_TRUE"));
        Assertions.assertEquals(Definitions.S_FALSE,defaults.getValue("S_FALSE"));
        Assertions.assertEquals(",",defaults.getValue("_C"));
        Assertions.assertEquals("https://github.com/EagleoutIce/LILLY",defaults.getValue("@GITHUB"));
    }

    @Test
    @Tag("GePard")
    @Order(4)
    @DisplayName("[Expandables] Überprüft das Parsen einer Datei mit Boxen")
    void _test_gepard_expandables_parse() throws IOException {
        // Get the config file to a general Location
        String path = Executer.getSHPath("/Gepard/ExpandablesSimple.gpd");
        Settings expandables = Expandables.getInstance().getExpandables(path);

        // The Defaults shall still exist/be altered:
        Assertions.assertEquals(Definitions.JAKE_VERSION,expandables.getValue("@JAKEVER"));
        Assertions.assertEquals("Ist Wichtig",expandables.getValue("SUPERWAFFEL")); // Set in Configfile :D
        Assertions.assertEquals("FALSE",expandables.getValue("S_TRUE")); // Swapped in Configfile :D
        Assertions.assertEquals("TRUE",expandables.getValue("S_FALSE")); // Again, swapped!
        // -- complex Ones
        Assertions.assertEquals("${HOME}/Tolle Welt/${TRUE}",expandables.getValue("SuperHome"));
        Assertions.assertEquals("@[CONFPATH]/Layout",expandables.getValue("LayoutConfig"));
        // They will be expanded correctly in fullexpand.
    }

}
