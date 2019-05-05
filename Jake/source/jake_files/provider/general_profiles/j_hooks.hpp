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
 * @brief Alle möglichen Einstellungen für Hooks
 */
extern settings_t __hooks_settings;

/// @brief Kapselt die __hooks_settings
extern __settings_t hooks_settings;

/**
 * @brief kläglicher versuch toll zu sein :D
 * 
 * @returns neu generiertes hooks-default;
 */
configuration_t get_default_hooks( void );

/**
 * @brief Liefert Regeln gemäß der Form: map[TYPE:NAME] = HOOKETEXT
 * 
 * @param rulefiles mit ':' separierte Liste an Dateien
 * 
 * @returns map aller gefundener Regeln map[TYPE:NAME] = HOOKTEXT
 */
configuration_t getHooks(const std::string& rulefiles);

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
