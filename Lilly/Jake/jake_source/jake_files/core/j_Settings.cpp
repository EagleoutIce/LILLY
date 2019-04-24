#include "j_Settings.hpp"

settings_t settings {
    {"operation",           "help"},
    {"file",                "none.tex"},
    {"debug",               "false"},
    {"path",                "./"},
    {"what",                "\\\\LILLYcommand"}, // operation specifier for get or so
#if defined(__linux__)
    {"install-path",        "\"${HOME}/texmf\""},
    {S_LILLY_PATH,          "\"$(dirname $(locate -e -q 'Lilly.cls' | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\""},
#else //if defined __APPLE__ oder __MACH__
    {"install-path",        "\"${HOME}/Library/texmf\""},
    {S_LILLY_PATH,          "\"$(dirname $(mdfind Lilly.cls | grep -v -e \".Trash\" -e \".vim\" -i -e \"backup\" | head -1))\""},
#endif
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
    {S_LILLY_SHOW_BOX_NAME, "true"},
    {"jobcount",            "2"},
    {S_LILLY_VORLESUNG,     "LAII"},
    {S_LILLY_N,             "42"},
    {S_LILLY_SEMESTER,      "1"},
    {S_GEPARDRULES_PATH,     ""}
};
