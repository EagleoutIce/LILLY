#include "j_expandables.hpp"

settings_t __expandables_settings = {
    /* Es ist möglich jede Art von Key-Value-Par als Expandable einzubringen,
     * So kann zum Beispiel BASENAME forciert werden, etc...
     */
};


configuration_t get_default_expandables( void ) {
    configuration_t _ret;
    /* cSpell:disable */
        _ret["TEXFILE"] = settings["file"];
        _ret["BASENAME"] = ("!!printf \"$(basename " + settings["file"] + " .tex)\"");
        _ret["PDFFILE"] = ("!!printf \"$(basename " + settings["file"] + " .tex).pdf\"");

        _ret["LATEXARGS"] = "-shell-escape -enable-write18 -interaction=batchmode";

        _ret["OUTPUTDIR"] = ("!!printf \"$(echo " + settings[S_LILLY_OUT] + "| sed 's:/*$$::')/\"");
        _ret["INPUTDIR"] = ("!!printf \"$(echo " + settings[S_LILLY_IN] + "| sed 's:/*$$::')/\"");
        
        _ret["BOXMODES"] = settings[S_LILLY_BOXES];

        _ret["CLEANTARGETS"] = settings[S_LILLY_CLEANS];
        
        _ret["SIGNATURECOL"] = settings[S_LILLY_SIGNATURE_COLOR];

        _ret["AUTHOR"] = settings[S_LILLY_AUTHOR];
        _ret["AUTHORMAIL"] = settings[S_LILLY_AUTHORMAIL];

        _ret["NAMEPREFIX"] = settings[S_LILLY_NAMEPREFIX];

        _ret["SEMESTER"] = settings[S_LILLY_SEMESTER];
        _ret["VORLESUNG"] = settings[S_LILLY_VORLESUNG];
        _ret["N"] = settings[S_LILLY_N];

        _ret["JOBCOUNT"] = settings["jobcount"];

        _ret["_LILLYARGS"] = R"(\\providecommand{\\LILLYxDOCUMENTNAME{)" + settings["file"] + R"(}}\\providecommand{\\LILLYxOUTPUTDIR}{)" + settings[S_LILLY_OUT] + R"(} \\providecommand{\\LILLYxPATH}{)" + settings[S_LILLY_IN]+  R"(}\\providecommand{\\AUTHOR}{)" + settings[S_LILLY_AUTHOR]+  R"(}\\providecommand{\\AUTHORMAIL}{)" + settings[S_LILLY_AUTHORMAIL]+  R"(}\\providecommand{\\LILLYxSemester}{)" + settings[S_LILLY_SEMESTER]+  R"(}\\providecommand{\\LILLYxVorlesung}{)" + settings[S_LILLY_VORLESUNG]+  R"(}\\providecommand{\\Hcolor}{)" + settings[S_LILLY_SIGNATURE_COLOR]+  R"(})" + ((settings[S_LILLY_BIBTEX]!="")?(R"(\\providecommand{\\LILLYxBIBTEX}{)" + settings[S_LILLY_BIBTEX] + R"(})"):"")
                 + R"(\\providecommand{\\lillyPathLayout}{\\LILLYxPATHxDATA/Layouts)" + settings[S_LILLY_LAYOUT_LOADER] + "}" 
                 + R"(\\providecommand{\\LILLYxEXTERNALIZE}{)" + ((settings[S_LILLY_EXTERNAL]=="true")?"TRUE":"FALSE") + "}";

        _ret["_C"] = ",";

        _ret["HOME"] = ("!!printf \"$HOME\"");

        _ret["TRUE"] = "true";
        _ret["FALSE"] = "false";

        // Extras all marked with @
        _ret["@JAKEVER"] = JAKE_VERSION;
        _ret["@JAKECDATE"] = __DATE__;
        _ret["@JAKECTIME"] = __TIME__;
        _ret["@GITHUB"] = "https://github.com/EagleoutIce/LILLY";
        _ret["@CONFPATH"] = ("!!printf \"${LILLY_JAKE_CONFIG_PATH}\""); // maybe lazy eval with !!?

        _ret["@WAFFLE"] = "GIVE ME THAT WAFFLE";

        _ret["@SELTEXF"] = ("!!printf \"$(ls | grep .tex | head -n 1)\"");
        _ret["@SELCONF"] = ("!!printf \"$(ls | grep .conf | head -n 1)\"");

    /* cSpell:enable */
    return _ret;
}

__settings_t expandables_settings{__expandables_settings};

configuration_t getExpandables(const std::string& rulefiles) {
    if(rulefiles=="") return get_default_expandables();

    GeneratorParser gp(rulefiles);
    configuration_t ret_config = get_default_expandables();
    std::vector<GeneratorParser::jObject> got = gp.parseFile(NAME_EXPANDABLE_BUILDRULE,expandables_settings._settings, true);
    for(GeneratorParser::jObject jo : got) {
        if(assert_all_differ(jo.configuration, "!!", "dieses Expandable"))
            continue;

        for(auto s = jo.configuration.begin(); s != jo.configuration.end(); ++s) {
            if(s->first.length() == 0 || (s->first[0]=='@' && s->first.length() < 2)) {
                warning_debug("Expandable-Bezeichner: \"" + s->first + "\" ist zu kurz! [Skipping]","expands");
                continue;
            }
            ret_config[s->first] = s->second.value;
            w_debug("Expandable  " + s->first + " " + s->first + "=" + s->second.value, "expands");
        }
    }
    return ret_config;
}


configuration_t expand_Settings( void ) {
    hl_debug("Jake wird nun alle Einstellungen erweitern!","compile");
    configuration_t expandables = getExpandables(settings[S_GEPARDRULES_PATH]);

    // Expand all settings

    settings_t::iterator it = settings.begin();
    while(it != settings.end()){
        it->second.value = expand(expandables, it->second.value);
        ++it;
    }
    return expandables;
}

status_t expand_Config( configuration_t& config ) {
    configuration_t expandables = getExpandables(settings[S_GEPARDRULES_PATH]);

    // Expand all settings
    configuration_t::iterator it = config.begin();
    while(it != config.end()){
        it->second = expand(expandables, it->second);
        ++it;
    }
    return EXIT_SUCCESS;
}

int rec_exp_calls=0;

std::string expand(configuration_t& expandables, std::string str) {
    if(rec_exp_calls > MAX_SETTINGS_REC) {
        throw std::runtime_error("Maximale Expansionstiefe für Expandables erreicht! Der Request: \"" + str + "\" führt zu einer zu tiefen Rekursion! In der Erweiterung eines Expandables darf dieses selbst nicht verwendet werden!");
    }
    rec_exp_calls++;
    std::string last = ""; int counter = 0;
    do {
        last = str;
        configuration_t::iterator it = expandables.begin();
        std::regex x;
        std::match_results<std::string::const_iterator> matches;
        while(it != expandables.end()){
            if(it->first[0]=='@')
                x = std::regex(R"(@[\[])" + it->first.substr(1) + R"([\]])");
            else 
                x = std::regex (R"(\$[\{\(])" + it->first + R"([\)\}])");
            
            // only eval if needed
            if(std::regex_search(str,matches,x)){
                hl2_debug("Expanding in: " + str + " /w"  + it->second,"expander");
                if(it->second.length() > 1 && it->second.substr(0,2) == "!!"){ //lazy eval
                    hl2_debug(" Which is lazy eval: " + exec(expand(expandables, it->second.substr(2))),"expander");
                    str = std::regex_replace(str,x,exec(expand(expandables,it->second.substr(2))));
                } else
                    str = std::regex_replace(str,x,it->second);
            }

            ++it;
        }
        counter++;
    } while(last != str && counter < 8);
    if(counter == 8) {
        std::cerr << "Recursive Expansion limit reached" << std::endl;
    }
    rec_exp_calls=0;
    return str;
}