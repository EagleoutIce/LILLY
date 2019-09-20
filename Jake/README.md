# Jake installieren


## Linux :penguin:

### Was brauche ich?

Du brauchst folgendes: 

- maven

## Windows

## MacOS

# Die Dokumentation

Jake ist im doxygen-Stil Dokumentiert. Eine entsprechende HTML-Dokumentation kannst du mithilfe von Doxygen erzeugen. 
Beachte, dass hierzu auch der dot-compiler aus dem `graphviz` Paket benötigt wird!
Auf Linux erhält man dies zum Beispiel durch:

`sudo apt-get install doxygen grapviz`


Danach genügt in ebendiesem Verzeichnis indem sich auch das `Doxyfile` befindet (`doc_source`) folgender Befehl:

`doxygen Doxyfile`

Anschließen kannst du die (sich im Unterordner `docs` befindliche) `index.html` mit einem Webbrowser deiner Wahl öffnen
und die Dokumentation genießen :).

**Viel Spaß**


