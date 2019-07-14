package de.eagle.gepard.modules;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

// Information:
// Alle "Entspricht ..." Kommentare gilt es zu ändern!

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class NameMaps {
    /**
     * Name der NameMap-Box
     */
    public static final String box_name = "nmap";

    /**
     * @return die Blaupause für die Einstellungen
     */
    public static Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> NameMaps",true, new HashMap<>());
            // ---- Mandatories
            blueprint.put("name", SettingDeskriptor.create("name", "Interner Name des Mappings", eSetting_Type.IS_TEXT, true));
            blueprint.put("patterns", SettingDeskriptor.create("patterns", "Komma-separierte Liste der Patterns, Strings werden akzeptiert", eSetting_Type.IS_TEXT, true));
                /* ZUDEM: ALLE in settings erlaubten Einstellungen können 
     * angefügt werden. Sie werden dann gesetzt, wenn das 
     * entsprechende Pattern anschlägt.
     */
        }
        return blueprint;
    }

    /**
     * Entspricht expandables Settings
     */
    private static Settings blueprint = null;


    /**
     * Entspricht get_default_expandables
     *
     * @return Standardeinstellungen
     */
    public static Settings getDefaults() {
        Settings settings = new Settings("<Default> NameMaps", true, new HashMap<>());

    /* cSpell:disable */
    // Semster 4, aber doch 2 ^^
        settings.emplace("PDP", "Erkannte Vorlesung: PDP", eSetting_Type.IS_SPECIAL, "pdp,PdP,PDP,[Pp]aradigmen[\\ \\-]?([Dd]er[\\ \\-]?)?[Pp]rogrammierung:lilly-vorlesung=PDP\nlilly-semester=2");

    // Semster: 2
        settings.emplace("GDBS", "Erkannte Vorlesung: GDBS", eSetting_Type.IS_SPECIAL, "gdbs,GdBS,GDBS,[Gg]rundlagen[\\ \\-]?([Dd]er[\\ \\-]?)?[Bb]etriebssysteme:lilly-vorlesung=GDBS\nlilly-semester=2");

        settings.emplace("ANA1", "Erkannte Vorlesung: ANA1", eSetting_Type.IS_SPECIAL, "ana1,ANA1,[Aa]nalysis[\\ \\-]?1:lilly-vorlesung=ANA1\nlilly-semester=2");

        settings.emplace("PVS", "Erkannte Vorlesung: PVS", eSetting_Type.IS_SPECIAL, "pvs,PvS,PVS,[Pp]rogrammierung[\\ \\-]?([Vv]on[\\ \\-]?)?[Ss]ystemen:lilly-vorlesung=PVS\nlilly-semester=2");

    // Semster: 1
        settings.emplace("GDRA", "Erkannte Vorlesung: GDRA", eSetting_Type.IS_SPECIAL, "[Gg][Dd][Rr][Aa],[Gg]rundlagen[\\ \\-]?([Dd]er[\\ \\-]?)?[Rr]echnerarchitektur:lilly-vorlesung=GDRA\nlilly-semester=1");

        settings.emplace("EIDI", "Erkannte Vorlesung: EIDI", eSetting_Type.IS_SPECIAL, "[Ee][Ii][Dd][Ii],[Ee]inführung[\\ \\-]?([Ii]n[\\ \\-]?)?([Dd]ie[\\ \\-]?)?[Ii]nformatik:lilly-vorlesung=EIDI\nlilly-semester=1");

        settings.emplace("FG", "Erkannte Vorlesung: FG", eSetting_Type.IS_SPECIAL, "[Ff][Gg],[Ff]ormale[\\ \\-]?[Gg]rundlagen:lilly-vorlesung=FG\nlilly-semester=1");

        settings.emplace("LA", "Erkannte Vorlesung: LA", eSetting_Type.IS_SPECIAL, "LA,LAII,[Ll]ineare[\\ \\-]?[Aa]lgebra:lilly-vorlesung=LAII\nlilly-semester=1");

    // Dokumenttypen
        settings.emplace("Übungsblatt", "Erkannter Dokumenttyp: Übungsblatt", eSetting_Type.IS_SPECIAL, "UB,uebungsblatt,[Üü]bungsblatt,ÜB:lilly-modes=uebungsblatt");
    /* cSpell:enable */
        return settings;
    }

    /**
     * Bearbeitet eine komplette Datei
     *
     * @param rulefiles die Liste der rulefiles
     * @return Einstellungen an denen die NameMaps angewendet sind
     * @throws IOException Im Falle eines Fails von
     *                     {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public static Settings getNameMaps(String rulefiles) throws IOException {
        if (rulefiles.isEmpty())
            return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        return parseRules(gp.parseFile(NameMaps.box_name, NameMaps.getBlueprint(), true));
    }

    /**
     * Entspricht dem Parse Teil für NameMaps (foreach)
     *
     * @param boxes    Array aller gefundenen Boxen
     *
     * @implNote Die MetaInformationen sind jeweils in brief kodiert.
     *
     * @see AbstractSettings#softJoin(AbstractSettings)
     * @see AbstractSettings#hardJoin(AbstractSettings)
     *
     * @return Einstellungen der NameMaps konfiguriert sind
     */
    public static Settings parseRules(GeneratorParser.JObject[] boxes) {
        Settings ret = new Settings("NameMaps Settings");
        ret.softJoin(getDefaults());
        for (GeneratorParser.JObject box : boxes) {
            ret.softJoin(parseBox(box));
        }
        return ret;
    }

    /**
     * Parst eine einzelne Box
     * 
     * @param box      die Box
     *
     * @return null, wenn es keine NameMap-Box war, sonst die entsprechende
     *         Einstellung
     */
    public static Settings parseBox(GeneratorParser.JObject box) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "NameMaps");

        Settings settings = new Settings(box.getName());

        StringBuilder pat = new StringBuilder();
        pat.append(box.config.getValue("patterns")).append(":");
        writeLoggerDebug3("Catched [" + box.config.getName() +  "]: " + pat.toString(),"NameMaps");

        // Füge nun die Anzuwendenden Einstellungen hinzu:
        for(var s: box.config){
            if(s.getKey().equals("name") || s.getKey().equals("patterns")) 
                continue; // Pass already catched
            pat.append(s.getKey()).append("=").append(s.getValue()).append("\n");
            writeLoggerDebug3("Adding config want: " + s.getKey() + " for: " + s.getValue() + " @source: " + box.config.getValue("name"), "NameMaps");   
        }

        settings.put(box.config.getValue("name"),
                SettingDeskriptor.create("", "NameMap für Konfiguration (Gepard)", pat.toString()));

        writeLoggerDebug2("Written NMap: @" + box.config.getValue("name") + " " + pat.toString(), "NameMaps");
        return settings;
    }


    public static String[] splitCSV(String str) {
        Matcher m = Pattern.compile("(\"[^\"]*\"|[^,\"]+)").matcher(str);
        ArrayList<String> matches = new ArrayList<>();
        while(m.find()) {
            matches.add(m.group());
        }
        return matches.toArray(new String[0]);
    }

    public static Settings whatTrigger(Settings rules, String sequence) {
        writeLoggerDebug2("Suche Trigger: \"" + sequence + "\"", "NameMaps");
        Settings settings = new Settings("NameMap Trigger");
        for (var sd : rules) {
            String val = sd.getValue().getValue();
            for(String reg : splitCSV(val.substring(0,val.indexOf(":")))) {
                writeLoggerDebug3("Bearbeite: " + reg + " für " + sd.getKey() + " mit pl: " + val.replaceAll("\n","#newline#"), "NameMaps");
                Matcher m = Pattern.compile(reg).matcher(sequence);
                if(m.find()) {
                    writeLoggerDebug2("Treffer für: " + reg + " PL: " + val.replaceAll("\n","#newline#"), "NameMaps");
                    settings.put(sd.getKey(), SettingDeskriptor.create("", "Von NameMaps beeinflusste Einstellung", val.substring(val.indexOf(":")+1)));
                    writeLoggerDebug2("Einstellung @" + sd.getKey() + " nun konfiguriert als: " + settings.get(sd.getKey()).getValue().replaceAll("\n","#newline#"), "NameMaps");
                }
            }
        }
        return settings;
    }
}
