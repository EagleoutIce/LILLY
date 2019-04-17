#if not (defined(__linux__) || defined(__APPLE__) | defined(__MACH__))
    #warning Bisher beherrscht Jake nur Betriebssysteme die der POSIX-Implementation (spezifisch: Linuxioide und MacOS) genügen!
#endif

/**
 * @file jake.cpp
 * @author Florian Sihler
 * @version 1.0.7
 * @warning Die Aktuelle Version ist WIP einige Funktionnen sind noch nicht implementiert
 * 
 * 
 * @brief Hilfsprogramm im Umgang mit LILLY
 * 
 * @note Zum Kompilieren dieser Datei sollte das beiliegende Makefile verwendet werdne
 * 
 * @page Important Super stuff
 * @tableofcontents
 *
 * @section Main Wichtiger Einstieg
 * Folgende Seiten sind unter Umständen interesssant
 * - @ref Changelog
 * - @ref jake.cpp
 * 
 * @section README README.md
 * @include README.md
 * 

 * 
 * @page Changelog Changelog
 * @tableofcontents
 *  @section Version100 Version 1.0.0 -- Penguin
 *      * Command-Line-Parser
 *      * Grundlegende Funktionalität
 *      * Kommentar-Grundstruktur
 * 
 * 
 */

/* ================================================================================================================== */        /* ################# */
/* Include Direktiven                                                                                                 */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <iterator>
#include <cstdlib>

/* ================================================================================================================== */        /* ################# */
/* Preprocessor Makros                                                                                                */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/core/j_Definitions.hpp"

/* ================================================================================================================== */        /* ################# */
/* Zusätzliche Datentypen                                                                                             */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/* ================================================================================================================== */        /* ################# */
/* usings und typedefs                                                                                                */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/core/j_Typedefs.hpp"

/* ================================================================================================================== */        /* ################# */
/* Globale Variablen                                                                                                  */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/core/j_Globals.hpp"

/* ================================================================================================================== */        /* ################# */
/* Hilfsfunktionen                                                                                                    */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/j_Helper.hpp"

/* ================================================================================================================== */        /* ################# */
/* Signaturen                                                                                                         */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/cmd_line/j_Functions.hpp"


/* ################################################################################################################## */        /* ################# */
/* Implementationen                                                                                                   */        /* ################# */
/* ################################################################################################################## */        /* ################# */

/* ================================================================================================================== */        /* ################# */
/* Strukturen - Mappings                                                                                              */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/core/j_Settings.hpp"


/* ================================================================================================================== */        /* ################# */
/* Installationsregeln                                                                                                */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/installer/j_ins_Linux.hpp"


/* ================================================================================================================== */        /* ################# */
/* Operationen                                                                                                        */        /* ################# */
/* ================================================================================================================== */        /* ################# */

#include "jake_files/cmd_line/j_Parser.hpp"


/* ================================================================================================================== */        /* ################# */
/* Main                                                                                                               */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/**
 * @brief startet Jake - jippie
 * 
 * @param argc Kommandozeilenargumentanzahl
 * @param argv Kommandozeilenargumentearray
 * 
 * @returns status
 */

int main(int argc, const char** argv) {
    program = argv[0];
    
    ld_settings(argc-1, argv);
    //fkt_help(argv[0]);
    if(functions.find(settings["operation"]) != functions.end()) {                   // Operation ist valide
            functions[settings["operation"]].fkt(argv[0]);                           // Führe Operation aus
    } else {
        er_unknown_setting(("operation (=" + settings["operation"] + ")").c_str());  // Diese Operation kenne ich nicht
    }
    
    return EXIT_SUCCESS;
}

/* ================================================================================================================== */        /* ################# */
/* Makefiledump                                                                                                       */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/*
## Makefile for jake.cpp

EXTRA_FILES := jake_files/j_Helper.cpp jake_files/cmd_line/j_Functions.cpp jake_files/installer/j_ins_Linux.cpp jake_files/core/j_Settings.cpp jake_files/cmd_line/j_Parser.cpp jake_files/core/j_Globals.cpp
LINKERS := -lstdc++fs

define WRITE_RC =
	@if grep -q "#LILLY_CODE" "$(1)"; then\
		echo "DEBUG: Already prepped $(1)";\
	else\
		echo "export PATH=\044PATH:$(shell pwd)  #LILLY_CODE"  >> $(1);\
	fi
endef

default: no_link
	@#Write for zshell
	$(call WRITE_RC,"${HOME}/.zshrc")
	@#Write for bash
	$(call WRITE_RC,"${HOME}/.bashrc")
	
	@export PATH=${PATH}:$(shell pwd)

no_link: jake.cpp
	@g++ -std=c++11 -I './' $(EXTRA_FILES) jake.cpp -o lilly_jake $(LINKERS)
	@chmod +x ./lilly_jake
	@echo Compiled lilly_jake
clean:
	@sed -i '/#LILLY_CODE/d' "${HOME}/.zshrc"
	@sed -i '/#LILLY_CODE/d' "${HOME}/.bashrc"
	
.PHONY: default no_link clean
*/
