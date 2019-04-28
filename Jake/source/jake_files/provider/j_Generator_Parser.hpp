#ifndef _J_GENERATOR_PARSER_HPP_
#define _J_GENERATOR_PARSER_HPP_ 10007

/**
 * @file j_Generator_Parser.hpp
 * @author Florian Sihler
 * @version 1.0.7
 *
 * @brief Kann Blueprints für verschiedene Designs erstellen - naja, bald :D
 *
 */

#include <fstream>
#include <memory>
#include <string>
#include <vector>
#include <map>
#include <stdexcept>
#include <regex>
#include <sstream>

#include "../core/j_Typedefs.hpp"
#include "../core/j_Debug.hpp"

#include "../j_Helper.hpp"

#include "j_Configurator.hpp"


/*
 * Konzept: erkennt Grupppen mithilfe von "BEGIN <NAME>" und "END"
 * Der Gruppenname gibt an, was generiert werden soll.
 * Beispiel: "BEGIN BOXMODE" zum Erstellen eines neuen Boxmodis
 * Die verchiedenen Gruppen können ihre eigenen Parameter definieren
 * und fordern. Der Generator_Parser arbeitet hierbei ähnlich wie
 * der Konfigurator indem er ein Datenpaket erhält welches er
 * zu füllen versucht.
 * Wie dann dieses Datenpaket zu interpretieren ist obliegt dem
 * initiator. So müsste es allerdings sehr einfach möglich sein
 * dem Generator listen für alle BOXMODE - Gruppen usw.
 * zu Übergeben, sodass er diese dann füllt
 * und Jake dann entsprechend damit arbieten kann!
 */

/**
 * @class GeneratorParser
 * @author Florian Sihler
 * @version 1.0.0
 *
 * @brief Tut bisher niada :D
 */

class GeneratorParser {

    std::vector<std::string> _op_paths;

public:
    /**
     * @struct jObject
     *
     * @brief Definiert einen Block, der Selbst einen Modus oder ähnliches definieren kann
     */
    struct jObject {
        /// @brief Der Bezeichner
        const std::string name;
        /// @brief Key-Value Liste der Konfiguration
        settings_t configuration;
    };

    /**
     * @struct Box
     *
     * @brief Enthält die Grunddaten einer eingelsenen Box
     */
    struct Box {
        /// @brief Name der Box
        std::string name = "";
        /// @brief unbearbeiteter Boxinhalt
        std::string content = "";

        /**
         * @brief Generiert ein jObject auf Basis des Configurators
         *
         * @param blueprint die Einstellungen auf derer Basis das Objekt erstellt werden soll
         *
         * @returns ein entsprechendes jObject;
         */
        GeneratorParser::jObject get_jObject(settings_t blueprint);
    };

    /**
     * @brief Konstruiert den Generator Parser (GePard)
     *
     * @param filenames Die mit ':' getrennten Dateinamen der zugrunde liegenden Datei
     */
    GeneratorParser(const std::string& filenames);

    /**
     * @brief Extrahiert alle 'identifier'-Definitionen aus einer Datei
     *
     * @param identifier der Name der Blöcke die Analysiert werden sollen
     * @param blueprint die Zugrunde liegenden Einstellungen - es ist nicht erlaubt unbekannte hinzu zu fügen
     *
     * @returns Alle gefundenen Objekte. Wird keins Gefunden so wird ein leerer Vektor zurück geben
     */
    std::vector<GeneratorParser::jObject> parseFile(
                                      const std::string& identifier,
                                      settings_t blueprint);

/* protected: */

    /**
     * @brief liefert die nächste BEGIN X END Klausel wirft einen Fehler wenn Datei Fehlerhaft
     *
     * @param inp Der Inputstream auf dem gearbeitet werden soll. tellg liefert danach eine Zeile nach END
     * @param name Wenn nicht ="" wird die erste Box mit dem entsprechenden Namen geliefert und "" wenn es keine mehr gibt
     * @param bufs die zu erwartende Block Größe - Muss normalerweise nicht verändert werden
     *
     * @note diese Funktion füttert Parse File und sollte sonst nicht verwendet werden, ist zu textzwecken aber doch public
     *
     * @returns den unveränderten String zwischen BEGIN X und END
     */
    GeneratorParser::Box get_next_box(std::istream& inp, const std::string& name="", size_t bufs = EXPECTED_BLOCKSIZE);

    // TODO: Generate Group
};


#endif
