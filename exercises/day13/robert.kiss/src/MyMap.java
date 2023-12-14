import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.IntStream;

public class MyMap {
    List<String> rows = new ArrayList<>();
    List<String> cols = null;

    public void addLine(String line) {
        this.rows.add(line);
    }
    private void calcCols(){
        if (cols == null) {
            cols = new ArrayList<>();
            IntStream.range(0,rows.get(0).length()).forEach(i->
                    cols.add(rows.stream().map(row->row.charAt(i)).collect(Collector.of(StringBuilder::new,StringBuilder::append,StringBuilder::append,StringBuilder::toString)))
            );
        }
    }

    private boolean checkReflection(List<String> lines, int first, int width, int error) {
        int diff = 0;
        for (int i=0;i<width/2;i++) {
            var line1 = lines.get(first+i);
            var line2 = lines.get(first+width-1-i);
            for (int j=0;j<line1.length();j++) {
                diff += line1.charAt(j)==line2.charAt(j) ? 0 : 1;
            }
            if (diff>error){
                return false;
            }
        }
        return diff == error;
    }

    public long findReflection(List<String> lines, int error) {
        long res = 0L;
        for (int width=2;width<=lines.size();width+=2) {
            res += checkReflection(lines,0,width,error) ? width/2 : 0;
        }
        for (int width=2;width<lines.size();width+=2) {
            res += checkReflection(lines,lines.size()-width,width,error) ? lines.size()-width/2 : 0;;
        }
        return res;
    }

    public long findReflection(List<String> lines) {
        return findReflection(lines, 0);
    }

    public long findSmudgedReflection(List<String> lines) {
        return findReflection(lines, 1);
    }

    public long solve1() {
        calcCols();
        return 100*findReflection(rows) + findReflection(cols);
    }

    public long solve2() {
        calcCols();
        return 100*findSmudgedReflection(rows) + findSmudgedReflection(cols);
    }

}
