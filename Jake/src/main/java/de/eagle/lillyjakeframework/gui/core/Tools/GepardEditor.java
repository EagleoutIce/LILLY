package de.eagle.lillyjakeframework.gui.core.Tools;

import com.sun.tools.javac.Main;
import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.util.datatypes.JakeDocument;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eDocument_Type;
import de.eagle.util.helper.PropertiesProvider;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GepardEditor extends JDialog {
    private JPanel MainPanel;
    private JTabbedPane tabbedPains;
    private JTree DocumentTree;
    private JTextField documentPath;
    private JButton btSelectFile;
    private JCheckBox cbFileCorrect;
    private JTextArea _filler;
    private JPanel general;
    private JScrollPane spane;
    private JPanel hooks;
    private JPanel generators;
    private JPanel buildrules;
    private JPanel expandables;
    private JPanel nmaps;
    private JPanel project;
    private JPanel treePanel;

    JakeDocument document;

    private final String[] supportedModules = new String[]{
            "hook", "generator", "buildrule", "expandable", "nmaps", "project"
    };

    public GepardEditor(JakeDocument doc) {
        this.document = doc;
        $$$setupUI$$$();
        init();
    }

    public GepardEditor() {
        this.document = new JakeDocument(eDocument_Type.IS_GEPARD, "unbenannt");
        $$$setupUI$$$();
        init();
    }

    public void chooseFile() {
        JFileChooser jf = new JFileChooser(PropertiesProvider.getWorkingDir());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Gepard-File (*.gpd)", "gpd");
        jf.setFileFilter(filter);
        if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            documentPath.setText(jf.getSelectedFile().getAbsolutePath());
    }

    public GepardEditor(String ConfigPath) {
        $$$setupUI$$$();
        if (ConfigPath.isEmpty()) {
            document = new JakeDocument(eDocument_Type.IS_GEPARD, "unnamed document");
        } else {
            document = new JakeDocument(new File(ConfigPath));
        }
        init();
    }

    public boolean enableTab(String str) {
        for (int i = 0; i < supportedModules.length; i++) {
            if (supportedModules[i].equals(str)) {
                tabbedPains.setEnabledAt(i + 1, true);
                return true;
            }
        }
        return false;
    }

    public void createTree(GeneratorParser.JObject[] objs) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) dtm.getRoot();
        root.removeAllChildren(); // Reset
        for (GeneratorParser.JObject jObject : objs) {
            DefaultMutableTreeNode mother;
            if (!enableTab(jObject.getName())) {
                JOptionPane.showMessageDialog(MainPanel, "Die Datei enthält ein Modul: " + jObject.getName() + " welches nicht existiert/unterstützt wird!", "Error", JOptionPane.ERROR_MESSAGE);
                mother = new DefaultMutableTreeNode("[NOT SUPPORTED] " + jObject.getName());
            } else {
                mother = new DefaultMutableTreeNode(jObject.getName());
            }

            dtm.insertNodeInto(mother, root, root.getChildCount());
            // Now add the elements:
            for (var s : jObject.config) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(s.getKey());
                dtm.insertNodeInto(child, mother, mother.getChildCount());
                // dtm.insertNodeInto(new DefaultMutableTreeNode("Brief: " + s.getValue().getBrief()), child, child.getChildCount());
                dtm.insertNodeInto(new DefaultMutableTreeNode("Type: " + s.getValue().getType()), child, child.getChildCount());
                dtm.insertNodeInto(new DefaultMutableTreeNode("Value: " + s.getValue().getValue()), child, child.getChildCount());
            }
        }
        // System.out.println(dtm.getChildCount(root));
        dtm.reload();
        DocumentTree.expandPath(DocumentTree.getSelectionPath());
    }

    private DefaultTreeModel dtm;

    public void clearTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Dokument");
        dtm.setRoot(root);
        dtm.reload(root);
    }

    public void init() {
        setContentPane(MainPanel);
        setSize(550, 360);
        setTitle("Editing: " + document.getName());
        dtm = ((DefaultTreeModel) DocumentTree.getModel());
        clearTree();

        for (int i = 1; i < tabbedPains.getTabCount(); i++) {
            tabbedPains.setEnabledAt(i, false);
        }

        btSelectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseFile();
            }
        });

        documentPath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                cbFileCorrect.setSelected(new File(documentPath.getText()).isFile());
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                cbFileCorrect.setSelected(new File(documentPath.getText()).isFile());
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });

        cbFileCorrect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    // Alle Boxen extrahieren
                    GeneratorParser gp = new GeneratorParser(documentPath.getText());
                    try {
                        createTree(gp.parseFile("", new Settings("jobj collect"), true));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static void main(String[] args) {
        new GepardEditor("").setVisible(true);
    }

    private void createUIComponents() {
        clearTree();
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
        MainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains = new JTabbedPane();
        tabbedPains.setEnabled(true);
        MainPanel.add(tabbedPains, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        general = new JPanel();
        general.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains.addTab("Genrelles", general);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        general.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        documentPath = new JTextField();
        general.add(documentPath, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btSelectFile = new JButton();
        btSelectFile.setText("Select File");
        general.add(btSelectFile, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        _filler = new JTextArea();
        _filler.setAutoscrolls(true);
        _filler.setEditable(false);
        _filler.setInheritsPopupMenu(false);
        general.add(_filler, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        cbFileCorrect = new JCheckBox();
        cbFileCorrect.setEnabled(false);
        cbFileCorrect.setText("");
        general.add(cbFileCorrect, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHEAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spane = new JScrollPane();
        general.add(spane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DocumentTree = new JTree();
        DocumentTree.setEditable(false);
        spane.setViewportView(DocumentTree);
        hooks = new JPanel();
        hooks.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        hooks.setEnabled(false);
        tabbedPains.addTab("Hooks", hooks);
        generators = new JPanel();
        generators.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains.addTab("Generators", generators);
        buildrules = new JPanel();
        buildrules.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains.addTab("Buildrules", buildrules);
        expandables = new JPanel();
        expandables.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains.addTab("Expandables", expandables);
        nmaps = new JPanel();
        nmaps.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains.addTab("Name Maps", nmaps);
        project = new JPanel();
        project.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPains.addTab("Projects", project);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

}
