package de.eagle.util.helper;

// Allows to execute a shellscript from ressource folder

import java.io.*;

/**
 * @author Florian Sihler
 * @since 1.0.10
 * @version 1.0.10
 */
public class Executer {
    /**
     *
     * @param path der Pfad im ressource folder
     * @return path im temp-verzeichnis
     */
    public static String getPath(String path) throws IOException {
        // Copy file to temp place
        // @see Cloner#cloneFileRessource(String,String)
        BufferedReader in = new BufferedReader(new InputStreamReader(Executer.class.getResourceAsStream(path)));
        File outf = File.createTempFile("jake-tmp", ".sh");
        BufferedWriter out = new BufferedWriter(new FileWriter(outf));
        String s;
        while((s = in.readLine()) != null){
            out.write(s+"\n");
        }
        in.close();out.close();
        return outf.getAbsolutePath();
    }
}
