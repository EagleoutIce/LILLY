#ifndef _J_FUNCTIONS_HPP_
#define _J_FUNCTIONS_HPP_ 10007


/**
 * @file j_Functions.hpp
 * @author Florian Sihler
 * @version 1.0.7
 * 
 * @brief Enthält alle Funkionen die über die Konsole aufgerufen werden können
 */


#include <iostream>
#include <fstream>
#include <sstream>

#include <experimental/filesystem>

#include "../core/j_Typedefs.hpp"
#include "../core/j_Globals.hpp"
#include "../core/j_Settings.hpp"
#include "../core/j_Definitions.hpp"

#include "../j_Helper.hpp"

#include "../installer/j_ins_Linux.hpp"

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

#endif
