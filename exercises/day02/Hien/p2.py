input = """Game 1: 1 green, 6 red, 4 blue; 2 blue, 6 green, 7 red; 3 red, 4 blue, 6 green; 3 green; 3 blue, 2 green, 1 red
Game 2: 2 blue, 4 red, 7 green; 17 red, 3 blue, 2 green; 3 green, 14 red, 1 blue
"""
from functools import reduce
res = 0
for game in input.split("\n"):
    game_ =game.split(":")
    if len(game_) != 2:
        continue
    max_cubes = {'red': 0, 'green': 0, "blue": 0}
    for s in game_[1].split(";"):
        for s_result in s.strip().split(","):
            count, color = s_result.strip().split()
            max_cubes[color] = max(max_cubes[color], int(count))
    res += reduce(lambda a, b : a*b, max_cubes.values())
print(res)