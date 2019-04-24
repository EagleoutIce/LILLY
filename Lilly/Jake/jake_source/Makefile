## Makefile for jake.cpp

EXTRA_FILES := jake_files/j_Helper.cpp \
	       jake_files/cmd_line/j_Functions.cpp \
	       jake_files/installer/j_ins_Linux.cpp \
	       jake_files/installer/j_ins_MacOS.cpp \
	       jake_files/core/j_Settings.cpp \
	       jake_files/cmd_line/j_Parser.cpp \
	       jake_files/core/j_Globals.cpp \
	       jake_files/provider/j_Tokenizer.cpp \
	       jake_files/provider/j_Configurator.cpp \
	       jake_files/provider/j_Generator_Parser.cpp \
	       jake_files/provider/box_profiles/j_buildrules.cpp \
	       jake_files/provider/general_profiles/j_hooks.cpp

LINKERS :=
RC_FILES := "${HOME}/.zshrc" "${HOME}/.bashrc" "${HOME}/.bash_profile"

DEBUG := 0

WRITE_RC = if grep -q "LILLY_CODE" "$(1)"; then \
        echo "DEBUG: Already prepped $(1)";\
    else \
        echo "export PATH=\044PATH:$(shell pwd); autoload bashcompinit &>/dev/null; bashcompinit &>/dev/null; source $(shell pwd)/_jake_autocomplete \#LILLY_CODE"  >> $(1);\
    fi

default: no_link
		@for path in $(RC_FILES); do if [ -f $$path ]; then $(call WRITE_RC,$$path); fi; done
		@if [ "$(shell which lilly_jake)" = "" ]; then echo "\033[31mBitte starte nun dein Terminal neu um auf lilly_jake zugreifen zu können!\033[m"; fi;
		
debug: _dodebug1 default

debug2: _dodebug2 default


no_link: jake.cpp
		@g++ -std=c++14 -I './' $(EXTRA_FILES) -DJAKE_PREPROCESS_DEBUG=$(DEBUG) jake.cpp -o lilly_jake $(LINKERS)
		
		@chmod +x ./lilly_jake
		@echo Compiled lilly_jake
clean:
		@$(foreach path,$(RC_FILES),if [ -f $(path) ] ; then echo "Lösche Lilly in: \"$(path)\" (backup: \"$(path).bak\")" && sed -i'.bak' '/\#LILLY_CODE/d' $(path); fi;)

_dodebug1:
	$(eval DEBUG=1)

_dodebug2:
	$(eval DEBUG=2)
	
.PHONY: default no_link clean debug debug2 _dodebug1 _dodebug2
