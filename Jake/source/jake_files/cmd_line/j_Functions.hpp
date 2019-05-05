#ifndef _J_FUNCTIONS_HPP_
#define _J_FUNCTIONS_HPP_ 10008


/**
 * @file j_Functions.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Enthält alle Funkionen die über die Konsole aufgerufen werden können
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
#include "../provider/general_profiles/j_hooks.hpp" // implement rules for create buildrule and LILLYxCompile to attach hook
#include "../provider/general_profiles/j_nmaps.hpp"

#include "../j_Helper.hpp"

#include "../installer/j_ins_Linux.hpp"
#include "../installer/j_ins_MacOS.hpp"

#include "j_Autocomplete.hpp"
#include "j_Parser.hpp"

/**
 * @brief Gibt Hilfe zur Verwendung von lilly_jake
 *
 * @note Die möglichen Optionen werden über die Funktionen generiert
 * @todo Implementiere Rückgabewert
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_help (const std::string& cmd) noexcept;


/**
 * @brief Testet den Tokenizer
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_tokentest (const std::string& cmd);

/**
 * @brief Lädt alle Einstellungen aus "file" und arbeitet ab da normal.
 *
 * @param cmd ist hier endlich mal wichtig :D 
 *
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_config (const std::string& cmd);

/**
 * @brief Gibt die aktuellen Settings aus
 *
 * @todo Implementiere Rückgabewert
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_dump(const std::string& cmd) noexcept;

/**
 * @brief Erstellt ein Makefile für die mit settings["file"] angegebene Datei
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_compile(const std::string& cmd);

/**
 * @brief Versucht LILLY zu installieren
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_install(const std::string& cmd) noexcept;


/**
 * @brief liefert eine Liste aller Einstellungen für die Autovervollständigung zurück
 * 
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @deprecated left for debugging reasons
 * 
 * @returns Statuswert (bisher nichts)
 * 
 */
status_t fkt_gsettings(const std::string& cmd) noexcept;

/**
 * @brief liefert eine Liste aller Operationen für die Autovervollständigung zurück
 * 
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @deprecated left for debugging reasons
 * 
 * @returns Statuswert (bisher nichts)
 * 
 */
status_t fkt_goptions(const std::string& cmd) noexcept;

/**
 * @brief liefert eine Liste aller Dinge für die Autovervollständigung zurück auf Basis von settings["what"]
 * 
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 * 
 */
status_t fkt_autoget(const std::string& cmd) noexcept;

/**
 * @brief liefert eine Liste aller Operationen deren spezifikation settings["what"] entspricht
 * 
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 * 
 */
status_t fkt_get(const std::string& cmd) noexcept;


/**
 * @brief Versucht Lilly zu aktualisieren. 
 * 
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden
 *
 * @returns Statuswert (bisher nichts)
 * 
 */
status_t fkt_update(const std::string& cmd) noexcept;

/**
 * @struct fkt_descriptor
 *
 * @brief Hält die Informationen für eine Funktion
 *
 */
struct fkt_descriptor {
    /// @brief Die Funktion die Beschrieben wird
    func_t fkt;
    /// @brief Die Funktionsbeschreibung
    std::string brief_description;
};

/// @brief Liste aller unterstützer Operationen
extern functions_t functions;

/// @brief Verhindert deadly recursion bei Einstellungen
extern uint8_t RECURSIVE_CALLCOUNTER;

#endif
