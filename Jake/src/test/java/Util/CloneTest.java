package Util; /**
 * @file Util.CloneTest.java
 * @author Florian Sihler
 * @version 1.0.10
 * 
 * @brief Überprüft Kopieroperationen auf Korrektheit
 */

import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testet kopier funktionalität einiger Klassen - einfach weil PassByReference nicht immer gewünscht ist hust hust
 */
@Tag("clone")
@DisplayName("Testet das kopieren einiger Objekte")
class CloneTest {


    /**
     * Generiert Einstellungen, die nur die Hallo Box erlauben
     * @return Einstellungen, welche nur die Hallo Box als Blueprint erlauben
     */
    Settings _generate_simple_settings(){
        HashMap<String, SettingDeskriptor<String>> testSets = new HashMap<>();
        testSets.put("Hallo", SettingDeskriptor.create("Hallo", "Hallo Welt Einstellung", eSetting_Type.IS_TEXT, false,"MAUUUUUUU"));

        Settings settings = new Settings("TestSettings", false, testSets);

        return settings;
    }


    /**
     * Überprüft die Funktionalität des Klonens
     *
     * @see de.eagle.lillyjakeframework.core.Definitions
     */
    @Test @DisplayName("[Cloner] Teste Simple kopie von Einstellungen")
    void _test_Settings_clone() {
        Settings set = _generate_simple_settings();
        Settings clone = set.cloneSettings();
        Assertions.assertTrue(set.equals(clone),"Das geklonte Einstellungsobjekt stimmt nicht mit der Quelle überein!");
    }

}
