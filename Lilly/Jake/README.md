# Jake
-----
**Author:** Florian Sihler
**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019
**OS:** Linux
----
## Was ist Jake?
Jake ist ein in C++ geschriebenes Programm, welches die Arbeit mit dem LILLY-Framework vereinfacht. Es ist bisher für folgende Betriebssysteme Definiert: 
 - Linux

Diese Version wurde entwickelt für LILLY (VER: 1.0.7) und zusammen mit dem nötigen makefile ausgeliefert. 
***JAKE Ist nicht nötig um mit dem LILLY-Framework zu arbeiten, es handelt sich lediglich um ein Hilfsprogramm.***

## Wie zieht man Jake das Anoräckchen an?
### Linux
Jake wird als `.cpp`--Datei geliefert. Um die Datei lauffertig zu machen genügt folgender Befehl:
`make`
Soll kein Symlink zu `/usr/local/bin` erstellt werden so wird die Regel `no_link` empfohlen:
`make no_link`
Zum entfernen eines existenten links genügt:
`make clean`
Anschließend kann man Jake ganz normal aufrufen 

### Windows
### MacOS

## Wie benutzt man Jake?
Startet man Jake mit dem Argument `install` so versucht Jake LILLY zu installieren. Hierzu geht Jake davon aus, dass es sich im Ordner `LILLY` befindet, in dem sich alle notwendigen Dateien befinden. Zum Aufsetzen von LILLY genügt also ein:
`jake install`
Möchte man nun für eine Datei ein Makefile generieren (welches auch mit Version 1.0.7 verschiedene BoxModi Unterstützt) so genügt das Schreiben des Dateinamens:
`jake TollesDokument.tex`
Weitere Funktionen sind dem Hilfe-Parameter zu entnehmen (`jake --help`) 
