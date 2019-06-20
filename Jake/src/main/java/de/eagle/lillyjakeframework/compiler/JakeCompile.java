package de.eagle.lillyjakeframework.compiler;

import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.logging.JakeLogger;

import java.io.File;

/**
 * @class JakeCompile
 *
 * Diese Klasse verwaltet den Prozess einer kompilierung durch Jake
 *
 * Diese Instanz ist der direkte Bruder von c_Jake.cpp aus Jake-Cpp und verwendet deswegen
 * native Systemaufrufe, auch wenn diese in der Form nicht nötig wären!
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @since 1.0.10
 *
 */
public class JakeCompile {

    /**
     * Der eigentliche kompilierungsprozess - Jaiks
     * @param command der Befehl aus der Kommandozeile
     * @return 0 wenn alles gut verlaufen ist
     */
    public static ReturnStatus compile(String command){
        JakeLogger.writeLoggerDebug1("Jake versucht nun das Lilly-Dokument zu kompilieren","compile");
        Settings expandables = /* TODO on expandables: expand_Settings() */ Settings.createDummy("expand_Settings()");

        String targetFile = getTargetFile();

        if(! new File(targetFile).canWrite()) { // check_file
            System.err.println("Die von dir angegebene Datei: " + targetFile + " konnte nicht gefunden werden! Soll Jake sie für dich erstellen?");
            if(CommandLine.get_answer("[(y)es/(n)o/(c)ancel]> ", new String[]{"Y","N","C"}).equals("Y")) {
                generateDummyFile(targetFile);
            }
        }

        return new ReturnStatus(0);
    }

    public static String getTargetFile(){
        String target = CoreSettings.requestValue("S_LILLY_IN") + PropertiesProvider.getPathSeparator() + CoreSettings.requestValue("S_FILE");
        JakeLogger.writeLoggerInfo("Zieldatei identifiziert: " + target,"compile");
        return target;
    }

    /**
     * Generiert ein initiales Texfile
     * @param name Name der Tex-Datei
     * @return 0 wenn alles gut gelaufen ist
     */
    public static ReturnStatus generateDummyFile(String name){
        // Todo identify documenttype first => nmaps!

        switch (CoreSettings.requestValue("S_DOCTYPE")){
            default: // annahme: Mitschrieb
                // CURRENT POINT OF TODO
        }

        return new ReturnStatus(1); // ALWAYS FAIL
    }



}
