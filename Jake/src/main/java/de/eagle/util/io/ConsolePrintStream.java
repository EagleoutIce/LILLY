package de.eagle.util.io;

import java.io.PrintStream;

public class ConsolePrintStream extends AbstractConsoleWriter {

    private PrintStream output;
    public ConsolePrintStream(PrintStream out) {
        output = out;
    }

    @Override
    public AbstractConsoleWriter println() {
        output.println(); return this;
    }

    @Override
    public AbstractConsoleWriter println(String s) {
        output.println(s); return this;
    }

    @Override
    public AbstractConsoleWriter print(String s) {
        output.print(s); return this;
    }

    @Override
    public AbstractConsoleWriter flush() {
        output.flush(); return this;
    }

    @Override
    public AbstractConsoleWriter append(String s) {
        output.append(s); return this;
    }

    @Override
    public AbstractConsoleWriter format(String format, Object... args) {
        output.format(format,args); return this;
    }
}
