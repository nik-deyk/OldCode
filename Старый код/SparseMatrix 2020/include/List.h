#pragma once

#include "Allocator.h"
#include "Elem.h"

namespace SM {
    template <typename D, typename LD, typename A>
    class Matrix;
    
    template <typename Double = double>
    class List : public Allocator<Double> {
        List *next{};

        Elem<Double> key_{};

        Position top_{1, 1}, bottom_{};
        Position center_{};

        [[nodiscard]] Elem *find(const Position &pos) const;

    public:
        List(size_t height = 1, size_t width = 1) : 
          bottom_{height, width}, 
          center_{(top_ + bottom_) / 2} 
        {
            if (width == 0 || height == 0)
                throw Err(ER_SIZE);
        }

        void add(const Elem & key);
        void remove(const Elem & key);
        double *edit(const Elem & key);
    };

    #include "List.inl"

}; //namespace SM