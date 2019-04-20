#include "j_Tokenizer.hpp"


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
        if(this->_input->eof()) return 0;
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
