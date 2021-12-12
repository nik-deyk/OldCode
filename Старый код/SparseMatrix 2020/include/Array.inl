#include "Array.h"

using namespace SM;

template <typename Double>
Array<Double>::Array(size_t n, size_t m) : Allocator<Double>(n, m) {
    array_ = new Double *[n];
    for (size_t i = 0; i < n; i++) {
        array_[i] = new Double[m];
    }
};

template <typename Double>
Array<Double>::~Array() {
    for (size_t i = 0; i < n_; i++) {
        delete[] array_[i];
    }
    delete[] array_;
}

template <typename Double>
Array<Double> *Array<Double>::Copy() {
    Array<Double> *res = new Array<Double>{n_, m_};
    for (size_t i = 0; i < n_; i++) {
        for (size_t j = 0; j < m_; j++) {
            res->array_[i][j] = array_[i][j];
        }
    }
    return res;
}

template <typename Double>
const Double *Array<Double>::find(const Position &pos) const noexcept {
    const Double &res = array_[pos.i - 1][pos.j - 1];
    if (std::equal_to<Double>()(res, Double{}))
        return NULL;
    return &res;
}

template <typename Double>
Double &Array<Double>::edit(const Position &pos) const {
    return array_[pos.i - 1][pos.j - 1];
}

template <typename Double>
bool Array<Double>::remove(const Position &pos) noexcept {
    Double &res = array_[pos.i - 1][pos.j - 1];
    if (std::equal_to<Double>()(res, Double{}))
        return false;
    res = Double{};
    return true;
}

template <typename Double>
bool Array<Double>::Equal(const Allocator<Double> *other) const noexcept { 
    const Array<Double> * arr = dynamic_cast<const Array<Double> *>(other);
    if (Position(n_, m_) != Position(arr->n_, arr->m_)) return false;

    for (size_t i = 0; i < n_; i++) {
        for (size_t j = 0; j < m_; j++) {
            if (!std::equal_to<Double>()(array_[i][j], arr->array_[i][j])) return false;
        }
    }
    
    return true;
}