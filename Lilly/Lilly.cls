\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Lilly}[2019/07/14 EagleoutIce - Latex-Ultimate styling class with command-line support]
\providecommand\AUTHOR{Florian Sihler}
\providecommand\AUTHORMAIL{florian.sihler@uni-ulm.de}
\RequirePackage{LILLYxVANILLA}
\newcounter{ctr_DUMMY}
% LILLY - Lilly is a latex loveable yougurht
% - it doens't has to make any sense if it does look beautiful
\providecommand*\LILLYxDummy{dummy}
\RequirePackage{LILLYxKEYVALxPARSER,LILLYxCORE,LILLYxLIST,LILLYxRANDOM}
\input{\LILLYxPATHxDATA/_LILLY_GENERAL_COMMANDS}
\providecommand\LILLYxMODExPRINT{print}
\ifx\LILLYxMODE\LILLYxMODExPRINT
\providecommand\LILLYxDocumentxTYPExExtras{twoside}
\providecommand\LILLYxLeftOffset{18mm}
\ifx\LILLYxDEBUG\true   \def\LILLYxGeometryxExtras{bindingoffset=\LILLYxLeftOffset} \else
\def\LILLYxGeometryxExtras{bindingoffset=\LILLYxLeftOffset} \fi
\else \providecommand\LILLYxDocumentxTYPExExtras{oneside} \fi
\LILLYxPoliteKnock[.cls]{\LILLYxDocumentxTYPE}{}
                  {\ClassError{Ich brauche leider \LILLYxDocumentxTYPE als Paket um irgendwas tun zu können! Ich darf auch nicht auf article oder gleichwertige Pakete umsteigen. Tut mir Leid, so wird das nichts!}}
\RequirePackage{\LILLYxPATHxDATA/Semester/EIDI/tikz-uml}
\tikzumlset{fill class=MudWhite, fill state=MudWhite, fill note=MudWhite!20}
\LoadClass[\LILLYxGeneralxPaperformat,\LILLYxGeneralxFontsize,titlepage=firstiscover,\LILLYxDocumentxTYPExExtras,\LILLYxPAPER]{\LILLYxDocumentxTYPE}

% Finde ich so generell schöner zum Lesen :D
\renewcommand*\thefootnote{$\langle$\textbf{\arabic{footnote}}$\rangle$}
\renewcommand*\thempfootnote{$\langle$\textbf{\doublealph{mpfootnote}}$\rangle$}

\LILLYxDemandPackage{geometry}{Paket zum Einstellen das Seitenlayouts}% Package, Info
    {Dieses Paket ist für LILLY überlebenswichtig}% Error-Text
    {\LILLYxGeneralxPaperformat,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight}, top=1.25in,centering,\LILLYxGeometryxExtras}{}
\LILLYxDemandPackage{babel}{Sprachverwaltung}
    {Um dieses Paket kommt LILLY nicht herum!}{english,ngerman}{}
\LILLYxDemandPackage{pgfkeys}{Schlüssel-Wert Paare}
    {Lilly ist sich ziemlich sicher, aber doch nicht ganz ob dieses Paket relevant ist}{}{}
\LILLYxLoadPackage{graphicx}{Fuer tolle Grafiken}
    {Dieses Paket ist für includegraphicsvon noeten!}
    {\input{\LILLYxPATHxFALLBACKS/_LILLY_FALLBACK_GRAPHICX}}{}{}
\LILLYxLoadPackage{setspace}{Abstand Wapstand Lapstnd}
    {Dieses Paket ist für Lilly nicht lebensnotwendig}
    {}{}{}
\RequirePackage{refcount,cancel,LILLYxENCODING}
\newcommand*\textLilly{\raisebox{-.125\baselineskip}{\kfont{Lilly}}}

\LILLYxDemandPackage{expl3}{Da wir auch was von der tollen Latex3-Zukunft abhaben moegen :D}
    {Leider kann LILLY aus diversen Gründen nicht ohne expl3 existieren :/}{}{}

\LILLYxDemandPackage{xparse}{Damit es auch toll und einfach geht mehrere optionale Argumente zu verwalten!}
                    {Leider kann LILLY aus diversen Gründen nicht ohne xparse existieren :/}
                    {log-declarations=false}{}
% Silence unwanted output:
\iflillydebug\else\ExplSyntaxOn
\msg_redirect_module:nnn { LaTeX / xparse } { info }    { none }
\msg_redirect_module:nnn { LaTeX / xparse } { warning } { none }
\ExplSyntaxOff\fi
\RequirePackage{LILLYxRANDOMxFLAVOURTEXT,LILLYxCOMMAND,LILLYxFONTS,LILLYxCONTROLLERxENVIRONMENT,LILLYxSHORTCUTS,LILLYxCOLOR}

\LILLYxDemandPackage{tocloft}{custom lists und so}{Leider wird dieses Paket zu oft verwendet :/}{}{}
\RequirePackage{hhline}
\ifLILLY@graphics\RequirePackage{LILLYxGRAPHICS}\fi
% Lstlisting include
\RequirePackage{multicol}
\ifLILLY@listings \RequirePackage{LILLYxLISTINGS,LILLYxLISTINGSxADVANCED}\fi
% Unterdrücken verschiedener Fehler
\hbadness99999 \hfuzz15000pt
\clubpenalty10000 \widowpenalty10000
\pdfminorversion5 \pdfcompresslevel9
\pdfobjcompresslevel2
\RequirePackage{LILLYxSWITCHxCASE,LILLYxPHILOSOPHER,LILLYxCONTROLLERxMODE,LILLYxCONTROLLERxLINK,LILLYxBOXES,LILLYxCONTROLLERxHYPHEN}
\ifLILLY@math \RequirePackage{LILLYxMATH} \fi
\RequirePackage{LILLYxTRANSLATIONSxUNIVERSE}
\ifLILLY@presenter \RequirePackage{LILLYxPRESENTER} \fi
\RequirePackage{LILLYxMIPS,LILLYxCONVERTERxMAYA,LILLYxHeapNStack,LILLYxCONTROLLERxLAYOUT}
\endinput