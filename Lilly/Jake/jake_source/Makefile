## Makefile for jake.cpp

EXTRA_FILES := jake_files/j_Helper.cpp jake_files/cmd_line/j_Functions.cpp jake_files/installer/j_ins_Linux.cpp jake_files/installer/j_ins_MacOS.cpp jake_files/core/j_Settings.cpp jake_files/cmd_line/j_Parser.cpp jake_files/core/j_Globals.cpp
LINKERS :=

WRITE_RC = @if grep -q "LILLY_CODE" "$(1)"; then \
        echo "DEBUG: Already prepped $(1)";\
    else \
        echo "export PATH=\044PATH:$(shell pwd)  \#LILLY_CODE"  >> $(1);\
    fi

default: no_link
		$(call WRITE_RC,"${HOME}/.zshrc")
		$(call WRITE_RC,"${HOME}/.bashrc")
		$(call WRITE_RC,"${HOME}/.bash_profile")
    @if [ "$(shell which lilly_jake)" = "" ]; then echo "\033[31mBitte starte nun dein Terminal neu um auf lilly_jake zugreifen zu können!\033[m"; fi;

no_link: jake.cpp
		@g++ -std=c++11 -I './' $(EXTRA_FILES) jake.cpp -o lilly_jake $(LINKERS)
		@chmod +x ./lilly_jake
		@echo Compiled lilly_jake
clean:
		@sed -i '/\#LILLY_CODE/d' "${HOME}/.zshrc"
		@sed -i '/\#LILLY_CODE/d' "${HOME}/.bashrc"
		@sed -i '/\#LILLY_CODE/d' "${HOME}/.bash_profile"

.PHONY: default no_link clean
