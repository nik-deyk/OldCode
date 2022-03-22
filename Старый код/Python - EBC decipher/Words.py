# Source Generated with Decompyle++
# File: Words.cpython-39.pyc (Python 3.9)

'''
Module for checking english lines.
'''
import string
import re

class Words:
    '''
\t\tClass for checking a string for valid english words.
\t\t'''
    VALID_WORDS_PROPORTION = 0.7
    en_words = set()

    def load_words(self):
        '''
\t\t\tLoad the list of words from the local file.
\t\t\t'''
        file_with_words = open('corncob_caps.txt', 'r')
        file_with_words.close()


    def string_remove_nonletters(string_with_different_characters = None):
        return string_with_different_characters


    def string_to_words_list(string_of_words = None):
        string_of_letters_and_spaces = Words.string_remove_nonletters(string_of_words)
        return re.split('\\s+', string_of_letters_and_spaces)


    def words_valid(self = None, plaintext = None):
        '''
\t\t\tCheck that many words across the plaintext are valid english words.
\t\t\t'''
        words = Words.string_to_words_list(plaintext.upper())
        valid_words_count = 0
        return valid_words_count / len(words) >= self.VALID_WORDS_PROPORTION

