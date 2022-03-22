# funcs:

def half_but_to_big(n: int) -> int:
	k = n // 2
	if n % 2 != 0:
		k += 1
	return k


def get_submatrix() -> list:
	matrix = list()
	for i in range(k):
		line = str()
		for j in range(k):
			line += chr(ord('a') + abs(j - i) % (CHARS_IN_ALPHABET - 1))
		matrix.append(line)
	return matrix

def sim_submatrix_vertical(m: list) -> list:
	not_include_last_char = bool(n % 2)
	# print(not_include_last_char)
	for i, line in enumerate(m):
		m[i] = line + ''.join(reversed(line[:len(line)-int(not_include_last_char)]))
	return m

def sim_submatrix_horisontal(m : list) -> list:
	for j in range(k-1 - bool(n % 2), -1, -1):
		m.append(m[j])
	return m

# prog:

n = int(input())
k = half_but_to_big(n)
CHARS_IN_ALPHABET = 27

m = get_submatrix()
m = sim_submatrix_vertical(m)
m = sim_submatrix_horisontal(m)

for i in range(n):
	for j in range(n):
		print(m[i][j], end='')
	print()