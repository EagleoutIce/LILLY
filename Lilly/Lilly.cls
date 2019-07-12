\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Lilly}[2019/02/24 EagleoutIce - Latex-Ultimate styling class with command-line support]


\providecommand{\AUTHOR}{Florian Sihler}
\providecommand{\AUTHORMAIL}{florian.sihler@web.de}
\newcommand*{\LILLYxVERSION}{1.0.9}
\newcommand*{\LILLYxSTATUS}{Work in Progress}
\newcommand*{\LILLYxVERSIONxLONG}{1.0.9 - Jakes' Revolution}


\providecommand{\LILLYxSemester}{1}
\providecommand{\LILLYxVorlesung}{LAII}

\RequirePackage{source/Helper/LILLYxPACKAGExCTRL}
\RequirePackage{source/Controllers/LILLYxCONTROLLERxCONTENT}


%\LILLY%LoadPackage{lipsum42}{Gutes Paket}{MAUMAUMAU}{\def\MauMau{Hauihao}}
%\LILLY%DemandPackage{lipsum42}{Super Paket}{HAUHAUHAU}{\def\MauMau{Raurau}}

\input{\LILLYxPATHxDATA/_LILLY_GENERAL_COMMANDS}

\newcounter{ctr_DUMMY}

%%%% LILLY - Lilly is a latex loveable yougurht
%%%% - it doens't has to make any sense if it does look beautiful

%%% lilly unterstütz die Verhaltenssteuerung über die Kommandozeile und erlaubt es so alle Paramter auch per '\def' zu setzen!

\providecommand*{\LILLYxDummy}{dummy}                        %%% ONLY THING DEFINE HERE

\input{\LILLYxPATHxHELPER/_LILLY_PATH} %% provides \linput as \LILLYxPATH based input

\input{\LILLYxPATHxHELPER/_LILLY_BOOLEAN}

\providecommand{\LILLYxDEBUG}{\false}                        %% DOES NOETHING USEFULE @Time .... @LEAST I THINK - haha



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Key-Val-Optionen zum verarbeiten von Argumenten                                                                     %     %%%% PACKAGE-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{\LILLYxPATHxCONTROLLERS/Parser/_LILLY_KEYVAL_PARSER}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Setzen und Abfragen der Werte für das Dokument                                                                      %     %%%% PRE-DEFAULTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\ifx\LILLYxGeneralxPaperformat \undefined
    \providecommand{\LILLYxGeneralxPaperformat}{a4paper}          %%% A4 Papier
\fi
\ifx\LILLYxGeneralxFontsize \undefined
    \providecommand{\LILLYxGeneralxFontsize}{11pt}           %%% 11-Punkt-Schrift
\fi
\ifx\LILLYxDocumentxTYPE \undefined
    \providecommand{\LILLYxDocumentxTYPE}{scrbook}           %%% Dokumentbasisklasse es Dokuments
\fi
\ifx\LILLYxDocumentxTYPExExtras \undefined
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
\RequirePackage{colortbl}
\RequirePackage{\LILLYxPATHxDATA/Semester/1/EIDI/tikz-uml}


%DEFAULT: \documentclass[a4paper,11pt,oneside]{scrbook}
\LoadClass[\LILLYxGeneralxPaperformat,\LILLYxGeneralxFontsize,titlepage=firstiscover,\LILLYxDocumentxTYPExExtras,table,makeidx,\LILLYxPAPER]{\LILLYxDocumentxTYPE}

%%\RequirePackage[\LILLYxGeneralxPaperformat, \LILLYxGeometryxExtras,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight},top=1.25in,centering]{geometry}
%top=1.5in,bottom=1.25in,textwidth=6.25in]{geometry}

%% NEU, weil cooler:

\LILLYxDemandPackage{geometry}{Paket zum Einstellen das Seitenlayouts}%% Package, Info
                    {Dieses Paket ist für LILLY überlebenswichtig}%% Error-Text
                    {\LILLYxGeneralxPaperformat, \LILLYxGeometryxExtras,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight}, top=1.25in,centering}%%Params
                    {}%% Diese sind nicht optional, damit jake die benötigten Bibliothken weitaus einfacher finden kann :D - wenn er das dann mal kann


%% \RequirePackage[english,ngerman]{babel}

\LILLYxDemandPackage{babel}{Dieses Paket brauchen wir für Sprachen, weil sie toll sind}
                    {Um dieses Paket kommt LILLY nicht herum!}
                    {english,ngerman}{}


%% \RequirePackage{pgfkeys}

\LILLYxDemandPackage{pgfkeys}{Stellt uns das beliebte Schlüssel-Wert-Spiel zur Verfuegung} %% IST DIES ÜBERHAUPT NÖTIG? TODO:
                    {Lilly ist sich ziemlich sicher, aber doch nicht ganz ob dieses Paket relevant ist}
                    {}{} %% keine Argumente


%%% TODO: MOVE: MOVE ME PLS
% \RequirePackage{tabularx} %%FIX REDEFBUG OF TABULARX WIDTHCONTROL

\LILLYxDemandPackage{tabularx}{Niemand würde es wagen, das glorreiche tabularx-Paket zu ersetzen!}
                    {NEIN NEIN NEIN OHNE MACHE ICH GARNICHTS - Aber, ich brauch doch gar kein tabularx %
                     - ABER ICH BIN EINE FLIESE.... ICH MEINE EIN DRUCKER... EINE BIENE... EIN STEIN? %
                     AUF JEDEN FALL GEHT DAS NICHT! AAAAAAAAARGH}
                    {}{}


\LILLYxLoadPackage{graphicx}{Für tolle Grafiken}
                    {Dieses Paket ist für includegraphics von nöten!}
                    {\input{\LILLYxPATHxFALLBACKS/_LILLY_FALLBACK_GRAPHICX}} %% Sloppy includegraphics draft
                    {}{}

\LILLYxLoadPackage{setspace}{Abstand Wapstand Lapstnd}
                    {Dieses Paket ist für Lilly nicht Lebensnotwendig}
                    {} %% Wir müssen keine Befehle faken damit die Welt schön aussieht
                    {}{}

\RequirePackage{ltablex} %%FIX REDEFBUG OF TABULARX WIDTHCONTROL
\RequirePackage{refcount}
\keepXColumns

\newlength{\LILLYxNegativeSkip}\setlength{\LILLYxNegativeSkip}{-1.5\baselineskip}
\providecommand{\nskip}{\vspace{\LILLYxNegativeSkip}}
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Grundlegende Pakete (Encoding etc.)                                                                                 %     %%%% ENCODINGS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\ifx\LILLYxEXCLUDExEncodings \undefined                   %%% Lade Standardkonfiguration für Encoding

%% \RequirePackage[utf8x]{inputenc}                        %unicode
    \LILLYxLoadPackage{inputenc}{Damit Eingaben auch brav codiert werden :D}
                      {Kein Inputencoding? Das ist seltsam, dann lasse ich die Option wohl weg?}
                      {} %% Wir müssen keine Befehle faken damit die Welt schön aussieht
                      {utf8x}{}
    %% FIX COMPAT ISSUE

%%    \RequirePackage[T1]{fontenc}                            %8-Bit encoding

    \LILLYxLoadPackage{fontenc}{Damit Schriften auch brav codiert werden :D}
                      {Kein Fontencoding? Das ist seltsam, dann lasse ich die Option wohl weg?}
                      {\def\LILLYxDEMOxFONTENC{Ist nicht Da}} %% Heile heile Welt
                      {T1}{\def\LILLYxDEMOxFONTENC{Ist Da}}


%%   \RequirePackage{ngerman}                                %Deutsche Sprache

    \LILLYxLoadPackage{ngerman}{Damit auch wirklich Deutsch gesprochen wird, ist zur Sicherheit da, redundant da babel!}
                      {Keine Deutsche Sprache ist schade, aber da mach ich mal nix :D}
                      {} %% Heile heile Welt
                      {}{} %% NO ARGS

%%  \RequirePackage{lmodern}

    \LILLYxLoadPackage{lmodern}{Da die normale Computer Modern in PDFs pixelig wirkt: nimm Latin Modern}
                      {Pffh dann behalte halt deine Pixel wenn es lmodern nicht gibt}
                      {} %% Heile heile Welt
                      {}{} %% NO ARGS

    \RequirePackage{textcomp}

\fi

\PrerenderUnicode{ä}\PrerenderUnicode{ö}
\PrerenderUnicode{ü}\PrerenderUnicode{ß}


\input Konanur.fd


\newcommand{\kfont}[1]{\begingroup\usefont{U}{Konanur}{xl}{n}#1\endgroup}

\newcommand{\Lilly}{\kfont{Lilly}}
\newcommand{\textLilly}{\raisebox{-0.125\baselineskip}{\kfont{Lilly}}}

\RequirePackage{cancel}

%%  \RequirePackage{expl3}
\LILLYxDemandPackage{expl3}{Da wir auch was von der tollen Latex3-Zukunft abhaben moegen :D}
                    {Leider kann LILLY aus diversen Gründen nicht ohne expl3 existieren :/}
                    {}{} %% keine Argumente


\LILLYxDemandPackage{xparse}{Damit es auch toll und einfach geht mehrere optionale Argumente zu verwalten!}
                    {Leider kann LILLY aus diversen Gründen nicht ohne xparse existieren :/}
                    {}{} %% keine Argumente

\input{\LILLYxPATHxHELPER/_LILLY_COMMAND}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Schriftarten                                                                                                        %     %%%% FONTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{\LILLYxPATHxDATA/LILLYxFONTS}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Shortcuts                                                                                                           %     %%%% SHORTCUTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{\LILLYxPATHxCONTROLLERS/_LILLY_ENVIRONMENT_CONTROLLER}
\input{\LILLYxPATHxHELPER/_LILLY_SHORTCUTS}

\LILLYxLoadPackage{wrapfig}{Um Figuren und Tabellen angenehmer einzubetten}
                    {Ich versuche nun von Hand die Umgebungen wrapfigure und wraptable notduerftig zu simulieren ERROR}
                    {\input{\LILLYxPATHxFALLBACKS/_LILLY_FALLBACK_WRAPFIG}}
                    {}{} %% NO ARGS

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% TiKz und andere Grafik freuden                                                                                      %     %%%% TIKZ-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{\LILLYxPATHxDATA/Colors/LILLYxCOLOR}

\RequirePackage{tocloft}
\RequirePackage{multirow}
%% \RequirePackage{booktabs}
\LILLYxLoadPackage{booktabs}{für noch tollerererererere Tabellen}
                    {Ich finde es gut, dass du kein booktabs hast, ist ja auch zu fanatisch, aber irgendwie müssen wir damit jetzt wohl klarkommen ERROR}
                    {\input{\LILLYxPATHxFALLBACKS/_LILLY_FALLBACK_BOOKTABS}}
                    {}{} %% NO ARGS

\RequirePackage{hhline}
\ifx\LILLYxTIKZxenable \undefined
    \input{\LILLYxPATHxGRAPHICS/Tikz-Core/_LILLY_TIKZ_CORE}
    \tikzset{invclip/.style={clip,insert path={{[reset cm](-100pt,-100pt) rectangle (100pt,100pt)}}}} %Für die FG-Kreise, damit man clippings auch invertieren kann
\fi






%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Lstlisting include                                                                                                  %     %%%% LSTLISTING-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{multicol}

\input{\LILLYxPATHxLISTINGS/_LILLY_LISTINGS_STYLE}

\input{\LILLYxPATHxLISTINGS/CommandLine/_LILLY_LISTINGS_CMDLINE}

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


\input{\LILLYxPATHxHELPER/_LILLY_SWITCH_CASE}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Generelle Controller                                                                                                %     %%%% General Controller %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{\LILLYxPATHxCONTROLLERS/_LILLY_MODE_CONTROLLER}
\input{\LILLYxPATHxCONTROLLERS/_LILLY_LINK_CONTROLLER}

\input{\LILLYxPATHxCONTROLLERS/Intro/_LILLY_INTRO_CONTROLLER}

\input{\LILLYxPATHxCONTROLLERS/_LILLY_BOX_CONTROLLER}
\input{\LILLYxPATHxHELPER/_LILLY_BOX_COUNTER}

\input{\LILLYxPATHxCONTROLLERS/_LILLY_HYPHEN_CONTROLLER}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Setup für mathematisches                                                                                            %     %%%% PACKAGE-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\providecommand{\LILLYxMathxMode}{normal}                %%% Niemand mag euler-mathfont

\RequirePackage{\LILLYxPATHxMATHS/LILLYxMATHS}

%\RequirePackage{kpfonts}

\input{\LILLYxPATHxDATA/Graphics/_LILLY_GRAPHICS_PROVIDER}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Übersetzungen, alternativen, etc...                                                                                 %     %%%% TRANSLATIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{\LILLYxPATHxDATA/Aliasse/LILLYxTRANSLATIONSxUNIVERSE}

\input{\LILLYxPATHxCONTROLLERS/Outro/_LILLY_OUTRO_CONTROLLER}
