#include "j_Autocomplete.hpp"

std::string print_settings() {
    std::string str = "";
    for(auto it = settings.begin(); it != settings.end(); ++it){
        if(it->second.type == IS_SWITCH) // boolean
            str += "-" + it->first + "\t";
        else if (it->second.type == IS_TEXTLIST){ // erlaubt +:
            str += "-" + it->first + ":\t-" + it->first + "+: \t";
        } else
            str += "-" + it->first + ":\t";
    }
    return str;
}

std::string print_options() {
    std::string str = "";
    for(auto it = functions.begin(); it != functions.end(); ++it){
        if (it->first.length()>0 && it->first[0] != HIDDEN)
            str += it->first +"\t";
    }
    return str;
}


const std::string parse_cmd_line_autocomplete( const std::string cmd_line ) {
    std::vector<std::string> a = split(cmd_line, ' ');
    std::string ret = "";

    if (a.size() <= 1) { // Gab es noch keine operatin - so schlagen wir stehts optionen for
        ret += print_options();
        std::cout << exec("ls | grep -e \".tex\" -e \".conf\"") ;
        return ret; // Zur übersicht
    }

    // TODO: suche operation

    // Erhalte typ des letzten Argument:
    // std::cout << strip_mod(a[a.size()-2].substr(1)) << std::endl; // kann nicht scheitern, da mind > 1

    if (in_str(a.back(), ":") && settings.find(strip_mod(a.back().substr(1))) != settings.end() ) { // Argument gefunden
        data_t t =  settings.get(strip_mod(a.back().substr(1)));
        switch (t.type) {
            case IS_CONFIG:
                ret =  exec("for a in $(find . -maxdepth 4 -type f | grep -e \".conf\"); do printf \"$a\t\"; done"); std::cout << "\t " << std::endl; break;
            case IS_FILE:
                ret =  exec("for a in $(find . -maxdepth 1 -type f ); do printf \"$a\t\"; done"); std::cout << "\t " << std::endl;  break;
            case IS_NUM:
                std::cout << "Erwarte: Zahl\tBitte gib eine Zahl ein!\t \tErklärung: " << t.brief << std::endl; break;
            case IS_OPERATION:
                ret += print_options(); break;
            case IS_PATH:
                ret = ""; break; // empty should force path show
            case IS_SETTING:
                ret += print_settings(); break;
            case IS_SWITCH:
                ret += "true\tfalse\t"; break;
            case IS_TEXT: case IS_TEXTLIST:
                std::cout << "Erwarte: Text\tBitte gib Text ein!\t \tErklärung: " << t.brief << std::endl; break;
            case IS_VLS:
                ret = "LAII\tEIDI\tGDRA\tFG\tKNN\tANA1\tGDBS\tPDP\tPVS\t";
                std::cout << "Erwarte: Vorlesung\tBitte gib den Bezeichner einer Vorlesung ein!\t \tErklärung: " << t.brief << std::endl;; break;
            default:
                std::cout << "Unbekannte Konfiguration\tBitte melde diesen Fehler\t " << std::endl;
        }

    } else {
        ret += print_settings();
    }


    /**
     * Ablauf:
     *  - Operation gesehen? 
     *    Nein: Auflisten .tex, .conf und operatioenen (wenn lilly-in spezifiziert: nutzen)
     *    Ja: Direkt Settings angeben
     * grade by settings? tanzen!
     */
    return ret;
}
