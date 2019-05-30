import de.eagle.lillyjakeframework.core.Definitions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testet die Definitionen auf plausibilität
 */
@Tag("Description")
class DefinitionsTest {

    /**
     * Testet, ob JAKE_VERSION und JAKE_VERSION_NUM übereinstimmen
     *
     * @see de.eagle.lillyjakeframework.core.Definitions
     */
    @Test
    @Tag("Meta")
    void testVersionNumberIntegrity(){
        Assertions.assertEquals(Definitions.JAKE_VERSION.replaceAll("\\.",""),
                                Integer.toString(Definitions.JAKE_VERSION_NUM));
    }

}
