import de.eagle.util.Setting_Deskriptor;
import de.eagle.util.exceptions.MandatorySettingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static de.eagle.util.enumerations.eSetting_Type.IS_OPERATION;

/**
 * Testet die Klasse Setting_Deskriptor auf Funktionalität
 *
 * @see Setting_Deskriptor
 */
@Tag("Setting_Deskriptor")
class Setting_DeskriptorTest {

    /**
     * Testet den 3-Argument Konstruktor
     */
    @Test
    @Order(1)
    void test_3_paramter_constructor() {
        Setting_Deskriptor<Integer> s = new Setting_Deskriptor<Integer>("TestDesk", "Testerklärung", IS_OPERATION);

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
    void test_4_paramter_constructor() {
        Setting_Deskriptor<Character> s = new Setting_Deskriptor<Character>("", "Sonnenschein", IS_OPERATION, true);

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
    void test_5_paramter_constructor() {
        Setting_Deskriptor<String> s = new Setting_Deskriptor<String>("Paul", "Rofll", IS_OPERATION, false,
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
    void test_is_mandatory() {
        Setting_Deskriptor<Character> s = new Setting_Deskriptor<Character>("", "", IS_OPERATION, true);
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
}
