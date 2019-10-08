![Titelschmild Witelbild](Header.png)

## Motivation

Dieses Framework wird im Rahmen des Informatikstudiums an der Uni Ulm betrieben, um die Realisierung von Skripten und Mitschriften in Latex zu vereinfachen.

## Installation

*(Information: Bisher ist diese Installation nur für Linuxioide-Betriebsysteme getestet.)*

Um die neuste Version inklusive Jake zu erhalten, [Klick Mich](https://github.com/EagleoutIce/LILLY/releases/latest/download/jake.jar)!
Nun genügt es sich einen heimelichen Ort auszusuchen (es werden keine weiteren Dateien an Ort und Stelle entpackt, allerdings werden alle Verweise auf den Installationsort gesetzt) und dort mittels `java -jar jake.jar` die Installation zu beginnen. Wahlweise existiert auch eine GUI-basierte Installation `java -jar jake.jar GUI`. Anschließend kann mittels `jake install` Lilly installiert werden (unter Umständen kann es nötig sein die Konfigurationsdatei der Konsole neu enzulesen, damit `jake` gefunden wird!).

Anschließende Updates können mittels `jake update` durchgeführt werden. Eine Reinstallation mittels `jake REI` und eine Deinstallation mittels `jake DEI`. Die grafische Oberfläche kann mittels `jake GUI` gestartet werden, alle weiteren Befehle lassen sich durch das bloße Eintippen von `jake` anzeigen. Weitere Informationen zur Einrichtung von Jake sind der Dokumentation (`jake docs` oder [Dokumentation](https://github.com/EagleoutIce/LILLY/releases/latest/download/Lilly-Dokumentation.doc.pdf)) zu entnehmen. Mit Version 2.1.0 von Jake werden diese nicht mehr mit dem Repository geliefert!

## Details zur Dokumentation

Um die `jake.jar` in ihrer Größe möglichst Kompakt zu halten, liefert sie auch nur eine reduzierte Variante der Dokumentation, bei der primär grafische Beispiele entfernt wurden. Die 'ausführliche' Dokumentation befindet sich hier: https://github.com/EagleoutIce/LILLY/releases/latest/download/Lilly-Dokumentation.doc.pdf oder kann mittels `jake` im Unterordner Dokumentation durch `jake doc.conf` selbst generiert werden (eine vorhandene, aktuelle Installation von Lilly vorausgesetzt).

## Wichtig

Wenn du mitarbeiten möchtest ist das super, melde dich einfach bei mir! Allgemein gilt zu beachten:
Dieses Repository akzeptiert nur signed Commits: https://help.github.com/en/articles/signing-commits

Ein exemplarischer Wiki-Eintrag um die Frage "How to create a wiki?" zu beantworten, befindet sich hier:
https://github.com/EagleoutIce/LILLY/wiki/Eine-Vorlesung-erstellen

Wenn dir beim Schreiben ein To-Do einfällt, so markiere es einfach entsprechend mit `TODO` oder `@todo` im Code :D

**Aktuelle Aufgabenfelder:**

1. [Lilly](https://github.com/EagleoutIce/LILLY/projects/3?fullscreen=true)

2. [Jake](https://github.com/EagleoutIce/LILLY/projects/2?fullscreen=true)

3. [Kommentieren](https://github.com/EagleoutIce/LILLY/projects/1?fullscreen=true)

Bitte füge alle geplanten oder gewünschten Ziele hinzu!

## Funktionen

*(Der volle Funktionsumfang, der mittlerweile weit über die unten genannten Punkte hinaus geht, ist am besten der jeweilg der Version beigelegten Dokumentation zu entnehmen.)*

Im Allgemeinen bietet Lilly die Möglichkeit ein in LaTeX geschriebenes Dokument im Nachhinein über Konsolbefehle zu konfigurieren. Hierzu bietet Lilly auch einige Funktionen zur Layoutgestaltung selbst an.
Momentan sind 3 Boxdesigns verfügbar:

- Default
- Alternate
- Limerence

sowie zwei verschiedenen Generierungstypen:

- Normal
- Druck

Desweiteren gibt es die kleine (mittlerweilen immer größer werdende) helfende Hand namens Jake.
Jake hilft bei der Installation von Lilly und fehlenden Latex-Paketen, beim Generieren der notwendigen Makefiles und noch vielem mehr!

## Deinstallation alter Lilly-Versionen

Alte Instanzen von Lilly können auch manuell entfernt werden. Es genügt die unter `${HOME}/texmf/tex/latex/` Lilly-zuzuschreibenden Dateien (je nach Installation der Ordner `Lilly` oder die `Lilly.cls` samt `source`-Ordner) zu löschen.
