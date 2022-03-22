strr: str = input()

# remove 0-es from begging:
while strr.endswith('0'):
	strr = strr[:-1]

print(strr.count('0'))