## Makefile for jake.cpp

define WRITE_RC =
	@if grep -q "#LILLY_CODE" "$(1)"; then\
		echo "Already prepped $(1)";\
	else\
		echo "#LILLY_CODE\nexport PATH=\044PATH:$(shell pwd)"  >> $(1);\
	fi
endef

default: no_link
	@#Write for zshell
	$(call WRITE_RC,"${HOME}/.zshrc")
	@#Write for bash
	$(call WRITE_RC,"${HOME}/.bashrc")
	
	@export PATH=${PATH}:$(shell pwd)

no_link: jake.cpp
	g++ -std=c++11 -I './' jake.cpp -o lilly_jake
	@chmod +x ./lilly_jake
