package Util; /**
              * @file Util.SettingDeskriptorTest.java
              * @author Florian Sihler
              * @version 2.0.0
              *
              * @brief Teste den SettingDeskriptor auf Funktionalität
              */

import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.SettingDeskriptorStringList;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.exceptions.MandatorySettingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.eagle.util.enumerations.eSetting_Type.IS_OPERATION;
import static de.eagle.util.enumerations.eSetting_Type.IS_TEXTLIST;

/**
 * Testet die Klasse SettingDeskriptor auf Funktionalität
 *
 * @see SettingDeskriptor
 */
@Tag("SettingDeskriptor")
@DisplayName("Tests für den SettingsDeskriptor")
class SettingDeskriptorTest {

    /**
     * Testet den 3-Argument Konstruktor
     */
    @Test
    @Order(1)
    @DisplayName("[SettingDeskriptor] 3-Parameter Konstruktor")
    void _test_3_paramter_constructor() {
        SettingDeskriptor<Integer> s = SettingDeskriptor.create("TestDesk", "Testerklärung", IS_OPERATION);

        Assertions.assertNull(s.getValue());
        Assertions.assertFalse(s.isMandatory());
        Assertions.assertEquals("TestDesk", s.getName());
        Assertions.assertEquals("Testerklärung", s.brief);
        Assertions.assertEquals(IS_OPERATION, s.type);
    }

    /**
     * Testet den 4-Argument Konstruktor
     */
    @Test
    @Order(2)
    @DisplayName("[SettingDeskriptor] 4-Parameter Konstruktor")
    void _test_4_paramter_constructor() {
        SettingDeskriptor<Character> s = SettingDeskriptor.create("", "Sonnenschein", IS_OPERATION, true);

        try {
            Assertions.assertNull(s.getValue());
            Assertions.fail("getValue() liefert bei einem Verpflichtenden Argument keinen Fehler bei =null");
        } catch (MandatorySettingException ignored) {
        }
        Assertions.assertTrue(s.isMandatory());
        Assertions.assertEquals("", s.getName());
        Assertions.assertEquals("Sonnenschein", s.brief);
        Assertions.assertEquals(IS_OPERATION, s.type);
    }

    /**
     * Testet den 5-Argument Konstruktor = maximaler
     */
    @Test
    @Order(3)
    @DisplayName("[SettingDeskriptor] 5-Parameter Konstruktor")
    void _test_5_paramter_constructor() {
        SettingDeskriptor<String> s = SettingDeskriptor.create("Paul", "Rofll", IS_OPERATION, false,
                "StartwertMartlehrt");

        Assertions.assertEquals("StartwertMartlehrt", s.getValue());
        Assertions.assertFalse(s.isMandatory());
        Assertions.assertEquals("Paul", s.getName());
        Assertions.assertEquals("Rofll", s.brief);
        Assertions.assertEquals(IS_OPERATION, s.type);
    }

    /**
     * Testet ob die Verpflichtung von Argumenten funktioniert.
     */
    @Test
    @Order(4)
    @DisplayName("[SettingDeskriptor] Testet verpflichtende Argumente ")
    void _test_is_mandatory() {
        SettingDeskriptor<Character> s = SettingDeskriptor.create("", "", IS_OPERATION, true);

        // Expect Exception gibts auch, ja ist hier aber nervig :S

        try {
            Assertions.assertNull(s.getValue());
            Assertions.fail("getValue() liefert bei einem verpflichtenden Argument keinen Fehler bei =null");
        } catch (MandatorySettingException ignored) {
        }

        s.setValue('X');

        try {
            Assertions.assertEquals('X', s.getValue());
        } catch (MandatorySettingException ignored) {
            Assertions.fail("getValue() liefert bei einem gesetzten verpflichtenden Argument einen Fehler bei !=null");
        }

        Assertions.assertTrue(s.isMandatory());

    }

    /**
     * Testet ob gesperrte und ungesperrte Einstellungen so Verhalten wie es
     * gefordert ist!
     */
    @Test
    @Order(5)
    @DisplayName("[SettingDeskriptor] Testet .lock()")
    void _test_is_locked() {
        SettingDeskriptor<Character> s = SettingDeskriptor.create("", "", IS_OPERATION, true);

        Assertions.assertTrue(s.setValue('X'), "Das Setzen eines entsperrten Deskriptors muss funktionieren");
        Assertions.assertEquals('X', s.getValue(), "Der Wert sollte dem gesetzten Wert entsprechen");

        // now with lock
        s.lock(); // .lockdown()? hihi

        Assertions.assertTrue(s.isLocked(), "Natürlich sollte eine gesperrte Einstellung sich so präsentieren");
        Assertions.assertFalse(s.setValue('Y'), "Das Setzen eines gesperrten Deskriptors muss scheitern");
        Assertions.assertEquals('X', s.getValue(),
                "Der Wert sollte bei einem gesperrten Deskriptor unverändert bleiben");
    }

    /**
     * Testet ob .create() funktioniert
     */
    @Test
    @Order(6)
    @DisplayName("[SettingDeskriptor] Testet das erstellen von Listen über .create()")
    void _test_create_list() {
        SettingDeskriptor s = SettingDeskriptor.create("", "", IS_TEXTLIST, "", ':');

        String defmsg = "Das Setzen eines entsperrten Deskriptors muss funktionieren";
        String addmsg = "Der Wert sollte dem gesetzten Wert auf Basis von Separator";

        Assertions.assertTrue(((SettingDeskriptorStringList) s).addValue("X"), defmsg);
        Assertions.assertEquals("X", s.getValue(), addmsg);
        Assertions.assertTrue(((SettingDeskriptorStringList) s).addValue("Y"), defmsg);
        Assertions.assertEquals("X:Y", s.getValue(), addmsg);
        Assertions.assertTrue(((SettingDeskriptorStringList) s).addValue(":Y"), defmsg);
        Assertions.assertEquals("X:Y:Y", s.getValue(), addmsg);
        Assertions.assertTrue(((SettingDeskriptorStringList) s).addValue(":G:"), defmsg);
        Assertions.assertEquals("X:Y:Y:G:", s.getValue(), addmsg);
        Assertions.assertTrue(((SettingDeskriptorStringList) s).addValue(":Y"), defmsg);
        Assertions.assertEquals("X:Y:Y:G:Y", s.getValue(), addmsg);
    }


    private static Stream<Arguments> _provider_test_equal(){
        return Stream.of(
                Arguments.of(SettingDeskriptor.create("1-Detlef", "Dieter"),SettingDeskriptor.create("1-Detlef", "Dieter"),true),
                Arguments.of(SettingDeskriptor.create("2-Detlef", "Dieter"),SettingDeskriptor.create("2-Detlef", "Bieter"),true), // Beschreibungen haben keine Aussage auf Gleichheit der Einstellungen
                Arguments.of(SettingDeskriptor.create("3-Detlef", "Dieter",42),SettingDeskriptor.create("3-Detlef", "Dieter",42),true),
                Arguments.of(SettingDeskriptor.create("4-Detlef", "Dieter",42),SettingDeskriptor.create("4-Detlef", "Dieter",43),false),
                Arguments.of(SettingDeskriptor.create("5-Detlef", "Dieter", eSetting_Type.IS_FILE),SettingDeskriptor.create("5-Detlef", "Dieter", eSetting_Type.IS_FILE),true),
                Arguments.of(SettingDeskriptor.create("6-Detlef", "Dieter", eSetting_Type.IS_FILE),SettingDeskriptor.create("6-Detlef", "Dieter", eSetting_Type.IS_TEXT),false),
                Arguments.of(SettingDeskriptor.create("7-Detlef", "Dieter", eSetting_Type.IS_FILE,42),SettingDeskriptor.create("7-Detlef", "Dieter", eSetting_Type.IS_FILE,42),true),
                Arguments.of(SettingDeskriptor.create("8-Detlef", "Dieter", eSetting_Type.IS_FILE,43),SettingDeskriptor.create("8-Detlef", "Dieter", eSetting_Type.IS_FILE,42),false),
                Arguments.of(SettingDeskriptor.create("9-Detlef", "Dieter", eSetting_Type.IS_PATH,true,"4"),SettingDeskriptor.create("9-Detlef", "Dieter", eSetting_Type.IS_PATH,true,"4"),true),
                Arguments.of(SettingDeskriptor.create("10-Detlef", "Dieter", eSetting_Type.IS_PATH,false, "4"),SettingDeskriptor.create("10-Detlef", "Dieter", eSetting_Type.IS_PATH,true, "4"),false)
        );
    }

    @Test
    @Order(7)
    @DisplayName("[SettingDeskriptor] Teste .get() und .set()")
    void _test_get_and_set() {
        SettingDeskriptor<String> sd = SettingDeskriptor.create("SDName", "SDBrief", eSetting_Type.IS_TEXT, true, "SDWert");
        // -- get
        Assertions.assertEquals("SDName", sd.getName());
        Assertions.assertEquals("SDBrief", sd.getBrief());
        Assertions.assertEquals("SDWert", sd.getValue());
        Assertions.assertEquals(eSetting_Type.IS_TEXT, sd.getType());
        Assertions.assertTrue(sd.isMandatory());
        Assertions.assertFalse(sd.isLocked());
        sd.setValue("42"); sd.setName("Dieter");
        // -- set
        Assertions.assertEquals("Dieter", sd.getName());
        Assertions.assertEquals("42", sd.getValue());
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("[SettingDeskriptor] Testet Vergleiche durch .equals()")
    @MethodSource("_provider_test_equal")
    void _test_equal(SettingDeskriptor a, SettingDeskriptor b, boolean shouldBeEqual) {
        Assertions.assertEquals(shouldBeEqual,a.equals((b)), a + " und " + b);
    }
}
