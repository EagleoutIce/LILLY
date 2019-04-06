/**
 * @file jake.cpp
 * @author Florian Sihler
 * @version 1.0.0
 * @warning Die Aktuelle Version ist WIP einige Funktionnen sind noch nicht implementiert
 * 
 * 
 * @brief Hilfsprogramm im Umgang mit LILLY
 * 
 * @note Zum Kompilieren dieser Datei sollte das beiliegende Makefile verwendet werdne
 * 
 * @mainpage
 * @section README README.md
 * @include README.md
 * 
 * Folgende Seiten sind unter Umständen interesssant
 * - @ref Changelog
 * - @ref jake.cpp
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
#include <string>
#include <map>
#include <iterator>
#include <cstdlib>

/* ================================================================================================================== */        /* ################# */
/* Preprocessor Makros                                                                                                */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/// @brief Design-Makro zum Zurücksetzen der Farbe
#define COL_RESET "\033[m"
/// @brief Design-Makro zum Setzen der Fehler-Farbe in der Konsole
#define COL_ERROR "\033[38;2;255;32;82m"
/// @brief Design-Makro zum Setzen der Erfolgs-Farbe in der Konsole
#define COL_SUCCESS "\033[38;2;102;250;0m"

/// @brief Signatur mit der ein single-Argument beginnt
#define ARG_PATTERN "-"
/// @brief Signatur einer Zuweisung
#define ASS_PATTERN "="
/// @brief Signatur einer TeX/LaTeX-Datei
#define TEX_PATTERN ".tex"

/// @brief convenience-wrapper
#define TO_DATA(x) x

/* ================================================================================================================== */        /* ################# */
/* Zusätzliche Datentypen                                                                                             */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/**
 * @struct fkt_descriptor
 * @brief Repräsentiert eine Methode der Signatur func_t mit Beschreibung
 * 
 * Wird in @ref functions_t benutzt
 */
struct fkt_descriptor;

/* ================================================================================================================== */        /* ################# */
/* usings und typedefs                                                                                                */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/// @brief Datentyp für die Einstellungen
using data_t = std::string;

/** 
 * @brief Rückabewert für Funktionen 
 * @todo Funktionalität einbauen
 */
using status_t = uint8_t;

/// @brief Funktions-Signatur für erlaubte Operationen
typedef status_t (* func_t)(const std::string&);

/// @brief Mappings-Signatur für die Einstellungen
using settings_t = std::map<std::string, data_t>;

/// @brief Signatur fürs Funktionsmapping
using functions_t = std::map<std::string, fkt_descriptor>;

/* ================================================================================================================== */        /* ################# */
/* Globale Variablen                                                                                                  */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/// @brief argv[0] - zur Einfachheit tihihihi
std::string program; 

/* ================================================================================================================== */        /* ################# */
/* Hilfsfunktionen                                                                                                    */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/**
 * @brief Lesbarer Test ob char* in char*
 * 
 * @param str Der String in dem gesucht werden soll
 * @param seq Die zu suchende Sequenz
 * 
 * @returns true - wenn die Sequenz enthalten ist, sonst false
 */
inline status_t in_str(const char* str,const char* seq){
    return (std::string(str).find(seq) != std::string::npos);
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

/* ================================================================================================================== */        /* ################# */
/* Signaturen                                                                                                         */        /* ################# */
/* ================================================================================================================== */        /* ################# */

/**
 * @brief Gibt Hilfe zur Verwendung von lilly_jake
 * 
 * @note Die möglichen Optionen werden über die Funktionen generiert
 * @todo Implementiere Rückgabewert
 * 
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden 
 * 
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_help (const std::string& cmd) noexcept;

/**
 * @brief Gibt die aktuellen Settings aus
 * 
 * @todo Implementiere Rückgabewert
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden 
 * 
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_dump(const std::string& cmd) noexcept;

/**
 * @brief Erstellt ein Makefile für die mit settings["file"] angegebene Datei
 * 
 * @todo Implementiere Funktion
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden 
 * 
 * @returns Statuswert (bisher nichts)
 */
inline status_t fkt_compile(const std::string& cmd) noexcept { /* unsupported */ }

/**
 * @brief Versucht LILLY zu installieren
 * 
 * @todo Implementiere Funktion
 *
 * @param cmd erfüllt bisher nur den Zweck der Signatur von func_t gerecht zu werden 
 * 
 * @returns Statuswert (bisher nichts)
 */
status_t fkt_install(const std::string& cmd) noexcept;

/**
 * @brief Lädt die Einstellung auf Basis der Kommandozeilen argumente
 *
 * @todo Implementiere Rückgabewert
 *
 * @param n Anzahl der Argumente die Übergeben werden
 * @param argv Array der Kommandozeilenargumente (alternativ beliebiges array)
 * 
 * @returns Statuswert (bisher nichts)
 */
status_t ld_settings(int n /* = argc */, const char** argv);


/**
 * @brief Installationsregel für Linux-Basierte Systeme - nutzt konfigurationen der settings
 * 
 * @todo Implementiere Rückgabewert
 *
 * @returns Statuswert (bisher nichts)
 */
status_t ins_linux( void );

/* ################################################################################################################## */        /* ################# */
/* Implementationen                                                                                                   */        /* ################# */
/* ################################################################################################################## */        /* ################# */

/* ================================================================================================================== */        /* ################# */
/* Strukturen - Mappings                                                                                              */        /* ################# */
/* ================================================================================================================== */        /* ################# */

struct fkt_descriptor {
    /// @brief Die Funktion die Beschrieben wird
    func_t fkt;
    /// @brief Die Funktionsbeschreibung
    std::string brief_description;
};

/// @brief Liste aller unterstützer Operationen
functions_t functions {
    {"help", {fkt_help, "Zeigt diese Hilfe an"}},
    {"dump", {fkt_dump, "Zeigt alle settings und ihre Werte an" }},
    {"file_compile", {fkt_compile, "Erstellt ein makefile für settings[\"file\"]"}},
    {"install", {fkt_install, "Versucht LILLY zu installieren"}}
};

/// @brief Speicherstruktur für Einstellungen
settings_t settings {
    {"operation", "help"},
    {"file", "none.tex"},
    {"debug", "false"},
    {"path", "./"},
    {"install-path","\"${HOME}/texmf\""},
    {"lilly-path","\"${PWD}/../../Lilly\""}
};


/* ================================================================================================================== */        /* ################# */
/* Installationsregeln                                                                                                */        /* ################# */
/* ================================================================================================================== */        /* ################# */

status_t ins_linux( void ) {
    std::cout << "  - Erstelle (" <<  settings["install-path"] << "/tex/latex): "
              << er_decode(system(("mkdir -p " + settings["install-path"] + "/tex/latex").c_str()))
              << std::endl;

    std::cout << "  - Verlinke (" << settings["lilly-path"] << ") nach (" << settings["install-path"] << "/tex/latex): "
              << er_decode(system(("ln -sf "+settings["lilly-path"]+" "+settings["install-path"]+"/tex/latex").c_str()))
              << std::endl;
    if(settings["lilly-path"]=="\"${PWD}/../../Lilly\"")
    std::cout << "    Information: es ist immer besser, wenn du den absoluten Pfad zu Lilly angibst. Nutze hierzu: " 
              << std::endl << "    jake -lilly-path=\"/pfad/zum/kuchen\" install" << std::endl;

    std::cout << "  - Informiere TEX über (" << settings["install-path"] << "): "
              << er_decode(system(("texhash " + settings["install-path"]).c_str()))
              << std::endl;
              
    std::cout << "Installationsprozess wurde abgeschlossen :D" << std::endl;
    
    return EXIT_SUCCESS;
}



/* ================================================================================================================== */        /* ################# */
/* Operationen                                                                                                        */        /* ################# */
/* ================================================================================================================== */        /* ################# */

status_t fkt_dump(const std::string& cmd) noexcept {
    std::cout << "Settings Dump: " << std::endl;
    settings_t::iterator it = settings.begin();
    while(it != settings.end()){ // iterate over all settings 
        // simple padding without <iomanip> std::setw
        std::cout << it->first << ": " << std::string(20-it->first.length(),' ')<< it->second << std::endl; 
        it++;
    }
}

status_t fkt_help(const std::string& cmd) noexcept {
    std::cerr << "Benutzung:" << std::endl << std::endl;
    std::cerr << program << " [options=help] [file]" << std::endl << std::endl;
    std::cerr << "[options]:" << std::endl;
    functions_t::iterator it = functions.begin();
    while(it != functions.end()){ // iterate over all functions 
        std::cout << "  " << it->first << " " << std::string(15-it->first.length(),' ')<< it->second.brief_description 
                  << std::endl;                                 // simple padding without <iomanip> std::setw
        it++;
    }
    std::cerr << std::endl << "[file]:" << std::endl
              << "Angabe genäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \
                  ein generelles makefile für \"xxx.tex\"."
              << std::endl;

    std::cerr << std::endl << "note:" << std::endl
              << "Allgemeine Einstellungen können über \"-key=value\" gesetzt werden (\"-key\" für boolesche). So \
                  setzt: \"-path=/es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: \
                  \"/es/gibt/kuchen\"" 
              << std::endl;
}

status_t ld_settings(int n /* = argc */, const char** argv) {
    for(; n > 0; n--) {
        if(!in_str(argv[n],ARG_PATTERN)){                       // not a single argument
            if(!in_str(argv[n],TEX_PATTERN)) {                  // not a .tex file
                settings["operation"] = TO_DATA(argv[n]);
            } else {                                            // is a .tex file
                settings["operation"] = "file_compile";
                settings["file"] = TO_DATA(argv[n]);
            }
        } else {                                                // is a single argument
            if(!in_str(argv[n], ASS_PATTERN)) {                 // not an assignment
                if(settings.find(argv[n] + 1 /* offset for '-' */) != settings.end()
                    && settings[argv[n]+1] == "false") {        // valid bool-setting
                    settings[argv[n]+1] = "true";
                } else {                                        // no valid bool-setting
                    er_unknown_setting(argv[n]+1);
                }
            } else {                                            // is an assignment
                std::string s{argv[n]+1};
                settings[s.substr(0,s.find("="))] = s.substr(s.find("=")+1);
            }
        }
    }
}

status_t fkt_install(const std::string& cmd) noexcept { 
//Determine Operating System
#if defined(__linux__)
    std::cout << "Betriebsystem wurde als Linux-Basiert identifiziert - versuche ins_linux()" << std::endl;
    ins_linux();
#else 
    std::cerr << "Es existiert keine Installationsregel für dein Betriebssystem :/ - Bitte melde dich beim Maintainer dieses Pakets!" << std::endl;
#endif
    
}


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

define WRITE_RC =
	@if grep -q "#LILLY_CODE" "$(1)"; then\
		echo "Already prepped $(1)";\
	else\
		echo "#LILLY_CODE\nexport PATH=\044PATH:$(shell pwd)"  >> $(1);\
	fi
endef

default: no_link
	@#Write for zshell
	$(call WRITE_RC,"${HOME}/.zshrc")
	@#Write for bash
	$(call WRITE_RC,"${HOME}/.bashrc")
	
	@export PATH=${PATH}:$(shell pwd)
	@chmod +x ./lilly_jake

no_link: jake.cpp
	g++ -std=c++11 -I './' jake.cpp -o lilly_jake
*/

// TODO:Prepare for github-launch
