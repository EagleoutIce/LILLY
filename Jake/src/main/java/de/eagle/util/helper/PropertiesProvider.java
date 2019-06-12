package de.eagle.util.helper;

/**
 * Liefert verschiedene System-Spezifische Einstellungen
 */
public class PropertiesProvider {
    /**
     * Spiegelt die Betriebssysteme dar, anhand derer eine unterschiedliche Behandlung nötig ist
     */
    public enum OS {
        WINDOWS("win"),
        LINUX("nix","nux","aix"),
        SOLARIS("sunos"),
        MAC("mac"),
        OTHER("WAFFEL");
        String[] matchers;
        OS(String... m){
            matchers = m;
        }
    }

    /**
     * Liefert das entsprechende Betriebssystem als {@link PropertiesProvider.OS}
     * @return Das entsprechende Betriebssystem
     */
    public static OS getOS(){
         String os = System.getProperty("os.name").toLowerCase();
         for(OS o : OS.values()){
             for(String s : o.matchers){
                 if (os.contains(s)) return o;
             }
         }
        return OS.OTHER;
    }

    /**
     * @return Liefert das Home-Verzeichnis des Nutzers
     */
    public String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * @return Liefert den Nutzernamen
     */
    public String getUsername() {
        return System.getProperty("user.name");
    }

    /**
     * @return Liefert das aktuelle Arbeitsverzeichnis des Nutzers
     */
    public String getWorkingDir(){
        return System.getProperty("user.dir");
    }

    /**
     * @return Liefert den Datei-Separator ('/' auf Linux/Mac, '\' auf Windoof)
     */
    public String getFileSeparator(){
        return System.getProperty("file.separator");
    }

    /**
     * @todo use for lists etc.
     * @return Liefert den Pfad-Separator (':') kann für Lists verwendet werden
     */
    public String getPathSeparator(){
        return System.getProperty("path.separator");
    }

    /**
     * @return Liefert die Zeichen für den Start einer neuen Zeile (z.B. '\n' oder '\r\n')
     */
    public String getLineSeparator(){
        return System.getProperty("line.separator");
    }

    /**
     * @return Liefert die Java Version
     */
    public String getJavaVersion(){
        return System.getProperty("java.version");
    }

    /**
     * @return Liefert den Pfad zum temporären Verzeichnis (JakeLogger etc.)
     */
    public String getTempPath(){
        return System.getProperty("java.io.tmpdir");
    }
}
