package Jake.Core;
/**
 * @file Jake.DefinitionsTest.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Testet die Definitionen auf plausibilität
 */

import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.blueprints.Translator;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.enumerations.eSetting_Type;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.eagle.lillyjakeframework.core.Definitions.JAKE_VERSION;

/**
 * Testet die Einstellungen und Erweiterungen auf Plausibilität und Konsistenz
 */
@Tag("General")
@Tag("CoreSettings")
@DisplayName("Test der CoreSettings (validität der Einstellungen + Expand)")
class CoreSettingsTest {

    @Test
    @Order(1)
    void _test_get_and_set() {
        // Natürlich werden die Einstellungen danach wieder zurückgesetzt :D
        // -- get
        // Assertions.assertEquals("@[JAKEVER]", CoreSettings.get(CoreSettings.getTranslator().translate("S_VERSION")));
        // Assertions.assertEquals("@[JAKEVER]", CoreSettings.requestValue("S_VERSION"));

        boolean oldVal = CoreSettings.requestSwitch("S_LILLY_EXTERNAL");
        // -- set
        CoreSettings.set(CoreSettings.getTranslator().translate("S_LILLY_EXTERNAL"), !oldVal);
        Assertions.assertEquals(!oldVal, CoreSettings.requestSwitch("S_LILLY_EXTERNAL"));
        // -- reset
        CoreSettings.set(CoreSettings.getTranslator().translate("S_LILLY_EXTERNAL"), oldVal);
        Assertions.assertEquals(oldVal, CoreSettings.requestSwitch("S_LILLY_EXTERNAL"));
    }

    private static Stream<Arguments> _provider_test_expand_basics() {
        return Stream.of(
                    Arguments.of("Unverändert", "Unverändert"),
                    Arguments.of("$Version", JAKE_VERSION),
                    Arguments.of("Die: $operation", "Die: " + CoreSettings.requestValue("S_OPERATION")),
                    Arguments.of("Es gibt $lilly-external bäume? $42", "Es gibt " + CoreSettings.requestSwitch("S_LILLY_EXTERNAL") + " bäume? $42"),
                    Arguments.of("Es gibt $lilly-n bäume? $42", "Es gibt " + CoreSettings.requestValue("S_LILLY_N") + " bäume? $42")
        );
    }


    /**
     * Testet, ob JAKE_VERSION und JAKE_VERSION_NUM übereinstimmen
     *
     * @see de.eagle.lillyjakeframework.core.CoreSettings
     */
    @ParameterizedTest
    @Tag("Meta")
    @Order(2)
    @DisplayName("[CoreSettings] Überprüfe Expandieren")
    @MethodSource("_provider_test_expand_basics")
    void _test_expand_basics(String source, String expected) {
        Assertions.assertEquals(expected, CoreSettings.expandFull(source));
    }




}
