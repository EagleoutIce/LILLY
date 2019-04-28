#include "j_Settings.hpp"


settings_t generate_default_lilly_settings() {
    // Manuelles Zuweisen ist um einiges angenehmer :P (wenn auch langsamer)

    settings_t ret_settings;

    // ==== Generelle Einstellungen
    ret_settings["operation"]                  = {"help", "Beschreibt was getan werden soll", IS_OPERATION};
    ret_settings["file"]                       = {"none.tex", "Beschreibt die Datei die bearbeitet werden soll", IS_FILE};
    ret_settings["debug"]                      = {"false", "Sollen Meldungen ausgegeben werden?", IS_SWITCH};
    ret_settings["path"]                       = {"./", "Zum Beispiel: Pfad zu LILLY", IS_PATH};
    ret_settings["what"]                       = {R"(\\LILLYcommand)", "Spezifikator, um was gehts?", IS_TEXT};

    // ==== Installation
#if defined(__linux__)
    ret_settings["install-path"]               = {"\"${HOME}/texmf\"", "Wohin gilt es Lilly zu installieren?", IS_PATH};
    ret_settings[S_LILLY_PATH]                 = {"\"$(dirname $(locate -e -q 'Lilly.cls' | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\"",
                                                "Wo liegt die Lilly.cls?", IS_PATH};
#else
    ret_settings["install-path"]               = {"\"${HOME}/Library/texmf\"", "Wohin gilt es Lilly zu installieren?", IS_PATH};
    ret_settings[S_LILLY_PATH]                 = {"\"$(dirname $(mdfind Lilly.cls | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\"",
                                                "Wo liegt die Lilly.cls?", IS_PATH};
#endif

    // ==== Makefile Generierung
    ret_settings["mk-name"]                    = {"Makefile", "Name des Makefiles", IS_TEXT};
    ret_settings["mk-path"]                    = {"./", "Wo soll das Makefile hin?", IS_PATH};

    // ==== Externe Konfiguration
    ret_settings[S_GEPARDRULES_PATH]           = {"", "Konfigurationsdatei für Gepard", IS_CONFIG};

    // ==== Lilly Konfiguration
    ret_settings[S_LILLY_OUT]                  = {"./$(BASENAME)-OUT/", "Ausgabeordner der PDF-Datei", IS_PATH};
    ret_settings[S_LILLY_IN]                   = {"./", "Eingabeordner der TEX-Datei", IS_PATH};

    ret_settings[S_LILLY_NAMEPREFIX]           = {"", "Namenspräfix für PDF-Datei", IS_TEXT};
    ret_settings[S_LILLY_BOXES]                = {"DEFAULT", "Welche Boxmodi sollen unterstützt werden?", IS_TEXTLIST};
    ret_settings[S_LILLY_MODES]                = {"default print", "Welche Kompiliermodi sollen unterstützt werden?", IS_TEXTLIST};

    ret_settings[S_LILLY_COMPLETE]             = {"false", "Sollen vollständige Kompiliermodi angeboten werden?", IS_SWITCH};
    ret_settings[S_LILLY_COMPLETE_NAME]        = {"COMPLETE-", "Wie sollen die vollständigen Versionen genannt werden?", IS_TEXT};
    ret_settings[S_LILLY_PRINT_NAME]           = {"PRINT-", "Wie soll die Druck Version genannt werden", IS_TEXT};

    ret_settings[S_LILLY_CLEANS]               = {"log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock SATZE ZSM UB TOP listing upa ilg TOPIC DEFS",
                                                "Welche Dateieindungen liegen auf der Abschuss- (/clean-)liste?", IS_TEXTLIST};
    ret_settings[S_LILLY_COMPILETIMES]         = {"3", "Wie oft soll kompiliert werden?", IS_NUM};
    ret_settings[S_LILLY_AUTOCLEAN]            = {"true", "Soll automatisch aufgeräumt werden?", IS_SWITCH};
    ret_settings[S_LILLY_SHOW_BOX_NAME]        = {"true", "Sollen Boxnamen hinzugefügt werden?", IS_SWITCH};

    // ==== Besondere Lilly-Einstellungen
    ret_settings[S_LILLY_VORLESUNG]            = {"LAII", "Um was für eine Vorlesung handelt es sich?", IS_VLS}; // Fällt vorerst auf IS_TEXT zurück
    ret_settings[S_LILLY_N]                    = {"42", "Um das wievielte Übungsblatt handelt es sich?", IS_NUM}; 
    ret_settings[S_LILLY_SEMESTER]             = {"1", "Das wie vielte Semester ist es?", IS_NUM};

    ret_settings[S_LILLY_EXTERNAL]             = {"false", "Soll versucht werden tikzternal auszulagern?", IS_SWITCH};

    ret_settings[S_LILLY_LAYOUT_LOADER]        = {"", "Ladepfad für Layouts (Typ)", IS_PATH};

    // ==== Makefile Regeln
    ret_settings["jobcount"]                   = {"2", "Wie viele Jobs für multithreaded-Kompilieren", IS_NUM};

    return ret_settings;
}

__settings_t settings{generate_default_lilly_settings()};
