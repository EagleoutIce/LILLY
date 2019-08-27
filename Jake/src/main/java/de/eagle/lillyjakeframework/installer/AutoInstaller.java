package de.eagle.lillyjakeframework.installer;

import de.eagle.gepard.modules.Expandables;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.util.datatypes.FunctionCollector;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * Legt fest, was ein Installer alles können muss
 */
public abstract class AutoInstaller implements Iterable<String> {

    public FunctionCollector<String, String[]> steps;

    // nächstes element
    protected String next;

    String name;
   public static boolean doGui;

   public static Settings exp = null;

    public AutoInstaller(String name, boolean gui){
        this.name = name;
        this.doGui = gui;
        try {
            if(exp == null)
            exp = Expandables.getInstance().getExpandables(CoreSettings.requestValue("S_GEPARDRULES_PATH"));
        } catch (IOException ignored) {}
    }
    public static final String HOME = PropertiesProvider.getHomeDirectory();

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
    public String nextStep() {
        String[] ret = steps.get(next).function.apply("");
        progress += 100.0/steps.size();
        next = ret[0];
        if(ret[1] == null) progress = 100; // float kompensation
        return ret[1];
    }

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
    public boolean automatedInstall() {
        for(String s: this){}
        return true;
    }

    public boolean uninstall(){
        if(canUninstall()) {
            throw new UnsupportedOperationException("Auf diesem Betriebssystem kann man wohl noch nicht deinastallieren");
        } else {
            JakeWriter.err.println("ERROR: Anscheinend kann man Jake nicht deinstallieren, bitte erstelle ein Issue: https://github.com/EagleoutIce/LILLY/issues/new/choose");
            return false;
        }
    }

    public static final String[] END = new String[] {"END", null};


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
