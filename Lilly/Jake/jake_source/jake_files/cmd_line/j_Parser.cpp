#include "j_Parser.hpp"

status_t ld_settings(int n /* = argc */, const char** argv) {
    for(; n > 0; n--) {
        if(!in_str(argv[n],ARG_PATTERN)){                       // not a single argument
            if(!in_str(argv[n],TEX_PATTERN)) {                  // not a .tex file
                settings["operation"] = TO_DATA(argv[n]);
            } else {                                            // is a .tex file
                settings["operation"] = "file_compile";
                settings["file"] = TO_DATA(argv[n]);
            }
        } else {                                                // is a single argument
            if(!in_str(argv[n], ASS_PATTERN)) {                 // not an assignment
                if(settings.find(argv[n] + 1 /* offset for '-' */) != settings.end()
                    && settings[argv[n]+1] == "false") {        // valid bool-setting
                    settings[argv[n]+1] = "true";
                } else {                                        // non valid bool-setting
                    er_unknown_setting(argv[n]+1);
                }
            } else {                                            // is an assignment
                std::string s{argv[n]+1};
                if(in_str(argv[n], ASA_PATTERN)) {              // is an addition statement
                    settings[s.substr(0,s.find(ASA_PATTERN))] += s.substr(s.find(ASA_PATTERN)+std::string(ASA_PATTERN).length());
                } else {                                        // normal statement
                    settings[s.substr(0,s.find(ASS_PATTERN))]  = s.substr(s.find(ASS_PATTERN)+std::string(ASS_PATTERN).length());
                }
            }
        }
    }
    return EXIT_SUCCESS;
}
