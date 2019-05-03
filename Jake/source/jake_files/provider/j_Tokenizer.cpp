#include "j_Tokenizer.hpp"

Tokenizer::Tokenizer(const std::string& input_path, const std::string& pattern) : _eol('\n'){
    _pattern = std::regex(pattern); _skipper = std::regex(R"(![^!]*!)");
    _ifs = std::ifstream(input_path);
    if(!_ifs.good()){
        std::cerr << COL_ERROR << "Konnte keinen Tokenizer erstellen!" 
                  << " Die Datei: \"" << input_path << "\" scheint korrupt/nonexistent zu sein!" 
                  << COL_RESET << std::endl;
        throw std::runtime_error("Tokenizer: Datei scheint fehlerhaft/nonexistent");
    }
    _input = &_ifs;
}


Tokenizer::Tokenizer(std::istream& input,
                     const std::string& pattern,
                     unsigned int skip_lines,
                     const std::string& skipper,
                     const char eol) : _eol(eol)  {
                  _input = &input; 
                  _pattern = std::regex(pattern); 
                  _skipper = std::regex(skipper);
                  for(int i = 0; i < skip_lines; ++i) std::getline(*this->_input,_current_line,this->_eol);
                }


status_t Tokenizer::loadNext() {
    std::string tmp=" ";

    while(tmp.find_first_not_of(' ') == std::string::npos) {
        std::getline(*this->_input,tmp,this->_eol);
        if(this->_input->eof()) return 0; // if(getline)
        // Multiline via '++\'
        while(tmp.length() > 2 && tmp.substr(tmp.length()-3) == R"(++\)") {
            tmp = tmp.substr(0, tmp.length()-3);
            w_debug("Lineskip detected - welcome2multiline: \"" + tmp + "\"","TokenZ");
            std::string supertmp =""; // :DDDDDDD
            std::getline(*this->_input,supertmp,this->_eol);
            if(this->_input->eof()) return 0; // again: if(getline) => ERROR MULTILINE MISSING
            tmp += supertmp.substr(supertmp.find_first_not_of(' ')); // skip beginning spaces because its nice
        }
        _current_original = tmp;
        tmp = Tokenizer::erase_comments(tmp);
    }

    this->_current_line = tmp;
    return 1;
}

Tokenizer::Match Tokenizer::get() {
    std::string line = this->get_current_line();

    std::match_results<std::string::const_iterator> matches;
    if(std::regex_search(line, matches, this->_pattern)){
        std::vector<std::string> m;
        for(int i = 0; i < matches.size(); ++i)
            m.push_back(matches[i]);
        return Match(m,_current_original,line);
    }

    return Match({},"",""); // Explicit empty
}
