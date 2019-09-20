package de.eagle.util.datatypes;


/**
 * @file jakeDocument.java
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @since 2.0.0
 *
 * @brief Repräsentiert ein von Jake handhabbares Dokument.
 */

import de.eagle.gepard.modules.Expandables;
import de.eagle.gepard.parser.Configurator;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.enumerations.eDocument_Type;
import de.eagle.util.helper.CommandLine;
import de.eagle.util.helper.PropertiesProvider;
import de.eagle.util.io.JakeLogger;
import de.eagle.util.io.JakeWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @class JakeDocument
 *
 * Diese Klasse repräsentiert ein von Jake verarbeitbares Dokument. Hierbei kann es sich sowohl um ein
 * Config, ein Gepard oder ein TeX-Dokument handeln.
 *
 *
 * @author Florian Sihler
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class JakeDocument {


    public interface iDocumentChangedListener {
        default void JakeDocument_Saved(String path) {};
        default void JakeDocument_Loaded() {};
        default void JakeDocument_Changed(ArrayList<String> lines) {};
        default void JakeDocument_ChangedMeta(String Key, String Value) {};
    }

    public List<iDocumentChangedListener> listeners = new LinkedList<>();

    public void addListener(iDocumentChangedListener listener) {
        listeners.add(listener);
    }

    // ======================================================================
    /**
     * Checks a TeX-Document for validity
     *
     * @param doc the Document
     * @return true if its valid, false otherwise
     */
    public static boolean checkTeX(JakeDocument doc) {
        return true;
    }

    /**
     * Checks a Conf-Document for validity
     *
     * @param doc the Document
     * @return true if its valid, false otherwise
     */
    public static boolean checkConf(JakeDocument doc) {
        if(doc.getPath() == null) return false;
        if(doc.isNew()) return false;
        // Check if the Configurator accepts the config-file using the default Settings
        Settings CoreSettingsClone = CoreSettings.getSettings().cloneSettings();
        try {
            Configurator configurator = new Configurator(doc.getPath().getAbsolutePath());
            configurator.parse_settings(CoreSettingsClone, true);
            if(CoreSettingsClone.size() != CoreSettings.getSettings().size()){ // unknown Configs
                JakeWriter.err.println("Document not Valid as the used Configuration contained unknown Configurations!"); // maybe not make this fatal?
                return false;
            }
            boolean valid = true;
            Settings exps = Expandables.getInstance().expandsCS();
            for(var sdmap : CoreSettingsClone) {
                if(!sdmap.getValue().getType().isValid(Expandables.expand(exps, sdmap.getValue().getValue()))){
                    JakeWriter.err.println("Document not valid as Configuration: " + sdmap.getValue().getName() + " with setting: " + sdmap.getValue().getValue() + " doesn't hold up to the condition: " + sdmap.getValue().getType().getBrief());
                    valid =  false; // Semantic Check Failed
                }
            }
            return valid;
        } catch (IOException e) {
            JakeWriter.err.println("Document not Valid, as: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks a Gepard-Document for validity
     *
     * @param doc the Document
     * @return true if its valid, false otherwise
     */
    public static boolean checkGPD(JakeDocument doc) {
        return true;
    }

    /**
     * Checks an unknown Documenttype for validity
     *
     * @param doc the Document
     * @return true if its valid, false otherwise
     */
    public static boolean checkUnknown(JakeDocument doc) {
        return true;
    }
    // ======================================================================

    /**
     * Konstruiert einen Dokumentkopf für ein neues Dokument
     *
     * @param dType Der Dokumenttyp
     * @param name Der Name des Dokuments
     */
    public JakeDocument(eDocument_Type dType, String name) {
        this.name = name; this.docType = dType;
        this.path = new File("");
        isNew = true;
    }

    /**
     * Konstruiert den Dokumentkopf für ein bestehendes Dokument
     *
     * @param dType Der Dokumenttyp
     * @param name Der Name des Dokuments
     * @param path Der Pfad zum Dokument
     */
    public JakeDocument(eDocument_Type dType,String name, File path) {
        this.name = name; this.docType = dType;
        this.path = path;
        isNew = false; load();
    }

    public static eDocument_Type identifyType(File path){
        for (eDocument_Type t : eDocument_Type.values()){
            if (path.getName().endsWith(t.getSuffix()))
                    return t;
        }
        return eDocument_Type.IS_UNKNOWN;
    }

    /**
     * Konstruiert den Dokumentkopf für ein bestehendes Dokument
     *
     * @param name Der Name des Dokuments
     * @param path Der Pfad zum Dokument
     */
    public JakeDocument(String name, File path) {
        this.name = name; this.docType = identifyType(path);
        this.path = path;
        isNew = false; load();
    }

    /**
     * Konstruiert den Dokumentkopf für ein bestehendes Dokument
     *
     * @param path Der Pfad zum Dokument
     */
    public JakeDocument(File path) {
        this.name = path.getName();
        this.path = path; this.docType = identifyType(path);
        isNew = false; load();
    }

    protected ArrayList<String> fileLines = new ArrayList<>();

    /**
     * @return the contents of the File
     */
    public ArrayList<String> getFileContents(){
        return fileLines;
    }

    /**
     * sets contents of the File
     * @param data the new Contens of the File
     */
    public void setFileContents(ArrayList<String> data){
        fileLines = data;
        setChanged();
    }

    /**
     * Dokumenttyp
     */
    protected eDocument_Type docType;

    /**
     * Ist das Dokument neu?
     */
    protected boolean isNew;

    /**
     * Name of the Document
     */
    protected String name;

    /**
     * Path to the Document
     */
    protected File path;

    /**
     * @return The Name of the Document. This mustn't correspond to the actual name saved as this will be resolved from the path
     */
    public String getName(){
        return this.name;
    }

    /**
     * @param newName Changes the Documenttname
     */
    public void setName(String newName){
        this.name = newName;
        for(var l : listeners) l.JakeDocument_ChangedMeta("name", this.name);
    }

    /**
     * @param newPath Changes the path to Document
     */
    public void setPath(File newPath) {
        this.path = newPath;
        for (var l : listeners) l.JakeDocument_ChangedMeta("path", this.path.getAbsolutePath());
    }

    /**
     * @return The Path to the Document
     */
    public File getPath() {
        return path;
    }

    /**
     * @return true, if the document is new
     */
    public boolean isNew(){
        return isNew;
    }

    /**
     * @return Liefert den Typ des Dokuments
     */
    public eDocument_Type getType() {
        return docType;
    }

    /**
     * Gibt an ob das Dokument valide ist
     *
     * @return true, wenn ja, sonst false.
     */
    public boolean isValid() {
        return this.getType().checkValid.apply(this);
    }

    /**
     * Zeigt eine Änderung des Inhalts an.
     */
    public void setChanged() {
        for (var l : listeners) l.JakeDocument_Changed(getFileContents());
    }

    /**
     * Fragt den Nutzer nach einem Pfad zum Speichern
     *
     * @param assuredSuffix garantierte Endung
     *
     * @return null wenn abgebrochen, sonst der entsprechende (absolute) Pfad
     */
    public static String getSavePath(String assuredSuffix) {
        return getSavePath(PropertiesProvider.getWorkingDir(),assuredSuffix);
    }

    /**
     * Fragt den Nutzer nach einem Pfad zum Speichern
     *
     * @param init Pfad von dem aus gesucht werden soll
     * @param assuredSuffix garantierte Endung
     *
     * @return null wenn abgebrochen, sonst der entsprechende (absolute) Pfad
     */
    public static String getSavePath(String init, String assuredSuffix) {
        if (PropertiesProvider.isHeadless() || !Definitions.GUI) {
            JakeWriter.out.println("Please enter File Path (" + init + ")>");
            Scanner s = new Scanner(JakeWriter.in);
            String p = s.nextLine();
            if (!p.startsWith("/")) p = init + "/" + p;
            if(new File(p).exists()){
                JakeWriter.out.println("Die Datei: " + p + " existiert bereits, soll sie Überschrieben werden?");
                if ("Y".equals(CommandLine.get_answer("Soll das Dokument überschrieben werden?"))) {
                    return p;
                }
                return null;
            }
        } else {
            JFileChooser jfc = new JFileChooser(init);
            if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String file = jfc.getSelectedFile().getAbsolutePath();
                if(!file.endsWith(assuredSuffix))
                    file += assuredSuffix;
                return file;
            } else {
                return null;
            }
        }
        return null;
    }


    /**
     * Saves the Document
     *
     * @return false, if the saving failed
     */
    public boolean saveDocument(){
        String target = this.path.getAbsolutePath();
        if(this.isNew) {
            target = getSavePath(this.getType().suffix);
            if(target==null) return false;
            else for(var l : listeners) l.JakeDocument_ChangedMeta("path", target);
        }

        return saveDocument(target);
    }

    /**
     * sets the document to saved
     * @param path filetargetpaths
     */
    public void setSaved(String path) {
        if(isNew)
            setPath(new File(path));
        for (var l : listeners) l.JakeDocument_Saved(path);
        isNew = false;
    }

    /**
     * Saves the Document to the path
     *
     * @param path the target path
     * @return false, if the saving failed
     */
    public boolean saveDocument(String path) {
        JakeLogger.writeLoggerInfo("Saving Document " + this.name + " to: " + path, "aDoc");

        try {
            PrintWriter pw = new PrintWriter(new File(path));
            pw.println("! " + this.getName() + " created by Jake JakeDocument !");
            for (String line : getFileContents())
                pw.println(line);
            pw.close();
            setSaved(path);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * Will try to load the Document data
     *
     * @return true, if the loading worked :D
     */
    public boolean load() {
        if(!isNew){
            try {
                this.fileLines = (ArrayList<String>) Files.lines(this.path.toPath()).collect(Collectors.toList());
                for (var l : listeners) l.JakeDocument_Loaded();

                return true;
            } catch (IOException e) {
                return false;
            }
        }
        for (var l : listeners) l.JakeDocument_Loaded();
        return false;
    }

    public Map<String,String> getMetadata(){
        if(getPath() == null) return null;
        HashMap<String,String> metadata = new HashMap<>();
        metadata.put("File-Name", path.getName());
        metadata.put("File-Path", path.getPath());
        metadata.put("File-Size", path.length()/1024.0 + "Kb");
        return metadata;
    }
}
