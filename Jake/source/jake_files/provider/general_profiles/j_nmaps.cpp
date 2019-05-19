#include "j_nmaps.hpp"

settings_t __nmaps_settings = {
    {"name", {"!!", "Name des Mappings"}}, // needed, must be unique - specifier
    {"patterns", {"!!", "Komma-separierte Liste der Patterns"}} 
    /* ZUDEM: ALLE in settings erlaubten Einstellungen können 
     * angefügt werden. Sie werden dann gesetzt, wenn das 
     * entsprechende Pattern anschlägt.
     */
};


configuration_t get_default_nmaps( void ) {
    configuration_t _ret;
    /* cSpell:disable */
    _ret["GDBS"] = "gdbs,GdBS,GDBS,[Gg]rundlagen[\\ \\-]?([Dd]er[\\ \\-]?)?[Bb]etriebssysteme:lilly-vorlesung=GDBS\nlilly-semester=2";
    _ret["ANA1"] = "ana1,ANA1,[Aa]nalysis[\\ \\-]?1:lilly-vorlesung=ANA1\nlilly-semester=2";
    _ret["PVS"] = "pvs,PvS,PVS,[Pp]rogrammierung[\\ \\-]?([Vv]on[\\ \\-]?)?[Ss]ystemen:lilly-vorlesung=PVS\nlilly-semester=2";
    _ret["PDP"] = "pdp,PdP,PDP,[Pp]aradigmen[\\ \\-]?([Dd]er[\\ \\-]?)?[Pp]rogrammierung:lilly-vorlesung=PVS\nlilly-semester=2";

    _ret["GDRA"] = "[Gg][Dd][Rr][Aa],[Gg]rundlagen[\\ \\-]?([Dd]er[\\ \\-]?)?[Rr]echnerarchitektur:lilly-vorlesung=GDRA\nlilly-semester=1";
    _ret["EIDI"] = "[Ee][Ii][Dd][Ii],[Ee]inführung[\\ \\-]?([Ii]n[\\ \\-]?)?([Dd]ie[\\ \\-]?)?[Ii]nformatik:lilly-vorlesung=EIDI\nlilly-semester=1";
    _ret["FG"] = "[Ff][Gg],[Ff]ormale[\\ \\-]?[Gg]rundlagen:lilly-vorlesung=FG\nlilly-semester=1";
    _ret["LA"] = "LA,LAII,[Ll]ineare[\\ \\-]?[Aa]lgebra:lilly-vorlesung=LAIIa\nlilly-semester=1";

    _ret["Übungsblatt"] = "UB,uebungsblatt,[Üü]bungsblatt,ÜB:lilly-modes=uebungsblatt";
    /* cSpell:enable */

    return _ret;
}

__settings_t nmaps_settings{__nmaps_settings};

configuration_t getNameMaps(const std::string& rulefiles) {
    if(rulefiles=="") return get_default_nmaps();

    GeneratorParser gp(rulefiles);
    configuration_t ret_config = get_default_nmaps();
    std::vector<GeneratorParser::jObject> got = gp.parseFile(NAME_NMAP_BUILDRULE,nmaps_settings._settings, true);
    for(GeneratorParser::jObject jo : got) {
        if(assert_all_differ(jo.configuration, "!!", "diese Name Map"))
            continue;
        ret_config[jo.configuration["name"].value] = jo.configuration["patterns"].value + ":";
        w_debug("Catched: " + jo.configuration["name"].value + " /w patterns: " + jo.configuration["patterns"].value, "nmaps");
        for(auto s = jo.configuration.begin(); s != jo.configuration.end(); ++s) {
            if(s->first == "name" || s->first == "patterns") continue;
            ret_config[jo.configuration["name"].value] += s->first + "=" + s->second.value + "\n";
            w_debug("Adding  " + jo.configuration["name"].value + " " + s->first + "=" + s->second.value, "nmaps");
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
            w_debug("Bearbeite: " + reg + " für " + i->first + " mit pl: " + i->second,"nmaps");
            if(std::regex_search(seq, match, std::regex(reg))) {
                w_debug("Match: " + reg + " PL: " + i->second, "nmaps");
                ret_config[i->first] = (i->second).substr(i->second.find(":")+1);
                w_debug("ret contains now: " + ret_config[i->first], "nmaps");
            } /* else w_debug("No Match: " + reg + " for: " + i->first, "nmaps"); */
        }
    }
    return ret_config;
}
