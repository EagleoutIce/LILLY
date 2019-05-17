#include "j_Functions.hpp"

status_t fkt_dump(const std::string& cmd) noexcept {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Liefere die Konfigurationen (fkt_dump)", "func");
    std::cout << "Settings Dump: " << std::endl
              << "Information: Die '[' ']' gehören jeweils nicht zum Wert, sie dienen lediglich der Übersicht!" << std::endl;
    settings_t::iterator it = settings.begin();
    while(it != settings.end()){ // iterate over all settings
        // simple padding without <iomanip> std::setw
        std::cout << it->first << ": " << std::string(20-it->first.length(),' ')
                  << STY_PARAM << "[" << it->second.value << "]" << COL_RESET << std::endl;
        ++it;
    }
    return EXIT_SUCCESS;
}

status_t fkt_help(const std::string& cmd) noexcept {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Gebe die Hilfe aus (fkt_help)", "func");
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
              << "Angabe gemäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \
                  ein generelles makefile für \"xxx.tex\"."
              << std::endl;

    std::cerr << std::endl << "note:" << std::endl
              << "Allgemeine Einstellungen können über \"-key" << ASS_PATTERN << " value\" gesetzt werden (\"-key\" für boolesche). So "
              << "setzt: \"-path" << ASS_PATTERN <<  " /es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: "
              << "\"/es/gibt/kuchen\". Weiter ist es möglich mit '" << ASA_PATTERN << "' values hinzuzufügen."
              << std::endl;
    return EXIT_SUCCESS;
}

status_t fkt_install(const std::string& cmd) noexcept {
    w_debug("Beginnne mit der Installation (fkt_install) aufruf an ins:", "func");
    w_debug4("Morgeeen, na wie gehts? ", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
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
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
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
                 << R"(\\providecommand{\\lillyPathLayout}{\\LILLYxgetDOCPATH/)" << settings[S_LILLY_LAYOUT_LOADER]<< "}" 
                 << R"(\\providecommand{\\LILLYxEXTERNALIZE}{)" << ((settings[S_LILLY_EXTERNAL]=="true")?"TRUE":"FALSE") << "}"<< std::endl << std::endl //DONT'T Change
                 << "JOBCOUNT     := " << padPrint(settings["jobcount"]+"#")         << "## should: <= cpu/thread count!"      << std::endl
                 << std::endl << std::endl;

    buf_makefile << "void = true"                                                                                   << std::endl<< std::endl<< std::endl;
    //Compile-Regel
    buf_makefile << "LILLYxCompile = \\"                                                                                   << std::endl;
    configuration_t all_hooks = getHooks(settings[S_GEPARDRULES_PATH]);
    configuration_t::iterator a = all_hooks.begin();
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
    buf_makefile << "       echo \"\\033[38;2;102;250;0mGenerierierung von \"${1}" << ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?("$${bm}-"):"")  << "${2}\" ($${bm}) abgeschlossen\\033[m\"; done &&\\"                                                                                                   << std::endl;

    writeHooks(buf_makefile, all_hooks, "POST");

    // call clean routine if clean is enabled :D
    if (settings[S_LILLY_AUTOCLEAN] == "true")
        buf_makefile << "    $(call $(CLEANTARGET)) ) || (";
    else
        buf_makefile << "    echo \"Kein autoclean da zugehörige Einstellung (lilly-autoclean) != true\" ) || (";

    buf_makefile << "echo \"\\033[31m! Das Kompilieren mit LILLY ist fehlgeschlagen. Fehler finden sich in der entsprechendne Logdatei\\033[m\"; exit 1;)"<< std::endl<< std::endl;  //) && (echo \"$(shell echo -E \"$(grep -R --include=\"*.log\" -i -E \"error[^(bars)][^(messages)]\" -A 7 -H \"./$(OUTPUTDIR)\" | more)\")\"))" << std::endl << std::endl;


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
        std::cout << "Du hast debugging aktiviert. Soll das Makefile vor dem Schreiben im Log ausgegeben werden?" << std::endl
                 << "(y)es/(n)o> ";
        char answer='\0'; while(answer != 'y' && answer != 'n') std::cin >> answer;
        if(answer == 'y')
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
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
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
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Jetzt in: fkt_config","func");
    if(RECURSIVE_CALLCOUNTER++ > MAX_SETTINGS_REC) {
        std::cerr << COL_ERROR << "Du hast das Limit an Konfigurationsaufrufen erreicht! Mehr erscheint wirklich nicht sinnvoll!"
                  << COL_RESET << std::endl;
        throw std::runtime_error("Zu viele Konfigurationsdateien oder ungültige Konfigurationsdatei. Stelle sicher die 'operation' zu ändern!");
    }
    Configurator c(settings["file"]);
    c.parse_settings(&settings._settings);
    return in_settings(cmd);
}

// export d_path=$(dirname `which lilly_jake`)/../../Lilly/source/Data/Graphics/ && find ${d_path} | grep -E "${d_path}.*QUERY.*\.tex" 

status_t fkt_get(const std::string& cmd) noexcept {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Versuche nun alle Grafiken auf Basis von QUERY (-settings[what]) zu erstellen" + log_path,"func");

    //std::cout << (R"(bash -c 'rm -r /tmp/gcol/ && mkdir /tmp/gcol/ && export d_path=$(dirname `which lilly_jake`)/../../Lilly/source/Data/Graphics/ && export tfile=$(mktemp --prefix gcol/ --suffix .tex) && echo -e  "\\documentclass[Typ=PLAIN]{Lilly}\n\\\\begin{document}\n \\\\verb|Ergebnisse der Suche: )" + settings["what"] + R"(|\\\\\\\\ \\\\bigskip\n\\\\begin{tabular}{m{0.45\linewidth}>{\\\\centering\\\\arraybackslash}m{0.5\linewidth}}\\\\toprule \\\\bfseries Code &\\\\bfseries Ergebnis\\\\\\\\ \n\\\\midrule\n" > ${tfile} && for a in $(find ${d_path} | grep -E "${d_path}.*)" + settings["what"] + R"(.*\.tex"); do echo -e  "{\\\\small\\\\begin{lstlisting}[style=latex,frame=none, morekeywords={)" + settings["what"] + R"(}]\n\\\\framebox{%\n  \\\\getGraphics{${a/${d_path}/}}%\n}\\\\end{lstlisting}} & {\\\\begin{center}\\\\framebox{\\getGraphics{${a/${d_path}/}}}\\\\end{center}}\\\\\\\\ \n" >> ${tfile}; done && echo -e  "\n\\\\bottomrule\\\\end{tabular}\\\\end{document}" >> ${tfile} && cd /tmp/gcol && lilly_jake $(basename ${tfile}) -debug: false -lilly-show-boxname: false>/dev/null&& make >/dev/null && echo /tmp/gcol/$(basename ${tfile%.*})-OUT/ && xdg-open /tmp/gcol/$(basename ${tfile%.*})-OUT/$(basename ${tfile%.*}).pdf 2>/dev/null')") << std::endl;

    // Have fun debugging that future idiot :P 
    std::cout << "Suche: " << settings["what"] << " " << er_decode(system ((R"(bash -c 'rm -r /tmp/gcol/ && mkdir /tmp/gcol/ && export d_path=$(dirname `which lilly_jake`)/../../Lilly/source/Data/Graphics/ && export tfile=$(mktemp --prefix gcol/ --suffix .tex) && echo -e "\\documentclass[Typ=PLAIN]{Lilly}\n\\\\begin{document}\n \\\\verb|Ergebnisse der Suche: )" + settings["what"] + R"(|\\\\\\\\ \n Die Größe der Grafiken wurde angepasst! \\\\bigskip \\\\\\\\ \n\\\\begin{tabular}{m{0.45\linewidth}>{\\\\centering\\\\arraybackslash}m{0.5\linewidth}}\\\\toprule \\\\bfseries Code &\\\\bfseries Ergebnis\\\\\\\\ \n\\\\midrule\n" > ${tfile} && for a in $(find ${d_path} | grep -E "${d_path}.*)" + settings["what"] + R"(.*\.tex" | sort ); do echo -e  "{\\\\small\\\\begin{lstlisting}[style=latex,frame=none, morekeywords={)" + settings["what"] + R"(}]\n\\\\framebox{%\n  \\\\getGraphics{${a/${d_path}/}}%\n}\\\\end{lstlisting}} & {\\\\begin{center}\\\\resizebox{0.97\\\\linewidth}{!}{\\\\framebox{\\getGraphics{${a/${d_path}/}}}}\\\\end{center}}\\\\\\\\ \n" >> ${tfile}; done && echo -e  "\n\\\\bottomrule\\\\end{tabular}\\\\end{document}" >> ${tfile} && cd /tmp/gcol && lilly_jake $(basename ${tfile}) -debug: false -lilly-show-boxname: false>/dev/null&& make >/dev/null && echo /tmp/gcol/$(basename ${tfile%.*})-OUT/ && xdg-open /tmp/gcol/$(basename ${tfile%.*})-OUT/$(basename ${tfile%.*}).pdf 2>/dev/null')").c_str())) << std::endl;

    return EXIT_SUCCESS;
    //return system(("grep -E \"" + settings["what"] + "\" -r * -hs").c_str());
}

status_t fkt_autoget(const std::string& cmd) noexcept{
    std::cout << parse_cmd_line_autocomplete(settings["what"]);
    return EXIT_SUCCESS;
}

status_t fkt_update(const std::string& cmd) noexcept{
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Versuche Lilly zu Aktualisieren (fkt_update)","func");
    std::cout << "Aktualisiere Lilly: " << std::endl;
    if(settings["path"] == "./") {
        std::cout << "Pfad soll nicht überschrieben werden, nutze alten (${LILLY_JAKE_CONFIG_PATH}=" << exec("printf ${LILLY_JAKE_CONFIG_PATH}") << ")" << std::endl;
        w_debug("Aktualisiere: export tf=$(mktemp) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"${LILLY_JAKE_CONFIG_PATH}\" > ${tf} && lilly_jake install -debug >> ${tf}", "update");
        std::cout << "Update: " << er_decode(system("export tf=$(mktemp) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"${LILLY_JAKE_CONFIG_PATH}\" > ${tf} && lilly_jake install -debug >> ${tf}")) << std::endl;
    } else {
        std::cout << "Pfad soll überschrieben werden, nutze neuen (settings[\"path\"]=" << settings["path"] << ")" << std::endl;
        w_debug("Aktualisiere: export tf=$(mktemp) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"settings[\"path\"]\" > ${tf} && lilly_jake install -debug >> ${tf}", "update");
        std::cout << "Update: " << er_decode(system(("export tf=$(mktemp) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"" + settings["path"] + "\" > ${tf} && lilly_jake install -debug >> ${tf}").c_str())) << std::endl;
    }
#if defined(__linux__) 
    std::cout << "Soll eine Dokumentation generiert werden?" << std::endl << "(y)es/(n)o> ";

    char answer='\0'; while(answer != 'y' && answer != 'n') std::cin >> answer;
    if(answer=='y') {
        w_debug("Prüfe auf doxygen und graphviz.","update");
        if(system("which doxygen > /dev/null") || system("which dot > /dev/null")) {
            w_debug("Prüfung gescheitert","update");
            std::cout << "Du verfügst nicht über mindestens eins der beiden benötigten Pakete doxygen und graphviz. Soll Jake sie für dich installieren? " << std::endl << "(y)es/(n)o> ";
            answer='\0'; while(answer != 'y' && answer != 'n') std::cin >> answer;
            if(answer=='y') {
                std::cout << "Installiere Pakete: " << er_decode(system("sudo apt install doxygen graphviz")) << std::endl;
            } else {
                std::cerr << COL_ERROR << "Abbruch!" << COL_RESET << std::endl;
                return EXIT_FAILURE;
            }
        }
        w_debug("Aktualisiere mit: export tf=$(mktemp) && echo \"Schreibe Dokumentations-log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../Jake/source && make documentation > ${tf} 2>&1","update");
        std::cout << "Erstelle Dokumentation... " << er_decode(system("export tf=$(mktemp) && echo \"Schreibe Dokumentations-log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../Jake/source && make documentation > ${tf} 2>&1")) << std::endl;
    } else {
        std::cout << "Fertig!" << std::endl;
    }
#endif
    return EXIT_SUCCESS;
}


functions_t functions = {
    {"help", {fkt_help, "Zeigt diese Hilfe an"}},
    {"dump", {fkt_dump, "Zeigt alle settings und ihre Werte an" }},
    {"file_compile", {fkt_compile, "Erstellt ein makefile für settings[\"file\"]"}},
    {"install", {fkt_install, "Versucht LILLY zu installieren"}},
    {"tokentest", {fkt_tokentest, "Testet den Tokenizer auf seine Funktionalität"}},
    {"config", {fkt_config, "Lädt die Einstellungen aus der Datei 'file'"}},
    {"get", {fkt_get, "Sucht nach Grafiken die settings[\"what\"] enthalten!"}},
    {"update", {fkt_update, "Versucht Lilly & Jake zu aktualisieren"}},
    {"_get", {fkt_autoget, "Interne Funktion, liefert Alles für die Autovervollständigung"}}
};

