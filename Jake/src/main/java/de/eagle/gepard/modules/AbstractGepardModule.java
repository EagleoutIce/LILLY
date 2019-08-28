package de.eagle.gepard.modules;

/**
 * @file AbstractGepardModule.java
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.0.0
 *
 * @brief Stellt die Grundanforderungen an eine Gepardmodul
 * @see de.eagle.gepard.parser.GeneratorParser
 */

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.blueprints.AbstractSettings;
import de.eagle.util.datatypes.Settings;

import java.io.IOException;

/**
 * Definiert die geforderten Grundeigenschaften eines Gepardmoduls :D
 */
public abstract class AbstractGepardModule {

    /**
     * @brief der entsprechende Name der Box
     */
    protected final String boxName;

    /**
     * @brief Dürfen bei der Generierung unbekannte Einstellungen übernommen werden?
     */
    protected final boolean add_unknown;

    /**
     * @return Liefert den Identifikator der BOX
     */
    public String getBoxIdentifier() {
        return this.boxName;
    }

    /**
     * Konstruiert ein neues GepardModul
     *
     * @param boxName Namen der Box
     */
    public AbstractGepardModule(String boxName) {
        this.boxName = boxName;
        this.add_unknown = false;
    }

    /**
     * Konstruiert ein neues GepardModul
     *
     * @param boxName Namen der Box
     * @param add_unknown Sollen bei der Generierung unbekannte Einstellungen übernommen werden?
     */
    public AbstractGepardModule(String boxName, boolean add_unknown) {
        this.boxName = boxName;
        this.add_unknown = add_unknown;
    }

    /**
     * Liefert die Blaupause einer Box. Jede Einstellung entspricht einer (möglichen) Box-Option
     *
     * @return Die Blaupause der Box
     */
    public abstract Settings getBlueprint();

    /**
     * Liefert die Standardinstanzen des Moduls. So kann es möglich sein, dass auch bei der Angabe keiner Gepard-Konfiguration
     * bestimmte Boxen standardmäßig konfiguriert sind.
     *
     * @return Standard-Boxen als Konfigurationen einer meist beliebigen und interpretationsabhängigen zuordnung.
     */
    public abstract Settings getDefaults();

    /**
      * Bearbeitet eine komplette Datei
      *
      * @param rulefiles die Liste der rulefiles
      * @param addon  Ermöglicht eine Zusatzkonfiguration für das Szenario
      * @return Einstellungen die entsprechend der Boxen konfiguriert sind
      * @throws IOException Im Falle eines Fails von
      *                     {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public Settings parseRules(String rulefiles, boolean addon) throws IOException {
        if (rulefiles.isEmpty())
            return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        return parseRules(gp.parseFile(getBoxIdentifier(), getBlueprint(), add_unknown), addon);
    }

    /**
     * Entspricht dem Parse Teil für das Gepard Modul (foreach)
     *
     * @param boxes    Array aller gefundenen Boxen
     * @param complete Soll auch eine complete Variante erstellt werden?
     *
     * @implNote Die MetaInformationen sind jeweils in brief kodiert.
     *
     * @see de.eagle.util.blueprints.AbstractSettings#softJoin(AbstractSettings)
     * @see de.eagle.util.blueprints.AbstractSettings#hardJoin(AbstractSettings)
     *
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     */
    public Settings parseRules(GeneratorParser.JObject[] boxes, boolean complete) {
        Settings ret = new Settings(getBoxIdentifier() + " Settings");
        ret.softJoin(getDefaults());
        for (GeneratorParser.JObject box : boxes) {
            ret.softJoin(parseBox(box, complete));
        }
        return ret;
    }

    /**
     * Parst eine einzelne Box
     *
     * @param box      die Box
     * @param complete soll eine complete-Version erstellt werden
     *
     * @return null, wenn die Box nicht dem gesuchten Boxtyp entspricht, sonst die entsprechende
     *         Einstellung
     */
    public abstract Settings parseBox(GeneratorParser.JObject box, boolean complete);

    /**
     * Klassischer .toString()
     *
     * @return String-Repräsentation
     */
    @Override
    public String toString() {
        return "AbstractGepardModule{" +
                "boxName='" + boxName + '\'' +
                ", add_unknown=" + add_unknown +
                '}';
    }
}
