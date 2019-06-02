package de.eagle.gepard.parser;

/**
 * @file TokenMatch.java
 * @author Florian Sihler
 * @version 1.0.10
 * 
 * @brief Hilfsklasse für den Tokenizer - Repräsentiert einen Treffer
 * 
 * @see Tokenizer
 */

/**
 * repräsentiert einen leicht zu handhabenden Regex-Match
 */
public class TokenMatch {
    String[] matchings;
    String original;
    String stripped;

    /**
     * Konstruktor
     *
     * @param m Matchings ([0] = entire, [1] = First Group, ...)
     * @param o original
     * @param s stripped Match
     */
    public TokenMatch(String[] m, String o, String s) {
        this.matchings = m;
        this.original = o;
        this.stripped = s;
    }

    /**
     * @return Liefert die Matchings
     */
    public String[] getMatchings() {
        return matchings;
    }

    /**
     * @return Liefert die originale, unmodifzierte Zeichenkette
     */
    public String getOriginal() {
        return original;
    }

    /**
     * @return Liefert die von Kommentaren und anderem Firlefanz befreite 
     *         Zeichenkette
     */
    public String getStripped() {
        return stripped;
    }

    // Note for empty check: StringUtils.isblank()


    /**
     * Testet den Match auf validität
     * @return Liefert dann False, wenn stripped aber keine Matchings oder stripped und kein original angebgebn wurden
     */
    public boolean isValid() {
        return !((this.matchings.length==0 || this.original.isEmpty()) 
                    && !stripped.isEmpty());  // stripped gefüllt aber keine matchings/kein Original
    }

    /**
     * Testet ob ein Match besteht
     * @return Dann False, wenn der Match entweder nicht Valide ist und oder Leer
     */
    public boolean failure() {
        return this.stripped.isEmpty() || !isValid();
    }
}
