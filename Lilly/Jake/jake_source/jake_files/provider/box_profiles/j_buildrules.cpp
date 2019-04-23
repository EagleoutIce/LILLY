#include "j_buildrules.hpp"

settings_t buildrules_settings = {
    {"name", ""}, // needed
    {"display-name", ""}, // needed
    {"lilly-mode", ""}, // needed
    {"complete", "false"},
    {"complete-prefix", "c_"},
    {"lilly-complete-prefix", "COMPLETE-"},
    {"nameprefix", ""},
    {"lilly-loader", R"(\\input{$(INPUTDIR)$(TEXFILE)})"}
};

configuration_t buildrules_default = {
    {"default",         create_buildrule("Standart","default","default", false)},
    {"print",           create_buildrule("Druck","print","print", false, settings[S_LILLY_PRINT_NAME])},
    {"uebungsblatt",    create_buildrule("Übungsblatt","uebungsblatt","default", true, "",                                             R"(\\documentclass[Typ=Uebungsblatt${_C}Vorlesung=${VORLESUNG}${_C}n=${N}${_C}Semester=${SEMESTER}]{Lilly}\\begin{document}\\input{$(INPUTDIR)$(TEXFILE)}\\end{document})")},
    {"c_default",       create_buildrule("Standart","c_default","default", true)},
    {"c_print",         create_buildrule("Druck","c_print","print", true, settings[S_LILLY_PRINT_NAME])}
};

configuration_t getRules(const std::string& rulefile, bool complete) {
    if(rulefile=="") return buildrules_default;

    GeneratorParser gp(rulefile);
    configuration_t ret_config = buildrules_default;
    std::vector<GeneratorParser::jObject> got = gp.parseFile(NAME_BOXPROFILE_BUILDRULE,buildrules_settings);
    for (GeneratorParser::jObject jo : got){
        ret_config[jo.configuration["name"]] = create_buildrule(jo.configuration["display-name"],
                                                    (jo.configuration["complete"]=="true")?
                                                    jo.configuration["complete-prefix"]:"" +
                                               jo.configuration["name"],
                                               jo.configuration["lilly-mode"],
                                               jo.configuration["complete"]=="true",
                                               jo.configuration["nameprefix"],
                                               jo.configuration["lilly-loader"],
                                               jo.configuration["lilly-complete-prefix"]
                                              );
        if (complete)
            ret_config[jo.configuration["complete-prefix"]+jo.configuration["name"]] = create_buildrule(jo.configuration["display-name"],
                                                                        jo.configuration["complete-prefix"] +
                                                                    jo.configuration["name"],
                                                                    jo.configuration["lilly-mode"],
                                                                    true,
                                                                    jo.configuration["nameprefix"],
                                                                    jo.configuration["lilly-loader"],
                                                                    jo.configuration["lilly-complete-prefix"]
                                                                );
    }

    return ret_config;
}
