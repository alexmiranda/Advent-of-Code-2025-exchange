import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main extends MySolutionBase {


    private List<SpringRow> springRows;

    public Main(String inputFilename) {
        super(inputFilename);
    }

    private Main play1() {
        this.springRows = getInputLinesAsStream().map(l->new SpringRow(l)).collect(Collectors.toList());
        long sum = springRows.stream().mapToLong(SpringRow::countMatches).sum();
        System.out.println(sum);
        return this;
    }

    private String unfold(String line) {
        return ("?"+line.split(" ")[0]).repeat(5).substring(1)
                +" "
                +(","+line.split(" ")[1]).repeat(5).substring(1);
    }
    private Main play2() {
        this.springRows = getInputLinesAsStream()
                .map(this::unfold)
                .map(l->new SpringRow(l)).collect(Collectors.toList());
        long sum = springRows.stream().mapToLong(SpringRow::countMatches).sum();
        System.out.println(sum);
        System.out.println();
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
