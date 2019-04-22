# Jake 1.0.7 - A Servant for Lilly     (compiled on: Apr 22 2019 at: 15:11:57)

TEXFILE      := Lilly-Dokumentation.doc.tex
BASENAME     := $(basename $(TEXFILE))
PDFFILE      := $(addsuffix .pdf,$(basename $(TEXFILE)))
LATEXARGS    := -shell-escape -enable-write18 -interaction=batchmode
## Directories used for INPUT and OUTPUT Files 
OUTPUTDIR    := $(shell echo ./| sed 's:/*$$::')/
INPUTDIR     := $(shell echo ./| sed 's:/*$$::')/
BOXMODES     := LIMERENCE#                    ## Seperator: ' '
CLEANTARGET  := LILLYxClean
CLEANTARGETS := log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock md5 SATZE ZSM UB TOP listing upa ilg TOPIC DEFS
NAMEPREFIX   :=                               ## Immer
SEMESTER     := 1                             ## Übungsblatt
VORLESUNG    := LAII                          ## Übungsblatt
N            := 42                            ## Anzahl
_C           := ,                             ## No Joke xD

## Makefile/General settings
_LILLYARGS   :=  \\providecommand{\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) \\providecommand{\\LILLYxPATH}{${INPUTDIR}}

JOBCOUNT     := 2                             ## should: <= cpu/thread count!


void = return 0;  


LILLYxCompile = \
    mkdir -p "$(OUTPUTDIR)" && \
    touch $(OUTPUTDIR)LILLY_COMPILE.log && echo LILLY_LOGFILE stamp: $(shell date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 &&\
    (for bm in $(BOXMODES); do \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4}>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4}>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4}>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       echo "\033[38;2;102;250;0mGenerierierung von "${1}$${bm}-${2}" ($${bm}) abgeschlossen\033[m"; done &&\
    $(call $(CLEANTARGET)) ) || (echo "\033[31m! Das Kompilieren mit LILLY ist fehlgeschlagen. Fehler finden sich in der entsprechendne Logdatei\033[m"; return 1;)

LILLYxClean = echo "\033[38;2;255;191;0m> Lösche temporäre Dateien...[m" && \
     for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done


default: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Standart-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX),$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{FALSE},"\\input{$(INPUTDIR)$(TEXFILE)}")

c_default: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Standart-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)COMPLETE-,$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{TRUE},"\\input{$(INPUTDIR)$(TEXFILE)}")


all: $(INPUTDIR)$(TEXFILE)
	@$(MAKE) -s --no-print-directory default c_default  CLEANTARGET=void -j ${JOBCOUNT} --output-sync=line
	@$(MAKE) -s --no-print-directory clean

clean:
	@$(call LILLYxClean)

.PHONY: default c_default all clean
