#include "j_Debug.hpp"


std::string log_path = exec("printf $(mktemp lilly.XXXXXXXXXX --tmpdir)");
std::ofstream log_output_stream{log_path, std::ios::out | std::ios::app };

const std::string _PT( void ) {
    time_t     now = time(0);
    struct tm  ts;
    char       buf[80];
    ts = *localtime(&now);
    strftime(buf, sizeof(buf), "%d.%m.%Y  %T", &ts);
    return std::string(" [") + buf + " UTC]" ;
}

status_t _w_debug(const std::string& what,
                        const std::string& who,
                        const std::string& file,
                         const std::string& line,
                        const std::string& signature,
                        const std::string& bgcode,
                        const std::string& fgcode,
                        const std::string& prenote
                       ) {
    if(settings["debug"]=="true"){
        if(log_output_stream.is_open()) {
        log_output_stream << _PT() << "  ["<< std::setw(3) << signature << "] "
                          << std::left << std::setw(8)<< who << ": " << prenote 
                          << std::left << std::setw(LOG_LEN-prenote.length())
                          << what << " @"<< std::right << std::setw(5)
                          << line << " ~ " << std::setw(PAT_LEN) << std::left << DEBUG_FORMAT(file,PAT_LEN)
                          << std::endl;
        } else {
            std::cout << "mööp" << std::endl;
        }

        std::cout  << DEBUG_RESET << DEBUG_BOLD << bgcode << fgcode
                   << "[" << std::setw(3) << signature << "] "
                   << std::left << std::setw(8)<< who << ": " << prenote
                   << DEBUG_NORMALIZE << std::left
                   << std::setw(MSG_LEN - prenote.length() + GET_UTF8_LENGTH_OFFSET(what))
                   << DEBUG_FORMATF(what, MSG_LEN - prenote.length()) << " @"<< std::right << std::setw(5)
                   << line << " ~ " << std::setw(PAT_LEN) << std::left << DEBUG_FORMAT(file,PAT_LEN) << _PT()
                   << DEBUG_RESET << std::endl;
        return EXIT_SUCCESS;
    }
    return EXIT_FAILURE;
}