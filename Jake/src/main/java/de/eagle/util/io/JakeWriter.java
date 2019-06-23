package de.eagle.util.io;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Kapselt println format und print um auch für die GUI zu funktionieren
 */
public class JakeWriter {
    public static PrintStream out = System.out;
    public static PrintStream err = System.err;
    public static InputStream in = System.in;

}