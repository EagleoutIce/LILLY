package de.eagle.lillyjakeframework.gui.core.Tools;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;

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
    private DefaultTableModel dtm = null;

    public boolean CheckRowBlank(int r) {
        for (int i = 0; i < 3; i++) {
            if (dtm.getValueAt(r, i) != null && !dtm.getValueAt(r, i).toString().isBlank())
                return false;
        }
        return true;
    }

    private String ConfigPath = "";
    Settings exps;

    public ConfigEditor(String ConfigPath) {
        setContentPane(MainPanel);
        setSize(550, 360);
        this.ConfigPath = ConfigPath;
        if (ConfigPath.isEmpty()) {
            setTitle("Editing: Unnamed Configfile");
            dtm = new DefaultTableModel(new String[]{"Key", "Value", "Expanded Value"}, 1) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0 || (column == 1 && !DataTable.getValueAt(row, 0).toString().isBlank());
                }
            };
            DataTable.setModel(dtm);
        } else {
            setTitle("Editing: " + ConfigPath);
            try {
                String[][] data = Configurator.parseConfigFile(ConfigPath).toArray(new String[][]{});
                dtm = new DefaultTableModel(data, new String[]{"Key", "Value", "Expanded Value"}) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 0 || (column == 1 && !DataTable.getValueAt(row, 0).toString().isBlank());
                    }
                };
                DataTable.setModel(dtm);
                ;
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
                        } catch(Exception e){eval = "[Error] Compiling failed: " + e.getMessage();}
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
        setUpKeyColumn(DataTable, DataTable.getColumnModel().getColumn(0));

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
            if (DataTable.getValueAt(r, 0) != null && DataTable.getValueAt(r, 1) != null) {
                String k = DataTable.getValueAt(r, 0).toString().trim(), v = DataTable.getValueAt(r, 1).toString().trim();
                if (!k.isBlank() && !v.isBlank()) {
                    // Do first Check: boolean
                    if (CoreSettings.getSettings().get(k).getType().equals(eSetting_Type.IS_SWITCH)) {
                        String ev = Expandables.expand(exps, v);
                        if (!(ev.equals(Definitions.S_TRUE) || ev.equals(Definitions.S_FALSE))) {
                            JOptionPane.showMessageDialog(this, "A Switch (like: " + k + ") must get one of the values: " + Definitions.S_TRUE + ", " + Definitions.S_FALSE + ". Not: " + v, "(Semantic) wrong Value", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
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
            if (ConfigPath.isEmpty()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Config File", "conf"));
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    saveFile = fileChooser.getSelectedFile();
                    if (!saveFile.getName().endsWith(".conf"))
                        saveFile = new File(saveFile.getAbsolutePath() + ".conf");
                    ConfigPath = saveFile.getAbsolutePath();
                } else {
                    JOptionPane.showMessageDialog(this, "No target selected. ABORT!", "No save-target!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                saveFile = new File(ConfigPath);
            }
            setTitle("Editing: " + ConfigPath);

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
        if (DataTable.getValueAt(r, 0) == null || DataTable.getValueAt(r, 0).toString().isBlank()) {
            lbInfo.setText("As no Key provided, this Line will be ignored while saving!");
            return;
        }
        String s = DataTable.getValueAt(r, 0).toString();
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
        MainPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        btSave = new JButton();
        btSave.setBackground(new Color(-15100068));
        btSave.setForeground(new Color(-131585));
        btSave.setText("Save");
        MainPanel.add(btSave, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        MainPanel.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        MainPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DataTable = new JTable();
        DataTable.setAutoCreateRowSorter(false);
        DataTable.setAutoResizeMode(2);
        scrollPane1.setViewportView(DataTable);
        lbInfo = new JLabel();
        lbInfo.setForeground(new Color(-10658463));
        lbInfo.setText("Information");
        MainPanel.add(lbInfo, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

}
