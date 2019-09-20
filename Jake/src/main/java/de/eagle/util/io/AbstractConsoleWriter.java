package de.eagle.util.io;

/**
 * @file AbstractConsoleWriter.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Grundanforderungen an einen "Stream" über den Daten analog zu einem {@link java.io.PrintStream} ausgegeben werden können.
 */


/**
 * @class AbstractConsoleWriter
 *
 * Grundlegende Anforderungen an einen ConsoleWriter
 *
 * @see ConsolePrintStream
 * @see TextAreaConsoleStream
 * @see VoidPrintStream
 *
 * @version 1.0.0
 * @since 2.0.0
 *
 */
public abstract class AbstractConsoleWriter {

    /**
     * Leerer Konstruktor
     */
    public AbstractConsoleWriter(){}


    /**
     * @return Start einer neuen Zeile
     */
    public abstract AbstractConsoleWriter println();

    /**
     * Gibt eine neue Zeile inklusive von s aus.
     *
     * @param s String s
     * @return Instanz des Writers
     */
    public abstract AbstractConsoleWriter println(String s);

    /**
     * Fügt der aktuellen Zeile s an
     *
     * @param s String s
     * @return Instanz des Writers
     */
    public abstract AbstractConsoleWriter print(String s);

    /**
     * Aktualisiert die Ausgabe
     *
     * @return Instanz des Writers
     */
    public abstract AbstractConsoleWriter flush();

    /**
     * Analog zu {@link #print(String)}
     *
     * @param s String s
     * @return Instanz des Writers
     */
    public abstract AbstractConsoleWriter append(String s);

    /**
     * Formtiert die eingabe auf Basis der Objekte
     *
     * @param format Formatierungsstring
     * @param args Argumente
     * @return Instanz des Writers
     */
    public abstract AbstractConsoleWriter format(String format, Object... args);
}
