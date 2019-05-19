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
#define PRG_BRIEF "Jake 1.0.8 - An intelligent Servant for Lilly"

///@brief Jake Version als Makro (const char*)
#define JAKE_VERSION "1.0.8"

/// @brief Design-Makro zum Zurücksetzen der Farbe
#define COL_RESET "\033[0m"
/// @brief Design-Makro zum Setzen der Fehler-Farbe in der Konsole
#define COL_ERROR "\033[38;2;255;32;82m"
/// @brief Design-Makro zum Setzen der Erfolgs-Farbe in der Konsole
#define COL_SUCCESS "\033[38;2;102;250;0m"
/// @brief Design für Parameter
#define STY_PARAM "\033[2;3;51m"

/// @brief Signatur mit der ein single-Argument beginnt
#define ARG_PATTERN '-'
/// @brief Signatur einer Zuweisung
#define ASS_PATTERN ":"
/// @brief Signatur einer Hinzufügenden Zuweisung
#define ASA_PATTERN "+:"
/// @brief Signatur einer TeX/LaTeX-Datei
#define TEX_PATTERN ".tex"

/// @brief hidden functions und so :D
#define HIDDEN '_'

/// @brief Signatur einer Konfigurationsdatei für Jake
#define CONF_PATTERN ".conf"
/// @brief convenience-wrapper
#define TO_DATA(x) x

/// @brief Maximum sukzessiver Einstellungsaufrufe
#define MAX_SETTINGS_REC 5

/// @brief geschätzte Größe eines Blocks
#define EXPECTED_BLOCKSIZE 75


/// @brief Name für Konfigurationsboxen für Buildregeln (c_Makefile,c_Jake)
#define NAME_BOXPROFILE_BUILDRULE "buildrule"
/// @brief Name für Konfigurationsboxen für Hooks (c_Makefile,c_Jake)
#define NAME_HOOK_BUILDRULE "hook"
/// @brief Name für Konfigurationsboxen für NMaps (c_Makefile,c_Jake)
#define NAME_NMAP_BUILDRULE "nmap"
/// @brief Name für Konfigurationsboxen für NMaps (c_Makefile,c_Jake)
#define NAME_EXPANDABLE_BUILDRULE "expandable"

// FÜR LESBARE BOXMODE KODIERUNG
/// @brief Name des Dokuments für die buildrule
#define B_NAME 0
/// @brief Extra Code für die Buildrule
#define B_EXTRA 1
/// @brief Input-Loader für die Buildrule
#define B_INPUT 2
/// @brief Text für die Buildrule
#define B_TEXT 3

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
/// @brief Bsp: Das wievielte Übunsgblatt?
#define S_LILLY_N                       "lilly-n"
/// @brief Das wievielte Semester?
#define S_LILLY_SEMESTER                "lilly-semester"
///@brief Soll der Boxname bei der Generierung angezeigt werden? dies macht nur Sinn auf false gesetzt werden wenn es nur eine Box gibt
#define S_LILLY_SHOW_BOX_NAME           "lilly-show-boxname"
/// @brief Pfad zur Buildrule-settings-Datei (wenn leer (default), dann werden die Standarddaten geladen :D )
#define S_GEPARDRULES_PATH              "gepardrule-path"
/// @brief Bsp: Loader-Path für Lilly Layout
#define S_LILLY_LAYOUT_LOADER           "lilly-layout-loader"
/// @brief Soll externalization verwendet werden?
#define S_LILLY_EXTERNAL                "lilly-external"
/// @brief Zum Setzen des Autors
#define S_LILLY_AUTHOR                  "lilly-author"
/// @brief Zum Setzen der Autoren-Mail
#define S_LILLY_AUTHORMAIL              "lilly-author-mail"
/// @brief Farbe mit der in Lilly Dokumenten per \Hcolor (und \HBcolor) eine Farbe definiert wird
#define S_LILLY_SIGNATURE_COLOR         "lilly-signatur-farbe"
/// @brief Soll Bibtex automatisch integriert werden => JA? Name der Datei angeben 
#define S_LILLY_BIBTEX                  "lilly-bibtex"
#endif
