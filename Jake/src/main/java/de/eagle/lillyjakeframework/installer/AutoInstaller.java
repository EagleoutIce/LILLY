package de.eagle.lillyjakeframework.installer;

import java.util.Iterator;

import de.eagle.util.datatypes.FunctionCollector;

/**
 * Legt fest, was ein Installer alles können muss
 */
public abstract class AutoInstaller implements Iterable<String> {

    public FunctionCollector<String, String[]> steps;

    String name;

    public AutoInstaller(String name){
        this.name = name;
    }

    /**
     * @return Der Name des Installers
     */
    public String getName() {
        return name;
    }

    // aktueller Fortschritt/100
    protected int progress = 0;

    /**
     * @return Aktueller Fortschritt/100
     */
    public int getProgress(){
        return this.progress;
    }

    /**
     * Führt den nächsten nötigen Schritt aus
     * @return Anzeige, für den *nächsten* Schritt, null wenn beendet
     */
    public abstract String nextStep();

    public boolean finished() {
        return this.getProgress() >= 100; // ok in margin of error
    }

    /**
     * Validiert, ob alles korrekt installiert wurde
     * @return true, wenn ja, sonst false
     */
    public abstract boolean validate();

    /**
     * Installiert das Programm automatisiert,
     * @return Führt solange nextStep aus, bis die Rückgabe null ist
     */
    public abstract boolean automatedInstall();

    public boolean canUninstall() {
        return true;
    }

    @Override
    public Iterator<String> iterator(){
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return !finished();
            }

            @Override
            public String next() {
                String s = nextStep();
                if(s == null) return "Installation finished";
                else return s;
            }
        };
    }
}
