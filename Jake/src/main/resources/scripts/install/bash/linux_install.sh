#!/usr/bin/env bash
## This script will take infinite arguments and tries to install
## all of them, if one of them fails, it will try to install the others
## but return with a error score, equal to the amount of failed packages

## identify used package manager
declare -A osInfo;
osInfo[/etc/redhat-release]="yum install"
osInfo[/etc/arch-release]="pacman -S"
osInfo[/etc/gentoo-release]="emerge -uD"
osInfo[/etc/SuSE-release]="zypper install"
osInfo[/etc/debian_version]="apt-get install"

ctr=0
echo "Installiere: \"$@\""
read -p "Drücke Enter um die Installation zu bestätigen"
for f in ${!osInfo[@]}
do
    if [[ -f $f ]];then
        for a in $@ ; do
            if ! sudo ${osInfo[$f]} ${a} >/dev/null 2>/dev/null; then
                ((ctr++))
            fi
        done
    fi
done

if [[ ${ctr} -gt 0 ]]; then
    echo Es gab ${ctr}-Fehler!
fi
exit ${ctr}