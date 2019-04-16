## Makefile for jake.cpp

EXTRA_FILES := jake_files/j_Helper.cpp jake_files/cmd_line/j_Functions.cpp jake_files/installer/j_ins_Linux.cpp jake_files/core/j_Settings.cpp jake_files/cmd_line/j_Parser.cpp jake_files/core/j_Globals.cpp
LINKERS := -lstdc++fs

define WRITE_RC =
	@if grep -q "#LILLY_CODE" "$(1)"; then\
		echo "DEBUG: Already prepped $(1)";\
	else\
		echo "export PATH=\044PATH:$(shell pwd)  #LILLY_CODE"  >> $(1);\
	fi
endef

default: no_link
	@#Write for zshell
	$(call WRITE_RC,"${HOME}/.zshrc")
	@#Write for bash
	$(call WRITE_RC,"${HOME}/.bashrc")
	
	@export PATH=${PATH}:$(shell pwd)

no_link: jake.cpp
	@g++ -std=c++11 -I './' $(EXTRA_FILES) jake.cpp -o lilly_jake $(LINKERS)
	@chmod +x ./lilly_jake
	@echo Compiled lilly_jake
clean:
	@sed -i '/#LILLY_CODE/d' "${HOME}/.zshrc"
	@sed -i '/#LILLY_CODE/d' "${HOME}/.bashrc"
	
.PHONY: default no_link clean
