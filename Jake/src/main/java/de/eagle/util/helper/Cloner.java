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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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

    /**
     * Will copy a whole directory from the Jar to an external Directory
     *
     * Taken from: <a href="https://stackoverflow.com/questions/1386809/copy-directory-from-a-jar-file">Stackoverflow</a>
     *
     * @param source the resource path
     * @param target the target Path
     * @throws URISyntaxException In case of a malformed URI
     * @throws IOException reading fails
     */
    public static void copyFromJar(String source, final Path target) throws URISyntaxException, IOException {
        FileSystem fileSystem = ResourceControl.getFs();

        final Path jarPath = fileSystem.getPath(source);

        Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>() {

            private Path currentTarget;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                currentTarget = target.resolve(jarPath.relativize(dir).toString());
                Files.createDirectories(currentTarget);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

        });
    }
}
