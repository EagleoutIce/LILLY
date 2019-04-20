# LILLY Data - Zweites Semster

Dieses Datenpaket benötigt die Option `LILLY@Semster=2` und wird *nur* über den `_LILLY_INTRO_CONTROLLER` geladen.

## Vorlesungen
Es werden folgende Bezeichner über `LILLY@Vorlesung` zur Verfügung gestellt:

**TODO**

Andere Bezeichner werden *nicht* beachtet. Die spezifikation wird genutzt für:

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
