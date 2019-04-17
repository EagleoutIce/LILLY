# Lilly_Files>Fallbacks

---
Dieser Ordner ist unmittelbar abhängig von _LILLY_PACKAGE_CTRL und sollte auch nur auf der Basis der darin definierten Befehle:

- `\LILLYxDemandPackage (Signatur: {Paketname}{Infotext}{Fehlermeldung}{Optionale Paketargumente}{Code für den Fall, dass das Paket existiert})`
  Information: Das letzte Argument ist mitnichten obsolet. Da Latex bei derartigen Fehlern nicht direkt abbrechen soll,
  können so Folge-Fehler vermieden werden!

- `\LILLYxLoadPackage (Signatur: {Paketname}{Infotext}{Fehlermeldung}{Fehlerbehandlung}{Optionale Paketargumente}{Code für den positiven Fall})`


durch Controller verwendet Werden!

---

#### Sinn und Zweck

Um im Falle einer Abwesenheit von Paketen wie Wrapfig dennoch ein funktionales Dokument zu erzeugen werden in dieser
Datei Fallbacks für oft genutzte Befehle verwendet. Sie sind in dieser Hinsicht weder Vollständig noch in irgendeiner
Form adäquate Ersetzungen, allerdings sollte es nicht verpflichtend sein jedes Paket zu Besitzen!
