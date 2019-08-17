package de.eagle.lillyjakeframework.compiler;

import static de.eagle.lillyjakeframework.core.Definitions.B_EXTRA;
import static de.eagle.lillyjakeframework.core.Definitions.B_INPUT;
import static de.eagle.lillyjakeframework.core.Definitions.B_NAME;
import static de.eagle.lillyjakeframework.core.Definitions.B_TEXT;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;
import static de.eagle.util.io.JakeLogger.writeLoggerError;
import static de.eagle.util.io.JakeLogger.writeLoggerInfo;
import static de.eagle.util.io.JakeLogger.writeLoggerWarning;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eagle.gepard.modules.Buildrules;
import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.modules.Hooks;
import de.eagle.gepard.modules.NameMaps;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

/**
 * @class JakeCompile
 *
 *        Diese Klasse verwaltet den Prozess einer kompilierung durch Jake
 *
 *        Diese Instanz ist der direkte Bruder von c_Jake.cpp aus Jake-Cpp und
 *        verwendet deswegen native Systemaufrufe, auch wenn diese in der Form
 *        nicht nötig wären!
 *
 * @author Florian Sihler
 * @version 1.0.10
 * @since 1.0.10
 *
 */
public class JakeCompile {

    Settings expandables = new Settings("Expandables TODO");

    /**
     * Der eigentliche kompilierungsprozess - Jaiks
     *
     * @param command der Befehl aus der Kommandozeile
     * @return 0 wenn alles gut verlaufen ist
     * @throws IOException Wenn es ein Problem beim schreiben einer Datei gibt
     */
    public static ReturnStatus compile(String[] command) throws IOException {
        JakeLogger.writeLoggerDebug1("Jake versucht nun das Lilly-Dokument zu kompilieren", "compile");
        Settings expandables = Expandables.expandsCS();

        String targetFile = getTargetFile();

        JakeWriter.out.format("%sKompiliere Dokument: \"%s\" für: %s (%s)%s%n", ColorConstants.COL_GOLD, targetFile,
                CoreSettings.requestValue("S_LILLY_AUTHOR"), CoreSettings.requestValue("S_LILLY_AUTHORMAIL"),
                ColorConstants.COL_RESET);

        if (!new File(targetFile).canWrite()) { // check_file
            JakeWriter.err.println("Die von dir angegebene Datei: " + targetFile
                    + " konnte nicht gefunden werden! Soll Jake sie für dich erstellen?");
            switch (CommandLine.get_answer("[(y)es/(n)o/(c)ancel]> ", new String[] { "Y", "N", "C" })) {
            default:
            case "Y":
                generateDummyFile(targetFile);
                break;
            case "N":
                JakeWriter.out.println("Jake wird so tun, als gäbe es die Datei und spielt heile Welt!");
                break;
            case "C":
                JakeWriter.out.println("Abbruch!");
                return ReturnStatus.EXIT_FAILURE;
            }
        }

        // TODO: show total errors

        // System.out.println(CoreSettings.requestValue("S_FILE"));
        Settings update_config = NameMaps.whatTrigger(
                NameMaps.getNameMaps(CoreSettings.requestValue("S_GEPARDRULES_PATH")),
                CoreSettings.requestValue("S_FILE"));

        if (update_config.size() > 0) {
            JakeWriter.out.println(
                    "Information: Aufgrund des Name-Mappings werden deine Einstellungen angepasst. Die Regeln im Folgenden werden jeweils angezeigt und angwendet:");
            StringBuilder new_config = new StringBuilder();
            for (var sd : update_config) {
                JakeWriter.out.format("    - %s%n", sd.getKey());
                new_config.append(sd.getValue().getValue()).append("\n");
            }
            Configurator config_update = new Configurator(
                    new ByteArrayInputStream(new_config.toString().getBytes(Charset.defaultCharset())));
            config_update.parse_settings(CoreSettings.settings, false);
            expandables = Expandables.expandsCS();
        }
        writeLoggerDebug1("Jake Footprint: " + Definitions.PRG_BRIEF + " (" + PropertiesProvider.getNow() + ")",
                "compile"); // TODO: change to real compile date

        Settings all_hooks = Hooks.getHooks(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        // Expandables.expandSettings(all_hooks); // hooks will now be lazy-expanded

        try {
            if (CoreSettings.requestSwitch("S_LILLY_EXTERNAL"))
                Files.createDirectories(Paths.get(CoreSettings.requestValue("S_LILLY_OUT"),
                        CoreSettings.requestValue("S_LILLY_EXTERNAL_OUT")));
            else
                Files.createDirectories(Paths.get(CoreSettings.requestValue("S_LILLY_OUT")));
        } catch (IOException ex) {
            writeLoggerError("FileCreation failed!", "compile");
        }

        RequestClean();

        // execute PRE-Hooks
        Hooks.executeHooks(all_hooks, "PRE");

        Path lilly_log_out = Paths.get(CoreSettings.requestValue("S_LILLY_OUT"), "LILLY_COMPILE.log");
        File f_lilly_log_out = lilly_log_out.toFile();
        writeLoggerDebug1("Create Logfile: \"" + lilly_log_out.toString() + "\"... ", "compile");
        PrintWriter llo = new PrintWriter(f_lilly_log_out);
        llo.println("LILLY_LOGFILE stamp: " + PropertiesProvider.getNow());
        llo.close();
        writeLoggerDebug2("Write init sequence...", "compile");

        Settings b_rules = Buildrules.parseRules(CoreSettings.requestValue("S_GEPARDRULES_PATH"),
                CoreSettings.requestSwitch("S_LILLY_COMPLETE"));
        String[] buildrules = CoreSettings.requestValue("S_LILLY_MODES").split(" +");

        JakeCompile_Worker.failed = false;
        JakeCompile_Worker[] workers = new JakeCompile_Worker[buildrules.length];
        int ctr = 0;
        for (String buildrule : buildrules) {
            String br = b_rules.getValue(buildrule);
            if (br == null) {
                writeLoggerWarning("Es wurde: \"" + buildrule + "\" angefordert. Diese existiert allerdings nicht!",
                        "compiler");
                continue;
            }
            String[] b_data = br.split("!");
            if (!b_rules.containsKey(buildrule) || b_data.length != 4)
                continue;
            workers[ctr] = new JakeCompile_Worker(ctr, b_data, expandables, all_hooks);
            ctr++;
        }

        // await completion
        for (int i = 0; i < workers.length; i++) {
            try {
                if (workers[i] != null)
                    workers[i].join();
            } catch (InterruptedException e) {
                writeLoggerError("Join of ID" + i + " failed!", "compile");
            }
            writeLoggerInfo("Completed ID" + i, "compile");
            // JakeWriter.out.format("Completed ID%d%n", i); // Das muss doch niemand wissen
            // :P
        }

        if (JakeCompile_Worker.failed) {
            System.exit(1); // report failure for Linux-Systems
            return ReturnStatus.EXIT_FAILURE;
        }

        // execute POST Hooks
        Hooks.executeHooks(all_hooks, "POST");

        // Autoclean ?
        RequestClean();
        JakeWriter.out.println("Kompilieren abgeschlossen!");

        return ReturnStatus.EXIT_SUCCESS;
    }

    public static void RequestClean() throws IOException {
        if (CoreSettings.requestSwitch("S_LILLY_AUTOCLEAN")) {
            JakeWriter.out.format("%s> Lösche temporäre Dateien...%s%n", ColorConstants.COL_GOLD,
                    ColorConstants.COL_RESET);
            for (String ftext : CoreSettings.requestValue("S_LILLY_CLEANS").split(" +")) {
                Files.list(Paths.get(CoreSettings.requestValue("S_LILLY_OUT")))
                        .filter(s -> s.toString().contains("." + ftext)).forEach(s -> s.toFile().delete());
                if (CoreSettings.requestSwitch("S_LILLY_EXTERNAL"))
                    Files.list(Paths.get(CoreSettings.requestValue("S_LILLY_OUT"),
                            CoreSettings.requestValue("S_LILLY_EXTERNAL_OUT")))
                            .filter(s -> s.toString().contains("." + ftext)).forEach(s -> s.toFile().delete());
            }
        } else {
            JakeWriter.out.format("Kein autoclean, da zugehörige Einstellung (%s) != true%n",
                    CoreSettings.getTranslator().translate("S_LILLY_EXTERNAL"));
        }

    }

    public static String getTargetFile() {
        String target = Paths.get(CoreSettings.requestValue("S_LILLY_IN"), CoreSettings.requestValue("S_FILE"))
                .toString();
        JakeLogger.writeLoggerInfo("Zieldatei identifiziert: " + target, "compile");
        return target;
    }

    /**
     * Generiert ein initiales Texfile
     *
     * @param name Name der Tex-Datei
     * @return 0 wenn alles gut gelaufen ist
     * @throws IOException Wenn die Dateien nicht geschrieben werden können
     */
    public static ReturnStatus generateDummyFile(String name) throws IOException {
        // Todo identify documenttype first => nmaps!
        PrintWriter out = new PrintWriter(name);

        out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        out.write("%% Von Jake erstelltes Lilly LaTeX-File                        %%\n");
        out.write("%% Version: " + Definitions.JAKE_VERSION + "  Author: Florian Sihler                    %%\n");
        out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        switch (CoreSettings.requestValue("S_LILLY_DOCTYPE")) {
        default: // annahme: Mitschrieb
        case "Mitschrieb":
            // CURRENT POINT OF TODO
            out.write("%% Dokumenttyp: Mitschrieb\n");
            out.write("\\documentclass[Typ=Mitschrieb,Jake]{Lilly}\n\n");
            out.write("\\begin{document}\n");
            out.write("Hallo Welt\\newline\n");
            out.write("Lilly-Version: \\LILLYxSTATUS\\newline\n");
            out.write("Status: \\LILLYxSTATUS\\newline\n");
            out.write("Colormap: \\LILLYxCOLORxRainbow\n");
            out.write("\\end{document}\n");
            out.close();
            break;
        }

        return ReturnStatus.EXIT_FAILURE; // ALWAYS FAIL
    }

    public static class JakeCompile_Worker extends Thread {
        public static boolean failed = false;

        public void run() {
            long startTime = System.currentTimeMillis();
            writeLoggerInfo(b_data[B_TEXT].replace("\"\"", "") + " -Version("
                    + CoreSettings.requestValue("S_LILLY_BOXES") + ") der Latex-Datei: "
                    + CoreSettings.requestValue("S_FILE") + "..." + ColorConstants.COL_RESET, tag);

            for (String boxmode : CoreSettings.requestValue("S_LILLY_BOXES").split(" +")) {
                writeLoggerInfo("Schreibe boxmode...", tag);
                // TODO: for threadsafety chagne name including thread id and pass threadid on
                // compile
                File bibt = Paths.get(PropertiesProvider.getTempPath(), "lillytmp.bib.p").toFile();
                try (PrintWriter bibo = new PrintWriter(bibt)) {
                    bibo.println(boxmode);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String final_name = b_data[B_NAME]
                        + (CoreSettings.requestSwitch("S_LILLY_SHOW_BOX_NAME") ? boxmode + "-" : "")
                        + new File(CoreSettings.requestValue("S_FILE")).toString().replaceFirst("[.][^.]+$", "");
                final_name = final_name.replace("\"", "");

                if (CoreSettings.requestSwitch("S_LILLY_EXTERNAL")) {
                    writeLoggerInfo("Erstelle Ghost Dokument...", tag);
                    Path go_p = Paths.get(Expandables.expand(expandables,
                            (b_data[B_NAME]
                                    + ((CoreSettings.requestSwitch("S_LILLY_SHOW_BOX_NAME")) ? boxmode + "-" : "")
                                    + CoreSettings.requestValue("S_FILE")).replaceAll("\"", "")));
                    try (PrintWriter go = new PrintWriter(go_p.toFile())) {
                        go.format("\\providecommand{\\LILLYxBOXxMODE}{%s}", boxmode);
                        go.format("\\providecommand{\\LILLYxPDFNAME}{%s}", final_name); // TODO: better expand
                        go.format("\\providecommand{\\LILLYxTHREADxID}{%d}", id);
                        go.format("%s %s %s", b_data[B_EXTRA].replaceAll("\\\\\\\\", "\\\\"),
                                Expandables.expand(expandables, " ${_LILLYARGS} ").replaceAll("\\\\\\\\", "\\\\"),
                                Expandables.expand(expandables, b_data[B_INPUT]).replaceAll("\\\\\\\\", "\\\\"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                // Start of compile

                int fb = 0;
                for (int i = 0; i < Integer.parseInt(CoreSettings.requestValue("S_LILLY_COMPILETIMES")); i++) {
                    // execute IN-hooks
                    synchronized (Expandables.finalName) {
                        Expandables.finalName = final_name + ".pdf";
                        Hooks.executeHooks(all_hooks, "IN" + i);
                    }
                    writeLoggerInfo("Kreiere Latex-Datei (führe pdflatex aus)...", tag);
                    StringBuilder compileCommand = new StringBuilder();
                    compileCommand.append("pdflatex -jobname ").append(final_name).append(" $(LATEXARGS) ")
                            .append(b_data[B_EXTRA]).append("${_LILLYARGS}")
                            .append("\\\\providecommand{\\\\LILLYxBOXxMODE}{").append(boxmode).append("}");
                    compileCommand.append("\\\\providecommand{\\\\LILLYxPDFNAME}{").append(final_name).append("}");
                    compileCommand.append("").append(b_data[B_INPUT]);

                    String cc = Expandables.expand(expandables, compileCommand.toString());
                    // System.out.println(cc);
                    JakeLogger.writeLoggerInfo(cc, "compile");
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", cc });
                        int ret = p.waitFor();
                        JakeLogger.writeLoggerDebug3("pdflatex returned: " + ret, "compile");
                        if (ret != 0) {
                            synchronized (JakeWriter.err) {
                                failed = true;
                                JakeWriter.err.format(
                                        "%sDas Kompilieren mit pdflatex und Jake ist in thread %d für %s%s fehlgeschlagen bitte sieh im entsprechenden Logfile nach!%s%n",
                                        ColorConstants.COL_ERROR, id, b_data[B_TEXT], ColorConstants.COL_ERROR,
                                        ColorConstants.COL_RESET);
                                // Fake analyse until implemented:
                                JakeWriter.out.println("Versuche die Probleme zu finden: ");
                                int errmax = Integer.parseInt(CoreSettings.requestValue("S_ERRORCOUNT"));
                                Exception e = null;
                                for (Charset s : new Charset[] { Charset.defaultCharset(),
                                        Charset.forName("ISO8859_15") })
                                    if ((e = analyse(final_name, errmax, s)) == null)
                                        break;
                                if (e != null) {
                                    e.printStackTrace();
                                    JakeWriter.err.format(
                                            "%sEs gab ein kleines Problem mit dem finden der Log-Datei, du musst wohl einen übermächtigen Magier finden, der dir hilft, den Drachen zu besiegen. Ich kann deine Prinzessin nicht retten :/%s%n",
                                            ColorConstants.COL_ERROR, ColorConstants.COL_RESET);
                                }
                                // Ein, das war eine gute Idee relikt:
                                /*
                                 * Stream<String> lines = Files.lines(Paths.get(final_name + ".log")) .filter(x
                                 * -> (x.startsWith("!") || x.toLowerCase().contains("improper alph") && (last =
                                 * true)) || (last && !(last = false))); lines.forEachOrdered(x ->
                                 * JakeWriter.out.format(" - %s%n", x));
                                 */
                                JakeWriter.err.format("%s > Kompilieren fehlgeschlagen %s%n", ColorConstants.COL_ERROR,
                                        ColorConstants.COL_RESET);
                                return;
                            }
                        }
                        // Path lilly_log_out = Paths.get(CoreSettings.requestValue("S_LILLY_OUT")
                        // ,"LILLY_COMPILE.log");
                        // PrintWriter llo = new PrintWriter(lilly_log_out.toFile());
                        // new BufferedReader( new
                        // InputStreamReader(p.getInputStream())).lines().forEachOrdered(llo::println);
                        // new BufferedReader( new
                        // InputStreamReader(p.getErrorStream())).lines().forEachOrdered(llo::println);
                        // llo.close();
                    } catch (Exception ex) {
                        failed = true;
                        ex.printStackTrace();
                        JakeWriter.err.format(
                                "%sDas Kompilieren mit pdflatex und Jake ist in thread %d für %s%s fehlgeschlagen bitte sieh im entsprechenden Logfile nach!%s%n",
                                ColorConstants.COL_ERROR, id, b_data[B_TEXT], ColorConstants.COL_ERROR,
                                ColorConstants.COL_RESET);
                        return;
                    }

                }
                JakeWriter.out.format("%sGenerierung von \"%s.pdf\" (%s) abgeschlossen. (Zeit: %ss)%s%n",
                        ColorConstants.COL_SUCCESS, final_name, boxmode,
                        (System.currentTimeMillis() - startTime) / 1000.0, ColorConstants.COL_RESET);
            }

        }

        /**
         * @brief test, ob es sich um einen Fehler handelt
         *
         * @param line Zeile die es zu testen gilt
         *
         * @returns true, im Falle eines Fehlers
         */
        public static boolean isErrorLine(String line) {
            return line.startsWith("!") || line.toLowerCase().contains("improper alph");
        }

        public static Exception analyse(String final_name, int errmax, Charset charset) {
            try {
                String[] lines = Files.lines(Paths.get(final_name + ".log"), charset).toArray(String[]::new);

                int errCount = 0;
                for (int k = 0; k < lines.length; k++) {
                    String cur = lines[k];

                    if (isErrorLine(cur)) {
                        JakeWriter.out.format("%n %3d. %s%n", ++errCount, cur);
                        // print all meta-lines
                        int meta_count = 0;
                        while (k < lines.length - 2 && meta_count++ < 10) {
                            k++;
                            cur = lines[k];
                            // Highlight Line-Numbers
                            Matcher ln = Pattern.compile("l\\.\\d+").matcher(cur);
                            if (ln.find()) {
                                String rp = ln.group();
                                cur = cur.replaceAll(rp, Matcher
                                        .quoteReplacement(ColorConstants.COL_GOLD + rp + ColorConstants.COL_RESET));
                            }
                            if (cur.isBlank() && lines[k + 1].isBlank() || lines[k - 1].contains("X <return>  to quit.")
                                    || lines[k + 1].startsWith("!")) { // zwei
                                                                       // leerzeilen
                                                                       // oder
                                                                       // letzte
                                                                       // war das
                                                                       // quit-Angebot
                                                                       // oder nächst
                                                                       // ist ein neuer
                                                                       // fehler
                                                                       // =>
                                                                       // abbruch
                                JakeWriter.out.println();
                                break;
                            }

                            JakeWriter.out.format("       %s%n", cur);
                        }
                    }
                    if (errCount >= errmax) {
                        JakeWriter.out.format(
                                "%n%s------------------------------------ Erste %d Fehler ------------------------------------%s%n", // %2$s
                                ColorConstants.COL_ERROR, errmax, ColorConstants.COL_RESET);
                        // Skip count other errors
                        for (; k < lines.length; k++) {
                            if (isErrorLine(lines[k]))
                                errCount++;
                        }
                        break;
                    }
                    // )) (/usr/share/texlive/texmf-dist/tex/latex/pgf/utilities/pgffor.sty
                    // (/usr/shar
                    // -------------------------------- Erste 5 Fehler
                    // --------------------------------
                }
                JakeWriter.out.format("%n%sInsgesamt wurden %d Fehler registriert.%s%n", // %2$s
                        ColorConstants.COL_CYAN, errCount, ColorConstants.COL_RESET);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        int id;
        String[] b_data;
        Settings expandables, all_hooks;
        String tag;

        public JakeCompile_Worker(int id, final String[] b_data, Settings expandables, Settings all_hooks) {
            this.id = id;
            this.b_data = b_data.clone();
            this.expandables = expandables.cloneSettings();
            this.all_hooks = all_hooks.cloneSettings();
            tag = "comp" + id;
            this.start();
        }

    }

    /**
     * pdflatex -jobname ./OUTPUT/DEFAULT-dummy -shell-escape -enable-write18
     * -interaction=batchmode
     * \providecommand\LILLYxMODE{default}\providecommand\LILLYxMODExEXTRA{FALSE}
     * \providecommand{\LILLYxDOCUMENTNAME}{dummy.tex}\providecommand{\LILLYxOUTPUTDIR}{./OUTPUT/}
     * \providecommand{\LILLYxPATH}{.}\providecommand{\AUTHOR}{}\providecommand{\AUTHORMAIL}{}
     * \providecommand{\LILLYxSemester}{}\providecommand{\LILLYxVorlesung}{}
     * \providecommand{\Hcolor}{}\providecommand{\lillyPathLayout}{\LILLYxPATHxDATA/Layouts}(\providecommand{\LILLYxEXTERNALIZE}{FALSE}\providecommand{\LILLYxBOXxMODE}{DEFAULT}\providecommand{\LILLYxPDFNAME}{./OUTPUT/DEFAULT-dummy}\input{./dummy.tex}
     */

}
