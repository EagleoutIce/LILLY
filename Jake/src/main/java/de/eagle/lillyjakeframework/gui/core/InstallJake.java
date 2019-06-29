package de.eagle.lillyjakeframework.gui.core;

import de.eagle.lillyjakeframework.core.CoreFunctions;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.lillyjakeframework.installer.AutoInstaller;
import de.eagle.lillyjakeframework.installer.LinuxInstaller;
import de.eagle.lillyjakeframework.installer.MacOSInstaller;
import de.eagle.lillyjakeframework.installer.WindowsInstaller;
import de.eagle.util.helper.PropertiesProvider;

import javax.swing.*;
import java.awt.event.*;

public class InstallJake extends JDialog {
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
        } catch (Exception e2){
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                } catch (Exception e3){
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception ignored){
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


        switch (PropertiesProvider.getOS()){
            case LINUX:
            case MAC:
                briefDescription.setText("<html>WIP (NOT WORKING UNTIL INSTALLER WORKS):<BR> Es scheint, als dass du Jake mithilfe der .JAR-Datei gestartet hast. <BR>Es ist möglich, Jake zu installieren, drücke hierfür den entsprechenden Button (\"Install\"). <BR>Dies installiert zusätzlich die altbekannte Kommandozeilen Unterstützung durch 'lilly_jake'</html>");
                break;
            case WINDOWS:
                briefDescription.setText("<html>WIP (NOT WORKING UNTIL INSTALLER WORKS):<BR> Es scheint, als dass du Jake mithilfe der .JAR-Datei gestartet hast. <BR>Es ist möglich, Jake zu installieren, drücke hierfür den entsprechenden Button (\"Install\")</html>");break;
        }

        JakeVersion.setText(Definitions.PRG_BRIEF);

    }

    private void onOK() {
        installationProgress.setVisible(true);
        installationProgress.setValue(1);
        installationText.setText("Identifziere Betriebssystem...");
        AutoInstaller installer;
        switch (PropertiesProvider.getOS()){
            case LINUX:
                installationText.setText("Identifziere Betriebssystem [LINUX]");
                installer = new LinuxInstaller();

                break;
            case MAC:
                installationText.setText("Identifziere Betriebssystem [MACOS]");
                installer = new MacOSInstaller();break;

            case WINDOWS:
                installationText.setText("Identifziere Betriebssystem [WINDOWS]");
                installer = new WindowsInstaller();break;
            default:
                installationText.setText("Betriebssystem wurde nicht erkannt!");
                JOptionPane.showMessageDialog(new JFrame(), "Dein Betriebssystem wird nicht unterstützt!", "ERROR", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
        }
        for(String s : installer){
            installationText.setText(s);
            installationProgress.setValue(installer.getProgress());
        }
        //dispose();
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
}
