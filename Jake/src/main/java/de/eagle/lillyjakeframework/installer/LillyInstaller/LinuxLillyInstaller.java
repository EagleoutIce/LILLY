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
import de.eagle.util.helper.Cloner;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.Executer;
import de.eagle.util.io.JakeWriter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
                + Paths.get(CoreSettings.requestValue("S_INSTALL_PATH").replace("\"", ""),"tex","latex").toFile().mkdirs(),"LLInst");

        JakeWriter.out.println("Installations-Ordnerstruktur erstellt");
        return new String[] {"install_lilly", "Installiere Lilly"};
    }

    public static File searchLillyCls(){
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
            //JakeWriter.err.println("Konnte die Lilly.cls nicht am entsprechenden Pfad (" + f2 + ") finden");
        return f;
    }

    public static boolean link_existing(File f){
        if(f != null) {
            // TODO: teset auf miktex
            String i = CoreSettings.requestValue("S_INSTALL_PATH");
            String g = Paths.get(i,"tex","latex").toString();
            writeLoggerInfo("Installiere Lilly für 'texlive'","LLInst");
            JakeWriter.out.format("Verlinke (%s=%s) nach (%s): %s%n",
                    CoreSettings.requestValue("S_LILLY_PATH"),
                    f.getAbsolutePath(), g,
                    Arrays.toString(Executer.runBashCommand("ln -sf " + CoreSettings.requestValue("S_LILLY_PATH") + " " + g).lines().toArray(String[]::new))
            );

            // TODO: information über angabe des Pfades
            JakeWriter.out.format("Informiere TEX über (%s): %s%n",i,
                    Arrays.toString(Executer.runBashCommand("texhash " + i).lines().toArray(String[]::new)));
            return true;
            // TODO: As last step: search for packages
        }
        return false;
    }


    public static boolean installIncluded() throws IOException {
        // Just to be sure, we will copy the whole Structure to the Target-Path without Linking :D
        // First of All, Copy the Lilly.cls:
        String i = CoreSettings.requestValue("S_INSTALL_PATH").replace("\"","");
        String out = Paths.get(i,"tex","latex").toString();
        try {
            out = Expandables.expand(Expandables.getInstance().getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH")), out);
        } catch (IOException ignored) {}
        Cloner.cloneFileRessource("/Lilly.cls", out + "/Lilly.cls");
        //System.out.println(LinuxLillyInstaller.class.getResource("/Lilly.cls"));
        // Now clone the folder
        try {
            Cloner.copyFromJar("/source", Paths.get(out + "/source"));
            // System.out.println("On System: " + ResourceControl.getNewestModificationDate(out+"/source"));
            // System.out.println("In Jar: " + ResourceControl.getNewestModificationDateResource("/source"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String[] fkt_install_lilly(String s) {
        // Prüfe zu Beginn, ob es denn überhaupt eine Lilly.cls gibt :D
        File target = searchLillyCls();
        if(target != null) {
            writeLoggerInfo("Erfrage Installationsart für interne Lilly.cls", "LLInst");
            JakeWriter.out.println("Jake kann Lilly mit Version 2.0.0 auf 2 verschiedene Arten installieren:");
            JakeWriter.out.println("   1) Verlinkung der gefunden Lilly-Instanz (" + target.getPath() + ")");
            JakeWriter.out.println("   2) Installation der in Jake enthaltenen Variante von Lilly");
            switch (CommandLine.get_answer("Gebe deine Auswahl ein [1/2/(c)ancel]> ", new String[]{"1","2","c"})) {
                case "1":
                    link_existing(target); break;
                case "2":
                    try {
                        installIncluded();
                    } catch (IOException e) {
                        JakeWriter.err.println(e.getMessage());
                    }
                    break;
                default:
                case"c":
                    JakeWriter.out.println("Abbruch!");
                    return END;
            }
        } else {
            writeLoggerInfo("Kopiere interne Variante von Lilly", "LLInst");
            try {
                installIncluded();
            } catch (IOException e) {
                JakeWriter.err.println(e.getMessage());
            }
        }

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
