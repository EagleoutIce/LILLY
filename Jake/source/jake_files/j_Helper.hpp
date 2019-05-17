#ifndef _J_HELPER_HPP_
#define _J_HELPER_HPP_ 10008

/**
 * @file j_Helper.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Enthält diverse Hilfsprogramme
 */


#include <string>
#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <array>

#include "core/j_Definitions.hpp"
#include "core/j_Settings.hpp"
#include "core/j_Typedefs.hpp"
#include "core/j_Globals.hpp"
#include "core/j_Debug.hpp"

/**
 * @brief Lesbarer Test ob char* in string
 *
 * @param str Der String in dem gesucht werden soll
 * @param seq Die zu suchende Sequenz
 *
 * @returns true - wenn die Sequenz enthalten ist, sonst false
 */
inline status_t in_str(const std::string& str,const char* seq){
    return (str.find(seq) != std::string::npos);
}

/**
 * @brief Lesbarer Test ob char* in char*
 *
 * @param str Der String in dem gesucht werden soll
 * @param seq Die zu suchende Sequenz
 *
 * @returns true - wenn die Sequenz enthalten ist, sonst false
 */
inline status_t in_str(const char* str,const char* seq){
    return in_str(std::string(str), seq);
}

/**
 * @brief Lesbare Ausgabe einer unbekannten Einstellung
 *
 * @param setting die Einstellung die unbekannt ist
 */
inline void er_unknown_setting(const char* setting){
    std::cerr << COL_ERROR << "Die Einstellung: \"" << setting << "\" ist so nicht gültig!" << std::endl << "Schreib \""
              << program << " help\"" << COL_RESET << std::endl;
}

/**
 * @brief Dekodiert Rückgabewerte Menschenlesbar
 *
 * @param code GNU Fehlercode
 *
 * @returns SUCCESS wenn == 0, sonst: ERROR (code)
 */
inline std::string er_decode(int code) {
    return ((code)?std::string(COL_ERROR) + "ERROR (" + std::to_string(code) +")"
                  :(std::string(COL_SUCCESS) + "SUCCESS")) + COL_RESET;
}
/**
 * @brief Fügt ähnlich iomanip's setw Leerfelder ein
 *
 * @param entry Eintrag der mit (left) pad ausgegeben werden soll
 * @param toWidth Breite auf die der Pad gesetzt werden soll
 *
 * @note Sollte toWidth <= der Länge von entry sein, so wird lediglich entry zurück gegeben!
 *
 * @returns formatierte Zeichenkette
 */
std::string padPrint(const std::string& entry, uint16_t toWidth=30);


/**
 * @brief testet ob eie Datei existiert
 *
 * @note Im gegensatz zu stat ist diese Option portabel
 *
 * @param name Der Pfad zur Datei
 *
 * @returns ob auf die Datei zugegriffen werden kann
 *
 *
 */
inline bool check_file (const std::string& name) {
    /*if(name.find(".Trash") != std::string::npos ||
       name.find("Backup") != std::string::npos ||
       name.find("backup") != std::string::npos)
        return false;*/
    std::ifstream f(name.c_str());
    return f.good();
}

/**
 * @brief Spaltet eine Zeichenketten auf Basis eines Zeichens
 *
 * @param str der zu spaltende String
 * @param delim Zeichen an dem gespalten werden soll
 *
 * @returns vector - der alle teile enthält
 */
std::vector<std::string> split(const std::string& str, char delim = ' ');

/**
 * @brief erstellt eine buildrule fürs Makefile
 *
 * @deprecated mit j_buildrules.hpp
 *
 * @param name_type Name des Typs
 * @param name_rule der Name der Regel
 * @param mode der Name des LILLY-Modi
 * @param complete soll eine complete variante erstellt werden?
 * @param name_addon Zusätzlicher Namensbezeichner
 * @param input_build Input-regel
 * @param l_comp_name should the boxname be shown in name?
 * @todo: others zusätzliche optionen
 *
 * @returns String-Repräsentation der Build-Regel
 */
std::string create_buildrule(const std::string& name_type, const std::string& name_rule,
                             const std::string& mode, bool complete=false, const std::string& name_addon=settings[S_LILLY_NAMEPREFIX],
                             const std::string& input_build=R"(\\input{$(INPUTDIR)$(TEXFILE)})",
                             const std::string& l_comp_name = settings[S_LILLY_COMPLETE_NAME]/*, const std::vector<std::string>& others*/) noexcept;
/**
 * @brief Sichert zu, dass alle Konfigurationen nicht diesen Wert haben
 *
 * @param theall alle Konfigurationen die getestet werden Sollen
 * @param thediffer der String auf den der Unterschied getestet wird
 * @param flavour Zusatz-text für die Ausgabe
 *
 * @note Wirft ohne 'debug' eine Ausnahme wenn ein String die Regel verletzt
 *
 * @returns 1 Wenn im Debug ein Fehler passiert => Verwerfe Box/assertedObject
 *
 */
status_t assert_all_differ(settings_t theall, const std::string& thediffer = "!!", const std::string& flavour = "diesen Boxtyp");

/**
 * @brief Führt einen Befehl aus und liefert das Ergebnis zurück
 *
 * @param command der Befehl
 *
 * @returns das Ergebnis
 */
std::string exec(const std::string& command);
#endif
