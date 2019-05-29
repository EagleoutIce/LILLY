#ifndef _J_PARSER_HPP_
#define _J_PARSER_HPP_ 10008

/**
 * @file j_Parser.hpp
 * @author Florian Sihler
 * @version 1.0.8
 * 
 * @brief Verwaltet das Laden der Einstellungen auf Basis der Kommandozeilenargumente
 */

#include <iostream>
#include <string>
#include <regex>

#include "../core/j_Typedefs.hpp"
#include "../core/j_Settings.hpp"
#include "../core/j_Definitions.hpp"
#include "../core/j_Debug.hpp"

#include "../provider/j_Configurator.hpp"
#include "../provider/general_profiles/j_expandables.hpp"

#include "../j_Helper.hpp"

#include "j_Functions.hpp"

/**
 * @brief Lädt die Einstellung auf Basis der Kommandozeilen argumente
 *
 * @todo Implementiere Rückgabewert
 *
 * @param n Anzahl der Argumente die Übergeben werden
 * @param argv Array der Kommandozeilenargumente (alternativ beliebiges array)
 * 
 * @returns Statuswert
 */
status_t ld_settings(int n /* = argc */, const char** argv);

/**
 * @brief lädt die Standardkonfiguration und, wenn vorhanden, die Nutzer-Konfiguration
 * 
 * @param v0 Die Art, wie das Programm aufgerufen wurde :D (./, etc)
 * 
 * @returns Statuswert 
 */
status_t ld_config( std::string v0);

/**
 * @brief Forciert, dass die aktuellen Einstellungen interpretiert werden - Dies kann eine endlose Rekursion verursachen :D
 * 
 * @param v0 Die Art, wie das Programm aufgerufen wurde :D (./, etc)
 * 
 * @returns Statuswert 
 */
status_t in_settings( std::string v0 );

/**
 * @brief Entfernt alle modifikatoren wie '-' und ':' von einem String
 * 
 * @param str die Zeichenkette die es zu bearbeiten gilt
 * 
 * @returns den modifizierten String
 */
std::string strip_mod( const std::string& str );

#endif
