import glob

all = glob.glob("**/*.tex", recursive=True)

with open("./all.tex",'w') as out:
    out.write("\\documentclass[PLAIN]{Lilly}\n");
    out.write("\\begin{document}\n");
    out.write("\\begin{latex}\n%%Einbindung erfolgt über:\n\\getGraphics{:lan:Pfad:ran:}\n \\end{latex}\n")
    out.write("\\begin{tabularx}{\\linewidth}{^m{0.5\\linewidth}^>{\\centering\\arraybackslash}X+}\n\\toprule\\headerrow Pfad & Ergebnis\\\\\n\\midrule\n")
    for x in all:
        if x != "all.tex" and "Eigene" not in x:
            if "DreiSchichten" in x:
                out.write("\\verb|{0}| & \\getGraphics[0.3\linewidth]{{{0}}}\\\\\n\\midrule".format(x))
            else:
                out.write("\\verb|{0}| & \\getGraphics{{{0}}}\\\\\n\\midrule".format(x))
    out.write("\\bottomrule\n\\end{tabularx}\n\\clearpage\n Code für diese Datei:\n \\begingroup\\scriptsize\\ilatex{./all.tex}\\endgroup")
    out.write("\\end{document}")

import os
os.system("jake all.tex -lilly-nameprefix: \"\" -lilly-show-boxname: false");
os.system("xdg-open ./all-OUT/all.pdf")

