package de.eagle.gepard.modules;

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.datatypes.Settings;

import java.io.IOException;

// Information:
// Alle "Entspricht ..." Kommentare gilt es zu ändern!

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Buildrules {
    /**
     * Name der Buildrule-Box
     */
    public static final String box_name = "buildrule";

    /**
     * @return die Blaupause für die Einstellungen
     */
    public static Settings getBlueprint() {
        return blueprint;
    }

    /**
     * Entspricht buildrule Settings
     * Mandatory-Arguments starten leer
     */
    private static Settings blueprint = new Settings("<Blueprint> Buildrules");

    /**
     * Entspricht get_default_buildrules
     *
     * @return Standarteinstellungen
     */
    public static Settings getDefaults() {
        return new Settings("<Default> Buildrules");
    }

    /**
     * Bearbeitet eine komplette Datei
     * @param rulefiles die Liste der rulefiles (durch -TODO: settings- ':' getrennt)
     * @param complete Soll auch eine complete Variante erstellt werden?
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     * @throws IOException Im Falle eines Fails von {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public static Settings parseRules(String rulefiles, boolean complete) throws IOException {
        if(rulefiles.isEmpty()) return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        return parseRules(gp.parseFile(Buildrules.box_name,Buildrules.getBlueprint(),false), complete );
    }

    /**
     * Entspricht dem Parse Teil für Buildrules (foreach)
     * @param boxes Array aller gefundenen Boxen
     * @param complete Soll auch eine complete Variante erstellt werden?
     *
     * @implNote Die MetaInformationen sind jeweils in brief kodiert.
     *
     * @see de.eagle.util.blueprints.AbstractSettings#softJoin(AbstractSettings)
     * @see de.eagle.util.blueprints.AbstractSettings#hardJoin(AbstractSettings)
     *
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     */
    public static Settings parseRules(GeneratorParser.JObject[] boxes, boolean complete) {
        return new Settings("<Generated> Buildrules");
    }

    /**
     * Parst eine einzelne Box
     * @param box die Box
     * @param complete soll eine complete-Version erstellt werden
     *
     * @return null, wenn es keine buildrule-Box war, sonst die entsprechende Einstellung
     */
    public static Settings parseBox(GeneratorParser.JObject box, boolean complete){
        return new Settings("<Generated> Buildrules");
    }
}
