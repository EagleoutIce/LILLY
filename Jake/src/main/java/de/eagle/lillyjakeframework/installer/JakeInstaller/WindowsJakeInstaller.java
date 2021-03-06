package de.eagle.lillyjakeframework.installer.JakeInstaller;

/**
 * @file WindowsJakeInstaller.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Installiert Jake auf Windows.
 */

import de.eagle.lillyjakeframework.installer.AutoInstaller;

public class WindowsJakeInstaller extends AutoInstaller {

    public WindowsJakeInstaller(boolean gui) {
        super("Windows Jake Installer",gui);
        this.progress = 99; // TEMPORARY => CHANGE
    }

    /**
     * Führt den nächsten nötigen Schritt aus
     *
     * @return Anzeige, für den *nächsten* Schritt, null wenn beendet
     */
    @Override
    public String nextStep() {
        this.progress = 100; // TEMPORARY
        return null;
    }

    /**
     * Validiert, ob alles korrekt installiert wurde
     *
     * @return true, wenn ja, sonst false
     */
    @Override
    public boolean validate() {
        return false;
    }

    /**
     * Installiert das Programm automatisiert,
     *
     * @return Führt solange nextStep aus, bis die Rückgabe null ist
     */
    @Override
    public boolean automatedInstall() {
        return false;
    }



}
