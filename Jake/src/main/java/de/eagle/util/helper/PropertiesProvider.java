package de.eagle.util.helper;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.lillyjakeframework.installer.LinuxInstaller;
import de.eagle.lillyjakeframework.installer.MacOSInstaller;
import de.eagle.lillyjakeframework.installer.WindowsInstaller;

import javax.swing.*;

/**
 * Liefert verschiedene System-Spezifische Einstellungen
 */
public class PropertiesProvider {
    /**
     * Spiegelt die Betriebssysteme dar, anhand derer eine unterschiedliche
     * Behandlung nötig ist
     */
    public enum OS {
        WINDOWS("win"), LINUX("nix", "nux", "aix"), SOLARIS("sunos"), MAC("mac"), OTHER("WAFFEL");
        String[] matchers;

        OS(String... m) {
            matchers = m;
        }
    }

    public static String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"));
    }

    /**
     * Liefert das entsprechende Betriebssystem als {@link PropertiesProvider.OS}
     * 
     * @return Das entsprechende Betriebssystem
     */
    public static OS getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        for (OS o : OS.values()) {
            for (String s : o.matchers) {
                if (os.contains(s))
                    return o;
            }
        }
        return OS.OTHER;
    }

    /**
     * @return Liefert das Home-Verzeichnis des Nutzers
     */
    public static String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * @return Liefert den Nutzernamen
     */
    public static String getUsername() {
        return System.getProperty("user.name");
    }

    /**
     * @return Liefert das aktuelle Arbeitsverzeichnis des Nutzers
     */
    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    /**
     * @return Liefert den Datei-Separator ('/' auf Linux/Mac, '\' auf Windoof)
     */
    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * @todo use for lists etc.
     * @return Liefert den Pfad-Separator (':') kann für Lists verwendet werden
     */
    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    /**
     * @return Liefert die Zeichen für den Start einer neuen Zeile (z.B. '\n' oder
     *         '\r\n')
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * @return Liefert die Java Version
     */
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * @return Liefert den Pfad zum temporären Verzeichnis (JakeLogger etc.)
     */
    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getThisPath() {
        try {
            return PropertiesProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString().replaceFirst("file:", "");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ".";
    }

    public static AutoInstaller getInstaller(boolean gui) {
        switch (PropertiesProvider.getOS()) {
            case LINUX:
                return new LinuxInstaller(gui);
            case MAC:
                return new MacOSInstaller(gui);
            case WINDOWS:
                return new WindowsInstaller(gui);
            default:
                throw new UnsupportedOperationException("Es existiert kein Installer für dieses Betriebssystem");
        }
    }

    public static boolean isInstalled(){
        switch(getOS()) {
            case LINUX:
                return new LinuxInstaller(false).validate();
            case MAC:
                return new MacOSInstaller(false).validate();
            case WINDOWS:
                return new WindowsInstaller(false ).validate();
            default: // we don't care
                return true;
        }
    }

}
