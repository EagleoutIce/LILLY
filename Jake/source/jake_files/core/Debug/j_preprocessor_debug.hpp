#ifndef _J_PREPROCESSOR_DEBUG_HPP_
#define _J_PREPROCESSOR_DEBUG_HPP_ 10008

/**
 * @file j_preprocessor_debug.hpp
 * 
 * @brief Macros für Preprocessor Debug
 * 
 * @note diese Datei hat einen Bruder! - ../j_Debug.hpp
 * 
 * Die kompatible Version dieser Datei basiert auf J_Definitions und
 * wird nur sporadisch aktualisiert um den Funktionsumfang zu erweitern.
 * Die lokale Versionsnummer findet sich hier:
 * 
 * @version 3.2.0a
 * 
 * Die Semantik dieser Datei wurde von eBoard - 3.2.53d [510] abgeleitet
 * 
 * Die hier aufgelisteten Macros werden mit JAKE_PREPROCESS_DEBUG gesteuert
 */


#include "../j_Definitions.hpp"

/*============================================================================*/
/*                                preprocessor                                */
/*============================================================================*/

#define VALUE_TO_STRING(x) #x
/* double remap */
#define VALUE(x) VALUE_TO_STRING(x)

#define PERFORM_PRAGMA(str) _Pragma(#str)

#if JAKE_PREPROCESS_DEBUG > 0
    #pragma message ("Du kompilierst Jake " VALUE(JAKE_VERSION) " mit BUILD-NBR " VALUE(_J_DEFINITIONS_HPP_))
    #define PREP_DEB_MSG(str)     PERFORM_PRAGMA(message ("" #str))
    #define PREP_MAC_MSG(mac,str) PERFORM_PRAGMA(message (" Du hast " #mac " auf " VALUE(mac) "gesetzt : " #str ))
#else 
    #define PREP_DEB_MSG(str) ;
    #define PREP_MAC_MSG(mac,str);
#endif

#if JAKE_PREPROCESS_DEBUG > 1
    #pragma ("Du hast das volle Preprocessor Level aktiviert - viel spaß beim Debugging")
    #define PREP_DEB_FULL_MSG(str)  PERFORM_PRAGMA(message ("FULL: " #str))
#else
    #define PREP_DEB_FULL_MSG(str);
#endif

#endif
