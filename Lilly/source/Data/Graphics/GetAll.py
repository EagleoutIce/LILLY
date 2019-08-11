import glob

all = glob.glob("**/*.tex", recursive=True)
all.sort()

prefix = ""
oprefix = ""
with open("./all.tex",'w') as out:
    out.write("\\documentclass[PLAIN]{Lilly}\n");
    out.write("\\begin{document}\n\\pagenumbering{arabic}\n\\tableofcontents \\clearpage \n");
    out.write("\\begin{lstplain}[language=lLatex]\n%%Einbindung erfolgt über:\n\\getGraphics{:lan:Pfad:ran:}\n \\end{lstplain}\n")
    out.write("\\begin{tabularx}{\\linewidth}{^m{0.5\\linewidth}^>{\\centering\\arraybackslash}X+}\n\\toprule\\headerrow Pfad & Ergebnis\\\\\n\\midrule\n")
    for x in all:
        if x != "all.tex" and "Eigene" not in x:
            prefix = x[0:x.index("/")]
            if prefix != oprefix:
                out.write("\n\\addcontentsline{{toc}}{{chapter}}{{{0}}}\n".format(prefix))
                oprefix = prefix
            x = x.replace(".tex","")
            out.write("\\addcontentsline{{toc}}{{section}}{{{1}}}\\verb|{0}| & \\getGraphics{{{0}}}\\\\\n\\midrule ".format(x,x[x.index("/")+1:]))
    out.write("\\bottomrule\n\\end{tabularx}\n")
    out.write("\\end{document}")

import os
os.system("jake all.tex -lilly-nameprefix: \"\" -lilly-show-boxname: false");
os.system("xdg-open ./all-OUT/all.pdf")

