package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Sudoku {

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static Integer[][] initSudoku() throws IOException {
		String sudokuFile = readFile("./in/sudoku.txt", StandardCharsets.UTF_8).replaceAll("\r\n", "");
		Integer[][] tableSudoku = new Integer[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				char value = sudokuFile.charAt(i * 9 + j);
				if (value == ' ') {
					tableSudoku[i][j] = null;
				} else {
					tableSudoku[i][j] = Integer.parseInt("" + value);
				}
			}
		}
		return tableSudoku;
	}

	public static List<Integer>[][] initNumbersUsed() {
		List<Integer>[][] numbersUsed = new ArrayList[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				numbersUsed[i][j] = new ArrayList<Integer>();
			}
		}
		return numbersUsed;
	}

	public static boolean lineContainsNumber(Integer[] line, Integer number) {
		boolean exist = false;
		for (int i = 0; i < 9; i++) {
			if (line[i] == number) {
				exist = true;
			}
		}
		return exist;
	}

	public static boolean tableContainsNumber(Integer[][] table, Integer number) {
		boolean exist = false;
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == number) {
					exist = true;
				}
			}
		}
		return exist;
	}

	public static void showTable(Integer[][] tableSudoku) {
		for (int i = 0; i < tableSudoku.length; i++) {
			if (i == 0) {
				System.out.print("=====================================================\n");
			} else if (i % 3 == 0 && i != 0) {
				System.out.print("\n=====================================================\n");
			}
			for (int j = 0; j < tableSudoku[i].length; j++) {
				if (j % 3 == 0) {
					System.out.print("||");
				}
				if (tableSudoku[i][j] == null) {
					System.out.print("  .  ");
				} else {
					System.out.print("  " + tableSudoku[i][j] + "  ");
				}
				if (j + 1 == 9) {
					System.out.print("||");
				}
			}
			if ((i + 1) % 3 != 0) {
				System.out.println("\n");
			}
			if (i + 1 == 9) {
				System.out.println("\n=====================================================");
			}
		}
	}

	public static Integer[][] getSmallSudokuTable(Integer[][] sudokuTable, int indexLine, int indexColumn) {
		Integer[][] currentSmallSudoku = new Integer[3][3];
		int tableLinePosition = indexLine / 3;
		int tableColumnPosition = indexColumn / 3;
		int k, l;
		k = 0;
		for (int i = tableLinePosition * 3; i < tableLinePosition * 3 + 3; i++) {
			l = 0;
			for (int j = tableColumnPosition * 3; j < tableColumnPosition * 3 + 3; j++) {
				currentSmallSudoku[k][l] = sudokuTable[i][j];
				if (i == indexLine && j == indexColumn) {
					currentSmallSudoku[k][l] = 0;
				}
				l++;
			}
			k++;
		}
		return currentSmallSudoku;
	}

	public static Integer[] getHorizontalLine(Integer[][] sudokuTable, int indexLine, int indexColumn) {
		Integer[] line = new Integer[9];
		for (int i = 0; i < sudokuTable[indexLine].length; i++) {
			line[i] = sudokuTable[indexLine][i];
			if (i == indexColumn) {
				line[i] = 0;
			}
		}
		return line;
	}

	public static Integer[] getVerticalLine(Integer[][] sudokuTable, int indexLine, int indexColumn) {
		Integer[] line = new Integer[9];
		for (int i = 0; i < sudokuTable.length; i++) {
			line[i] = sudokuTable[i][indexColumn];
			if (i == indexLine) {
				line[i] = 0;
			}
		}
		return line;
	}

	public static boolean checkExistanceNumber(Integer[][] sudokuTable, int indexLine, int indexColumn,
			Integer numberToCheck) {
		boolean existLineHorizontal = false;
		boolean existLineVertical = false;
		boolean existTable = false;
		boolean exist = false;

		existLineHorizontal = lineContainsNumber(getHorizontalLine(sudokuTable, indexLine, indexColumn), numberToCheck);
		existLineVertical = lineContainsNumber(getVerticalLine(sudokuTable, indexLine, indexColumn), numberToCheck);
		existTable = tableContainsNumber(getSmallSudokuTable(sudokuTable, indexLine, indexColumn), numberToCheck);

		if (existLineHorizontal || existLineVertical || existTable) {
			exist = true;
		}
		return exist;
	}

	public static Integer returnPossibleNumber(Integer[][] sudokuTable, int indexLine, int indexColumn,
			List<Integer>[][] numbersUsed) {
		for (int i = 1; i <= 9; i++) {
			if (!checkExistanceNumber(sudokuTable, indexLine, indexColumn, i)
					&& !numbersUsed[indexLine][indexColumn].contains(i)) {
				return i;
			}
		}
		return null;
	}

	public static Integer[] lastPositionNotCorrect(Integer[][] sudokuTable, int indexLine, int indexColumn,
			List<Integer>[][] numbersUsed) throws IOException {
		Integer[] positionAndNewValue = new Integer[3];
		for (int i = indexLine; i >= 0; i--) {
			int initJ;
			if (i == indexLine) {
				initJ = indexColumn - 1;
			} else {
				initJ = 8;
			}
			for (int j = initJ; j >= 0; j--) {
				if (numbersUsed[i][j].size() == 0) {
					continue;
				}
				Integer possibleNumber = returnPossibleNumber(sudokuTable, i, j, numbersUsed);
				if (possibleNumber != null) {
					positionAndNewValue[0] = i;
					positionAndNewValue[1] = j;
					positionAndNewValue[2] = possibleNumber;
					return positionAndNewValue;
				} else {
					sudokuTable[i][j] = null;
					numbersUsed[i][j].clear();
				}
			}
		}
		return null;
	}

	public static Integer[][] resetToNullSudoku(Integer[][] sudokuTable, Integer[] indexBegin, Integer[] indexEnd)
			throws IOException {
		for (int i = indexBegin[0]; i <= indexEnd[0]; i++) {
			int endJ;
			int initJ;
			if (i == indexBegin[0]) {
				initJ = indexBegin[1];
			} else {
				initJ = 0;
			}
			if (i == indexEnd[0]) {
				endJ = indexEnd[1];
			} else {
				endJ = 8;
			}
			for (int j = initJ; j < endJ; j++) {
				if (initSudoku()[i][j] != null) {
					continue;
				} else {
					sudokuTable[i][j] = null;
				}
			}
		}
		return sudokuTable;
	}

	public static List<Integer>[][] clearNumbersUsed(List<Integer>[][] numbersUsed, Integer[] indexBegin,
			Integer[] indexEnd) throws IOException {
		for (int i = indexBegin[0]; i <= indexEnd[0]; i++) {
			int endJ;
			int initJ;
			if (i == indexBegin[0]) {
				initJ = indexBegin[1] + 1;
			} else {
				initJ = 0;
			}
			if (i == indexEnd[0]) {
				endJ = indexEnd[1];
			} else {
				endJ = 8;
			}
			for (int j = initJ; j < endJ; j++) {
				numbersUsed[i][j].clear();
			}
		}
		return numbersUsed;
	}

	public static Integer[][] resolveSudoku(Integer[][] sudokuTable, List<Integer>[][] numbersUsed) throws IOException {
		for (int i = 0; i < sudokuTable.length; i++) {
			for (int j = 0; j < sudokuTable[i].length; j++) {
				if (initSudoku()[i][j] != null) {
					continue;
				}
				Integer possibleNumber = returnPossibleNumber(sudokuTable, i, j, numbersUsed);
				if (possibleNumber != null) {
					sudokuTable[i][j] = possibleNumber;
					numbersUsed[i][j].add(possibleNumber);
				} else {
					Integer[] lastPositionNotCorrect = lastPositionNotCorrect(sudokuTable, i, j, numbersUsed);
					if (lastPositionNotCorrect != null) {
						sudokuTable = resetToNullSudoku(sudokuTable,
								new Integer[] { lastPositionNotCorrect[0], lastPositionNotCorrect[1] },
								new Integer[] { i, j });
						numbersUsed = clearNumbersUsed(numbersUsed,
								new Integer[] { lastPositionNotCorrect[0], lastPositionNotCorrect[1] },
								new Integer[] { i, j });
						i = lastPositionNotCorrect[0];
						j = lastPositionNotCorrect[1];
						sudokuTable[i][j] = lastPositionNotCorrect[2];
						numbersUsed[i][j].add(lastPositionNotCorrect[2]);
					}
				}
			}
		}
		return sudokuTable;
	}

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		long endTime;
		Integer[][] sudokuTable = initSudoku();
		List<Integer>[][] numbersUsed = initNumbersUsed();

		System.out.println("\n----before-----");
		showTable(sudokuTable);

		resolveSudoku(sudokuTable, numbersUsed);

		System.out.println("\n----after-----");
		showTable(sudokuTable);

		endTime = System.currentTimeMillis();
		System.out.println("That tooks " + (endTime - startTime) + " milliseconds");
	}
}
