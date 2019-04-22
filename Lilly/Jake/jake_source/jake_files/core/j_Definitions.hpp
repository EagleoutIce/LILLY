#ifndef _J_DEFINITIONS_HPP_
#define _J_DEFINITIONS_HPP_ 10008

/**
 * @file j_Definitions.hpp
 * @author Florian Sihler
 * @version 1.0.8
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
#define ARG_PATTERN '-'
/// @brief Signatur einer Zuweisung
#define ASS_PATTERN "="
/// @brief Signatur einer Hinzufügenden Zuweisung
#define ASA_PATTERN "+="
/// @brief Signatur einer TeX/LaTeX-Datei
#define TEX_PATTERN ".tex"

/// @brief convenience-wrapper
#define TO_DATA(x) x

/// @brief Maximum successiver Einstellungsaufrufe
#define MAX_SETTINGS_REC 5

/// @brief geschätzte Größe eines Blocks
#define EXPECTED_BLOCKSIZE 75


/// @brief Name für Konfigurationsboxen für Buildregeln des Makefiles
#define NAME_BOXPROFILE_BUILDRULE "buildrule"

// Setting Definitions: ======================================================================================

/// @brief Pfad an dem die Lilly.cls liegen soll
#define S_LILLY_PATH                    "lilly-path"
/// @brief Lilly output path
#define S_LILLY_OUT                     "lilly-out"
/// @brief Lilly input path
#define S_LILLY_IN                      "lilly-in"
/// @brief Lilly Namenspärfix für das generieren einer Datei
#define S_LILLY_NAMEPREFIX              "lilly-nameprefix"
/// @brief Zur Verfügung stehende Boxen
#define S_LILLY_BOXES                   "lilly-boxes"
/// @brief Zur Verfügung stehende Modi
#define S_LILLY_MODES                   "lilly-modes"
/// @brief sollen complete Varianten erstellt werden?
#define S_LILLY_COMPLETE                "lilly-complete"
/// @brief Lilly Namenspräfix für vollständige Dokumentvarianten
#define S_LILLY_COMPLETE_NAME           "lilly-complete-name"
/// @brief Lilly Namenspräfix für die Druckvariante
#define S_LILLY_PRINT_NAME              "lilly-print-name"
/// @brief Lilly cleantargets für `make clean`
#define S_LILLY_CLEANS                  "lilly-cleans"
/// @brief wie oft soll das Dokument kompiliert werden?
#define S_LILLY_COMPILETIMES            "lilly-compiletimes"
/// @brief sollen nach dem kompilieren automatisch alle Arbeitsdateien entfernt werden?
#define S_LILLY_AUTOCLEAN               "lilly-autoclean"
/// @brief um welches Vorlesungskürzel handelt es sich? Siehe Lilly Dokumentation für Gültige vertreter
#define S_LILLY_VORLESUNG               "lilly-vorlesung"
/// @brief Bsp: Das wievielste Übunsgblatt?
#define S_LILLY_N                       "lilly-n"
/// @brief Das wievielste Semster?
#define S_LILLY_SEMESTER                "lilly-semester"
/// @brief Pfad zur Buildrule-settings-Datei (wenn leer (default), dann werden die Standartdaten geladen :D )
#define S_BUILDRULES_PATH               "buildrule-path"
#endif
