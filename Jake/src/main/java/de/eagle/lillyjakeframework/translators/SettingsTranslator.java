package de.eagle.lillyjakeframework.translators;

import de.eagle.util.blueprints.Translator;

import java.io.FileNotFoundException;

/**
 *
 */
public class SettingsTranslator extends Translator<String,String> {

    /**
     * Konstruiert einen Translator auf Basis eines Namens
     *
     * @param name Der Name des Translators
     */
    public SettingsTranslator(String name) throws FileNotFoundException {
        this(name,"/translators/CoreSettings.trans");
    }

    /**
     * Konstruiert einen Translator auf Basis eines Namens
     *
     * @param name Der Name des Translators
     */
    public SettingsTranslator(String name, String resourcePath) throws FileNotFoundException {
        super(name);
        this.loadData(this.getClass().getResourceAsStream(resourcePath));
    }

    /**
     * Gibt, da String to String, die Zeichenkette zurück
     * @param key Der Key, enthält keine anführenden oder endständigen Leerfelder oder Kommentare
     * @return key
     */
    @Override
    protected String stringToKey(String key) {
        return key;
    }

    /**
     * Gibt, da String to String, die Zeichenkette zurück
     * @param value Der Wert, enthält keine anführenden oder endständigen Leerfelder oder Kommentare
     * @return value
     */
    @Override
    protected String stringToValue(String value) {
        return value;
    }
}
