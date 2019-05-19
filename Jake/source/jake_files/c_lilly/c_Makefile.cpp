#include "c_Makefile.hpp"

status_t c_makefile(const std::string& cmd) {
    std::cout << "Generiere Makefile für Datei: " << settings["file"] << std::endl;

    std::string targetpath = settings[S_LILLY_IN] + "/" + settings["file"];

    if(!check_file(targetpath)) {
        std::cout << "Die von dir angegebene Datei: " << targetpath << " konnte nicht gefunden werden! Soll Jake sie für dich erstellen?" << std::endl;

        char answer = get_answer("[(y)es/(n)o/(c)ancel]> ","ync");
        if(answer=='y') generate_dummyfile(targetpath);
        else if (answer=='n') std::cout << "Jake wird so tun als gäbe es die Datei und spielt heile Welt" << std::endl;
        else {
            std::cout << "Abbruch!" << std::endl; return EXIT_FAILURE;
        }
    }

    configuration_t update_config = whatTrigger(getNameMaps(settings[S_GEPARDRULES_PATH]),settings["file"]);
    if(!update_config.empty()) {
        std::cout << "    - Information: aufgrund des Name-Mappings werden deine Einstellungen angepasst. Die Regeln werden im Folgenden angezeigt und angewendet!" << std::endl;

        std::string new_config = "";
        for(auto it = update_config.begin(); it != update_config.end(); ++it) {
            std::cout << "        - " << it->first << std::endl;
            new_config += it->second + "\n"; // die letzte Neuzeile kann vernachlässigt werden.
        }
        std::istringstream strstr_conf(new_config);
        Configurator config_updator(strstr_conf);
        config_updator.parse_settings(&settings._settings);
    }

#if defined(__linux__) || defined(__APPLE__) || defined(__MACH__)
    // Generiere Notwendige Ordnerstruktur für Ein- und Ausgabedateien!
    std::cout << "    - Erstelle Ordner settings[\"mk-path\"] (" << settings["mk-path"] << ")... "
              << er_decode(system(("mkdir -p " + settings["mk-path"]).c_str())) << std::endl; // OS - BARRIER
#else
    std::cout << "!   Auf deinem Betriebssystem ist noch keine Regel implementiert die mir es erlaubt einen Ordner zu erstellen!" << std::endl
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
                 << "SIGNATURECOL := " << settings[S_LILLY_SIGNATURE_COLOR]                                                     << std::endl
                 << "AUTHOR       := " << settings[S_LILLY_AUTHOR]                                                              << std::endl
                 << "AUTHORMAIL   := " << settings[S_LILLY_AUTHORMAIL]                                                          << std::endl
                 // lilly- names
                 << "NAMEPREFIX   := " << padPrint(settings[S_LILLY_NAMEPREFIX]+"#")           << "## Immer"                    << std::endl
                 << "SEMESTER     := " << padPrint(settings[S_LILLY_SEMESTER]+"#")             << "## Übungsblatt"              << std::endl
                 << "VORLESUNG    := " << padPrint(settings[S_LILLY_VORLESUNG]+"#")            << "## Übungsblatt"              << std::endl
                 << "N            := " << padPrint(settings[S_LILLY_N]+"#")                    << "## Anzahl"                   << std::endl
                 << "_C           := ,                             ## No Joke xD"                                               << std::endl
                 << std::endl
                 //Generals
                 << "## Makefile/General settings"                                                                              << std::endl
                 << R"(_LILLYARGS   :=  \\providecommand{\\LILLYxDOCUMENTNAME{$(TEXFILE)}}\\providecommand{\\LILLYxOUTPUTDIR{$(OUTPUTDIR)}} $(DEBUG) \\providecommand{\\LILLYxPATH}{${INPUTDIR}}\\providecommand{\\AUTHOR}{${AUTHOR}}\\providecommand{\\AUTHORMAIL}{${AUTHORMAIL}}\\providecommand{\\LILLYxSemester}{${SEMESTER}}\\providecommand{\\LILLYxVorlesung}{${VORLESUNG}}\\providecommand{\\Hcolor}{${SIGNATURECOL}})" << ((settings[S_LILLY_BIBTEX]!="")?(R"(\\providecommand{\\LILLYxBIBTEX}{)" + settings[S_LILLY_BIBTEX] + R"(})"):"")
                 << R"(\\providecommand{\\lillyPathLayout}{\\LILLYxPATHxDATA/Layouts)" << settings[S_LILLY_LAYOUT_LOADER]<< "}" 
                 << R"(\\providecommand{\\LILLYxEXTERNALIZE}{)" << ((settings[S_LILLY_EXTERNAL]=="true")?"TRUE":"FALSE") << "}"<< std::endl << std::endl //DONT'T Change
                 << "JOBCOUNT     := " << padPrint(settings["jobcount"]+"#")         << "## should: <= cpu/thread count!"      << std::endl
                 << std::endl << std::endl;

    buf_makefile << "void = true"                                                                                   << std::endl<< std::endl<< std::endl;
    //Compile-Regel
    buf_makefile << "LILLYxCompile = \\"                                                                                   << std::endl;
    configuration_t all_hooks = getHooks(settings[S_GEPARDRULES_PATH]);
    /*while(a != all_hooks.end()){
        std::cout << a->first << " " << a->second << std::endl;;
        ++a;
    }*/

    writeHooks(buf_makefile, all_hooks, "PRE");

#if defined(__linux__) || defined(__APPLE__) || defined(__MACH__)
    if(settings[S_LILLY_EXTERNAL]=="true")
        buf_makefile << "    mkdir -p \"$(OUTPUTDIR)/extimg\" && \\" << std::endl; // Auf windows vermutlich identisch nur ohne -p
    else
        buf_makefile << "    mkdir -p \"$(OUTPUTDIR)\" && \\" << std::endl; // Auf windows vermutlich identisch nur ohne -p        
#endif
    //clean log

    buf_makefile << R"(    touch $(OUTPUTDIR)LILLY_COMPILE.log && echo LILLY_LOGFILE stamp: $(shell date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 &&\)"                   << std::endl
                 << "    (for bm in $(BOXMODES); do echo $${bm} > /tmp/lillytmp.bib.p && \\" << std::endl;
    if(settings[S_LILLY_EXTERNAL]=="true")
        if(settings[S_LILLY_SHOW_BOX_NAME]=="true")
        buf_makefile << "       ([ -r $(basename ${1}$${bm}-${2}).tex ] || echo \\\\providecommand{\\\\LILLYxBOXxMODE}{$${bm}}\\\\providecommand{\\\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4} > $(basename ${1}$${bm}-${2}).tex) \\" << std::endl;
        else 
        buf_makefile << "       ([ -r $(basename ${1}${2}).tex ] || echo \\\\providecommand{\\\\LILLYxBOXxMODE}{$${bm}}\\\\providecommand{\\\\LILLYxPDFNAME}{${1}${2}}  ${3} $(_LILLYARGS) ${4} > $(basename ${1}${2}).tex) \\" << std::endl;
    for(int i = 0; i < std::stoi(settings[S_LILLY_COMPILETIMES]); i++) {
        writeHooks(buf_makefile, all_hooks, "IN" + std::to_string(i));
        buf_makefile << "       pdflatex -jobname $(basename ${1}" << ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?("$${bm}-"):"")
                     << "${2}) $(LATEXARGS)" << R"( \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1})" 
                     << ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?("$${bm}-"):"")  << R"(${2}} )" << " ${3} $(_LILLYARGS) ${4}" 
                     << R"(>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \)" << std::endl;
    }
    buf_makefile << "       echo \"\\033[38;2;102;250;0mGenerierung von \"${1}" << ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?("$${bm}-"):"")  << "${2}\" ($${bm}) abgeschlossen\\033[m\"; done &&\\"                                                                                                   << std::endl;

    writeHooks(buf_makefile, all_hooks, "POST");

    // call clean routine if clean is enabled :D
    if (settings[S_LILLY_AUTOCLEAN] == "true")
        buf_makefile << "    $(call $(CLEANTARGET)) ) || (";
    else
        buf_makefile << "    echo \"Kein autoclean da zugehörige Einstellung (lilly-autoclean) != true\" ) || (";

    buf_makefile << "echo \"\\033[31m! Das Kompilieren mit LILLY ist fehlgeschlagen. Fehler finden sich in der entsprechenden Logdatei\\033[m\"; exit 1;)"<< std::endl<< std::endl;  //) && (echo \"$(shell echo -E \"$(grep -R --include=\"*.log\" -i -E \"error[^(bars)][^(messages)]\" -A 7 -H \"./$(OUTPUTDIR)\" | more)\")\"))" << std::endl << std::endl;


    buf_makefile << "LILLYxClean = echo \"\\033[38;2;255;191;0m> Lösche temporäre Dateien...\033[m\" && \\"                     << std::endl;
    if(settings[S_LILLY_EXTERNAL]=="true")
        buf_makefile << "     for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd && rm -f $(OUTPUTDIR)extimg/*.$$fd; done"                                         << std::endl<< std::endl<< std::endl;
    else
        buf_makefile << "     for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done"                                         << std::endl<< std::endl<< std::endl;

    // parse the modes:
    configuration_t b_rules = getRules(settings[S_GEPARDRULES_PATH],settings[S_LILLY_COMPLETE]=="true");

    configuration_t::iterator it = b_rules.begin();
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
        std::cout << "Du hast debugging aktiviert. Soll das Makefile vor dem Schreiben im Log ausgegeben werden?" << std::endl;
        if(get_answer() == 'y')
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