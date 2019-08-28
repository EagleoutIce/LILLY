package de.eagle.util.enumerations;

/**
 * @file eDocument_Type.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Typ eines ({@link de.eagle.util.datatypes.JakeDocument}) Dokuments
 */

import de.eagle.util.datatypes.JakeDocument;
import de.eagle.util.datatypes.FunctionDeskriptor;

/**
 * @enum eDocument_Type
 *
 * Zeigt den Typ eines {@link JakeDocument} an.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public enum eDocument_Type {
    IS_TEX("Es handelt sich um ein (La)TeX-Dokument","tex", new FunctionDeskriptor<>("checkTeX", "Testet ein TeX-Dokument auf validität", JakeDocument::checkTeX)),
    IS_CONF("Es handelt sich um eine Konfigurationsdatei", "conf", new FunctionDeskriptor<>("checkConf", "Testet ein Konfigurationsdokument auf validität", JakeDocument::checkConf)),
    IS_GEPARD("Es handelt sich um ein Gepard-Dokument", "gpd", new FunctionDeskriptor<>("checkGPD", "Testet ein Gepard-Dokument auf validität", JakeDocument::checkGPD)),
    IS_UNKNOWN("Der Dokumenttyp ist unbekannt", "", new FunctionDeskriptor<>("checkUnknown", "Testet ein unbekanntes Dokument auf validität", JakeDocument::checkUnknown));
    /// Kurzbeschreibung
    public String brief, suffix;
    public FunctionDeskriptor<JakeDocument,Boolean> checkValid;

    eDocument_Type(String brief, String suffix, FunctionDeskriptor<JakeDocument,Boolean> validityChecker) {
        this.brief = brief;
        this.suffix = suffix;
        this.checkValid = validityChecker;
    }

    /**
     * @return get Brief
     */
    public String getBrief() {
        return brief;
    }

    /**
     * @return get Suffix
     */
    public String getSuffix() {
        return suffix;
    }
}
