package Jake.Core; /**
                   * @file Jake.DefinitionsTest.java
                   * @author Florian Sihler
                   * @version 2.0.0
                   *
                   * @brief Testet die Definitionen auf plausibilität
                   */

import de.eagle.lillyjakeframework.core.Definitions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testet die Definitionen auf plausibilität
 */
@Tag("General")
@Tag("Definitions")
@DisplayName("Test der DefinitionsKlasse (validität der Einstellungen)")
class DefinitionsTest {

    /**
     * Testet, ob JAKE_VERSION und JAKE_VERSION_NUM übereinstimmen
     *
     * @see de.eagle.lillyjakeframework.core.Definitions
     */
    @Test
    @Tag("Meta")
    @DisplayName("[Definitions] Teste Validität der Versionseinstellung")
    void _test_version_number_integrity() {
        Assertions.assertEquals(Definitions.JAKE_VERSION.replaceAll("\\.", ""),
                Integer.toString(Definitions.JAKE_VERSION_NUM));
    }

}
