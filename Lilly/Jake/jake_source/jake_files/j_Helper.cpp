#include "j_Helper.hpp"

std::string padPrint(const std::string& entry, uint16_t toWidth) {
    if (toWidth<=entry.length()) return entry;
    return entry + std::string(toWidth-entry.length(), ' ');
}

std::vector<std::string> split(const std::string& str, char delim) {
    std::stringstream strstr(str); std::string buffer;
    std::vector<std::string> tokens;
    while(getline(strstr,buffer,delim))
        tokens.push_back(buffer);
    return tokens;
}

std::string create_buildrule(const std::string& name_type, const std::string& name_rule, const std::string& mode, bool complete, const std::string& name_addon/*, const std::vector<std::string>& others*/) noexcept {
    std::string ret_str = name_rule + ": $(INPUTDIR)$(TEXFILE)\n\t@echo \"";
    ret_str += "\\033[38;2;255;191;0mGeneriere " + name_type + "-Version($(BOXMODES)) der Latex-Datei: \"$(TEXFILE)\"...\\033[m\"\n";
    ret_str += std::string("\t") + R"(@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX))" + name_addon + R"(,$(PDFFILE),\\providecommand\\LILLYxMODE{)" + mode + R"(}\\providecommand\\LILLYxMODExEXTRA{)" + (complete?"TRUE":"FALSE") + R"(}))";
    return ret_str;
}
