package de.eagle.util.io;

/**
 * @file VoidPrintStream.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Schreibt ins nichts
 */


public class VoidPrintStream extends AbstractConsoleWriter {

    public VoidPrintStream() {

    }

    @Override
    public AbstractConsoleWriter println() {
        return this;
    }

    @Override
    public AbstractConsoleWriter println(String s) {
        return this;
    }

    @Override
    public AbstractConsoleWriter print(String s) {
        return this;
    }

    @Override
    public AbstractConsoleWriter flush() {
        return this;
    }

    @Override
    public AbstractConsoleWriter append(String s) {
        return this;
    }

    @Override
    public AbstractConsoleWriter format(String format, Object... args) {
        return this;
    }
}
