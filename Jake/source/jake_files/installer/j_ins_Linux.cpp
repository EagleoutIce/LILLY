#include "j_ins_Linux.hpp"

void li_show_error( void ){
    std::cerr<< " Bitte gib explizit einen anderen Pfad an! Hierzu stellt sich folgendes Muster zur Verfügung: " << std::endl
    << "    $ " << program << " -lilly-path=\"/pfad/zum/kuchen\" install" << std::endl
    << "Hierbei muss '/Lilly.cls' nichtmehr im Pfad angeben werden!" << COL_RESET << std::endl;
    std::cerr << "Hier eine Liste an möglichen Pfaden an denen eine 'Lilly.cls' gefunden wurde: " << std::endl;
    system("locate -e -q -l 5 'Lilly.cls'");
}

status_t ins_linux( void ) {
    w_debug4("Im installer: Linux", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
    int fb;

    w_debug4("Teste Vorhandensein durch: 'which pdflatex > /dev/null'", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
    std::cout << "  - Prüfe auf das Vorhandensein von pdflatex: "
              << er_decode(fb = system("which pdflatex > /dev/null"))
              << std::endl;

    if(fb) {
        w_debug4("er_decode(fb = system(\"which pdflatex > /dev/null\")) != 0 => Frage nach Installation", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
        std::cout << "  Jake kann 'pdflatex' nicht finden, dies ist für die Arbeit mit Lilly zwanghaft notwendig!" << std::endl
                  << "  Soll 'texlive-full' installiert werden?";

        if(get_answer() == 'y'){
            w_debug4("Installation scheint erwünscht!", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
            std::cout << "Installiere 'texlive-full' via install() : " << std::endl << er_decode(install("texlive-most mlocate texlive-lang texlive-langextra biber","texlive-full")) << std::endl;
        }else
            std::cout << "  Jake installiert LILLY nun weiter, ohne 'pdflatex', da  du (n)o gewählt hast!" << std::endl;
    }

    w_debug4("Erstelle Installationspfad auf Basis von settings[\"install-path\"](=" + settings["install-path"] + "): system((\"mkdir -p \" + settings[\"install-path\"] + \"/tex/latex\").c_str())" , "inst","INF","", DEBUG_8BIT_FOREGROUND(33));

    std::cout << "  - Erstelle (" <<  settings["install-path"] << "/tex/latex): "
              << er_decode(system(("mkdir -p " + settings["install-path"] + "/tex/latex").c_str()))
              << std::endl;


    // Expand path:
    if (!check_file(exec("echo -n " + settings[S_LILLY_PATH])+"/Lilly.cls")) {
        std::cerr << COL_ERROR << "Die Lilly.cls konnte unter dem eingestellten Pfad: \""
                << exec("echo -n " + settings[S_LILLY_PATH]) << "\" nicht gefunden werden. "
                << "Soll die Datenbank aktualisiert werden? Dies kann etwas dauern!" << std::endl;

        if(get_answer() == 'y') std::cout << "Aktualisiere die Datenbank 'sudo updatedb': " << er_decode(system("sudo updatedb")) << std::endl;

        if (!check_file(exec("echo -n " + settings[S_LILLY_PATH])+"/Lilly.cls")) {

          std::cerr << COL_ERROR << "Die Lilly.cls konnte nun wieder nicht gefunden werden :/ "
          << "Soll eine ausführliche Suche gestartet werden? Dies kann etwas dauern!" << std::endl;

          if(get_answer() == 'y') {
              std::string path;
              std::cout << "Suche Lilly.cls: " << (path = exec("echo -n $(dirname $(find \"${HOME}\" / -name Lilly.cls 2>/dev/null))")) << std::endl;
              if(check_file(path + "/Lilly.cls")){
                  std::cout << "Lilly wurde erfolgreich gefunden! Aktualisiere Einstellungen :D" << COL_RESET << std::endl;
                  settings[S_LILLY_PATH] = path;
              } else {
                  li_show_error();
              }
          } else {
                  li_show_error();
                  return EXIT_FAILURE;
              }
          }
    }

    //check for MiKTeX
    w_debug4("Teste auf aktive MiKTeX Konfiguration: 'pdflatex --version | grep MiKTeX' == \"\"", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
    std::cout << "  - Prüfe auf aktive MiKTeX Konfiguration..." << std::endl;
    if(exec("printf \" $(pdflatex --version | grep MiKTeX)\"").find_first_not_of(" ") != std::string::npos) {
        //MiKTeX installed
        /* cSpell:disable */
        std::cout << "  MiKTeX wurde als aktiv identifiziert. Jake wird die Installation dementsprechend anpassen." << std::endl;
        std::cout << "  - Kopiere Inhalt von " << settings[S_LILLY_PATH] << " nach " << settings["install-path"] << ": "
                << er_decode(system(("cp -u -R " + settings[S_LILLY_PATH] + "/* " + settings["install-path"] + "/tex/latex").c_str())) << std::endl;
        std::cout << "  - Prüfe, ob das Verzeichnis MiKTeX bekannt ist: " << er_decode(fb = system(("cat /var/lib/miktex-texmf/miktex/config/miktexstartup.ini | grep " + settings["install-path"]).c_str())) << std::endl;
        if(fb) {
            //not in config
            std::cout << "  Das Verzeichnis ist MiKTeX nicht bekannt. Jake wird nun die Konfigurationsdatei ergänzen." << std::endl;
            std::cout << "  - Schreibe in /var/lib/miktex-texmf/miktex/config: "
                    << er_decode(system(("sudo sh -c \"echo 'CommonRoots;=" + settings["install-path"] + "' >> /var/lib/miktex-texmf/miktex/config/miktexstartup.ini\"").c_str())) << std::endl;
        }
        std::cout << "  Fahre nun mit der Installation fort." << std::endl;
    } else {
        std::cout << "  MiKTeX wurde als nicht-aktiv identifiziert. Jake baut auf einer texlive-Implementierung auf." << std::endl;
        //normal installation
        std::cout << "  - Verlinke (" << settings[S_LILLY_PATH] << " = " << exec("echo -n " + settings[S_LILLY_PATH])
                  << ") nach (" << settings["install-path"] << "/tex/latex): "
                  << er_decode(system(("ln -sf " + settings[S_LILLY_PATH] + " " + settings["install-path"] +
                                       "/tex/latex").c_str()))
                  << std::endl;
    }
    /* cSpell:enable */
    if(settings[S_LILLY_PATH]=="\"$(dirname $(locate -e -q 'Lilly.cls' | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\"")
    std::cout << COL_ERROR << "    Information: es ist immer besser, wenn du den absoluten Pfad zu Lilly angibst."<< std::endl
              << "    Nutze hierzu: "
              << std::endl << "    $ " << program << " -lilly-path" << ASS_PATTERN << " \"/pfad/zum/kuchen\" install" << COL_RESET << std::endl;

    std::cout << "  - Informiere TEX über (" << settings["install-path"] << "): "
              << er_decode(system(("texhash " + settings["install-path"] + " > /dev/null").c_str()))
              << std::endl;

    std::cout << "  ? Experimental sucht Jake alle Pakete heraus die Lilly benötigen könnte um dann, " << std::endl << "    in einer späteren Version, Variable Installationen anzubieten :D" << std::endl;

    //for i in $(cd ~/texmf/tex/latex/Lilly/ && grep -E "(LILLYxDemandPackage|LILLYxLoadPackage){([a-zA-Z]+[a-zA-Z0-9]*)}" -r * --exclude=_LILLY_PACKAGE_CTRL.tex -hos | grep -E "{[a-zA-Z]+[a-zA-Z0-9]*" -os); do if [[ "$i" == "LILLYxLoadPackage" ]]; then export a=load && continue; elif [[ "$i" == "LILLYxDemandPackage" ]]; then export a=demand && continue; fi;  echo "- ($a): $i"; done;

    std::cout << "Sammlung der Pakete abgeschlossen: " << er_decode(system(("for i in $(cd " + settings["install-path"] + "/tex/latex && grep -E " + \
                    "\"(LILLYxDemandPackage|LILLYxLoadPackage){([a-zA-Z]+[a-zA-Z0-9]*)}\" -r * " + \
                    "-hos | grep -E '[a-zA-Z]+[a-zA-Z0-9]*' -os); do if " +\
                    "[ \"$i\" = \"LILLYxLoadPackage\" ]; then export a=load && continue; elif " + \
                    "[ \"$i\" = \"LILLYxDemandPackage\" ]; then export a=demand && continue; fi; " +\
                    "echo \"      - ($a): $i\"; done;").c_str())) << std::endl;


    //kpsewhich -progname=pdflatex Lilly.cls
    w_debug4("Verify install: " + er_decode(system("kpsewhich -progname=pdflatex Lilly.cls")), "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
    std::cout << "Der Installationsprozess wurde abgeschlossen :D" << std::endl;

    return EXIT_SUCCESS;
}

