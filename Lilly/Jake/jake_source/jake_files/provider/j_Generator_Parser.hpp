#ifndef _J_GENERATOR_PARSER_HPP_
#define _J_GENERATOR_PARSER_HPP_ 10007

/**
 * @file j_Generator_Parser.hpp
 * @author Florian Sihler
 * @version 1.0.7
 *
 * @brief Kann Blueprints für verschiedene Designs erstellen - naja, bald :D
 *
 * @warning #WIP
 */

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
    
};


#endif
