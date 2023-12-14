import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main extends MySolutionBase {


    char[][] map;
    int start_x;
    int start_y;
    char start_dir;

    public Main(String inputFilename) {
        super(inputFilename);
        this.map = new char[getInputLinesAsList().get(0).length()][getInputLinesAsList().size()];
        for(int y=0;y<getInputLinesAsList().size();y++ ) {
            var line = getInputLinesAsList().get(getInputLinesAsList().size()-y-1).toCharArray();
            for(int x=0;x<line.length;x++) {
                map[x][y] = line[x];
                if (line[x] == 'S') {
                    start_x=x;
                    start_y=y;
                }
            }
        }
        this.start_dir = ( map[start_x+1][start_y] == '-' || map[start_x+1][start_y] == '7' || map[start_x+1][start_y] == 'J' ) ? 'R' :
                ( map[start_x][start_y+1] == '|' || map[start_x][start_y+1] == '7' || map[start_x][start_y+1] == 'F' ) ? 'U' : 'L';
    }


    Map<Character,Map<Character,Character>> dirMap = Map.of(
            'R',Map.of('J','U','-','R','7','D','S',' '),
            'U',Map.of('7','L','|','U','F','R','S',' '),
            'L',Map.of('F','D','-','L','L','U','S',' '),
            'D',Map.of('L','R','|','D','J','L','S',' ')
            );
    Map<Character,Map<Character,Integer>> turnMap = Map.of(
            'R',Map.of('J',-1,'-',0,'7',1,'S',0),
            'U',Map.of('7',-1,'|',0,'F',1,'S',0),
            'L',Map.of('F',-1,'-',0,'L',1,'S',0),
            'D',Map.of('L',-1,'|',0,'J',1,'S',0)
    );

    private Main play1() {
        int x = start_x;
        int y = start_y;
        char dir = start_dir;

        int dist=0;
        while (dist==0||map[x][y]!='S') {
            //System.out.println(String.format("%d,%d dir: %c",x,y,dir));
            x = x + (dir=='R' ? 1 : 0) + (dir=='L' ? -1 : 0);
            y = y + (dir=='U' ? 1 : 0) + (dir=='D' ? -1 : 0);
            dir = dirMap.get(dir).get(map[x][y]);
            dist+=1;
        };
        System.out.println(dist/2);
        return this;
    }

    char[][] map2;
    private Main play2() {
        this.map2 = new char[this.map.length][this.map[0].length];
        for(int y=0; y<this.map2[0].length; y++ ) {
            for (int x = 0; x<this.map2.length; x++) {
                this.map2[x][y]='.';
            }
        }

        int x = start_x;
        int y = start_y;
        char dir = start_dir;
        int turn_sum = 0;

        int dist=0;
        while (dist==0||map[x][y]!='S') {
            map2[x][y]='X';
            switch (dir) {
                case 'U':
                    switch (map[x][y]) {
                        case 'L':
                            color(x-1,y,'L');
                            color(x,y-1,'L');
                            break;
                        case '|':
                            color(x-1,y,'L');
                            color(x+1,y,'R');
                            break;
                        case 'J':
                            color(x+1,y,'R');
                            color(x,y-1,'R');
                            break;
                    }
                    break;
                case 'R':
                    switch (map[x][y]) {
                        case 'L':
                            color(x,y-1,'R');
                            color(x-1,y,'R');
                            break;
                        case '-':
                            color(x,y-1,'R');
                            color(x,y+1,'L');
                            break;
                        case 'F':
                            color(x,y+1,'L');
                            color(x-1,y,'L');
                            break;
                    }
                    break;
                case 'D':
                    switch (map[x][y]) {
                        case 'F':
                            color(x,y+1,'R');
                            color(x-1,y,'R');
                            break;
                        case '|':
                            color(x-1,y,'R');
                            color(x+1,y,'L');
                            break;
                        case '7':
                            color(x+1,y,'L');
                            color(x,y+1,'L');
                            break;
                    }
                    break;
                case 'L':
                    switch (map2[x][y]) {
                        case '7':
                            color(x+1,y,'R');
                            color(x,y+1,'R');
                            break;
                        case '-':
                            color(x,y-1,'L');
                            color(x,y+1,'R');
                            break;
                        case 'J':
                            color(x+1,y,'L');
                            color(x,y-1,'L');
                            break;
                    }
                    break;
            }
            //System.out.println(String.format("%d,%d dir: %c",x,y,dir));
            x = x + (dir=='R' ? 1 : 0) + (dir=='L' ? -1 : 0);
            y = y + (dir=='U' ? 1 : 0) + (dir=='D' ? -1 : 0);
            turn_sum += turnMap.get(dir).get(map[x][y]);
            dir = dirMap.get(dir).get(map[x][y]);
            dist+=1;
        };


        boolean change = true;
        while (change) {
            change = false;
            for (int yy = 0; yy < this.map2[0].length; yy++) {
                for (int xx = 0; xx < this.map2.length; xx++) {
                    var c = this.map2[xx][yy];
                    if (c == 'R' || c == 'L') {
                        change |= color(xx + 1, yy, c);
                        change |= color(xx - 1, yy, c);
                        change |= color(xx, yy + 1, c);
                        change |= color(xx, yy - 1, c);
                    }
                }
            }
        }
        int cR = 0;
        int cL = 0;
        for (int yy = 0; yy < this.map2[0].length; yy++) {
            for (int xx = 0; xx < this.map2.length; xx++) {
                if (this.map2[xx][yy]=='R') {
                    cR += 1;
                }
                if (this.map2[xx][yy]=='L') {
                    cL += 1;
                }
            }
        }

        System.out.println("cR:"+cR);
        System.out.println("cL:"+cL);
        System.out.println("turn_sum="+turn_sum);
        System.out.println(turn_sum >0?cR:cL);
        return this;
    }

    private boolean color(int x, int y, char c) {
        if (x>=0 && x < map2.length && y>=0 && y<map2[0].length) {
            if (map2[x][y]=='.'){
                map2[x][y]=c;
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        try {
            new Main("sample.txt").play1().play2();
            new Main("input.txt").play1().play2();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
