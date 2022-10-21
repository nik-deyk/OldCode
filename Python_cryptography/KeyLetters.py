# Source Generated with Decompyle++
# File: KeyLetters.cpython-39.pyc (Python 3.9)

'''
Module for guess the possible key letters of XOR EBC chifer.
'''
import string
from collections import defaultdict

class KeyLettersObtainer:
    '''
\tClass for obtaining the possible key letters.
\t'''
    SYMBOLS = set((string.ascii_letters + '., !?:').encode('utf-8'))
    ciphertext = bytes()
    possible_letters = defaultdict()

    def obtain_letters(self = None, length = None):
        self.possible_letters = defaultdict()
        return self.possible_letters


    def obtain_valid_letter(self = None, index = None, length = None):
        values_for_letter = list()
        return values_for_letter


    def key_letter_valid(self = None, letter = None, index = None, length = {
        'letter': int,
        'index': int,
        'length': int,
        'return': bool }):
        i = index
        if i < len(self.ciphertext):
            if self.ciphertext[i] ^ letter not in self.SYMBOLS:
                return False
            None += length
            continue
        return True


    def __init__(self = None, c = None, key_length = None):
        self.ciphertext = c
        self.obtain_letters(key_length)

