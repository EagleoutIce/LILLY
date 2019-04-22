#include "j_Parser.hpp"

status_t ld_settings(int n /* = argc */, const char** argv) {
    for(int i = 1; i <= n; i++) { // 0 = Programmaufruf (wie z.B. lilly_jake)
        if(argv[i][0] != ARG_PATTERN){                       // not a single argument
            if(!in_str(argv[i],TEX_PATTERN)) {                  // not a .tex file
                settings["operation"] = TO_DATA(argv[i]);
            } else {                                            // is a .tex file
                settings["operation"] = "file_compile";
                settings["file"] = TO_DATA(argv[i]);
                if (std::string(TO_DATA(argv[i])).find("uebungsblatt") != std::string::npos  
                    || std::string(TO_DATA(argv[i])).find("UBdata") != std::string::npos ) // Definitiv zu faul im intensitive search einzubauen
                        settings[S_LILLY_MODES] = "uebungsblatt";
            }
        } else {                                                // is a single argument
            if(!in_str(argv[i], ASS_PATTERN)) {                 // not an assignment
                if(settings.find(argv[i] + 1 /* offset for '-' */) != settings.end()) {
                    if(settings[argv[i]+1] == "false") {        // valid bool-setting
                        settings[argv[i]+1] = "true";
                    } else if (settings[argv[i]+1] == "true") // Das ist zwar dumm, aber ich bin faul :D
                        settings[argv[i]+1] = "false";
                    else                                         // non valid bool-setting
                        er_unknown_setting(argv[i]+1);
                } else                                         // non valid bool-setting
                    er_unknown_setting(argv[i]+1); // zu faul zum entkaspeln
            } else {                                            // is an assignment
                std::string s{argv[i]+1};
                if(in_str(argv[i], ASA_PATTERN)) {              // is an addition statement
                    settings[s.substr(0,s.find(ASA_PATTERN))] += s.substr(s.find(ASA_PATTERN)+std::string(ASA_PATTERN).length());
                } else {                                        // normal statement
                    settings[s.substr(0,s.find(ASS_PATTERN))]  = s.substr(s.find(ASS_PATTERN)+std::string(ASS_PATTERN).length());
                }
            }
        }
    }
    return EXIT_SUCCESS;
}

status_t in_settings(std::string v0) {
    if(functions.find(settings["operation"]) != functions.end()) {                   // Operation ist valide
            functions[settings["operation"]].fkt(v0);                                // Führe Operation aus
    } else {
        er_unknown_setting(("operation (=" + settings["operation"] + ")").c_str());  // Diese Operation kenne ich nicht
        return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
}
