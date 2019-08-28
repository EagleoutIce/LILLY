package Jake.CommandLine;
/**
 * @file AutocompleteTest.java
 *
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * Testet {@link de.eagle.lillyjakeframework.cmdline.Autocomplete} auf Integrität
 */

import de.eagle.lillyjakeframework.cmdline.Autocomplete;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@Tag("Autocomplete")
@DisplayName("Testet das Autocomplete Feature")
public class AutocompleteTest {

    private static Stream<Arguments> _provider_test_was_there_what() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(new String[0], false),
                Arguments.of(new String[] {"Test.tex"}, true),
                Arguments.of(new String[] {"_get"}, false),
                Arguments.of(new String[] {"config"}, true),
                Arguments.of(new String[] {"nutzlos","DEI"}, true),
                Arguments.of(new String[] {"nutzlos","REI"}, true),
                Arguments.of(new String[] {"nutzlos","GUI"}, true),
                Arguments.of(new String[] {"nutzlos", "wutzlos", "shutzlos"}, false),
                Arguments.of(new String[] {"nutzlos hutsbos", "wutzlos", "shutzlos"}, false),
                Arguments.of(new String[] {"nutzlos DEI", "wutzlos", "shutzlos"}, true)
        );
    }

    @ParameterizedTest
    @Order(1)
    @MethodSource("_provider_test_was_there_what")
    @DisplayName("[Autocomplete] Teste Validität von was_there_what")
    public void _test_was_there_what(String[] test, boolean expected) {
        Assertions.assertEquals(expected, Autocomplete.was_there_what(test), "Es wird dieses Ergebnis erwartet");
    }

}
