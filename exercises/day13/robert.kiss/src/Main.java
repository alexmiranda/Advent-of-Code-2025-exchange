import java.util.ArrayList;
import java.util.List;

public class Main extends MySolutionBase {

    private final List<MyMap> myMaps = new ArrayList<>();

    public Main(String inputFilename) {
        super(inputFilename);
        MyMap myMap = null;
        for(String line:getInputLinesAsList()) {
            if (line.isEmpty()) {
                myMap = null;
            } else {
                if (myMap==null) {
                    myMap = new MyMap();
                    this.myMaps.add(myMap);
                }
                myMap.addLine(line);
            }
        }
    }

    private Main play1() {
        long result = this.myMaps.stream().mapToLong(m->m.solve1()).sum();
        System.out.println(result);
        return this;
    }

    private Main play2() {
        long result = this.myMaps.stream().mapToLong(m->m.solve2()).sum();
        System.out.println(result);
        return this;
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
