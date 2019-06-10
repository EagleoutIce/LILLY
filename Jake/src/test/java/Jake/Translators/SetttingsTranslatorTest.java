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
            SettingsTranslator settingsTranslator = new SettingsTranslator("TestSettingsTranslator");

            Assertions.assertEquals(settingsTranslator.getValue("S_VERSION"), "Version");
            Assertions.assertEquals(settingsTranslator.getValue("S_LILLY_PATH"), "lilly-path");
            Assertions.assertEquals(settingsTranslator.getValue("S_LILLY_SIGNATURE_COLOR"), "lilly-signatur-farbe");
        }

}
