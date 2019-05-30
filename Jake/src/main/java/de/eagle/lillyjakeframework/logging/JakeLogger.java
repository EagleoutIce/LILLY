package de.eagle.lillyjakeframework.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Liefert den Logger (static)
 */
public class JakeLogger {
    /// der logger
    private static Logger LOGGER;


    /**
     * Initialisiert den Logger mit einer Temp-Datei
     */
    static void initLogger(){
        try {
            initLogger(File.createTempFile("lilly-temp-log", ".temp").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert den Logger auf einem Pfad
     * @param targetFilePath die Zieldatei
     */
    public static void initLogger(String targetFilePath){
        LOGGER = Logger.getLogger("Main Jake Log");
        getLogger().setUseParentHandlers(false);
        try {
            FileHandler fh = new FileHandler(targetFilePath);
            fh.setFormatter(new JakeFormatter());
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Erhalte die Loggerinstanz, erstelle im Zweifelsfall eine Neue (Singleton)
     */
    public static Logger getLogger(){
        if(LOGGER==null)
            initLogger();
        return LOGGER;
    }

}
