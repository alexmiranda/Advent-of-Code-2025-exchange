import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Main extends MySolutionBase {

    class RC {
        public int r;
        public int c;
        public RC(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    char[][] map;
    int map_rows;
    int map_cols;
    boolean[] row_empty;
    boolean[] col_empty;
    List<RC> stars = new ArrayList<>();
    long expansion =1;

    public Main(String inputFilename) {
        super(inputFilename);
        map_rows = getInputLinesAsList().size();
        map_cols = getInputLinesAsList().get(0).length();
        this.map = new char[map_rows][map_cols];
        this.row_empty = new boolean[map_rows];
        this.col_empty = new boolean[map_cols];
        Arrays.fill(this.row_empty,true);
        Arrays.fill(this.col_empty,true);
        for(int r=0;r<map_rows;r++ ) {
            var rowchars = getInputLinesAsList().get(r).toCharArray();
            for(int c=0;c<rowchars.length;c++) {
                map[r][c] = rowchars[c];
                if (map[r][c] =='#') {
                    this.stars.add(new RC(r,c));
                }
                if (map[r][c] !='.') {
                    this.row_empty[r] =false;
                    this.col_empty[c] =false;
                }
            }
        }
    }

    private long starDist(RC star1, RC star2) {
        int r1 = Integer.min(star1.r,star2.r);
        int r2 = Integer.max(star1.r,star2.r);
        int c1 = Integer.min(star1.c,star2.c);
        int c2 = Integer.max(star1.c,star2.c);
        long d = IntStream.range(r1,r2).mapToLong(r->this.row_empty[r]?this.expansion:1L).sum()+
               IntStream.range(c1,c2).mapToLong(c->this.col_empty[c]?this.expansion:1L).sum();
        //System.out.println(String.format("(%d,%d) -> (%d,%d) = %d",r1,c1,r2,c2,d));
        return d;
    }


    private Main play1() {
        this.expansion = 2;
        Long sumDist =this.stars.stream().mapToLong(star1->{
            return this.stars.stream().filter(star2->(star2.r>star1.r || (star2.r==star1.r && star2.c>star1.c))).mapToLong(star2->starDist(star1,star2)).sum();
        }).sum();
        System.out.println(sumDist);
        return this;
    }

    char[][] map2;
    private Main play2() {
        this.expansion = 1000000;
        Long sumDist =this.stars.stream().mapToLong(star1->{
            return this.stars.stream().filter(star2->(star2.r>star1.r || (star2.r==star1.r && star2.c>star1.c))).mapToLong(star2->starDist(star1,star2)).sum();
        }).sum();
        System.out.println(sumDist);
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
