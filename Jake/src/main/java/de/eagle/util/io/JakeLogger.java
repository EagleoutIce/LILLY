package de.eagle.util.io;

/**
 * @file JakeLogger.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief verwaltet einen globalen Log
 * @see de.eagle.util.io.JakeFormatter
 */

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Liefert den Logger (static)
 */
public class JakeLogger {
    /// der logger
    private static Logger LOGGER;


    /**
     * Initialisiert den Logger mit einer Temp-Datei
     * 
     * @param output mirror to console?
     * @param level auf Basis welches Levels soll die Ausgabe erfolgen ?
     */
    static void initLogger(boolean output, Level level) {
        try {
            initLogger(File.createTempFile("lilly-temp-log", ".temp").getAbsolutePath(), output, level);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert den Logger auf einem Pfad
     *
     * @param output mirror to console?
     * @param targetFilePath die Zieldatei
     * @param level auf Basis welches Levels soll die Ausgabe erfolgen ?
     */
    public static void initLogger(String targetFilePath, boolean output, Level level) {
        LOGGER = Logger.getLogger("Main Jake Log");
        getLogger().setUseParentHandlers(output);
        getLogger().setLevel(level);
        try {
            FileHandler fh = new FileHandler(targetFilePath);
            fh.setFormatter(new JakeFormatter());
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Erhalte die Loggerinstanz, erstelle im Zweifelsfall eine Neue (Singleton)
     */
    protected static Logger getLogger() {
        if (LOGGER == null)
            initLogger(false,Level.ALL);
        return LOGGER;
    }


    /* ====================================================================================================== */
    /* Nette kleine shortcuts - diese können später auch andere Designs hervorrufen und werden einfach mal    */
    /* Auf Basis der guten Java-Schule gekapselt - JAY                                                        */
    /* ====================================================================================================== */

    /// @note für korrekte Angabe des Debugs wurde WriteLogger als zwischenschritt wieder entfernt ^^ - SAD

    /**
     * Schreibt eine Debug Nachricht für Level 1
     * Eine solche Nachricht darf alles beinhaltet was einzelne Schritte eines Programms aufdeckt
     * so zum Beispiel:
     *   - Neue Zeile geladen: ...
     *   - Datenverabeitung ergab: ...
     * 
     * @param message die Nachricht
     * @param sender wer möchte die Nachricht abgeben?
     * 
     */
    public static void writeLoggerDebug1(String message, String sender){
        getLogger().log(Level.FINE, String.format("%-45s        %-8s: %s", getCallerName(), sender, message));
    }




    /**
     * Schreibt eine Debug Nachricht für Level 2
     * Eine solche Nachricht darf alles beinhaltet was einzelne Zwischenschritte eines Programms aufdeckt
     * so zum Beispiel:
     *   - Entferne Kommentare,...
     *   - Prüfe auf isValid()...
     * Es ist duchaus möglich, dass derartige Nachrichten hinter jeder zweiten Zeile auftauchen
     * weswegen dieses Level im endgültigen Release selbstredend nicht vorhanden sein wird :D
     * Die Tests werden es allerdings stets mit ausspucken :)
     * 
     * @param message die Nachricht
     * @param sender wer möchte die Nachricht abgeben?
     * 
     */
    public static void writeLoggerDebug2(String message, String sender){
        getLogger().log(Level.FINER, String.format("%-45s        %-8s: %s", getCallerName(), sender, message));
    }

    /**
     * Schreibt eine Debug Nachricht für Level 3
     * Dieses Level beschreibt das Abgrund-Level des Debuggings und darf an sich alles enthalten
     * was sich in keiner anderen Kategorie aufgehoben fühlt. Hiervon AUSGENOMMEN sind:
     * 
     *   - Ich bin hier 
     *          (Stattdessen: get_next_box erreicht if-check für p_begin(line).find)
     *
     *   - Das sollte nicht erreicht werden 
     *          (Stattdessen: get_next_box hat nicht-valide geliefert läuft aber dennoch weiter!!)
     * 
     * Es ist duchaus möglich, dass derartige Nachrichten hinter jeder Zeile auftauchen
     * weswegen dieses Level im endgültigen Release selbstredend nicht vorhanden sein wird :D
     * Die Tests werden es allerdings stets mit ausspucken :)
     * 
     * @param message die Nachricht
     * @param sender wer möchte die Nachricht abgeben?
     * 
     */
    public static void writeLoggerDebug3(String message, String sender){
        getLogger().log(Level.FINEST, String.format("%-45s        %-8s: %s", getCallerName(), sender, message));
    }

    /**
     * Schreibt eine Warnung in den Log
     * Eine Warnung ist alles, was die Weiterarbeit an sich nicht direkt unmöglich macht,
     * allerdings einschränken könnte. Warnungen könnenn in der Hinsicht auch Hinweise
     * an den Nutzer über etwaige fehlerhafte Konfigurationen enthalten!
     * 
     * @param message die Nachricht
     * @param sender wer möchte diese Warnung auslösen?
     * 
     */
    public static void writeLoggerWarning(String message, String sender){
        getLogger().log(Level.WARNING, String.format("%-45s   !!   %-8s: %s", getCallerName(), sender, message));
    }

    /**
     * Tolle Funktion weil Java einfach super ist.. hahahahahahahaha.... haha ha... nein
     * @return CLASS.METHOD des aufrufers
     */
    public static String getCallerName(){
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String newcl = elements[3].getClassName().replace("de.eagle.", "").replace("lillyjakeframework", "[ljf]") + "." + elements[3].getMethodName();
        if(newcl.length()>45)
            return "..." + newcl.substring(newcl.length()-42);
        else return newcl;
    }

    /**
     * Informationen sind relevante Punkte so wie man sie im Log erhalten möchte
     * eine Info kann zum Beispiel die Versionsnummer des Programms sein,
     * oder Daten enthalten wie: "Zeile geladen!"
     * Sie sind also mehr erwartetes Verhalten was den Logverlauf auch
     * gliedern kann.
     * 
     * @param message die Nachricht
     * @param sender wer möchte eine Information kundtun?
     * 
     */
    public static void writeLoggerInfo(String message, String sender){
        getLogger().log(Level.INFO, String.format("%-45s        %-8s: %s", getCallerName(), sender, message));
    }


    /**
     * Ein Fehler macht die Weiterarbeit unmöglich und ist vermutlich
     * die letzte Zeile im Log (ausgenommen: Aufräumarbeiten)
     * Es ist prinzipiell nicht verboten mehrere Fehler ausgeben zu
     * lassen oder auch nach Fehlern weiterzuarbeiten,
     * allerdings sollte dem Endnutzer auch direkt ein solcher
     * Fehler schmerzlich näher gebracht werden!
     * (Dies wird von writeLoggerError natürlich nicht übernommen)
     *
     * @implNote Schreibt direkt an den Error OutputStream des Systems
     *
     * @param message die Nachricht
     * @param sender wo brennts?
     * 
     */
    public static void writeLoggerError(String message, String sender){
        // @Todo nicht machen wenn Terminal-Sync aktiv
        // System.err.format("%-45s   ##   %-8s: %s", getCallerName(), sender, message);
        getLogger().log(Level.SEVERE, String.format("%-45s   ##   %-8s: %s", getCallerName(), sender, message));
    }


    /**
     * Nichtmehr so ultimatives Mind hinter allen anderen Logger Funktionen
     * Schreibt eine Nachricht die Wirklich nirgendwo sonst reinpasst
     * Diese Funktion sollte so nicht verwendet werden! Kann aber
     * 
     * @note Please don't use me!
     * 
     * @param message die Nachricht
     * @param sender Nachrichtenschreiber
     * @param level Relevanz der Nachricht
     *
     * Nutzer:
     *     - {@link #writeLoggerDebug1(String, String)}
     *     - {@link #writeLoggerDebug2(String, String)}
     *     - {@link #writeLoggerDebug3(String, String)}
     *     - {@link #writeLoggerInfo(String, String)}
     *     - {@link #writeLoggerWarning(String, String)}
     *     - {@link #writeLoggerError(String, String)}
     *
     * @see JakeLogger
     */
    public static void writeLogger(String message, String sender, Level level){
        getLogger().log(Level.WARNING, String.format("%-45s   ~~   %-8s: %s", getCallerName(), sender, message));
    }


}
