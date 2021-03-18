import glob
import sys, os, re
import subprocess, platform
dir_path = os.path.dirname(os.path.realpath(__file__))
#print(dir_path+ "/**/*.tex")
all = glob.glob(dir_path + '/**/*.tex', recursive=True)
# print(all)
for a in ["all","latest","filtered"]: # remove recursives :D
    a = dir_path + "/" + a + ".tex"
    if a in all:
        all.remove(a)

def view(path):
    if platform.system() == 'Darwin':       # macOS?
        subprocess.call(('open', path))
    elif platform.system() == 'Windows':
        os.startfile(path)
    else:
        subprocess.call(('xdg-open', path))

def pdfwsamename(x):
    return os.path.isfile(x+ '-pdf.pdf')

changewatcher = {}

def loadChanges(file):
    if os.path.isfile(file):
        lines = open(file).readlines()
        for tl in lines:
            if tl != "":
                ba = tl.split(" T: ")
                if len(ba) == 2:
                    changewatcher[ba[0]] = ba[1]

if len(sys.argv) > 1 and sys.argv[1] == "prerender":
    loadChanges("./changes.save")
else:
    loadChanges("./ALLchanges.save")

def gt (x):
    return x, os.path.getmtime(x)

def dumpChanges(file):
    with open(file,'w') as clogOut:
        for k,v in changewatcher.items():
            if k != "":
                clogOut.write("{0} T: {1}\n".format(k,v))

outname = "all"

if len(sys.argv) > 1:
    if sys.argv[1] == 'latest':
        min, mint = gt(all[0])
        for a in all:
            tmin, tmint = gt(a)
            # print(tmin + ": " + str(tmint))
            if tmint > mint and tmin:
                min, mint = tmin, tmint
        all = [min]
        outname = "latest"
    elif sys.argv[1]=="prerender":
        for x in all:
            if "-pdf" not in x: # sloppy :: "Eigene" not in x and
                x = x.replace(dir_path + "/","")
                if x not in changewatcher or float(changewatcher[x]) < os.path.getmtime(x):
                    p = x.replace(".tex","-pdf.tex")
                    with open(p,'w') as out:
                        out.write("\\documentclass[tikz,preview]{article}\n\\usepackage[active,tightpage]{preview}\n\\usepackage{LILLYxGRAPHICS}\n\\usepackage{LILLYxLISTINGS}\n\\usepackage{LILLYxSHORTCUTS}\n\\usepackage{LILLYxMATH}\n\\usepackage{LILLYxTABLES}\n\\usepackage[ngerman]{babel}\n\\begin{document}\n\\tikzumlset{fill class=MudWhite, fill state=MudWhite, fill note=MudWhite!20} % , font=\small\LILLYxlstTypeWriter}%\n")
                        out.write("\\begin{{preview}}\\getGraphics{{{0}}}\\end{{preview}}\n".format(x))
                        out.write("\\end{document}")
                        out.close()
                    h,t = os.path.split(p);
                    stmt = "jake {0} -lilly-nameprefix: \"\" -lilly-show-boxname: false -lilly-out: \"./{1}/\" -lilly-in: \"./{1}/\"".format(t,h)
                    print(stmt)
                    if os.system(stmt) != 0:
                        print("Error, Exiting...")
                        dumpChanges("./changes.save")
                        sys.exit(0)
                    os.remove(p)
                else:
                    print("[Passing (unchanged)] {0}".format(x))
                changewatcher[x] = os.path.getmtime(x)
        # write changes
        dumpChanges("./changes.save")
        sys.exit(0)

    elif sys.argv[1].startswith(":"):
        rep = re.compile(sys.argv[1][1:])
        all = list(filter(rep.search,all))
        outname = "filtered"
# print(min + ": " + str(mint))
all.sort()

newchanges={}
prefix = ""
oprefix = ""
fname = outname + ".tex"
update = False
with open("./" + fname,'w') as out:
    out.write("\\documentclass[PLAIN]{Lilly}\n");
    out.write("\\begin{document}\n\\pagenumbering{arabic}\n\\tableofcontents \\clearpage \n");
    out.write("\\begin{lstplain}[language=lLatex]\n%%Einbindung erfolgt über:\n\\getGraphics{:lan:Pfad:ran:}\n \\end{lstplain}\n")
    out.write("\\begin{tabularx}{\\linewidth}{^m{0.5\\linewidth}^>{\\centering\\arraybackslash}m{0.5\\linewidth}+}\n\\toprule\\headerrow Pfad & Ergebnis\\\\\n\\midrule\n")
    for x in all:
        x = x.replace(dir_path + "/","")
        prefix = x[0:x.index("/")]
        if x not in changewatcher or float(changewatcher[x]) < os.path.getmtime(x):
            update = True
            newchanges[x] = os.path.getmtime(x)

        if prefix != oprefix:
            out.write("\n\\phantomsection \\addcontentsline{{toc}}{{chapter}}{{{0}}}\n".format(prefix))
            oprefix = prefix
        x = x.replace(".tex","")
        print(x)
        if pdfwsamename(x):
            out.write("\\phantomsection \\addcontentsline{{toc}}{{section}}{{{1}}}\\verb|{0}|\\quad{{\\tiny pdf}}& \\includegraphics[width=0.8\\linewidth]{{{2}}}\\\\\n\\midrule ".format(x,x[x.index("/")+1:],x+"-pdf.pdf"))
        else:
            out.write("\\phantomsection \\addcontentsline{{toc}}{{section}}{{{1}}}\\verb|{0}| & \\getGraphics{{{0}}}\\\\\n\\midrule ".format(x,x[x.index("/")+1:]))
    out.write("\\bottomrule\n\\end{tabularx}\n")
    out.write("\\end{document}")
if update:
    if os.system("jake " + fname + " -lilly-nameprefix: \"\" -lilly-show-boxname: false -lilly-compiletimes: 2")==0: # update timestamps if succesful
        for k,v in newchanges.items():
            changewatcher[k] = v
        if outname == "all": ## don't dump for latest
            dumpChanges("./ALLchanges.save")
        view("{0}/{1}-OUT/{1}.pdf".format(dir_path,outname))
    else:
        print("Generation failed!")
else:
    print("Nothing has changed since last generation.... won't recompile. Delete 'ALLchanges.save' to force")
#os.system("xdg-open " + dir_path + "/" + outname + "-OUT/" + outname + ".pdf")
    view("{0}/{1}-OUT/{1}.pdf".format(dir_path,outname))
