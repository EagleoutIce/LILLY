\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Lilly}[2019/07/14 EagleoutIce - Latex-Ultimate styling class with command-line support]


\providecommand{\AUTHOR}{Florian Sihler}
\providecommand{\AUTHORMAIL}{florian.sihler@web.de}

%% Sloppy Defaults if no Jake is used:
% \providecommand{\LILLYxSemester}{1} % v2.1.0
\providecommand{\LILLYxVorlesung}{LAII}

\RequirePackage{LILLYxCORE}
\RequirePackage{LILLYxLIST}
\RequirePackage{LILLYxRANDOM}


\input{\LILLYxPATHxDATA/_LILLY_GENERAL_COMMANDS}

\newcounter{ctr_DUMMY}

%%%% LILLY - Lilly is a latex loveable yougurht
%%%% - it doens't has to make any sense if it does look beautiful

%%% lilly unterstütz die Verhaltenssteuerung über die Kommandozeile und erlaubt es so alle Paramter auch per '\def' zu setzen!

\providecommand*{\LILLYxDummy}{dummy}                        %%% ONLY THING DEFINE HERE

\RequirePackage{LILLYxPATH} %% provides \linput as \LILLYxPATH based input
\RequirePackage{LILLYxBOOLEAN} %% \true und \false



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Key-Val-Optionen zum verarbeiten von Argumenten                                                                     %     %%%% PACKAGE-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%\input{\LILLYxPATHxCONTROLLERS/Parser/_LILLY_KEYVAL_PARSER}
\RequirePackage{LILLYxKEYVALxPARSER}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Setzen und Abfragen der Werte für das Dokument                                                                      %     %%%% PRE-DEFAULTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\ifx\LILLYxGeneralxPaperformat\undefined
    \providecommand{\LILLYxGeneralxPaperformat}{a4paper}          %%% A4 Papier
\fi
\ifx\LILLYxGeneralxFontsize\undefined
    \providecommand{\LILLYxGeneralxFontsize}{11pt}           %%% 11-Punkt-Schrift
\fi
\ifx\LILLYxDocumentxTYPE\undefined
    \providecommand{\LILLYxDocumentxTYPE}{scrbook}           %%% Dokumentbasisklasse es Dokuments
\fi
\ifx\LILLYxDocumentxTYPExExtras\undefined
    \providecommand\LILLYxMODExPRINT{print}
    \ifx\LILLYxMODE\LILLYxMODExPRINT
        \providecommand{\LILLYxDocumentxTYPExExtras}{twoside}    %%% Weitere Zusätze für das Dokument
        \providecommand{\LILLYxLeftOffset}{18mm}
        \ifx\LILLYxDEBUG\true
            %\providecommand{\LILLYxGeometryxExtras}{bindingoffset=8mm,showframe}
            \providecommand{\LILLYxGeometryxExtras}{bindingoffset=\LILLYxLeftOffset}
        \else
            \providecommand{\LILLYxGeometryxExtras}{bindingoffset=\LILLYxLeftOffset}
        \fi
    \else
        \providecommand{\LILLYxDocumentxTYPExExtras}{oneside}    %%% Weitere Zusätze für das Dokument
        \providecommand{\LILLYxGeometryxExtras}{}
    \fi
\fi
\providecommand{\LILLYxLayoutxWidth}{6in}
\providecommand{\LILLYxLayoutxHeight}{9.05in}

\LILLYxPoliteKnock[.cls]{\LILLYxDocumentxTYPE}{} %% Hier ist alles toll
                  {\ClassError{Ich brauche leider \LILLYxDocumentxTYPE als Paket um irgendwas tun zu können! Ich darf auch nicht auf article oder gleichwertige Pakete umsteigen. Tut mir Leid, so wird das nichts!}} %% Hier dann wohl nicht!


%% TODO  MOVE PACKAGE
\RequirePackage{\LILLYxPATHxDATA/Semester/EIDI/tikz-uml}
\tikzumlset{fill class=MudWhite, fill state=MudWhite, fill note=MudWhite!20} % , font=\small\LILLYxlstTypeWriter


%DEFAULT: \documentclass[a4paper,11pt,oneside]{scrbook}
\LoadClass[\LILLYxGeneralxPaperformat,\LILLYxGeneralxFontsize,titlepage=firstiscover,\LILLYxDocumentxTYPExExtras,\LILLYxPAPER]{\LILLYxDocumentxTYPE}

%% Finde ich so generell schöner zum Lesen :D
\renewcommand*{\thefootnote}{$\langle$\textbf{\doublealph{footnote}}$\rangle$}
\renewcommand*{\thempfootnote}{$\langle$\textbf{\arabic{mpfootnote}}$\rangle$}

%%\RequirePackage[\LILLYxGeneralxPaperformat, \LILLYxGeometryxExtras,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight},top=1.25in,centering]{geometry}
%top=1.5in,bottom=1.25in,textwidth=6.25in]{geometry}

\RequirePackage{LILLYxSHOWCASE}

%% NEU, weil cooler:

\LILLYxDemandPackage{geometry}{Paket zum Einstellen das Seitenlayouts}%% Package, Info
                    {Dieses Paket ist für LILLY überlebenswichtig}%% Error-Text
                    {\LILLYxGeneralxPaperformat,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight}, top=1.25in,centering,\LILLYxGeometryxExtras}%%Params
                    {}%% Diese sind nicht optional, damit jake die benötigten Bibliothken weitaus einfacher finden kann :D - wenn er das dann mal kann


%% \RequirePackage[english,ngerman]{babel}

\LILLYxDemandPackage{babel}{Dieses Paket brauchen wir für Sprachen, weil sie toll sind}
                    {Um dieses Paket kommt LILLY nicht herum!}
                    {english,ngerman}{}


%% \RequirePackage{pgfkeys}

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

\RequirePackage{refcount}


\RequirePackage{LILLYxENCODING}


\newcommand{\textLilly}{\raisebox{-0.125\baselineskip}{\kfont{Lilly}}}

\RequirePackage{cancel}

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

\RequirePackage{LILLYxCOMMAND}

\RequirePackage{LILLYxRANDOMxFLAVOURTEXT}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Schriftarten                                                                                                        %     %%%% FONTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{LILLYxFONTS}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Shortcuts                                                                                                           %     %%%% SHORTCUTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%\input{\LILLYxPATHxCONTROLLERS/_LILLY_ENVIRONMENT_CONTROLLER}
\RequirePackage{LILLYxCONTROLLERxENVIRONMENT}
\RequirePackage{LILLYxSHORTCUTS}

\LILLYxLoadPackage{wrapfig}{Um Figuren und Tabellen angenehmer einzubetten}
                    {Ich versuche nun von Hand die Umgebungen wrapfigure und wraptable notduerftig zu simulieren ERROR}
                    {\input{\LILLYxPATHxFALLBACKS/_LILLY_FALLBACK_WRAPFIG}}
                    {}{} %% NO ARGS

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% TiKz und andere Grafik freuden                                                                                      %     %%%% TIKZ-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{LILLYxCOLOR}


%% https://www.ctan.org/pkg/tocloft
\LILLYxDemandPackage{tocloft}{custom lists und so}
                    {Leider wird dieses Paket zu oft verwendet :/}
                    {}{} %% keine Argumente

\RequirePackage{hhline}
\ifLILLY@graphics
    \RequirePackage{LILLYxGRAPHICS}
\fi




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Lstlisting include                                                                                                  %     %%%% LSTLISTING-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{multicol}

\ifLILLY@listings
    \RequirePackage{LILLYxLISTINGS}
    \RequirePackage{LILLYxLISTINGSxADVANCED}
\fi
\ifLILLY@runtimes
    \RequirePackage{LILLYxRUNTIMES}
\fi
%\ExplSyntaxOn
%\DeclareCaptionFont{black}{ \color{black} }
%\DeclareCaptionFormat{none}{
%    \makebox[\textwidth]{#1#2#3}
%}
%\DeclareCaptionFont{black}{ \color{black} }
%
%\ExplSyntaxOff
%\DeclareCaptionFormat{listing}{
%    \makebox[\textwidth]{#1#2#3}
%}

%\def\noprint#1{}

%\ExplSyntaxOn
%\NewDocumentCommand \mipsspecifier { }
%{
%    \tl_set:No \l_demo_tl {\the\use:c{lst@token}}
%    \regex_replace_all:nnN { .([a-zA-Z]+[0-9]+) } {\$\c{textcolor}\cB\{ limegreen \cE\}\cB{ \1 \cE} } \l_demo_tl
%    \tl_use:N \l_demo_tl
%    \noprint
%}

%\ExplSyntaxOff


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Unterdrücken verschiedener Fehler                                                                                   %     %%%% ERROR HANDLING %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\hbadness=99999
\hfuzz=15000pt
\clubpenalty=10000
\widowpenalty=10000


\RequirePackage{LILLYxSWITCHxCASE}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Generelle Controller                                                                                                %     %%%% General Controller %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{LILLYxPHILOSOPHER}

\RequirePackage{LILLYxCONTROLLERxMODE}
\RequirePackage{LILLYxCONTROLLERxLINK}

%\input{\LILLYxPATHxCONTROLLERS/Intro/_LILLY_INTRO_CONTROLLER}

\RequirePackage{LILLYxBOXES}
\RequirePackage{LILLYxCONTROLLERxHYPHEN}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Setup für mathematisches                                                                                            %     %%%% PACKAGE-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\ifLILLY@math
    \RequirePackage{LILLYxMATH}
\fi

%\RequirePackage{kpfonts}
%%\input{\LILLYxPATHxDATA/Graphics/_LILLY_GRAPHICS_PROVIDER}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Übersetzungen, alternativen, etc...                                                                                 %     %%%% TRANSLATIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{LILLYxTRANSLATIONSxUNIVERSE}

%\input{\LILLYxPATHxCONTROLLERS/Outro/_LILLY_OUTRO_CONTROLLER}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Datendarstellungen, Presenter                                                                                       %     %%%% PRESENTER %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\ifLILLY@presenter
    \RequirePackage{LILLYxPRESENTER}
\fi

% should be linked to gdra?
\RequirePackage{LILLYxMIPS}
\RequirePackage{LILLYxCONVERTERxMAYA}

% link to eidi?
\RequirePackage{LILLYxHeapNStack}

\RequirePackage{LILLYxCONTROLLERxLAYOUT}
