# Jake 1.0.8 - An intelligent Servant for Lilly     (compiled on: May  7 2019 at: 11:46:39)

TEXFILE      := Mitschrieb-GDBS.tex
BASENAME     := $(basename $(TEXFILE))
PDFFILE      := $(addsuffix .pdf,$(basename $(TEXFILE)))
LATEXARGS    := -shell-escape -enable-write18 -interaction=batchmode
## Directories used for INPUT and OUTPUT Files 
OUTPUTDIR    := $(shell echo ./$(BASENAME)-OUT/| sed 's:/*$$::')/
INPUTDIR     := $(shell echo ./| sed 's:/*$$::')/
BOXMODES     := DEFAULT#                      ## Seperator: ' '
CLEANTARGET  := LILLYxClean
CLEANTARGETS := log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock SATZE ZSM UB TOP listing upa ilg TOPIC DEFS
SIGNATURECOL := Leaf
AUTHOR       := Florian der Echte von Sieben
AUTHORMAIL   := florian.sihler@web.de
NAMEPREFIX   := #                             ## Immer
SEMESTER     := 2#                            ## Übungsblatt
VORLESUNG    := GDBS#                         ## Übungsblatt
N            := 42#                           ## Anzahl
_C           := ,                             ## No Joke xD

## Makefile/General settings
_LILLYARGS   :=  \\providecommand{\\LILLYxDOCUMENTNAME{$(TEXFILE)}}\\providecommand{\\LILLYxOUTPUTDIR{$(OUTPUTDIR)}} $(DEBUG) \\providecommand{\\LILLYxPATH}{${INPUTDIR}}\\providecommand{\\AUTHOR}{${AUTHOR}}\\providecommand{\\AUTHORMAIL}{${AUTHORMAIL}}\\providecommand{\\LILLYxSemester}{${SEMESTER}}\\providecommand{\\LILLYxVorlesung}{${VORLESUNG}}\\providecommand{\\Hcolor}{${SIGNATURECOL}}\\providecommand{\\lillyPathLayout}{\\LILLYxgetDOCPATH/}\\providecommand{\\LILLYxEXTERNALIZE}{FALSE}

JOBCOUNT     := 2#                            ## should: <= cpu/thread count!


void = true


LILLYxCompile = \
    echo "Lilly PRE-Hook[hello-world] evaluiert zu: $(shell echo Hello World)" && \
    echo "Lilly PRE-Hook[testhook] evaluiert zu: $(shell (touch testfile) >$(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo  ["Ist gut gelaufen"] ||  echo  [failure])" && \
    mkdir -p "$(OUTPUTDIR)" && \
    touch $(OUTPUTDIR)LILLY_COMPILE.log && echo LILLY_LOGFILE stamp: $(shell date +'%d.%m.%Y %H:%M:%S') > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 &&\
    (for bm in $(BOXMODES); do \
    echo "Lilly IN0-Hook[in0-hook] evaluiert zu: $(shell (echo "Hallo Welt - will it break?") >$(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo  [""] ||  echo  [failure])" && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4}>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
    echo "Lilly IN1-Hook[in1-hook] evaluiert zu: $(shell (echo "Hallo Welt"&&echo "Wuff  Duffel") >$(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo  [success] ||  echo  ["It is dead"])" && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4}>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
    echo "Lilly IN2-Hook[in2-hook] evaluiert zu: $(shell (echo "Hallo Welt 2") >$(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && echo  [success] ||  echo  ["It's dead"])" && \
       pdflatex -jobname $(basename ${1}$${bm}-${2}) $(LATEXARGS) \\providecommand{\\LILLYxBOXxMODE}{$${bm}}\\providecommand{\\LILLYxPDFNAME}{${1}$${bm}-${2}}  ${3} $(_LILLYARGS) ${4}>> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1 && \
       echo "\033[38;2;102;250;0mGenerierierung von "${1}$${bm}-${2}" ($${bm}) abgeschlossen\033[m"; done &&\
    echo "Lilly POST-Hook[hello-world-out] evaluiert zu: $(shell echo Hello World - I am out)" && \
    $(call $(CLEANTARGET)) ) || (echo "\033[31m! Das Kompilieren mit LILLY ist fehlgeschlagen. Fehler finden sich in der entsprechendne Logdatei\033[m"; exit 1;)

LILLYxClean = echo "\033[38;2;255;191;0m> Lösche temporäre Dateien...[m" && \
     for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done


default: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Standart-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX),$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{FALSE},"\\input{$(INPUTDIR)$(TEXFILE)}")

print: $(INPUTDIR)$(TEXFILE)
	@echo "\033[38;2;255;191;0mGeneriere Druck-Version($(BOXMODES)) der Latex-Datei: "$(TEXFILE)"...\033[m"
	@$(call LILLYxCompile,$(OUTPUTDIR)$(NAMEPREFIX)PRINT-,$(PDFFILE),\\providecommand\\LILLYxMODE{print}\\providecommand\\LILLYxMODExEXTRA{FALSE},"\\input{$(INPUTDIR)$(TEXFILE)}")


all: $(INPUTDIR)$(TEXFILE)
	@$(MAKE) -s --no-print-directory default print  CLEANTARGET=void -j ${JOBCOUNT} --output-sync=line
	@$(MAKE) -s --no-print-directory clean

clean:
	@$(call LILLYxClean)

.PHONY: default print all clean
