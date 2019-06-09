package Jake.Core; /**
 * @file CoreFunctionsTest.java
 * @author Raphael Straub
 * @version 1.0.10
 * @brief Testet die Funktionen in CoreFunctions.java
 */

import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.lillyjakeframework.core.CoreFunctions;

import de.eagle.util.datatypes.Settings;
import org.junit.jupiter.api.*;

import java.io.IOException;


@Tag("CoreFunctions")
@DisplayName("Test der Funktionen in CoreFunctions")
public class CoreFunctionsTest {

    /**
     * Testet nicht wirklich etwas, aber deer Output von fkt_help ist sehr gut
     */
    @Test
    @Order(1)
    @DisplayName("Testet den fkt_help output")
    void _test_fkt_help() {
        CoreFunctions.fkt_help(null);
    }

    /**
     * Testet, ob Settings.dump() funktioniert, wenn keine .conf datei angegeben wird
     *
     * @throws IOException Wird von settings.dump() verlangt, aber wird nie geworfen, da keine .conf datei geöffnet wird
     */
    @Test
    @Order(2)
    @DisplayName("Testet Settings.dump() ohne .conf datei")
    void _test_Settings_dump_default() throws IOException {
        String[] arg_arr = "-name:#TollerName#waffel#-testtoggle".split("\\s*#\\s*");
        String[] exp_arr = "name#TollerName#operation#waffel#testtoggle#false".split("\\s*#\\s*");
        Settings settings = new Settings("TestSettings");
        CommandLineParser.parse_args(arg_arr, settings);
        String[] a = settings.dump();
        for (int i = 0; i < a.length; i++)
            Assertions.assertTrue(a[i].contains(exp_arr[2 * i + 1]));

    }


    /**
     * Testet, ob Settings.dump() funktioniert, wenn eine .conf datei angegeben wird
     *
     * @throws IOException Wenn die gegebene .conf datei nicht gefunden wird
     */
    @Test
    @Order(3)
    @DisplayName("Testet Settings.dump() mit .conf datei")
    void _test_Settings_dump_conf() throws IOException {
        String[] arg_arr = "/Configs/WorkingExample.conf".split("\\s*#\\s*");
        Settings settings = new Settings("TestSettings");
        CommandLineParser.parse_args(arg_arr, settings);
        String[] a = settings.dump();
        for (String b : a) {
            System.out.println(b);
            Assertions.assertTrue(("  lilly-print-name    : \033[2;3;51m[Druck-]" +
                    "  file                : \033[2;3;51m[Mitschrieb-GDBS.tex]\n" +
                    "  debug               : \033[2;3;51m[true]\n" +
                    "  lilly-boxes         : \033[2;3;51m[LIMERENCE]\n" +
                    "  lilly-modes         : \033[2;3;51m[c_print c_default]\n" +
                    "  lilly-show-boxname  : \033[2;3;51m[false]\n" +
                    "  operation           : \033[2;3;51m[file_compile]\n" +
                    "  lilly-external      : \033[2;3;51m[true]").contains(b), "");
        }
    }

}
