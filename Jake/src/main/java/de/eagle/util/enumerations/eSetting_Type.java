package de.eagle.util.enumerations;

/**
 * @file eSetting_Type.java
 * @author Florian Sihler
 * @version 2.0.0
 *
 * @brief Hält die Typen für Einstellungen
 * @see de.eagle.util.datatypes.Settings
 * @see de.eagle.util.blueprints.AbstractSettings
 */

import de.eagle.gepard.modules.Projects;
import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.FunctionDeskriptor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Verwaltet die Typen die eine Einstellung haben kann
 */
public enum eSetting_Type {
    IS_TEXT("Einstellung ist beliebiger Text", new FunctionDeskriptor<>("CheckText", "", eSetting_Type::CheckText)),
    IS_LATEX("Einstellung ist Latex-Code, dieser wird automatisch escaped", new FunctionDeskriptor<>("CheckText", "", eSetting_Type::CheckText)),
    IS_OPERATION("Einstellung ist eine gültige Operation", new FunctionDeskriptor<>("CheckOperation", "", eSetting_Type::CheckOperation)),
    IS_SETTING("Einstellung ist eine gültige andere Einstellung", new FunctionDeskriptor<>("CheckSetting", "", eSetting_Type::CheckSetting)),
    IS_PATH("Einstellung ist ein gültiger absoluter oder relativer Pfad", new FunctionDeskriptor<>("CheckPath", "", eSetting_Type::CheckPath)),
    IS_REL_PATH("Einstellung ist ein relativer Pfad", new FunctionDeskriptor<>("CheckPath", "", eSetting_Type::CheckPath)),
    IS_ABS_PATH("Einstellung ist ein absoluter Pfad", new FunctionDeskriptor<>("CheckPath", "", eSetting_Type::CheckPath)),
    IS_FILE("Einstellung ist eine gültige Datei", new FunctionDeskriptor<>("CheckFile", "", eSetting_Type::CheckFile)),
    IS_SWITCH("Einstellung ist ein boolescher Wert (true,false)", new FunctionDeskriptor<>("CheckSwitch", "", eSetting_Type::CheckSwitch)),
    IS_TEXTLIST("Einstellung ist eine durch ':' getrennte Liste", new FunctionDeskriptor<>("CheckText", "", eSetting_Type::CheckText)),
    IS_NUM("Einstellung ist eine Zahl", new FunctionDeskriptor<>("CheckNumeric", "", eSetting_Type::CheckNumeric)),
    IS_VLS("Demo: Einstellung ist eine Vorlesung", new FunctionDeskriptor<>("CheckText", "", eSetting_Type::CheckText)),
    IS_SPECIAL("Einstellung ist ein weiter zu verarbeitender Ausdruck", new FunctionDeskriptor<>("CheckText", "", eSetting_Type::CheckText));


    /**
     * checks if the Data is valid Text
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckText(String data){
        return true;
    }

    /**
     * checks if the Data is a valid Path
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckPath(String data){
        return true;
    }

    /**
     * checks if the Data is a valid File
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckFile(String data){
        return new File(data).isFile() || Paths.get(CoreSettings.requestValue("S_LILLY_IN"), data).toFile().isFile();
    }

    /**
     * checks if the Data is valid Operation
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckOperation(String data){
        return CoreFunctions.functions_t.containsKey(data) || Arrays.asList(Projects.getInstance().getAllProjectNames(Definitions.projects())).contains(data);
    }

    /**
     * checks if the Data is a valid Setting
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckSetting(String data){
        return CoreSettings.getSettings().containsKey(data);
    }

    /**
     * checks if the Data is a valid Switch
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckSwitch(String data){
        return data.equals(Definitions.S_FALSE) || data.equals(Definitions.S_TRUE);
    }

    /**
     * checks if the Data is a valid Number
     * @param data the Data
     * @return true if the data is valid
     */
    public static Boolean CheckNumeric(String data){
        for (char c : data.toCharArray())
            if (!Character.isDigit(c)) return false;
        return true;
    }

    /**
     * @return Brief description of Setting
     */
    public String getBrief() {
        return brief;
    }

    // Kurzbeschreibung
    public String brief;
    public FunctionDeskriptor<String,Boolean> checkValidtiy;

    public boolean isValid(String data) {
        return checkValidtiy.function.apply(data);
    }

    eSetting_Type(String brief, FunctionDeskriptor<String,Boolean> validityChecker) {
        this.brief = brief; this.checkValidtiy = validityChecker;
    }
}
