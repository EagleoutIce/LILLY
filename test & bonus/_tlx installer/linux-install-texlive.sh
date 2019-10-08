#!/bin/bash

# Florian Sihler

# This skript tries to install a legal version of texlive on apt-based systems

######## CONSTANTS
TEXLIVE_URL=https://ctan.kako-dev.de/systems/texlive/tlnet/
TEXLIVE_NAME=install-tl-unx.tar.gz

WGET_LOGFILE=./wget.log

EXEC_PRE="sudo -sE"

REPO_URL="http://mirror.ctan.org/systems/texlive/tlnet"
REPO_OPT="-repository ${REPO_URL}"

################# Setup

# First of all, change to a temp-directory to do all of the work :D
TMP_TEX_DIR=$(mktemp -d -t tmp-tex-dir-XXXXXXXXXXXXX)
echo "Using the freshly created temp-directory: ${TMP_TEX_DIR}"


## Create the Profile:
${EXEC_PRE} $(which cp) ./tl.profile ${TMP_TEX_DIR}


# Switch dir
cd ${TMP_TEX_DIR}

###### Get tlmgr
DOWNLOAD_FEEDBACK=$(wget ${TEXLIVE_URL}${TEXLIVE_NAME} &> ${WGET_LOGFILE})

if [ $? -ne 0 ]; then
    echo "Downloading failed, please see the logfile: ${WGET_LOGFILE}"
    exit
fi



## unpacking the tar.gz
UNPACK_FEEDBACK=$(tar -xzf ${TEXLIVE_NAME})

if [ $? -ne 0 ]; then
    echo "Unpacking failed!"
    exit
fi

UNPACKED_TL_NAME=$(ls -d */ | tail -1)

echo "Unpacking successfull, working with folder: ${UNPACKED_TL_NAME}"
cd ${UNPACKED_TL_NAME}

echo "Starting the installer, using the supplied perl-script"
${EXEC_PRE} $(which perl) install-tl --profile ../tl.profile ${REPO_OPT}


## After routine

TLMGR_LOC=$(which tlmgr)

TLMGR="${EXEC_PRE} ${TLMGR_LOC} ${REPO_OPT}"


echo "Es werden nun einige Befehle mit sudo ausgeführt um die Installation abzuschließen"

# Set target repo
${EXEC_PRE} ${TLMGR_LOC} option repository ${REPO_URL}

# Setup Texdoc
${TLMGR} install collection-binextra
${TLMGR} install texdoc

## Ask for Dokumentation
read -p "Soll die Dokumentation installiert werden, dies kann einige Zeit dauern (~30m)? [yes/No] " choice
case "$choice" in
  [yY] |[yY][eE][sS] )
    ${TLMGR} option docfiles 1
    ${TLMGR} install --reinstall $(${TLMGR_LOC} list --only-installed | $(which sed) -E 's/i (.*):.*$/\1/')
  ;;
  * ) echo "Die Dokumentationen werden nicht geladen!";;
esac
