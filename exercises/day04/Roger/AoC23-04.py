#!/usr/bin/env python
# coding: utf-8

# In[27]:


import re
from math import pow
from functools import partial

test_input = '''Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11'''

def parse_line(lines):
    return [set(re.findall(r"\d+", ns)) for ns in lines.split(":")[1].split(" | ")]

def matching_numbers(sets):
    return sets[0] & sets[1]

def part1(lines):
    return sum(list(map(lambda n: pow(2, n-1) ,filter(lambda n: n > 0, (map(lambda l: len(matching_numbers(parse_line(l))), lines))))))

assert 13 == part1(test_input.splitlines())

part1(open("input_d4.txt").readlines())


# In[67]:


def following_card_numbers(i, xs):
    return {i+n for n in range(1, len(xs)+1)}

def index_won(lines):
    return {i+1: following_card_numbers(i+1, won) for i, won in enumerate(map(matching_numbers, list(map(parse_line, lines))))}

def progressive_add(index):
    acc = {}
    keys_asc = list(index.keys())
    keys_asc.sort()
    for n in keys_asc:
        acc[n] = acc.get(n, 0) + 1
        for other in index[n]:
            acc[other] = acc.get(other, 0) + acc[n] 
    return acc

assert {1: 1, 2: 2, 3: 4, 4: 8, 5: 14, 6: 1} == progressive_add(index_won(test_input.splitlines()))


# In[69]:


def part2(lines):
    return sum(progressive_add(index_won(lines)).values())

part2(open("input_d4.txt").readlines())


# In[ ]:




