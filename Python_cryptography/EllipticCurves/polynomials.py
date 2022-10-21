# field is 2^5.

pows: dict = dict()

class Polynomial:
    coefficients: list = list()

    def __init__(self, *args):
        self.coefficients = list(args)
        if len(self.coefficients) != 5:
            raise RuntimeError("Bad size of coefficients array!")

    def __add__(self, other):
        result_coefficients = [(a + b) % 2 for (a, b) in
                                zip(self.coefficients, other.coefficients)]
        return Polynomial(*result_coefficients)

    def __mul__(self, other):
        a1, a2, a3, a4, a5 = self.coefficients
        b1, b2, b3, b4, b5 = other.coefficients
        coefficient_8 = (a1*b1) % 2
        coefficient_7 = (a1*b2 + a2*b1) % 2
        coefficient_6 = (a1*b3 + a2*b2 + a3*b1) % 2
        coefficient_5 = (a1*b4 + a2*b3 + a3*b2 + a4*b1) % 2
        result_coefficients = [0] * 5
        result_coefficients[0] = (a1*b5 + a5*b1 + a2*b4 +
                                  a4*b2 + a3*b3 + coefficient_7) % 2
        result_coefficients[1] = (a2*b5 + a3*b4 + a4*b3 +
                                  a5*b2 + coefficient_6 + coefficient_8) % 2
        result_coefficients[2] = (a3*b5 + a4*b4 + a5*b3 +
                                  coefficient_5 + coefficient_7 +
                                  coefficient_8) % 2
        result_coefficients[3] = (a4*b5 + a5*b4 + coefficient_6) % 2
        result_coefficients[4] = (a5*b5 + coefficient_5 +
                                  coefficient_8) % 2
        return Polynomial(*result_coefficients)

    def __pow__(self, other):
        if type(other) != type(int()) or other < 0:
            raise RuntimeError("Can power only positive integer!")
        if other == 1:
            return self
        return self * self.__pow__(other - 1)

    def trace(self):
        return self + self**2 + self**4 + self**8 + self**16

    def is_zero(self):
        return self.coefficients == [0,0,0,0,0]

    def __str__(self):
        result_string = ""
        have_something: bool = False
        if (self.coefficients[0] != 0):
            result_string += "θ^4"
            have_something = True
        if (self.coefficients[1] != 0):
            if have_something:
                result_string += " + "
            result_string += "θ^3"
            have_something = True
        if (self.coefficients[2] != 0):
            if have_something:
                result_string += " + "
            result_string += "θ^2"
            have_something = True
        if (self.coefficients[3] != 0):
            if have_something:
                result_string += " + "
            result_string += "θ"
            have_something = True
        if (self.coefficients[4] != 0):
            if have_something:
                result_string += " + "
            result_string += "1"
        return result_string if result_string != "" else "0"

    def __eq__(self, other):
        if not isinstance(other, Polynomial):
            raise TypeError("Операнд справа должен иметь тип Polynomial")
        return self.coefficients == other.coefficients

    def get_pow(self):
        for k, v in pows.items():
            if v == self:
                return k
        raise RuntimeError(f"Не нашел себя в карте! + {self}")

    def simple_str(self):
        return f"θ^{self.get_pow()}"

    def get_reverse(self):
        my_pow = self.get_pow()
        return pows[2**5 - 1 - my_pow]
        

class EllepticDot:
    x: Polynomial = None
    y: Polynomial = None

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        lambd = (self.y + other.y) * (self.x + other.x).get_reverse()
        x3 = lambd**2 + self.x + other.x
        return EllepticDot(x3, lambd*(self.x + x3) + self.y + Polynomial(0,0,0,0,1)) # a = 1

    def pow2(self):
        lambd = self.x**2
        x3 = lambd**2
        return EllepticDot(x3, lambd*(self.x + x3) + self.y + Polynomial(0,0,0,0,1))

    def __pow__(self, other):
        if not isinstance(other, int) or other <= 0:
            raise TypeError("Операнд справа должен иметь тип положительного int")
        if (other == 1):
            return self
        if other % 2 == 0:
            return (self**(other // 2)).pow2()
        return (self**(other // 2)).pow2() + self

    def __str__(self):
        return f"M({self.x.simple_str()}, {self.y.simple_str()})"

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

if __name__ == "__main__":
    unit = Polynomial(0,0,0,0,1)
    p = unit
    theta = Polynomial(0,0,0,1,0)
    current_pow = 0
    while current_pow < 2**5:
        pows[current_pow] = p
        p = p * theta
        current_pow += 1
    # map is filled out

    unit = Polynomial(0,0,0,0,1)
    p = unit
    theta = Polynomial(0,0,0,1,0)
    current_pow = 0
    while current_pow < 2**5:
        # print(f"Tr(θ^{current_pow} = {str(p)}) = {str(p.trace())}")
        pows[current_pow] = p
        if 24 <= current_pow <= 30:
            if p.trace().is_zero():
                # print(f"Found zero trace, power is θ^{current_pow}")
                x = p
                y = x**2 + x**8
                e1 = EllepticDot(x, y)
                e2 = EllepticDot(x, y + unit)
                # print(f"M1 = {e1}, M2 = {e2}")
                if (e1**4 == e1):
                    print(f"{e1} is of pow 3")
                if (e2**4 == e2):
                    print(f"{e2} is of pow 3")
                if (e1**12 == e1):
                    print(f"{e1} is of pow 11")
                if (e2**12 == e2):
                    print(f"{e2} is of pow 11")
                # print(f"{e1**4} == {e1} ?")
            
        p = p * theta
        current_pow += 1
