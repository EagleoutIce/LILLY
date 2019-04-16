#include "j_Settings.hpp"

settings_t settings {
    {"operation",           "help"},
    {"file",                "none.tex"},
    {"debug",               "false"},
    {"path",                "./"},
    {"install-path",        "\"${HOME}/texmf\""},
    {S_LILLY_PATH,          "\"$(dirname $(locate -e -q -l 1 'Lilly.cls'))\""},
    {"mk-name",             "Makefile"},
    {"mk-path",             "./"},
    {S_LILLY_OUT,           "./$(BASENAME)-OUT/"}, // issues mkdir inside of makefile -- OS-Slave
    {S_LILLY_IN,            "./"},
    {S_LILLY_NAMEPREFIX,    ""},
    {S_LILLY_BOXES,         "DEFAULT "},
    {S_LILLY_MODES,         "default print "},
    {S_LILLY_COMPLETE,      "false"},
    {S_LILLY_COMPLETE_NAME, "COMPLETE-"}, /// @todo move lilly-x-name to rule descriptor
    {S_LILLY_PRINT_NAME,    "PRINT-"}, 
    {S_LILLY_CLEANS,        "log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock md5 SATZE ZSM UB TOP listing upa ilg TOPIC DEFS"},
    {S_LILLY_COMPILETIMES,  "3"}, // wie oft soll pdflatex aufgerufen werden?
    {S_LILLY_AUTOCLEAN,     "true"},
    {"jobcount",            "2"},
};
