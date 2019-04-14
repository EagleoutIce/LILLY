#ifndef _J_DEFINITIONS_HPP_
#define _J_DEFINITIONS_HPP_ 10007

/**
 * @file j_Definitions.hpp
 * @author Florian Sihler
 * @version 1.0.7
 * 
 * @brief Präprozessor Makros
 */

/// @brief Kurzbeschreibung der Aktuellen Jake-Version
#define PRG_BRIEF "Jake 1.0.7 - A Servant for Lilly"

/// @brief Design-Makro zum Zurücksetzen der Farbe
#define COL_RESET "\033[m"
/// @brief Design-Makro zum Setzen der Fehler-Farbe in der Konsole
#define COL_ERROR "\033[38;2;255;32;82m"
/// @brief Design-Makro zum Setzen der Erfolgs-Farbe in der Konsole
#define COL_SUCCESS "\033[38;2;102;250;0m"
/// @brief Design für Parameter
#define STY_PARAM "\033[2;3;51m"

/// @brief Signatur mit der ein single-Argument beginnt
#define ARG_PATTERN "-"
/// @brief Signatur einer Zuweisung
#define ASS_PATTERN "="
/// @brief Signatur einer Hinzufügenden Zuweisung
#define ASA_PATTERN "+="
/// @brief Signatur einer TeX/LaTeX-Datei
#define TEX_PATTERN ".tex"

/// @brief convenience-wrapper
#define TO_DATA(x) x

#endif
