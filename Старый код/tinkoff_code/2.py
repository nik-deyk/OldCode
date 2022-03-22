from functools import cmp_to_key

table_of_comparations = dict()

class Letter:
	cymbol : str = ''

	def __init__(self, symbol):
		self.cymbol = symbol

def cmp_by_table(x: Letter, y: Letter):
	if (x.cymbol, y.cymbol) in table_of_comparations:
		return table_of_comparations[(x.cymbol, y.cymbol)]
	return -table_of_comparations[(y.cymbol, x.cymbol)]


for pair in [('a', 'b'), ('a', 'c'), ('b', 'c')]:
	cmp = input()
	if cmp == '>':
		table_of_comparations[pair] = 1
	elif cmp == '=': 
		table_of_comparations[pair] = 0
	else:
		table_of_comparations[pair] = -1

list_of_letters = [Letter(c) for c in 'abc']
list_of_letters = sorted(list_of_letters, key=cmp_to_key(cmp_by_table))

# Add all possible seqs :
pool_of_seqs = set()
pool_of_seqs.add(''.join([x.cymbol for x in list_of_letters]))
# debug: # print(pool_of_seqs)
for i in range(len(list_of_letters) - 1):
	for j in range(i + 1, len(list_of_letters)):
		if cmp_by_table(list_of_letters[i], list_of_letters[j]) == 0:
			for seq in tuple(pool_of_seqs):
				seq = seq[:i] + seq[j] + seq[i+1:j]+ seq[i] + seq[j+1:]
				pool_of_seqs.add(seq)

for seq in sorted(list(pool_of_seqs)):
	print(seq)