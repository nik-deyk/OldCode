#pragma once

#include "Elem.h"

namespace SM {
    template <typename D>
    class ElemPtr;

    template <typename D, typename LD, typename A>
    class Matrix;

    template <typename Double = double>
    class Allocator {
        friend class ElemPtr<Double>;
        
        template <typename D, typename LD, typename A>
        friend class Matrix;

    protected:
        const size_t n_, m_;

        Allocator(size_t n, size_t m) : n_(n), m_(m) {};
        virtual ~Allocator() {};

        [[nodiscard]] virtual Allocator<Double> * Copy() = 0;

        [[nodiscard]] virtual const Double * find(const Position &) const noexcept = 0;

        [[nodiscard]] virtual Double & edit(const Position &) const = 0;

        virtual bool remove(const Position &) noexcept = 0; //false if element was set default

        virtual bool Equal(const Allocator<Double> *) const noexcept = 0;
    };
};