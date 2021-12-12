#pragma once

#include <iostream>

namespace SM {
    enum ErrorCode { 
        ER_SPECIAL,
        ER_MEM_ALL,
        ER_MEM_REALL,
        ER_MEM_NEW, 
        ER_SIZE, 
        ER_BOUNDS,
        ER_NULL,
        ER_TEMP,
    };

    struct Err {
    private:
        ErrorCode code_{ER_SPECIAL};
        const std::string error_text_;

    public:
        Err(ErrorCode code) : code_{code}, error_text_{} {};

        Err(const std::string& text) : code_{ER_SPECIAL}, error_text_{text} {};

        [[nodiscard]] ErrorCode getcode() const noexcept { return code_; }

        std::string gettext() const noexcept { return *this; }

        operator std::string() const noexcept {
            switch (code_) {
                case ER_SPECIAL: return error_text_;
                case ER_MEM_ALL: return "Memory allocation";
                case ER_MEM_REALL: return "Memory reallocation";
                case ER_MEM_NEW: return "Troubles while using \'new\' keyword";
                case ER_SIZE: return "Bad size parameter";
                case ER_BOUNDS: return "Going beyond borders";
                case ER_NULL: return "Null argument passed";
                case ER_TEMP: return "Trying to edit a temporary object";
                default: return "Invalid error code";
            }
        }

        void print() const { 
            std::cout << std::string(*this) << std::endl; 
        }
    };

}; //namespace SM