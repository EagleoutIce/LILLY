#include "j_Parser.hpp"

status_t ld_settings(int n /* = argc */, const char** argv) {
    for(int x = 1; x <= n; x++){
        if ( argv[x][0] != ARG_PATTERN ) { // es handelt sich um keine Option                                       ==== OPERATION
            if ( in_str(argv[x], TEX_PATTERN) ) { // Es ist eine Tex-Datei
                settings["operation"] = "file_compile";
                settings["file"] = TO_DATA(argv[x]);
                if ( in_str(TO_DATA(argv[x]), "uebungsblatt") || in_str(TO_DATA(argv[x]), "UBdata") ) // Definitiv zu faul im intensitive search einzubauen
                        settings[S_LILLY_MODES] = "uebungsblatt";
            } else if ( in_str(argv[x], CONF_PATTERN) ) { // Es ist eine Konfigurationsdatei
                settings["operation"] = "config";
                settings["file"] = TO_DATA(argv[x]);
            } else { // Es ist eine normale Operation
                settings["operation"] = TO_DATA(argv[x]);
            }
        } 
            else if ( in_str(argv[x], ASS_PATTERN) ) { // Es handelt sich um eine Zuweisung gemäß -xxxxxxx:         ==== ASSIGNMENT
                std::string s{argv[x]+1};
                if (x == n) {
                    std::cerr << COL_ERROR << "Die Option: " << s << " benötigt ein Argument!" << COL_RESET << std::endl;
                    warning_debug(":brief: " + settings.get(strip_mod(s)).brief, "parser");
                    return EXIT_FAILURE;
                }
                if(in_str(argv[x], ASA_PATTERN)) {              // is an addition statement
                    settings[s.substr(0,s.find(ASA_PATTERN))] += std::string(" ") + argv[++x];
                } else {                                        // normal statement
                    settings[s.substr(0,s.find(ASS_PATTERN))]  = argv[++x];
                }
        } 
            else { // es handelt sich um keine Zuweisung => Switch (-xxxxxxx)                                       ==== Switch
            if(settings.find(argv[x] + 1 /* offset for '-' */) != settings.end()) {
                    if(settings[argv[x]+1] == "false") {        // valid bool-setting
                        settings[argv[x]+1] = "true";
                    } else if (settings[argv[x]+1] == "true") // Das ist zwar dumm, aber ich bin faul :D
                        settings[argv[x]+1] = "false";
                    else                                         // non valid bool-setting
                        er_unknown_setting(argv[x]+1);
                } else                                         // non valid bool-setting
                    er_unknown_setting(argv[x]+1); // zu faul zum entkaspeln
        }
    }
    return EXIT_SUCCESS;
}

status_t in_settings(std::string v0) {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    if(functions.find(settings["operation"]) != functions.end()) {                   // Operation ist valide
            functions[settings["operation"]].fkt(v0);                                // Führe Operation aus
    } else {
        er_unknown_setting(("operation (=" + settings["operation"] + ")").c_str());  // Diese Operation kenne ich nicht
        return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
}

std::string strip_mod(const std::string& str) {
    if(str.length() > 1)
        return str.substr(0,str.find( (in_str(str.c_str(), ASA_PATTERN))?ASA_PATTERN:ASS_PATTERN));
    return str;
}

status_t ld_config( std::string v0) {
    // Lade Standartkonfiguration
    try {
        w_debug("Lade Standartkonfiguration mit: \"printf $(dirname $(which lilly_jake))/jake_files/jake_default.conf\"","parser");
        Configurator default_config_loader(exec("printf $(dirname $(which lilly_jake))/jake_files/jake_default.conf"));
        default_config_loader.parse_settings(&settings._settings);
    } catch (std::runtime_error er) {
        error_debug("Laden gescheitert","parser");
        std::cerr << COL_ERROR << "Die Installation von Jake scheint entweder fehlerhaft zu sein, oder du hast an der Konfigurationsdatei gepfuscht!"
                  << std::endl << "Fehler: " << std::endl <<
                  er.what() << std::endl;
    }
    if(exec("printf $(dirname $(which lilly_jake))/jake_files/jake_default.conf") != exec("printf ${LILLY_JAKE_CONFIG_PATH}")){
        try {
            w_debug("Lade Nutzerkonfiguration mit: \"printf ${LILLY_JAKE_CONFIG_PATH}\"","parser");
            Configurator user_config_loader(exec("printf ${LILLY_JAKE_CONFIG_PATH}"));
            user_config_loader.parse_settings(&settings._settings);
        } catch (std::runtime_error er) {
            warning_debug("Laden gescheitert","parser");
            std::cerr << COL_ERROR << "Deine Konfigurationsdatei für Jake scheint fehlerhaft: "
                    << std::endl << "Fehler: " << std::endl <<
                    er.what() << std::endl;
        }
    } else {
        w_debug("Keine Nutzer-Konfiguration registriert!", "parser");
    }
    return EXIT_SUCCESS;
}