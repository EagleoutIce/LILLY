package de.eagle.util.enumerations;

/**
 * Verwaltet die Typen die eine Einstellung haben kann
 */
public enum eSetting_Type {
    IS_TEXT("Einstellung ist beliebiger Text"),
    IS_OPERATION("Einstellung ist eine gültige Operation"),
    IS_SETTING("Einstellung ist eine gültige andere Einstellung"),
    IS_PATH("Einstellung ist ein gültiger absoluter oder relativer Pfad"),
    IS_REL_PATH("Einstellung ist ein relativer Pfad"),
    IS_ABS_PATH("Einstellung ist ein absoluter Pfad"),
    IS_FILE("Einstellung ist eine gültige Datei"),
    IS_SWITCH("Einstellung ist ein boolscher Wert (true,false)"),
    IS_TEXTLIST("Einstellung ist eine durch ':' getrennte Liste"),
    IS_NUM("Einstellung ist eine Zahl"),
    IS_VLS("Demo: Einstellung ist eine Vorlesung");

    /// Kurzbeschreibung
    public String brief;

    eSetting_Type(String brief) {
        this.brief = brief;
    }
}
