package de.eagle.util.blueprints;

import de.eagle.gepard.parser.TokenMatch;
import de.eagle.gepard.parser.Tokenizer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import static de.eagle.util.io.JakeLogger.writeLoggerWarning;

/**
 * Allgemeiner Übersetzer, der aus einer Datei seine Daten bezieht. Er erlaubt
 * es Daten mit einem internen Bezeichner zu versehen, welche dynamisch und oder
 * nach Wunsch verwendet werden können. Wird vermutlich als String -> String
 * Mapper verwendet
 *
 * @param <K> Der Datentyp des Schlüssels
 * @param <V> Der Datentyp des Werts
 *
 * @author Florian Sihler
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class Translator<K, V> {
    /* ======== Regex ======= */
    /// Der komplette Regex-Treffer
    private static final int ALL = 0;
    /// Left hand side der Operation (XXXX = )
    private static final int LHS = 1;
    /// Operation ( X=X )
    private static final int OPT = 2;
    /// Right hand side der Operation ( = XXXX)
    private static final int RHS = 3;

    /**
     * Die Intern verwaltete Karte
     */
    protected HashMap<K, V> map = new HashMap<>();

    /**
     * Der Name des Translators
     */
    protected String name;

    /**
     * Konstruiert einen Translator auf Basis eines Namens
     * 
     * @param name Der Name des Translators
     */
    public Translator(String name) {
        this.name = name;
    }

    /**
     * Soll eine eingelesene Zeichenkette in den entsprechenden Schlüssel verwandeln
     *
     * @see #prepareData(String)
     *
     * @param key Der Key, enthält keine anführenden oder endständigen Leerfelder
     *            oder Kommentare
     * @return der entsprechende Key
     */
    protected abstract K stringToKey(String key);

    /**
     * Soll eine eingelesene Zeichenkette in den entsprechenden Wert verwandeln
     *
     * @see #prepareData(String)
     *
     * @param value Der Wert, enthält keine anführenden oder endständigen Leerfelder
     *              oder Kommentare
     * @return das entsprechende Value
     */
    protected abstract V stringToValue(String value);

    // =============== Implementation

    /**
     * Bereitet eine Zeile für das Konvertieren vor
     * 
     * @see #stringToKey(String)
     * @see #stringToValue(String)
     *
     * @implNote Da diese Zeile über einen {@link Tokenizer#get()} geladen wird sind
     *           Kommentare und Tabs bereits entfernt!
     *
     * @param data Die entsprechende Zeile
     * @return die Verarbeitete Zeile
     */
    public static String prepareData(String data) {
        return data.trim();
    }

    /**
     * Besserer Name für {@link #getValue(Object)}
     * 
     * @param key der zu mappende Key
     * @return das entsprechende Value
     */
    public V translate(K key) {
        return getValue(key);
    }

    /**
     * @return {@link HashMap#get(Object)}
     */
    public V getValue(K key) {
        return map.get(key);
    }

    /**
     * @return {@link HashMap#containsKey(Object)}
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    /**
     * @return {@link HashMap#containsValue(Object)}
     */
    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    /**
     * Liest die entsprechenden Daten aus einer Datei ein. Es obliegt dem Erben ob
     * er diese Methode öffentlich macht
     *
     * @implNote Bisher wurde kein scheitern implementiert
     *
     * @param path der Pfad zur Datei
     * @return false im Falle eines Fehlers, der das Weiterarbeiten unmöglich macht
     * @throws FileNotFoundException Wenn die Datei nicht gefunden wurde
     */
    protected boolean loadData(String path) throws FileNotFoundException {
        return loadData(new FileInputStream(path));
    }

    /**
     * Liest die entsprechenden Daten aus einer Datei ein
     *
     *
     * @param input Stream zu der Datei, aus der die Einstellungen gelesen werden
     * @return true, wenn das Lesen der Daten fertiggestellt ist
     *
     * @implNote Struktur der Eigenschaften: Attribut = Wert
     * @implNote Capture Groups(noch nicht implementiert): ALL = "Attribut = Wert"
     *           LHS = "Attribut" OPT = "=" RHS = "Wert"
     */
    protected boolean loadData(InputStream input) throws FileNotFoundException {

        // Konstruiert einen Tokenizer aus dem Input Stream und einem Pattern dass der
        // Syntax entspricht
        Tokenizer tknz = new Tokenizer(input,
                Pattern.compile("^ *([a-zA-Z0-9_\\-äöüßÄÖÜ]+) *(=) *([a-zA-Z0-9_\\-äöüÄÖÜß ]+)$", Pattern.MULTILINE));
        // Iteriert über jeden TokenMatch des InputStreams des Tokenizer
        for (TokenMatch m : tknz) {

            // Überprüft ob der TokenMatch valide und nicht leer ist
            if (m.failure())
                continue;
            // Speichert alle Matchings ab
            String[] g = m.getMatchings();
            // Überprüft ob die Zeile fehlerhaft ist (Falls nicht alle Capture Groups
            // gepasst haben, oder die 2. Capture Group nahelegt, dass es keine Operation
            // ist)
            if (g.length != 4 || !g[OPT].equals("=")) {
                // Schreibt eine Fehlermeldung in den Log und beginnt den nächsten
                // Schleifendurchlauf
                writeLoggerWarning("Die Zeile: \"" + m.getOriginal() + "\" ist fehlerhaft und wird ignoriert!",
                        "Translator");
                continue;
            }

            // Erstellt einen Key auf Basis der Linken Seite der Operation
            K key = stringToKey(prepareData(g[LHS]));
            // Überprüft ob diese Einstellung bereits zuvor gesetzt wurde, überschreibt sie,
            // falls nötig
            if (this.map.containsKey(key)) {
                writeLoggerWarning(
                        "Die Einstellung: " + prepareData(g[LHS]) + " wurde bereits gesetzt und wird überschrieben!",
                        "Translator");
            }
            this.map.put(key, stringToValue(prepareData(g[RHS])));
        }
        return true;
    }

    /**
     *
     * @param o Objekt, dass mit dem Translator verglichen werden soll
     * @return false, falls o kein Translator ist oder die beiden map oder die
     *         beiden Namen nicht übereinstimmen true, falls sie identisch sind
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Translator))
            return false;
        Translator<?, ?> that = (Translator<?, ?>) o;
        return Objects.equals(map, that.map) && Objects.equals(name, that.name);
    }

    /**
     *
     * @return int, der aus map und name gehasht wurde
     */
    @Override
    public int hashCode() {
        return Objects.hash(map, name);
    }

    /**
     *
     * @return String String-Repräsentation des Translators
     */
    @Override
    public String toString() {
        return "Translator{" + "map=" + map + ", name='" + name + '\'' + '}';
    }
}
