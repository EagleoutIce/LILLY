#include "j_nmaps.hpp"

settings_t __nmaps_settings = {
    {"name", {"!!", "Name des Mappings"}}, // needed, must be unique - specifier
    {"patterns", {"!!", "Komma-separierte Liste der Patterns"}} 
    /* ZUDEM: ALLE in settings erlaubten Einstellungen können 
     * angefügt werden. Sie werden dann gesetzt, wenn das 
     * entsprechende Pattern anschlägt.
     */
};

configuration_t nmaps_default = {
    {"GDBS", "gdbs,GdBS,GDBS:lilly-vorlesung=GDBS\nlilly-semester=2"}
};

__settings_t nmaps_settings{__nmaps_settings};

configuration_t getNameMaps(const std::string& rulefiles) {
    if(rulefiles=="") return nmaps_default;

    GeneratorParser gp(rulefiles);
    configuration_t ret_config = nmaps_default;
    std::vector<GeneratorParser::jObject> got = gp.parseFile(NAME_NMAP_BUILDRULE,nmaps_settings._settings, true);
    for(GeneratorParser::jObject jo : got) {
        if(assert_all_differ(jo.configuration, "!!", "diese Name Map"))
            continue;
        ret_config[jo.configuration["name"].value] = jo.configuration["patterns"].value + ":";
        for(auto s = jo.configuration.begin(); s != jo.configuration.end(); ++s) {
            if(s->first == "name" || s->first == "patterns") continue;
            ret_config[jo.configuration["name"].value] += s->first + "=" + s->second.value;
        } // Dies kann man effizienter machen
    }
    return ret_config;
}

configuration_t whatTrigger(const configuration_t& rules, const std::string& seq) {
    w_debug4("Suche Trigger: \"" + seq + "\"", "nmaps","INF","", DEBUG_8BIT_FOREGROUND(33));
    // Iteriere über alle Trigger
    configuration_t ret_config;
    std::match_results<std::string::const_iterator> match;
    for(auto i = rules.begin(); i != rules.end(); ++i) {
        for(const std::string& reg : getPatterns(i->second)) {
            if(std::regex_search(seq, match, std::regex(reg))) {
                w_debug("Match: " + reg + " PL: " + i->second, "nmaps");
                ret_config[i->first] = (i->second).substr(i->second.find(":")+1);
            } else w_debug("No Match: " + reg + " for: " + i->first, "nmaps");
        }
    }
    return ret_config;
}
