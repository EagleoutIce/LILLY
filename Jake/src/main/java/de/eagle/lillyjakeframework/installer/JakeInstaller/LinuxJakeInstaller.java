package de.eagle.lillyjakeframework.installer.JakeInstaller;

import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.lillyjakeframework.gui.core.LinuxInstallPackages;
import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.FunctionCollector;
import de.eagle.util.datatypes.FunctionDeskriptor;
import de.eagle.util.helper.Cloner;
import de.eagle.util.helper.Executer;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;


// TODO: insert debug-statements

public class LinuxJakeInstaller extends AutoInstaller {
    public static LinkedList<String[]> neededPrograms = new LinkedList<>(Arrays.asList(new String[]{"git","git"},new String[]{"pdflatex", "texlive-full"},
            new String[]{"test-waffel", "waffel-test"}));


    public static LinkedList<String> shells = new LinkedList<>(
            Arrays.asList(Paths.get(HOME, ".zshrc").toString(), Paths.get(HOME, ".bashrc").toString(),
                    Paths.get(HOME, ".bash_profile").toString(), Paths.get(HOME, ".kshrc").toString()));

    public static String getDesktopPath() {
        return Paths.get(HOME, ".local", "share", "applications").toString() + PropertiesProvider.getFileSeparator()
                + "jake.desktop";
    }

    public static String getCmdLinePath() {
        return PropertiesProvider.getFileSeparator() + Paths.get(HOME, ".local", "bin").toString()
                + PropertiesProvider.getFileSeparator() + "jake";
    }

    /**
     * Generiert den Menü eintrag
     * 
     * @return Array der Form [ID-der nächsten Funktion, Beschreibung der nächsten
     *         Funktion]
     */
    public static String[] fkt_generate_menu_entry(String s) {
        try { // TODO: das kann man auslagern
            Process p = Runtime.getRuntime().exec("which lilly_jake");
            BufferedReader bis = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if(bis.readLine() != null) {
                JakeWriter.out.println("Wie es scheint, hast du bereits 'lilly_jake' installiert, das freut mich. Glückliche Nachricht: Die Installation von Java-Jake sollte ohne Probleme nebenher möglich sein, weiter noch ist sie mit allen bisherigen Einstellungen kompatibel und sollte sogar deine Nutzerkonfiguration akzeptieren! Da es sich bei Java-Jake allerdings immernoch um eine WIP-Version handelt, werden hier auch keine Anstalten gemacht, 'lilly_jake' zu deinstallieren!");
            }
            p.waitFor();
        } catch(Exception ignored) {}

        if(PropertiesProvider.isHeadless()){
            return new String[] { "generate_cmd_line_exec", "Stelle Jake der Konsole zur Verfügung" };
        }
        
        try (PrintWriter pw = new PrintWriter(getDesktopPath())) {

            pw.println("[Desktop Entry]");
            pw.println("Type=Application");
            pw.println("Name=Jake");
            pw.println("GenericName=Jake");
            pw.println("Comment=" + Definitions.PRG_BRIEF);
            pw.println("Exec=java -jar " + PropertiesProvider.getThisPath() + " GUI %u");
            // TODO: icon
            pw.println("NoDisplay=false");
            pw.println("Terminal=false");
            pw.println("TerminalOptions=");
            pw.println(
                    "X-GNOME-Autostart-Delay=10\nX-MATE-Autostart-Delay=10\nX-KDE-autostart-after=panel\nX-GNOME-Autostart-enabled=true");
            pw.println("StartupNotify=true");
            pw.println("Categories=Development;");
        } catch (Exception ignored) {
        }

        // validate Jake exists:
        if (!new File(getDesktopPath()).canRead()) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Die Installation scheint fehlgeschlagen zu sein! (DesktopInstall)", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return new String[] { "generate_cmd_line_exec", "Stelle Jake der Konsole zur Verfügung" };
    }

    public static String[] fkt_generate_cmd_line_exec(String s) {
        // Erstelle command line path wenn noch nicht vorhanden:
        new File(getCmdLinePath()).getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(getCmdLinePath())) {
            pw.println("#!/bin/bash");
            pw.println("java -jar " + PropertiesProvider.getThisPath() + " \"$@\"");
        } catch (Exception ignored) {
        }
        new File(getCmdLinePath()).setExecutable(true, false);

        if (!new File(getCmdLinePath()).canExecute()) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Die Installation scheint fehlgeschlagen zu sein! (CmdLineInstall)", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return new String[] { "inject_path_extend", "Erweitere Path-Variable" };
    }

    public static String[] fkt_inject_path_extend(String s) {

        /*
            Generate the autocomplete script
        */
        Paths.get(HOME, ".local", "bash_completition").toFile().mkdirs(); // Anlegen des Verzeichnisses
        String autocompletePath = Paths.get(HOME, ".local", "bash_completition","_jake.complete").toString();
        try {
            Cloner.cloneFileRessource("/configs/jake_autocomplete.complete", autocompletePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    

        // source $(shell pwd)/_jake_autocomplete
        for (String f : shells) {
            File file = new File(f);
            JakeLogger.writeLoggerDebug3("Checking: " + file.getAbsolutePath(),"LinuxIns");

            if (file.canWrite()) {
                try {
                    String[] lines = Files.lines(Paths.get(file.getAbsolutePath())).filter(x -> !x.contains("# JAVA_JAKE")).toArray(String[]::new); // contains '# Java_Jake'
                    //lines.forEachOrdered(x-> System.out.println(x)); 
                    // Da die Datei überschrieben wird, scheitert die Stream-Operation danach
                    JakeLogger.writeLoggerInfo("Prepping: " + file.getAbsolutePath(),"LinuxIns");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file)); //, true
                    for (String l : lines)
                        pw.println(l);
                    pw.format("export PATH=$PATH:%s; %s%s%s # JAVA_JAKE%n",Paths.get(HOME, ".local", "bin").toString(),
                    (!CoreSettings.requestValue("S_PATH").equals("./")?"export LILLY_JAKE_CONFIG_PATH=\"" + CoreSettings.requestValue("S_PATH") +"\";":""), // wenn angegeben, dann übernommen
                    (f.contains(".zshrc")?"autoload bashcompinit &>/dev/null; bashcompinit &>/dev/null;":""),
                    "source " + autocompletePath);
                    pw.close();
                } catch (Exception ignored) {}
            }
        }

        return new String[]{"vaildate_tools","Überprüfe auf verfügbare Pakete"};
    }


    public static String[] fkt_vaildate_tools(String s){
        // testet ob die notwendigen Programme installiert sind
        Process p = null;
            LinkedList<String> needed = new LinkedList<>();
            for(String[] prg : neededPrograms){
                try {

                    p = Runtime.getRuntime().exec("which " + prg[0]);
                    BufferedReader bis = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    if(bis.readLine() == null) {
                        needed.add(prg[1]);
                    }
                    p.waitFor();
                } catch (Exception ingored) { needed.add(prg[1]); }

            }
            if(needed.size()>0){
                if(doGui){
                    LinuxInstallPackages dialog = new LinuxInstallPackages(needed.toArray(new String[]{}));
                    dialog.pack();
                    dialog.setVisible(true);
                } else {
                    installPackages(needed.toArray(new String[0]));
                }

            }

        return END;
    }

    public static boolean installPackages(String... pkgs){
        try {
            // Überprüfe laufende X-Server Instanz:
            if(PropertiesProvider.isHeadless()) {
                JakeWriter.out.format("%sHeadless-Installation erkannt, insofern hier eine Liste der von Jake beziehungsweise LILLY benötigten Pakete, es obliegt dem Administrator diese Pakete entsprechend bereitzustellen. Jake vertraut auf die Macht eines anderen! :D%s%n", ColorConstants.COL_ERROR, ColorConstants.COL_RESET);
                for (String pkg : pkgs) {
                    JakeWriter.out.format(" - %s%n", pkg);
                }
                return true; 
            }

            String tpath = Executer.getPath("/scripts/install/bash/linux_install.sh");

            String[] bargs = new String[]{"x-terminal-emulator", "-e", "bash", tpath}; // todo make method with LinuxInstallPackages and remove terminal if not neccssar
            String[] args = new String[pkgs.length + bargs.length];
            System.arraycopy(bargs, 0, args, 0, bargs.length);
            System.arraycopy(pkgs, 0, args, bargs.length, pkgs.length);
            //System.out.println(Arrays.toString(args));
            Process tp = new ProcessBuilder(args).start();
            if(tp.waitFor() != Definitions.SUCCESS) {
                JakeWriter.out.println("Die Installation scheint gescheitert/abgebrochen!"); 
                throw new RuntimeException("Installation Cancelled");
            }
            //System.out.println(tp.waitFor()); // todo, retrieve correct return code to detect failures
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }


    public LinuxJakeInstaller(boolean gui) {
        super("Linux Jake Installer",gui);
        // test if lilly_jake is installed:
        steps = new FunctionCollector<>(Map.ofEntries(
            Map.entry("generate_menu_entry", new FunctionDeskriptor<String, String[]>("fkt_generate_menu_entry","Generiert einen Menüeintrag", LinuxJakeInstaller::fkt_generate_menu_entry)),
            Map.entry("generate_cmd_line_exec",  new FunctionDeskriptor<String, String[]>("fkt_generate_cmd_line_exec","Generiert den Kommandozeilenstarter", LinuxJakeInstaller::fkt_generate_cmd_line_exec)),
            Map.entry("inject_path_extend",  new FunctionDeskriptor<String, String[]>("fkt_inject_path_extend","Erweitere Path-Variable", LinuxJakeInstaller::fkt_inject_path_extend)),
            Map.entry("vaildate_tools",  new FunctionDeskriptor<String, String[]>("fkt_vaildate_tools","Teste installierte Pakete", LinuxJakeInstaller::fkt_vaildate_tools))
        ));

        this.progress = 0; // TEMPORARY => CHANGE
        next = "generate_menu_entry";
    }


    /**
     * Validiert, ob alles korrekt installiert wurde
     *
     * @return true, wenn ja, sonst false
     */
    @Override
    public boolean validate() {
        return (new File(getDesktopPath()).canRead() || PropertiesProvider.isHeadless()) && new File(getCmdLinePath()).canExecute();
    }


    /**
     * Deinstalliert die Jake
     */
    @Override
    public boolean uninstall() {
        JakeLogger.writeLoggerInfo("Deinstalliere Jake (Linux)", "Uninst");

        // Entferne Eintrag in Shellrc:
        for (String f : shells) {
            File file = new File(f);
            JakeLogger.writeLoggerDebug3("Checking: " + file.getAbsolutePath(),"Uninst");

            if (file.canWrite()) {
                try {
                    String[] lines = Files.lines(Paths.get(file.getAbsolutePath())).filter(x -> !x.contains("# JAVA_JAKE")).toArray(String[]::new); // contains '# Java_Jake'
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                    for (String l : lines)
                        pw.println(l);
                    pw.close();
                    JakeLogger.writeLoggerDebug3("Removed injection","Uninst");
                } catch (Exception ignored) {

                }
            }
        }
        // Entferne launch-skript
        if(! new File(getCmdLinePath()).canRead()){
            JakeLogger.writeLoggerWarning("Die Datei: " + getCmdLinePath() + " kann nicht gefunden werden", "Uninst");
        } else {
            new File(getCmdLinePath()).delete();
            JakeLogger.writeLoggerInfo("Die Datei: " + getCmdLinePath() + " wurde entfernt", "Uninst");
        }

        // Entferne .desktop-Eintrag, das kann man besser machen ich weiß
        if(! new File(getDesktopPath()).canRead()){
            JakeLogger.writeLoggerWarning("Die Datei: " + getDesktopPath() + " kann nicht gefunden werden", "Uninst");
        } else {
            new File(getDesktopPath()).delete();
            JakeLogger.writeLoggerInfo("Die Datei: " + getDesktopPath() + " wurde entfernt", "Uninst");
        }

        // Entferne complete-skript (bash), das kann man besser machen, aber copy paste ist lustig 
        String ptp = Paths.get(HOME, ".local", "bash_completition","_jake.complete").toString();
        if(! new File(ptp).canRead()){
            JakeLogger.writeLoggerWarning("Die Datei: " + ptp + " kann nicht gefunden werden", "Uninst");
        } else {
            new File(ptp).delete();
            JakeLogger.writeLoggerInfo("Die Datei: " + ptp + " wurde entfernt", "Uninst");
        }
        JakeWriter.out.println("Jake wurde deinstalliert, LOG-Datei dieser Sitzung: \"" + JakeLogger.getTarget() + "\"");
        return true;
    }

}
