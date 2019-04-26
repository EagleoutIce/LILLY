#ifndef _J_HOOKS_HPP_
#define _J_HOOKS_HPP_ 10008

/**
 * @file j_hooks.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Definiert die GeParD-Box 'hook' (genauer: NAME_HOOK_BUILDRULE)
 */

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
extern settings_t hooks_settings;

/// @brief Die Standartkonfiguration für Bauregeln
extern configuration_t hooks_default;

/**
 * @brief Liefert Regeln gemäß der Form: map[NAME] = MAKEFILETEXT
 * 
 * @param rulefile mit ':' separierte Liste an Dateien
 * @param complete soll eine complete Regel automatisch erstellt werden?
 * @todo in_max Anzahl der laufenden Compilevorgänge for IN-RULE
 * 
 * @returns map aller gefundener Regeln map[NAME] = MAKEFILETEXT
 */
configuration_t getHooks(const std::string& rulefiles, /* not supported: */ uint8_t in_max=3);

/**
 * @brief liefert aus einer Masse an Regeln die, deren Präfix-Tag (PRE:RULENAME) dem TAG entspricht
 * 
 * @param rules die Regeln welche durchsucht werden sollen um erneutes Parsen zu vermeiden
 * @param tag der zu suchende tag
 * 
 * @returns neue map die mitglieder von tag ohne tag enthält
 */
configuration_t getTagged(configuration_t rules, const std::string& tag="PRE");

/**
 * @brief Schreibt alle Hooks die tag oder ALL entsprechend in den angegebenen Stream
 * 
 * @param out der Stream auf den geschrieben werden soll
 * @param rules die Regeln auf deren Basis die hooks gesucht werden sollen
 * @param tag der Tag der gesucht werden soll 
 * 
 * @returns EXIT_FAILURE wenn der Stream beschädigt ist.
 */
status_t writeHooks(std::ostream& out, configuration_t rules, const std::string& tag);
#endif
