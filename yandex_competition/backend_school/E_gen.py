import os
import random
from types import new_class

def check(myStr) -> bool:
    open_list = ["{"]
    close_list = ["}"]
    stack = []
    for i in myStr:
        if i in open_list:
            stack.append(i)
        elif i in close_list:
            pos = close_list.index(i)
            if ((len(stack) > 0) and
                (open_list[pos] == stack[len(stack)-1])):
                stack.pop()
            else:
                return False
    if len(stack) == 0:
        return True
    else:
        return False

def get_correct_answer(program_input) -> int:
    for i, letter in enumerate(program_input):
        if letter == '{' or letter == '}':
            new_str = program_input[0:i] + program_input[i+1:]
            if (check(new_str)):
                return i + 1
    
    return -1


while True:
    N = random.randint(4, 20)
    in_str = ''.join(random.choice("{} +") for _ in range(N))
    with open("input.txt", "w") as f:
        print(in_str, file=f)
    java_res = int(os.popen('java E').read().strip())
    python_res: int = get_correct_answer(in_str)
    if (java_res != python_res):
        print(repr(in_str), f"in java: {java_res} but in python: {python_res}")
