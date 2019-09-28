package de.eagle.gepard.modules;

/**
 * @file Generators.java
 * @author Florian Sihler
 * @version 1.0.0
 * @since 2.1.0
 *
 * @brief Gepardmodul welches Generatoren für Dokumente zur Verfügung stellt.
 * @see de.eagle.gepard.modules.AbstractGepardModule
 * @see de.eagle.gepard.parser.GeneratorParser
 */

/*
 * Ein Generator soll wie folgt verfahren, das Ziel ist es,
 * Mithilfe eines Namens ein Dokument bezeihungsweise eine ganze Ordnerstruktur
 * mit gewissen Eigenscahften und Inhalten zu erstellen.
 * So kann zum Beispiel ein Generator 'Mitschrieb' existieren,
 * der wie folgt verfährt:
 *      - Abfragen von für den Generator-Parser relevanter Variablen Getreu:
 *          - "Gib den gewünschten Namen für das Dokument ein: " => NAME
 *          - "Gib den Namen der zugehörigen Vorlesung ein; " => VORLESUNG
 *          - ....
 *      - Erzeugen der Dokumente nach dem entsprechenden Muster, wobei die gewünschten
 *        Variablen über den Doppelpunkt als expandable zur Verfügung stehen und so
 *        eingesetzt werden können. Es können so natürlich auch Lilly-interne Dokumente generiert
 *        werden. Dies ist der eigentliche Hautpgrund für die Existenz eines Generators und hierfür
 *        wurde das Modul auch ursorünglich konstruiert. 
 * 
 * 
 * 
 */

import de.eagle.gepard.parser.GeneratorParser;
import de.eagle.lillyjakeframework.core.CoreSettings;
import de.eagle.lillyjakeframework.core.Definitions;
import de.eagle.util.constants.ColorConstants;
import de.eagle.util.datatypes.ReturnStatus;
import de.eagle.util.datatypes.SettingDeskriptor;
import de.eagle.util.datatypes.Settings;
import de.eagle.util.enumerations.eSetting_Type;
import de.eagle.util.helper.Executer;
import de.eagle.util.io.JakeWriter;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static de.eagle.util.io.JakeLogger.writeLoggerDebug1;
import static de.eagle.util.io.JakeLogger.writeLoggerDebug2;


/**
 * Bearbeitet Boxen mit dem Bezeichner {@value box_name}
 */
public class Generators extends AbstractGepardModule{
    /**
     * Name der Generator-Box
     */
    public static final String box_name = "generator";

    private static Generators tInstance = null;

    public static Generators getInstance() {
        if(tInstance == null)
            tInstance = new Generators();
        return tInstance;
    }

    /**
     * Konstruiert das GepardModul
     */
    private Generators() {
        super(box_name);
    }

    /**
     * Gespeicherte Blaupause, die es ermöglicht, die entsprechende Blaupause nur einmal pro Klasse
     * zu generieren.
     */
    protected static Settings blueprint = null;

    /**
     * 
     * @brief aktuell wird nur die generierung einer einzelnenn Datei unterstützt, an einer template-dir option wird gearbeitet. diese könne als Zip-Datei vorleiegen?
     * 
     * @return die Blaupause für die Einstellungen
     */
    @Override
    public Settings getBlueprint() {
        if (blueprint == null) {
            blueprint = new Settings("<Blueprint> Generators");
            // ---- Mandatories
            blueprint.put("name",
                    SettingDeskriptor.create("name", "Interner Name der Regel", eSetting_Type.IS_TEXT, true));
            // blueprint.put("display-name", SettingDeskriptor.create("display-name",
            //         "Name der beim Ausführen angezeigt wird", eSetting_Type.IS_TEXT, true));
            blueprint.put("template-file", SettingDeskriptor.create("template-file",
                    "Name der Template - Datei, sie folgt einer etwas eigenen Syntax :D", eSetting_Type.IS_TEXT, true));
            blueprint.put("target-mode", SettingDeskriptor.create("target-mode",
                    "Modus für Expandables.", eSetting_Type.IS_NUM, true));
            // 0: Default expandables will not be replaced
            // 1: Default expandables will be replaced

            // So zum Beispiel, dass man eine Zeile, die eine Variable fordert durch ':' anführen kann. In diesem Fall wird die Zeile nur dann ausgegeben und übernommen, wenn die Variable einen Wert erhalten hat etc.
            // Weiter kann die Datei durch eine Shebang-Ähnliche Startzeile anweisen, dass sie lieber an den dort notierten Ort gesetzt werden möchte. Tritt eine solche Zelle auf, wird der Nutzer gefragt und kann - frei nach Wunsch - auch eine andere Zeile angeben :D

            // -- optionals
            blueprint.put("brief",
                    SettingDeskriptor.create("brief", "Was macht der Generator?", eSetting_Type.IS_TEXT, false, ""));
            // blueprint.put("variables", SettingDeskriptor.create("variables", "Kommaseparierte Liste an benötigten Variablen",
            //         eSetting_Type.IS_TEXT, false));
            // Alle weiteren Variablen die gebraucht werden können als
            // VARIABLE = ERFRAGETEXT
            // Angegeben werden.
        }
        return blueprint;
    }

    /**
     * @return Standardeinstellungen
     */
    @Override
    public Settings getDefaults() {
        Settings settings = new Settings("<Default> Generators", true, new HashMap<>());
        // settings.put("0test!AUTHOR!Bitte gib einen Autor an!HAMSTER!Ich bin optional", SettingDeskriptor.create("test", "Exemplarischer Generator. Pfade, die mit einem '!' beginnen verweisen auf '.jar'-Elemente", eSetting_Type.IS_TEXT, false, "!/templates/test.template"));
        // settings.put("0test2!Name!Bitte gib den Namen der Konfiguration an!HAMSTER!Ich bin optional", SettingDeskriptor.create("test2", "Exemplarischer Generator.", eSetting_Type.IS_TEXT, false, "!/templates/test2.template"));
        settings.put("1mitschrieb!Name!Bitte gib den Namen der Datei an", SettingDeskriptor.create("mitschrieb", "Generiert die Dateien für einen Mitschrieb.", eSetting_Type.IS_TEXT, false, "!/templates/mitschrieb.template"));
        settings.put("1uebungsblatt!Vorlesung!Bitte gib die zugehörige Vorlesung an (ANA1, LAII, ...)!Ub-Nr!Bitte gib die Nummer des Übungsblattes an!cAuthor!Bitte gib den Namen der Autoren des Übungsblattes an [optional]", SettingDeskriptor.create("uebungsblatt", "Generiert die Dateien für ein Übungsblatt.", eSetting_Type.IS_TEXT, false, "!/templates/uebungsblatt.template"));

        // settings.put("test:thereal", SettingDeskriptor.create("test", "hihi" , eSetting_Type.IS_TEXT, false));
        // Es sollte keine Standardprojekte geben
        return settings;
    }

    /**
     * Bearbeitet eine komplette Datei
     *
     * @param rulefiles die Liste der rulefiles
     * 
     * @return Einstellungen die entsprechend der Boxen konfiguriert sind
     * @throws IOException Im Falle eines Fails von
     *                     {@link GeneratorParser#parseFile(String, Settings, boolean)}
     */
    public Settings getGenerators(String rulefiles) throws IOException {
        if (rulefiles.isEmpty())
            return getDefaults();

        GeneratorParser gp = new GeneratorParser(rulefiles);
        GeneratorParser.JObject[] j = gp.parseFile(Generators.box_name, new Settings("Generators"), true);
        return parseRules(j, true);
    }

    
    /**
     * Parst eine einzelne Box
     * 
     * @param box      die Box
     * @param complete ignored
     *
     * @return null, wenn es keine generators-Box war, sonst die entsprechende
     *         Einstellung
     */
    public Settings parseBox(GeneratorParser.JObject box, boolean complete) {
        writeLoggerDebug1("Bearbeite nun: " + box.toString(), "Generators");

        Settings settings = new Settings(box.getName());

        LinkedList<String> name = new LinkedList<>();
            name.add(box.config.getValue("target-mode")+box.config.getValue("name"));

        for (var s : box.config) {
            String _key = s.getKey();
            if(!_key.equals("name") && !_key.equals("template-file") && !_key.equals("brief") && !_key.equals("target-mode")) {
                name.add(_key); name.add(s.getValue().getValue());
            }
        }

        settings.put(String.join("!",name),
                SettingDeskriptor.create("", (box.config.getValue("brief")==null?"Box-Deskriptor für Project (Gepard)":box.config.getValue("brief")),box.config.getValue("template-file")));
        writeLoggerDebug2("erhalten: " + settings.toString(), "Generators");
        return settings;
    }

    /**
     * Liefert den Generator mit dem entsprechenden Namen
     *
     * @param generators Sammlung aller Generatoren
     * @param name Gesuchter Generatorname
     * @return null, wenn der Generator nicht gefunden wurde, sonst entsprechend der Generator
     */
    private SettingDeskriptor<String> getGenerator(Settings generators, String name){
        for (var d : generators) {
            String _k = d.getKey();
            if(_k.contains("!"))
                _k = _k.substring(0,_k.indexOf("!"));
            if (_k.substring(1).equals(name))
                return d.getValue();
        }
        return null;
    }

    private String containsOptExpansion(String line, ArrayList<String> opts) {
        for(String o : opts){
            if(line.contains("${" + o + "}")) //explicitly disabled? || line.contains("$(" + o + ")"))
                return o;
        }
        return null;
    }

    public String sbangCheck(String line, ArrayList<String> optionals, Settings expandables, Scanner scanner) throws IOException {
        if(line.startsWith("#")) {
            String _sbang = line.substring(1);
            // Expand with set Variables:
            String _optstr = containsOptExpansion(_sbang,optionals);
            if( _optstr != null){
                JakeWriter.err.println("Die Variable \"" + _optstr + "\" war nicht optional und wurde von dir nicht geliefert!");
                JakeWriter.err.println("Abbruch.");
                return null;
            }
            _sbang = Expandables.expand(expandables, _sbang);
            // And now, expand with the CoreSettings:
            _sbang = Expandables.expand(Expandables.getInstance().expandsCS(), _sbang);
            JakeWriter.out.println("Der von dir gewählte Generator möchte eine Datei hier hin platzieren: " + ColorConstants.COL_GREEN + _sbang + ColorConstants.COL_RESET);
            if(!CoreSettings.requestValue("S_ANSWER").toUpperCase().trim().startsWith("Y")) {
                JakeWriter.out.print("Gebe einen anderen Pfad ein, wenn du eine andere Platzierung in Erwägung ziehst: ");
                String p = scanner.nextLine();
                if (!p.isBlank())
                    _sbang = p;
                JakeWriter.out.println(ColorConstants.COL_GREEN + "Schreibe nun in: " + _sbang + ColorConstants.COL_RESET);
                if (new File(_sbang).getParentFile().mkdirs()) {
                    JakeWriter.out.println("Die benötigte Ordnerstruktur wurde erstellt.");
                }
            }
            return _sbang;
        }
        return "";
    }

    public ReturnStatus launchGenerator(Settings _generators, String _name){
        SettingDeskriptor<String> sd = getGenerator(_generators, _name);
        if(sd == null){
            // Unbekannt => Liste alle Verfügbaren auf
            JakeWriter.out.format("Der Generator (-what): \"%s\" existiert nicht.%nHier einer Liste aller verfügbaren Generatoren%n",_name);
            for(var s : _generators){
                String _k = s.getKey();
                if(_k.contains("!"))
                    _k = _k.substring(0,_k.indexOf("!"));
                JakeWriter.out.format("    - %s: %s%n", _k.substring(1),s.getValue().getBrief());
            }

            return ReturnStatus.EXIT_FAILURE;
        }
        // Erfrage die Werte für die jeweiligen Variablen und füge sie den Expandable-Settings hinzu.
        // Da diese nach der Sitzung zurück gesetzt werden können die gesamten Expandables verwendet werden.

        String[] names = sd.getName().split("!");
        // 0 => Name of the Generator
        // 1,2 => first var, 3,4 => second var, ...

        try {
            Settings expandables;
            switch (names[0].charAt(0)){
                default:
                case '0': // own only
                    expandables =  new Settings("expandables"); break;
                case '1': // defaults too
                    expandables = Expandables.getInstance().expandsCS(); break;
            }
            expandables.setUnknownPolicy(true);
            Scanner scanner = new Scanner(System.in);
            ArrayList<String> optionals = new ArrayList<>();
            if(names.length > 1)
                JakeWriter.out.println(ColorConstants.COL_CYAN + "Der Generator benötigt ein paar Informationen, die es nun auszufüllen gilt. Du kannst keinen Wert angeben um eine optionale Angabe zu verweigern." + ColorConstants.COL_RESET);
            for(int i = 1; i < names.length; i+=2) {
                JakeWriter.out.format("[%s%s%s] %s> ", ColorConstants.COL_PURPLE, names[i], ColorConstants.COL_RESET, names[i+1]);
                String value = scanner.nextLine();
                if(!value.isBlank())
                    expandables.put(names[i], SettingDeskriptor.create(names[i], "Vom Generator erfragte Einstellung", value));
                else optionals.add(names[i]);
                // Maybe mark optionals?
            }
            // Detect if the supplied path links to a jar-internal-file:
            String path = sd.getValue();
            Stream<String> _tline;
            if(path.startsWith("!")){// In Jar
                _tline = new BufferedReader(new InputStreamReader(Generators.class.getResourceAsStream(path.substring(1)))).lines();
            } else {// on disk or whatever
                _tline = Files.lines(Paths.get(path));
            }
            String[] lines = _tline.map(x -> x.replaceAll("!:.*:!","")).filter(x -> !x.isBlank()).toArray(String[]::new);
            if (lines.length == 0) {
                JakeWriter.err.println("Keine nutzbare Zeile erhalten");
                JakeWriter.err.println("Abbruch.");
                return ReturnStatus.EXIT_FAILURE;
            }
            // Analyse first Line for being shebang to get correct target
            String filename = "./" + new File(path).getName();
            if(new File(filename).getParentFile().mkdirs()) {
                JakeWriter.out.println("Die benötigte Ordnerstruktur wurde erstellt.");
            }
            JakeWriter.out.format("Generiere auf Basis von: %s%s%s%n", ColorConstants.COL_CYAN,filename, ColorConstants.COL_RESET);
            // ================ Expandables
            expandables.put("GEN-FILENAME", SettingDeskriptor.create("","Generator Expandable für den Dateinamen",filename));
            expandables.put("GEN-DATE", SettingDeskriptor.create("","Generator Expandable für den Dateinamen", Definitions.date_format.format(LocalDate.now())));
            expandables.put("GEN-TIME", SettingDeskriptor.create("","Generator Expandable für den Dateinamen",Definitions.time_format.format(LocalTime.now())));
            expandables.put("GEN-JAKEVER", SettingDeskriptor.create("","Generator Expandable für den Dateinamen",Definitions.JAKE_VERSION));
            // ================
            PrintWriter out = null;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String _sbang = sbangCheck(line, optionals, expandables, scanner);
                if(_sbang == null)
                    return ReturnStatus.EXIT_FAILURE;
                else if (!_sbang.equals("")) {// New File
                    if (!_sbang.equals(filename)){
                        //JakeWriter.out.println("Wechsle die Datei!");
                        if(out != null)
                            out.close();
                        out = new PrintWriter(_sbang);
                    }
                    continue;
                } else if (i==0){
                    out = new PrintWriter(filename);
                }
                // We will check, if a line contains an expansion - request and if so, it will check if the Variable was passed
                // If not the Variable will be passed. If it was optional, and the line wasn't optional (startet with an ':')
                // There will be an error
                boolean optAccepted = line.startsWith(":");
                if(optAccepted) line = line.substring(1);
                String _optstr = containsOptExpansion(line,optionals);
                if( _optstr != null){
                    if(optAccepted)
                        continue;
                    // Error we do not allow passing this variable:
                    JakeWriter.err.println("Die Variable \"" + _optstr + "\" war nicht optional und wurde von dir nicht geliefert!");
                    JakeWriter.err.println("Abbruch.");
                    return ReturnStatus.EXIT_FAILURE;
                }
                // System.out.println("processing: " + line + " => " + Expandables.expand(expandables,line));
                if(out == null) {
                    JakeWriter.err.println("Das angeforderte Dateiziel scheint invalide.");
                    JakeWriter.err.println("Abbruch.");
                    return ReturnStatus.EXIT_FAILURE;
                } else {
                    out.println(Expandables.expand(expandables, line));
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnStatus.EXIT_SUCCESS;
    }
}