
/**
 * @file LogTest.java
 * @author Florian Sihler
 * @version 1.0.10
 * 
 * @brief Überprüft die Funktionalität des Loggers
 * 
 * @see JakeLogger
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static de.eagle.util.logging.JakeLogger.*;

import java.util.logging.Level;

@Tag("Meta")
@DisplayName("Test der Log-Funktionalität")
class LogTest {
    @Test
    @DisplayName("[Log] Teste Log-Zugriffe (wird nicht wirklich getestet^^)")
    void _test_logger() {
        writeLoggerInfo("Info", "LogTest");
        writeLoggerWarning("Warning", "LogTest");
        writeLoggerError("Error", "LogTest");
        writeLoggerDebug1("Debug1", "LogTest");
        writeLoggerDebug2("Debug2", "LogTest");
        writeLoggerDebug3("Debug3", "LogTest");
        writeLogger("Generell", "LogTest", Level.ALL);
    }
}
