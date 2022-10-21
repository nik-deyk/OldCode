a, x0, x1, p, deg = eval(input("Enter a, x0, x1, p, deg:"))

def get_back(num: int):
    num_orig = num
    p_copy = p
    q = list()
    while p_copy % num != 0:
        q.append(p_copy//num)
        r = p_copy % num
        p_copy = num
        num = r
    q.append(p_copy)
    p_prev = 1
    p_current = q[0]
    for i in range(1, len(q)):
        p_new = p_current*q[i]+p_prev
        p_prev = p_current
        p_current = p_new
    if (p_new != p):
        print(f"for {num} get last as {p_new}, where {p_current=} &{p_prev=}")
        raise RuntimeError("Imposssssssilble!!!?!")
    if (p_prev * num_orig % p == 1):
        return p_prev
    p_prev = p - p_prev
    if (p_prev * num_orig % p == 1):
        return p_prev
    raise RuntimeError("what!!!?!")

def power2(M: tuple) -> tuple:
    lambd = ((3 * M[0]**2 + a) * get_back(2*M[1])) % p
    x = (lambd**2 - 2 *M[0]) % p
    if x < 0:
        x += p
    y = (lambd*(M[0]-x)-M[1]) % p
    if y < 0:
        y += p
    return (x, y)

def add_M(M1: tuple, M2: tuple) -> tuple:
    numerator = (M2[1] - M1[1]) % p
    if numerator < 0:
        numerator += p
    denominator = M2[0] - M1[0]
    if denominator < 0:
        denominator += p
    if denominator != 0:
        lambd = numerator * get_back(denominator) % p
    else:
        if (M1[1]+M2[1] == p):
            return 1
        else:
            raise RuntimeError("Attempt to add 2 points with the same x and y1!=-y2")
    x = (lambd**2 - M2[0] - M1[0]) % p
    if x < 0:
        x += p
    y = (lambd*(M1[0] - x) - M1[1]) % p
    if y < 0:
        y += p
    return (x, y)

if __name__ == "__main__":
    pows = list()
    pow2 = 2
    while (pow2 < deg):
        pow2 *= 2
    deg_copy = deg
    while deg_copy != 0:
        while pow2 > deg_copy:
            pow2 //= 2
        deg_copy -= pow2
        pows.append(pow2)
    print(f"Will power M point to get this powers: {pows}")
    deg_copy = 0
    for i in pows:
        deg_copy += i
    if (deg_copy != deg):
        raise RuntimeError("Bad pows!")

    M = (x0 % p, x1 % p)
    M_pows = list()
    M_pows.insert(0, M)
    deg_copy = 1
    print(f"{deg_copy} M{M}")
    while deg_copy != pows[0]:
        M = power2(M)
        deg_copy *= 2
        if deg_copy in pows:
            M_pows.insert(0, M)
            print(f"{deg_copy} M{M}")

    M_sum = M_pows[0]
    for i in range(1, len(M_pows)):
        print(f"{M_sum} + {M_pows[i]} = ", end='')
        M_sum = add_M(M_sum, M_pows[i])
        print(f"{M_sum}")
