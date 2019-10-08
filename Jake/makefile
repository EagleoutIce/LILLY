all: compile

default:
	mvn install && java -jar target/development-jake.jar

gui: clean
	mvn install && java -jar target/development-jake.jar GUI

update-stable: compile
	cp target/development-jake.jar jake.jar

compile:
	mvn clean install

run:
	java -jar target/development-jake.jar

docs: target/development-jake.jar
	cd doc_source && $(MAKE)

clean:
	mvn clean
	@rm -f ~/.local/bin/jake
	@rm -f ~/.local/share/applications/jake.desktop
