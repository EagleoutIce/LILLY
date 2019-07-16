package de.eagle.util.io;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

import de.eagle.lillyjakeframework.core.Definitions;

/**
 * Kapselt println format und print um auch für die GUI zu funktionieren
 */
public class JakeWriter {
    // This will output The text to wherever you want, AND pass through all needed
    // Information to the according Log-File denoted by JakeLogger - this is english, cause i like it

    public static class MirrorStream {
        private final boolean mirrorLog;
        private final boolean mirrorConsole;
        PrintStream out = System.out;

        public MirrorStream(){
            this(Definitions.MIRROR_LOG, Definitions.MIRROR_CONSOLE);
        }

        public MirrorStream(boolean mirrorLog, boolean mirrorConsole) {
            this.mirrorLog = mirrorLog;
            this.mirrorConsole = mirrorConsole;
            JakeLogger.writeLoggerInfo("Konfiguration: " + this.toString(), "WRITER");// Always tell
        }

        public MirrorStream append(char c) {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Appended: " + c, "WRITER");
            if(mirrorConsole) out.append(c);
            return this;
        }

        public MirrorStream append(CharSequence c) {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Appended: " + c, "WRITER");
            if(mirrorConsole) out.append(c);
            return this;
        }
        public MirrorStream format(String format, Object... args) {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Fromatting: \"" + format + "\" with: \"" + Arrays.toString(args) + "\"", "WRITER");
            if(mirrorConsole) out.format(format,args);
            return this;
        }
        public MirrorStream print(String s) {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Written: " + s, "WRITER");
            if(mirrorConsole) out.print(s);
            return this;
        }
        public MirrorStream println(String s) {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Written Line: " + s, "WRITER");
            if(mirrorConsole) out.println(s);
            return this;
        }
        public MirrorStream println() {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Written empty line!", "WRITER");
            if(mirrorConsole) out.println();
            return this;
        }
        public MirrorStream flush() {
            if(mirrorLog) JakeLogger.writeLoggerInfo("Flushed!", "WRITER");
            if(mirrorConsole) out.flush();
            return this;
        }

        @Override
        public String toString() {
            return "MirrorStream [mirrorConsole=" + mirrorConsole + ", mirrorLog=" + mirrorLog + ", out=" + out + "]";
        }
    }

    public static MirrorStream out = new MirrorStream();
    public static MirrorStream err = new MirrorStream();
    public static InputStream in = System.in;

}