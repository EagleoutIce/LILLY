package Jake.Compiler;
import org.junit.jupiter.api.Tag;

import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.util.helper.PropertiesProvider;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.*;

@Tag("JakeCompiler")
public class JakeCompilerTest {

    @Test @Order(1)
    @DisplayName("[Compiler] Teste Dummy-Datei Kreierung")
    void _test_dummy_file_creation() throws IOException {
        JakeCompile.generateDummyFile(PropertiesProvider.getTempPath() + "/TEST-Testfile.tex"); //
        Assertions.assertTrue(new File(PropertiesProvider.getTempPath() + "/TEST-Testfile.tex").canRead(),"Die Datei sollte existieren :D");
        // more tests
    }


}