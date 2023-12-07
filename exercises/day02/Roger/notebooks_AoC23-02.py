#!/usr/bin/env python
# coding: utf-8

import re
from functools import reduce, partial

def gameset_to_dict(xs):
    return {color: int(quantity) for color, quantity in map(lambda s: s.split()[::-1], xs)}

assert {"blue": 3, "red": 4} == gameset_to_dict(['3 blue', ' 4 red'])

def parse_line(s):
    id = re.findall(r"Game (\d+)", s)[0]
    picks = re.findall(r"[:;]([^;]+)", s)
    picks = list(map(gameset_to_dict, (map(lambda s: s.strip().split(','), picks))))
    return {"id": int(id), "picks": picks}

assert {'id': 1, 'picks': [{'blue': 3, 'red': 4}, {'red': 1, 'green': 2, 'blue': 6}, {'green': 2}]} \
    == parse_line("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")

def possible_pick(ref, d):
    res = True
    for k in d.keys():
        res &= ref[k] >= d[k]
    return res
    
assert True == possible_pick({"red": 12, "green": 13, "blue": 14}, {'blue': 3, 'red': 4})
assert False == possible_pick({"red": 12, "green": 13, "blue": 14}, {'blue': 3, 'red': 14})

def picks_possible(ref, ds):
    return reduce(lambda a, b: a & b, map(partial(possible_pick, ref), ds))

assert True == picks_possible({"red": 12, "green": 13, "blue": 14}, [{'blue': 3, 'red': 4}, {'red': 1, 'green': 2, 'blue': 6}, {'green': 2}])

def part1(lines):
    possible_games = filter(lambda d: picks_possible({"red": 12, "green": 13, "blue": 14}, d["picks"]), map(parse_line, lines))
    return sum(map(lambda d: d["id"], possible_games))

test_lines = '''Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green'''.splitlines()
assert 8 == part1(test_lines)

print(part1(open("input_d2p1.txt").readlines()))


def select_max(d1, d2):
    res = {}
    for color in set(list(d1.keys()) + list(d2.keys())):
        res[color] = max([d1.get(color, 0), d2.get(color, 0)])
    return res

def bag_minima(ds):
    return reduce(select_max, ds)

assert {'blue': 6, 'red': 4, 'green': 2} == bag_minima([{'blue': 3, 'red': 4}, {'red': 1, 'green': 2, 'blue': 6}, {'green': 2}])

def bag_power(d):
    return reduce(lambda a, v: a * v, d.values())

assert 48 == bag_power({'blue': 6, 'red': 4, 'green': 2})

def part2(lines):
    return sum(map(lambda d: bag_power(bag_minima(d["picks"])), map(parse_line, lines)))
    
assert 2286 == part2(test_lines)

print(part2(open("input_d2p1.txt").readlines()))
