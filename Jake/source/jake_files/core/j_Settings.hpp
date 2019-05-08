#ifndef _J_SETTINGS_HPP_
#define _J_SETTINGS_HPP_ 10008

/**
 * @file j_Settings.hpp
 * @author Florian Sihler
 * @version 1.0.8
 * 
 * @brief Die Einstellungen
 */

// Danke #DreckOS
#include <string>

#include "j_Typedefs.hpp"
#include "j_Definitions.hpp"

/**
 * @brief Generiert alle Standard-Einstellungen - Kann somit auch für Fallbacks verwendet werden
 * 
 * @returns die neuen Einstellungen
 */
settings_t generate_default_lilly_settings( void );

/**
 * @struct __settings_t
 * 
 * @brief Generelle nette kleine feine Kapselung für settings_t
 */
struct __settings_t {
    /// @brief das gekapselte - nackt und ungeschützt, hilflos daliegende gekapselte Einstellungsobjekt
    settings_t _settings;

    /**
     * @brief Konstruktor
     * 
     * @param init die Standardmäßig zu verwendeten Einstellungen
     */
    __settings_t( settings_t init =  generate_default_lilly_settings()) : _settings(init) {};

    /**
     * @brief kapselt den Map-Zugriff
     * 
     * @note Dies ist ein shortcut für get(dat).value
     * 
     * @param dat das Element welches erfragt wird
     * @returns der dem erfragten Element zugehörige Wert
     */
    inline std::string &operator[](const settings_id_t& dat) {return _settings[dat].value;} 
    /**
     * @brief kapselt den Map-Zugriff
     * 
     * @param dat das Element welches erfragt wird
     * @returns das erfragte Element
     */
    inline data_t &get(const settings_id_t& dat) {return _settings[dat];}

    /**
     * @brief Shortcut für: ._settings.begin()
     * 
     * @returns begin-iterator für die Einstellungen
     */
    settings_t::iterator begin() {return _settings.begin();}
    /**
     * @brief Shortcut für: ._settings.end()
     * 
     * @returns end-iterator für die Einstellungen (also nach letztem Element per Standard)
     */
    settings_t::iterator end() {return _settings.end();}
    /**
     * @brief Shortcut für: ._settings.find(dat)
     * 
     * @param dat - Die zu suchende Einheit
     * 
     * @returns iterator zum Element oder end() wenn nicht gefunden
     */
    settings_t::iterator find(const settings_id_t& dat) {return _settings.find(dat);}
};
extern __settings_t settings;

/// @brief Speicherstruktur für Einstellungen



#endif
