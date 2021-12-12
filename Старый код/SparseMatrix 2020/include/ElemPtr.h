#pragma once

#include "Allocator.h"
#include "Err.h"

namespace SM {   
    typedef long long int index;

    template <typename D, typename LD, typename A>
    class Matrix;

    template <typename Double = double> class ElemPtr {
        template <typename D, typename LD, typename A>
        friend class Matrix;
    
        struct FlPos {
            index i{}, j{};
            FlPos(index i0 = {1}, index j0 = {1}) : i(i0), j(j0) {};
            FlPos(const Position & x) : i(x.i), j(x.j) {};
            operator Position() const { return Position(i, j); }
        };

        Allocator<Double> *allocator_{};
        FlPos current_{};

        FlPos shift(index k) const noexcept {
            index glob_shift = (current_.i - 1) * allocator_->m_ + (current_.j - 1) + k;
            index res_row = (glob_shift / allocator_->m_ + 1);
            index res_inner_shift = (glob_shift % allocator_->m_ + 1);
            return FlPos(res_row, res_inner_shift);
        }

        static inline void check_bounds(Allocator<Double> * allocator, const FlPos &pos) {
            if (!(0 < pos.i && pos.i <= allocator->n_) || !(0 < pos.j && pos.j <= allocator->m_))
                throw Err(ER_BOUNDS);
        }

        ElemPtr(Allocator<Double> *allocator, index i = {0}, index j = {0})
            : allocator_(allocator), current_(i, j) {
            if (!allocator_)
                throw Err(ER_NULL);
        }
    public:

        Double &operator*() const {
            check_bounds(allocator_, current_);
            return allocator_->edit(this->current_);
        }

        ElemPtr<Double> operator+(index k) const {
            const FlPos &res = shift(k);
            return ElemPtr<Double>(allocator_, res.i, res.j);
        }

        ElemPtr<Double> operator-(index k) const { return this->operator+(-k); }

        Double &operator[](index k) const {
            const FlPos &res = shift(k);
            check_bounds(allocator_, res);
            return allocator_->edit(res);
        }

        friend ElemPtr<Double> operator+(index k, const ElemPtr<Double> &r) {
            return r.operator+(k);
        }
    };
}; //namespace SM