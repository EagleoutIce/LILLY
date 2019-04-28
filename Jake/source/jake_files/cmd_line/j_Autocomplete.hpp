#ifndef _J_AUTOCOMPLETE_HPP_
#define _J_AUTOCOMPLETE_HPP_ 10008


/**
 * @file j_Autocomplete.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Erhält bisherige Kommandozeile über what - erstellt auf der Basis eine Liste aller verfügbarer Argumente
 *
 */

#include <string>
#include <vector>

#include "../cmd_line/j_Functions.hpp"

#include "../core/j_Settings.hpp"
#include "../core/j_Typedefs.hpp"
#include "../core/j_Definitions.hpp"

#include "../j_Helper.hpp"

const std::string parse_cmd_line_autocomplete( const std::string cmd_line );

/**
 * @brief liste alle Optionen auf
 */
std::string print_options( void );
/**
 * @brief liste alle Einstellungen auf
 */
std::string print_settings( void );

#endif

