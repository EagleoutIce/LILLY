\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Lilly}[2019/02/24 EagleoutIce - Latex-Ultimate styling class with command-line support]


\providecommand{\AUTHOR}{Florian Sihler}
\providecommand{\AUTHORMAIL}{florian.sihler@web.de}
\newcommand*{\LILLYxVERSION}{1.0.7}
\newcommand*{\LILLYxSTATUS}{Work in Progress}
\newcommand*{\LILLYxVERSIONxLONG}{1.0.7 - Wanna take a shower}

\newcommand*\TikZ{\textup{Ti\textit kZ}}


\newcounter{ctr_DUMMY}

%%%% LILLY - Lilly is a latex loveable yougurht
%%%% - it doens't has to make any sense if it does look beautiful

%%% lilly unterstütz die Verhaltenssteuerung über die Kommandozeile und erlaubt es so alle Paramter auch per '\def' zu setzen!

\providecommand*{\LILLYxDummy}{dummy}                        %%% 

\input{Lilly_Files/Helper/_LILLY_PATH} %% provides \linput as \LILLYxPATH based input

\input{Lilly_Files/Helper/_LILLY_BOOLEAN}

\providecommand{\LILLYxDEBUG}{\false}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Setzen und Abfragen der Werte für das Dokument                                                                      %     %%%% PRE-DEFAULTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\ifx\LILLYxGeneralxPaperformat \undefined
    \newcommand*{\LILLYxGeneralxPaperformat}{a4paper}          %%% A4 Papier
\fi
\ifx\LILLYxGeneralxFontsize \undefined
    \newcommand*{\LILLYxGeneralxFontsize}{11pt}           %%% 11-Punkt-Schrift
\fi
\ifx\LILLYxDocumentxTYPE \undefined
    \newcommand*{\LILLYxDocumentxTYPE}{scrbook}           %%% Dokumentbasisklasse es Dokuments
\fi
\ifx\LILLYxDocumentxTYPExExtras \undefined
    \providecommand\LILLXxMODExPRINT{print}
    \ifx\LILLYxMODE\LILLXxMODExPRINT
        \newcommand*{\LILLYxDocumentxTYPExExtras}{twoside}    %%% Weitere Zusätze für das Dokument
        \ifx\LILLYxDEBUG\true
            %\newcommand*{\LILLYxGeometryxExtras}{bindingoffset=8mm,showframe}            
            \newcommand*{\LILLYxGeometryxExtras}{bindingoffset=8mm}            
        \else
            \newcommand*{\LILLYxGeometryxExtras}{bindingoffset=8mm}
        \fi
    \else
        \newcommand*{\LILLYxDocumentxTYPExExtras}{oneside}    %%% Weitere Zusätze für das Dokument
        \newcommand*{\LILLYxGeometryxExtras}{}
    \fi
\fi
\providecommand{\LILLYxLayoutxWidth}{6in}
\providecommand{\LILLYxLayoutxHeight}{9.05in}

%DEFAULT: \documentclass[a4paper,11pt,oneside]{scrbook}
\LoadClass[\LILLYxGeneralxPaperformat,\LILLYxGeneralxFontsize,titlepage=firstiscover,\LILLYxDocumentxTYPExExtras,table,makeidx]{\LILLYxDocumentxTYPE}
\RequirePackage[\LILLYxGeneralxPaperformat, \LILLYxGeometryxExtras,total={\LILLYxLayoutxWidth, \LILLYxLayoutxHeight},top=1.25in,centering]{geometry}
%top=1.5in,bottom=1.25in,textwidth=6.25in]{geometry}

\RequirePackage[german,english]{babel}

\RequirePackage{tabularx} %%FIX REDEFBUG OF TABULARX WIDTHCONTROL 
\RequirePackage{ltablex} %%FIX REDEFBUG OF TABULARX WIDTHCONTROL 

\keepXColumns


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Grundlegende Pakete (Encoding etc.)                                                                                 %     %%%% ENCODINGS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\ifx\LILLYxEXCLUDExEncodings \undefined                   %%% Lade standartkonfiguration für Encoding
    \RequirePackage[utf8x]{inputenc}                        %unicode
    \RequirePackage[T1]{fontenc}                            %8-Bit encoding
    \RequirePackage{ngerman}                                %Deutsche Sprache
    \RequirePackage{lmodern}
\fi

\RequirePackage{expl3}

\input{Lilly_Files/Helper/_LILLY_COMMAND}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Schriftarten                                                                                                        %     %%%% FONTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{Lilly_Files/Data/_LILLY_FONTS}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Shortcuts                                                                                                           %     %%%% SHORTCUTS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{Lilly_Files/Controllers/_LILLY_ENVIRONMENT_CONTROLLER}
\input{Lilly_Files/Helper/_LILLY_SHORTCUTS}
\RequirePackage{wrapfig}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Key-Val-Optionen zum verarbeiten von Argumenten                                                                     %     %%%% PACKAGE-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{Lilly_Files/Controllers/Parser/_LILLY_KEYVAL_PARSER}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% TiKz und andere Grafik freuden                                                                                      %     %%%% TIKZ-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{tocloft}
\RequirePackage{multirow}
\RequirePackage{booktabs}

\ifx\LILLYxTIKZxenable \undefined
    \input{Lilly_Files/Data/Graphics/Tikz-Core/_LILLY_TIKZ_CORE}
    \tikzset{invclip/.style={clip,insert path={{[reset cm](-100pt,-100pt) rectangle (100pt,100pt)}}}} %Für die FG-Kreise, damit man clippings auch invertieren kann
\fi

%%MOVE TO COMPAT
\newcommand{\trenner}{\ensuremath{\tab[0.05cm]\Big|\tab[0.05cm]}}




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Lstlisting include                                                                                                  %     %%%% LSTLISTING-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{multicol}

\input{Lilly_Files/Listings/_LILLY_LISTINGS_STYLE}

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


\input{Lilly_Files/Helper/_LILLY_SWITCH_CASE}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Generelle Controller                                                                                                %     %%%% General Controller %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\input{Lilly_Files/Controllers/_LILLY_MODE_CONTROLLER}
\input{Lilly_Files/Controllers/_LILLY_LINK_CONTROLLER}

\input{Lilly_Files/Controllers/Intro/_LILLY_INTRO_CONTROLLER}

\input{Lilly_Files/Controllers/_LILLY_BOX_CONTROLLER}




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Setup für mathematisches                                                                                            %     %%%% PACKAGE-OPTIONS %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\providecommand{\LILLYxMathxMode}{normal}                %%% Niemand mag euler-mathfont


\input{Lilly_Files/Maths/_LILLY_MATHS_INCLUDES}

%\RequirePackage{kpfonts}

\mathversion{\LILLYxMathxMode}                              % Welche Schriftart für Matheumgebung

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Mathe Shortcuts %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Zahlen und andere Freuden 

\input{Lilly_Files/Maths/_LILLY_MATHS_SYMBOLS}
\input{Lilly_Files/Maths/_LILLY_MATHS_OPERATORS}
\input{Lilly_Files/Maths/_LILLY_MATHS_COMPATIBILITIES}

\input{Lilly_Files/Controllers/Outro/_LILLY_OUTRO_CONTROLLER}
