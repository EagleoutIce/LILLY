all: clean
	mvn install && java -cp target/lilly-jake-framework-1.0.10-DEVELOPMENT-shaded.jar de.eagle.lillyjakeframework.Jake 

gui: clean
	mvn install && java -jar target/lilly-jake-framework-1.0.10-DEVELOPMENT-shaded.jar GUI

run: 
	java -cp target/lilly-jake-framework-1.0.10-DEVELOPMENT-shaded.jar de.eagle.lillyjakeframework.Jake

clean:
	mvn clean
	@rm -f ~/.local/bin/jake
	@rm -f ~/.local/share/applications/jake.desktop
