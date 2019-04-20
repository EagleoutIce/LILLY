# Die Provider
| Dieser Ordner enthält Parser um Jake noch dynamischer zu gestalten :D |
| --- |
| Er befindet sich momentan im Aufbau! Wenn du dich daran beteiligen möchtest so melde dich einfach bei mir: `maeusepiep99@web.de`. Sonst viel Spaß beim Stöbern :D|

## Der Tokenizer

-----

**Author:** Florian Sihler

**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019

**Gehört zu:** [Jake](../../README.md) 1.0.7

**Wohnt in:** [`j_Tokenizer.hpp`](j_Tokenizer.hpp) (Quelle: [`j_Tokenizer.cpp`](j_Tokenizer.cpp))

----

Der Tokenizer liefert die gleichnamige Klasse `Tokenizer` wdelche grundlegend als Regex Parser von allen anderen
providern (zum Beispiel dem Konfigurator) verwendet wird.


## Der Konfigurator

-----

**Author:** Florian Sihler

**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019

**Gehört zu:** [Jake](../../README.md) 1.0.7

**Wohnt in:** [`j_Configurator.hpp`](j_Configurator.hpp) (Quelle: [`j_Configurator.cpp`](j_Configurator.cpp))

----

Ermöglicht es Einstellungen für Jake mithilfe der Option `config` aus einer Datei zu Laden. Die Datei wiederrum kann mithilfe
der Einstellung `file` angegeben werden!

## Der Generator-Parser (WIP)

-----

**Author:** Florian Sihler

**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019

**Gehört zu:** [Jake](../../README.md) 1.0.7

**Wohnt in:** [`j_Generator_Parser.hpp`](j_Generator_Parser.hpp) (Quelle: Work-In-Progress)

----

Aktueller TODO-Auszug:

Konzept: erkennt Grupppen mithilfe von "BEGIN <NAME>" und "END"
Der Gruppenname gibt an, was generiert werden soll. 
Beispiel: "BEGIN BOXMODE" zum Erstellen eines neuen Boxmodis
Die verchiedenen Gruppen können ihre eigenen Parameter definieren
und fordern. Der Generator_Parser arbeitet hierbei ähnlich wie 
der Konfigurator indem er ein Datenpaket erhält welches er
zu füllen versucht. 
Wie dann dieses Datenpaket zu interpretieren ist obliegt dem
initiator. So müsste es allerdings sehr einfach möglich sein
dem Generator listen für alle BOXMODE - Gruppen usw. 
zu Übergeben, sodass er diese dann füllt 
und Jake dann entsprechend damit arbieten kann!
