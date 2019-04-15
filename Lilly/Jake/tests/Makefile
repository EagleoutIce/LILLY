# Jake 1.0.7 - A Servant for Lilly     (compiled on: Apr 15 2019 at: 13:21:59)

TEXFILE      := test.tex
BASENAME     := $(basename $(TEXFILE))
PDFFILE      := $(addsuffix .pdf,$(basename $(TEXFILE)))
LATEXARGS    := -shell-escape -enable-write18 -interaction=batchmode
## Directories used for INPUT and OUTPUT Files 
OUTPUTDIR    := $(shell echo ./$(BASENAME)-OUT/| sed 's:/*$$::')/
INPUTDIR     := $(shell echo ./| sed 's:/*$$::')/
BOXMODES     := DEFAULT ALTERNATE#            ## Seperator: ' '
CLEANTARGET  := LILLYxClean
CLEANTARGETS := log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock md5 SATZE ZSM UB TOP listing upa ilg TOPIC DEFS
NAMEPREFIX   :=                               ## Immer

## Makefile/General settings
_LILLYARGS   :=  \\providecommand{\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) \\providecommand{\\LILLYxPATH}{${INPUTDIR}} \\input{$(INPUTDIR)$(TEXFILE)} 

JOBCOUNT     := 2                             ## <= CPU_COUNT! 


define LILLYxCompile = 
    @echo LILLY_LOGFILE stamp: $(shell date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1
    @for bm in $(BOXMODES); do \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS)>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS)>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS)>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       echo "\033[38;2;102;250;0mGenerierierung von "${1}$${bm}-${2}" ($${bm}) abgeschlossen\033[m"; done;
    $(call ${CLEANTARGET}) ##  Da 'lilly-autoclean' = true 
endef
define LILLYxClean = 
    @echo "[38;2;255;191;0m> Lösche temporäre Dateien...[m"
    @for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done;
endef

default: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Standart-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX),$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{FALSE}))

c_default: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere vollständige Standart-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)COMPLETE-,$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{TRUE}))

print: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Druck-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)PRINT-,$(PDFFILE),\\providecommand\\LILLYxMODE{print}\\providecommand\\LILLYxMODExEXTRA{FALSE}))

c_print: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Vollständige-Druck-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)PRINT-COMPLETE-,$(PDFFILE),\\providecommand\\LILLYxMODE{print}\\providecommand\\LILLYxMODExEXTRA{TRUE}))


all: $(INPUTDIR)$(TEXFILE)
	@$(MAKE) -s --no-print-directory default c_default print c_print  CLEANTARGET=void -j ${JOBCOUNT} --output-sync=line
	@$(MAKE) -s --no-print-directory clean

clean: 
	$(call LILLYxClean)

.PHONY: default c_default print c_print all clean
