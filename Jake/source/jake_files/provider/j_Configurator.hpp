#ifndef _J_CONFIGURATOR_HPP_
#define _J_CONFIGURATOR_HPP_ 10008

/**
 * @file j_Configurator.hpp
 * @author Florian Sihler
 * @brief Dieser ermöglicht es alle Jake-Einstellungen auch über ein config-Dokument zu laden :D
 * 
 * @todo Implementieren von '-f' für Jake um Einstellungen komplett aus einer Datei zu beziehen! (Diese können dann durch nachfolgende Kommandozeilenargumente wieder überschrieben werden)
 * 
 */

#include <istream>
#include <string>
#include <memory>

#include "../core/j_Debug.hpp"
#include "../core/j_Definitions.hpp"

#include "../core/j_Typedefs.hpp"

#include "j_Tokenizer.hpp"

/**
 * @class Configurator
 * @brief Lädt eine Konfigurationsdatei für Jake und überschreibt auf dieser Basis Einstellungen
 * 
 */
class Configurator {

    ///@brief Operating Tokenizer
    std::unique_ptr<Tokenizer> _t = nullptr;

public:

    /**
     * @brief konstruiert einen neuen Konfigurator
     * 
     * @param config_file der Pfad zur Datei
     */
    Configurator (const std::string& config_file);

    /**
     * @brief konstruiert einen neuen Konfigurator
     * 
     * @param config_file input-stream der Datei/der Quelle
     */
    Configurator (std::istream& config_file);
    
    /**
     * @brief Modifiziert die Einstellungen gegeben der geladenen Daten
     * 
     * @param settings die Einstellungen, die modifiziert werden sollen.
     * @param add_unkown Wie soll mit unbekannten einstellungen verfahren werden?
     * 
     */
    status_t parse_settings(std::map<std::string, data_t>* settings, bool add_unkown=false);

};

#endif
