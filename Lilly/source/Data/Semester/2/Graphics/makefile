# Alle Titelbilder
PAPERSTEX := $(shell ls titleimage*.tex)
PAPERSPDF := $(addsuffix .pdf,$(basename $(PAPERSTEX)))

JOBCOUNT := 3

CLEANTARGETS := log aux out ind bbl blg lof lot toc idx acn acr alg glg glo gls fls fdb_latexmk auxlock md5 SATZE ZSM UB TOP listing upa ilg TOPIC DEFS
CLEANTARGET  := LILLYxClean

void = return 0;

LILLYxClean = echo "\033[38;2;255;191;0m> Lösche temporäre Dateien...[m" && \
     for fd in $(CLEANTARGETS); do rm -f $(OUTPUTDIR)*.$$fd; done

#create all papers
all:
	@make --no-print-directory $(PAPERSPDF) CLEANTARGET=void -j $(JOBCOUNT) --output-sync=line
	@make --no-print-directory clean

%.pdf: %.tex
	pdflatex -jobname $(basename $@) \\input{$<} > ./LILLY_COMPILE.log 2>&1
	pdflatex -jobname $(basename $@) \\input{$<} >> ./LILLY_COMPILE.log 2>&1
	@$(call $(CLEANTARGET))

clean:
	@$(call $(CLEANTARGET))

cleanpdf:
	rm -f *.pdf
