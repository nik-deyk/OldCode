#pragma once

#include <cstdio>

namespace SM {
    struct Position {
        size_t i{}, j{};

        Position(size_t i0 = 0, size_t j0 = 0) : i(i0), j(j0) {}

        Position operator+(const Position &other) const {
            return Position(this->i + other.i, this->j + other.j);
        }

        Position operator/(size_t x) const {
            return Position(this->i / x, this->j / x);
        }

        bool operator==(const Position &other) const {
            return this->i == other.i && this->j == other.j;
        }

        bool operator!=(const Position &other) const {
            return !this->operator==(other);
        }
    };

    template <typename Double = double>
    struct Elem : Position {
        Double d;

        Elem(size_t i0 = 0, size_t j0 = 0, Double d0 = {}) : Position(i0, j0), d(d0) {}
        
        Elem(Double d0) : d(d0) {}
    };
};