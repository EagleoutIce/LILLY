package de.eagle.lillyjakeframework.installer.JakeInstaller;

import de.eagle.lillyjakeframework.installer.AutoInstaller;

public class MacOSJakeInstaller extends AutoInstaller {

    public MacOSJakeInstaller(boolean gui) {
        super("macOS Jake Installer",gui);
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
