# Jake
-----
**Author:** Florian Sihler

**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019

**OS:** Linux & MacOS

----
## Was ist Jake?
Jake ist ein in C++ geschriebenes Programm, welches die Arbeit mit dem LILLY-Framework vereinfacht. Es ist bisher für folgende Betriebssysteme Definiert: 
 - Linux

Diese Version wurde entwickelt für LILLY (VER: 1.0.7) und zusammen mit dem nötigen makefile ausgeliefert. 

***JAKE Ist nicht nötig um mit dem LILLY-Framework zu arbeiten, es handelt sich lediglich um ein Hilfsprogramm.***

## Wie zieht man Jake das Anoräckchen an?
### Linux
Jake wird als `.cpp`--Datei geliefert. Um die Datei lauffertig zu machen genügt (im Verzeichnis `jake_files`) folgender Befehl:

`make`

| Beachte: |
| --- |
| Bisher verändert Jake `$PATH` indem er sich sowohl in die `.bashrc` als auch in eine `.zshrc` einträgt. Dies kann unter umständen zu Problemen führen. In diesem Fall wird empfohlen einfach zu Weinen und anschließend die jeweiligen Dateien zu öffnen und die Zeile nach `#LILLY_CODE` zu entfernen. Es wird geraten den Kommentar unverändert zu lassen! Über ihn identifiziert das Makefile ob es etwas in die Dateien eintragen soll oder nicht.|

Soll keine Veränderung von `$PATH` durchgeführt werden so wird die Regel `no_link` empfohlen:

`make no_link`

Zum Entfernen eines existenten links genügt:

`make clean`

Anschließend kann man Jake (als `lilly_jake`) ganz normal aufrufen.

### Windows
### MacOS

## Wie benutzt man Jake?

### Installation von Lilly

Startet man Jake mit dem Argument `install` so versucht Jake LILLY zu installieren. Hierzu geht Jake davon aus, dass es sich im Ordner `LILLY` befindet, in dem sich alle notwendigen Dateien befinden. Zum Aufsetzen von LILLY genügt also ein:

`lilly_jake install`

Ohne konfiguration sucht make hierbei automatisch den Ordner in dem die Lilly.cls vorliegt. Sollte hierbei nichts gefunden werden, oder die datei die gefunden wird fehlerhaft sein, so bricht Lilly die Installation ab, mit der Bitte den expliziten Pfad zur Lilly-Installation anzugeben. Diese Angabe kann zum Beispiel wie folgt aussehen:

`lilly_jake -lilly-path="/eagle_extra/Studium/LILLY/Lilly" install`


### Kompilieren eines Dokuments

Jake unterstützt bisher nur das explizite Generieren einer Datei durch das generieren eines Makefiles, weitere Funktionen sind in Arbeit :D

#### Makefile

Möchte man nun für eine Datei ein Makefile generieren (welches auch mit Version 1.0.7 verschiedene BoxModi Unterstützt) so genügt das Schreiben des Dateinamens:

`lilly_jake TollesDokument.tex`

Anschließend reicht `make` um die gewünschte Konfiguration durchzuführen.

Weitere Funktionen und mappings sind dem Hilfe-Parameter zu entnehmen (`lilly_jake help`) 
