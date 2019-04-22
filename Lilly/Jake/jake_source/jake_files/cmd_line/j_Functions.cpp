#include "j_Functions.hpp"


status_t fkt_gsettings(const std::string& cmd) noexcept {
    settings_t::iterator it = settings.begin();
    while(it != settings.end()){ 
        if(it->second=="true"||it->second=="false") // boolean
            std::cout << "-" << it->first << " \t";
        else if (it->second.length() > 0 && it->second[it->second.length()-1] == ' '){ // erlaubt +=
            std::cout << "-" << it->first << "=\t";
            std::cout << "-" << it->first << "+=\t";
        } else  {
            std::cout << "-" << it->first << "=\t";
        }
        
        ++it;
    }
    return EXIT_SUCCESS;
}

status_t fkt_goptions(const std::string& cmd) noexcept {
    functions_t::iterator it = functions.begin();
    while(it != functions.end()){ 
        if (it->first.length()>0 && it->first[0] != '_')
            std::cout << it->first << " \t"; ++it;
    }
    return EXIT_SUCCESS;
}

status_t fkt_dump(const std::string& cmd) noexcept {
    std::cout << "Settings Dump: " << std::endl
              << "Information: Die '[' ']' gehören jeweils nicht zum Wert, sie dienen lediglich der Übersicht!" << std::endl;
    settings_t::iterator it = settings.begin();
    while(it != settings.end()){ // iterate over all settings
        // simple padding without <iomanip> std::setw
        std::cout << it->first << ": " << std::string(20-it->first.length(),' ') << STY_PARAM << "[" << it->second << "]" << COL_RESET << std::endl;
        ++it;
    }
    return EXIT_SUCCESS;
}

status_t fkt_help(const std::string& cmd) noexcept {
    std::cerr << "Benutzung:" << std::endl << std::endl;
    std::cerr << program << " [options=help] [file]" << std::endl << std::endl;
    std::cerr << "[options]:" << std::endl;
    functions_t::iterator it = functions.begin();
    while(it != functions.end()){ // iterate over all functions
        if (it->first.length()>0 && it->first[0] != '_')
        std::cout << "  " << it->first << " " << std::string(15-it->first.length(),' ')<< it->second.brief_description
                  << std::endl;                                 // simple padding without <iomanip> std::setw
        ++it;
    }
    std::cerr << std::endl << "[file]:" << std::endl
              << "Angabe genäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \
                  ein generelles makefile für \"xxx.tex\"."
              << std::endl;

    std::cerr << std::endl << "note:" << std::endl
              << "Allgemeine Einstellungen können über \"-key=value\" gesetzt werden (\"-key\" für boolesche). So "
              << "setzt: \"-path=/es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: "
              << "\"/es/gibt/kuchen\". Weiter ist es möglich mit '+=' values hinzuzufügen."
              << std::endl;
    return EXIT_SUCCESS;
}

status_t fkt_install(const std::string& cmd) noexcept {
//Determine Operating System
#if defined(__linux__)
    std::cout << "Betriebsystem wurde als Linux-Basiert identifiziert - versuche ins_linux()" << std::endl;
    ins_linux();
#elif defined(__APPLE__) || defined(__MACH__)
    std::cout << "Betriebsystem wurde als MacOS identifiziert - versuche ins_macOS()" << std::endl;
    ins_macOS();
#else
    std::cerr << "Es existiert keine Installationsregel für dein Betriebssystem :/ - Bitte melde dich beim Maintainer dieses Pakets!" << std::endl;
#endif
   return EXIT_SUCCESS;
}

status_t fkt_compile(const std::string& cmd) {
    std::cout << "Generiere Makefile für Datei: " << settings["file"] << std::endl;

    //URGENT TODO:
    //check if file exists:

    std::string targetpath = settings[S_LILLY_IN] + "/" + settings["file"];

    if(!check_file(targetpath)) {
        std::cout << "Die von dir angegebene Datei: " << targetpath << " konnte nicht gefunden werden! Soll Jake sie für dich erstellen?" << std::endl
                  << "(y)es/(n)o/(c)ancel> ";
        char answer; std::cin >> answer;
        if(answer=='y'){
            std::ofstream out_texfile(targetpath, std::fstream::out);

            out_texfile << R"(%% Von Jake erstelltes Lilly-Texfile :D)" << std::endl;
            out_texfile << R"(%% TODO: implement structures)" << std::endl;

            /// @todo do stuff like: import Lilly, and give Instruction with comments about how to use and stuff..

            out_texfile.close();
        } else if (answer=='n') {
            std::cout << "Jake wird so tun als gäbe es die Datei und spielt heile Welt" << std::endl;
        } else {
            std::cout << "Abbruch!" << std::endl;
            return EXIT_FAILURE;
        }
    }



#if defined(__linux__) || defined(__APPLE__) || defined(__MACH__)
    // Generiere Notwendige Ordnerstruktur für Ein- und Ausgabedateien!
    std::cout << "    - Erstelle Ordner settings[\"mk-path\"] (" << settings["mk-path"] << ")... "
              << er_decode(system(("mkdir -p " + settings["mk-path"]).c_str())) << std::endl; // OS - BARRIER
#else
    std::cout << "!   Auf deinem Betriesbsystem ist noch keine Regel implementiert die mir es erlaubt einen Ordner zu erstellen!" << std::endl
              << "!   Es ist wichtig, dass du die Existenz aller Pfade die du benötigst selbst gewährleistest :/" << std::endl;
#endif

    std::cout << "    - Erstelle Makefile settings[\"mk-name\"] (" << settings["mk-name"] << "): " << std::endl;

    std::stringstream buf_makefile;

    buf_makefile << "# " << PRG_BRIEF << "     (compiled on: " << __DATE__ << " at: " << __TIME__ << ")" << std::endl << std::endl;

    // Einfügen der Variablen des Makefiles, es erhält eine andere Struktur wie das, welches von lilly_compile zur Verfügung gestellt wird!!
    buf_makefile << "TEXFILE      := " << settings["file"]                                                                      << std::endl
                 << "BASENAME     := $(basename $(TEXFILE))"                                                                    << std::endl
                 << "PDFFILE      := $(addsuffix .pdf,$(basename $(TEXFILE)))"                                                  << std::endl
                 << "LATEXARGS    := -shell-escape -enable-write18 -interaction=batchmode"                                      << std::endl
                 // lilly- settings
                 << "## Directories used for INPUT and OUTPUT Files "                                                           << std::endl
                 << "OUTPUTDIR    := $(shell echo " << settings[S_LILLY_OUT] << "| sed 's:/*$$::')/"                            << std::endl
                 << "INPUTDIR     := $(shell echo " << settings[S_LILLY_IN] << "| sed 's:/*$$::')/"                             << std::endl
                 << "BOXMODES     := " << padPrint(settings[S_LILLY_BOXES]+"#")      << "## Seperator: ' '"                     << std::endl
                 << "CLEANTARGET  := LILLYxClean"                                                                               << std::endl
                 << "CLEANTARGETS := " << settings[S_LILLY_CLEANS]                                                              << std::endl
                 // lilly- names
                 << "NAMEPREFIX   := " << padPrint(settings[S_LILLY_NAMEPREFIX])           << "## Immer"                        << std::endl
                 << "SEMESTER     := " << padPrint(settings[S_LILLY_SEMESTER])             << "## Übungsblatt"                  << std::endl
                 << "VORLESUNG    := " << padPrint(settings[S_LILLY_VORLESUNG])            << "## Übungsblatt"                  << std::endl
                 << "N            := " << padPrint(settings[S_LILLY_N])                    << "## Anzahl"                       << std::endl
                 << "_C           := ,                             ## No Joke xD"                                               << std::endl
                 << std::endl
                 //Generals
                 << "## Makefile/General settings"                                                                              << std::endl
                 << R"(_LILLYARGS   :=  \\providecommand{\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) \\providecommand{\\LILLYxPATH}{${INPUTDIR}})" << std::endl << std::endl //DONT'T Change
                 << "JOBCOUNT     := " << padPrint(settings["jobcount"])              << "## should: <= cpu/thread count!"      << std::endl
                 << std::endl << std::endl;

    buf_makefile << "void = return 0;  "                                                                                   << std::endl<< std::endl<< std::endl;
    //Compile-Regel
    buf_makefile << "LILLYxCompile = \\"                                                                                   << std::endl;
#if defined(__linux__)
    buf_makefile << "    mkdir -p \"$(OUTPUTDIR)\" && \\" << std::endl; // Auf windows vermutlich identisch nur ohne -p
#endif
    //clean log

    buf_makefile << R"(    touch $(OUTPUTDIR)LILLY_COMPILE.log && echo LILLY_LOGFILE stamp: $(shell date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 &&\)"                   << std::endl
                 << "    (for bm in $(BOXMODES); do \\" << std::endl;
    for(int i = 0; i < std::stoi(settings[S_LILLY_COMPILETIMES]); i++) {
        buf_makefile << "       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS)" << R"( \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}} )" << " ${3} $(_LILLYARGS) ${4}" << R"(>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \)" << std::endl;
    }
    buf_makefile << "       echo \"\\033[38;2;102;250;0mGenerierierung von \"${1}$${bm}-${2}\" ($${bm}) abgeschlossen\\033[m\"; done &&\\"                                                                                                   << std::endl;

    // call clean routine if clean is enabled :D
    if (settings[S_LILLY_AUTOCLEAN] == "true")
        buf_makefile << "    $(call $(CLEANTARGET)) ) || (";
    else 
        buf_makefile << "    echo \"Kein autoclean da zugehörige Einstellung != true\" || (";
    
    buf_makefile << "echo \"\\033[31m! Das Kompilieren mit LILLY ist fehlgeschlagen. Fehler finden sich in der entsprechendne Logdatei\\033[m\"; return 1;)"<< std::endl<< std::endl;  //) && (echo \"$(shell echo -E \"$(grep -R --include=\"*.log\" -i -E \"error[^(bars)][^(messages)]\" -A 7 -H \"./$(OUTPUTDIR)\" | more)\")\"))" << std::endl << std::endl;
    

    buf_makefile << "LILLYxClean = echo \"\\033[38;2;255;191;0m> Lösche temporäre Dateien...\033[m\" && \\"                     << std::endl;
    buf_makefile << "     for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done"                                         << std::endl<< std::endl<< std::endl;

    // parse the modes:
    configuration_t b_rules = getRules(settings[S_BUILDRULES_PATH],settings[S_LILLY_COMPLETE]=="true");

    std::string added_rules = "";

    for(std::string s : split(settings[S_LILLY_MODES] /* => buildrule_names*/)) {
        if(b_rules.find(s) != b_rules.end()){
            buf_makefile << b_rules[s] << std::endl << std::endl;
            if(settings[S_LILLY_COMPLETE] == "true" && b_rules.find("c_" + s) != b_rules.end())
                buf_makefile << b_rules["c_" + s] << std::endl << std::endl;
            added_rules += s + " ";
        } else 
            std::cerr << COL_ERROR << "    Der spezifizierte Build-Modus: \"" << s << "\" steht leider nicht zur Verfügung!" << COL_RESET << std::endl;
    }


    std::string all = "";
    // generate all rule:
    buf_makefile << std::endl << "all: $(INPUTDIR)$(TEXFILE)" << std::endl;
    for(std::string s : split(added_rules)) {
        all += s + ((settings[S_LILLY_COMPLETE] == "true")?(" c_" + s):"") + " ";
    }

    buf_makefile << "\t@$(MAKE) -s --no-print-directory " << all << " CLEANTARGET=void -j ${JOBCOUNT} --output-sync=line"
                 << std::endl << "\t@$(MAKE) -s --no-print-directory clean" << std::endl << std::endl;

    //generate clean rule:
    buf_makefile << "clean:" << std::endl << "\t@$(call LILLYxClean)"<< std::endl << std::endl;

    //generate Phony
    buf_makefile << ".PHONY: " << all << "all clean" << std::endl;



    targetpath = settings["path"] + "/" + settings["mk-name"];
    std::ofstream out_makefile(targetpath, std::ofstream::out);

    std::cout << "        - Makefile Dateistatus: \"" << targetpath << "\", good: " << out_makefile.good() << std::endl
              << "          Compile Version: " << __DATE__ << " " << __TIME__ << std::endl << std::endl;

    if(settings["debug"]=="true"){
    std::cout << std::endl
              << "Debug Writing: =================================================================" << std::endl << buf_makefile.str()
              << "================================================================================" << std::endl;
    }
    out_makefile << buf_makefile.rdbuf();

    // finish
    out_makefile.close();

    /// @todo add debug, general: options for remote compile, etc.
    /// @todo jake prepare document: expandable macros, encapsulate - restructure commands etc.
    std::cout << "Generierung des Makefiles abgeschlossen: " << er_decode(EXIT_SUCCESS) << std::endl;
    return EXIT_SUCCESS;
}


status_t fkt_tokentest(const std::string& cmd) {
    // test für den Tokenizer :D
    std::cout << "Einzelne Gruppen werden mit \"~\" getrennt!" << std::endl;
    Tokenizer t(settings["file"]);
    while(t.loadNext()) { 
        Tokenizer::Match m = t.get();
        if(m.failure()) continue;
        for(int i = 1; i < m.matchings.size(); ++i){
            std::cout << m.matchings[i] << "~";
        }
        std::cout << "#" << std::endl;
    }
    return EXIT_SUCCESS;
}

uint8_t RECURSIVE_CALLCOUNTER = 0;

status_t fkt_config(const std::string& cmd) {
    if(RECURSIVE_CALLCOUNTER++ > MAX_SETTINGS_REC) {
        std::cerr << COL_ERROR << "Du hast das Limit an Konfigurationsaufrufen erreicht! Mehr erscheint wirklich nicht sinnvoll!" 
                  << COL_RESET << std::endl;
        throw std::runtime_error("Zu viele Konfigurationsdateien");
    }
    Configurator c(settings["file"]);
    c.parse_settings(&settings);
    return in_settings(cmd); 
}



functions_t functions = {
    {"help", {fkt_help, "Zeigt diese Hilfe an"}},
    {"dump", {fkt_dump, "Zeigt alle settings und ihre Werte an" }},
    {"file_compile", {fkt_compile, "Erstellt ein makefile für settings[\"file\"]"}},
    {"install", {fkt_install, "Versucht LILLY zu installieren"}},
    {"tokentest", {fkt_tokentest, "Testet den Tokenizer auf seine Funktionalität"}},
    {"config", {fkt_config, "Lädt die Einstellungen aus der Datei 'file'"}},
    {"_gsettings", {fkt_gsettings, "Interne Funktion, liefert Einstellungen für die Autovervollständigung"}},
    {"_goptions", {fkt_goptions, "Interne Funktion, liefert Operationen für die Autovervollständigung"}}
};
