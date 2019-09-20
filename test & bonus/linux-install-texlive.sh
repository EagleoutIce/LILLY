#!/bin/bash

# Florian Sihler

# This skript tries to install a legal version of texlive on apt-based systems

######## CONSTANTS
TEXLIVE_URL=https://ctan.kako-dev.de/systems/texlive/tlnet/
TEXLIVE_NAME=install-tl-unx.tar.gz

WGET_LOGFILE=./wget.log

################# Setup

# First of all, change to a temp-directory to do all of the work :D
TMP_TEX_DIR=$(mktemp -d -t tmp-tex-dir-XXXXXXXXXXXXX)
echo "Using the freshly created temp-directory: ${TMP_TEX_DIR}"
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
perl install-tl