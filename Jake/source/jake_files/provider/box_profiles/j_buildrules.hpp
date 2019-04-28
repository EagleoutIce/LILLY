#ifndef _J_BUILDRULES_HPP_
#define _J_BUILDRULES_HPP_ 10008

/**
 * @file j_buildrules.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Definiert die GeParD-Box 'buildrule' (genauer: NAME_BOXPROFILE_BUILDRULE)
 */


#include <map>
#include <string>
#include <vector>

#include "../../core/j_Definitions.hpp"
#include "../../core/j_Typedefs.hpp"
#include "../../core/j_Settings.hpp"

#include "../../j_Helper.hpp"

#include "../j_Generator_Parser.hpp"

/**
 * @brief Alle möglichen Einstellungen für Buildrules
 */
extern settings_t __buildrules_settings;

/// @brief Kapselt die eigentlichen Einstellungen 
extern __settings_t buildrule_settings;

/// @brief Die Standartkonfiguration für Bauregeln
extern configuration_t buildrules_default;

/**
 * @brief Liefert Regeln gemäß der Form: map[NAME] = MAKEFILETEXT
 * 
 * @param rulefiles mit ':' separierte Liste an Dateien
 * @param complete soll eine complete Regel automatisch erstellt werden?
 * 
 * @returns map aller gefundener Regeln map[NAME] = MAKEFILETEXT
 */
configuration_t getRules(const std::string& rulefiles, bool complete = false);
#endif
