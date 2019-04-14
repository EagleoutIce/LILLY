#include "j_ins_Linux.hpp"

std::string exec(const std::string& command) {
    std::array<char, 128> buffer;
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

status_t ins_linux( void ) {
    int fb;
    std::cout << "  - Prüfe auf das Vorhandensein von pdflatex: "
              << er_decode(fb = system("which pdflatex > /dev/null"))
              << std::endl;
    
    if(fb) {
        char c_inp = '\0';
        std::cout << "  Jake kann 'pdflatex' nicht finden, dies ist für die Arbeit mit Lilly zwanghaft notwendig!" << std::endl
                  << "  Soll 'texlive-full' installiert werden?  [(y)es/(n)o]> ";
        while(c_inp != 'y' && c_inp != 'n')
            std::cin >> c_inp;
        if(c_inp == 'y') {
            std::cout << "Installiere 'texlive-full' via _apt_ : " << er_decode(system("sudo apt install texlive-full")) << std::endl;
        } else {
            std::cout << "  Jake installiert LILLY nun weiter, ohne 'pdflatex', da  du (n)o gewählt hast!" << std::endl;
        }
    }  

    std::cout << "  - Erstelle (" <<  settings["install-path"] << "/tex/latex): "
              << er_decode(system(("mkdir -p " + settings["install-path"] + "/tex/latex").c_str()))
              << std::endl;

    // Expand path:
    if (!check_file(exec("echo -n " + settings["lilly-path"])+"/Lilly.cls")) {
        std::cerr << COL_ERROR << "Die Lilly.cls konnte unter dem eingestellten Pfad: \"" << exec("echo -n " + settings["lilly-path"]) << "\" nicht gefunden werden. Bitte gib explizit einene anderen Pfad an! Hierzu stellt sich folgendes Muster zur Verfügung: " << std::endl
        << "    $ " << program << " -lilly-path=\"/pfad/zum/kuchen\" install" << std::endl
        << "Hierbei muss '/Lilly.cls' nichtmehr im Pfad angegben werden!" << COL_RESET << std::endl;
        std::cerr << "Hier eine Liste an möglichen Pfaden an denen eine 'Lilly.cls' gefunden wurde: " << std::endl;
        system("locate -e -q -l 5 'Lilly.cls'");
        return EXIT_FAILURE;
    }
    std::cout << "  - Verlinke (" << settings["lilly-path"] << ") nach (" << settings["install-path"] << "/tex/latex): "
              << er_decode(system(("ln -sf "+settings["lilly-path"]+" "+settings["install-path"]+"/tex/latex").c_str()))
              << std::endl;
    if(settings["lilly-path"]=="\"${PWD}/../../Lilly\"")
    std::cout << COL_ERROR << "    Information: es ist immer besser, wenn du den absoluten Pfad zu Lilly angibst. Nutze hierzu: " 
              << std::endl << "    $ " << program << " -lilly-path=\"/pfad/zum/kuchen\" install" << COL_RESET << std::endl;

    std::cout << "  - Informiere TEX über (" << settings["install-path"] << "): "
              << er_decode(system(("texhash " + settings["install-path"] + " > /dev/null").c_str()))
              << std::endl;
              
    std::cout << "Installationsprozess wurde abgeschlossen :D" << std::endl;
    
    return EXIT_SUCCESS;
}
