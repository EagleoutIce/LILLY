#include "j_hooks.hpp"

settings_t hooks_settings = {
    {"name", "!!"}, // needed, must be unique - specifier
    {"type", "!!"}, // needed PRE, IN, POST, TODO: ALL- wann soll die hook loslegen?
    {"body", ""}, //hookcontent - was macht die hook ? (shellbefehle via Makefile $(shell ...))
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
        if(! (jo.configuration["type"]=="PRE" || jo.configuration["type"]=="IN" || jo.configuration["type"] == "POST"  /*|| jo.configuration["type"] == "ALL"*/))
            throw std::runtime_error("Für eine Buildrule sind für \"Typ\" nur die Konfigurationen 'IN', 'PRE' und 'POST' zulässig!");
        ret_config[jo.configuration["type"] + ":" + jo.configuration["name"]] = jo.configuration["body"] 
                            + " && echo  [" + jo.configuration["on-success"]  + "] ||  echo  [" 
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
