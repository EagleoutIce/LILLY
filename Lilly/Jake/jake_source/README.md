# Jake installieren


## Linux :penguin:

### Was brauche ich?

Du brauchst Folgendes: 

- gcc
- make

Unter der Annahme, dass du eine auf Debian basierte Distribution besitzt, musst du eigentlich gar nichts machen!

### Wie gehe ich vor?
Öffne einfach dein Terminal und navigiere in ebendieses Verzeichnis. Du erkennst es daran, dass es die `jake.cpp` enthält!
Nun gib in deinem Terminal ein:

`make`

und folge den Anweisungen, falls es welche geben sollte. Kommt eine Meldung wie: `Die Datei .zshrc konnte nicht gefunden werden`
So ist dies nicht weiter schlimm, bisher ist das Makefile einfach nur sehr dumm und testet nicht, ob die Datei existiert :bowtie:

Das Makefile wird dich vermutlich dazu auffordern deine Konsole neu zu starten (es genügt auch eine neue Konsole zu öffnen). 
Nun steht dir das Programm `lilly_jake` zur Verfügung, welches du zum Installieren von LILLY verwenden kannst. Ab jetzt musst du 
dieses Verzeichnis hier eigentlich nicht mehr besuchen - außer es gibt eine neuere Version von Jake und Jake besitzt noch nicht den
`update` Befehl :P - Dann kannst du zum Aktualisieren einfach erneut make ausführen :)

## Windows

## MacOS

# Die Dokumentation

Jake ist im doxygen-Stil dokumentiert. Eine entsprechende HTML-Dokumentation kannst du mithilfe von Doxygen erzeugen. 
Beachte, dass hierzu auch der dot-compiler aus dem `graphviz` Paket benötigt wird!

Danach genügt in ebendiesem Verzeichnis, in dem sich auch das `Doxyfile` befindet, folgender Befehl:

`doxygen Doxyfile`

Anschließen kannst du die, sich im Unterordner `docs` befindliche, `index.html` mit einem Webbrowser deiner Wahl öffnen
und die Dokumentation genießen :).

**Viel Spaß**


