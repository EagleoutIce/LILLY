#if not (defined(__linux__) || defined(__APPLE__) || defined(__MACH__))
    #warning Bisher beherrscht Jake nur Betriebssysteme die der POSIX-Implementation (spezifisch: Linuxioide und MacOS) genügen!
#endif

/**
 * @file jake.cpp
 * @author Florian Sihler
 * @version 1.0.7
 * @warning Die Aktuelle Version ist WIP einige Funktionen sind noch nicht implementiert
 *
 *
 * @brief Hilfsprogramm im Umgang mit LILLY
 *
 * @note Zum Kompilieren dieser Datei sollte das beiliegende Makefile verwendet werden
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
 *  @section Version108 Version 1.0.8 -- TODO
 *      @todo continue changelog
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

#include "jake_files/provider/j_Generator_Parser.hpp"
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

    if(!ld_settings(argc-1, argv)) 
        in_settings(argv[0]);
    //fkt_help(argv[0]);
    /* GeneratorParser gp(settings["file"]);

    settings_t mama_config = {
        {"Hallo", "mallo"},
        {"Gallo", "Schelt"}
    };

    std::vector<GeneratorParser::jObject> ret = gp.parseFile("Mama", mama_config);

    for (GeneratorParser::jObject a : ret){
        std::cout << "Found: " << a.name << " Hallo=" << a.configuration["Hallo"] << std::endl;
    }
    */

    return EXIT_SUCCESS;
}

/* ================================================================================================================== */        /* ################# */
/* Makefiledump                                                                                                       */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/*
## Makefile for jake.cpp

EXTRA_FILES := jake_files/j_Helper.cpp \
	       jake_files/cmd_line/j_Functions.cpp \
	       jake_files/cmd_line/j_Autocomplete.cpp \
	       jake_files/installer/j_ins_Linux.cpp \
	       jake_files/installer/j_ins_MacOS.cpp \
	       jake_files/core/j_Settings.cpp \
	       jake_files/cmd_line/j_Parser.cpp \
	       jake_files/core/j_Globals.cpp \
	       jake_files/provider/j_Tokenizer.cpp \
	       jake_files/provider/j_Configurator.cpp \
	       jake_files/provider/j_Generator_Parser.cpp \
	       jake_files/provider/box_profiles/j_buildrules.cpp \
	       jake_files/provider/general_profiles/j_hooks.cpp \
	       jake_files/provider/general_profiles/j_nmaps.cpp

LINKERS :=
RC_FILES := "${HOME}/.zshrc" "${HOME}/.bashrc" "${HOME}/.bash_profile"

DEBUG := 0

WRITE_RC = if grep -q "LILLY_CODE" "$(1)"; then \
        echo "DEBUG: Already prepped $(1)";\
    else \
        echo "export PATH=\044PATH:$(shell pwd); autoload bashcompinit &>/dev/null; bashcompinit &>/dev/null; source $(shell pwd)/_jake_autocomplete \#LILLY_CODE"  >> $(1);\
    fi

UNAME_S := $(shell uname -s)

default: clean no_link
		@for path in $(RC_FILES); do if [ -f $$path ]; then $(call WRITE_RC,$$path); fi; done
		@if [ "$(shell which lilly_jake)" = "" ]; then echo "\033[31mBitte starte nun dein Terminal neu um auf lilly_jake zugreifen zu können!\033[m"; fi;

debug: _dodebug1 default

debug2: _dodebug2 default


documentation:
	@doxygen Doxyfile

	@$(shell open docs/index.html || xdg-open docs/index.html )

no_link: jake.cpp $(EXTRA_FILES)
		@g++ -std=c++14 -I './' $(EXTRA_FILES) -DJAKE_PREPROCESS_DEBUG=$(DEBUG) jake.cpp -o lilly_jake $(LINKERS)

		@chmod +x ./lilly_jake
		@echo Compiled lilly_jake
clean:
		@$(foreach path,$(RC_FILES),if [ -f $(path) ] ; then echo "Lösche Lilly in: \"$(path)\" (backup: \"$(path).bak\")" && sed -i'.bak' '/\#LILLY_CODE/d' $(path); fi;)

_dodebug1:
	$(eval DEBUG=1)

_dodebug2:
	$(eval DEBUG=2)

.PHONY: default no_link clean debug debug2 _dodebug1 _dodebug2
*/
