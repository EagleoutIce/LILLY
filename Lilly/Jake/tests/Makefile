# Jake 1.0.7 - A Servant for Lilly     (compiled on: Apr 15 2019 at: 00:17:41)

TEXFILE      := test.tex
BASENAME     := $(basename $(TEXFILE))
PDFFILE      := $(addsuffix .pdf,$(basename $(TEXFILE)))
LATEXARGS    := -shell-escape -enable-write18 -interaction=batchmode
## Directories used for INPUT and OUTPUT Files 
OUTPUTDIR    := $(shell echo ./$(BASENAME)-OUT/| sed 's:/*$$::')/
INPUTDIR     := $(shell echo ./| sed 's:/*$$::')/
BOXMODES     := "DEFAULT"#                    ## Seperator: ' '
CLEANTARGETS := log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock md5 SATZE ZSM UB TOP listing upa ilg TOPIC DEFS
NAMEPREFIX   :=                               ## Immer

## Makefile/General settings
_LILLYARGS   :=  \\\providecommand{\\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) \\\providecommand{\\\LILLYxPATH}{${INPUTDIR}} \\input{$(INPUTDIR)$(TEXFILE)} 

JOBCOUNT     := 2                             ## <= CPU_COUNT! 


define LILLYxCompile = 
    @echo LOGFILE: $(date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1
    @for bm in $(BOXMODES); do \
    pdflatex -jobname $(basename ${1}) $(LATEXARGS) $(_LILLYARGS) ${2}\\\providecommand{\\\LILLYxBOXxMODE}{$$bm}\\\providecommand{\\\LILLYxPDFNAME}{${1}} >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && pdflatex -jobname $(basename ${1}) $(LATEXARGS) $(_LILLYARGS) ${2}\\\providecommand{\\\LILLYxBOXxMODE}{$$bm}\\\providecommand{\\\LILLYxPDFNAME}{${1}} >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && pdflatex -jobname $(basename ${1}) $(LATEXARGS) $(_LILLYARGS) ${2}\\\providecommand{\\\LILLYxBOXxMODE}{$$bm}\\\providecommand{\\\LILLYxPDFNAME}{${1}} >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo "\033[38;2;102;250;0mGenerierierung von "${1}" abgeschlossen\033[m"; done;
    $(call LILLYxClean)
endef
define LILLYxClean = 
    @echo "[38;2;255;191;0m> Lösche temporäre Dateien...[m"
    @for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done;
endef
default: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Standart-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{FALSE}))
print: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Druck-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)$(PDFFILE),\\providecommand\\LILLYxMODE{print}\\providecommand\\LILLYxMODExEXTRA{FALSE}))
