from E_origin_solution import solve
from itertools import product

from progress.bar import IncrementalBar


TEST_NUM = 10**5
MAX_VALUE = 10


if __name__ == "__main__":
    bar = IncrementalBar('Countdown', max = TEST_NUM)

    i = 1

    with open("test.txt", "w") as f:
        f.write("DESCRIPTION /{Test suite for Ambulance task (see python 'vival' package at https://github.com/ViktorooReps/vival).}/\n")
        f.write("\n\n")
        for input in product(list(range(1, MAX_VALUE + 1)), repeat=5):
            k1, m, k2, p2, n2 = input
            res = solve(k1, m, k2, p2, n2)

            f.write(f"""Test {i}

INPUT
/{{{k1} {m} {k2} {p2} {n2}}}/

CMD
/{{-ea E}}/

OUTPUT 
/{{{res[0]} {res[1]}
}}/

""")
            bar.next()
            i+=1

    bar.finish()