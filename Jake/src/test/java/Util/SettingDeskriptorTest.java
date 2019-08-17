package Util; /**
              * @file Util.SettingDeskriptorTest.java
              * @author Florian Sihler
              * @version 2.0.0
              *
              * @brief Teste den SettingDeskriptor auf Funktionalität
              */

import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.SettingDeskriptorStringList;
import de.eagle.util.exceptions.MandatorySettingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
}
