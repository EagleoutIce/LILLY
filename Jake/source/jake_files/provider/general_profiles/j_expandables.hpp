#ifndef _J_EXPANDABLES_HPP_
#define _J_EXPANDABLES_HPP_ 10009

/**
 * @file j_expandables.hpp
 * @author Florian Sihler
 * @version 1.0.9
 *
 * @brief Definiert die GeParD-Box 'expandable' (genauer: NAME_EXPANDABLE_BUILDRULE)
 * 
 * Ein Expandable ist ein Begriff, welcher analog zu Umgebungsvariablen in allen
 * Einstellungen von Jake verwendet werden kann. Er wird durch den Parser erweitert
 * Sofern er als "$(EXPANDABLE)" (oder "${EXPANDABLE}") eingebettet ist. Es findet 
 * eine MEHRFACHE Erweiterung statt. $(HALLO) => $(BALLO) wird weiter
 * aufgelöst!
 * Es gibt zusätzliche expandables mit @ welche über die Funktionsweise des Makefiles hinausgehen
 * Sie werden über @[EXPANDABLE] aufgelöst.
 */

#include <map>
#include <string>
#include <vector>

#include "../../core/j_Debug.hpp"

#include "../../core/j_Definitions.hpp"
#include "../../core/j_Typedefs.hpp"

#include "../../j_Helper.hpp"

#include "../j_Generator_Parser.hpp"

#include "../../cmd_line/j_Parser.hpp"

/**
 * @brief Alle möglichen Einstellungen für Expandables
 */
extern settings_t __expandables_settings;

/// @brief Kapselt die __expandables_settings
extern __settings_t expandables_settings;

/**
 * @brief kläglicher Versuch toll zu sein :D
 * 
 * @returns neu generiertes expandables-default;
 */
configuration_t get_default_expandables( void );

/**
 * @brief Liefert Regeln gemäß der Form: map[Name] = expandsto
 * 
 * @param rulefiles mit ':' separierte Liste an Dateien
 * 
 * @note Aus Sicherheitsgründen ist es nicht gestattet direkt auf settings zuzugreifen
 *       Jake kapselt alle notwendingen Einstellungen Analog zum MAkefile
 * 
 * @returns map aller gefundener Regeln map[Name] = expandsto
 */
configuration_t getExpandables(const std::string& rulefiles);

/**
 * @brief Erweitert die Einstellungsargumente
 *  
 * @returns zur Einfachheit die expandables
 */
configuration_t expand_Settings( void );

/**
 * @brief Erweitert die Konfiguration
 * 
 * @param config die zu erweiternde Konfiguration
 *  
 * @returns != 0 bei Fehler
 */
status_t expand_Config( configuration_t& config );


extern int rec_exp_calls;

/**
 * @brief erweitert Bestimmte Argumente (auch um Makefile-kompatibel zu bleiben) 
 * 
 * @note Arbeitet auf Basis der Expandables. Jake wird niemals shell-Befehle über
 *       ein Expandable forcieren! 
 * 
 * @param expandables die Expandables die ersetzt werden können
 * 
 * @sparam str die zu bearbeitende Sequenz
 * 
 * @returns Zeichenkette in der alle validen Expandables erweitert wurden. Invalide Sequenzen wurden bestehen gelassen!
 */
std::string expand(configuration_t& expandables, std::string str);

#endif

