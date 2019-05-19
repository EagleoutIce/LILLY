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

    hl_debug("Jake Footprint: " + std::string(PRG_BRIEF) + " (" + __DATE__ + " | " + __TIME__ + ")","compile");

    // Expand Hooks
    configuration_t all_hooks = getHooks(settings[S_GEPARDRULES_PATH]);

    expand_Config(all_hooks);

#if defined(__linux__) || defined(__APPLE__) || defined(__MACH__)
    w_debug("Generiere Output-Ordner: \"" + settings[S_LILLY_OUT] + "\"", "compile");
    if(settings[S_LILLY_EXTERNAL]=="true")
        system(("mkdir -p \"" + settings[S_LILLY_OUT] + "/extimg\"").c_str());
    else
        system(("mkdir -p \"" + settings[S_LILLY_OUT] + "\"").c_str());
#endif


    executeHooks(all_hooks,"PRE");

    // creating logfile
    hl_debug("Create Logfile \"" + settings[S_LILLY_OUT] + "LILLY_COMPILE.log\": " + er_decode(system(("touch " + settings[S_LILLY_OUT] + "LILLY_COMPILE.log").c_str())),"compile");
    hl_debug("Write init sequence: " + er_decode(system(("echo LILLY_LOGFILE stamp: $(date +'%d.%m.%Y %H:%M:%S') > " + settings[S_LILLY_OUT] + "LILLY_COMPILE.log 2>&1").c_str())),"compile");

    // compile boxmodes

    configuration_t b_rules = getRules(settings[S_GEPARDRULES_PATH],settings[S_LILLY_COMPLETE]=="true");

    configuration_t::iterator it = b_rules.begin();
    



    std::vector<std::string> buildrules = split(settings[S_LILLY_MODES]);

    std::thread* ar_trd[buildrules.size()];
    uint8_t ctr = 0;
    for(const std::string& buildrule : buildrules) { // Build modi
        std::vector<std::string> b_data = split(b_rules[buildrule],'!');
        if(b_rules.find(buildrule) == b_rules.end() || b_data.size() != 4) continue; // valid buildrule
        ar_trd[ctr] = new std::thread(c_jake_compile,ctr,b_data,expandables,all_hooks);
        ctr++;
    }

    // await completition
    for(int i = 0; i < buildrules.size(); i++){
        try {
            ar_trd[i]->join();
        } catch (std::runtime_error er){
            std::cout << er.what() << std::endl;
        }
        // print stream when buffered (todo)
        std::cout << "Completed ID: " << i << std::endl;
    }


    // Post-Hooks
    executeHooks(all_hooks, "POST");

    // Autoclean?
    if (settings[S_LILLY_AUTOCLEAN] == "true") {
        std::cout << DEBUG_COLOR_FG_RGB(255,191,0) << "> Lösche temporäre Dateien..." << COL_RESET << std::endl; 
        for(const std::string& fext /*file extension*/ : split(settings[S_LILLY_CLEANS])) {
            system(("rm -f " + settings[S_LILLY_OUT] + "*." + fext).c_str());
            if(settings[S_LILLY_EXTERNAL] == "true")
                system(("rm -f " + settings[S_LILLY_OUT] + "extimg/*." + fext).c_str());
        }


    } else
        std::cout << "Kein autoclean da zugehörige Einstellung (lilly-autoclean) != true" << std::endl;
    
    std::cout << "Kompilieren abgeschlossen" << std::endl;
    return EXIT_SUCCESS;
}


// TODO SYNC OUTPUT BY REDIRECTING AND PRINTING AFTER COMPLETITION
void c_jake_compile(uint8_t id, const std::vector<std::string>& b_data, configuration_t expandables, configuration_t all_hooks) {
        std::cout << "[" << std::to_string(id) << "] " <<std::regex_replace(b_data[B_TEXT],std::regex("\"\""),"") << "-Version(" << settings[S_LILLY_BOXES] << ") der Latex-Datei: " << settings["file"] << "...\033[m" << std::endl;

        // === COMPILE
        for(const std::string& boxmode : split(settings[S_LILLY_BOXES])) {
            // Schreib Boxmodus in tmp-Datei für den Fall dass sie für bibtex benötigt wird
            w_debug("Schreibe boxmode: " + er_decode(system(("echo " + boxmode + " > /tmp/lillytmp.bib.p").c_str())),"compile" + std::to_string(id));


            // External: Erstelle ghost-dokument
            if(settings[S_LILLY_EXTERNAL] == "true"){
                w_debug("Erstelle Ghost Dokument... " + er_decode(system(("echo \"" + expand(expandables,"\\\\providecommand{\\\\LILLYxBOXxMODE}{" + boxmode + "}\\\\providecommand{\\\\LILLYxPDFNAME}{" + b_data[B_NAME] 
                        + ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?boxmode+"-":"") + "$(PDFFILE)}" + b_data[B_EXTRA] + expand(expandables," ${_LILLYARGS} ") + b_data[B_INPUT] + "\" > $(OUTPUTDIR)" + exec("printf \"$(basename " + b_data[B_NAME]
                        + ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?boxmode+"-":"") + expand(expandables,"$(PDFFILE)") + " .pdf).tex\""
                        ))).c_str())),"compile" + std::to_string(id));

            }

            std::string final_name = b_data[B_NAME] + ((settings[S_LILLY_SHOW_BOX_NAME]=="true")?boxmode+"-":"") + exec("printf \"$(basename " + expand(expandables,"$(PDFFILE)") + " .pdf)\"");

            int fb = 0;
            for(int i = 0; i < std::stoi(settings[S_LILLY_COMPILETIMES]); i++) { // Compile X times
                executeHooks(all_hooks, "IN" + std::to_string(i));
                hl_debug("Execute PDF-Latex: " + \
                    er_decode(fb = system((expand(expandables,"pdflatex -jobname " /* erhalte Namensbezeichner */ +  final_name \
                        + " $(LATEXARGS) " // set Boxmode
                        + b_data[B_EXTRA] + /* extr cmds */  " ${_LILLYARGS} "  \
                        + R"( \\providecommand{\\LILLYxBOXxMODE}{)" + boxmode + R"(} \\providecommand{\\LILLYxPDFNAME}{)" + final_name + "}"\
                        + b_data[B_INPUT] /* set input */\
                        + /* to log */ " >> " + settings[S_LILLY_OUT] + "LILLY_COMPILE.log 2>&1"
                        )).c_str())),\
                         "compile");
                if(fb) throw std::runtime_error(("Das Kompilieren mit pdflatex und Jake ist in thread " + std::to_string(id) + " für " + b_data[B_TEXT] + " fehlgeschlagen bitte sieh im entsprechenden Logfile nach!").c_str()); // todo resolve logfile
            }

            // Ausgabe fertig
            std::cout << DEBUG_COLOR_FG_RGB(102,250,0) << "Generierung von \"" + final_name + "\" (" + boxmode + ") abgeschlossen" << COL_RESET << std::endl;
    }
}