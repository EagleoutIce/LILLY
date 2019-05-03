#ifndef _J_NMAPS_HPP_
#define _J_NMAPS_HPP_ 10009

/**
 * @file j_nmaps.hpp
 * @author Florian Sihler
 * @version 1.0.9
 *
 * @brief Definiert die GeParD-Box 'nmap' (genauer: NAME_NMAP_BUILDRULE)
 * 
 * Eine Name-Map ist nichts anderes wie eine Dateinamen-basierte Zuordnung
 * Sie soll es mit statischen Konfigurationsdateien ermöglichen,
 * den Schreibaufwand rapide zu vereinfachen und so auch Flags wie Author,
 * und so weiter automatisch zu konfigurieren. Die exemplarische Verwendung
 * wäre durch das Pattern "GdBS" nicht nur die Vorlesung, sondern auch das
 * Semester automatisch zu setzen. 
 * 
 * @note Diese Maps ändern nur Einstellungen die weder über die Kommando-
 *       Zeile, noch über andere Konfigurationen bereits geändert wurden.
 *       Es ist bisher auch nicht geplant ein forcieren dieser Mappings 
 *       zu ermöglichen!
 * 
 * TODO Automatische \documentclass generierung  auch für Mitschriebe!
 * 
 * TODO enforce external option - it doesn't recompile automatically on change for some reason?
 */

#include <map>
#include <string>
#include <vector>

#include "../../core/j_Debug.hpp"

#include "../../core/j_Definitions.hpp"
#include "../../core/j_Typedefs.hpp"

#include "../../j_Helper.hpp"

#include "../j_Generator_Parser.hpp"

/**
 * @brief Alle möglichen Einstellungen für Name Mappings
 */
extern settings_t __nmaps_settings;

/// @brief Kapselt die __nmaps_settings
extern __settings_t nmaps_settings;

/// @brief Die Standartkonfiguration für Mappings
extern configuration_t nmaps_default;

/**
 * @brief Liefert Regeln gemäß der Form: map[Name] = Pattern:Settings(\n)
 * 
 * @param rulefiles mit ':' separierte Liste an Dateien
 * 
 * @warning die Reihenfolge der Einstellungen kann momentan nicht garantiert werden
 *          dies sollte aber mitnichten schlimm sein, da eine saubere Definition
 *          Mehrfachangaben vermeidet
 * 
 * @note Hinzufügende Einstellungen können gemäß:  "Hallo+    =     Miau" definiert werden.
 *       Einstellungen mit einem schließenden Plus funktionieren so nicht!
 * 
 * @returns map aller gefundener Regeln map[NAME] = MAKEFILETEXT
 */
configuration_t getNameMaps(const std::string& rulefiles);

/**
 * @brief Extrahiert das Pattern stupide
 * 
 * @param src Die Quelle
 * 
 * @returns die Patterns als Vektor
 */
inline std::vector<std::string> getPatterns(const std::string& src) {
    return split(src.substr(0,src.find(":")),',');
}

/**
 * @brief liefert alle treffer (siehe -debug für spezieller Ablauf)
 * 
 * @todo sollen die Typnamen für configuration_t aufgespalten werden? sodass zum Beispiel nmap_t verwendet wird
 * `     um doppeldeutigkeiten zu vermeiden?
 * 
 * @param rules die Regeln die überprüft werden sollen - müssen nameMaps sein 
 * @param seq - die zu testende Sequenz
 * 
 * @note whatTrigger verwendet die C++-Standart-Regeximplementation (ohne Look-Arounds usw.) Dies muss beachtet werden!
 *       gibt aber gleichzeitig auch ein enormes Spektrum an Möglichkeiten für Patterns
 * 
 * @returns <leer> entsprechend zu false: niemand triggert
 */
configuration_t whatTrigger(const configuration_t& rules, const std::string& seq );

#endif

