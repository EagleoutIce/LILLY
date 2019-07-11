package de.eagle.lillyjakeframework.gui.core;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.lillyjakeframework.installer.LinuxInstaller;
import de.eagle.lillyjakeframework.installer.MacOSInstaller;
import de.eagle.lillyjakeframework.installer.WindowsInstaller;
import de.eagle.util.helper.PropertiesProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InstallJake extends JDialog {
    private static final long serialVersionUID = -3168972755951250609L;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel briefDescription;
    private JProgressBar installationProgress;
    private JLabel JakeVersion;
    private JLabel installationText;

    public InstallJake() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        //  installationProgress.setVisible(false);
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
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        switch (PropertiesProvider.getOS()) {
            case LINUX:
            case MAC:
                briefDescription.setText("<html>WIP (NOT WORKING UNTIL INSTALLER WORKS):<BR> Es scheint, als dass du Jake mithilfe der .JAR-Datei gestartet hast. <BR>Es ist möglich, Jake zu installieren, drücke hierfür den entsprechenden Button (\"Install\"). <BR>Dies installiert zusätzlich die altbekannte Kommandozeilen Unterstützung durch 'lilly_jake'</html>");
                break;
            case WINDOWS:
                briefDescription.setText("<html>WIP (NOT WORKING UNTIL INSTALLER WORKS):<BR> Es scheint, als dass du Jake mithilfe der .JAR-Datei gestartet hast. <BR>Es ist möglich, Jake zu installieren, drücke hierfür den entsprechenden Button (\"Install\")</html>");
                break;
        }

        JakeVersion.setText(Definitions.PRG_BRIEF);

    }

    private void onOK() {
        installationProgress.setVisible(true);
        installationProgress.setValue(1);
        installationText.setText("Identifziere Betriebssystem...");
        AutoInstaller installer;
        switch (PropertiesProvider.getOS()) {
            case LINUX:
                installationText.setText("Identifziere Betriebssystem [LINUX]");
                installer = new LinuxInstaller(true);

                break;
            case MAC:
                installationText.setText("Identifziere Betriebssystem [MACOS]");
                installer = new MacOSInstaller(true);
                break;

            case WINDOWS:
                installationText.setText("Identifziere Betriebssystem [WINDOWS]");
                installer = new WindowsInstaller(true);
                break;
            default:
                installationText.setText("Betriebssystem wurde nicht erkannt!");
                JOptionPane.showMessageDialog(new JFrame(), "Dein Betriebssystem wird nicht unterstützt!", "ERROR", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
        }
        for (String s : installer) {
            installationText.setText(s);
            installationProgress.setValue(installer.getProgress());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        InstallJake dialog = new InstallJake();
        dialog.pack();
        dialog.setVisible(true);
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 3, 10), -1, -1));
        Font contentPaneFont = this.$$$getFont$$$(null, -1, 8, contentPane.getFont());
        if (contentPaneFont != null) contentPane.setFont(contentPaneFont);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Install");
        buttonOK.setToolTipText("Installiere Jake");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        buttonCancel.setToolTipText("Breche die Installation ab");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        installationProgress = new JProgressBar();
        installationProgress.setEnabled(true);
        installationProgress.setForeground(new Color(-6557919));
        installationProgress.setIndeterminate(false);
        installationProgress.setToolTipText("Fortschritt der Installation");
        panel1.add(installationProgress, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder("Jake-Installieren"));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        briefDescription = new JLabel();
        briefDescription.setText("");
        panel3.add(briefDescription, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JakeVersion = new JLabel();
        Font JakeVersionFont = this.$$$getFont$$$(null, -1, 8, JakeVersion.getFont());
        if (JakeVersionFont != null) JakeVersion.setFont(JakeVersionFont);
        JakeVersion.setText("0");
        contentPane.add(JakeVersion, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        installationText = new JLabel();
        Font installationTextFont = this.$$$getFont$$$(null, -1, 8, installationText.getFont());
        if (installationTextFont != null) installationText.setFont(installationTextFont);
        installationText.setHorizontalAlignment(10);
        installationText.setText("");
        contentPane.add(installationText, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return contentPane;
    }

}
