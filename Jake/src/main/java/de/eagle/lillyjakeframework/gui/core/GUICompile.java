package de.eagle.lillyjakeframework.gui.core;

/**
 * @file GUICompile.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Übernimmt die Auswahl des zu kompilierenden Dokuments.
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.lillyjakeframework.gui.core.Tools.ConfigEditor;
import de.eagle.lillyjakeframework.gui.core.Tools.GepardEditor;
import de.eagle.util.datatypes.JakeDocument;
import de.eagle.util.enumerations.eDocument_Type;
import de.eagle.util.helper.Executer;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeWriter;
import de.eagle.util.io.TextAreaConsoleStream;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Florian Sihler
 * @version 1.0.0
 * @class GUICompile
 * @since 2.0.0
 * <p>
 * In dieser Klasse kann eine Datei kompiliert werden.
 * Sie bietet mehrere Optionen wobei alles mit dem Laden einer '.tex'-Datei beziehungsweise einer '.conf'-Datei
 * startet. Hier können dan noch weitere Konfigurationen angepasst werden, sowie das Configfile erstellt/modifiziert
 * werden.
 * Entsprechende Hooks werden (hoffentlich bald) angezeigt und aufbereitet werden.
 * <p>
 * Zur Nutzung von GUICompile bedarf erstmal keiner Installation. Erst durch die Integration einer Nutzergebundenen
 * Konfiguration wird eine Installation, zumindest über einen Ort für Konfigurationsdateien, notwendig.
 */
public class GUICompile extends JFrame {
    private JTextField tfFMainFile;
    private JButton btSelectFile;
    private JPanel MainPanel;
    private JLabel lbStatus;
    private JCheckBox cbFileCorrect;
    private JButton btCompile;
    private JTable DataTable;
    private JPanel ToolsGroup;
    private JButton btEditConfig;
    private JButton btEditGepard;
    private JTabbedPane main_tabp;
    private JPanel file_selection;
    private JPanel compile_log;
    private JEditorPane outlog;
    private JScrollPane outlogsp;

    JakeDocument loadedDoc = null;

    private boolean checkCompileable() {
        return cbFileCorrect.isSelected();
    }


    public final TextAreaConsoleStream OUT;

    public String[][] receiveDocumentData(JakeDocument doc) {
        // First of all Detect if the File is a config or a tex file:
        ArrayList<String[]> ar = new ArrayList<>();
        switch (doc.getType()) {
            case IS_TEX:
                for (var l : doc.getMetadata().entrySet()) {
                    ar.add(new String[]{l.getKey() + ":", l.getValue()});
                }
                break;
            case IS_CONF:
                lbStatus.setText("Retrieving Status from Config-File");

                try {
                    ar = Configurator.parseConfigFile(doc.getPath().getAbsolutePath());
                } catch (Exception e) {
                    lbStatus.setText("Retrieving Status from Config-File failed: " + e.getMessage());
                }
                break;
            default:
                break;
        }
        return ar.toArray(new String[][]{});
    }

    public void chooseFile() {
        JFileChooser jf = new JFileChooser(PropertiesProvider.getWorkingDir());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("LaTeX/TeX (*.tex) or Config File (*.conf)", "tex", "conf");
        jf.setFileFilter(filter);
        if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            tfFMainFile.setText(jf.getSelectedFile().getAbsolutePath());
    }

    public GUICompile() {
        setContentPane(MainPanel);
        setTitle("Compile");
        setSize(550, 360);
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


        //for (JCheckBox j : checked) {
        cbFileCorrect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    //btCompile.setEnabled(checkCompileable());
                    // Set correct Input and Output-Base as the File can be from another directory :D
                    JakeDocument jd = new JakeDocument(new File(tfFMainFile.getText()));
                    // We will try to assign the in and output file relative to the File location :D
                    //JakeWriter.out.println("Setting user directory: " + PropertiesProvider.setUserDirectoy(jd.getSHPath().getParentFile().getAbsolutePath()));
                    Definitions.setRelativeWorkingDir(jd.getPath().getParentFile().getAbsolutePath());
                    //CoreSettings.set(CoreSettings.getTranslator().translate("S_LILLY_IN"), jd.getSHPath().getParentFile().getAbsolutePath() + "/");

                    if (!new File(CoreSettings.requestValue("S_LILLY_IN")).isAbsolute())
                        CoreSettings.assignValue("S_LILLY_IN", String.valueOf(Paths.get(Definitions.getRelativeWorkingDir(), CoreSettings.requestValue("S_LILLY_IN")) + "/"));
                    if (!new File(CoreSettings.requestValue("S_LILLY_OUT")).isAbsolute())
                        CoreSettings.assignValue("S_LILLY_OUT", String.valueOf(Paths.get(Definitions.getRelativeWorkingDir(), CoreSettings.requestValue("S_LILLY_OUT")) + "/"));


                    // If this is correct we can try to Examine the File Data
                    String[][] data = receiveDocumentData(jd);
                    DataTable.setModel(new DefaultTableModel(data, new String[]{"Key", "Value", "Expanded Value"}));
                    loadedDoc = jd;
                    loadedDoc.addListener(new JakeDocument.iDocumentChangedListener() {
                        @Override
                        public void JakeDocument_Saved(String path) {
                            System.out.println("Called Saved!");
                            String[][] data = receiveDocumentData(jd);
                            DataTable.setModel(new DefaultTableModel(data, new String[]{"Key", "Value", "Expanded Value"}));
                            // Update when saved
                        }
                    });
                    if (checkCompileable() && loadedDoc.isValid()) {
                        switch (loadedDoc.getType()) {
                            case IS_CONF:
                                if (CoreSettings.requestValue("S_FILE").endsWith(".tex") && CoreSettings.requestValue("S_OPERATION").equals("file_compile")) {
                                    btCompile.setEnabled(true);
                                } else {
                                    btCompile.setEnabled(false);
                                    lbStatus.setText("<html>Information: Wrong Settings!<br>file: " + CoreSettings.requestValue("S_FILE") + " and operation: " + CoreSettings.requestValue("S_OPERATION") + "</html>");
                                }
                                break;
                            case IS_TEX:
                                btCompile.setEnabled(true);
                                break;
                            default:
                                btCompile.setEnabled(false);
                                lbStatus.setText("Information: The Document isn't valid (wrong doctype)!");
                        }
                    } else {
                        btCompile.setEnabled(false);
                        lbStatus.setText("Information: The Document isn't valid!");
                    }
                } else {
                    loadedDoc = null; // No document loaded :D
                    DataTable.setModel(new DefaultTableModel(new String[][]{}, new String[]{"Key", "Value", "Expanded Value"}));
                }
            }
        });
        //}

        tfFMainFile.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                cbFileCorrect.setSelected(new File(tfFMainFile.getText()).isFile());
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                cbFileCorrect.setSelected(new File(tfFMainFile.getText()).isFile());
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        btSelectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseFile();
            }
        });

        btEditConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ConfigEditor ce = new ConfigEditor((loadedDoc == null || !loadedDoc.getType().equals(eDocument_Type.IS_CONF)) ? new JakeDocument(eDocument_Type.IS_CONF, "Unnamed Config") : loadedDoc);
                ce.setModal(true);
                ce.setVisible(true);
                if (loadedDoc != null && loadedDoc.getType().equals(eDocument_Type.IS_CONF)) {
                    //loadedDoc.load();
                    String[][] data = receiveDocumentData(loadedDoc);
                    DataTable.setModel(new DefaultTableModel(data, new String[]{"Key", "Value", "Expanded Value"}));
                }
            }
        });
        btEditGepard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GepardEditor ce = new GepardEditor();
                ce.setModal(true);
                ce.setVisible(true);
            }
        });


        btCompile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (loadedDoc == null) return;
                btCompile.setEnabled(false);
                // We will operate similar to projects by spawning new little Jakes :smile:
                new Thread(new async_compile()).start();
            }
        });
        OUT = new TextAreaConsoleStream(outlog);
    }

    public class async_compile implements Runnable {
        @Override
        public void run() {
            BufferedReader br = compileFile();
            String _line;
            // we will expand the Path to allow Expandables to work on it :D
            try {
                while ((_line = br.readLine()) != null) {
                    OUT.println(_line);
                    // JakeWriter.out.println(_line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            btCompile.setEnabled(true);
        }
    }

    BufferedReader compileFile() {
        String m = loadedDoc.getPath().getAbsolutePath(); // Ignore pending whitespaces
        // System.out.println("Got Path: " + m);
        BufferedReader br = Executer
                .runBashCommand("cd \"" + new File(m).getParent() + "\" && jake \"" + m
                        + "\" || echo 'Error! The File [" + m + "] seems to be invalid'");
        return br;

    }

    public static void main(String[] args) {
        new GUICompile().setVisible(true);

    }

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
        MainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        MainPanel.setMaximumSize(new Dimension(-1, -1));
        MainPanel.setMinimumSize(new Dimension(162, 100));
        MainPanel.setOpaque(true);
        MainPanel.setPreferredSize(new Dimension(162, 100));
        main_tabp = new JTabbedPane();
        main_tabp.setEnabled(true);
        MainPanel.add(main_tabp, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        file_selection = new JPanel();
        file_selection.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        main_tabp.addTab("File Selection", file_selection);
        cbFileCorrect = new JCheckBox();
        cbFileCorrect.setEnabled(false);
        cbFileCorrect.setText("");
        file_selection.add(cbFileCorrect, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        file_selection.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DataTable = new JTable();
        DataTable.setAutoCreateRowSorter(true);
        DataTable.setAutoResizeMode(2);
        DataTable.setEnabled(false);
        DataTable.setFillsViewportHeight(true);
        DataTable.setShowHorizontalLines(false);
        DataTable.setShowVerticalLines(true);
        DataTable.setToolTipText("Show Data about the selected File");
        DataTable.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        DataTable.putClientProperty("Table.isFileList", Boolean.FALSE);
        scrollPane1.setViewportView(DataTable);
        ToolsGroup = new JPanel();
        ToolsGroup.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(3, 3, 3, 3), -1, -1));
        ToolsGroup.setToolTipText("Nice Tools");
        file_selection.add(ToolsGroup, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ToolsGroup.setBorder(BorderFactory.createTitledBorder("Tools"));
        btEditConfig = new JButton();
        btEditConfig.setEnabled(true);
        btEditConfig.setText("Config");
        ToolsGroup.add(btEditConfig, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btEditGepard = new JButton();
        btEditGepard.setText("Gepard");
        ToolsGroup.add(btEditGepard, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        ToolsGroup.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        tfFMainFile = new JTextField();
        tfFMainFile.setText("File");
        tfFMainFile.setToolTipText("The file that should be compiled");
        file_selection.add(tfFMainFile, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        btSelectFile = new JButton();
        btSelectFile.setAutoscrolls(true);
        btSelectFile.setHideActionText(false);
        btSelectFile.setText("Select File");
        btSelectFile.setToolTipText("Open a File Chooser to Pick the File you want to compile");
        file_selection.add(btSelectFile, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btCompile = new JButton();
        btCompile.setBackground(new Color(-15100068));
        btCompile.setEnabled(false);
        btCompile.setForeground(new Color(-197377));
        btCompile.setText("Compile");
        btCompile.setToolTipText("Press to Compile the Document :D");
        file_selection.add(btCompile, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbStatus = new JLabel();
        lbStatus.setText("Entering Data");
        file_selection.add(lbStatus, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compile_log = new JPanel();
        compile_log.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        compile_log.setEnabled(true);
        main_tabp.addTab("Compile Log", compile_log);
        outlogsp = new JScrollPane();
        compile_log.add(outlogsp, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        outlog = new JEditorPane();
        outlog.setContentType("text/html");
        outlog.setEditable(false);
        Font outlogFont = this.$$$getFont$$$("Hack", -1, -1, outlog.getFont());
        if (outlogFont != null) outlog.setFont(outlogFont);
        outlog.setText("<html>\n  <head>\n    \n  </head>\n  <body>\n  </body>\n</html>\n");
        outlogsp.setViewportView(outlog);
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

}
