package de.eagle.gepard.parser;

/**
 *  repräsentiert einen leicht zu handhabenden Regex-Match
 */
public class TokenMatch {
    /**
     * Konstruktor
     *
     * @param m Matchings ([0] = entire, [1] = First Group, ...)
     * @param o original
     * @param s stripped Match
     *
     */
    TokenMatch(String[] m, String o, String s)  {
        this.matchings = m;
        this.original = o;
        this.stripped = s;
    }

    String[] matchings;
    String original;

    public String[] getMatchings() {
        return matchings;
    }

    public String getOriginal() {
        return original;
    }

    public String getStripped() {
        return stripped;
    }

    String stripped;

    boolean failure(){
        return this.stripped.isEmpty();
    }
}
