#!/usr/bin/python3

def gcd(a, b):
    if (a < b):
        a, b = b, a

    if (b):
        return gcd(b, a % b)
    else:
        return a


def get_primary_multiplyer(a, from_n):
    i = from_n
    while i < a:
        if (a % i == 0):
            break
        i += 1
    else:
        return (i, 1)

    n = 0
    while (a > 1):
        if (a % i != 0):
            return (i, n)
        a //= i
        n += 1

    return (i, n)

# MAIN
x = int(input())

initial_value = x

if (x <= 2):
    print("NO")
    quit()

a = get_primary_multiplyer(x, 2)
list_m = []
while (x != 1):
    x //= a[0] ** a[1]
    list_m.append(a)
    print("~~~~", a)
    a = get_primary_multiplyer(x, a[0])

u = list_m[0][1]
for q in list_m:
    u = gcd(u, q[1])

print("YES" if u > 1 else "NO")
# optional
print(list_m)
