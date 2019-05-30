import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static de.eagle.lillyjakeframework.logging.JakeLogger.getLogger;

@Tag("Meta")
class LogTest {
    @Test
    void _test_logger() {
        getLogger().info("Hallo Welt");
    }
}
