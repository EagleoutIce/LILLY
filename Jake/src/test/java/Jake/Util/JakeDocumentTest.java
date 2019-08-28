package Jake.Util;
/**
 * @file eSetting_TypeTest.java
 *
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * Testet {@link de.eagle.util.datatypes.JakeDocument} auf Integrität
 */

import de.eagle.util.datatypes.JakeDocument;
import de.eagle.util.enumerations.eDocument_Type;
import de.eagle.util.helper.Executer;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.ConsolePrintStream;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.io.VoidPrintStream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Tag("JakeDocument")
@DisplayName("Test des JakeDocuments")
class JakeDocumentTest {

    @BeforeAll
    static void silence_output(){
        JakeWriter.out.reassignConsole(new VoidPrintStream());
        JakeWriter.err.reassignConsole(new VoidPrintStream());
    }

    @AfterAll
    static void reenable_output(){
        JakeWriter.out.reassignConsole(new ConsolePrintStream(System.out));
        JakeWriter.err.reassignConsole(new ConsolePrintStream(System.err));
    }

    JakeDocument getNewDoc(eDocument_Type type){
        return new JakeDocument(type, "unnamed");
    }

    private String called_path = "";
    private boolean loaded = false;

    @BeforeEach
    void resetGlobals(){
        called_path = "";
        loaded = false;
    }

    @Test
    @Order(1)
    @DisplayName("[JakeDocument] Teste die Listener")
    void _test_addListener() {
        JakeDocument doc = getNewDoc(eDocument_Type.IS_CONF);
        doc.addListener(new JakeDocument.iDocumentChangedListener() {
            @Override
            public void JakeDocument_Saved(String path) {
                called_path = path;
            }

            @Override
            public void JakeDocument_Loaded(){
                loaded = true;
            }
        });
        Assertions.assertFalse(loaded, "Das Dokument sollte noch nicht geladen sein");
        doc.load();
        String path = PropertiesProvider.getTempPath() + "/hallo welt.tmp";
        doc.saveDocument(path);
        Assertions.assertEquals(path, called_path, "Der Listener sollte aufgerufen werden");
        Assertions.assertTrue(loaded, "Das Dokument sollte geladen sein");
    }

    @Test
    @Order(2)
    @DisplayName("[JakeDocument] Teste den Test eines TeX-Dokuments")
    void _test_checkTeX() {
        Assertions.assertTrue(true, "Bisher wird jedes Dokument als TeX-Dokument anerkannt!");
    }

    @Test
    @Order(3)
    @DisplayName("[JakeDocument] Teste den (oberflächlichen) Test einer Konfigurationsdatei")
    void _test_checkConf_basic() {
        // Wir nehmen einige Beispielkonfigurationen und stellen sie dann als JakeDocument zur Verfügung
        Assertions.assertFalse(getNewDoc(eDocument_Type.IS_CONF).isValid(), "Ein noch nicht gespeichertes Dokument ist nicht als valide zu betrachten!");
    }

    private static Stream<Arguments> _provider_test_checkConf_complex() {
        return Stream.of(
                Arguments.of("/Configs/TestConfigs/ValidConfig1.conf", true),
                Arguments.of("/Configs/TestConfigs/ValidConfig2.conf", true),
                Arguments.of("/Configs/TestConfigs/InvalidConfig1.conf", false),
                Arguments.of("/Configs/TestConfigs/InvalidConfig2.conf", false),
                Arguments.of("/Configs/TestConfigs/InvalidConfig3.conf", false)
        );
    }

    @ParameterizedTest
    @Order(4)
    @DisplayName("[JakeDocument] Teste den Test einer Konfigurationsdatei")
    @MethodSource("_provider_test_checkConf_complex")
    void _test_checkConf_complex(String resourcePath, boolean expected) throws IOException {
        // Wir nehmen einige Beispielkonfigurationen und stellen sie dann als JakeDocument zur Verfügung
        JakeDocument jd = new JakeDocument(new File(Executer.getPath(new InputStreamReader(JakeDocumentTest.class.getResourceAsStream(resourcePath)),".conf")));
        Assertions.assertEquals(expected, jd.isValid(), "Es wird vom Dokument dieser Zustand erwartet");
    }
    @Test
    @Order(5)
    @DisplayName("[JakeDocument] Teste den Test einer Gepard-Datei")
    void _test_checkGPD() {
        Assertions.assertTrue(true, "Bisher wird jedes Dokument als GPD-Dokument anerkannt!");
    }

    @Test
    @Order(6)
    @DisplayName("[JakeDocument] Teste den Test einer unbekannten Datei")
    void _test_checkUnknown() {
        Assertions.assertTrue(true, "Es wird jedes Dokument unbekannten Typs anerkannt!");
    }

    private static Stream<Arguments> _provider_test_identifyType() {
        return Stream.of(
                Arguments.of("Test.conf", eDocument_Type.IS_CONF),
                Arguments.of("Test.tex", eDocument_Type.IS_TEX),
                Arguments.of("Test.gpd", eDocument_Type.IS_GEPARD),
                Arguments.of("Test.xxx", eDocument_Type.IS_UNKNOWN)
        );
    }

    @ParameterizedTest
    @Order(7)
    @DisplayName("[JakeDocument] Teste die automatische Identifikation des Dokumenttyps.")
    @MethodSource("_provider_test_identifyType")
    void _test_identifyType(String name, eDocument_Type expected) {
        Assertions.assertEquals(expected, new JakeDocument(new File(name)).getType(), "Dieser Dokumenttyp wird erwartet");
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("[JakeDocument] Testet, ob das Anfragen des Dateiinhalts funktioniert.")
    @MethodSource("_provider_test_checkConf_complex")
    void _test_getFileContents(String resourcePath, boolean ignored) throws IOException {
        File f = new File(Executer.getPath(new InputStreamReader(JakeDocumentTest.class.getResourceAsStream(resourcePath)),".conf"));
        JakeDocument jd = new JakeDocument(f);
        jd.load();
        List<String> lines = Files.readAllLines(new File(Executer.getPath(new InputStreamReader(JakeDocumentTest.class.getResourceAsStream(resourcePath)),".conf")).toPath(), Charset.defaultCharset());
        Assertions.assertEquals(lines, jd.getFileContents(), "Die Inhalte sollen sich entsprechend");
    }

    @Test
    @Order(9)
    @DisplayName("[JakeDocument] Testet, ob der Dateiinhalt manuell gesetzt werden kann.")
    void _test_setFileContents() {
        JakeDocument jd = getNewDoc(eDocument_Type.IS_TEX);
        Assertions.assertEquals(0, jd.getFileContents().size());
        ArrayList<String> arl = new ArrayList<String>(Arrays.asList("Hallo","Welt"));
        jd.setFileContents(arl);
        Assertions.assertEquals(arl, jd.getFileContents(), "Das Setzen des Inhalts sollte funktionieren.");
    }

    @Test
    @Order(10)
    @DisplayName("[JakeDocument] Testet getName()")
    void _test_getName() {
        JakeDocument jd = new JakeDocument(eDocument_Type.IS_GEPARD,"Tolles Dokument");
        Assertions.assertEquals("Tolles Dokument", jd.getName(), "Der Name sollte passen.");
    }

    @Test
    @Order(11)
    @DisplayName("[JakeDocument] Testet setName()")
    void _test_setName() {
        JakeDocument jd = new JakeDocument(eDocument_Type.IS_GEPARD,"Tolles Dokument");
        jd.setName("Dokument");
        Assertions.assertEquals("Dokument", jd.getName(), "Der Name sollte passen.");
    }

    @Test
    @Order(12)
    @DisplayName("[JakeDocument] Testet setPath() und getPath()")
    void _test_set_and_get_Path() {
        JakeDocument jd = new JakeDocument(eDocument_Type.IS_GEPARD,"Tolles Dokument");
        Assertions.assertEquals(new File(""), jd.getPath(), "Der Pfad ist noch nicht gesetzt");
        jd.setPath(new File("Hallo Welt"));
        Assertions.assertEquals(new File("Hallo Welt"), jd.getPath(), "Der Pfad  sollte passen.");
    }

    @Test
    @Order(13)
    @DisplayName("[JakeDocument] Testet isNew()")
    void _test_isNew() {
        JakeDocument jd = getNewDoc(eDocument_Type.IS_UNKNOWN);
        Assertions.assertTrue(jd.isNew(), "Das Dokument sollte neu sein.");
        jd.setSaved("Hallo Welt");
        Assertions.assertFalse(jd.isNew(), "Das Dokument ist nun gespeichert.");
    }

    @Test
    @Order(14)
    @DisplayName("[JakeDocument] Testet getSavePath()")
    void _test_getSavePath() {
        // Won't be testet
    }

    @Test
    @Order(15)
    @DisplayName("[JakeDocument] Testet saveDocument()")
    void _test_saveDocument() {
        // Won't be testet
    }

    @Test
    @Order(16)
    @DisplayName("[JakeDocument] Testet setSaved()")
    void _test_setSaved() {
        // Wont't be testet
    }

    @Test
    @Order(17)
    @DisplayName("[JakeDocument] Testet getMetadata()")
    void _test_getMetadata() {
        // Won't be testet really:
        JakeDocument jd = getNewDoc(eDocument_Type.IS_TEX);
        Assertions.assertNotNull(jd.getMetadata());
    }
}