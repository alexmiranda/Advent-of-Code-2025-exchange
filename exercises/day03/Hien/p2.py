input="""...........441.................367................296........................................567..47.....45.................947.............
...606..........888.....................508..........*892................+..=138.381..967...............*....%......926...........218......."""

import re
data = input.split()
n_rows = len(data)
n_cols = len(data[0])
markings_count = [[0 for i in range(n_cols)] for j in range(n_rows)]
markings_number = [[1 for i in range(n_cols)] for j in range(n_rows)]
result = 0

def set_mark(col_start, col_end ,row, value):
    for jj in range(col_start-1,col_end+1):
        for ii in (row-1,row,row+1):
            if 0<=ii<n_rows and 0<=jj<n_cols:
                markings_number[ii][jj] *= value
                markings_count[ii][jj] += 1

for idx, row in enumerate(data):
    pattern = r"\d+"
    res = re.finditer(pattern, row)
    for r in res:
        start, end = r.start(), r.end()
        value = int(row[start:end])
        set_mark(start, end, idx, value)

for i in range(n_rows):
    for j in range(n_cols):
        if data[i][j] == "*" and markings_count[i][j]==2:
            result += markings_number[i][j] 

print(result)