package de.eagle.lillyjakeframework.core;

import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

import java.util.Map;
import java.util.regex.Pattern;

import static de.eagle.lillyjakeframework.core.Definitions.JAKE_VERSION;
import static de.eagle.util.logging.JakeLogger.writeLoggerInfo;

public class CoreSettings {
    public static Settings settings = null;

    /**
     * Initialisiert die Einstellungen mit den entsprechendne Standarts
     * @return true, wenn eine die Einstellungen generiert werden mussten.
     */
    public static boolean init() {
        if(settings != null) {
            writeLoggerInfo("Es wurde unnötiger weise CoreSettings::Init aufgerufen!","CoreS");
            // Dies könnte nicht gewollt sein
            return false;
        }
        writeLoggerInfo("Es werden neue Einstellungen für CoreSettings generiert (init)", "CoreS");
        settings = new Settings("<Jake> CoreSettings");
        settings.put("Version", SettingDeskriptor.create("Version","Jake-Version", eSetting_Type.IS_TEXT, JAKE_VERSION));

        // -- Hier die anderen @SerpentSaviour

        return true;
    }

    /**
     * Expandiert alle Einstellungen in dieser Zeichenfolge. Es wird gesucht nach: $<Settings> und dementsprechend
     * expandiert. So wird zum Beispiel $Version zu {@value Definitions#JAKE_VERSION}
     *
     * @note Bitte erweitern: CoreSettingsTest>_test_expand_basics
     *
     * @param str der zu expandierende String
     * @return der expandierte string
     */
    public static String expand(String str){
        init();
        for(Map.Entry<String, SettingDeskriptor<String>> s : settings){
            str = str.replaceAll("\\$"+ Pattern.quote(s.getKey()), s.getValue().getValue());
        }
        return str;
    }


}
