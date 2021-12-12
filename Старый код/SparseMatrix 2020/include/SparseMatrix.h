#pragma once

#include "Array.h"
#include "ElemPtr.h"
#include <cassert>
#include <cstdio>

namespace SM {
    template <typename Double = double, typename LongDouble = long double, typename A = Array<Double>> class Matrix {
        static_assert(std::is_base_of<Allocator<Double>, A>::value,
                      "Allocator should be inherited from base class & have same element type");
        static_assert(!std::is_same<Allocator<Double>, A>::value,
                      "Allocator should be valid (not virtual) class");
        static_assert(std::is_convertible<LongDouble, Double>::value && std::is_convertible<Double, LongDouble>::value,
                      "Types Double & LongDouble should be convertible to each other");

        Allocator<Double> *allocator_{};
        const bool copyed_;
        index row_{};

        Matrix(const Matrix<Double, LongDouble, A> *other, index row)
            : allocator_{other->allocator_}, copyed_{true}, row_{row} {}

        Double get_no_check(const Position &pos) const {
            const Double *d = allocator_->find(pos);
            if (d == NULL)
                return Double{};
            return *d;
        }

        void set_no_check(const Position &pos, Double value) {
            if (std::equal_to<Double>()(value, Double{})) {
                allocator_->remove(pos);
            } else
                allocator_->edit(pos) = value;
        }

        inline void set_no_check_no_remove(const Position &pos, Double value) {
            if (!std::equal_to<Double>()(value, Double{}))
                allocator_->edit(pos) = value;
        }

    public:
        Matrix(const Matrix<Double, LongDouble, A> &other) : copyed_{false} {
            if (other.copyed_) throw Err(ER_TEMP);
            allocator_ = other.allocator_->Copy();
        }

        Matrix<Double, LongDouble, A> operator=(const Matrix<Double, LongDouble, A> &other) {
            if (copyed_ || other.copyed_)
                throw Err(ER_TEMP);
            delete allocator_;
            allocator_ = other.allocator_->Copy();
        }

        Matrix(size_t, size_t);
        Matrix(size_t, size_t, Double (*)(size_t, size_t));

        [[nodiscard]] Double get(size_t, size_t) const;
        void set(size_t, size_t, Double);

        [[nodiscard]] size_t num_rows() const noexcept { return allocator_->n_; }
        [[nodiscard]] size_t num_columns() const noexcept { return allocator_->m_; }

        bool operator==(const Matrix<Double, LongDouble, A> &) const;
        bool operator!=(const Matrix<Double, LongDouble, A> &s2) const { return !(*this == s2); }
        Matrix<Double, LongDouble, A> operator+(const Matrix<Double, LongDouble, A> &) const;
        Matrix<Double, LongDouble, A> operator*(const Matrix<Double, LongDouble, A> &) const;

        ElemPtr<Double> operator*() const;
        Matrix<Double, LongDouble, A> operator+(index) const;
        Matrix<Double, LongDouble, A> operator-(index k) const { return this->operator+(-k); }
        ElemPtr<Double> operator[](index) const;

        friend Matrix<Double, LongDouble, A> operator+(index k, const Matrix<Double, LongDouble, A> &m) { return m.operator+(k); }

        ~Matrix();
    };

#include "SparseMatrix.inl"

    typedef Matrix<> SparseMatrix;

}; // namespace SM