package de.eagle.lillyjakeframework.installer.LillyInstaller;

/**
 * @file LinuxLillyInstaller.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Installiert Lilly auf einem Linuxioiden Betriebssystem
 */

import de.eagle.gepard.modules.Expandables;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.lillyjakeframework.installer.JakeInstaller.LinuxJakeInstaller;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.FunctionCollector;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.Executer;
import de.eagle.util.io.JakeWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import static de.eagle.util.io.JakeLogger.*;

public class LinuxLillyInstaller extends AutoInstaller {
    public LinuxLillyInstaller(boolean gui) {
        super("Linux Lilly Installer", gui);
        steps = new FunctionCollector<>(Map.ofEntries(
                Map.entry("check_pdftex", new FunctionDeskriptor<>("fkt_check_pdflatex","Teste, ob 'pdflatex' vorhanden ist", LinuxLillyInstaller::fkt_check_pdflatex)),
                Map.entry("create_install_path", new FunctionDeskriptor<>("fkt_create_install_path","Erstellt das Installationsziel", LinuxLillyInstaller::fkt_create_install_path)),
                Map.entry("install_lilly", new FunctionDeskriptor<>("fkt_install_lilly","Installiere Lilly", LinuxLillyInstaller::fkt_install_lilly))
        ));
        this.progress = 0;
        next = "check_pdftex";
    }

    public static String[] fkt_check_pdflatex(String s) {
        writeLoggerInfo("Teste Vorhandensein von 'pdflatex'","LLInst");
        try {
            if(Executer.runCommand("which pdflatex").readLine() == null) {
                // Nicht installiert
                writeLoggerWarning("'pdflatex' ist NICHT vorhanden, tue mein bestes es zu installieren","LLInst");
                JakeWriter.out.format("%sEs kann kein 'pdflatex' gefunden werden. Ohne ist Jake ein nichts. " +
                        "Es ist geplant und in Aussicht, dass Jake hier selbst ansetzt und 'pdflatex' Portabel und " +
                        "unabhängig von deinem System installiert, allerdings ist das erst für 1.1.0 geplant und somit " +
                        "noch nicht, naja, implementiert. Darob hier, simple Installation, du kannst die Anfrage " +
                        "einfach abbrechen, es wird dann ohne 'pdflatex' weitergemacht.%s%n",
                        ColorConstants.COL_ERROR, ColorConstants.COL_RESET);
                JakeWriter.out.println("Soll 'texlive' installiert werden?");
                switch (CommandLine.get_answer("[(y)es/(n)o/(c)ancel]> ", new String[] { "Y", "N", "C" })) {
                    case "Y":
                        LinuxJakeInstaller.installPackages("texlive-most mlocate texlive-lang texlive-langextra biber texlive-full");
                        break;
                    case "N":
                        JakeWriter.out.println("Installation wird nicht durchgeführt");
                        break;
                    case "C":
                        JakeWriter.err.println("Installation wird abgebrochen");
                        return END;
                }
            } else {
                writeLoggerDebug2("'pdflatex' ist vorhanden","LLInst");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[] {"create_install_path", "Erstelle Installationsziel"};
    }

    public static String[] fkt_create_install_path(String s) {
        writeLoggerInfo("Erstelle Installationspfad auf Basis von \"install-path\" "
                + Paths.get(CoreSettings.requestValue("S_INSTALL_PATH"),"tex","latex").toFile().mkdirs(),"LLInst");

        JakeWriter.out.println("Installations-Ordnerstruktur erstellt");
        return new String[] {"install_lilly", "Installiere Lilly"};
    }

    public static String[] fkt_install_lilly(String s) {
        writeLoggerInfo("Suche die Lilly.cls","LLInst");
        File f1 = null, f2 = null;
        String p = null;
        try {
            p = Executer.runBashCommand("echo -n " + Expandables.expand(exp, CoreSettings.requestValue("S_LILLY_PATH"))).readLine();
            f1 = Paths.get(p, "Lilly.cls").toFile();
            f2 = Paths.get(p).toFile();
        } catch (Exception ignored){}
        File f = null;
        if(f1 != null && f1.canRead()) f = f1;
        else if(f2 != null && f2.canRead()) f = f2;
        else {
            JakeWriter.err.println("Konnte die Lilly.cls nicht am entsprechenden Pfad (" + f + ") finden");
        }
        if(f != null) {
            // TODO: teset auf miktex
            String i = CoreSettings.requestValue("S_INSTALL_PATH");
            String g = Paths.get(i,"tex","latex").toString();
            writeLoggerInfo("Installiere Lilly für 'texlive'","LLInst");
            JakeWriter.out.format("Verlinke (%s=%s) nach (%s): %s%n",
                    CoreSettings.requestValue("S_LILLY_PATH"),
                    p, g,
                    Arrays.toString(Executer.runBashCommand("ln -sf " + CoreSettings.requestValue("S_LILLY_PATH") + " " + g).lines().toArray(String[]::new))
                    );

            // TODO: information über angabe des Pfades
            JakeWriter.out.format("Informiere TEX über (%s): %s%n",i,
                    Arrays.toString(Executer.runBashCommand("texhash " + i).lines().toArray(String[]::new)));
        }
        // TODO: As last step: search for packages

        JakeWriter.out.println("Lilly installiert");
        return END;
    }

        /**
         * Validiert, ob alles korrekt installiert wurde
         *
         * @return true, wenn ja, sonst false
         */
    @Override
    public boolean validate() {
        return false;
    }

}
