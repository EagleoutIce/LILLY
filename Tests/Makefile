### 09.04.2019 22:36:01 - Generiertes Makefile (makefile) für Lilly

TEXFILE   = Test.tex## Alternativ: '$(shell ls *.tex)'
PDFFILE   := $(addsuffix .pdf,$(basename $(TEXFILE)))
LATEXARGS := -shell-escape -enable-write18 -interaction=batchmode
OUTPUTDRIVER := 
OUTPUTDIR = Test/
DEBUG := 
NAMEPREFIX = 
BOXMODE = ALTERNATE
PRINTNAME = PRINT-
COMPLETENAME = COMPLETE-
INPUTDIR = ./
CLEANTARGET = LILLY_clean
JOBCOUNT = 2
define LILLY_compile = 
    @pdflatex -jobname $(basename ${1}) $(LATEXARGS) \\def\\LILLYxDummy{Hallo Welt}\\\providecommand{\\\LILLYxBOXxMODE}{$(BOXMODE)}\\\providecommand{\\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) ${2}\\\providecommand{\\\LILLYxPDFNAME}{${1}}\\\providecommand{\\\LILLYxPATH}{${INPUTDIR}} \\input{$(INPUTDIR)$(TEXFILE)} > $(OUTPUTDIR)LILLY_COMPILE.log 2>&1
    @pdflatex -jobname $(basename ${1}) $(LATEXARGS) \\def\\LILLYxDummy{Hallo Welt}\\\providecommand{\\\LILLYxBOXxMODE}{$(BOXMODE)}\\\providecommand{\\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) ${2} \\\providecommand{\\\LILLYxPDFNAME}{${1}}\\\providecommand{\\\LILLYxPATH}{${INPUTDIR}}\\input{$(INPUTDIR)$(TEXFILE)} >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1
    @pdflatex -jobname $(basename ${1}) $(OUTPUTDRIVER) $(LATEXARGS) \\def\\LILLYxDummy{Hallo Welt}\\\providecommand{\\\LILLYxBOXxMODE}{$(BOXMODE)}\\\providecommand{\\\LILLYxDOCUMENTNAME{$(TEXFILE)}} $(DEBUG) ${2}  \\\providecommand{\\\LILLYxPDFNAME}{${1}}\\\providecommand{\\\LILLYxPATH}{${INPUTDIR}}\\input{$(INPUTDIR)$(TEXFILE)}  >> $(OUTPUTDIR)LILLY_COMPILE.log 2>&1
    @## Lösche unbenötigte Dateien: (Kann durch '-no_clean' verhindert werden!)
    $(call ${CLEANTARGET})
    @echo "[38;2;102;250;0mGenerierierung von \"${1}\" abgeschlossen[m"
endef


default: $(INPUTDIR)$(TEXFILE)
	@echo "[38;2;255;191;0mGeneriere Standart-Version($(BOXMODE)) der Latex-Datei: \"$(TEXFILE)\"...[m"
	$(call LILLY_compile,$(OUTPUTDIR)$(NAMEPREFIX)$(PDFFILE),\\providecommand\\LILLYxMODE{default})

complete_default: $(INPUTDIR)$(TEXFILE)
	@echo "[38;2;255;191;0mGeneriere Standart-Version($(BOXMODE), complete:Bilder, Übungsblätter,...) der Latex-Datei: \"$(TEXFILE)\"...[m"
	$(call LILLY_compile,$(OUTPUTDIR)$(NAMEPREFIX)$(COMPLETENAME)$(PDFFILE),\\providecommand\\LILLYxMODE{default}\\providecommand\\LILLYxMODExEXTRA{TRUE})

print: $(INPUTDIR)$(TEXFILE)
	@echo "[38;2;255;191;0mGeneriere Druck-Version($(BOXMODE)) der Latex-Datei: \"$(TEXFILE)\"...[m"
	$(call LILLY_compile,$(OUTPUTDIR)$(NAMEPREFIX)$(PRINTNAME)$(PDFFILE),\\providecommand\\LILLYxMODE{print})

complete_print: $(INPUTDIR)$(TEXFILE)
	@echo "[38;2;255;191;0mGeneriere Druck-Version($(BOXMODE), complete:Bilder, Übungsblätter,...) der Latex-Datei: \"$(TEXFILE)\"...[m"
	$(call LILLY_compile,$(OUTPUTDIR)$(NAMEPREFIX)$(COMPLETENAME)$(PRINTNAME)$(PDFFILE),\\providecommand\\LILLYxMODE{print}\\providecommand\\LILLYxMODExEXTRA{TRUE})

all: $(INPUTDIR)$(TEXFILE)
	@$(MAKE) -s --no-print-directory default print complete_default complete_print CLEANTARGET=void -j ${JOBCOUNT} --output-sync=line
	@$(MAKE) -s --no-print-directory clean
.PHONY: clean

clean: 
	$(call LILLY_clean)


define LILLY_clean = 
    @echo "[38;2;255;191;0m> Lösche temporäre Dateien...[m"
    @rm -f $(OUTPUTDIR)*.log && rm -f $(OUTPUTDIR)*.aux && rm -f $(OUTPUTDIR)*.out
    @rm -f $(OUTPUTDIR)*.ind && rm -f $(OUTPUTDIR)*.bbl && rm -f $(OUTPUTDIR)*.blg
    @rm -f $(OUTPUTDIR)*.lof && rm -f $(OUTPUTDIR)*.lot && rm -f $(OUTPUTDIR)*.toc
    @rm -f $(OUTPUTDIR)*.idx && rm -f $(OUTPUTDIR)*.acn && rm -f $(OUTPUTDIR)*.acr
    @rm -f $(OUTPUTDIR)*.alg && rm -f $(OUTPUTDIR)*.glg && rm -f $(OUTPUTDIR)*.glo
    @rm -f $(OUTPUTDIR)*.gls && rm -f $(OUTPUTDIR)*.fls && rm -f $(OUTPUTDIR)*.fdb_latexmk
    @rm -f $(OUTPUTDIR)*.auxlock && rm -f $(OUTPUTDIR)*.md5 && rm -f $(OUTPUTDIR)*.DEFS
    @rm -f $(OUTPUTDIR)*.SATZE && rm -f $(OUTPUTDIR)*.ZSM && rm -f $(OUTPUTDIR)*.UB
    @rm -f $(OUTPUTDIR)*.TOP && rm -f $(OUTPUTDIR)*.listing && rm -f $(OUTPUTDIR)*.upa
    @rm -f $(OUTPUTDIR)*.ilg && rm -f $(OUTPUTDIR)*.TOPIC 
endef 


