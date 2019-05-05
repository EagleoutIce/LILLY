#ifndef _J_DEBUG_CONTROLLER_HPP_
#define _J_DEBUG_CONTROLLER_HPP_ 10008

/**
 * @file j_Debug.hpp
 * @author Florian Sihler
 * @version 1.0.8
 *
 * @brief Macros fürs Debugging
 *
 * @note diese Datei hat einen Bruder! - ./Debug/eagle_preprocess_debug.hpp
 *
 * Hier werden die veschiedenen DEBUG-Level zur Laufzeit aufgelistet
 * Es sind 3 Stufen unterstützt die bei Bedarf beliebig erweitert werden
 * können.
 * j_Debug beruft sich auf den j_color_debug Farbstandart
 *
 * @todo include logger
 *
 * Die Kompatible Version dieser Datei basiert auf JAKE_PREPROCESS_DEBUG und
 * wird nur sporadisch aktualisiert um den Funktionsumfang zu erweitern.
 * Die lokale Versionsnummer findet sich hier:
 *
 * @version 0.1.2 -- 0.1.5
 *
 * @note stark modifiziert für Jake - A Lilly servant
 *
 * Die hier aufgelisteten Macros werden mit JAKE_PREPROCESS_DEBUG gesteuert
 */

#include <iostream>
#include <iomanip>
#include <time.h>
#include <fstream>


#include "Debug/j_color_debug.hpp"
#include "Debug/j_preprocessor_debug.hpp"

#include "../j_Helper.hpp"

#include "j_Typedefs.hpp"
#include "j_Settings.hpp"

PREP_DEB_FULL_MSG("Eingebunden - DATEI: j_Debug.hpp");

#define LOG_LEN  100
#define MSG_LEN  70
#define PAT_LEN  40

/**
 * @brief Liefert aktuelle UTC-Zeitangabe im europäisch gängigen Format
 *
 * @returns entsprechende Zeitangabe
 */
const std::string _PT( void );

#define DEBUG_FORMATF(str,len) ((str.length()>(len-3))?(str.substr(0,len-3) + "..."): (str) ) // FORMAT_FRONT
#define DEBUG_FORMAT(str,len) ((str.length()>(len-3))?  ( "..." +  str.substr(str.length()-len+3) ) : (str) ) //FORMAT _BACK
extern std::string log_path;
extern std::ofstream log_output_stream;

/**
 * @brief Generelles Debug
 *
 * @param what Die Fehlernachricht
 * @param who Wer sendet den Fehlerbericht? (locked to 10 chars)
 * @param file aktuelle Datei
 * @param line aktuelle Zeile
 * @param signature Der Fehlertyp
 * @param bgcode Hintergrundfarbe
 * @param fgcode Vordergrundfarbe
 * @param prenote Daten wie PASSED
 *
 * @returns EXIT_SUCCESS, wenn die Nachricht ausgegeben wurde, sonst EXIT_FAILURE
 */
status_t _w_debug(const std::string& what,
                        const std::string& who,
                        const std::string& file,
                         const std::string& line,
                        const std::string& signature = "INF",
                        const std::string& bgcode = "",
                        const std::string& fgcode = DEBUG_FG_CYAN,
                        const std::string& prenote = ""
                       );

//THIS IS CRAPPY BUT RESOLVE IN THE RIGHT LINE AND FILE REFERENCE

#define w_debug(what, who) _w_debug(what,who,_S(__FILE__),_T(__LINE__))
#define w_debug2(what, who, signature) _w_debug(what,who,_S(__FILE__),_T(__LINE__),signature)
#define w_debug3(what, who, signature, bgcode) _w_debug(what,who,_S(__FILE__),_T(__LINE__),signature,bgcode)
#define w_debug4(what, who, signature, bgcode, fgcode) _w_debug(what,who,_S(__FILE__),_T(__LINE__),signature,bgcode,fgcode)
#define w_debug5(what, who, signature, bgcode, fgcode, prenote) _w_debug(what,who,_S(__FILE__),_T(__LINE__),signature,bgcode,fgcode,prenote)

#define warning_debug(what, who) w_debug4(what, who, "WAR", DEBUG_COLOR_BG_RGB(250,204,000), DEBUG_COLOR_FG_RGB(000,000,000))

#define error_debug(what, who) w_debug4(what, who, "ERR", DEBUG_COLOR_BG_RGB(204,000,000), DEBUG_COLOR_FG_RGB(255,254,255));

#define pass_debug(what, who) w_debug5(what, who, "UNT", DEBUG_COLOR_BG_RGB(210,255,210), DEBUG_COLOR_FG_RGB(000,000,000), "PASSED");


/*
#if JAKE_PREPROCESS_DEBUG >  0
    #define DEB_MSG_1S(str) { \
      std::cout  << DEBUG_RESET << DEBUG_BOLD << DEBUG_COLOR_BG_RGB(240,255,240)<< \
      DEBUG_COLOR_FG_RGB(000,000,000)<< "[DL1] "<< std::left << std::setw(10)<<\
      SCOPE_NAME << ": " << DEBUG_NORMALIZE << std::left << std::setw(MSG_LEN) << str << \
      " @"<< std::right << std::setw(5) << __LINE__ <<" ~ "<< \
      DEBUG_FORMAT(_S(__FILE__),PAT_LEN) << _PT() << DEBUG_RESET <<  std::endl; }
#else
    #define DEB_MSG_1(str);
    #define DEB_MSG_1(str);
#endif
#if JAKE_PREPROCESS_DEBUG > 1
    #define DEB_MSG_2S(str) { \
      std::cout  << DEBUG_RESET << DEBUG_BOLD << DEBUG_COLOR_BG_RGB(240,220,240)<< \
      DEBUG_COLOR_FG_RGB(000,000,000)<< "[DL2] "<< std::left << std::setw(10)<<\
      SCOPE_NAME << ": " << DEBUG_NORMALIZE << std::left << std::setw(MSG_LEN) << str << \
      " @"<< std::right << std::setw(5) << __LINE__ <<" ~ "<< \
      DEBUG_FORMAT(_S(__FILE__),PAT_LEN) << _PT() << DEBUG_RESET <<  std::endl; }
#else
    #define DEB_MSG_2S(str);
#endif
#if JAKE_PREPROCESS_DEBUG > 2
    #define DEB_MSG_3S(str) { \
      std::cout  << DEBUG_RESET << DEBUG_BOLD << DEBUG_COLOR_BG_RGB(230,230,230)<< \
      DEBUG_COLOR_FG_RGB(000,000,000)<< "[DL3] "<< std::left << std::setw(10)<<\
      SCOPE_NAME << ": " << DEBUG_NORMALIZE << std::left << std::setw(MSG_LEN) << str << \
      " @"<< std::right << std::setw(5) << __LINE__ <<" ~ "<< \
      DEBUG_FORMAT(_S(__FILE__),PAT_LEN) << _PT() << DEBUG_RESET <<  std::endl; }
#else
    #define DEB_MSG_3S(str);
#endif

#if JAKE_PREPROCESS_DEBUG > 3
    #define DEB_MSG_CS(str) { \
      std::cout  << DEBUG_RESET << DEBUG_BOLD << \
      DEBUG_COLOR_BG_RGB(188,205,209)<< \
      DEBUG_COLOR_FG_RGB(000,000,000)<< "[CAL] "<< std::left << std::setw(10)<<\
      SCOPE_NAME << ": CALLED " << DEBUG_NORMALIZE << std::left << std::setw(MSG_LEN -7) \
      << str << \
      " @"<< std::right << std::setw(5) << __LINE__ <<" ~ "<< \
      DEBUG_FORMAT(_S(__FILE__),PAT_LEN) << _PT() << DEBUG_RESET <<  std::endl; }
#else
    #define DEB_MSG_CS(str);
#endif

#define DEB_WAR(str) DEB_WARS(str)
#define DEB_ERR(str) DEB_ERRS(str)

#define DEB_MSG_C(str) DEB_MSG_CS(str)

#define DEB_MSG_3(str) DEB_MSG_3S(str)
#define DEB_MSG_2(str) DEB_MSG_2S(str)
#define DEB_MSG_1(str) DEB_MSG_1S(str)
*/

#endif
