#ifndef _J_TYPEDEFS_HPP_
#define _J_TYPEDEFS_HPP_ 10007

/**
 * @file j_Typedefs.hpp
 * @author Florian Sihler
 * @version 1.0.7
 * 
 * @brief Diverse abkürzende Typdefinitionen
 */

#include <map>

/**
 * @struct fkt_descriptor
 * @brief Repräsentiert eine Methode der Signatur func_t mit Beschreibung
 * 
 * Wird in @ref functions_t benutzt
 */
struct fkt_descriptor;

/// @brief Datentyp für die Einstellungen
using data_t = std::string;

/** 
 * @brief Rückabewert für Funktionen 
 * @todo Funktionalität einbauen
 */
using status_t = uint8_t;

/// @brief Funktions-Signatur für erlaubte Operationen
typedef status_t (* func_t)(const std::string&);

/// @brief Mappings-Signatur für die Einstellungen
using settings_t = std::map<std::string, data_t>;

/// @brief Signatur fürs Funktionsmapping
using functions_t = std::map<std::string, fkt_descriptor>;

/// @brief Signatur für Konfigurationen
using configuration_t = std::map<std::string, std::string>; 
#endif
