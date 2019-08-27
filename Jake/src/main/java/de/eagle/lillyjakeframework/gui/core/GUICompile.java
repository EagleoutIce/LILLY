package de.eagle.lillyjakeframework.gui.core;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.gui.core.Tools.ConfigEditor;
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

    String ConfPath = "";

    private boolean checkCompileable() {
        return cbFileCorrect.isSelected();
    }



    public String[][] receiveDocumentData(File file) {
        // First of all Detect if the File is a config or a tex file:
        ArrayList<String[]> ar = new ArrayList<>();
        ConfPath = "";
        if (file.getName().endsWith(".tex")) {
            ar.add(new String[]{"File-Name:", file.getName()});
            ar.add(new String[]{"File-Path:", file.getPath()});
            ar.add(new String[]{"File-Size:", file.length() / 1024.0 + "Kb"});
        } else if (file.getName().endsWith(".conf")) {
            lbStatus.setText("Retrieving Status from Config-File");
            ConfPath = file.getAbsolutePath();

            try {
                ar = Configurator.parseConfigFile(ConfPath);
            } catch (IOException e) {
                lbStatus.setText("Retrieving Status from Config-File failed");
            }
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
                    btCompile.setEnabled(checkCompileable());
                    // Set correct Input and Output-Base as the File can be from another directory :D
                    File f = new File(tfFMainFile.getText());
                    CoreSettings.set("lilly-in", f.getParentFile().getAbsolutePath());

                    // If this is correct we can try to Examine the File Data
                    String[][] data = receiveDocumentData(f);
                    DataTable.setModel(new DefaultTableModel(data, new String[]{"Key", "Value", "Expanded Value"}));

                    btEditConfig.setEnabled(true);
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
                ConfigEditor ce = new ConfigEditor(ConfPath);
                ce.setModal(true); ce.setVisible(true);
                // TODO: reread config file, further: disable Compile Button, when no File!
            }
        });

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
