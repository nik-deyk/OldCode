# Source Generated with Decompyle++
# File: GetKey.cpython-39.pyc (Python 3.9)

'''
Module for guess the key of XOR EBC chifer.
'''
import string
import re
from progress.bar import ShadyBar
from Bytes import *

class NoKeysException(Exception):
    '''
\tException signilized that there were all keys counted.
\t'''
    pass


class GuessKey:
    '''
\tClass for obtaining the key.
\t'''

    class Words:
        __qualname__ = 'GuessKey.Words'
        __doc__ = '\n\t\tClass for checking for valid english words.\n\t\t'
        SYMBOLS = set(string.printable.encode('utf-8'))
        VALID_WORDS_PROPORTION = 0.7
        en_words = list()

        def load_words():
            '''
\t\t\tLoad the list of words from the local file.
\t\t\t'''
            file_with_words = open('corncob_caps.txt', 'r')
            file_with_words.close()
            GuessKey.Words.en_words = set(GuessKey.Words.en_words)


        def string_remove_nonletters(string_with_different_characters = None):
            return string_with_different_characters


        def string_to_words_list(string_of_words = None):
            string_of_letters_and_spaces = GuessKey.Words.string_remove_nonletters(string_of_words)
            return re.split('\\s+', string_of_letters_and_spaces)


        def words_valid(plaintext = None):
            '''
\t\t\tCheck that many words across the plaintext are valid english words.
\t\t\t'''
            words = GuessKey.Words.string_to_words_list(plaintext.upper())
            valid_words_count = 0
            return valid_words_count / len(words) >= GuessKey.Words.VALID_WORDS_PROPORTION


    ciphertext = bytes()
    valid_keys_and_plaintext = list()

    def decrypt(self = None, key = None):
        '''
\t\tReturn decrypted ciphertext with the key.
\t\t'''
        plaintext = bytearray(self.ciphertext)
        return bytes(plaintext)


    def next_key(self = None, key = None):
        '''
\t\tReturn next key in such a way that plaintext will contain only valid characters.
\t\tRaise exception if no keys left.
\t\t'''
        next_key = bytearray(key)
        i = 0
        shift_next = False
        if i < len(next_key):
            shift_next = next_key[i] == 255
            if shift_next:
                next_key[i] = 0
            else:
                next_key[i] += 1
            if not self.key_letter_valid(next_key[i], i, len(next_key)):
                if next_key[i] == 255:
                    if shift_next:
                        raise NoKeysException()
                    shift_next = True
                    next_key[i] = 0
                    continue
                next_key[i] += 1
                continue
            if not shift_next:
                pass

            i += 1
            shift_next = False
            continue
        if i == len(next_key):
            raise NoKeysException()
        return None(next_key)


    def key_letter_valid(self = None, letter = None, index = None, length = {
        'letter': int,
        'index': int,
        'length': int,
        'return': bool }):
        i = index
        if i < len(self.ciphertext):
            if self.ciphertext[i] ^ letter not in GuessKey.Words.SYMBOLS:
                return False
            None += length
            continue
        return True


    def get_init_key(self = None, key_length = None):
        key = bytearray(b'\x00' * key_length)
        key[i] += 1
        continue
        continue
        return bytes(key)


    def key_valid(self = None, key = None):
        plaintext = self.decrypt(key)
        if not GuessKey.Words.words_valid(plaintext.decode()):
            return (False,)
        return (None, plaintext)


    def next_valid_key(self = None, key = None):
        '''
\t\tReturn next key that will decrypt the ciphertext to printable characters.
\t\t'''
        next_key = self.next_key(key)
        validity_and_plaintext = self.key_valid(next_key)
        if not validity_and_plaintext[0]:
            next_key = self.next_key(next_key)
            validity_and_plaintext = self.key_valid(next_key)
            continue
        return (next_key, validity_and_plaintext[1])


    def init_fields(self = None, c = None):
        GuessKey.Words.load_words()
        self.ciphertext = c


    def __init__(self = None, c = None, key_length = None):
#Unsupported opcode: JUMP_IF_NOT_EXC_MATCH
        self.init_fields(c)
        current_key = self.get_init_key(key_length)
    # WARNING: Decompyle incomplete

