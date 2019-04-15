#include "j_Functions.hpp"

status_t fkt_dump(const std::string& cmd) noexcept {
    std::cout << "Settings Dump: " << std::endl;
    settings_t::iterator it = settings.begin();
    while(it != settings.end()){ // iterate over all settings 
        // simple padding without <iomanip> std::setw
        std::cout << it->first << ": " << std::string(20-it->first.length(),' ') << STY_PARAM << it->second << COL_RESET << std::endl; 
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
#else 
    std::cerr << "Es existiert keine Installationsregel für dein Betriebssystem :/ - Bitte melde dich beim Maintainer dieses Pakets!" << std::endl;
#endif
   return EXIT_SUCCESS; 
}

status_t fkt_compile(const std::string& cmd) {
    std::cout << "Generiere Makefile für Datei: " << settings["file"] << std::endl;
    std::ofstream out_makefile(settings["path"]+"/"+settings["mk-name"], std::ofstream::out); 
   
    // Generiere Notwendige Ordnerstruktur für Ein- und Ausgabedateien!
    std::cout << "    Erstelle Ordner settings[\"mk-path\"] (" << settings["mk-path"] << ") " 
              << er_decode(system(("mkdir -p " + settings["mk-path"]).c_str())) << std::endl; // OS - BARRIER
    
    
    out_makefile << "# " << PRG_BRIEF << "     (compiled on: " << __DATE__ << " at: " << __TIME__ << ")" << std::endl << std::endl;
    
    // Einfügen der Variablen des Makefiles, es erhält eine andere Struktur wie das, welches von lilly_compile zur Verfügung gestellt wird!!
    out_makefile << "TEXFILE      := " << settings["file"]                                                                      << std::endl    
                 << "BASENAME     := $(basename $(TEXFILE))"                                                                    << std::endl
                 << "PDFFILE      := $(addsuffix .pdf,$(basename $(TEXFILE)))"                                                  << std::endl
                 << "LATEXARGS    := -shell-escape -enable-write18 -interaction=batchmode"                                      << std::endl 
                 // lilly- settings 
                 << "## Directories used for INPUT and OUTPUT Files "                                                           << std::endl
                 << "OUTPUTDIR    := $(shell echo " << settings["lilly-out"] << "| sed 's:/*$$::')/"                            << std::endl
                 << "INPUTDIR     := $(shell echo " << settings["lilly-in"] << "| sed 's:/*$$::')/"                             << std::endl
                 << "BOXMODES     := " << padPrint(settings["lilly-boxes"]+"#")      << "## Seperator: ' '"                     << std::endl
                 << "CLEANTARGET  := LILLYxClean"                                                                               << std::endl
                 << "CLEANTARGETS := " << settings["lilly-cleans"]                                                              << std::endl
                 // lilly- names
                 << "NAMEPREFIX   := " << padPrint(settings["lilly-nameprefix"])            << "## Immer"                       << std::endl
                 << std::endl
                 //Generals
                 << "## Makefile/General settings"                                                                              << std::endl
                 << R"(_LILLYARGS   :=  \\providecommand{\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) \\providecommand{\\LILLYxPATH}{${INPUTDIR}} \\input{$(INPUTDIR)$(TEXFILE)} )" << std::endl << std::endl //DONT'T Change
                 << "JOBCOUNT     := " << padPrint(settings["jobcount"])                    << "## should: <= CPU_COUNT! "      << std::endl
                 << std::endl << std::endl;
    //Compile-Regel
    out_makefile << "define LILLYxCompile = "                                                                                   << std::endl;
    //clean log
    out_makefile << R"(    @echo LILLY_LOGFILE stamp: $(shell date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1)"                   << std::endl
                 << "    @for bm in $(BOXMODES); do \\" << std::endl;
    for(int i = 0; i < std::stoi(settings["lilly-compiletimes"]); i++) {
        out_makefile << "       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS)" << R"( \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}} )" << " ${3} $(_LILLYARGS)" << R"(>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \)" << std::endl;
    }
    out_makefile << "       echo \"\\033[38;2;102;250;0mGenerierierung von \"${1}$${bm}-${2}\" ($${bm}) abgeschlossen\\033[m\"; done;"                                                                                                   << std::endl;

    // call clean routine if clean is enabled :D
    if (settings["lilly-autoclean"] == "true")
        out_makefile << "    $(call ${CLEANTARGET}) ##  Da 'lilly-autoclean' = true " << std::endl;

    out_makefile << "endef" << std::endl;

    out_makefile << "define LILLYxClean = "                                                                                     << std::endl;
    out_makefile << "    @echo \"\033[38;2;255;191;0m> Lösche temporäre Dateien...\033[m\""                                     << std::endl;
    out_makefile << "    @for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done;"                                        << std::endl;
    out_makefile << "endef" << std::endl << std::endl;


    // parse the modes:
    /**
     * @todo [MK] Make modes editable by textfile! so print shall be defined as: Bezeichner:LILLYxMODE,MODExEXTRA,OTHERS...
     * 
     * @todo [MK] .Phony
     * 
     * @todo [MK] build-rules sollen auch variablen überschreibe können so soll es möglich sein BoxModues nur spezifisch herzustellen um so zum Beispiel _test_ oder so zu ermöglochen undnicht immer ein eigenes Makefile zu forcieren
     */

    for(std::string s : split(settings["lilly-modes"] /* => buildrule_names*/)) {
        if(s == "default"){
            out_makefile << create_buildrule("Standart","default","default", false) << std::endl << std::endl;
            if((settings["lilly-complete"]=="true"))
                out_makefile << create_buildrule("vollständige Standart", "c_default", "default", true, settings["lilly-complete-name"]) << std::endl << std::endl;
        } else if(s == "print"){
            out_makefile << create_buildrule("Druck","print","print", false, settings["lilly-print-name"]) << std::endl << std::endl;
            if((settings["lilly-complete"]=="true"))
                    out_makefile << create_buildrule("Vollständige-Druck", "c_print", "print", true, settings["lilly-print-name"] + settings["lilly-complete-name"]) << std::endl << std::endl;
        } else {
            std::cerr << "    Der spezifizierte Build-Modus: \"" << s << "\" steht leider nicht zur Verfügung!" << std::endl;
        }
    }
    
    std::string all = "";
    // generate all rule:
    out_makefile << std::endl << "all: $(INPUTDIR)$(TEXFILE)" << std::endl; 
    for(std::string s : split(settings["lilly-modes"])) {
        all += s + ((settings["lilly-complete"] == "true")?(" c_" + s):"") + " ";
    }
    
    out_makefile << "\t@$(MAKE) -s --no-print-directory " << all << " CLEANTARGET=void -j ${JOBCOUNT} --output-sync=line"
                 << std::endl << "\t@$(MAKE) -s --no-print-directory clean" << std::endl << std::endl;
    
    //generate clean rule:
    out_makefile << "clean: " << std::endl << "\t$(call LILLYxClean)"<< std::endl << std::endl;  
    
    //generate Phony
    out_makefile << ".PHONY: " << all << "all clean" << std::endl;
    
    //finish
    out_makefile.close(); ///@todo  PREFIXES FOR DIFFERENT RULES DEFINE WITH: --rule=name:specials or --rule:name:specials
   
    // TODO:
    /// @todo add debug, general: options for remote compile, etc.
    /// @todo jake prepare document: expandable macros, encapsulate - restructure commands etc.
    return EXIT_SUCCESS;
}


functions_t functions = {
    {"help", {fkt_help, "Zeigt diese Hilfe an"}},
    {"dump", {fkt_dump, "Zeigt alle settings und ihre Werte an" }},
    {"file_compile", {fkt_compile, "Erstellt ein makefile für settings[\"file\"]"}},
    {"install", {fkt_install, "Versucht LILLY zu installieren"}}
};
