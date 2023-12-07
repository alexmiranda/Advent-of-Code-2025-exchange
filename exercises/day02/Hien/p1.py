input = """Game 1: 1 green, 6 red, 4 blue; 2 blue, 6 green, 7 red; 3 red, 4 blue, 6 green; 3 green; 3 blue, 2 green, 1 red
"""

threshold = {'red': 12, 'green': 13, "blue": 14}
res = 0
for line in input.split("\n"):
    game_ =line.split(":")
    if len(game_) != 2:
        continue
    game_id = int(game_[0].split()[1])
    is_impossible = False
    for s in game_[1].split(";"):
        for s_result in s.strip().split(","):
            count, color = s_result.strip().split()
            if threshold[color]< int(count):
                is_impossible=True
                break
        if is_impossible:
            break
    if not is_impossible:
        res += game_id
print(res)