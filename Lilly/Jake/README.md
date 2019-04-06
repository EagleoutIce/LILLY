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

| Beachte: |
| --- |
| Bisher verändert Jake `$PATH` indem er sich sowohl in die `.bashrc` als auch in eine `.zshrc` einträgt. Dies kann unter umständen zu Problemen führen. In diesem Fall wird empfohlen einfach zu Weinen und anschließend die jeweiligen Dateien zu öffnen und die Zeile nach `#LILLY_CODE` zu entfernen. Es wird geraten den Kommentar unverändert zu lassen! Über ihn identifiziert das Makefile ob es etwas in die Dateien eintragen soll oder nicht.|

Soll keine Veränderung von `$PATH` durchgeführt werden so wird die Regel `no_link` empfohlen:

`make no_link`

(TODO:) Zum Entfernen eines existenten links genügt:

`make clean`

Anschließend kann man Jake (als `lilly_jake`) ganz normal aufrufen.

### Windows
### MacOS

## Wie benutzt man Jake?
Startet man Jake mit dem Argument `install` so versucht Jake LILLY zu installieren. Hierzu geht Jake davon aus, dass es sich im Ordner `LILLY` befindet, in dem sich alle notwendigen Dateien befinden. Zum Aufsetzen von LILLY genügt also ein:

`lilly_jake install`

Möchte man nun für eine Datei ein Makefile generieren (welches auch mit Version 1.0.7 verschiedene BoxModi Unterstützt) so genügt das Schreiben des Dateinamens:

`lilly_jake TollesDokument.tex`

Weitere Funktionen sind dem Hilfe-Parameter zu entnehmen (`lilly_jake help`) 
