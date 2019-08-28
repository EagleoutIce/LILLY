package Jake.Util;

/**
 * @file eSetting_TypeTest.java
 *
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * Testet {@link de.eagle.util.enumerations.eSetting_Type} auf Integrität
 */

import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.enumerations.eSetting_Type;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Tag("General")
@Tag("eSetting_Type")
@DisplayName("Test der Type-Tester")
class eSetting_TypeTest {

    @Test
    @Order(1)
    @DisplayName("[eSetting_Type] Check the CheckText Method")
    void _test_checkText() {
        Assertions.assertTrue(eSetting_Type.CheckText("Ich bin Text"), "Naja, schlägt immer an :D");
    }

    @Test
    @Order(2)
    @DisplayName("[eSetting_Type] Check the CheckPath Method")
    void _test_checkPath() {
        Assertions.assertTrue(eSetting_Type.CheckPath("/Hallo/Welt"), "Naja, schlägt immer an :D");
    }

    @Test
    @Order(3)
    @DisplayName("[eSetting_Type] Check the CheckFile Method")
    void _test_checkFile() throws IOException {
        File f = File.createTempFile("Hallo", ".tmp");
        Assertions.assertTrue(eSetting_Type.CheckFile(f.getAbsolutePath()), "Ist eine Datei");
        Assertions.assertFalse(eSetting_Type.CheckFile("Ich/Bin/KeinPfadLÖÖÖÖL"), "Ist eine Datei");

    }

    @Test
    @Order(4)
    @DisplayName("[eSetting_Type] Check the CheckOperation Method")
    void _test_checkOperation() {
        for(String s : CoreFunctions.functions_t.keySet()){
            Assertions.assertTrue(eSetting_Type.CheckOperation(s), "Operationen sollten korrekt erkannt werden");
        }
        Assertions.assertFalse(eSetting_Type.CheckOperation("DETLEFDIETEROPERATONDEREWIGENHÖLLE"), "Dies ist keine Operation :D");
    }

    @Test
    @Order(5)
    @DisplayName("[eSetting_Type] Check the CheckSetting Method")
    void _test_checkSetting() {
        for(var s : CoreSettings.getSettings()){
            Assertions.assertTrue(eSetting_Type.CheckSetting(s.getKey()), "Einstellungen sollten korrekt erkannt werden");
        }
        Assertions.assertFalse(eSetting_Type.CheckSetting("DETLEFDIETEROPERATONDEREWIGENHÖLLE"), "Dies ist keine Einstellung :D");
    }

    @Test
    @Order(6)
    @DisplayName("[eSetting_Type] Check the CheckSwitch Method")
    void _test_checkSwitch() {
        Assertions.assertTrue(eSetting_Type.CheckSwitch(Definitions.S_TRUE), "Das ist ein gültiger Wert für einen Switch");
        Assertions.assertTrue(eSetting_Type.CheckSwitch(Definitions.S_FALSE), "Das ist ein gültiger Wert für einen Switch");
        Assertions.assertFalse(eSetting_Type.CheckSwitch("GÜNTHEEER"), "Das ist kein gültiger Wert für einen Switch");

    }

    @Test
    @Order(7)
    @DisplayName("[eSetting_Type] Check the CheckNumeric Method")
    void _test_checkNumeric() {
        Assertions.assertTrue(eSetting_Type.CheckNumeric("42"), "Das ist eine (Ganz-)Zahl");
        Assertions.assertTrue(eSetting_Type.CheckNumeric("004"), "Das ist eine (Ganz-)Zahl");

        Assertions.assertFalse(eSetting_Type.CheckNumeric("-42"), "Das ist keine (Ganz-)Zahl");
        Assertions.assertFalse(eSetting_Type.CheckNumeric("3a"), "Das ist keine (Ganz-)Zahl");
        Assertions.assertFalse(eSetting_Type.CheckNumeric("0x12"), "Das ist keine dezimale (Ganz-)Zahl");
        Assertions.assertFalse(eSetting_Type.CheckNumeric("5.23"), "Das ist keine (Ganz-)Zahl");
        Assertions.assertFalse(eSetting_Type.CheckSwitch("GÜNTHEEER"), "Das ist keine (Ganz-)Zahl");

    }

    private static Stream<Arguments> _provider_test_isValid() {
        return Stream.of(
                Arguments.of(eSetting_Type.IS_TEXT, "Hallo Welt", true),
                Arguments.of(eSetting_Type.IS_PATH, "Hallo/Welt", true),
                Arguments.of(eSetting_Type.IS_FILE, "Hallo Welt", false),
                Arguments.of(eSetting_Type.IS_SWITCH, Definitions.S_TRUE, true),
                Arguments.of(eSetting_Type.IS_SWITCH, "Dieter", false),
                Arguments.of(eSetting_Type.IS_OPERATION, "file_compile", true),
                Arguments.of(eSetting_Type.IS_OPERATION, "DIETERDERWELT", false),
                Arguments.of(eSetting_Type.IS_SETTING, "lilly-n", true),
                Arguments.of(eSetting_Type.IS_SETTING, "ich-mag-züge-ganz-gern", false),
                Arguments.of(eSetting_Type.IS_NUM, "42", true),
                Arguments.of(eSetting_Type.IS_NUM, "-13", false),
                Arguments.of(eSetting_Type.IS_NUM, "Hallo Welt", false),
                Arguments.of(eSetting_Type.IS_LATEX, "Ich mag Günther", true)
        );
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("[eSetting_Type] Check the isValid Method")
    @MethodSource("_provider_test_isValid")
    void _test_isValid(eSetting_Type type, String val, boolean isValid) {
        Assertions.assertEquals(isValid, type.isValid(val), "Sollte übereinstiimen");
    }
}