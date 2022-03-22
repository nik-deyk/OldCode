# Source Generated with Decompyle++
# File: KeysVariants.cpython-39.pyc (Python 3.9)

'''
Module for brute forcing the key by possible values for its letters.
'''
from collections import defaultdict
from Words import Words

class KeyObtainer:
    ciphertext = bytes()
    key_letters = defaultdict()
    keys_with_plaintext = list()

    def decrypt(self = None, key = None):
        plaintext = bytearray(self.ciphertext)
        return bytes(plaintext)


    def get_first_key_and_indexes(self = None):
        return (None((lambda .0 = None: [ self.key_letters.get(i)[0] for i in .0 ])(self.key_letters)), [
            0] * len(self.key_letters))


    def next_key(self = None, key = None, indexes = None):
        i = 0
        if i < len(key):
            if indexes[i] == len(self.key_letters.get(i)) - 1:
                indexes[i] = 0
                key[i] = self.key_letters.get(i)[0]
                i += 1
                continue
            indexes[i] += 1
            key[i] = self.key_letters.get(i)[indexes[i]]

        return (None, None)
        return (key, indexes)


    def key_words_valid(self = None, key = None, w = None):
        plaintext = self.decrypt(key).decode('ignore', **('errors',))
        return (w.words_valid(plaintext), plaintext)


    def try_all_keys(self):
        (current_key, key_letter_variants) = self.get_first_key_and_indexes()
        w = Words()
        w.load_words()
        if current_key != None:
            (key_validity, plaintext) = self.key_words_valid(current_key, w)
            if key_validity:
                self.keys_with_plaintext.append((current_key, plaintext))
            (current_key, key_letter_variants) = self.next_key(current_key, key_letter_variants)
            continue


    def __init__(self = None, ciphertext = None, letters = None):
        self.ciphertext = ciphertext
        self.key_letters = letters
        self.try_all_keys()

