package de.eagle.gepard.modules;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;

import java.util.HashMap;

/**
 * @file Buildrules.java
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * @brief Gepardmodul welches Buildrules zur Verfügung stellt.
 * @see de.eagle.gepard.modules.AbstractGepardModule
 * @see de.eagle.gepard.parser.GeneratorParser
 */

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.blueprints.Translator;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

// Information:
// Alle "Entspricht ..." Kommentare gilt es zu ändern!

/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Buildrules extends AbstractGepardModule{
    /**
     * Name der Buildrule-Box
     */
    public static final String box_name = "buildrule";

    private static Buildrules tInstance = null;

    public static Buildrules getInstance() {
        if(tInstance == null)
            tInstance = new Buildrules();
        return tInstance;
    }

    /**
     * Konstruiert das GepardModul
     */
    private Buildrules() {
        super(box_name);
    }

    /**
     * Gespeicherte Blaupause, die es ermöglicht, die entsprechende Blaupause nur einmal pro Klasse
     * zu generieren.
     */
    protected static Settings blueprint = null;

    /**
     * @return die Blaupause für die Einstellungen
     */
    @Override
    public Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Buildrules");
            // ---- Mandatories
            blueprint.put("name",
                    SettingDeskriptor.create("name", "Interner Name der Regel", eSetting_Type.IS_TEXT, true));
            blueprint.put("display-name", SettingDeskriptor.create("display-name",
                    "Name der beim Generieren angezeigt wird", eSetting_Type.IS_TEXT, true));
            blueprint.put("lilly-mode", SettingDeskriptor.create("lilly-mode", "Korrespondierender \\LILLYxMODE",
                    eSetting_Type.IS_TEXT, true));

            // ---- Optionals
            blueprint.put("complete", SettingDeskriptor.create("complete",
                    "Soll eine vollständige Version erstellt werden?", eSetting_Type.IS_SWITCH, "false"));
            blueprint.put("complete-prefix", SettingDeskriptor.create("complete-prefix", "Präfix zur Unterscheidung",
                    eSetting_Type.IS_TEXT, "c_"));
            blueprint.put("lilly-complete-prefix", SettingDeskriptor.create("lilly-complete-prefix", "Complete-Präfix",
                    eSetting_Type.IS_TEXT, "COMPLETE-"));
            blueprint.put("nameprefix",
                    SettingDeskriptor.create("nameprefix", "Allgemeines Präfix für Name", eSetting_Type.IS_TEXT, ""));
            blueprint.put("lilly-loader", SettingDeskriptor.create("lilly-loader", "Lader-Sequenz für Datei",
                    eSetting_Type.IS_LATEX, "\\ignorespaces\\noindent \\input{$(INPUTDIR)$(TEXFILE)}"));
            blueprint.put("name-pattern ", SettingDeskriptor.create("name-pattern", "Allgemeines Namens-Muster",
                    eSetting_Type.IS_TEXT, "$(TEXNAME)"));
        }
        return blueprint;
    }

    public static String createRuleFromData(Settings setting, Translator<String, String> ts, String name, String mode,
            boolean complete, String mode_prefix) {
        return createRuleFromData(setting, ts, name, mode, complete, "\\\\input{$(INPUTDIR)$(TEXFILE)}", mode_prefix +
                setting.requestValue(ts, "S_LILLY_NAMEPREFIX"), setting.requestValue(ts, "S_LILLY_COMPLETE_NAME"),
                "TODO");
    }

    /**
     * Erstellt eine Regel auf Basis der Metaeigenschaften. Die Funktion kann selbst
     * autark vom Rest der Klasse benutzt werden, sollte aber nicht alleine getestet
     * werden, da ein derartiges Verfahren sehr oft Änderungen unterliegt.
     *
     * @param setting        die zu beziehenden Einstellungen
     * @param ts             der zu verwendende Translator
     * @param name           Der Name der Regel wie sie angezeigt werden soll
     * @param mode           Der von Lilly zu verwenden Modus, es stehen
     * @param complete       Soll die Option eine complete-option sein? Hierbei
     *                       handelt es sich nur um eine Shortcut-version
     * @param loaderSequence Sequenz über den die Datei geladen werden soll
     * @param nameprefix     Das Präfix, welches der Regel angehängt werden soll
     * @param completeName   Der Name im Falle einer complete-option - spielt mit
     *                       complete zusammen
     * @param namePattern    Das zugrundeliegende Name-Pattern. Dieses kann
     *                       überschrieben werden um so zum Beispiel eine ganz
     *                       eigenen Benamung vorzunehmen TODO: EXPANDABLES
     * @return Die entsprechende Sequenz fürs Laden der Datei inklusive dem Setzen
     *         der relevanten Metatdaten
     */
    public static String createRuleFromData(Settings setting, Translator<String, String> ts, String name, String mode,
            boolean complete, String loaderSequence, String nameprefix, String completeName, String namePattern) {
        // Da JavaJake keine Makefile option mehr anbietet wird hier lediglich die
        // "normale" Variante für
        // on-the-fly-compile generiert:
        return setting.requestValue(ts, "S_LILLY_OUT") + // == Sektion: Name (meta)
                nameprefix + ((complete) ? completeName : "") + "!" + // die einzelnen Sektionen
                // werden mithilfe von "!"
                // getrennt
                "\\\\providecommand{\\\\LILLYxMODE}{" + // == Sektion: Befehle
                mode + "}\\\\providecommand{\\\\LILLYxMODExEXTRA}{" + (complete ? "TRUE" : "FALSE") +
                "}!" + //
                loaderSequence + // Ladesequenz
                "!" +
                ColorConstants.COL_GOLD + "Generiere: " + ((complete) ? completeName : "") +
                name + ColorConstants.COL_RESET;
    }

    /**
     * Entspricht get_default_buildrules
     *
     * @see Buildrules#createRuleFromData(Settings, Translator, String, String, boolean, String)
     * @see Buildrules#createRuleFromData(Settings, Translator, String, String, boolean, String, String, String, String)
     *
     * @return Standardeinstellungen
     */
    @Override
    public Settings getDefaults() {
        Settings settings = new Settings("<Default> Buildrules", true, new HashMap<>());

        // Default
        settings.emplace("default", "Standard-Buildrule ohne Boni :D", eSetting_Type.IS_TEXT, createRuleFromData(
                CoreSettings.getSettings(), CoreSettings.getTranslator(), "Standard", "default", false, ""));

        // Print
        settings.emplace("print", "Druck-Buildrule ohne Boni :D", eSetting_Type.IS_TEXT,
                createRuleFromData(CoreSettings.getSettings(), CoreSettings.getTranslator(), "Druck", "print", false, 
                CoreSettings.requestValue("S_LILLY_PRINT_NAME")));

        // Übungsblatt
        settings.emplace("uebungsblatt", "Übungsblatt-Buildrule, erwartet Dokument ohne \\begin usw.",
                eSetting_Type.IS_TEXT,
                createRuleFromData(CoreSettings.getSettings(), CoreSettings.getTranslator(), "Übungsblatt", "default",
                        true,
                        "\\\\documentclass[Uebungsblatt${_C}Vorlesung=${VORLESUNG}${_C}n=${N}${_C}Semester=${SEMESTER}]{Lilly}\\\\begin{document}\\\\ignorespaces\\\\noindent \\\\input{$(INPUTDIR)$(TEXFILE)}\\\\end{document}",
                        CoreSettings.requestValue("S_LILLY_NAMEPREFIX"), "", "TODO"));
        
        // Druck-Übungsblatt
        settings.emplace("puebungsblatt", "(Druck)Übungsblatt-Buildrule, erwartet Dokument ohne \\begin usw.",
                eSetting_Type.IS_TEXT,
                createRuleFromData(CoreSettings.getSettings(), CoreSettings.getTranslator(), "Druck-Übungsblatt", "print",
                        true,
                        "\\\\documentclass[Uebungsblatt${_C}Vorlesung=${VORLESUNG}${_C}n=${N}${_C}Semester=${SEMESTER}]{Lilly}\\\\begin{document}\\\\ignorespaces\\\\noindent \\\\input{$(INPUTDIR)$(TEXFILE)}\\\\end{document}",
                        CoreSettings.requestValue("S_LILLY_NAMEPREFIX"), "", "TODO"));

        // Complete Default
        settings.emplace("c_default", "Complete Standard-Buildrule", eSetting_Type.IS_TEXT, createRuleFromData(
                CoreSettings.getSettings(), CoreSettings.getTranslator(), "Standard", "default", true,""));

        // Complete Print
        settings.emplace("c_print", "Complete Druck-Buildrule", eSetting_Type.IS_TEXT,
                createRuleFromData(CoreSettings.getSettings(), CoreSettings.getTranslator(), "Druck", "print", true,
                CoreSettings.requestValue("S_LILLY_PRINT_NAME")));
        return settings;
    }

    /**
     * Parst eine einzelne Box
     * 
     * @param box      die Box
     * @param complete soll eine complete-Version erstellt werden
     *
     * @return null, wenn es keine buildrule-Box war, sonst die entsprechende
     *         Einstellung
     */
    public Settings parseBox(GeneratorParser.JObject box, boolean complete) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Buildrules");

        Settings settings = new Settings(box.getName());

        settings.put(box.config.getValue("name"),
                SettingDeskriptor.create("", "Box-Deskriptor für Buildrule (Gepard)",
                        createRuleFromData(CoreSettings.getSettings(), CoreSettings.getTranslator(),
                                box.config.getValue("display-name"), box.config.getValue("lilly-mode"),
                                box.config.getValue("complete").equals(Definitions.S_TRUE), box.config.getValue("lilly-loader"),
                                box.config.getValue("nameprefix"), box.config.getValue("lilly-complete-prefix"),
                                box.config.getValue("name-pattern"))));
        writeLoggerDebug2("erhalten: " + settings.toString(), "Buildrules");

        return settings;
    }
}
