package de.eagle.lillyjakeframework.gui.core.Tools;

/**
 * @file ConfigEditor.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Erlaubt es im GUI-Modus, Konfigurationsdokumente zu editieren.
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.datatypes.JakeDocument;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eDocument_Type;
import de.eagle.util.io.JakeWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ConfigEditor extends JDialog {
    private JPanel MainPanel;
    private JTable DataTable;
    private JButton btSave;
    private JLabel lbInfo;
    private JButton btCancel;
    private DefaultTableModel dtm = null;

    private static final int KEY_COL = 0;
    private static final int VAL_COL = 1;

    public boolean CheckRowBlank(int r) {
        for (int i = 0; i < 3; i++) {
            if (dtm.getValueAt(r, i) != null && !dtm.getValueAt(r, i).toString().isBlank())
                return false;
        }
        return true;
    }

    private JakeDocument document;

    Settings exps;

    public ConfigEditor(JakeDocument doc) {
        this.document = doc;
        init();
    }

    public ConfigEditor(String ConfigPath) {
        if (ConfigPath.isEmpty()) {
            document = new JakeDocument(eDocument_Type.IS_CONF, "unnamed document");
        } else {
            document = new JakeDocument(new File(ConfigPath));
        }
        init();
    }

    public void init() {
        setContentPane(MainPanel);
        setSize(550, 360);
        setTitle("Editing: " + document.getName());
        try {
            exps = Expandables.getInstance().expandsCS();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document.isNew()) {
            dtm = new DefaultTableModel(new String[]{"Key", "Value", "Expanded Value"}, 1) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == KEY_COL || (column == VAL_COL && !DataTable.getValueAt(row, KEY_COL).toString().isBlank());
                }
            };
            DataTable.setModel(dtm);
        } else {
            try {
                String[][] data = Configurator.parseConfigFile(document.getPath().getAbsolutePath()).toArray(new String[][]{});
                dtm = new DefaultTableModel(data, new String[]{"Key", "Value", "Expanded Value"}) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == KEY_COL || (column == VAL_COL && !DataTable.getValueAt(row, KEY_COL).toString().isBlank());
                    }
                };
                DataTable.setModel(dtm);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(MainPanel, e.getMessage());
            }
        }
        DataTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                int r = tableModelEvent.getFirstRow(), c = tableModelEvent.getColumn();
                int s = DataTable.getRowCount() - 1;
                if (r == s) {
                    if (!CheckRowBlank(r)) {
                        dtm.addRow(new String[]{"", "", ""});
                    }
                } else if (r < s && s != 1) { // Delete empty but not last
                    if (CheckRowBlank(r)) {
                        dtm.removeRow(r);
                    }
                }
                //setUpKeyColumn(DataTable, DataTable.getColumnModel().getColumn(0));
                if (c == 1) { // Value Row
                    try {
                        exps = Expandables.getInstance().expandsCS();
                        String val = DataTable.getValueAt(r, c).toString();
                        String eval = "";
                        try {
                            eval = Expandables.expand(exps, val);
                        } catch (Exception e) {
                            eval = "[Error] Compiling failed: " + e.getMessage();
                        }
                        if (!val.equals(eval))
                            DataTable.setValueAt(eval, r, c + 1);
                        else DataTable.setValueAt("", r, c + 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                updateInfo(r);
            }
        });
        //if (DataTable.getRowCount() == 0) {
        dtm.addRow(new String[]{"", "", ""}); // do it always :P
        //}
        setUpKeyColumn(DataTable, DataTable.getColumnModel().getColumn(KEY_COL));

        DataTable.getTableHeader().setReorderingAllowed(false);

        ListSelectionModel lsm = DataTable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lsm.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int r = DataTable.getSelectedRow();
                updateInfo(r);
            }
        });

        btSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Saving
                save();
            }
        });

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });
    }

    public boolean startsWithKey(ArrayList<String> ar, String k) {
        for (String s : ar) {
            if (s.trim().startsWith(k)) return true;
        }
        return false;
    }

    public void save() {
        ArrayList<String> linesToSave = new ArrayList<>();
        for (int r = 0; r < DataTable.getRowCount(); r++) {
            if (DataTable.getValueAt(r, KEY_COL) != null && DataTable.getValueAt(r, VAL_COL) != null) {
                String k = DataTable.getValueAt(r, KEY_COL).toString().trim(), v = DataTable.getValueAt(r, VAL_COL).toString().trim();
                if (!k.isBlank() && !v.isBlank()) {
                    // Do first Check: boolean
                    SettingDeskriptor sd = CoreSettings.getSettings().get(k);
                    String ev = "";
                    try {
                        ev = Expandables.expand(exps, v);
                        if (!sd.getType().isValid(ev)) {
                            JOptionPane.showMessageDialog(this,
                                    "The Value \"" + v + "\" (" + ev + ") of Key \"" + k + "\" doesn't hold up to: \"" + sd.getType().getBrief() + "\"", "Setting not Valid", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (RuntimeException ex) {
                        JakeWriter.err.println("Error, when trying to Expand expandable: " + ex.getMessage());
                    }

                    if (startsWithKey(linesToSave, k)) {
                        JOptionPane.showMessageDialog(this, "You've already assigned a Value to: " + k + " it will get overwritten!", "Doubled Value", JOptionPane.INFORMATION_MESSAGE);
                    }
                    linesToSave.add(String.format("%-15s = %s", k, v));
                }
            }
        }
        if (linesToSave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Lines to save. Fill in valid Information", "Nothing to Save!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            File saveFile;
            if (document.isNew()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Config File (*.conf)", "conf"));
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    saveFile = fileChooser.getSelectedFile();
                    if (!saveFile.getName().endsWith(".conf"))
                        saveFile = new File(saveFile.getAbsolutePath() + ".conf");
                    document.setFileContents(linesToSave);
                    document.setSaved(saveFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, "No target selected. ABORT!", "No save-target!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                saveFile = document.getPath();
            }
            setTitle("Editing: " + document.getPath().getName());

            try {
                PrintWriter pw = new PrintWriter(saveFile);
                pw.println("! ConfigFile created by Jake ConfigEditor !");
                for (String line : linesToSave)
                    pw.println(line);
                pw.close();
                JOptionPane.showMessageDialog(this, "Saved!", "Saved!", JOptionPane.INFORMATION_MESSAGE);

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Writing failed!", "Writing failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void updateInfo(int r) {
        if (r < 0) return;
        Object val = DataTable.getValueAt(r, KEY_COL);
        if (val == null || val.toString().isBlank()) {
            lbInfo.setText("As no Key provided, this Line will be ignored while saving!");
            return;
        }
        String s = val.toString();
        if (s != null && !s.isBlank()) {
            SettingDeskriptor sd = CoreSettings.getSettings().get(s);
            lbInfo.setText("<html><p><b>" + sd.getName() + ":</b> (" + sd.getType() + ")<br>" + sd.getBrief() + "<br><b>Current Value:</b><br>" + sd.getValue() + "</p></html>");
        } else {
            lbInfo.setText("Information");
        }
    }

    public boolean keyInTable(String key) {
        for (int r = 0; r < DataTable.getRowCount(); r++) {
            if (key.equals(DataTable.getValueAt(r, 0)))
                return true;
        }
        return false;
    }

    public void setUpKeyColumn(JTable table, TableColumn KeyCol) {
        ArrayList<String> values = new ArrayList<>();
        values.add("");
        for (var sdmap : CoreSettings.getSettings()) {
            String key = sdmap.getKey();
            //if (!keyInTable(key))
            values.add(key);
        }
        Collections.sort(values);
        JComboBox<String> jb = new JComboBox<>(values.toArray(new String[0]));
        KeyCol.setCellEditor(new DefaultCellEditor(jb));
        DefaultTableCellRenderer dtcrenderer = new DefaultTableCellRenderer();
        dtcrenderer.setToolTipText("Select Wanted Configuration");

    }

    public static void main(String[] args) {
        new ConfigEditor("").setVisible(true);
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
        MainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        btSave = new JButton();
        btSave.setBackground(new Color(-15100068));
        btSave.setForeground(new Color(-131585));
        btSave.setText("Save");
        MainPanel.add(btSave, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        MainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setOpaque(true);
        MainPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DataTable = new JTable();
        DataTable.setAutoCreateRowSorter(false);
        DataTable.setAutoResizeMode(2);
        DataTable.setRowHeight(25);
        scrollPane1.setViewportView(DataTable);
        btCancel = new JButton();
        btCancel.setBackground(new Color(-1113988));
        btCancel.setForeground(new Color(-197377));
        btCancel.setMargin(new Insets(3, 3, 3, 3));
        btCancel.setText("Cancel");
        MainPanel.add(btCancel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbInfo = new JLabel();
        lbInfo.setForeground(new Color(-10658463));
        lbInfo.setText("Information");
        MainPanel.add(lbInfo, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

}
