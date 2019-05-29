# LILLY Data - Zweites Semester

Dieses Datenpaket benötigt die Option `LILLY@Semester=2` und wird *nur* über den `_LILLY_INTRO_CONTROLLER` geladen.

## Vorlesungen
Es werden folgende Bezeichner über `LILLY@Vorlesung` zur Verfügung gestellt:

- **ANA1** - Analysis 1 für Inf. und Ing.   [Leiter: Prof. Dr. Jan-Willem Liebezeit, ÜBL: Müller]
- **GDBS** - Grundlagen der Betriebssysteme [Leiter: Prof. Dr.-Ing. Franz J. Hauck, ÜBL: Habiger&Mödinger]
- **PDP**  - Paradigmen der Programmierung  [Leiter: Prof. Dr. Alexander Raschke & Frühwirth, ÜBL: Böhm]
- **PVS**  - Programmierung von Systemen    [Leiter: Prof. Dr. Matthias Tichy, ÜBL: Stefan Götz]

Weiter werden noch die folgenden Proseminare ebenfalls über `LILLY@Vorlesung` akzeptiert:

- **KNN**  - Proseminar: Künstliche neuerale Netze [Leiter: PD Dr. Friedhelm Schwenker, TUT: Peter Bellmann]


Andere Bezeichner werden *nicht* beachtet. Die Spezifikation wird genutzt für:

- Mitschrieb (`LILLY@Typ=Mitschrieb`):
  - Generierung der Titelseite
  - Titel- & Fußleiste
  - Boxen design (speziell: *NoBox*)
  - Candy-Commands (zum vermeiden von Doppeldeutigkeiten)

- Übungsblatt (`LILLY@Typ=Uebungsblatt`)
  - Generierung der Tutorbox
  - Titel- & Fußleiste (No Buttons etc. )
  - Boxen design (Task-Box wird überschrieben)
  - Kompiliermodus (kann z.B. `print` forcieren)
