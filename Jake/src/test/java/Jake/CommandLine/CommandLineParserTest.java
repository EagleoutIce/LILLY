package Jake.CommandLine; /**
              * @file Jake.CommandLineParserTest.java
              * @author Florian Sihler
              * @version 2.0.0
              *
              * @brief Testet die Deifnitionen auf plausibilität
              */

import de.eagle.lillyjakeframework.core.CoreSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.eagle.lillyjakeframework.cmdline.CommandLineParser;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

/**
 * Testet den CommandLineParser
 */
@Tag("General")
@Tag("CommandLineParser")
@DisplayName("Test des CommandLineParsers")
class CommandLineParserTest {

    /**
     * Testet, ob strip_modifications funktioniert wie erwartet
     *
     * @see de.eagle.lillyjakeframework.cmdline.CommandLineParser
     */
    @ParameterizedTest(name = "[CmdLineParser] Teste strip_modifications mit: {0}")
    @Tag("Meta")
    @CsvSource({ "Unverändert, Unverändert", "OhnePunkt:, OhnePunkt", "AddDoppelpunkt+:, AddDoppelpunkt",
            ":FalscherPunkt?, #", // Leere Zeichenkette
            "Wäffle:Delüxe, Wäffle" })
    @DisplayName("[CmdLineParser] Teste strip_modifications")
    void _test_cmd_line_strip_modification(String arg, String expect) {
        Assertions.assertEquals(expect.replace("#", ""), CommandLineParser.strip_modifications(arg));
    }

    /**
     * Testet, das parsen von Kommandozeilenargumenten
     *
     * @see de.eagle.lillyjakeframework.cmdline.CommandLineParser
     */
    @ParameterizedTest(name = "[CmdLineParser] Teste parse_next_arg mit: {0}")
    @Tag("Parse")
    @CsvSource({ "name#TollerName#operation#waffel#testtoggle#false, -name:#TollerName#waffel#-testtoggle", // querbeet
            "testtoggle#true#name#Hallo Mama, -testtoggle#-testtoggle#-testtoggle#-name:#Hallo Mama#-testtoggle", // toggle
            "operation#config#file#test.conf, test.conf", // config test
            "operation#file_compile#file#test.tex, test.tex", // file_compile test
            "operation#file_compile#file#Abgabe-GdBS-Blatt2.tex#lilly-n#2#lilly-modes#uebungsblatt#testtoggle#true#lilly-nameprefix#FlorianS-FelixR-#lilly-author#Florian Sihler, "
                    + "Abgabe-GdBS-Blatt2.tex#-lilly-n:#2#-lilly-modes:#uebungsblatt#-error-toggle#-lilly-nameprefix:#FlorianS-FelixR-#-lilly-author:#Florian Sihler" // Complex
                                                                                                                                                                      // test
    })
    @DisplayName("[CmdLineParser] Teste parse_next_arg")
    void _test_cmd_line_parse_next_arg(String expect, String args) {
        String[] exp_arr = expect.split("\\s*#\\s*");
        String[] arg_arr = args.split("\\s*#\\s*");

        Settings args_settings = new Settings("ParseNextArg TestSettings");
        args_settings.put("testtoggle",
                SettingDeskriptor.create("testtoggle", "Testtoggle", eSetting_Type.IS_SWITCH, false, "true"));
        args_settings.put("name", SettingDeskriptor.create("name", "Name", eSetting_Type.IS_TEXT, false, ""));
        args_settings.put("operation", SettingDeskriptor.create("operation", "Operation", eSetting_Type.IS_TEXT, false, ""));
        args_settings.put("lilly-n", SettingDeskriptor.create("lilly-n", "lilly-n", eSetting_Type.IS_TEXT, false, ""));
        args_settings.put("error-toggle", SettingDeskriptor.create("error-toggle", "error-toggle", eSetting_Type.IS_SWITCH, false, ""));
        args_settings.put("lilly-modes", SettingDeskriptor.create("lilly-modes", "lilly-modes", eSetting_Type.IS_TEXT, false, ""));
        args_settings.put("lilly-author", SettingDeskriptor.create("lilly-author", "lilly-author", eSetting_Type.IS_TEXT, false, ""));
        args_settings.put("lilly-nameprefix", SettingDeskriptor.create("lilly-nameprefix", "lilly-nameprefix", eSetting_Type.IS_TEXT, false, ""));
        CommandLineParser.parse_args(arg_arr, args_settings);

        for (int i = 0; i < exp_arr.length; i += 2) { // teste ob Einstellungen passen
            Assertions.assertEquals(exp_arr[i + 1], args_settings.getValue(exp_arr[i]),
                    "Einstellung muss identisch sein");
        }
    }

    @Tag("Parse")
    @DisplayName("[CmdLineParser] Teste parse_next_arg")
    @Test
    void _test_cmd_line_what_shortcut() {
        Settings args_settings = new Settings("ParseNextArg TestSettings");
        args_settings.put("what",
                SettingDeskriptor.create("what", "testwhat", eSetting_Type.IS_TEXT, false, ""));
        CommandLineParser.parse_args(new String[]{"dump",":testwhat",""}, args_settings);
        Assertions.assertEquals("testwhat", args_settings.getValue("what"));
    }

    //TODO: Teste Locks :D
}
