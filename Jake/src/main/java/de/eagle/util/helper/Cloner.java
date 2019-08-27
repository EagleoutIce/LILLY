package de.eagle.util.helper;

/**
 * @file Cloner.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Versucht jedes Objekt zu kopieren ;P (#DankeJava for broken 'Cloneable')
 *
 * @see de.eagle.util.interfaces.iRealCloneable
 */

import de.eagle.util.interfaces.iRealCloneable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

/**
 * Versucht mithilfe von Javas Reflection-Mechanismen ein Objekt zu Clonen, da
 * Javas Cloneable-Interface ja eine Katastrophe ist (insbesondere für Generics)
 *
 * @param <T> zu klonender Typ
 */
public class Cloner<T> implements iRealCloneable<T> {

    /// Zwischenspeicher der zu kopierenden Daten
    private T clone_data;

    /**
     * Konstruiert den entsprechenden Kopierer
     *
     * @param data zu kopierende Daten
     */
    public Cloner(T data) {
        clone_data = data;
    }

    /**
     * @return Eine (hoffentlich) unabhängige Kopie des Objekts
     */
    @SuppressWarnings("unchecked")
    public T clone() {
        if (clone_data == null)
            return null;

        Cloner<T> newT = new Cloner<T>(null);
        try {
            newT.clone_data = (T) clone_data.getClass().getMethod("clone").invoke(clone_data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // Versuche normale zuweisung
            // JakeWriter.out.println("Cloner scheitert für: " + clone_data);
            try {
                newT = (Cloner<T>) super.clone();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        }
        return newT.clone_data;
    }

    /**
     * Diese Methode spiegelt eine Datei aus den Ressourcen ins richtige Dateisystem
     *
     * @throws IOException
     */
        public static String cloneFileRessource(String res, String dst) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(Executer.class.getResourceAsStream(res)));
        File outf = new File(dst);
        BufferedWriter out = new BufferedWriter(new FileWriter(outf));
        String s;
        while ((s = in.readLine()) != null) {
            out.write(s + "\n");
        }
        in.close();
        out.close();
        return dst;
    }
}
