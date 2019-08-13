import glob
import sys
import os

all = glob.glob("**/*.tex", recursive=True)
all.remove('all.tex') # this file

def pdfwsamename(x):
    return os.path.isfile(x+ '.pdf')



def gt (x):
    return x, os.path.getmtime(x)

if len(sys.argv) > 1:
    if sys.argv[1] == 'latest':
        min, mint = gt(all[0])
        for a in all:
            tmin, tmint = gt(a)
            # print(tmin + ": " + str(tmint))
            if tmint > mint and tmin:
                min, mint = tmin, tmint
        all = [min]

# print(min + ": " + str(mint))
all.sort()


prefix = ""
oprefix = ""
with open("./all.tex",'w') as out:
    out.write("\\documentclass[PLAIN]{Lilly}\n");
    out.write("\\begin{document}\n\\pagenumbering{arabic}\n\\tableofcontents \\clearpage \n");
    out.write("\\begin{lstplain}[language=lLatex]\n%%Einbindung erfolgt über:\n\\getGraphics{:lan:Pfad:ran:}\n \\end{lstplain}\n")
    out.write("\\begin{tabularx}{\\linewidth}{^m{0.5\\linewidth}^>{\\centering\\arraybackslash}X+}\n\\toprule\\headerrow Pfad & Ergebnis\\\\\n\\midrule\n")
    for x in all:
        prefix = x[0:x.index("/")]
        if prefix != oprefix:
            out.write("\n\\phantomsection \\addcontentsline{{toc}}{{chapter}}{{{0}}}\n".format(prefix))
            oprefix = prefix
        x = x.replace(".tex","")
        if pdfwsamename(x):
            out.write("\\phantomsection \\addcontentsline{{toc}}{{section}}{{{1}}}\\verb|{0}|\\quad{{\\tiny pdf}}& \\includegraphics[width=0.8\\linewidth]{{{2}}}\\\\\n\\midrule ".format(x,x[x.index("/")+1:],x+".pdf"))
        else:
            out.write("\\phantomsection \\addcontentsline{{toc}}{{section}}{{{1}}}\\verb|{0}| & \\getGraphics{{{0}}}\\\\\n\\midrule ".format(x,x[x.index("/")+1:]))
    out.write("\\bottomrule\n\\end{tabularx}\n")
    out.write("\\end{document}")

os.system("jake all.tex -lilly-nameprefix: \"\" -lilly-show-boxname: false");
os.system("xdg-open ./all-OUT/all.pdf")

