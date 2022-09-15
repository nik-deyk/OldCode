import random
import string

N = 30
COUNT = 40_000

with open("test.txt", "w") as f:
    print(COUNT, file=f)
    for i in range(COUNT):
        print(''.join(random.choice(string.ascii_lowercase) for _ in range(N)), file=f)
