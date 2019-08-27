package Jake.Translators;

import de.eagle.lillyjakeframework.translators.SettingsTranslator;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;

@Tag("SettingsTranslator")
@DisplayName("Testet den SettingsTranslator")
public class SetttingsTranslatorTest {

        @Test @Order(1)
        @DisplayName("[SettingsTranslator] Teste ob das Mapping funktioniert")
        void _test_settings_translator_mapping() throws FileNotFoundException {
            SettingsTranslator settingsTranslator = new SettingsTranslator("TestSettingsTranslator","/translators/CoreSettings.trans");

            Assertions.assertEquals(settingsTranslator.getValue("S_VERSION"), "Version");
            Assertions.assertEquals(settingsTranslator.getValue("S_LILLY_PATH"), "lilly-path");
            Assertions.assertEquals(settingsTranslator.getValue("S_LILLY_SIGNATURE_COLOR"), "lilly-signatur-farbe");
        }

        @Test @Order(2)
        @DisplayName("[SettingsTranslator] Teste ob das Mapping funktioniert. Hier mit einer manuellen Konfiguration")
        void _test_settings_translator_mapping_2() throws FileNotFoundException {
            SettingsTranslator settingsTranslator = new SettingsTranslator("TestSettingsTranslator","/Translators/Example.trans");

            Assertions.assertEquals(settingsTranslator.getValue("Test"), "Test");
            Assertions.assertEquals(settingsTranslator.getValue("HALLO"), "Welt");
            Assertions.assertEquals(settingsTranslator.getValue("Ich_Mag"), "Zug");
        }
}
