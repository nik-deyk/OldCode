n, h, m, k = eval(input().replace(' ', ','))

time_table = list()

for i in range(n):
	hh, mm = eval(input().replace(' ', ','))
	time_table.append(hh * m + mm)

results : list = [0,] * (m //2)

def not_good_trains(t):
	times = list()
	akk = t
	while akk < h * m:
		times.append((akk, akk + k))
		akk += m//2

	tr = 0
	for t in time_table:
		for ran in times:
			if ran[0] < t < ran[1]:
				tr +=1 
				break

	return tr

for t in range(len(results)):
	results = not_good_trains(t)

o = min(results)

print(results.index(o), o)