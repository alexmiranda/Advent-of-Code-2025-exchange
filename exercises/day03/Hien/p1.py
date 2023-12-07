input="""...........441.................367................296........................................567..47.....45.................947.............
...606..........888.....................508..........*892................+..=138.381..967...............*....%......926...........218......."""

import re
data = input.split()
n_rows = len(data)
n_cols = len(data[0])
markings = [[0 for i in range(n_cols)] for j in range(n_rows)]

def set_mark(i,j):
    for ii in (i-1,i,i+1):
        for jj in (j-1,j,j+1):
            if 0<=ii<n_rows and 0<=jj<n_cols:
                markings[ii][jj]=1
for i in range(n_rows): 
    for j in range(n_cols):
        if not data[i][j].isdigit() and data[i][j] != ".":
            set_mark(i,j)
result = 0
for idx in range(n_rows):
    row = data[idx]
    pattern = r"\d+"
    res = re.finditer(pattern, row)
    for r in res:
        start, end = r.start(), r.end()
        if sum(markings[idx][start:end]) > 0:
            result += int(data[idx][start:end])

print(result)