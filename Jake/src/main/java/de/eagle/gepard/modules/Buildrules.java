package de.eagle.gepard.modules;

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Buildrules");
            // ---- Mandatories
            blueprint.put("name",         SettingDeskriptor.create("name",        "Interner Name der Regel",                    eSetting_Type.IS_TEXT, true));
            blueprint.put("display-name", SettingDeskriptor.create("display-name","Name der beim Generieren angezeigt wird",    eSetting_Type.IS_TEXT, true));
            blueprint.put("lilly-mode", SettingDeskriptor.create("lilly-mode","Korrespondierender \\LILLYxMODE",                eSetting_Type.IS_TEXT, true));

            // ---- optionals
            blueprint.put("complete", SettingDeskriptor.create("complete", "Soll eine vollständige Version erstellt werden?",   eSetting_Type.IS_SWITCH, "false"        ));
            blueprint.put("complete-prefix", SettingDeskriptor.create("complete-prefix","Präfix zur Unterscheidung",            eSetting_Type.IS_TEXT, "c_"             ));
            blueprint.put("lilly-complete-prefix", SettingDeskriptor.create("lilly-complete-prefix", "Complete-Präfix",         eSetting_Type.IS_TEXT, "COMPLETE-"      ));
            blueprint.put("nameprefix", SettingDeskriptor.create("nameprefix", "Allgemeines Präfix für Name",                   eSetting_Type.IS_TEXT,   ""             ));
            blueprint.put("lilly-loader", SettingDeskriptor.create("lilly-loader","Lader-Sequenz für Datei",                    eSetting_Type.IS_LATEX, "\\input{$(INPUTDIR)$(TEXFILE)}"  ));
            blueprint.put("name-pattern ", SettingDeskriptor.create("name-pattern", "Allgemeines Namens-Muster",                eSetting_Type.IS_TEXT, "$(TEXNAME)"));
        }
        return blueprint;
    }

    /**
     * Entspricht buildrule Settings
     * Mandatory-Arguments starten leer
     */
    private static Settings blueprint = null;


    /**
     *  Erstellt eine Regel auf Basis der Metaeigenschaften. Die Funktion kann selbst autark vom Rest
     *  der Klasse benutzt werden, sollte aber nicht alleine getestet werden, da ein derartiges Verfahren
     *  sehr oft Änderungen unterliegt.
     *
     * @param name Der Name der Regel wie sie angezeigt werden soll
     * @param rulenName Der Name der Regel innerhalb von jake (c_default etc.)
     * @param mode Der von Lilly zu verwenden Modus, es stehen
     * @param complete Soll die Option eine complete-option sein? Hierbei handelt es sich nur um eine Shortcut-version
     * @param nameprefix Das Präfix, welches allen Bezeichnern voran gestellt werden soll
     * @param loaderSequence Sequenz über den die Datei geladen werden soll
     * @param completeName Der Name im Falle einer complete-option - spielt mit complete zusammen
     * @param namePattern Das zugrundeliegende Name-Pattern. Dieses kann überschrieben werden um so zum Beispiel
     *                    eine ganz eigenen Benamung vorzunehmen
     * @return Die entsprechende Sequenz fürs Laden der Datei inklusive dem Setzen der relevanten Metatdaten
     */
    public static String createRuleFromData(String name, String rulenName, String mode, boolean complete,
                                            String nameprefix, String loaderSequence, String completeName,
                                            String namePattern) {
        // Da JavaJake keine Makefile option mehr anbietet wird hier lediglich die "normale" Variante für
        // on-the-fly-compile generiert:
        StringBuilder ret_str = new StringBuilder();
        //ret_str.append()

        return "";
    }

    /**
     * Entspricht get_default_buildrules
     *
     * @see Buildrules#createRuleFromData(String, String, String, boolean, String, String, String, String)
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
