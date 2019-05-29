#ifndef _J_COLOR_DEBUG_HPP_
#define _J_COLOR_DEBUG_HPP_ 10008


/**
 * @file j_color_debug.hpp
 * 
 * @brief Farbmacros für Preprocessor Debug
 * 
 * @note diese Datei hat Kinder: - ../j_Debug.hpp j_preprocessor_debug.hpp
 * 
 * Die kompatible Version dieser Datei basiert auf J_Definitions und
 * wird nur sporadisch aktualisiert um den Funktionsumfang zu erweitern.
 * Die lokale Versionsnummer findet sich hier:
 * 
 * @version 3.2.0a (mod 1.0.9j)
 * 
 * Die Semantik dieser Datei wurde von eBoard - 3.2.53e [511] abgeleitet und durch NWE 0.4d modifiziert
 * 
 */

#include <string>

#include "../j_Definitions.hpp"

#include "j_preprocessor_debug.hpp"

PREP_DEB_FULL_MSG("Eingebunden - DATEI: j_color_debug.hpp");

#define _S(s)                      std::string(s)
#define _T(s)                      std::to_string(s)

#define DEBUG_ESCAPE               _S("\033[")
#define DEBUG_SINGLE_C(c)          (DEBUG_ESCAPE+_T(c)+"m")

#define DEBUG_COLOR_FG_RGB(r,g,b)  (DEBUG_ESCAPE+_S("38;2;")+_T(r)+_S(";")+ \
                                    _T(g)+_S(";")+_T(b)+_S("m"))
#define DEBUG_COLOR_BG_RGB(r,g,b)  (DEBUG_ESCAPE+_S("48;2;")+_T(r)+_S(";")+ \
                                    _T(g)+_S(";")+_T(b)+_S("m"))

#define DEBUG_RESET                DEBUG_SINGLE_C( 0)

#define DEBUG_NORMALIZE            DEBUG_SINGLE_C(22)
#define DEBUG_NORMALIZE_2          DEBUG_SINGLE_C(23)
#define DEBUG_NO_UNDERLINE         DEBUG_SINGLE_C(24)
#define DEBUG_NO_OVERLINED         DEBUG_SINGLE_C(55)

#define DEBUG_BOLD                 DEBUG_SINGLE_C( 1)
#define DEBUG_FAINT                DEBUG_SINGLE_C( 2)
#define DEBUG_ITALIC               DEBUG_SINGLE_C( 3)
#define DEBUG_UNDERLINE            DEBUG_SINGLE_C( 4)
#define DEBUG_OVERLINED            DEBUG_SINGLE_C(53)
#define DEBUG_SLOW_BLINK           DEBUG_SINGLE_C( 5)
#define DEBUG_REVERSE              DEBUG_SINGLE_C( 7)
#define DEBUG_STRIKE               DEBUG_SINGLE_C( 9)

#define DEBUG_8BIT_FOREGROUND(c)   "\033[38;5;" + _T(c) + "m"
#define DEBUG_DEFAULT_FOREGROUND   DEBUG_SINGLE_C(39)

#define DEBUG_FG_BLACK             DEBUG_SINGLE_C(30)
#define DEBUG_FG_RED               DEBUG_SINGLE_C(31)
#define DEBUG_FG_GREEN             DEBUG_SINGLE_C(32)
#define DEBUG_FG_YELLOW            DEBUG_SINGLE_C(33)
#define DEBUG_FG_BLUE              DEBUG_SINGLE_C(34)
#define DEBUG_FG_MAGENTA           DEBUG_SINGLE_C(35)
#define DEBUG_FG_CYAN              DEBUG_SINGLE_C(36)
#define DEBUG_FG_WHITE             DEBUG_SINGLE_C(37)

#define DEBUG_8BIT_BACKGROUND(c)   "\033[48;5;" + _T(c) + "m"
#define DEBUG_DEFAULT_BACKGROUND   DEBUG_SINGLE_C(49)

#define DEBUG_BG_BLACK             DEBUG_SINGLE_C(40)
#define DEBUG_BG_RED               DEBUG_SINGLE_C(41)
#define DEBUG_BG_GREEN             DEBUG_SINGLE_C(42)
#define DEBUG_BG_YELLOW            DEBUG_SINGLE_C(43)
#define DEBUG_BG_BLUE              DEBUG_SINGLE_C(44)
#define DEBUG_BG_MAGENTA           DEBUG_SINGLE_C(45)
#define DEBUG_BG_CYAN              DEBUG_SINGLE_C(46)
#define DEBUG_BG_WHITE             DEBUG_SINGLE_C(47)

#endif

