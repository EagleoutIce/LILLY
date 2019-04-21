#include "j_Generator_Parser.hpp"

#define BOX_NAME 2

GeneratorParser::jObject GeneratorParser::Box::get_jObject(settings_t blueprint) {
    std::stringstream strstr(this->content);
    Configurator cfg(strstr);
    cfg.parse_settings(&blueprint);
    return { this->name, blueprint };
}



GeneratorParser::GeneratorParser(const std::string& filename) : _op_path(filename) {}

std::vector<GeneratorParser::jObject> GeneratorParser::parseFile(const std::string& identifier, settings_t blueprint) {
    // Erstellen des InputStreams:
    std::ifstream dataFile(this->_op_path);
    GeneratorParser::Box cur_box;

    std::vector<GeneratorParser::jObject> ret_jObjects = {};

    for(;;){
        cur_box = get_next_box(dataFile, identifier); // Erhalte eine Box
        if(cur_box.name == "") break; // Die Box ist ungültig / es gibt keine mehr
        ret_jObjects.push_back(cur_box.get_jObject(blueprint)); // Füge Box hinzu
    }

    return ret_jObjects;
}

GeneratorParser::Box GeneratorParser::get_next_box(std::istream& inp, const std::string& name, size_t bufs) {
    // Check if inp is valid:
    if(!inp.good() || inp.eof()) throw std::runtime_error("Inputstream in get_next_box failed (Datei nonexistent?)");

    std::string buffer; buffer.reserve(bufs);

    std::string current_line;

    const std::regex p_begin = std::regex(R"(^ *(BEGIN) *([\w-äöüßÄÖÜ]+))");
    const std::regex p_end   = std::regex(R"(^ *(END))");

    GeneratorParser::Box ret_box;

    //    if(std::regex_search(line, matches, this->_pattern)){
    std::match_results<std::string::const_iterator> c_match;
    while(std::getline(inp, current_line)){
        current_line = Tokenizer::erase_skipper(current_line);
        if(std::regex_search(current_line, c_match, p_begin)) { // match BEGIN X
            if (name == "" || c_match[BOX_NAME] == name) { // FOUND A BOX
                ret_box = GeneratorParser::Box{c_match[BOX_NAME]};
                //search throuh end;
                while(std::getline(inp, current_line)){
                    current_line = Tokenizer::erase_skipper(current_line);
                    if(std::regex_search(current_line, c_match, p_begin)) // match BEGIN X
                        throw std::runtime_error("\"BEGIN " + ret_box.name + "\" enthält ein weiteres BEGIN, das ist nicht erlaubt");
                    if(std::regex_search(current_line, c_match, p_end))  // found end :D
                        return ret_box;
                    ret_box.content += current_line + "\n";
                }
                throw std::runtime_error("\"BEGIN " + ret_box.name + "\" hat kein passendes END!");
                // Gibt nur einen Fehler aus, wenn er im Zusammenhang mit einer gesuchten Box besteht
            } // found no box
        }
    }

    return {""}; // Da ist nichts
}

#undef BOX_NAME
