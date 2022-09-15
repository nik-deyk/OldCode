# For C number.

import string
import random

def sh(l):
    new_l = list(l)
    random.shuffle(new_l)
    return new_l

for i in range(100):
    print(''.join(sh(string.digits)))