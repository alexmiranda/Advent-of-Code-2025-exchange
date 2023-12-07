import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * see: https://adventofcode.com/2023/day/04
 */
public class Y23Day04Visualization {
 
	/*
	 * example input: 
	 * 
	 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
	 * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
	 * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
	 * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
	 * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
	 * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
	 * 
	 */

	private static final String INPUT_RX   = "^Card \\s*([0-9]+): ([ 0-9]+) [|] ([ 0-9]+)$";

	public static String num3(int n) {
		String result = Integer.toString(n);
		if (result.length()<3) {
			result = "  ".substring(0,3-result.length())+result;
		}
		return result;
	}
	
	public static record InputData(int cardnum, Set<Integer> winNums, Set<Integer> ownNums) {
		@Override public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("Card ").append(num3(cardnum)).append(":");
			for (int num:winNums) {
				result.append(num3(num));
			}
			result.append(" |");
			for (int num:ownNums) {
				result.append(num3(num));
			}
			return result.toString();
		}
		public String toColoredString(Set<Integer> matchedNumbers) {
			StringBuilder result = new StringBuilder();
			result.append(vis.color("gray")).append("Card ").append(num3(cardnum)).append(":");
			for (int num:winNums) {
				if (matchedNumbers.contains(num)) {
					result.append(vis.style("b2"));
				}
				result.append(num3(num));
				if (matchedNumbers.contains(num)) {
					result.append(vis.color("gray"));
				}
			}
			result.append(" |");
			for (int num:ownNums) {
				if (matchedNumbers.contains(num)) {
					result.append(vis.style("b2"));
				}
				result.append(num3(num));
				if (matchedNumbers.contains(num)) {
					result.append(vis.color("gray"));
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
			if (line.matches(INPUT_RX)) {
				int cardNum = Integer.parseInt(line.replaceFirst(INPUT_RX, "$1"));
				String[] winNumArray = line.replaceFirst(INPUT_RX, "$2").trim().split(" +");
				String[] ownNumArray = line.replaceFirst(INPUT_RX, "$3").trim().split(" +");
				Set<Integer> winNums = new HashSet();
				for (String winNum:winNumArray) {
					winNums.add(Integer.parseInt(winNum));
				}
				Set<Integer> ownNums = new HashSet();
				for (String ownNum:ownNumArray) {
					ownNums.add(Integer.parseInt(ownNum));
				}
				return new InputData(cardNum, winNums, ownNums);
			}
			else {
				throw new RuntimeException("invalid line '"+line+"'");
			}
		}
	}

	static record Pos(int x, int y) {
		@Override public String toString() { return "("+x+","+y+")"; }
	}

	public static class Visualization {
		Y23GUIOutput04 output;
		boolean markStars;
		public Visualization(String title, boolean markStars) {
			this.output = new Y23GUIOutput04(title, true);
			this.markStars = markStars;
		}
        List<String> inputRows = new ArrayList<>();
        List<String> outputRows = new ArrayList<>();
        public void addInput(String line) {
        	inputRows.add(line);
        }
        public void addOutput(String line) {
        	inputRows.remove(0);
        	outputRows.add(line);
        }
		public void show() {
			StringBuilder result = new StringBuilder();
			result.append(output.color("gray"));
			for (String line:outputRows) {
				result.append(line).append("\n");
			}
			result.append(output.color("black"));
			for (String line:inputRows) {
				result.append(line).append("\n");
			}
			output.addStep(result.toString());
		}
		public String color(String colName) { return output.color(colName); }
		public String style(String colName) { return output.style(colName); }
	}
	static Visualization vis;
	
	
	public static void mainPart1(String inputFile) {
	    vis = new Visualization("2023 Day 04 Part I", false);
		int sumPoints = 0;
		
		int maxLineLength = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			String line = data.toString();
			vis.addInput(line);
			maxLineLength = Math.max(maxLineLength, line.length());
		}
		vis.show();
		for (InputData data:new InputProcessor(inputFile)) {
			System.out.println(data);
			HashSet<Integer> matchedNumbers = new HashSet<>(data.ownNums);
			matchedNumbers.retainAll(data.winNums);
			int cntWins = matchedNumbers.size();
			System.out.println(cntWins);
			int winPoints = cntWins == 0 ? 0 : (int) Math.pow(2, cntWins-1);
			System.out.println(winPoints);
			sumPoints+=winPoints;
			String markedLine = data.toColoredString(matchedNumbers);
			markedLine = markedLine + "    ";
			if (winPoints==0) {
				markedLine += vis.color("green") + "   " + padLeft(sumPoints, 5) + vis.color("gray");
			}
			else {
				markedLine += vis.color("green") + padLeft(winPoints, 3) + padLeft(sumPoints, 5) + vis.color("gray");
			}
			vis.addOutput(markedLine);
			vis.show();
		}
		System.out.println(sumPoints);
	}


	private static String padLeft(int num, int len) {
		return padLeft(Integer.toString(num), len);
	}

	private static String padLeft(String text, int len) {
		return spaces(len-text.length()) + text;
	}

	private static String spaces(int n) {
		if (n<=0) {
			return ""; 
		}
		StringBuilder result = new StringBuilder(n);
		for (int i=0; i<n; i++) {
			result.append(' ');
		}
		return result.toString();
	}


	public static void mainPart2(String inputFile) {
	    vis = new Visualization("2023 Day 04 Part II", false);
		Map<Integer, Long> cardNum2Factor = new HashMap<>();
		
		int maxLineLength = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			String line = data.toString();
			vis.addInput(line);
			maxLineLength = Math.max(maxLineLength, line.length());
		}
		vis.show();
		
		long sumCards = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			System.out.println(data);
			int currentCardNum = data.cardnum;
			long currentCardFactor = cardNum2Factor.getOrDefault(currentCardNum, 1L);
			sumCards += currentCardFactor;
			HashSet<Integer> matchedNumbers = new HashSet<>(data.ownNums);
			matchedNumbers.retainAll(data.winNums);
			int cntWins = matchedNumbers.size();
			System.out.println(currentCardFactor+" x Card "+currentCardNum+" wins "+cntWins);
			for (int nextCardNum=currentCardNum+1; nextCardNum<=currentCardNum+cntWins; nextCardNum++) {
				cardNum2Factor.put(nextCardNum, cardNum2Factor.getOrDefault(nextCardNum, 1L) + currentCardFactor);
			}
			String markedLine = data.toColoredString(matchedNumbers);
			markedLine += vis.color("green") + " " + padLeft(cntWins, 2) + " " + padLeft("x"+currentCardFactor, 7) + " " + padLeft((int)sumCards, 9) + vis.color("gray");
			vis.addOutput(markedLine);
			vis.show();
		}
		System.out.println(sumCards);
	}




	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
		mainPart1("exercises/day04/Feri/input-example.txt");
//		mainPart1("exercises/day04/Feri/input.txt");         
		System.out.println("---------------");
		System.out.println("--- PART II ---");
		mainPart2("exercises/day04/Feri/input-example.txt");
//		mainPart2("exercises/day04/Feri/input.txt");     
		System.out.println("---------------");    // 
	}
	
}
