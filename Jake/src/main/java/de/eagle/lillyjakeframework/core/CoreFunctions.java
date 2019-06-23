package de.eagle.lillyjakeframework.core;

/**
 * @file CoreFunctions.java
 * @author Raphael Straub
 * @version 1.0.10
 *
 * @brief Enthält viele Funktionen
 */

import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.datatypes.FunctionCollector;

import java.util.Comparator;
import java.util.Map;

import static de.eagle.util.io.JakeLogger.*;

/**
 * Enthält viele Funktionen
 *
 * @author Raphael Straub
 * @version 1.0.10
 * @since 1.0.10
 */
public final class CoreFunctions {
    ///Wird noch nicht genutzt
    public static byte RECURSIVE_CALLCOUNTER = 0;

    /// "Map"  für alle Funktionen der CoreFunctrions Klasse
    public static final FunctionCollector<String[], ReturnStatus> functions_t = new FunctionCollector<>(Map.ofEntries(
            Map.entry("help", new FunctionDeskriptor<>("fkt_help","Zeigt diese Hilfe an", CoreFunctions::fkt_help)),
            Map.entry("dump", new FunctionDeskriptor<>("fkt_dump","Zeigt alle settings und ihre Werte an", CoreFunctions::fkt_dump)),
            Map.entry("file_compile", new FunctionDeskriptor<>("fkt_compile","Erstellt ein makefile für settings[\\\"file\\\"]", CoreFunctions::fkt_compile)),
            Map.entry("install", new FunctionDeskriptor<>("fkt_install","Versucht LILLY zu installieren", CoreFunctions::fkt_install)),
            Map.entry("tokentest", new FunctionDeskriptor<>("fkt_tokentest","Testet den Tokenizer auf seine Funktionalität", CoreFunctions::fkt_tokentest)),
            Map.entry("config", new FunctionDeskriptor<>("fkt_config","Lädt die Einstellungen aus der Datei 'file'", CoreFunctions::fkt_config)),
            Map.entry("get", new FunctionDeskriptor<>("fkt_get","Sucht nach Grafiken die settings[\\\"what\\\"] enthalten!", CoreFunctions::fkt_get)),
            Map.entry("update", new FunctionDeskriptor<>("fkt_update","Versucht Lilly & Jake zu aktualisieren", CoreFunctions::fkt_update)),
            Map.entry("_get", new FunctionDeskriptor<>("fkt_autoget","Interne Funktion, liefert Alles für die Autovervollständigung", CoreFunctions::fkt_autoget))
    ));

    /**
     * Wird in der Zukunft alle aktuellen Settings ausgeben
     *
     * @param cmd Wird nicht genutzt, aber wird vom FunctionCollector so gefordert
     * @return ReturnStatus(0)
     */
    public static ReturnStatus fkt_dump(String[] cmd){
        writeLoggerDebug1("Liefere die Konfigurationen (fkt_dump)","func");

        /*  Placeholder  */

        return new ReturnStatus(0);
    }

    /**
     * Gibt die Hilfe, also alle Funktionen in dieser Klasse und die dazugehörigen Beschreibungen aus
     *
     * @param cmd Wird nicht genutzt, aber wird vom FunctionCollector so gefordert
     * @return ReturnStatus(0)
     */
    public static ReturnStatus fkt_help(String[] cmd){
        writeLoggerDebug1("Gebe die Hilfe aus (fkt_help)","func");
        writeLoggerInfo("Benutzung:\n\n!{program} [options=help] [file]\n\n[options]:\n","func");
        functions_t.entrySet().stream()
                .filter(m -> m.getKey().length() > 0 && m.getKey().charAt(0) != Definitions.HIDDEN_ARG)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(m -> JakeWriter.out.format("  %-15s: " + m.getValue().brief + "%n", m.getKey()));
        JakeWriter.out.println("\n[file]:\nAngabe gemäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \\" +
                " ein generelles makefile für \"xxx.tex\".");
        JakeWriter.out.println("\nnote:\nAllgemeine Einstellungen können über \"-key"+ Definitions.ASS_PATTERN +"value\" gesetzt werden (\"-key\" für boolesche). " +
                "So setzt: \"-path"+ Definitions.ASS_PATTERN +"/es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: \"/es/gibt/kuchen\". " +
                "Weiter ist es möglich mit '"+ Definitions.ASS_PATTERN +"' values hinzuzufügen.");
        return new ReturnStatus(0);
    }

    public static ReturnStatus fkt_install(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}

    public static ReturnStatus fkt_compile(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}

    public static ReturnStatus fkt_tokentest(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}

    public static ReturnStatus fkt_config(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}

    public static ReturnStatus fkt_get(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}

    public static ReturnStatus fkt_autoget(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}

    public static ReturnStatus fkt_update(String[] cmd){/* Placeholder */ return new ReturnStatus(0);}
}
