Der Inhalt des Dictonaries wurde erstellt mit:
```bash
aspell --lang de dump master | aspell --lang de expand | tr ' ' '\n' > de_DE.dic
```
Hierzu muss das deutsche `aspell` Paket installiert sein:
```bash
sudo apt install aspell-de
```