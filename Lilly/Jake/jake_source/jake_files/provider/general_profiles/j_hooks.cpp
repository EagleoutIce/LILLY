#include "j_hooks.hpp"

settings_t hooks_settings = {
    {"name", "!!"}, // needed, must be unique - specifier
    {"type", "!!"}, // needed PRE, IN, POST, TODO: ALL- wann soll die hook loslegen?
    {"body", ""}, //hookcontent - was macht die hook ? 
    {"on-success", "success"}, //Nachricht, wenn (zmd letzte) Operation erfolgreich
    {"on-failure", "failure"} //Nachricht, wenn (zmd letzte) Operation scheitert
    // letzere sind shortcuts für (op) && (echo succ) || (echo err)
};

configuration_t hooks_default = {
    {"PRE:hello-world", "echo Hello World"},
    {"POST:hello-world-out", "echo Hello World - I am out"}
};

configuration_t getHooks(const std::string& rulefiles, uint8_t in_max) {
    if(rulefiles=="") return hooks_default;

    GeneratorParser gp(rulefiles);
    configuration_t ret_config = hooks_default;
    std::vector<GeneratorParser::jObject> got = gp.parseFile(NAME_HOOK_BUILDRULE,hooks_settings);
    for(GeneratorParser::jObject jo : got) {
        if(assert_all_differ(jo.configuration, "!!", "diese Hook"))
            continue;
        if(! (jo.configuration["type"]=="PRE" || jo.configuration["type"] == "POST"  || jo.configuration["type"] == "ALL" || (jo.configuration["type"].length() > 1 && jo.configuration["type"].substr(0,2) =="IN") ))
            throw std::runtime_error("Für eine Buildrule sind für \"Typ\" nur die Konfigurationen 'INX', 'PRE' und 'POST' zulässig!");
        ret_config[jo.configuration["type"] + ":" + jo.configuration["name"]] = "(" + jo.configuration["body"] 
                            + ") >$(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo  [" + jo.configuration["on-success"]  + "] ||  echo  [" 
                            + jo.configuration["on-failure"]  + "]" ;
    }
    return ret_config;
}

configuration_t getTagged(configuration_t rules, const std::string& tag) {
    configuration_t::iterator it = rules.begin();
    configuration_t ret_conf;
    while(it != rules.end()){
        if(it->first.find(":") != std::string::npos) {
            if (it->first.substr(0,it->first.find(":")) == tag){
                ret_conf[it->first.substr(it->first.find(":")+1)] = it->second;
            }
        } // hier könnten ALL rein
        ++it;
    }
    return ret_conf;
}


status_t writeHooks(std::ostream& out, configuration_t rules, const std::string& tag) {
    if(!out.good()) return EXIT_FAILURE; 
    configuration_t::iterator it = rules.begin();
    while(it != rules.end()){
        if(it->first.find(":") != std::string::npos) {
            const std::string _rtag = it->first.substr(0,it->first.find(":"));
            const std::string _rnam = it->first.substr(it->first.find(":")+1);
            if (_rtag == tag){
                w_debug4("Platziere die " + _rtag + "-Hook: \""  + _rnam + "\" mit Body: \"" + it->second + "\" im Makefile!", "hooker","INF","", DEBUG_8BIT_FOREGROUND(33));
                out << "    echo \"Lilly " + _rtag + "-Hook[" << _rnam << "] evaluiert zu: $(shell " << it->second << ")\" && \\" << std::endl;
            } else if (_rtag == "ALL") {
                w_debug4("Platziere die " + _rtag + "-Hook als " + tag + ": \""  + _rnam + "\" mit Body: \"" + it->second + "\" im Makefile!", "hooker","INF","", DEBUG_8BIT_FOREGROUND(33));
                out << "    echo \"Lilly " + _rtag + "-Hook[" << _rnam << "] für " + tag + " evaluiert zu: $(shell " << it->second << ")\" && \\" << std::endl;
            }
        }
        ++it;
    }
    return EXIT_SUCCESS;
}
