# Die Controller
Controller werden von LILLY zum hauptsächlichen Kontrollfluss eingesetzt. Auch wenn sie sich noch nicht über alle Pakete durchgesetzt haben, so ist es geplant sie für jedes LILLY-Paket zu kreieren.

---

## Übersicht

1. [Boxen](#box)
2. [Environment](#environment)
3. [Worttrennungen](#Hyphen)
4. [Links](#Links)
5. [Modi](#Modi)

---
## Box-Controller <a name="box"></a>
Der Box-Controller ist das Herz der Strukturierung. Er bietet uns alle Designs die wir in der späteren Nutzung benötigen. 

Generell werden uns verschiedene Arten von Boxen zur Verfügung gestellt: 

  * Definitionen
  * Bemerkungen
  * Beispiele
  * Sätze
  * Beweis
  * Lemmas
  * Zusammenfassungen
  * Aufgaben

All diese Boxen können initialisiert werden mit `\begin{ -Boxtype- }[Titel]`.
Für Definitionen gibt es den Sonderfall `\begin{definition*}[Titel]`, wobei der Stern den Titel als "Wichtig" makiert durch eine kleine Stecknadel am oberen rechten Eck, als auch in der Zusammenfassung auf den ersten Seiten.
Des weiteren werden über den Box-Controller alle [Box-Designs](https://github.com/EagleoutIce/LILLY/tree/development-1.0.9/Lilly/Lilly_Files/Data/POIs) geladen, webei über `\LILLYxBOXxMODE` die Möglichkeit besteht eine bestimmt POI-Datei zu laden.
Außerdem liefrt der Box-Controller unmengen an weiteren Befehlen: 
 * `\LILLYxBOXx Boxtyp xEnable` kann das Anzeigen der gewählten Boxen getoggled werden
 .* `\LILLYxBOXxDefinitionxEnable` auf false deaktiviert zum Beispiel alle Definitionsboxen

## Environment-Controller <a name="environment"></a>

## Der Hyphen-Controller <a name="Hyphen"></a>

Beim einbetten einer neuen Trennung gilt es darauf zu achten, dass Umlaute nicht direkt eingegeben werden können! So muss anstelle von:

`\hyphenation{Äih-nä-trön-nung}`

Folgendes Geschrieben werden:

`\hyphenation{"Aih-n"a-tr"on-nung}`

## Link-Controller <a name="Links"></a>


## Mode-Controller <a name="Modi"></a>

