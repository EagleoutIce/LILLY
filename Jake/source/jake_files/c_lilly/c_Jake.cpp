#include "c_Jake.hpp"

status_t c_jake(const std::string& cmd){
    hl_debug("Jake versucht nun das Lilly-Dokument zu kompilieren","compile");

    configuration_t expandables = expand_Settings();

    std::string targetfile = settings[S_LILLY_IN] + "/" + settings["file"];
    hl_debug("Identifizierte Datei: " + targetfile, "compile");

    // Existiert die Datei?
    if(!check_file(targetfile)){
        std::cout << "Die von dir angegebene Datei: " << targetfile << " konnte nicht gefunden werden! Soll Jake sie für dich erstellen?" << std::endl;
        char answer = get_answer("[(y)es/(n)o/(c)ancel]> ","ync");
        if(answer=='y') generate_dummyfile(targetfile);
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
        expandables = expand_Settings();
    }

#if defined(__linux__) || defined(__APPLE__) || defined(__MACH__)
    // Generiere Notwendige Ordnerstruktur für Ein- und Ausgabedateien!
    std::cout << "    - Erstelle Ordner settings[\"mk-path\"] (" << settings["mk-path"] << ")... "
              << er_decode(system(("mkdir -p " + settings["mk-path"]).c_str())) << std::endl; // OS - BARRIER
#else
    std::cout << "!   Auf deinem Betriebssystem ist noch keine Regel implementiert die mir es erlaubt einen Ordner zu erstellen!" << std::endl
              << "!   Es ist wichtig, dass du die Existenz aller Pfade die du benötigst selbst gewährleistest :/" << std::endl;
#endif

    hl_debug("Jake Footprint: " + std::string(PRG_BRIEF) + " (" + __DATE__ + " | " + __TIME__ + ")","compile");

    // Expand Hooks
    configuration_t all_hooks = getHooks(settings[S_GEPARDRULES_PATH]);

    expand_Config(all_hooks);

    executeHooks(all_hooks,"PRE");

#if defined(__linux__) || defined(__APPLE__) || defined(__MACH__)
    w_debug("Generiere Output-Ordner: \"" + settings[S_LILLY_OUT] + "\"", "compile");
    if(settings[S_LILLY_EXTERNAL]=="true")
        system(("mkdir -p \"" + settings[S_LILLY_OUT] + "/extimg\"").c_str());
    else
        system(("mkdir -p \"" + settings[S_LILLY_OUT] + "\"").c_str());
#endif

    // creating logfile
    hl_debug("Create Logfile \"" + settings[S_LILLY_OUT] + "LILLY_COMPILE.log\": " + er_decode(system(("touch " + settings[S_LILLY_OUT] + "LILLY_COMPILE.log").c_str())),"compile");
    hl_debug("Write init sequence: " + er_decode(system(("echo LILLY_LOGFILE stamp: $(date +'%d.%m.%Y %H:%M:%S') > " + settings[S_LILLY_OUT] + "LILLY_COMPILE.log 2>&1").c_str())),"compile");

    // compile boxmodes

    // TODO JAKE compile multhread

    configuration_t b_rules = getRules(settings[S_GEPARDRULES_PATH],settings[S_LILLY_COMPLETE]=="true");

    configuration_t::iterator it = b_rules.begin();

    for(const std::string& buildrule : split(settings[S_LILLY_MODES])) { // Build modi
        std::vector<std::string> b_data = split(b_rules[buildrule],'!');
        if(b_rules.find(buildrule) == b_rules.end() || b_data.size() != 4) continue; // valid buildrule


        std::cout << b_data[B_TEXT] << "-Version(" << settings[S_LILLY_BOXES] << ") der Latex-Datei: " << settings["file"] << "...\033[m" << std::endl;

        // === COMPILE
        for(const std::string& boxmode : split(settings[S_LILLY_BOXES])) {
            // Schreib Boxmodus in tmp-Datei für den Fall dass sie für bibtex benötigt wird
            w_debug("Schreibe boxmode: " + er_decode(system(("echo " + boxmode + "> /tmp/lillytmp.bib.p").c_str())),"compile");


            // External: Erstelle ghost-dokument
            if(settings[S_LILLY_EXTERNAL] == "true"){
                w_debug("Erstelle Ghost Dokument... " + er_decode(system(expand(expandables,"\\\\providecommand{\\\\LILLYxBOXxMODE}{" + boxmode + "}\\\\providecommand{\\\\LILLYxPDFNAME}{" + b_data[B_NAME] 
                        + ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?boxmode+"-":"") + expand(expandables,"${PDFFILE}") + "}" + b_data[B_EXTRA] + expand(expandables," ${_LILLYARGS} ") + b_data[B_INPUT] + " > " + exec("printf \"$(basename " + b_data[B_NAME])
                        + ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?boxmode+"-":"") + expand(expandables,"${PDFFILE}") + " .tex).tex\""
                        ).c_str())),"compile");

            }
        //+ " > " + exec("printf \"$(basename " + b_data[B_NAME] 
        //                + ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?boxmode+"-":"") + expand(expandables,"${PDFFILE}") + " .tex).tex\"")

        }
        // b_rules[buildrule] => Name!EXTRACOMMANDS!INPUT!Brief-Text
        
    }


    return EXIT_SUCCESS;
}
