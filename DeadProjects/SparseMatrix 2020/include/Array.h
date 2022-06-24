#pragma once

#include "Allocator.h"

namespace SM {
    template <typename D, typename LD, typename A>
    class Matrix;

    template <typename Double = double>
    class Array : protected Allocator<Double> {
        template <typename D, typename LD, typename A>
        friend class Matrix;

        using Allocator<Double>::n_;
        using Allocator<Double>::m_;

        Double **array_{};

        Array(const Array<Double>& ) = delete;
        Array<Double> & operator=(const Array<double>&) = delete;

        Array(size_t, size_t);
        ~Array();

        [[nodiscard]] Array<Double> *Copy();

        [[nodiscard]] const Double * find(const Position &) const noexcept;

        [[nodiscard]] Double & edit(const Position &) const;

        bool remove(const Position &) noexcept;

        bool Equal(const Allocator<Double> *) const noexcept;
    };

    #include "Array.inl"
};