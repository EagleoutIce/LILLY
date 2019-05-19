#include "j_Functions.hpp"

status_t fkt_dump(const std::string& cmd) noexcept {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Liefere die Konfigurationen (fkt_dump)", "func");
    std::cout << "Settings Dump: " << std::endl
              << "Information: Die '[' ']' gehören jeweils nicht zum Wert, sie dienen lediglich der Übersicht! Durch '=>' gekennzeichnet ergibt sich die erweiterte Variante der Ausdrücke" << std::endl;
    settings_t::iterator it = settings.begin();
    std::string tmp;
    configuration_t expandables = getExpandables(settings[S_GEPARDRULES_PATH]);
    while(it != settings.end()){ // iterate over all settings
        // simple padding without <iomanip> std::setw
        std::cout << it->first << ": " << std::string(20-it->first.length(),' ')
                  << STY_PARAM << "[" << it->second.value << "]";
        tmp = expand(expandables,it->second.value);
        if (tmp != it->second.value)
          std::cout << "  =>  [" << tmp << "]";
        std::cout << COL_RESET << std::endl;
        ++it;
    }
    return EXIT_SUCCESS;
}

status_t fkt_help(const std::string& cmd) noexcept {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Gebe die Hilfe aus (fkt_help)", "func");
    std::cerr << "Benutzung:" << std::endl << std::endl;
    std::cerr << program << " [options=help] [file]" << std::endl << std::endl;
    std::cerr << "[options]:" << std::endl;
    functions_t::iterator it = functions.begin();
    while(it != functions.end()){ // iterate over all functions
        if (it->first.length()>0 && it->first[0] != '_')
        std::cout << "  " << it->first << " " << std::string(15-it->first.length(),' ')<< it->second.brief_description
                  << std::endl;                                 // simple padding without <iomanip> std::setw
        ++it;
    }
    std::cerr << std::endl << "[file]:" << std::endl
              << "Angabe gemäß \"xxx.tex\". Dies setzt die Operation automatisch auf file_compile und generiert damit \
                  ein generelles makefile für \"xxx.tex\"."
              << std::endl;

    std::cerr << std::endl << "note:" << std::endl
              << "Allgemeine Einstellungen können über \"-key" << ASS_PATTERN << " value\" gesetzt werden (\"-key\" für boolesche). So "
              << "setzt: \"-path" << ASS_PATTERN <<  " /es/gibt/kuchen\" die Einstellung settings[\"path\"] auf besagten Wert: "
              << "\"/es/gibt/kuchen\". Weiter ist es möglich mit '" << ASA_PATTERN << "' values hinzuzufügen."
              << std::endl;
    return EXIT_SUCCESS;
}

status_t fkt_install(const std::string& cmd) noexcept {
    w_debug("Beginne mit der Installation (fkt_install) aufruf an ins:", "func");
    w_debug4("Morgeeen, na wie gehts? ", "inst","INF","", DEBUG_8BIT_FOREGROUND(33));
//Determine Operating System
#if defined(__linux__)
    std::cout << "Betriebsystem wurde als Linux-Basiert identifiziert - versuche ins_linux()" << std::endl;
    ins_linux();
#elif defined(__APPLE__) || defined(__MACH__)
    std::cout << "Betriebsystem wurde als MacOS identifiziert - versuche ins_macOS()" << std::endl;
    ins_macOS();
#else
    std::cerr << "Es existiert keine Installationsregel für dein Betriebssystem :/ - Bitte melde dich beim Maintainer dieses Pakets!" << std::endl;
#endif
   return EXIT_SUCCESS;
}

status_t fkt_compile(const std::string& cmd) {
    if(settings["mk-use"]=="false"){ return c_jake(cmd); }
    else { return c_makefile(cmd); }
}


status_t fkt_tokentest(const std::string& cmd) {
    // test für den Tokenizer :D
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    std::cout << "Einzelne Gruppen werden mit \"~\" getrennt!" << std::endl;
    Tokenizer t(settings["file"]);
    while(t.loadNext()) {
        Tokenizer::Match m = t.get();
        if(m.failure()) continue;
        for(int i = 1; i < m.matchings.size(); ++i){
            std::cout << m.matchings[i] << "~";
        }
        std::cout << "#" << std::endl;
    }
    return EXIT_SUCCESS;
}

uint8_t RECURSIVE_CALLCOUNTER = 0;

status_t fkt_config(const std::string& cmd) {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Jetzt in: fkt_config","func");
    if(RECURSIVE_CALLCOUNTER++ > MAX_SETTINGS_REC) {
        std::cerr << COL_ERROR << "Du hast das Limit an Konfigurationsaufrufen erreicht! Mehr erscheint wirklich nicht sinnvoll!"
                  << COL_RESET << std::endl;
        throw std::runtime_error("Zu viele Konfigurationsdateien oder ungültige Konfigurationsdatei. Stelle sicher die 'operation' zu ändern!");
    }
    Configurator c(settings["file"]);
    c.parse_settings(&settings._settings);
    return in_settings(cmd);
}

// export d_path=$(dirname `which lilly_jake`)/../../Lilly/source/Data/Graphics/ && find ${d_path} | grep -E "${d_path}.*QUERY.*\.tex" 

status_t fkt_get(const std::string& cmd) noexcept {
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Versuche nun alle Grafiken auf Basis von QUERY (-settings[what]) zu erstellen" + log_path,"func");

    // Have fun debugging that future idiot :P 
    std::cout << "Suche: " << settings["what"] << " " << er_decode(system ((R"(bash -c 'rm -r /tmp/gcol/ > /dev/null; mkdir /tmp/gcol/ && export d_path=$(dirname `which lilly_jake`)/../../Lilly/source/Data/Graphics/ && export tfile=$(tempfile --prefix gcol/ --suffix .tex) && echo -e "\\documentclass[Typ=PLAIN]{Lilly}\n\\\\begin{document}\n \\\\verb|Ergebnisse der Suche: )" + settings["what"] + R"(|\\\\\\\\ \n Die Größe der Grafiken wurde angepasst! \\\\bigskip \\\\\\\\ \n\\\\begin{tabular}{m{0.45\linewidth}>{\\\\centering\\\\arraybackslash}m{0.5\linewidth}}\\\\toprule \\\\bfseries Code &\\\\bfseries Ergebnis\\\\\\\\ \n\\\\midrule\n" > ${tfile} && for a in $(find ${d_path} | grep -E "${d_path}.*)" + settings["what"] + R"(.*\.tex" | sort ); do echo -e  "{\\\\small\\\\begin{lstlisting}[style=latex,frame=none, morekeywords={)" + settings["what"] + R"(}]\n\\\\framebox{%\n  \\\\getGraphics{${a/${d_path}/}}%\n}\\\\end{lstlisting}} & {\\\\begin{center}\\\\resizebox{0.97\\\\linewidth}{!}{\\\\framebox{\\getGraphics{${a/${d_path}/}}}}\\\\end{center}}\\\\\\\\ \n" >> ${tfile}; done && echo -e  "\n\\\\bottomrule\\\\end{tabular}\\\\end{document}" >> ${tfile} && cd /tmp/gcol && lilly_jake $(basename ${tfile}) -debug: false -lilly-show-boxname: false>/dev/null&& make >/dev/null && echo /tmp/gcol/$(basename ${tfile%.*})-OUT/ && xdg-open /tmp/gcol/$(basename ${tfile%.*})-OUT/$(basename ${tfile%.*}).pdf 2>/dev/null')").c_str())) << std::endl;

    return EXIT_SUCCESS;
    //return system(("grep -E \"" + settings["what"] + "\" -r * -hs").c_str());
}

status_t fkt_autoget(const std::string& cmd) noexcept{
    std::cout << parse_cmd_line_autocomplete(settings["what"]);
    return EXIT_SUCCESS;
}

status_t fkt_update(const std::string& cmd) noexcept{
    w_debug("Refresh: Logpfad lautet: " + log_path,"STAT");
    w_debug("Versuche Lilly zu Aktualisieren (fkt_update)","func");
    std::cout << "Aktualisiere Lilly: " << std::endl;
    if(settings["path"] == "./") {
        std::cout << "Pfad soll nicht überschrieben werden, nutze alten (${LILLY_JAKE_CONFIG_PATH}=" << exec("printf ${LILLY_JAKE_CONFIG_PATH}") << ")" << std::endl;
        w_debug("Aktualisiere: export tf=$(mktemp lilly.XXXXXXXXXX --tmpdir) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"${LILLY_JAKE_CONFIG_PATH}\" > ${tf} && lilly_jake install -debug >> ${tf}", "update");
        std::cout << "Update: " << er_decode(system("export tf=$(mktemp lilly.XXXXXXXXXX --tmpdir) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"${LILLY_JAKE_CONFIG_PATH}\" > ${tf} && lilly_jake install -debug >> ${tf}")) << std::endl;
    } else {
        std::cout << "Pfad soll überschrieben werden, nutze neuen (settings[\"path\"]=" << settings["path"] << ")" << std::endl;
        w_debug("Aktualisiere: export tf=$(mktemp lilly.XXXXXXXXXX --tmpdir) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"settings[\"path\"]\" > ${tf} && lilly_jake install -debug >> ${tf}", "update");
        std::cout << "Update: " << er_decode(system(("export tf=$(mktemp lilly.XXXXXXXXXX --tmpdir) && echo \"Schreibe Update-Log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../ && git pull && cd Jake/source && make CONFIG=\"" + settings["path"] + "\" > ${tf} && lilly_jake install -debug >> ${tf}").c_str())) << std::endl;
    }
#if defined(__linux__) 
    std::cout << "Soll eine Dokumentation generiert werden?" << std::endl;

    char answer= get_answer();
    if(answer=='y') {
        w_debug("Prüfe auf doxygen und graphviz.","update");
        if(system("which doxygen > /dev/null") || system("which dot > /dev/null")) {
            w_debug("Prüfung gescheitert","update");
            std::cout << "Du verfügst nicht über mindestens eins der beiden benötigten Pakete doxygen und graphviz. Soll Jake sie für dich installieren? " << std::endl;
            answer=get_answer();
            if(answer=='y') {
                std::cout << "Installiere Pakete: " << er_decode(install("doxygen")) << er_decode(install("graphviz")) << std::endl;
            } else {
                std::cerr << COL_ERROR << "Abbruch!" << COL_RESET << std::endl;
                return EXIT_FAILURE;
            }
        }
        w_debug("Aktualisiere mit: export tf=$(mktemp lilly.XXXXXXXXXX --tmpdir) && echo \"Schreibe Dokumentations-log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../Jake/source && make documentation > ${tf} 2>&1","update");
        std::cout << "Erstelle Dokumentation... " << er_decode(system("export tf=$(mktemp lilly.XXXXXXXXXX --tmpdir) && echo \"Schreibe Dokumentations-log in: ${tf}\" && cd $(dirname $(which lilly_jake))/../../Jake/source && make documentation > ${tf} 2>&1")) << std::endl;
    } else {
        std::cout << "Fertig!" << std::endl;
    }
#endif
    return EXIT_SUCCESS;
}


functions_t functions = {
    {"help", {fkt_help, "Zeigt diese Hilfe an"}},
    {"dump", {fkt_dump, "Zeigt alle settings und ihre Werte an" }},
    {"file_compile", {fkt_compile, "Erstellt ein makefile für settings[\"file\"]"}},
    {"install", {fkt_install, "Versucht LILLY zu installieren"}},
    {"tokentest", {fkt_tokentest, "Testet den Tokenizer auf seine Funktionalität"}},
    {"config", {fkt_config, "Lädt die Einstellungen aus der Datei 'file'"}},
    {"get", {fkt_get, "Sucht nach Grafiken die settings[\"what\"] enthalten!"}},
    {"update", {fkt_update, "Versucht Lilly & Jake zu aktualisieren"}},
    {"_get", {fkt_autoget, "Interne Funktion, liefert Alles für die Autovervollständigung"}}
};