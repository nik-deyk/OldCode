#include <iostream>
#include "include/SparseMatrix.h"

using namespace SM;

int main() {    
    SparseMatrix m1(10, 20); // матрица 10 на 20
    m1[4][2] = 42;
    *(*(m1 + 4) + 2) = 42;

    const SparseMatrix m2 = m1;

    [[maybe_unused]] double v1 = m2[4][2];
    [[maybe_unused]] double v2 = *(*(m2 + 4) + 2);
    
    SparseMatrix sum = m1 + m2;

    double v3 = m1.get(5, 5);
        std::cout << v3 << std::endl;

    bool bb = m1 == m2; //Seg fault
        std::cout << bb << std::endl;

    return 0;
}