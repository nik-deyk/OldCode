a = int(input())
b = int(input())
n = int(input())

b_group = b // n
if b % n != 0:
	b_group += 1

a_group = a

print("Yes" if a_group > b_group else "No")