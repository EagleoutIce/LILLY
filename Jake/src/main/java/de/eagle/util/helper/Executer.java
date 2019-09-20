package de.eagle.util.helper;

/**
 * @file Executer.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Erlaubt es Dokumente aus dem ResourceFolder auszuführen oder allgemein Konsolen-Befehle zu initiieren.
 */


// Allows to execute a shellscript from ressource folder

import de.eagle.util.io.JakeLogger;

import java.io.*;

/**
 * @author Florian Sihler
 * @since 2.0.0
 * @version 2.0.0
 */
public class Executer {
    /**
     * Kopiert eine Datei aus dem Resource-Folder ins Temp-Verzeichnis
     *
     * @see Cloner#cloneFileRessource(String,String)
     * @param path der Pfad im ressource folder
     * @return path im temp-verzeichnis
     */
    public static String getSHPath(String path) throws IOException {
        // Copy file to temp place
        return getPath(new InputStreamReader(Executer.class.getResourceAsStream(path)),".sh");
    }

    /**
     * Kopiert die Daten eines InputStreamReaders ins Temp-Verzeichnis
     *
     * @see Cloner#cloneFileRessource(String,String)
     *
     * @param suffix Das Suffix der Datei
     * @param inpr die einzulesenden Daten
     * @return path im temp-verzeichnis
     */
    public static String getPath(InputStreamReader inpr, String suffix) throws IOException {
        // Copy file to temp place
        BufferedReader in = new BufferedReader(inpr);
        File outf = File.createTempFile("jake-tmp", suffix);
        BufferedWriter out = new BufferedWriter(new FileWriter(outf));
        String s;
        while ((s = in.readLine()) != null) {
            out.write(s + "\n");
        }
        in.close();
        out.close();
        return outf.getAbsolutePath();
    }

    public static BufferedReader runCommand(String cmd) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            return new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedReader runBashCommand(String cmd) {
        JakeLogger.writeLoggerInfo("Executing Bash-Command (via bash -c ...): " + cmd, "exec");
        return runCommand(new String[] { "bash", "-c", cmd });
    }

    public static BufferedReader runCommand(String[] cmd) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            return new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
