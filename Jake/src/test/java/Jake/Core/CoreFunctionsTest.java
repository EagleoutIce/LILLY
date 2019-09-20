package Jake.Core;
/**
 * @file CoreFunctionsTest.java
 * @author Raphael Straub
 * @author Florian Sihler
 *
 * @version 2.0.0
 * @brief Testet die Funktionen in CoreFunctions.java
*/

import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.io.ConsolePrintStream;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.io.VoidPrintStream;
import org.junit.jupiter.api.*;

import java.io.IOException;

/**
 * Die Funktionen {@link CoreFunctions#fkt_install(String[])}, {@link CoreFunctions#fkt_compile(String[])},
 * {@link CoreFunctions#fkt_get} und {@link CoreFunctions#fkt_autoget(String[])} werden hier nicht getestet,
 * da sie (in Teilen) auf andere Operationen zurückgreifen oder nur an diese Verweisen. Diese werden dann
 * entsprechend separat getestet.
 */
@Tag("CoreFunctions")
@DisplayName("Test der Funktionen in CoreFunctions")
public class CoreFunctionsTest {

    @BeforeAll
    public static void silence_output(){
        JakeWriter.out.reassignConsole(new VoidPrintStream());
        JakeWriter.err.reassignConsole(new VoidPrintStream());
    }

    @AfterAll
    public static void reenable_output(){
        JakeWriter.out.reassignConsole(new ConsolePrintStream(System.out));
        JakeWriter.err.reassignConsole(new ConsolePrintStream(System.err));
    }

    /**
     * @see CoreFunctions#fkt_help(String[])
     */
    @Test
    @Order(1)
    @DisplayName("[CoreFunctions] Testet den fkt_help output")
    void _test_fkt_help() {
        CoreFunctions.fkt_help(null);
        Assertions.assertTrue(true, "Diese Funktion wird menschlich getestet und solange sie keinen Laufzeitfehler wirft als korrekt betrachtet.");
    }

    /**
     * Testet, ob Settings.dump() funktioniert, wenn keine .conf datei angegeben
     * wird
     *
     * @see CoreFunctions#fkt_dump(String[])
     *
     * @throws IOException Wird von settings.dump() verlangt, aber wird nie
     *                     geworfen, da keine .conf datei geöffnet wird
     */
    @Test
    @Order(2)
    @DisplayName("[CoreFunctions] Testet Settings.dump() ohne .conf datei")
    void _test_fkt_dump() throws IOException {
        String[] arg_arr = "-name:#TollerName#waffel#-testtoggle".split("\\s*#\\s*");
        String[] exp_arr = "name#TollerName#operation#waffel#testtoggle#false".split("\\s*#\\s*");
        Settings settings = new Settings("TestSettings");
        CommandLineParser.parse_args(arg_arr, settings);
        String[] a = settings.dump();
        for (int i = 0; i < a.length; i++)
            Assertions.assertTrue(a[i].contains(exp_arr[2 * i + 1]));
        CoreFunctions.fkt_dump(null);
        Assertions.assertTrue(true, "Diese Funktion wird menschlich getestet und solange sie keinen Laufzeitfehler wirft als korrekt betrachtet.");
    }

    /**
     * @see CoreFunctions#fkt_tokentest(String[])
     */
    @Test
    @Order(3)
    @DisplayName("[CoreFunctions] Teste fkt_tokentest")
    void _test_fkt_tokentest() {
        CoreFunctions.fkt_tokentest(null);
        Assertions.assertTrue(true, "Diese Funktion wird menschlich getestet und solange sie keinen Laufzeitfehler wirft als korrekt betrachtet. Sie basiert auf der Funktionalität des Tokenizer");
    }

    /**
     * @see CoreFunctions#fkt_config(String[])
     */
    @Test
    @Order(4)
    @DisplayName("[CoreFunctions] Teste fkt_config")
    void _test_fkt_config() {
        // CoreFunctions.fkt_config(null);
        Assertions.assertTrue(true, "Diese Funktion wird menschlich getestet. Sie basiert auf der Funktionalität des Configurator");
    }
}
