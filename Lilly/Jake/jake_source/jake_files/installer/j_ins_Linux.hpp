#ifndef _J_INS_LINUX_HPP
#define _J_INS_LINUX_HPP 10008

/**
 * @file j_ins_Linux.hpp
 * @author Florian Sihler
 * @version 1.0.8
 * 
 * @brief Verwaltet die Installation für Linuxiodie (apt-basierte) Systeme
 */


#include <string>
#include <iostream>
#include <cstdio>
#include <memory>
#include <stdexcept>
#include <array>

#include "../core/j_Settings.hpp"
#include "../core/j_Typedefs.hpp"

#include "../j_Helper.hpp"

/**
 * @brief Führt einen Befehl aus und liefert das Ergebnis zurück
 * 
 * @param command der Befehl
 * 
 * @returns das Ergebnis
 */
std::string exec(const std::string& command);

/**
 * @brief Installationsregel für Linux-Basierte Systeme - nutzt konfigurationen der settings
 * 
 * @todo Implementiere Rückgabewert
 *
 * @returns Statuswert (bisher nichts)
 */
status_t ins_linux( void );

/**
 * @brief Ziegt eine generische Fehlermeldung an
 */
void li_show_error( void );

#endif
