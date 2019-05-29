#ifndef _C_MAKEFILE_HPP_
#define _C_MAKEFILE_HPP_ 10009


/**
 * @file c_Makefile.hpp
 * @author Florian Sihler
 * @version 1.0.9
 *
 * @brief Generiert ein Makefile zur Dokument-Generierung
 * 
 */


#include <iostream>
#include <fstream>
#include <sstream>

#include "../core/j_Debug.hpp"

#include "../core/j_Typedefs.hpp"
#include "../core/j_Globals.hpp"
#include "../core/j_Settings.hpp"
#include "../core/j_Definitions.hpp"

#include "../provider/j_Tokenizer.hpp"
#include "../provider/j_Configurator.hpp"

#include "../provider/box_profiles/j_buildrules.hpp"
#include "../provider/general_profiles/j_hooks.hpp"
#include "../provider/general_profiles/j_nmaps.hpp"

#include "../j_Helper.hpp"

#include "../cmd_line/j_Autocomplete.hpp"
#include "../cmd_line/j_Parser.hpp"

/**
 * @brief generiert ein Makefile 
 * 
 * @param cmd der Befehl
 * 
 * @returns != 0 wenn fehler
 */
status_t c_makefile(const std::string& cmd);



#endif
