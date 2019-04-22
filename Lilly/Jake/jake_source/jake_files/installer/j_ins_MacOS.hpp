#ifndef _J_INS_MACOS_HPP
#define _J_INS_MACOS_HPP 10008

/**
 * @file j_ins_Linux.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Verwaltet die Installation für MacOS Systeme
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

#include "j_ins_Linux.hpp"

/**
 * @brief Installationsregel für MacOS Systeme - nutzt konfigurationen der settings
 *
 * @todo Implementiere Rückgabewert
 *
 * @returns Statuswert (bisher nichts)
 */
status_t ins_macOS( void );

#endif
