#ifndef _J_TOKENIZER_HPP_
#define _J_TOKENIZER_HPP_ 10008

/**
 * @file j_Tokenizer.hpp
 * @author Florian Sihler
 * @brief liefert den grundlegenden Tokenizer
 */

#include <iostream>
#include <fstream>
#include <istream>
#include <regex>
#include <string>
#include <vector>
#include <stdexcept>

#include "../core/j_Debug.hpp"

#include "../core/j_Definitions.hpp"
#include "../core/j_Typedefs.hpp"

/** 
 * @class Tokenizer
 * @author Florian Sihler
 * @version 1.0.0
 * 
 * @brief Generelle Klasse, die für alle Lader einen Regex-Parser zur Verfügung stellt
 */

class Tokenizer {
public:
    /**
     * @struct Match
     * 
     * @brief Enthält alle notwendigen Informationen zu einem gefunden Matching
     */
    struct Match {
        /**
         * @brief Konstruktor
         * 
         * @param m Matchings ([0] = entire, [1] = First Group, ...)
         * @param o original
         * @param s stripped Match
         * 
         */
        Match(std::vector<std::string> m, const std::string& o, const std::string s) :
            matchings(m), original(o), stripped(s) {} 

        /// @brief enthält die Treffer
        std::vector<std::string> matchings;

        /// @brief die originale Sequenz sowie ihr stripped pendant
        const std::string& original, stripped;

        /// @returns Ist der Match valide?
        inline bool failure( void ) { return this->stripped=="";}
    };

    /**
     * @brief Konstruiert den Tokenizer auf einem normalen InputStream
     * 
     * @param input_path der Pfad zur Datei
     * @param pattern Token-Selector
     */
    Tokenizer(const std::string& input_path, const std::string& pattern = R"(^ *([a-zA-Z0-9_\-äöüßÄÖÜ]+(?: [a-zA-Z0-9_\-äöüÄÖÜß]+)*) *(=) *([a-zA-Z0-9_\-äöüÄÖÜß]+(?: [a-zA-Z0-9_\-äöüÄÖÜß]+)*))");

    /**
     * @brief Konstruiert den Tokenizer auf einem normalen InputStream
     * 
     * @param input der zugrunde liegende Input Stream
     * @param pattern Token-Selector
     * @param skip_lines Wie viele Zeilen sollen zu Beginn übersprungen werden
     * @param skipper Zeichen für einen Kommentar
     * @param eol Welches Zeichen beendet eine Zeile?
     */
    Tokenizer(std::istream& input,
              const std::string& pattern = R"(^ *([a-zA-Z0-9_\-äöüßÄÖÜ]+(?: [a-zA-Z0-9_\-äöüÄÖÜß]+)*) *(=) *([a-zA-Z0-9_\-äöüÄÖÜß]+(?: [a-zA-Z0-9_\-äöüÄÖÜß]+)*))", //Danke ECMAScript: ^([^=]*)(?<! ) *(=) *([^=\n]*)(?<! )
              unsigned int skip_lines = 0,
              const std::string& skipper = R"(![^!]*!)", // Matches ! Hallo ! na du !Wie gehts?! correct
              const char eol = '\n');
             //{_input = &input;}
    /**
     * @brief Lädt die nächste Zeile und liefert 0 wenn dies fehlschlägt
     * 
     * @brief 0 wenn es keine Zeile mehr gibt, oder nur noch Kommentare
     */
    status_t loadNext( void );

    /**
     * @brief Liefert die nächste Zeile.
     * 
     * @returns einen dementsprechend konfigurierten Match
     */
    Tokenizer::Match get( void );

/* =============================== */
/*            Helper               */
/* =============================== */

    /**
     * @brief Hilfsfunktion, entfernt Kommentare aus einer Zeile
     * 
     * @param inp Der zu bearbeitende String
     * @param skipper der zu verwendende Skipper
     * 
     * @returns einen bearbeiteten String
     */
    inline static const std::string erase_skipper(const std::string& inp, const std::regex& skipper = std::regex(R"(![^!]*!)")){
        return std::regex_replace(inp, skipper, ""); 
    }

    /**
     * @brief Hilfsfunktion, entfernt Kommentare aus einer Zeile
     * 
     * @param inp Der zu bearbeitende String
     * 
     * @returns einen bearbeiteten String
     */
    inline const std::string erase_comments(const std::string & inp) {
        return Tokenizer::erase_skipper(inp, this->_skipper);
    }

    /**
     * @brief Liefert die aktuell geladene Zeile
     * 
     * @returns Naja, keine Überraschung jetzt oder?
     */
    inline std::string get_current_line( void ) { return this->_current_line; }

protected:
    std::string _current_line, _current_original;
    
    std::ifstream _ifs; // only used if tokenizer is using own fs
    
    std::istream* _input;
    std::regex _pattern;
    std::regex _skipper;
    const char _eol;
};



#endif
