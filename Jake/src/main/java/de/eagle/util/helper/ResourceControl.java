package de.eagle.util.helper;

import de.eagle.gepard.modules.Expandables;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.io.JakeLogger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

/**
 * @class ResourceControl
 *
 * Diese Klasse bietet einige Hilfsfunktionen im Umgang mit der Ressourcenverwaltung.
 *
 * @author Florian sihler
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class ResourceControl {

    private static FileSystem fs = null;
    public static FileSystem getFs(){
        if(fs == null) {
            try {
                fs = FileSystems.newFileSystem(
                        ResourceControl.class.getResource("").toURI(),
                        Collections.<String, String>emptyMap()
                );
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return fs;
    }

    public static long getNewestModificationDate(String path) throws IOException {
        return Files.walk(Paths.get(path)).mapToLong(x -> x.toFile().lastModified()).max().orElse(0);
    }

    private static long curMax = 0;

    public static long getNewestModificationDateResource(String path) throws URISyntaxException, IOException {
        FileSystem fileSystem = ResourceControl.getFs();

        curMax = 0;

        final Path homePath = fileSystem.getPath(path);

        Files.walkFileTree(homePath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                long mfile = file.toUri().toURL().openConnection().getLastModified();
                if (mfile > curMax) curMax = mfile;
                return FileVisitResult.CONTINUE;
            }

        });
        return curMax;
    }

    /**
     * Schaue ob bei einer Jar-Installation von Lilly ein Update existiert.
     *
     * @return true, wenn eine neue Version existiert.
     */
    public static boolean LillyInJarIsNewer(){
        String i = CoreSettings.requestValue("S_INSTALL_PATH").replace("\"","");
        String out = Paths.get(i,"tex","latex").toString();
        try {
            out = Expandables.expand(Expandables.getInstance().getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH")), out);
        } catch (IOException ignored) { }
        // Check if Lilly is installed by jar (sloppy)
        if(!new File(out + "/Lilly.cls").isFile()){
            return false;
        }
        // Check if the Lilly.cls is newer:
        try {
            if(new File(out + "/Lilly.cls").lastModified() < ResourceControl.class.getResource("/Lilly.cls").openConnection().getLastModified())
                return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // No Check for Folders :D
        try {
            JakeLogger.writeLoggerInfo("LillyVer On Disk: " + getNewestModificationDate(out + "/source") + " in Jar: " + getNewestModificationDateResource("/source"), "ResCon");
            return getNewestModificationDate(out + "/source") < getNewestModificationDateResource("/source");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

}
