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

## Der Generator-Parser

-----

**Author:** Florian Sihler

**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019

**Gehört zu:** [Jake](../../README.md) 1.0.7

**Wohnt in:** [`j_Generator_Parser.hpp`](j_Generator_Parser.hpp) (Quelle: [`j_Generator_Parser.cpp`](j_Generator_Parser.cpp))

----

Super genereller Parser der in der Lage ist interne Konfigurations-Strukturen auf Basis von Gruppen zu erstellen. 


## Die Buildrules

-----

**Author:** Florian Sihler

**Version:** 1.0.7 (LILLY: 1.0.7) -- 5.4.2019

**Gehört zu:** [Jake](../../README.md) 1.0.7

**Wohnt in:** [`j_buildrules.hpp`](box_profiles/j_buildrules.hpp) (Quelle: [`j_buildrules.cpp`](box_profiles/j_buildrules.cpp))

----

Beispielhafte Implementation einer Funktionserweiterung auf dieser Basis weiß der Generator Parser wie er die
jObjects zur Verfügung stellen soll. Somit lassen sich beliebige buildrules erstellen. 
Da diese die Datei auch nicht verändern können übrigens alle Boxen in eine Datei gepackt werden :D 
Das ist ja der Sinn und Zweck der veschiedenen Gruppen :bowtie:

### Wie geht so eine Buildrule?

Im folgenden die Beispielhafte Implementation zweier Standard Buildrules sowie eines kleinen Bonus. 
Dies ist ein Aufzug der Datei: [`build_modes.parse`](../../../tests/build_modes.parse): 

```
BEGIN buildrule: ! Der Doppelpunkt ist optional. Ich mag ihn, man braucht ihn nicht !

    ! Das Einrücken _und_ die Leerfelder sind optional. !
    ! Allerdings sollten erstmal nur Leerfelder verwendet werden !
    ! Mit X sind Zuweisungen markiert die verpflichtend sein sollen (aber nicht sind) !

!X!  name            = default     ! buildrule name für lilly-modes !

!X!  display-name    = Standard    ! Anzeigename (Standard-Version) !

!X!  lilly-mode      = default     ! Welcher Modus soll an Lilly übergeben werden? !
                                   ! Info: Diese können noch nicht frei konfiguriert werden !

    complete         = false       ! Keine complete-Version !

    complete-prefix  = c_          ! Bezeichner wenn complete !

    nameprefix       = MY-DEFAULT- ! Weicht vom normalen default ab !

    lilly-loader     = \\\\input{$(INPUTDIR)$(TEXFILE)}
                    ! Diese Funktion ist advanced und beschreib die einbinde routine - einfach ignorieren !

END; ! Semikolon wieder nicht nötig, aber ich mag es :D !

BEGIN buildrule:

!X! name            = print
!X! display-name    = Druck
!X! lilly-mode      = print
    complete        = false
    complete-prefix = c_
    nameprefix      = MY-PRINT-
    lilly-loader    = \\\\input{$(INPUTDIR)$(TEXFILE)}

END;

BEGIN buildrule:

!X! name            = Waffel
!X! display-name    = DEMO-WAFFEL
!X! lilly-mode      = default
    nameprefix      = waffel-

END;
```
