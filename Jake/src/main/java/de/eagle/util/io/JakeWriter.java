package de.eagle.util.io;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Kapselt println format und print um auch für die GUI zu funktionieren
 */
public class JakeWriter {
    public static PrintStream out = System.out;
    public static InputStream in = System.in;
}