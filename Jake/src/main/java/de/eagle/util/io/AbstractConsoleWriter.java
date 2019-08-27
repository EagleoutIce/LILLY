package de.eagle.util.io;

public abstract class AbstractConsoleWriter {

    public AbstractConsoleWriter(){}


    public abstract AbstractConsoleWriter println();
    public abstract AbstractConsoleWriter println(String s);
    public abstract AbstractConsoleWriter print(String s);
    public abstract AbstractConsoleWriter flush();

    public abstract AbstractConsoleWriter append(String s);
    public abstract AbstractConsoleWriter format(String format, Object... args);
}
