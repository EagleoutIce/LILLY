#ifndef _J_TYPEDEFS_HPP_
#define _J_TYPEDEFS_HPP_ 10008

/**
 * @file j_Typedefs.hpp
 * @author Florian Sihler
 * @version 1.0.8
 * 
 * @brief Diverse abkürzende Typdefinitionen
 */

#include <map>
#include <string>

/**
 * @struct fkt_descriptor
 * @brief Repräsentiert eine Methode der Signatur func_t mit Beschreibung
 * 
 * Wird in @ref functions_t benutzt
 */
struct fkt_descriptor;

enum set_type {
    IS_TEXT, IS_OPERATION, IS_SETTING, IS_PATH, IS_FILE, IS_SWITCH, IS_CONFIG, IS_TEXTLIST, IS_NUM, IS_VLS
};

/**
 * @struct set_descriptor
 * @brief Repräsentiert einen Einstellungskörper
 * 
 * Wird als @ref data_t verwendet
 */
struct set_descriptor{
    /// @brief der zugewiesene Wert
    std::string value = "";
    /// @brief eine kurzbeschreibung der Einstellung
    std::string brief;
    /// @brief Handelt es sich um einen switch?
    set_type type = IS_TEXT; 

    /**
     * @brief shortcut für .value.length();
     * 
     * @returns value.length()
     */
    inline size_t length(){ return value.length(); }
    /**
     * @brief shortcut für .value == (set_descriptor).value ;
     * 
     * @param rhs zu vergleichender Beschreiber
     * 
     * @returns Sind die beiden Beschreiber-Werte identisch?
     */
    inline bool operator==(const set_descriptor& rhs) { return (value == rhs.value); }
    /**
     * @brief shortcut für .value = rhs;
     * 
     * @param rhs zuzuweisender Wert
     */
    inline void operator= (const std::string& rhs) { this->value = rhs; }
    /**
     * @brief shortcut für .value += rhs;
     * 
     * @note diese Zuweisung ist Listensensitiv ein trailing space ist _nicht_ nötig
     * 
     * @param rhs zuzuweisender Wert
     */
    inline void operator+=(const std::string& rhs) { this->value += ((type==IS_TEXTLIST)?" ": "") + rhs; }
};

/// @brief Datentyp für die Einstellungen
using data_t = set_descriptor;

/// @brief Wert des Einstellungs identifikators
using settings_id_t = std::string;


/** 
 * @brief Rückabewert für Funktionen 
 * @todo Funktionalität einbauen
 */
using status_t = uint8_t;

/// @brief Funktions-Signatur für erlaubte Operationen
typedef status_t (* func_t)(const std::string&);

/// @brief Mappings-Signatur für die Einstellungen
using settings_t = std::map<settings_id_t, data_t>;

/// @brief Signatur fürs Funktionsmapping
using functions_t = std::map<std::string, fkt_descriptor>;

/// @brief Signatur für Konfigurationen
using configuration_t = std::map<std::string, std::string>; 
#endif
