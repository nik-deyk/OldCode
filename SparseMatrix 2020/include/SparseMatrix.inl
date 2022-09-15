#include "SparseMatrix.h"

using namespace SM;

template <typename D, typename LD, typename A>
Matrix<D, LD, A>::Matrix(size_t n, size_t m) : copyed_(false) {
    allocator_ = new A{n, m};
}

template <typename D, typename LD, typename A>
Matrix<D, LD, A>::Matrix(size_t n, size_t m, D (*f)(size_t, size_t)) : copyed_(false) {
    allocator_ = new A{n, m};
    for (size_t i = 1; i <= n; i++) {
        for (size_t j = 1; j <= m; j++) {
            this->set_no_check_no_remove(i, j, f(i, j));
        }
    }
}

template <typename D, typename LD, typename A> 
D Matrix<D, LD, A>::get(size_t i, size_t j) const {
    if (copyed_)
        throw Err(ER_TEMP);

    const Position & pos = Position(i, j);

    ElemPtr<D>::check_bounds(allocator_, pos);

    return get_no_check(pos);
}

template <typename D, typename LD, typename A>
void Matrix<D, LD, A>::set(size_t i, size_t j, D value) {
    if (copyed_)
        throw Err(ER_TEMP);

    const Position & pos = Position(i, j);

    ElemPtr<D>::check_bounds(allocator_, pos);

    return set_no_check(pos, value);
}

template <typename D, typename LD, typename A>
bool Matrix<D, LD, A>::operator==(const Matrix &other) const {
    if (copyed_ || other.copyed_)
        throw Err(ER_TEMP);

    return allocator_->Equal(other.allocator_);
}

template <typename D, typename LD, typename A>
Matrix<D, LD, A> Matrix<D, LD, A>::operator+(const Matrix &other) const {
    if (copyed_ || other.copyed_)
        throw Err(ER_TEMP);

    const size_t & n = allocator_->n_;
    const size_t & m = allocator_->m_;
    if (Position{n, m} != Position{other.allocator_->n_, other.allocator_->m_})
        throw Err(ER_SIZE);

    Matrix<D, LD, A> result{n, m};
    Position cur{};
    for (size_t i = 1; i <= n; i++) {
        for (size_t j = 1; j <= m; j++) {
            cur.i = i; cur.j = j;
            result.set_no_check_no_remove(cur, this->get_no_check(cur) + other.get_no_check(cur));
        }
    } 
    return result;
}

template <typename D, typename LD, typename A>
Matrix<D, LD, A> Matrix<D, LD, A>::operator*(const Matrix &other) const {
    if (copyed_ || other.copyed_) throw Err(ER_TEMP);

    const size_t & n = allocator_->n_;
    const size_t & m = allocator_->m_;

    if (m != other.allocator_->n_) throw Err(ER_SIZE);

    Matrix<D, LD, A> result{n, other.allocator_->m_};

    for (size_t i = 1; i <= n; i++) {
        for (size_t j = 1; j <= other.allocator_->m; j++) {
            LD tmp{};
            for (size_t k = 0; k <= m; k++)
                tmp += get_no_check(Position(i, k)) * other.get_no_check(Position(k, j));
            result.set_no_check_no_remove(Position(i, j), tmp);
        }
    }
    
    return result;
}

template <typename D, typename LD, typename A> 
ElemPtr<D> Matrix<D, LD, A>::operator*() const {
    return ElemPtr<D>{allocator_, row_};
}

template <typename D, typename LD, typename A>
Matrix<D, LD, A> Matrix<D, LD, A>::operator+(index k) const {
    return Matrix<D, LD, A>{this, row_ + k};
}

template <typename D, typename LD, typename A> 
ElemPtr<D> Matrix<D, LD, A>::operator[](index k) const {
    return ElemPtr<D>{allocator_, row_ + k};
}

template <typename D, typename LD, typename A> 
Matrix<D, LD, A>::~Matrix() {
    if (!copyed_)
        delete allocator_;
}