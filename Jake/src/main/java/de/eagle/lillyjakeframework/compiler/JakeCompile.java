package de.eagle.lillyjakeframework.compiler;

import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * @class JakeCompile
 *
 *        Diese Klasse verwaltet den Prozess einer kompilierung durch Jake
 *
 *        Diese Instanz ist der direkte Bruder von c_Jake.cpp aus Jake-Cpp und
 *        verwendet deswegen native Systemaufrufe, auch wenn diese in der Form
 *        nicht nötig wären!
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @since 1.0.10
 *
 */
public class JakeCompile {

    Settings expandables = new Settings("Expandables TODO");

    /**
     * Der eigentliche kompilierungsprozess - Jaiks
     * 
     * @param command der Befehl aus der Kommandozeile
     * @return 0 wenn alles gut verlaufen ist
     * @throws IOException
     */
    public static ReturnStatus compile(String command) throws IOException {
        JakeLogger.writeLoggerDebug1("Jake versucht nun das Lilly-Dokument zu kompilieren", "compile");
        Settings expandables = /* TODO on expandables: expand_Settings() */ Settings.createDummy("expand_Settings()");

        String targetFile = getTargetFile();

        if (!new File(targetFile).canWrite()) { // check_file
            System.err.println("Die von dir angegebene Datei: " + targetFile
                    + " konnte nicht gefunden werden! Soll Jake sie für dich erstellen?");
            switch (CommandLine.get_answer("[(y)es/(n)o/(c)ancel]> ", new String[] { "Y", "N", "C" })) {
                default:
                case "Y": generateDummyFile(targetFile); break;
                case "N": JakeWriter.out.println("Jake wird so tun, als gäbe es die Datei und spielt heile Welt!"); break;
                case "C": JakeWriter.out.println("Abbruch!"); return new ReturnStatus(1);
            }
        }

        Settings update_config = new Settings("nmaps:what_trigger() TODO");
        if(update_config.size() > 0){
            JakeWriter.out.println("Information: Aufgrund des Name-Mappings werden deine Einstellungen angepasst. Die Regeln im Folgenden werden jeweils angezeigt und angwendet:");
            String new_config = "";
            for(var sd : update_config){
                System.out.format("    - %s%n", sd.getKey());
                new_config += sd.getValue().getValue() + "\n";
            }
            Configurator config_update = new Configurator(new ByteArrayInputStream(new_config.getBytes(Charset.defaultCharset())));
            config_update.parse_settings(CoreSettings.settings,false);
            expandables = new Settings("updated expandables");
        }
        writeLoggerDebug1("Jake Footprint: " + Definitions.PRG_BRIEF + " (" + PropertiesProvider.getNow() + ")", "compile"); // TODO: change to real compile date



        return new ReturnStatus(0);
    }

    public static String getTargetFile() {
        String target = CoreSettings.requestValue("S_LILLY_IN") + PropertiesProvider.getPathSeparator()
                + CoreSettings.requestValue("S_FILE");
        JakeLogger.writeLoggerInfo("Zieldatei identifiziert: " + target, "compile");
        return target;
    }



    /**
     * Generiert ein initiales Texfile
     * 
     * @param name Name der Tex-Datei
     * @return 0 wenn alles gut gelaufen ist
     * @throws IOException
     */
    public static ReturnStatus generateDummyFile(String name) throws IOException {
        // Todo identify documenttype first => nmaps!
        PrintWriter out = new PrintWriter(name);

        out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        out.write("%% Von Jake erstelltes Lilly LaTeX-File                        %%\n");
        out.write("%% Version: " + Definitions.JAKE_VERSION + "  Author: Florian Sihler                    %%\n");
        out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");
        
        switch (CoreSettings.requestValue("S_DOCTYPE")){
            default: // annahme: Mitschrieb
            case "Mitschrieb":
                // CURRENT POINT OF TODO
                out.write("%% Dokumenttyp: Mitschrieb\n");
                out.write("\\documentclass[Typ=Mitschrieb,Jake]{Lilly}\n\n");
                out.write("\\begin{document}\n");
                out.write("\\Hallo Welt\\newline\n");
                out.write("Lilly-Version: \\LILLYxSTATUS\\newline\n");
                out.write("Status: \\LILLYxSTATUS\\newline\n");
                out.write("Colormap: \\LILLYxCOLORxRainbow\n");
                out.write("\\end{document}\n");
                out.close();
                break;
        }

        return new ReturnStatus(1); // ALWAYS FAIL
    }



}
