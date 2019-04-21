#ifndef _J_BUILDRULES_HPP_
#define _J_BUILDRULES_HPP_ 10007

#include <map>
#include <string>
#include <vector>

#include "../../core/j_Definitions.hpp"
#include "../../core/j_Typedefs.hpp"

#include "../../j_Helper.hpp"

#include "../j_Generator_Parser.hpp"

/**
 * @brief Alle möglichen Einstellungen für Buildrules
 */
extern settings_t buildrules_settings;

/// @brief Die Standartkonfiguration für Bauregeln
extern configuration_t buildrules_default;

/**
 * @brief Liefert Regeln gemäß der Form: map[NAME] = MAKEFILETEXT
 * 
 * @param rulefile die Datei aus der die Regeln geladen werden sollen
 * @param complete soll eine complete Regel automatisch erstellt werden?
 * 
 * @returns map aller gefundener Regeln map[NAME] = MAKEFILETEXT
 */
configuration_t getRules(const std::string& rulefile, bool complete = false);

#endif
