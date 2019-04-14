#ifndef _J_PARSER_HPP_
#define _J_PARSER_HPP_ 10007

/**
 * @file j_Parser.hpp
 * @author Florian Sihler
 * @version 1.0.7
 * 
 * @brief Verwaltet das Laden der Einstellungen auf Basis der Kommandozeilenargumente
 */

#include <string>

#include "../core/j_Typedefs.hpp"
#include "../core/j_Settings.hpp"
#include "../core/j_Definitions.hpp"
#include "../j_Helper.hpp"

status_t ld_settings(int n /* = argc */, const char** argv);

#endif
