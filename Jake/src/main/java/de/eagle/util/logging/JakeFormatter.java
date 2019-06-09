package de.eagle.util.logging;

/**
 * @file JakeFormatter.java
 * @author Florian Sihler
 * @version 1.0.10
 *
 * @brief Übernimmt die Formatierung des Logs
 * @see de.eagle.util.logging.JakeLogger
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JakeFormatter extends Formatter {
    private static final DateFormat df = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss.SSSS");

    /**
     * Paddet den String
     *
     * @implNote size sollte > 4 sein
     *
     * @param str der String
     * @param size > 4
     * @return paddet String
     */
    public static String pad(String str,int size){
        if(str.length() < size)
            return String.format("%-" + size + "s", str);
        else
            return String.format("%-" + size + "s...", str.substring(size-str.length()-3));
    }

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        //builder.append("[")/*.append(record.getSourceClassName()).append(".")*/;
        //builder.append(record.getSourceMethodName()).append("] ").append(" ".repeat(15-record.getSourceMethodName().length()));
        builder.append("[").append(pad(record.getLevel().toString(),15)).append("] ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}
