package de.eagle.util.helper;

/**
 * @file PropertiesProvider.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Liefert Eigenschaften über das Betriebssystem oder andere Meta-Basierte Informationen.
 */


import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.lillyjakeframework.installer.JakeInstaller.LinuxJakeInstaller;
import de.eagle.lillyjakeframework.installer.JakeInstaller.MacOSJakeInstaller;
import de.eagle.lillyjakeframework.installer.JakeInstaller.WindowsJakeInstaller;
import de.eagle.lillyjakeframework.installer.LillyInstaller.LinuxLillyInstaller;

import java.io.File;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
     * @return true, wenn das (Linux-) System ohne laufenden X-Server betrieben wird
     */
    public static boolean isHeadless(){
        return System.getenv("DISPLAY") == null;
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

    /**
     * @return Liefert den Pfad zur dieser Anwendung
     */
    public static String getThisPath() {
        try {
            return PropertiesProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString().replaceFirst("file:", "");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ".";
    }

    /**
     * @param gui Soll der Installer mithilfe einer graphischen Oberfläche angezeigt werden.
     * @return einen Installer für das entsprechende Betriebssystem
     */
    public static AutoInstaller getJakeInstaller(boolean gui) {
        switch (PropertiesProvider.getOS()) {
            case LINUX:
                return new LinuxJakeInstaller(gui);
            case MAC:
                return new MacOSJakeInstaller(gui);
            case WINDOWS:
                return new WindowsJakeInstaller(gui);
            default:
                throw new UnsupportedOperationException("Es existiert kein Installer für dieses Betriebssystem");
        }
    }

    /**
     * @param gui Soll der Installer mithilfe einer graphischen Oberfläche angezeigt werden.
     * @return einen Installer für Lilly, für das entsprechende Betriebssystem
     */
    public static AutoInstaller getLillyInstaller(boolean gui) {
        switch (PropertiesProvider.getOS()) {
            case LINUX:
                return new LinuxLillyInstaller(gui);
            case MAC:
                return new MacOSJakeInstaller(gui);
            case WINDOWS:
                return new WindowsJakeInstaller(gui);
            default:
                throw new UnsupportedOperationException("Es existiert kein Installer für dieses Betriebssystem");
        }
    }

    /**
     * Überprüfe ob Jake installiert ist.
     *
     * @return true, wenn bereits installiert, sonst false.
     */
    public static boolean isInstalled(){
        switch(getOS()) {
            case LINUX:
                return new LinuxJakeInstaller(false).validate();
            case MAC:
                return new MacOSJakeInstaller(false).validate();
            case WINDOWS:
                return new WindowsJakeInstaller(false ).validate();
            default: // we don't care
                return true;
        }
    }

    /**
     * Set's the Working directory for the Program.
     * @param dirname The new working directory
     * @return false, if the operation failed (dir not exists etc.)
     */
    public static boolean setUserDirectoy(String dirname) {
        File dir = new File(dirname).getAbsoluteFile();
        if(dir.exists() || dir.mkdirs()) {
            return System.setProperty("user.dir", dir.getAbsolutePath()) != null;
        }
        return false;
    }

}
