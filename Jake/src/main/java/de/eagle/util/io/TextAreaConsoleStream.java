package de.eagle.util.io;

import de.eagle.util.constants.ColorConstants;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class TextAreaConsoleStream
 *
 * This is a simple Class that makes it possible to attach an OutputStream to a TextArea.
 * Used in Combination with {@link de.eagle.util.io.JakeWriter.MirrorStream#reassignConsole(AbstractConsoleWriter)}
 * to redirect the {@link JakeWriter}-Calls.
 *
 * @author Florian Sihler
 *
 * @since 2.0.0
 * @version 1.0.0
 */
public class TextAreaConsoleStream extends AbstractConsoleWriter {
    private JEditorPane target;

    String currentLine = "";

    private List<String> contents = new ArrayList<>();

    private TextAreaConsoleStream update() {
        flush();
        target.setText("<html> <head></head><body>" + String.join("<br>", contents) + "<br>" + currentLine + "</body></html>");
        flush();
        return this;
    }

    public TextAreaConsoleStream(JEditorPane out) {
        target = out;
        target.setContentType("text/html");
    }

    public static String format(String format) {
        format = "<font>" + format.replace(ColorConstants.COL_RESET,"</font><font>").replace(ColorConstants.COL_ERROR, "</font><font color=rgb(255,32,82)>")
                .replace(ColorConstants.COL_GOLD, "</font><font color=rgb(255,150,0)>")
                .replace(ColorConstants.STY_PARAM,"</font><font>")
                .replace(ColorConstants.COL_CYAN, "</font><font color=rgb(0,102,125)>")
                .replace(ColorConstants.COL_PURPLE, "</font><font color=rgb(203,47,122)>")
                .replace(ColorConstants.COL_GREEN, "</font><font color=rgb(102,184,67)>")
                .replace(ColorConstants.COL_SUCCESS, "</font><font color=rgb(102,184,67)>")+ "</font>";
        return format; // transforming the Data (change AnsiEscapeCodes to HTML)
    }

    @Override
    public AbstractConsoleWriter println() {
        contents.add(format(currentLine)); currentLine = ""; return update();
    }

    @Override
    public AbstractConsoleWriter println(String s) {
        contents.add(format(currentLine + s.replace("\\n","<br>")));
        currentLine = ""; return update();
    }

    @Override
    public AbstractConsoleWriter print(String s) {
        currentLine += format(s.replace("\\n","<br>"));
        return update();
    }

    @Override
    public AbstractConsoleWriter flush() {
        target.update(target.getGraphics());
        return this;
    }

    @Override
    public AbstractConsoleWriter append(String s) {
        return print(s);
    }

    @Override
    public AbstractConsoleWriter format(String format, Object... args) {
        return print(String.format(format,args));
    }
}
