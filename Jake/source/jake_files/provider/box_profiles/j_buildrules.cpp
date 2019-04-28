#include "j_buildrules.hpp"

settings_t __buildrules_settings = {
    {"name", {"!!", "interner Name der Regel" }}, // needed
    {"display-name", {"!!", "anzuzeigender Name"}}, // needed
    {"lilly-mode", {"!!", "zu verwendender Modi"}}, // needed
    {"complete", {"false", "Ist es eine complete-Version?", IS_SWITCH}},
    {"complete-prefix", {"c_", "Anzeige-Präfix für vollständige Version"}},
    {"lilly-complete-prefix", {"COMPLETE-", "Lilly-Präfix für vollständige Version"}},
    {"nameprefix", {"", "Generelles Namenspräfix"}},
    {"lilly-loader", {R"(\\input{$(INPUTDIR)$(TEXFILE)})", "Lade Sequenz für Datei"}}
};

configuration_t buildrules_default = {
    {"default",         create_buildrule("Standart","default","default", false)},
    {"print",           create_buildrule("Druck","print","print", false, settings[S_LILLY_PRINT_NAME])},
    {"uebungsblatt",    create_buildrule("Übungsblatt","uebungsblatt","default", true, "",                                             R"(\\documentclass[Typ=Uebungsblatt${_C}Vorlesung=${VORLESUNG}${_C}n=${N}${_C}Semester=${SEMESTER}]{Lilly}\\begin{document}\\input{$(INPUTDIR)$(TEXFILE)}\\end{document})")},
    {"c_default",       create_buildrule("Standart","c_default","default", true)},
    {"c_print",         create_buildrule("Druck","c_print","print", true, settings[S_LILLY_PRINT_NAME])}
};

configuration_t getRules(const std::string& rulefiles, bool complete) {
    if(rulefiles=="") return buildrules_default;

    GeneratorParser gp(rulefiles);
    configuration_t ret_config = buildrules_default;
    std::vector<GeneratorParser::jObject> got = gp.parseFile(NAME_BOXPROFILE_BUILDRULE,buildrule_settings._settings);
    for (GeneratorParser::jObject jo : got){
        // check validity with assigning "!!" to value (which cannot be in code) and then throwing debug error if msising

        if(assert_all_differ(jo.configuration, "!!", "diese Buildrule"))
            continue; //Pass on not correct

        //|| jo.configuration["display-mode"] =="!!" || jo.configuration["lilly-mode"] == "!!")

        ret_config[jo.configuration["name"].value] = create_buildrule(jo.configuration["display-name"].value,
                                                    (jo.configuration["complete"].value=="true")?
                                                    jo.configuration["complete-prefix"].value:"" +
                                               jo.configuration["name"].value,
                                               jo.configuration["lilly-mode"].value,
                                               jo.configuration["complete"].value=="true",
                                               jo.configuration["nameprefix"].value,
                                               jo.configuration["lilly-loader"].value,
                                               jo.configuration["lilly-complete-prefix"].value
                                              );
        if (complete)
            ret_config[jo.configuration["complete-prefix"].value+jo.configuration["name"].value] = create_buildrule(jo.configuration["display-name"].value,
                                                                        jo.configuration["complete-prefix"].value +
                                                                    jo.configuration["name"].value,
                                                                    jo.configuration["lilly-mode"].value,
                                                                    true,
                                                                    jo.configuration["nameprefix"].value,
                                                                    jo.configuration["lilly-loader"].value,
                                                                    jo.configuration["lilly-complete-prefix"].value
                                                                );
    }

    return ret_config;
}

__settings_t buildrule_settings{__buildrules_settings};
