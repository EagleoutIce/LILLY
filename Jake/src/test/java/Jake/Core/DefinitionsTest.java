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

    @Test
    @Tag("Meta")
    @DisplayName("[Definitions] Teste Validität von True und False")
    void _test_true_false_integrity() {
        Assertions.assertNotEquals(Definitions.S_TRUE, Definitions.S_FALSE, "Bei Gleichheit wären die Werte nicht mehr eindeutig identifizierbar.");
    }

    @Test
    @Tag("Meta")
    @DisplayName("[Definitions] Teste Validität relativeWorkingDir")
    void _test_set_and_get_relative_working_dir() {
        Assertions.assertEquals(Definitions.getRelativeWorkingDir(), "", "We want to start with an empty relative dir");
        Definitions.setRelativeWorkingDir("Hallo/Welt");
        Assertions.assertEquals(Definitions.getRelativeWorkingDir(), "Hallo/Welt", "set sollte wie erwartet funktionieren");
        Definitions.setRelativeWorkingDir("");
        Assertions.assertEquals(Definitions.getRelativeWorkingDir(), "", "set sollte wie erwartet funktionieren");

    }
}
