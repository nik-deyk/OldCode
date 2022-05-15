#!/usr/bin/env python3
# coding: utf-8
import sys
import os
from progress.bar import IncrementalBar
from decrypt import decrypt

integrity_check_suffix = [b'whatislo', b'vebabydo', b'nthurtme', b'donthurt', b'menomore']
MASK32 = 2**32

# байтовую строку в пару uint32 в big endian представлении
def bytes2pair(s):
    s = s[:4], s[4:]
    x = int.from_bytes(s[0], 'big')
    y = int.from_bytes(s[1], 'big')

    return x, y

# uint32 в байтовую строку в big endian представлении
def pair2bytes(x, y):
    s1 = bytes.fromhex(hex(x)[2:])
    s2 = bytes.fromhex(hex(y)[2:])

    return s1 + s2

def pair2str(x, y):
    return pair2bytes(x, y).decode()

# перевести hex-строку в пару uint32 в big endian представлении
def hex2pair(s):
    s = s[:8], s[8: 16]
    x = int(s[0], 16)
    y = int(s[1], 16)

    return x, y

def get_key(encrypted_pair, original_pair) -> list:
    delta = 0x9e3779b9

    v0, v1 = encrypted_pair
    b0, b1 = original_pair

    maybe_key = [0]*4

    found = False

    bar = IncrementalBar('First part', max = 256**2)

    for i in range(0, 256):
        for j in range(0, 256):
            hard_formula = (v1 - ((v0 << 4) ^ (i << 4) ^ (v0 + delta) ^ (v0 >> 5) ^ (j >> 5))) % MASK32
            bar.next()
            if hard_formula == b1:
                maybe_key[1] = i
                maybe_key[3] = j
                v1 = hard_formula
                found = True
                break

    bar.finish()

    if not found:
        raise RuntimeError("Can not get first pair!")
    found = False

    bar = IncrementalBar('Second part', max = 256**2)

    for i in range(0, 256):
        for j in range(0, 256):
            hard_formula = (v0 - ((v1 << 4) ^ (i << 4) ^ (v1 + delta) ^ (v1 >> 5) ^ (j >> 5))) % MASK32
            bar.next()
            if hard_formula == b0:
                maybe_key[0] = i
                maybe_key[2] = j
                v0 = hard_formula
                found = True
                break

    bar.finish()

    if not found:
        raise RuntimeError("Can not get second pair!")

    return maybe_key

def get_plaintext(key, ciphertext):
    plaintext = b''
    for i in range(0, len(ciphertext), 16):
        pair = hex2pair(ciphertext[i: i + 16])
        dec = decrypt(key, pair)
        plaintext += pair2bytes(*dec)

    if plaintext[-8:] not in integrity_check_suffix:
        raise RuntimeError("Plaintext have wrong ender!")

    return plaintext[:-8]


if __name__ == "__main__":
    orig_ciphertext = input("Input your text to decrypt in hex: ")

    if len(orig_ciphertext) % 16 != 0:
        print("You paste cipher text of length: ", len(orig_ciphertext))
        raise NotImplementedError("Ciphertext should be divisible by 8 (or 16 hex chars)")

    ciphertext = orig_ciphertext[-16:]
    print("Get last 16 bytes of ciphertext: ", ciphertext)

    cipher_pair = hex2pair(ciphertext)
    print("Get the pair of the last 16 bytes of the ciphertext: ", cipher_pair)

    for k, ender in enumerate(integrity_check_suffix):
        print(f"[{k}] Trying to guess: {ender}")
        must_be_pair = bytes2pair(ender)
        try:
            key = get_key(cipher_pair, must_be_pair)
            print(get_plaintext(key, orig_ciphertext))
        except RuntimeError as e:
            print("-")


'''
    plaintext = b''
    for i in range(0, len(ciphertext), 16):
        dec = decrypt(key, pair)
        plaintext += pair2bytes(*dec)

    assert plaintext[-8:] in integrity_check_suffix, "Something went wrong :("

    print(plaintext[:-8])
'''