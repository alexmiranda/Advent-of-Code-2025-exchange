import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2023/day/02
 */
public class Y23Day02Visualization {

	private static Y23GUIOutput02 output;
	
	
	/*
	 * example input: 
	 *
	 * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
	 * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
	 * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
	 * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
	 * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
	 * 
	 */

	private static final String INPUT_RX   = "^Game ([0-9]+): ([0-9a-z ,;]+)*+$";
	private static final String INPUT_RX_DICECOLOR   = "^([0-9]+) (red|green|blue)$";
	
	enum DCOLOR {red, green, blue}
	
	static record DiceCount(int cnt, DCOLOR color) {
		@Override public String toString() { return cnt + " " +color; }

		public Object toColoredString(Map<DCOLOR, Integer> maxDicesPerColor, String highlightColor, String normalColor) {
			String result = toString();
			if (cnt > maxDicesPerColor.get(color)) {
				result = highlightColor + result + normalColor;
			}
			return result;
		}
	}
	
	public static class InputData {
		int gameId;
		List<List<DiceCount>> shownDiceSets = new ArrayList<>();
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("Game ").append(Integer.toString(gameId)).append(": ");
			String seperatorSets = "";
			for (List<DiceCount> shownDiceSet:shownDiceSets) {
				result.append(seperatorSets);
				seperatorSets = "; ";
				String seperatorCount = "";
				for (DiceCount showDiceCount:shownDiceSet) {
					result.append(seperatorCount);
					seperatorCount = ", ";
					result.append(showDiceCount.toString());
				}
			}
			return result.toString();
		}
		public String  toColoredString(Map<DCOLOR, Integer> maxDicesPerColor, String highlightColor, String normalColor) {
			StringBuilder result = new StringBuilder();
			result.append("Game ").append(Integer.toString(gameId)).append(": ");
			String seperatorSets = "";
			for (List<DiceCount> shownDiceSet:shownDiceSets) {
				result.append(seperatorSets);
				seperatorSets = "; ";
				String seperatorCount = "";
				for (DiceCount showDiceCount:shownDiceSet) {
					result.append(seperatorCount);
					seperatorCount = ", ";
					result.append(showDiceCount.toColoredString(maxDicesPerColor, highlightColor, normalColor));
				}
			}
			return result.toString();
		}
	}
	
	public static class InputProcessor implements Iterable<InputData>, Iterator<InputData> {
		private Scanner scanner;
		public InputProcessor(String inputFile) {
			try {
				scanner = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		@Override public Iterator<InputData> iterator() { return this; }
		@Override public boolean hasNext() { return scanner.hasNext(); }
		@Override public InputData next() {
			String line = scanner.nextLine().trim();
			while (line.length() == 0) {
				line = scanner.nextLine();
			}
			InputData data = new InputData();
			if (line.matches(INPUT_RX)) {
				data.gameId = Integer.parseInt(line.replaceFirst(INPUT_RX, "$1"));
				for (String revealed:line.replaceFirst(INPUT_RX, "$2").split(";")) {
					List<DiceCount> shownDiceSet = new ArrayList<>();
					for (String shownDiceColor:revealed.split(",")) {
						shownDiceColor = shownDiceColor.trim();
						if (!shownDiceColor.matches(INPUT_RX_DICECOLOR)) {
							throw new RuntimeException("invalid show dice color '"+shownDiceColor+"'");
						}
						int count = Integer.parseInt(shownDiceColor.replaceFirst(INPUT_RX_DICECOLOR, "$1"));
						DCOLOR color = DCOLOR.valueOf(shownDiceColor.replaceFirst(INPUT_RX_DICECOLOR, "$2"));
						shownDiceSet.add(new DiceCount(count, color));
					}
					data.shownDiceSets.add(shownDiceSet);
				}
			}
			else {
				throw new RuntimeException("invalid line '"+line+"'");
			}
			return data;
		}
	}

	public static int getMaxLineLength(String inputFile) {
		try {
			int result = 0;
			Scanner scanner = new Scanner(new File(inputFile));
			while (scanner.hasNext()) {
				result = Math.max(result, scanner.nextLine().trim().length());
			}
			scanner.close();
			return result;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.toString(), e);
		}	
	}
	

	private static String SPACES = "                "; 
	
	private static String spaces(int cnt) {
		if (cnt < 0) {
			return "";
		}
		while (SPACES.length() <cnt) {
			SPACES = SPACES+SPACES;
		}
		return SPACES.substring(0, cnt);
	}
	private static String lPad(String txt, int length) {
		return spaces(length-txt.length())+txt;
	}
	private static String rPad(String txt, int length) {
		return txt+spaces(length-txt.length());
	}
	private static String nLen(int n, int length) {
		return lPad(Integer.toString(n), length);
	}

	
	public static void mainPart1(String inputFile, int maxRed, int maxGreen, int maxBlue) {
		output = new Y23GUIOutput02("Y23 Day 02 Part I", true);
		Map<DCOLOR, Integer> maxDicesPerColor = new HashMap<>();
		maxDicesPerColor.put(DCOLOR.red, maxRed);
		maxDicesPerColor.put(DCOLOR.green, maxGreen);
		maxDicesPerColor.put(DCOLOR.blue, maxBlue);
		System.out.println("MAX: " + maxDicesPerColor);
		int sumValidGameIDs = 0;
		List<String> inputLines = new ArrayList<>();
		List<String> outputLines = new ArrayList<>();
		int maxLineLength = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			String line = data.toString();
			inputLines.add(line);
			maxLineLength = Math.max(maxLineLength, line.length());
		}
		output.addStep(show(outputLines, inputLines, output.color("gray")));
		for (InputData data:new InputProcessor(inputFile)) {
			String line = data.toColoredString(maxDicesPerColor, output.style("bred"), output.color("gray"));
			String orig = inputLines.remove(0);
			boolean valid = orig.equals(line);
			line = line + spaces(maxLineLength-orig.length()+3) + output.color("green");
			if (valid) {
				sumValidGameIDs += data.gameId;
				line += nLen(data.gameId,3)+" "+nLen(sumValidGameIDs,5);
			}
			else {
				line += "   "+" "+nLen(sumValidGameIDs,5);
			}
			line = line + output.color("gray");
			outputLines.add(line);
			output.addStep(show(outputLines, inputLines, output.color("gray")));
		}
		System.out.println("SUM VALID GAME IDs: "+sumValidGameIDs);
	}


	private static String show(List<String> outputLines, List<String> inputLines, String outputColor) {
		StringBuilder result = new StringBuilder();
		result.append(outputColor);
		for (String outputLine:outputLines) {
			result.append(outputLine).append("\n");
		}
		result.append(output.color("black"));
		for (String inputLine:inputLines) {
			result.append(inputLine).append("\n");
		}
		return result.toString();
	}


	public static void mainPart2(String inputFile) {
		int sumSetValues = 0;
		output = new Y23GUIOutput02("Y23 Day 02 Part II", true);
		List<String> inputLines = new ArrayList<>();
		List<String> outputLines = new ArrayList<>();
		int maxLineLength = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			String line = data.toString();
			inputLines.add(line);
			maxLineLength = Math.max(maxLineLength, line.length());
		}
		output.addStep(show(outputLines, inputLines, output.color("darkgray")));
		for (InputData data:new InputProcessor(inputFile)) {
			Map<DCOLOR, Integer> maxDicesPerColor = new HashMap<>();
			maxDicesPerColor.put(DCOLOR.red, 0);
			maxDicesPerColor.put(DCOLOR.green, 0);
			maxDicesPerColor.put(DCOLOR.blue, 0);
			for (List<DiceCount> ds:data.shownDiceSets) {
				for (DiceCount dc:ds) {
					int max = Math.max(maxDicesPerColor.get(dc.color), dc.cnt-1);
					maxDicesPerColor.put(dc.color, max);
				}
			}
			String line = data.toColoredString(maxDicesPerColor, output.style("byellow"), output.color("darkgray"));
			String orig = inputLines.remove(0);
			boolean valid = orig.equals(line);
			line = line + spaces(maxLineLength-orig.length()+3) + output.color("green");
			int red = (maxDicesPerColor.get(DCOLOR.red)+1);
			int green = (maxDicesPerColor.get(DCOLOR.green)+1);
			int blue = (maxDicesPerColor.get(DCOLOR.blue)+1);
			int setValue = red * green * blue;
			sumSetValues += setValue;
			line += nLen(red,2)+" x "+nLen(green,2)+" x "+nLen(blue,2)+" = " + nLen(setValue,4) +"   "+ nLen(sumSetValues,6);
			line = line + output.color("darkgray");
			outputLines.add(line);
			output.addStep(show(outputLines, inputLines, output.color("darkgray")));
		}
		System.out.println("SUM SET VALUES: "+sumSetValues);
	}

	

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
		mainPart1("exercises/day02/Feri/input-example.txt", 12, 13, 14);
//		mainPart1("exercises/day02/Feri/input.txt", 12, 13, 14);
		System.out.println("---------------");
		System.out.println("--- PART II ---");
		mainPart2("exercises/day02/Feri/input-example.txt");
//		mainPart2("exercises/day02/Feri/input.txt");     
		System.out.println("---------------");    // 
	}
	
}
