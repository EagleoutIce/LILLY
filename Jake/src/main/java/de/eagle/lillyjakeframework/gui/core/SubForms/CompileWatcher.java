package de.eagle.lillyjakeframework.gui.core.SubForms;

/**
 * @file CompileWatcher.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Überwacht den Kompiliervorgang im GUI-Modus
 */


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.eagle.lillyjakeframework.compiler.JakeCompile;
import de.eagle.util.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompileWatcher extends JDialog {
    private JEditorPane taOutput;
    private JPanel MainPanel;
    private JTabbedPane tPane;
    private JPanel FirstPanel;
    private JTextArea taLogFile;
    private JScrollPane LogScrollPane;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        MainPanel = new JPanel();
        MainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tPane = new JTabbedPane();
        MainPanel.add(tPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        FirstPanel = new JPanel();
        FirstPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tPane.addTab("Console Output (buffered)", FirstPanel);
        taOutput = new JEditorPane();
        taOutput.setContentType("text/html");
        taOutput.setEditable(false);
        taOutput.setEnabled(true);
        Font taOutputFont = this.$$$getFont$$$(null, -1, -1, taOutput.getFont());
        if (taOutputFont != null) taOutput.setFont(taOutputFont);
        taOutput.putClientProperty("charset", "");
        FirstPanel.add(taOutput, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tPane.addTab("Log", panel1);
        LogScrollPane = new JScrollPane();
        LogScrollPane.setEnabled(false);
        LogScrollPane.setHorizontalScrollBarPolicy(32);
        LogScrollPane.setVerticalScrollBarPolicy(22);
        panel1.add(LogScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        taLogFile = new JTextArea();
        taLogFile.setEditable(false);
        taLogFile.setEnabled(false);
        taLogFile.setText("Inhalt des Logfiles");
        taLogFile.setWrapStyleWord(false);
        LogScrollPane.setViewportView(taLogFile);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

    public static class AsyncLaunchCompile extends Thread {
        String[] args = null;

        public AsyncLaunchCompile(String[] arsg) {
            this.args = args;
        }

        @Override
        public void run() {
            try {
                JakeCompile.compile(args);
            } catch (IOException e) {
                JakeWriter.err.print(e.getMessage());
            }
        }
    }


    public CompileWatcher(String[] args) {
        setContentPane(MainPanel);
        setSize(500, 400);
        setTitle("Compiling...");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e2) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                } catch (Exception e3) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        //AbstractConsoleWriter out = new TextAreaConsoleStream(taOutput);
        //JakeWriter.out.reassignConsole(out);
        //JakeWriter.err.reassignConsole(out);

        new AsyncLaunchCompile(args).start(); // Running Asynchronous for now as testing purpose this needs to change to a Future call :D

        JakeWriter.out.println("<font color=#ff2010><b>Log File:</b></font>");

        Runnable updateLog = new Runnable() {
            @Override
            public void run() {
                try {
                    taLogFile.setText(Files.lines(Paths.get(JakeLogger.getTarget())).collect(Collectors.joining("\n")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(updateLog, 0, 6, TimeUnit.SECONDS);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
    }

    public void onCancel() {
        JakeWriter.out.reassignConsole(new ConsolePrintStream(System.out));
        JakeWriter.err.reassignConsole(new ConsolePrintStream(System.err));
    }

    public static void main(String[] args) {
        new CompileWatcher(args).setVisible(true);
    }

}
