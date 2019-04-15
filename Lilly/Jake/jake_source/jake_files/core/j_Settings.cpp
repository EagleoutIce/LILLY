#include "j_Settings.hpp"

settings_t settings {
    {"operation",           "help"},
    {"file",                "none.tex"},
    {"debug",               "false"},
    {"path",                "./"},
    {"install-path",        "\"${HOME}/texmf\""},
    {"lilly-path",          "\"$(dirname $(locate -e -q -l 1 'Lilly.cls'))\""},
    {"mk-name",             "Makefile"},
    {"mk-path",             "./"},
    {"lilly-out",           "./$(BASENAME)-OUT/"}, // issues mkdir inside of makefile -- OS-Slave
    {"lilly-in",            "./"},
    {"lilly-nameprefix",    ""},
    {"lilly-boxes",         "DEFAULT "},
    {"lilly-modes",         "default print "},
    {"lilly-complete",      "false"},
    {"lilly-complete-name", "COMPLETE-"}, /// @todo move lilly-x-name to rule descriptor
    {"lilly-print-name",    "PRINT-"}, 
    {"lilly-cleans",        "log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock md5 SATZE ZSM UB TOP listing upa ilg TOPIC DEFS"},
    {"lilly-compiletimes",  "3"}, // wie oft soll pdflatex aufgerufen werden?
    {"lilly-autoclean",     "true"},
    {"jobcount",            "2"},
};
