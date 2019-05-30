package de.eagle.gepard.parser;

import de.eagle.util.datatypes.Settings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

import static de.eagle.lillyjakeframework.core.Definitions.EXPECTED_BLOCKSIZE;
import static de.eagle.lillyjakeframework.logging.JakeLogger.getLogger;

/**
 * Der GePard Parser
 * <p>
 * Konzept: erkennt Gruppen mithilfe von "BEGIN <NAME>" und "END" Der
 * Gruppenname gibt an, was generiert werden soll. Beispiel: "BEGIN BOXMODE" zum
 * Erstellen eines neuen Boxmodis Die verschiedenen Gruppen können ihre eigenen
 * Parameter definieren und fordern. Der Generator_Parser arbeitet hierbei
 * ähnlich wie der Configurator indem er ein Datenpaket erhält welches er zu
 * füllen versucht. Wie dann dieses Datenpaket zu interpretieren ist obliegt dem
 * initiator. So müsste es allerdings sehr einfach möglich sein dem Generator
 * listen für alle BOXMODE - Gruppen usw. zu Übergeben, sodass er diese dann
 * füllt und Jake dann entsprechend damit darbieten kann!
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @see Tokenizer
 * @see Configurator
 *      <p>
 *      For testing see: Tests > java > GepardTest
 * @since 1.0.10
 */
public class GeneratorParser {

    /// Alle Pfade in denen nach Konfigurationen gesucht werden soll
    private String[] _op_paths;

    /**
     * Konstruiert den Generator Parser (GePard)
     *
     * @param filenames Die mit ':' getrennten Dateinamen der zugrunde liegenden Datei
     */
    public GeneratorParser(String filenames) {
        this._op_paths = filenames.split(":");
    }


    /**
     * Repräsentiert ein Objekt des Gepards-Parsers
     * <p>
     * Dieses Objekt enthält lediglich name und eine zugehörige Sammlung an
     * Einstellungen
     */
    public static class JObject {
        /// Konfigurationen
        public Settings config;
        /// Name des Objekts
        private String name = "";

        /**
         * Konstruiert eine neues JObject
         * @param name Der Name des JObjects
         * @param config Die zugehörige Konfiguration
         */
        public JObject(String name, Settings config) {
            this.name = name;
            this.config = config;
        }

        /**
         * @return Liefert den Namen der Box
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * Eine mit BEGIN X und END abgegrenzte Box
     */
    public static class Box {
        /// Name der Box
        String name = "";
        /// unbearbeiteter Boxinhalt
        String content = "";
        /**
         * Generiert ein jObject auf Basis des Configurators
         *
         * @param blueprint die Einstellungen auf derer Basis das Objekt erstellt werden soll
         * @param add_unknown sollen unbekannte hinzugefügt werden? siehe: @ref Configurator
         *
         * @return ein entsprechendes jObject;
         */
        JObject get_JObject(Settings blueprint, boolean add_unknown) throws IOException {
            Settings newSettings = blueprint.cloneSettings();
            InputStream stringStream = new ByteArrayInputStream(this.content.getBytes(StandardCharsets.UTF_8));
            Configurator cfg = new Configurator(stringStream);
            cfg.parse_settings(newSettings, add_unknown);
            return new JObject(this.name, newSettings);
        }
    }


    /**
     * Extrahiert alle 'identifier'-Definitionen aus einer Datei
     *
     * @param identifier der Name der Blöcke die Analysiert werden sollen
     * @param blueprint die Zugrunde liegenden Einstellungen - es ist nicht erlaubt unbekannte hinzu zu fügen
     * @param add_unknown sollen unbekannte hinzugefügt werden? siehe: @ref Configurator
     *
     * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde
     *
     * @return Alle gefundenen Objekte. Wird keins Gefunden so wird ein leerer Vektor zurück geben
     */
    JObject[] parseFile(String identifier, Settings blueprint, boolean add_unknown) throws FileNotFoundException {
        Box current_box;
        LinkedList<JObject> jobjects;
        for(String path : this._op_paths) {
            BufferedReader dataFile = new BufferedReader(new FileReader(path));
            getLogger().info("GenPar: GeneratorParser bearbeitet nun die Datei: \"" + path + "\"");
            for(;;) {
                current_box = get_next_box(dataFile, identifier, EXPECTED_BLOCKSIZE);
            }
        }
        return null;
    }

    /**
     * liefert die nächste BEGIN X END Klausel wirft einen Fehler wenn Datei Fehlerhaft
     *
     * @param bufferedReader Der BufferedReader auf dem gearbeitet werden soll. Es liefert danach eine Zeile nach END
     * @param name Wenn nicht ="" wird die erste Box mit dem entsprechenden Namen geliefert und "" wenn es keine mehr gibt
     * @param bufs die zu erwartende Block Größe - Muss normalerweise nicht verändert werden
     *
     * @note diese Funktion füttert Parse File und sollte sonst nicht verwendet werden
     *
     * @returns den unveränderten String zwischen BEGIN X und END
     */
    protected Box get_next_box(BufferedReader bufferedReader, String name, int bufs) {
        return null;
    }

}

/* TODO MERGE:
GeneratorParser::Box GeneratorParser::get_next_box(std::istream& inp, const std::string& name, size_t bufs) {
    // Check if inp is valid:
    if(!inp.good() || inp.eof()) throw std::runtime_error("Inputstream in get_next_box failed (Datei nonexistent?) use -debug for more info");

    std::string buffer; buffer.reserve(bufs);

    std::string current_line;

    const std::regex p_begin = std::regex(R"(^ *(BEGIN) *([a-zA-Z-äöüßÄÖÜ]+))");
    const std::regex p_end   = std::regex(R"(^ *(END))");

    GeneratorParser::Box ret_box;

    //    if(std::regex_search(line, matches, this->_pattern)){
    std::match_results<std::string::const_iterator> c_match;
    while(std::getline(inp, current_line)){
        //w_debug("parsing: " + current_line);
        current_line = Tokenizer::erase_skipper(current_line);
        if(std::regex_search(current_line, c_match, p_begin)) { // match BEGIN X
            if (name == "" || c_match[BOX_NAME] == name) { // FOUND A BOX
                ret_box = GeneratorParser::Box{c_match[BOX_NAME]};
                //search throuh end;
                while(std::getline(inp, current_line)){
                    current_line = Tokenizer::erase_skipper(current_line);
                    if(std::regex_search(current_line, c_match, p_begin)) // match BEGIN X
                        throw std::runtime_error("\"BEGIN " + ret_box.name + "\" enthält ein weiteres BEGIN, das ist nicht erlaubt");
                    if(std::regex_search(current_line, c_match, p_end))  // found end :D
                        return ret_box;
                    ret_box.content += current_line + "\n";
                }
                throw std::runtime_error("\"BEGIN " + ret_box.name + "\" hat kein passendes END!");
                // Gibt nur einen Fehler aus, wenn er im Zusammenhang mit einer gesuchten Box besteht
            } // found no box
        }
    }
    return {.name = ""}; // Da ist nichts
}
 */