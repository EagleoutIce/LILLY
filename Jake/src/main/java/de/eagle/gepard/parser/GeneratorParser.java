package de.eagle.gepard.parser;
/**
 * @file GeneratorParser.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Die Grundlage von GePard - wird von allen weiteren BoxTypen erweitert
 *
 * TODO: BOXTYPEN :D
 *
 * @see Configurator
 * @see Tokenizer
 */

import de.eagle.util.datatypes.Settings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eagle.lillyjakeframework.core.CoreSettings;

import static de.eagle.util.io.JakeLogger.*;

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
 * @version 2.0.0
 * @since 2.0.0
 *
 * @implNote GePard an sich befindet sich bezüglich seiner Funktionalität noch
 *           im Aufbau. Änderungen in der Philosophie sind ebenso möglich, wie
 *           Änderungen in den Akzeptierten Begriffen und Patterns. Etwaige
 *           große Änderungen bezüglich vorhergehender Version werde jeweils
 *           vermerkt.
 *
 * @see Tokenizer
 * @see Configurator
 *      <p>
 *
 *
 *      For testing see: Tests > java > GepardTest
 *
 */
public class GeneratorParser {

    /// Alle Pfade in denen nach Konfigurationen gesucht werden soll
    private String[] _op_paths;
    /// Entspricht der Zeilenanzahl (fürs debugging)
    private int __lineCount;

    /**
     * Konstruiert den Generator Parser (GePard)
     *
     * @param filenames Die mit ':' getrennten Dateinamen der zugrunde liegenden
     *                  Datei
     */
    public GeneratorParser(String filenames) {
        this._op_paths = filenames.split(":");
        this.__lineCount = 0;
    }

    /**
     * @return the lineCount
     */
    public int getLineCount() {
        return __lineCount;
    }

    /**
     * Setzt den Zeilenzähler zurück
     *
     * @return den alten Wert des Zeilenzählers
     */
    public int resetLineCount() {
        int old = __lineCount;
        __lineCount = 0;
        return old;
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
         *
         * @param name   Der Name des JObjects
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

        @Override
        public String toString() {
            return "JObject [config=" + config + ", name=" + name + "]";
        }
    }

    /**
     * Eine mit BEGIN X und END abgegrenzte Box
     */
    public static class Box {
        /// Name der Box
        protected String name = "";
        /// unbearbeiteter Boxinhalt
        protected String content = "";

        // Datei in der sich die Box befindet
        protected String file;

        // Zeile an der sich das BEGIN-Tag der Box befindet
        protected int line = 0;

        /// Expliziter leerer Konstruktor
        public Box() {
        }

        /**
         * Konstruiert die Box entsprechend mit Namen und Inhalt
         *
         * @param name        Name der Box :D
         * @param fileName    Herkunft der Box
         * @param lineOfBegin Zeile des BEGIN-Tags
         */
        public Box(String name, String fileName, int lineOfBegin) {
            this.name = name;
            this.line = lineOfBegin;
        }

        /**
         * Konstruiert die Box entsprechend mit Namen und Inhalt
         *
         * @param name        Name der Box :D
         * @param content     Inhalt der Box (Magie!!!!!)
         * @param fileName    Herkunft der Box
         * @param lineOfBegin Zeile des BEGIN-Tags
         */
        public Box(String name, String content, String fileName, int lineOfBegin) {
            this.name = name;
            this.content = content;
            this.file = fileName;
            this.line = lineOfBegin;
        }

        /**
         * Generiert ein jObject auf Basis des Configurators
         *
         * @param blueprint   die Einstellungen auf derer Basis das Objekt erstellt
         *                    werden soll
         * @param add_unknown sollen unbekannte hinzugefügt werden? siehe: @ref
         *                    Configurator
         *
         * @return ein entsprechendes jObject;
         */
        JObject get_JObject(Settings blueprint, boolean add_unknown) throws IOException {
            writeLoggerDebug3("Generiere ein neues JObject auf Basis einer Box", "Box");
            Settings newSettings = blueprint.cloneSettings();
            InputStream stringStream = new ByteArrayInputStream(this.content.getBytes(StandardCharsets.UTF_8));
            Configurator cfg = new Configurator(stringStream);
            cfg.parse_settings(newSettings, add_unknown);
            writeLoggerDebug3("Generiere ein neues JObject auf Basis einer Box... abgeschlossen", "Box");
            return new JObject(this.name, newSettings);
        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }

        public String getFile() {
            return file;
        }

        public int getLine() {
            return line;
        }
    }

    /**
     * Extrahiert alle 'identifier'-Definitionen aus einer Datei
     *
     * @param identifier  der Name der Blöcke die Analysiert werden sollen
     * @param blueprint   die Zugrunde liegenden Einstellungen - es ist nicht
     *                    erlaubt unbekannte hinzu zu fügen
     * @param add_unknown sollen unbekannte hinzugefügt werden? siehe: @ref
     *                    Configurator
     *
     * @return Alle gefundenen Objekte. Wird keins Gefunden so wird ein leerer
     *         Vektor zurück geben
     *
     * @throws IOException Im Falle eines Fehlers beim Verarbeiten des Streams
     */
    public JObject[] parseFile(String identifier, Settings blueprint, boolean add_unknown) throws IOException {
        Box current_box;
        LinkedList<JObject> jobjects = new LinkedList<JObject>();
        for (String path : this._op_paths) {
            BufferedReader dataFile = new BufferedReader(new FileReader(path));
            writeLoggerInfo("GeneratorParser bearbeitet nun die Datei: \"" + path + "\"", "GePard");
            resetLineCount();
            for (;;) {
                current_box = get_next_box(dataFile, identifier, path /* file path */);
                if (current_box.name.isEmpty()) {
                    writeLoggerDebug1("Keine weitere Box gefunden", "GePard");
                    break;
                }
                jobjects.push(current_box.get_JObject(blueprint, add_unknown));
                writeLoggerDebug1("Box gefunden: " + jobjects.getFirst(), "GePard");
            }
        }
        writeLoggerInfo("Bearbeiten aller Dateien abgeschlossen", "GePard");
        return jobjects.toArray(new JObject[0]);
    }

    /**
     * Liest eine Zeile ein und erhöht die aktuelle LineCount
     *
     * @param reader Der Reader von dem gelesen werden soll
     * @return die gelesene Zeile, 'null' im Falle eines Fehlers
     */
    private String getLine(BufferedReader reader) {
        try {
            String bufferLine = reader.readLine();
            __lineCount++; // Inkrementeirt den Zähler nur, wenn das Lesen funktioniert
            return bufferLine;
        } catch (IOException e) {
            writeLoggerError(e.getMessage(), "GePard");
            return null; // Im Falle eines Fehlers => null
        }
    }

    /**
     * liefert die nächste BEGIN X END Klausel wirft einen Fehler wenn Datei
     * Fehlerhaft
     *
     * @param bufferedReader Der BufferedReader auf dem gearbeitet werden soll. Es
     *                       liefert danach eine Zeile nach END
     *
     * @param filterName     Wenn nicht ="" wird die erste Box mit dem
     *                       entsprechenden Namen geliefert und "" wenn es keine
     *                       mehr gibt. Der Filter wird als REGEX präsentiert dies
     *                       ist bei der Verwendung von (ohnehin verbotenen)
     *                       Kontrollsequenzen zu beachten!
     *
     * @param fileName       Der Name der Datei, aus der der bufferedReader seine
     *                       Daten bezieht, auch diese Option hat lediglich aus
     *                       Debug-Gründen Einzug erhalten. Namen wie "StringStream"
     *                       oder einfach nichts sind ebenfalls möglich. Die Datei
     *                       wird niemals nachverfolgt.
     *
     * @note bufs (size_t) wurde entfernt, da das tolle Java keine gute Möglichkeit
     *       für eine derartige Implementation zur Verfügung stellt!
     *
     * @throws IOException Wirft die Ausnahme, wenn der Reader beschädigt ist.
     *
     * @note diese Funktion füttert parse_file und sollte sonst nicht verwendet
     *       werden
     *
     * @return den unveränderten String zwischen BEGIN X und END
     */
    protected Box get_next_box(BufferedReader bufferedReader, String filterName, String fileName) throws IOException {

        String buffer = ""; // todo reserve

        String current_line = "";

        /*
         * Notiz zur Gestaltung der Umgebungsbeschränker (tolles deutsches Wort ^^):
         *
         * Es wurde absichtlich nur der Beginn der Zeile und _nicht_ das Ende gematcht
         * Eine Deklaration wie: - BEGIN boxname: Ist also ebenso valide wie: - BEGIN
         * boxname Ich bin auch noch da in dieser Zeile Analog ergibt sich für END: -
         * END; - END Hallo MAMA Als gültiger Terminator - Dies kann negativ betrachtet
         * werden, eröffnet aber zukünftige Möglichkeiten für weiteren Optionen und oder
         * Informationen....
         */

        Pattern p_begin = Pattern.compile("^ *(BEGIN) *(?<boxname>[a-zA-ZäöüßÄÖÜ_]+)", Pattern.MULTILINE);
        Pattern p_end = Pattern.compile("^ *(END)", Pattern.MULTILINE);
        Pattern p_boxname = Pattern.compile(filterName, Pattern.MULTILINE);

        boolean skipping = false;

        StringBuilder box_content_buffer = new StringBuilder();

        Box return_box = null;
        Matcher current_matcher = null;
        while ((current_line = getLine(bufferedReader)) != null) {
            writeLoggerDebug2("[Zeile: " + getLineCount() + "] GePard parsing: \"" + current_line + "\"", "GePard");
            current_line = current_line.replaceAll(CoreSettings.requestValue("S_COMMENT_PATTERN"), ""); // remove comments => Method?
            if ((current_matcher = p_begin.matcher(current_line)).find()) {
                writeLoggerDebug1("Box mit dem Name: \"" + current_matcher.group("boxname") + "\" detektiert!",
                        "GePard");
                // Wird die aktuelle Box gesucht? GePard erlaubt es eine Datei
                // Nur nach bestimmten Boxtypen zu durchfiltern, er wird die Boxen
                // dennoch mit dem entsprechenden Namenbezeichner versehen,
                // allerdings so eine gefilterte Sammlung gewährleisten.
                // Ist die Sammlung aller Boxen gewünscht so lässt sich der Filter
                // einfach auf einen leeren String stellen. Da der Match weiter
                // als regex::match durchgeführt wird ist es weiter möglich
                // die suche auch über eine RegexExpression durchzuführen.
                // Aus Andacht an CPP-Jake wird allerdings kein Box-Provider selbst
                // darauf zugreifen, die Option wird allerdings dennoch präsentiert,
                // weil, naja, irgendwas muss ja die Java-Version können (neben der
                // unabsprechbaren Fertigkeit mich zum Weinen zu bringen.)
                if (!p_boxname.matcher(current_matcher.group("boxname")).matches()
                        && !filterName.isEmpty() /* explicit */ ) {
                    writeLoggerDebug1(
                            "Die Box wird übersprungen, da sie nicht dem Filter (\"" + filterName + "\") entspricht",
                            "GePard");
                    // Skipping
                    skipping = true;
                    continue; // Diese Box wollen wir nicht
                }
                return_box = new Box(current_matcher.group("boxname"), "", fileName, getLineCount());
                // Fülle content:
                while ((current_line = getLine(bufferedReader)) != null) {
                    writeLoggerDebug2("[Zeile: " + getLineCount() + "] [BOX: \"" + return_box.getName()
                            + "\"] GePard parsing: \"" + current_line + "\"", "GePard");
                    current_line = current_line.replaceAll(CoreSettings.requestValue("S_COMMENT_PATTERN"), "");

                    // Ist hier ein verschachtelter Gruppen beginn?
                    // Dies soll Gepard bewusst nicht unterstützen,
                    // Die verarbeitung interner Gruppenbezeichnungen obliegt lediglich
                    // des jeweiligen Boxtyps GePard macht in der Hinsicht nichts,
                    // außer mimimi
                    if ((current_matcher = p_begin.matcher(current_line)).find()) {
                        throw new RuntimeException("[Zeile: " + getLineCount() + "] Box: " + return_box.name
                                + " enthält ein weiteres BEGIN (\"" + current_line
                                + "\" dies ist in Gepard explizit nicht gestattet !");
                    } else if ((current_matcher = p_end.matcher(current_line)).find()) {
                        // In diesem Fall wurde ein END gefunden, dies wird selbstredend
                        // nur dann gestattet, wenn es sich auch um eine gültige BEGIN BOX
                        // ein fehlplaziertes END wird außerhalb abgegrast

                        // Setzen des korrekten Inhalts:
                        return_box.content = box_content_buffer.toString();
                        writeLoggerDebug1("Beende Box", "GePard");
                        return return_box;
                    }
                    // Wenn wir hier sind handelt es sich um eine normale Zeile
                    // Sie wird normal, inklusive der neuen Zeile hinzugefügt,
                    // Die Information über die Ziele verfällt die Zeilennummer des
                    // Begin-Tags für etwaige Debugging-Zwecke wird oben bereits
                    // aufgesammelt
                    writeLoggerDebug2("Inhalt (\"" + current_line.trim() + "\") wurde der Box hinzugefügt.", "GePard");
                    box_content_buffer.append(current_line).append("\n");

                }
            } else if (p_end.matcher(current_line).find()) {
                // Es ist nicht erlaubt ein herrenloses END zu erhalten!
                if (skipping) {
                    skipping = false;
                    writeLoggerDebug2("[Zeile: " + getLineCount() + "] Box-Wurde übersprungen", "GePard");
                } else {
                    throw new RuntimeException("[Zeile: " + getLineCount()
                            + "] Es wurde ein END gefunden welches herrenlos in der Gegend umher schlawinert! Dies ist nicht gestattet!");
                }
            }
        }
        writeLoggerWarning("Liefere eine leere Box zurück, da keine weitere gefunden wurde!", "GePard");
        return new Box(); // return {.name = ""} // Da ist nichts; ein wahrer Schocker :D
    }

} // End Class :D
