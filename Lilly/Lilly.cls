\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Lilly}[2019/07/14 EagleoutIce - Latex-Ultimate styling class with command-line support]
\providecommand{\AUTHOR}{Florian Sihler}
\providecommand{\AUTHORMAIL}{florian.sihler@uni-ulm.de}

\RequirePackage{LILLYxVANILLA}% Wird benötigt, falls Jake nicht verwendet wird :D
%% Liefert außerdem: LILLYxBOOLEAN und LILLYxDEBUG!

\newcounter{ctr_DUMMY}
%%%% LILLY - Lilly is a latex loveable yougurht
%%%% - it doens't has to make any sense if it does look beautiful
%%% lilly unterstütz die Verhaltenssteuerung über die Kommandozeile und erlaubt es so alle Paramter auch per '\def' zu setzen!
\providecommand*\LILLYxDummy{dummy}                        %%% ONLY THING DEFINE HERE
% Key-Val-Optionen zum verarbeiten von Argumenten

\RequirePackage{LILLYxKEYVALxPARSER,LILLYxCORE,LILLYxLIST,LILLYxRANDOM}
\input{\LILLYxPATHxDATA/_LILLY_GENERAL_COMMANDS}

% Setzen und Abfragen der Werte für das Dokument
\providecommand\LILLYxMODExPRINT{print}
\ifx\LILLYxMODE\LILLYxMODExPRINT
\providecommand{\LILLYxDocumentxTYPExExtras}{twoside}    %%% Weitere Zusätze für das Dokument
\providecommand{\LILLYxLeftOffset}{18mm}
\ifx\LILLYxDEBUG\true
%\providecommand{\LILLYxGeometryxExtras}{bindingoffset=8mm,showframe}
\def\LILLYxGeometryxExtras{bindingoffset=\LILLYxLeftOffset}
\else
\def\LILLYxGeometryxExtras{bindingoffset=\LILLYxLeftOffset}
\fi
\else
\providecommand{\LILLYxDocumentxTYPExExtras}{oneside}    %%% Weitere Zusätze für das Dokument
\fi
\LILLYxPoliteKnock[.cls]{\LILLYxDocumentxTYPE}{} %% Hier ist alles toll
                  {\ClassError{Ich brauche leider \LILLYxDocumentxTYPE als Paket um irgendwas tun zu können! Ich darf auch nicht auf article oder gleichwertige Pakete umsteigen. Tut mir Leid, so wird das nichts!}} %% Hier dann wohl nicht!

\RequirePackage{\LILLYxPATHxDATA/Semester/EIDI/tikz-uml}
\tikzumlset{fill class=MudWhite, fill state=MudWhite, fill note=MudWhite!20} % , font=\small\LILLYxlstTypeWriter


%DEFAULT: \documentclass[a4paper,11pt,oneside]{scrbook}
\LoadClass[\LILLYxGeneralxPaperformat,\LILLYxGeneralxFontsize,titlepage=firstiscover,\LILLYxDocumentxTYPExExtras,\LILLYxPAPER]{\LILLYxDocumentxTYPE}

%% Finde ich so generell schöner zum Lesen :D
\renewcommand*\thefootnote{$\langle$\textbf{\arabic{footnote}}$\rangle$}
\renewcommand*\thempfootnote{$\langle$\textbf{\doublealph{mpfootnote}}$\rangle$}

\LILLYxDemandPackage{geometry}{Paket zum Einstellen das Seitenlayouts}%% Package, Info
    {Dieses Paket ist für LILLY überlebenswichtig}%% Error-Text
    {\LILLYxGeneralxPaperformat,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight}, top=1.25in,centering,\LILLYxGeometryxExtras}%%Params
    {}
\LILLYxDemandPackage{babel}{Dieses Paket brauchen wir für Sprachen, weil sie toll sind}
    {Um dieses Paket kommt LILLY nicht herum!}
    {english,ngerman}{}
\LILLYxDemandPackage{pgfkeys}{Stellt uns das beliebte Schlüssel-Wert-Spiel zur Verfuegung}
    {Lilly ist sich ziemlich sicher, aber doch nicht ganz ob dieses Paket relevant ist}
    {}{} %% keine Argumente
\LILLYxLoadPackage{graphicx}{Fuer tolle Grafiken}
    {Dieses Paket ist für includegraphicsvon noeten!}
    {\input{\LILLYxPATHxFALLBACKS/_LILLY_FALLBACK_GRAPHICX}} %% Sloppy includegraphics draft
    {}{}
\LILLYxLoadPackage{setspace}{Abstand Wapstand Lapstnd}
    {Dieses Paket ist für Lilly nicht lebensnotwendig}
    {} %% Wir müssen keine Befehle faken damit die Welt schön aussieht
    {}{}
\RequirePackage{refcount,cancel,LILLYxENCODING}
\newcommand*\textLilly{\raisebox{-.125\baselineskip}{\kfont{Lilly}}}

%% https://www.ctan.org/pkg/expl3
\LILLYxDemandPackage{expl3}{Da wir auch was von der tollen Latex3-Zukunft abhaben moegen :D}
    {Leider kann LILLY aus diversen Gründen nicht ohne expl3 existieren :/}
    {}{} %% keine Argumente

%% https://www.ctan.org/pkg/xparse
\LILLYxDemandPackage{xparse}{Damit es auch toll und einfach geht mehrere optionale Argumente zu verwalten!}
                    {Leider kann LILLY aus diversen Gründen nicht ohne xparse existieren :/}
                    {log-declarations=false}{}
% Silence unwanted output:
\iflillydebug\else
\ExplSyntaxOn
\msg_redirect_module:nnn { LaTeX / xparse } { info }    { none }
\msg_redirect_module:nnn { LaTeX / xparse } { warning } { none }
\ExplSyntaxOff
\fi
\RequirePackage{LILLYxRANDOMxFLAVOURTEXT,LILLYxCOMMAND}
% Schriftarten
\RequirePackage{LILLYxFONTS}
% Shortcuts
\RequirePackage{LILLYxCONTROLLERxENVIRONMENT}
\RequirePackage{LILLYxSHORTCUTS}
% TiKz und andere Grafik freuden
\RequirePackage{LILLYxCOLOR}
%% https://www.ctan.org/pkg/tocloft
\LILLYxDemandPackage{tocloft}{custom lists und so}
    {Leider wird dieses Paket zu oft verwendet :/}
    {}{} %% keine Argumente
\RequirePackage{hhline}
\ifLILLY@graphics
    \RequirePackage{LILLYxGRAPHICS}
\fi

% Lstlisting include
\RequirePackage{multicol}
\ifLILLY@listings
    \RequirePackage{LILLYxLISTINGS}
    \RequirePackage{LILLYxLISTINGSxADVANCED}
\fi

% Unterdrücken verschiedener Fehler
\hbadness=99999
\hfuzz=15000pt
\clubpenalty10000
\widowpenalty10000
\pdfminorversion=5
\pdfcompresslevel=9
\pdfobjcompresslevel=2

\RequirePackage{LILLYxSWITCHxCASE}
% Generelle Controller
\RequirePackage{LILLYxPHILOSOPHER,LILLYxCONTROLLERxMODE,LILLYxCONTROLLERxLINK,LILLYxBOXES,LILLYxCONTROLLERxHYPHEN}

% Setup für mathematisches
\ifLILLY@math
    \RequirePackage{LILLYxMATH}
\fi

% Übersetzungen, alternativen, etc...
\RequirePackage{LILLYxTRANSLATIONSxUNIVERSE}

% Datendarstellungen, Presenter
\ifLILLY@presenter
    \RequirePackage{LILLYxPRESENTER}
\fi

% should be linked to gdra?
\RequirePackage{LILLYxMIPS,LILLYxCONVERTERxMAYA}
% link to eidi?
\RequirePackage{LILLYxHeapNStack,LILLYxCONTROLLERxLAYOUT}
\endinput