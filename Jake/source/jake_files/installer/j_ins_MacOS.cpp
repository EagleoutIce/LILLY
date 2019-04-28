#include "j_ins_MacOS.hpp"

status_t ins_macOS( void ) {
    int fb;
    std::cout << "  - Prüfe auf das Vorhandensein von pdflatex: "
              << er_decode(fb = system("which pdflatex > /dev/null"))
              << std::endl;
    char c_inp = '\0';
    if(fb) {
        std::cout << "  Jake kann 'pdflatex' nicht finden, dies ist für die Arbeit mit Lilly zwanghaft notwendig!" << std::endl
                  << "  Soll 'texlive-full' installiert werden?  [(y)es/(n)o]> ";
        while(c_inp != 'y' && c_inp != 'n')
            std::cin >> c_inp;
        if(c_inp == 'y')
            std::cout << "Installiere (ungetestet) 'texlive-full' via _macports_ : " << er_decode(system("sudo port install texlive-full")) << std::endl;
        else
            std::cout << "  Jake installiert LILLY nun weiter, ohne 'pdflatex', da  du (n)o gewählt hast!" << std::endl;
    }

    std::cout << "  - Erstelle (" <<  settings["install-path"] << "/tex/latex): "
              << er_decode(system(("mkdir -p " + settings["install-path"] + "/tex/latex").c_str()))
              << std::endl;

    // Expand path:
    if (!check_file(exec("printf " + settings[S_LILLY_PATH])+"/Lilly.cls")) {
        std::cerr << COL_ERROR << "Die Lilly.cls konnte unter dem eingestellten Pfad: \""
                << (exec("printf " + settings[S_LILLY_PATH])+"/Lilly.cls") << "\" nicht gefunden werden. " << std::endl;

                std::cerr<< " Bitte gib explizit einene anderen Pfad an! Hierzu stellt sich folgendes Muster zur Verfügung: " << std::endl
                << "    $ " << program << " -lilly-path=\"/pfad/zum/kuchen\" install" << std::endl
                << "Hierbei muss '/Lilly.cls' nichtmehr im Pfad angegben werden!" << COL_RESET << std::endl;

                return EXIT_FAILURE;
    }

    std::cout << "  - Verlinke (" << settings[S_LILLY_PATH] << ") nach (" << settings["install-path"] << "/tex/latex): "
              << er_decode(system(("ln -sf "+settings[S_LILLY_PATH]+" "+settings["install-path"]+"/tex/latex").c_str()))
              << std::endl;
    if(settings[S_LILLY_PATH]=="\"$(dirname $(mdfind Lilly.cls | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\"")
    std::cout << COL_ERROR << "    Information: es ist immer besser, wenn du den absoluten Pfad zu Lilly angibst. Nutze hierzu: "
              << std::endl << "    $ " << program << " -lilly-path" << ASS_PATTERN << " \"/pfad/zum/kuchen\" install" << COL_RESET << std::endl;

    std::cout << "  - Informiere TEX über (" << settings["install-path"] << "): "
              << er_decode(system(("sudo texhash " + settings["install-path"] + " > /dev/null").c_str()))
              << std::endl;

    std::cout << "  ? Experimental sucht Jake alle Pakete heraus die Lilly benötigen könnte um dann, in einer späteren Version," << std::endl << "    Variable Installationen anzubieten :D" << std::endl;

    //for i in $(cd ~/texmf/tex/latex/Lilly/ && grep -E "(LILLYxDemandPackage|LILLYxLoadPackage){([a-zA-Z]+[a-zA-Z0-9]*)}" -r * --exclude=_LILLY_PACKAGE_CTRL.tex -hos | grep -E "{[a-zA-Z]+[a-zA-Z0-9]*" -os); do if [[ "$i" == "LILLYxLoadPackage" ]]; then export a=load && continue; elif [[ "$i" == "LILLYxDemandPackage" ]]; then export a=demand && continue; fi;  echo "- ($a): $i"; done;

    std::cout << "Sammlung der Pakete abgeschlossen: " << er_decode(system(("for i in $(cd " + settings["install-path"] + "/tex/latex && grep -E " + \
                    "\"(LILLYxDemandPackage|LILLYxLoadPackage){([a-zA-Z]+[a-zA-Z0-9]*)}\" -r * " + \
                    "--exclude=_LILLY_PACKAGE_CTRL.tex -hos | grep -E '[a-zA-Z]+[a-zA-Z0-9]*' -os); do if " +\
                    "[ \"$i\" = \"LILLYxLoadPackage\" ]; then export a=load && continue; elif " + \
                    "[ \"$i\" = \"LILLYxDemandPackage\" ]; then export a=demand && continue; fi; " +\
                    "echo \"      - ($a): $i\"; done;").c_str())) << std::endl;

    std::cout << "Der Installationsprozess wurde abgeschlossen :D" << std::endl;

    return EXIT_SUCCESS;
}
