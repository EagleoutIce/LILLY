#!/bin/bash

# VERSION 1.0.0

#Diese Datei generiert ein ${MAKEFILE}, welches die gewünschte Latex-Datei (auf Basis von LILLY) in das gewünschte Format verwandelt
##TODO: ADD COMMAND LINE PARAMTERS
##TODO: Angabe der Boxen im Debug
##TODO: AUTOCOMPLETION BY https://askubuntu.com/questions/68175/how-to-create-script-with-auto-complete#68203
##      => INSTALLFILE
ESC="\033["
COL_RESET="${ESC}m"
COL_HIGHLIGHT="${ESC}38;2;160;32;240m"
COL_SUCC="${ESC}38;2;102;250;0m"
COL_WARNING="${ESC}38;2;255;191;0m"
COL_ERROR="${ESC}38;2;255;32;82m"

DATE=$(date +'%d.%m.%Y %H:%M:%S')


echo -e "${COL_HIGHLIGHT}[Compile] Generiere ${MAKEFILE}...${COL_RESET}"

##Generiere angepasstes ${MAKEFILE}

DIRECTORY="./"
OUTPUTDIR="./"
INPUTDIR="./"
FILE="dummy"
COMPILE_MODES="default"
DUMMY="Hallo Welt"
MAKEFILE="Makefile"
CLEAN="TRU"
COMPLETE="FALS"
DEBUG=""
BOXDEF="ALTERNATE"
TS=\\\\


for i in "$@" ## Für alle Argumente
do case $i in ## Switch case
    -dir=*|--directory=*)
        DIRECTORY="${i#*=}"
    ;;
    -odir=*|--output_directory=*)
        OUTPUTDIR="${i#*=}"
    ;;
    -idir=*|--input_directory=*)
        INPUTDIR="${i#*=}"
    ;;
    *.tex)
        FILE="${i}"
    ;;
    --debug)
        DEBUG="${TS}providecommand${TS}LILLYxDEBUG{TRUE}"
    ;;
    --boxdefault=*)
        BOXDEF="${i#=*}"
    ;;
    print|blockpoi|dummy)
        COMPILE_MODES="${COMPILE_MODES} ${i}"
    ;;
    -mode=*)
        COMPILE_MODES="${i#*=}"
    ;;
    --dummy=*)
        DUMMY="${i#*=}"
    ;;
    --complete)
        COMPLETE="TRU"
    ;;
    --no_clean)
        CLEAN="False"
    ;;
    --name=*|-name=*)
        MAKEFILE="${i#*=}"
    ;;
    *)
        echo -e "${COL_WARNING}Die Option: \"${i}\" ist nicht bekannt!${COL_RESET}"
    ;;
esac
done

TARGET=${DIRECTORY}${MAKEFILE}


if [ "$FILE" == "dummy" ] 
    then
        echo -e "${COL_ERROR}Bitte gibt die Latex-Quelldatei an!${COL_RESET}"
        exit 1
fi
COMPILE_MODE=($COMPILE_MODES)

##Kind-of Debug

echo -e "### ${DATE} - Generiertes ${MAKEFILE} (makefile) für Lilly\n" > ${TARGET}   ## Override old ${MAKEFILE} if it exists

#setze ${MAKEFILE}-Variablen unabhängig der Konfigurationen ##TODO: MAKE EDITABLE

echo "TEXFILE   = ${FILE}## Alternativ: '\$(shell ls *.tex)'" >> ${TARGET} 
echo "PDFFILE   := \$(addsuffix .pdf,\$(basename \$(TEXFILE)))" >> ${TARGET} 
echo "LATEXARGS := -shell-escape -enable-write18 -interaction=batchmode" >> ${TARGET} 
echo "OUTPUTDRIVER := """ >> ${TARGET} 
echo "OUTPUTDIR = ${OUTPUTDIR}" >> ${TARGET}
echo "DEBUG := ${DEBUG}" >> ${TARGET} 
echo "NAMEPREFIX = " >> ${TARGET} 
echo "BOXMODE = ${BOXDEF}"  >> ${TARGET}  #####  INSERT ALTERNATE IF CHECK TO APPLY
echo "PRINTNAME = PRINT-" >> ${TARGET} 
echo "COMPLETENAME = COMPLETE-" >> ${TARGET} 
echo "INPUTDIR = ${INPUTDIR}" >> ${TARGET} 
echo "CLEANTARGET = LILLY_clean" >> ${TARGET} 
echo "JOBCOUNT = 2" >> ${TARGET} 

## Generiere Make-Typen
echo "define LILLY_compile = " >> ${TARGET} 
echo "    @pdflatex -jobname \$(basename \${1}) \$(LATEXARGS) ${TS}def${TS}LILLYxDummy{${DUMMY}}${TS}\providecommand{${TS}\LILLYxBOXxMODE}{\$(BOXMODE)}${TS}\providecommand{${TS}\LILLYxDOCUMENTNAME{\$(TEXFILE)}} \$(DEBUG) \${2}${TS}\providecommand{${TS}\LILLYxPDFNAME}{\${1}}${TS}\providecommand{${TS}\LILLYxPATH}{\${INPUTDIR}} ${TS}input{\$(INPUTDIR)\$(TEXFILE)} > \$(OUTPUTDIR)LILLY_COMPILE.log 2>&1" >> ${TARGET} 
## echo "    bibtex ${FILE%".tex"}" >> ${TARGET} 
echo "    @pdflatex -jobname \$(basename \${1}) \$(LATEXARGS) ${TS}def${TS}LILLYxDummy{${DUMMY}}${TS}\providecommand{${TS}\LILLYxBOXxMODE}{\$(BOXMODE)}${TS}\providecommand{${TS}\LILLYxDOCUMENTNAME{\$(TEXFILE)}} \$(DEBUG) \${2} ${TS}\providecommand{${TS}\LILLYxPDFNAME}{\${1}}${TS}\providecommand{${TS}\LILLYxPATH}{\${INPUTDIR}}${TS}input{\$(INPUTDIR)\$(TEXFILE)} >> \$(OUTPUTDIR)LILLY_COMPILE.log 2>&1" >> ${TARGET} #Kein Fehler brauchen wir
echo "    @pdflatex -jobname \$(basename \${1}) \$(OUTPUTDRIVER) \$(LATEXARGS) ${TS}def${TS}LILLYxDummy{${DUMMY}}${TS}\providecommand{${TS}\LILLYxBOXxMODE}{\$(BOXMODE)}${TS}\providecommand{${TS}\LILLYxDOCUMENTNAME{\$(TEXFILE)}} \$(DEBUG) \${2}  ${TS}\providecommand{${TS}\LILLYxPDFNAME}{\${1}}${TS}\providecommand{${TS}\LILLYxPATH}{\${INPUTDIR}}${TS}input{\$(INPUTDIR)\$(TEXFILE)}  >> \$(OUTPUTDIR)LILLY_COMPILE.log 2>&1" >> ${TARGET} 
#CLEAN
if [ "$CLEAN" == "TRU" ]  
    then ## Löschen entstehender Daten ist erwünscht - Standart
    echo -e "    @## Lösche unbenötigte Dateien: (Kann durch '-no_clean' verhindert werden!)" >> ${TARGET} 
    echo -e "    \$(call \${CLEANTARGET})" >> ${TARGET} 
fi
echo -e "    @echo \"${COL_SUCC}Generierierung von \\\"\${1}\\\" abgeschlossen${COL_RESET}\"" >> ${TARGET} 
echo -e "endef\n\n" >> ${TARGET} 

# $1: rule name
# $2: version name
# $3: complete - options
# $4: prefix
create_buildrule() {
    echo "$1: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
    echo -e "\t@echo \"${COL_WARNING}Generiere $2-Version(\$(BOXMODE)) der Latex-Datei: \\\"\$(TEXFILE)\\\"...${COL_RESET}\"" >> ${TARGET} 
    echo -e "\t\$(call LILLY_compile,\$(OUTPUTDIR)\$(NAMEPREFIX)${4}\$(PDFFILE),${TS}\\providecommand${TS}\\LILLYxMODE{$1})\n" >> ${TARGET} 
    if [ "$3" == "TRU" ]
        then
        echo "complete_$1: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
        echo -e "\t@echo \"${COL_WARNING}Generiere $2-Version(\$(BOXMODE), complete:Bilder, Übungsblätter,...) der Latex-Datei: \\\"\$(TEXFILE)\\\"...${COL_RESET}\"" >> ${TARGET} 
        echo -e "\t\$(call LILLY_compile,\$(OUTPUTDIR)\$(NAMEPREFIX)\$(COMPLETENAME)${4}\$(PDFFILE),${TS}\\providecommand${TS}\\LILLYxMODE{$1}${TS}\\providecommand${TS}\\LILLYxMODExEXTRA{TRUE})\n" >> ${TARGET}
        COMPILE_MODES="${COMPILE_MODES} complete_$1"
    fi
}


for i in "${COMPILE_MODE[@]}" ## Für alle Argumente
do case $i in ## Switch case
    default)
        create_buildrule "default" "Standart" "${COMPLETE}" ""
        #echo "default: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
        #echo -e "\t@echo \"${COL_WARNING}Generiere Standart-Version(\$(BOXMODE)) der Latex-Datei: \\\"\$(TEXFILE)\\\"...${COL_RESET}\"" >> ${TARGET} 
        #echo -e "\t\$(call LILLY_compile,\$(OUTPUTDIR)\$(NAMEPREFIX)\$(PDFFILE),${TS}\\providecommand${TS}\\LILLYxMODE{default})\n" >> ${TARGET} 
    ;;
    #dummy)
    #   create_buildrule "dummy" "Dummy" "${COMPLETE}"
        #echo "dummy: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
        #echo -e "\t@echo \"${COL_WARNING}Generiere Dummy-Version(\$(BOXMODE)) der Latex-Datei: \\\"\$(TEXFILE)\\\"...${COL_RESET}\"" >> ${TARGET} 
        #echo -e "\t\$(call LILLY_compile,\$(OUTPUTDIR)\$(NAMEPREFIX)\${DUMMYNAME}\$(PDFFILE),${TS}\\providecommand${TS}\\LILLYxMODE{dummy})\n" >> ${TARGET} 
    #;;
    print)
        create_buildrule "print" "Druck" "${COMPLETE}" "\$(PRINTNAME)"
        #echo "print: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
        #echo -e "\t@echo \"${COL_WARNING}Generiere Print-Version(\$(BOXMODE)) der Latex-Datei: \\\"\$(TEXFILE)\\\"...${COL_RESET}\"" >> ${TARGET} 
        #echo -e "\t\$(call LILLY_compile,\$(OUTPUTDIR)\$(NAMEPREFIX)\${PRINTNAME}\$(PDFFILE),${TS}\\providecommand${TS}\\LILLYxMODE{print})\n" >> ${TARGET} 
    ;;
    #print)  
        #echo "print: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
        #echo -e "\t@echo \"${COL_WARNING}Generiere Print-Version(\$(BOXMODE)) der Latex-Datei: \\\"\$(TEXFILE)\\\"...${COL_RESET}\"" >> ${TARGET} 
        #echo -e "\t\$(call LILLY_compile,\$(OUTPUTDIR)\$(NAMEPREFIX)\${PRINTNAME}\$(PDFFILE),${TS}\\providecommand${TS}\\LILLYxMODE{print})\n" >> ${TARGET} 
    #;;
esac
done
COMPILE_MODE=($COMPILE_MODES) # update for output

echo "all: \$(INPUTDIR)\$(TEXFILE)" >> ${TARGET} 
    echo -e "\t@\$(MAKE) -s --no-print-directory $COMPILE_MODES CLEANTARGET=void -j \${JOBCOUNT} --output-sync=line" >> ${TARGET} 
    echo -e "\t@\$(MAKE) -s --no-print-directory clean" >> ${TARGET} 


echo -e ".PHONY: clean\n" >> ${TARGET}
echo "clean: " >> ${TARGET} 
echo -e "\t\$(call LILLY_clean)\n\n" >> ${TARGET} 

echo "define LILLY_clean = ">> ${TARGET}
echo -e "    @echo \"${COL_WARNING}> Lösche temporäre Dateien...${COL_RESET}\"" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.log && rm -f \$(OUTPUTDIR)*.aux && rm -f \$(OUTPUTDIR)*.out" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.ind && rm -f \$(OUTPUTDIR)*.bbl && rm -f \$(OUTPUTDIR)*.blg" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.lof && rm -f \$(OUTPUTDIR)*.lot && rm -f \$(OUTPUTDIR)*.toc" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.idx && rm -f \$(OUTPUTDIR)*.acn && rm -f \$(OUTPUTDIR)*.acr" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.alg && rm -f \$(OUTPUTDIR)*.glg && rm -f \$(OUTPUTDIR)*.glo" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.gls && rm -f \$(OUTPUTDIR)*.fls && rm -f \$(OUTPUTDIR)*.fdb_latexmk" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.auxlock && rm -f \$(OUTPUTDIR)*.md5 && rm -f \$(OUTPUTDIR)*.DEFS" >> ${TARGET}
echo "    @rm -f \$(OUTPUTDIR)*.SATZE && rm -f \$(OUTPUTDIR)*.ZSM && rm -f \$(OUTPUTDIR)*.UB" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.TOP && rm -f \$(OUTPUTDIR)*.listing && rm -f \$(OUTPUTDIR)*.upa" >> ${TARGET} 
echo "    @rm -f \$(OUTPUTDIR)*.ilg && rm -f \$(OUTPUTDIR)*.TOPIC " >> ${TARGET} 


echo -e "endef \n\n" >> ${TARGET}

echo -e "${COL_HIGHLIGHT}Makefile generiert für Datei: \"${DIRECTORY}${FILE}\" -- unterstützte Modis: \"${COMPILE_MODE[*]}\" (${#COMPILE_MODE[*]})${COL_RESET}"

