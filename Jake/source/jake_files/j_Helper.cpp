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
    if(settings["mk-use"] == "true"){
        std::string ret_str = name_rule + ": $(INPUTDIR)$(TEXFILE)\n\t@echo \"";
        ret_str += "\\033[38;2;255;191;0mGeneriere " + (complete?l_comp_name:"") + name_type + "-Version($(BOXMODES)) der Latex-Datei: \"$(TEXFILE)\"...\\033[m\"\n";
        ret_str += std::string("\t") + R"(@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX))" + (complete?l_comp_name:"") + name_addon + R"(,$(PDFFILE),\\providecommand\\LILLYxMODE{)" 
                    + mode + R"(}\\providecommand\\LILLYxMODExEXTRA{)" + (complete?"TRUE":"FALSE") + R"(},")" + input_build + "\")";
        return ret_str;
    } else {
        // return  Name!EXTRACOMMANDS!INPUT!Brief-Text
        return settings[S_LILLY_OUT] + settings[S_LILLY_NAMEPREFIX] + (complete?l_comp_name:"") + name_addon + "!" + R"(\\providecommand\\LILLYxMODE{)" 
                    + mode + R"(}\\providecommand\\LILLYxMODExEXTRA{)" + (complete?"TRUE":"FALSE") + R"(}!)" + input_build + "!\033[38;2;255;191;0mGeneriere " + (complete?l_comp_name:"") + name_type;
    }
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


status_t install(const std::string& install_str, const std::string& backup) {
w_debug("Versuche \"" + install_str + "\" zu installieren...","p-inst");
#if defined(__linux__) 
// identify package manager
w_debug("Versuche nun Paket-Manager zu identifizieren...","p-inst");
std::string pm = exec(R"(which bash >/dev/null && bash -c 'declare -A supportedOS; supportedOS[/etc/arch-release]="pacman -S"; supportedOS[/etc/debian_version]="apt-get install"; supportedOS[/etc/SuSE-release]="zypp install"; for file in ${!supportedOS[@]}; do if [[ -f $file ]]; then printf "${supportedOS[$file]}"; fi; done' || printf "error")");

if(pm=="error"){
    w_debug("Identifikation gescheitert, falle auf \"apt\" zurück","p-inst");
    pm = "apt install";
} else w_debug("Identifiziert: \"" + pm + "\"", "p-inst");
int fb = system(("sudo " + pm + " " + install_str + " 2>/dev/null").c_str());
if(fb && backup != "") {
    w_debug("Installation gescheitert, versuche: \"" + backup +"\"", "p-inst");
    fb = system(("sudo " + backup + " " + install_str + " 2>/dev/null").c_str());
}
return fb;

#elif defined(__APPLE__) || defined(__MACH__)
    #warning currently the installation for macOS is unsafe and work in progress
    return system("sudo brew install " + install_str+ " 2>/dev/null");
#else 
    #warning unsupported install architecture
    w_debug("Keine Installation, Betriebssystem nicht Unterstützt","p-inst");
#endif
}

char get_answer(const std::string& prompt, const std::string& options) {
    if(settings["answer"].find_first_of(options) != std::string::npos) return settings["answer"][0];
    std::string answer = "\0";
    while(answer.find_first_of(options) == std::string::npos){
        std::cerr << prompt; std::getline(std::cin,answer);
        if(answer.length() > 0)
        answer = answer[0];
    }
    return answer[0];
}

status_t generate_dummyfile(const std::string name) {
    std::ofstream out_texfile(name, std::fstream::out);

    if(!out_texfile.good()) return EXIT_FAILURE;

    out_texfile << R"(%% Von Jake erstelltes Lilly-Texfile :D)" << std::endl << std::endl
                << R"(\documentclass[Typ=Mitschrieb,Jake]{Lilly})" << std::endl<<std::endl
                << R"(\begin{document})" << std::endl
                << R"(Hallo Welt!\newline)" << std::endl
                << R"(Lilly-Version: \LILLYxVERSIONxLONG!\newline)" << std::endl
                << R"(Status: \LILLYxSTATUS\newline)" << std::endl
                << R"(Colormap: \LILLYxCOLORxRainbow)" << std::endl
                << R"(\end{document})" << std::endl;
    out_texfile.close();
    return EXIT_SUCCESS;
}