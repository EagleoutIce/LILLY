#ifndef _L_LILLY_HPP_
#define _L_LILLY_HPP_ 10009


/**
 * @file c_Lilly.hpp
 * @author Florian Sihler
 * @version 1.0.9
 *
 * @brief Liefert eine Klasse die die Arbeit mit LILLY erleichtern soll indem sie alles notwendige verwaltet
 * 
 * @todo implement :D
 */


#include "../core/j_Definitions.hpp"
#include "../core/j_Typedefs.hpp"


class c_Lilly {

    /**
     * @brief Repräsentiert die minimal geforderte Lilly-Version für diesen Jake-Build
     * Sie wird nach abwärtskompatibilität gesetzt und sollte auch so nicht verändert werden.
     * 
     * Es ist möglich 'MIN_SUP_VER' beim kompilieren von Jake anzugeben
     */
    const uint_least8_t min_sup_version;


public:
    
    c_Lilly(configuration_t _defaults, uint_least8_t _min_ver = _J_DEFINITIONS_HPP_) : min_sup_version(_min_ver), options(_defaults) {}
    
    
    // TODO AUTO ESCAPE: ä => \"{a}, " => \" , "\" => "\\" usw...
    
    
    /**
     * @brief Bestimmt die Ladersequenz. - TODO Variablen zur Verfügung stellen :D
     */
    std::string loader = "";

    /**
     * @brief Hält eine Liste alle der zu beginn zu setzenden Optionen, enthält nicht den loader
     */
    configuration_t options;

    /**
     * @brief Diese Funktion generiert auf Basis die entsprechende PDF-Datei
     * 
     * @param prefix_code Code, der vor dem kompilieren ausgeführt werden soll
     */
    status_t compile_document(const std::string& prefix_code);
};



#endif
