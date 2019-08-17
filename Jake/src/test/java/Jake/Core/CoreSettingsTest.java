package Jake.Core;
/**
 * @file Jake.DefinitionsTest.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Testet die Definitionen auf plausibilität
 */

import de.eagle.lillyjakeframework.core.CoreSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static de.eagle.lillyjakeframework.core.Definitions.JAKE_VERSION;

/**
 * Testet die Einstellungen und Erweiterungen auf Plausibilität und Konsistenz
 */
@Tag("General")
@Tag("CoreSettings")
@DisplayName("Test der CoreSettings (validität der Einstellungen + Expand)")
class CoreSettingsTest {

    /**
     * Testet, ob JAKE_VERSION und JAKE_VERSION_NUM übereinstimmen
     *
     * @see de.eagle.lillyjakeframework.core.CoreSettings
     */
    @ParameterizedTest
    @Tag("Meta")
    @DisplayName("[CoreSettings] Überprüfe Expandieren")
    @CsvSource({ "Unverändert, Unverändert", "$Version," + JAKE_VERSION })
    void _test_expand_basics(String source, String expected) {
        Assertions.assertEquals(expected.replaceAll("#", ""), CoreSettings.expandFull(source));
    }

}
