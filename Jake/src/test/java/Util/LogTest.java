package Util;
/**
 * @file Util.LogTest.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Überprüft die Funktionalität des Loggers
 *
 * @see JakeLogger
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static de.eagle.util.io.JakeLogger.*;

import java.util.logging.Level;

@Tag("Meta")
@DisplayName("Test der Log-Funktionalität")
class LogTest {
    @Test
    @DisplayName("[Log] Teste Log-Zugriffe (wird nicht wirklich getestet^^)")
    void _test_logger() {
        writeLoggerInfo("Info", "Util.LogTest");
        writeLoggerWarning("Warning", "Util.LogTest");
        writeLoggerError("Error", "Util.LogTest");
        writeLoggerDebug1("Debug1", "Util.LogTest");
        writeLoggerDebug2("Debug2", "Util.LogTest");
        writeLoggerDebug3("Debug3", "Util.LogTest");
        writeLogger("Generell", "Util.LogTest", Level.ALL);
    }
}
