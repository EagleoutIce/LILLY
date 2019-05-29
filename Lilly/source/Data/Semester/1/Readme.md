# LILLY Data - Erstes Semester

Dieses Datenpaket benötigt die Option `LILLY@Semester=1` und wird *nur* über den `_LILLY_INTRO_CONTROLLER` geladen.

## Vorlesungen
Es werden folgende Bezeichner über `LILLY@Vorlesung` zur Verfügung gestellt:

- **LAII** - Lineare Algebra für Inf. und Ing. [Leiter: Prof. Dr. Baur, ÜBL: Garlef]
- **EIDI** - Einführung in die Informatik [Leiter: Prof. Dr. Frühwirth, ÜBL: Gall & Witte]
- **GDRA** - Grundlagen der Rechnerarchitektur [Leiter: Prof. Dr. Slomka, ÜBL: Rieß]
- **FG** - Formale Grundlagen [Leiter: Prof. Dr. Torán, ÜBL: Baier]

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
