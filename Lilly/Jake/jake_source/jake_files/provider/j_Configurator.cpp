#include "j_Configurator.hpp"

#define LHS 1
#define OP 2
#define RHS 3

Configurator::Configurator(const std::string& config_file) {
    this->_t = std::make_unique<Tokenizer>(config_file,R"(([^= ]*) *(=|\+=) *([^=\n]*)$)");
}

status_t Configurator::parse_settings(std::map<std::string, data_t>* settings, bool add_unkown) {
    while(_t->loadNext()) {
        Tokenizer::Match m = _t->get();
        if(m.failure()) continue;
        if(m.matchings.size() == 4){ // ALL LHS OP RHS
            if(settings->find(m.matchings[LHS]) != settings->end() || add_unkown) {
                if(m.matchings[OP]=="=")
                    (*settings)[m.matchings[LHS]] = TO_DATA(m.matchings[RHS]);
                else if (m.matchings[OP]=="+=")
                    (*settings)[m.matchings[LHS]] += TO_DATA(m.matchings[RHS]);
            } else 
            std::cerr << COL_ERROR << "Die Zuordnung: \"" << m.stripped << "\" ist ungültig! " 
                      << COL_RESET << std::endl;
        } else {
            std::cerr << COL_ERROR << "Die Zeile: " << m.matchings[0] << " ist ungültig! " 
                      << COL_RESET << std::endl;
        }
    }
    return true;
}

#undef LHS
#undef OP
#undef RHS
