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
import de.eagle.lillyjakeframework.gui.core.SubForms.CompileWatcher;
import de.eagle.lillyjakeframework.gui.core.Tools.ConfigEditor;
import de.eagle.util.datatypes.JakeDocument;
import de.eagle.util.enumerations.eDocument_Type;
import de.eagle.util.helper.PropertiesProvider;

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

    JakeDocument loadedDoc = null;

    private boolean checkCompileable() {
        return cbFileCorrect.isSelected();
    }


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
                    //JakeWriter.out.println("Setting user directory: " + PropertiesProvider.setUserDirectoy(jd.getPath().getParentFile().getAbsolutePath()));
                    Definitions.setRelativeWorkingDir(jd.getPath().getParentFile().getAbsolutePath());
                    //CoreSettings.set(CoreSettings.getTranslator().translate("S_LILLY_IN"), jd.getPath().getParentFile().getAbsolutePath() + "/");

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

        btCompile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (loadedDoc == null) return;
                switch (loadedDoc.getType()) {
                    case IS_TEX:
                        CoreSettings.set(CoreSettings.getTranslator().translate("S_FILE"), loadedDoc.getPath().getName());
                        lbStatus.setText("Compiling...");
                        showCompileWatcher();
                        lbStatus.setText("Compiling finished successfully!");

                        break;
                    case IS_CONF:
                        // To be sure, wen wan't to rejoin the config-Settings with the CoreSettings
                        try {
                            Configurator cfg = new Configurator(loadedDoc.getPath().getAbsolutePath());
                            cfg.parse_settings(CoreSettings.getSettings(), false);
                            lbStatus.setText("Compiling...");
                            //JakeCompile.compile(new String[]{});
                            showCompileWatcher();
                        } catch (IOException e) {
                            lbStatus.setText(e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void showCompileWatcher() {
        CompileWatcher cw = new CompileWatcher(new String[]{});
        cw.setModal(true);
        cw.setVisible(true); // here we must evaluate the feedback  Value :D
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
        MainPanel.setLayout(new GridLayoutManager(4, 4, new Insets(3, 3, 3, 3), -1, -1));
        MainPanel.setMaximumSize(new Dimension(-1, -1));
        MainPanel.setMinimumSize(new Dimension(162, 100));
        MainPanel.setOpaque(true);
        MainPanel.setPreferredSize(new Dimension(162, 100));
        btSelectFile = new JButton();
        btSelectFile.setAutoscrolls(true);
        btSelectFile.setHideActionText(false);
        btSelectFile.setText("Select File");
        btSelectFile.setToolTipText("Open a File Chooser to Pick the File you want to compile");
        MainPanel.add(btSelectFile, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btCompile = new JButton();
        btCompile.setBackground(new Color(-15100068));
        btCompile.setEnabled(false);
        btCompile.setForeground(new Color(-197377));
        btCompile.setText("Compile");
        btCompile.setToolTipText("Press to Compile the Document :D");
        MainPanel.add(btCompile, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbFileCorrect = new JCheckBox();
        cbFileCorrect.setEnabled(false);
        cbFileCorrect.setText("");
        MainPanel.add(cbFileCorrect, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        MainPanel.add(scrollPane1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
        tfFMainFile = new JTextField();
        tfFMainFile.setText("File");
        tfFMainFile.setToolTipText("The file that should be compiled");
        MainPanel.add(tfFMainFile, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        lbStatus = new JLabel();
        lbStatus.setText("Entering Data");
        MainPanel.add(lbStatus, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ToolsGroup = new JPanel();
        ToolsGroup.setLayout(new GridLayoutManager(1, 2, new Insets(3, 3, 3, 3), -1, -1));
        ToolsGroup.setToolTipText("Nice Tools");
        MainPanel.add(ToolsGroup, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ToolsGroup.setBorder(BorderFactory.createTitledBorder("Tools"));
        btEditConfig = new JButton();
        btEditConfig.setEnabled(true);
        btEditConfig.setText("Config");
        ToolsGroup.add(btEditConfig, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        ToolsGroup.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

}
