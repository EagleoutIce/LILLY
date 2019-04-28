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

std::string create_buildrule(const std::string& name_type, const std::string& name_rule, 
                             const std::string& mode, bool complete, const std::string& name_addon, 
                             const std::string& input_build, const std::string& l_comp_name/*, const std::vector<std::string>& others*/) noexcept {
    std::string ret_str = name_rule + ": $(INPUTDIR)$(TEXFILE)\n\t@echo \"";
    ret_str += "\\033[38;2;255;191;0mGeneriere " + (complete?l_comp_name:"") + name_type + "-Version($(BOXMODES)) der Latex-Datei: \"$(TEXFILE)\"...\\033[m\"\n";
    ret_str += std::string("\t") + R"(@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX))" + (complete?l_comp_name:"") + name_addon + R"(,$(PDFFILE),\\providecommand\\LILLYxMODE{)" 
                + mode + R"(}\\providecommand\\LILLYxMODExEXTRA{)" + (complete?"TRUE":"FALSE") + R"(},")" + input_build + "\")";
    return ret_str;
}

status_t assert_all_differ(settings_t theall, const std::string& thediffer, const std::string& flavour) {
        settings_t::iterator it = theall.begin();
        while (it != theall.end()){
            if(it->second.value == thediffer){
                std::cerr << COL_ERROR << "Die Option \"" << it->first << "\" ist für " << flavour << " verpflichtend! Bitte gib sie an" << COL_RESET << std::endl;
                if(warning_debug("Im debug-Modus wird diese Box verworfen!","Asserter") == EXIT_FAILURE)
                    throw std::runtime_error("MISSING NAME-SPECIFIER FOR BOX RUN WITH -debug FOR MORE INFORMATION");
                return EXIT_FAILURE;
            }
            ++it;
        }
    return EXIT_SUCCESS; 
}

std::string exec(const std::string& command) {
    std::array<char, 1024> buffer;
    std::string result;
    std::unique_ptr<FILE, decltype(&pclose)> pipe(popen(command.c_str(), "r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }
    return result;
}
