package solverpkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class HelperMethods {

	// 1- update Sudoku matrix
	// 2- check for rows
	// 3- check for columns
	// 4- check sub-matrix

	public void updateSudoku(int x /* 1,2,3,... */, int y /* A,B,C,... */, int v, int[][] m) {

	}

	public boolean checkRow(int x/* 1,2,3,... */, int y /* A,B,C,... */, int v, int[][] m) {
		return false;
		// x, y represents value index -> v[x][y]
	}

	public boolean checkColumn(int x/* 1,2,3,... */, int y /* A,B,C,... */, int v, int[][] m) {
		return false;
		// x, y represents value index -> v[x][y]
	}

	public boolean checkSubMatrix(int x/* 1,2,3,... */, int y /* A,B,C,... */, int v, int[][] m) {
		return false;
		// x, y represents value index -> v[x][y]
		///
	}

	//
	// climb procedure
	public int[] readFile(String path, int[] t) {
		try {
			t = new int[81];
			int count = 0;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			// StringBuilder sb = new StringBuilder();
			String line = "";
			while (line != null) {
				line = bufferedReader.readLine();
				t[count] = Integer.valueOf(line);
				count++;
			}
			return t;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	public void climbProcess(InitialSolutions in , int[][] finalSol) {
//		int[][] finalSol = new int[9][9];
		int temp[] = new int[81];
		temp = readFile("gameMatrix/1.txt", temp);
		int[][] occurrences = new int[2][9], tempSubMatrix = new int[3][3];
		for (int i = 0; i < 9; i++) {
			occurrences[0][i] = i + 1;
			occurrences[1][i] = 0;
		} // initialization of occurrences array
		int[][] m = new int[9][9], newSol = new int[9][9];
		fillMatrix(m, in.getSolution());
		fillMatrix(newSol, temp);
		
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					finalSol[i][j] = tempSubMatrix[i][j];
					System.out.print(finalSol[i][j] + " * ");
				}
				System.out.println();
			}

			climbSubFunction(m, tempSubMatrix, newSol, occurrences, 3, 6, 0, 3);

			for (int i = 0; i < 3; i++) {
				for (int j = 0, x = 3; j < 3 && x < 6; j++, x++) {
					finalSol[i][x] = tempSubMatrix[i][j];
					System.out.print(finalSol[i][x] + " $ ");
				}
				System.out.println();
			}
		

	}

	public int[][] climbSubFunction(int[][] m, int[][] temp, int[][] init, int[][] occurrences, int x1, int x2, int y1,
			int y2) {

		int fitness = 1, min, inxMin, max, inxMax;
		for (int i = 0; i < 9; i++) {
			occurrences[0][i] = i + 1;
			occurrences[1][i] = 0;
		}

		for (int i = y1, d = 0; i < y2 && d < 3; i++, d++) {
			for (int j = x1, g = 0; j < x2 && g < 3; j++, g++) {
				occurrences[1][(m[i][j] - 1)]++;
				temp[d][g] = m[i][j];
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
		occurrences = sortOccurrences(occurrences);
		fitness = 1;
		for (int i = 0; i < 9; i++) {
			if (occurrences[1][i] != 0)
				fitness *= (occurrences[0][i] * occurrences[1][i]);
		}
		while (fitness != 362880) {
			fitness = 1;
			occurrences = sortOccurrences(occurrences);
			min = occurrences[1][0];
			inxMin = occurrences[0][0];
			max = occurrences[1][8];
			inxMax = occurrences[0][8];
			System.out.println("Max.: " + max);
			System.out.println("Min.: " + min);
			System.out.println("tempSubMatrix: 1");
			temp = findAndReplace(inxMin, inxMax, occurrences, temp, init);
			for (int i = 0; i < 9; i++) {
				if (occurrences[1][i] != 0)
					fitness *= (occurrences[0][i] * occurrences[1][i]);
			}
			System.out.println("sumFitness: " + fitness);
			System.out.println("Updated tempSubMatrix:");
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					System.out.print(temp[i][j] + " ");
				}
				System.out.println();
			}

		}
		// System.out.println("Updated new Sol:");
		// for (int i = y1, d = 0; i < y2 && d < 3; i++, d++) {
		// for (int j = x1, g = 0; j < x2 && g < 3; j++, g++) {
		// init[i][j] = temp[d][g] ;
		// System.out.print(init[i][j] + " ");
		// }
		// System.out.println();
		// }
		return temp;
	}

	public int[][] sortOccurrences(int[][] arr) {
		int temp = 0, temp2 = 0, n = 9;
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (arr[1][j - 1] > arr[1][j]) {
					// swap elements
					temp = arr[1][j - 1];
					temp2 = arr[0][j - 1];
					arr[1][j - 1] = arr[1][j];
					arr[0][j - 1] = arr[0][j];
					arr[1][j] = temp;
					arr[0][j] = temp2;
					System.out.print(temp2 + " - ");
				}

			}
		}
		System.out.println();
		return arr;
	}

	public int[][] findAndReplace(int min, int max, int[][] occ, int[][] temp, int[][] init) {
		int[] t = new int[9], i = new int[9];
		i = flattenToArr(init, i, 3);
		t = flattenToArr(temp, t, 3);
		for (int c = 0; c < 9; c++) {
			if (i[c] == 0 && t[c] == max) {
				t[c] = min;
				occ[1][0]++;
				occ[1][8]--;
				break;
			}
		}
		return fillMatrixGeneric(temp, t, 3);
	}

	public int[][] updateSol(int[][] init, int[][] temp, int x1, int x2, int y1, int y2) {

		for (int i = 0, y = y1; i < 3 && y < y2; i++, y++) {
			for (int j = 0, x = x1; i < 3 && x < x2; j++, x++) {
				init[y][x] = temp[i][j];
			}
		}
		return init;
	}

	public int[] flattenToArr(int[][] m, int[] t, int len) {
		int count = 0;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				t[count] = m[i][j];
				System.out.print(t[count] + ", ");
				count++;
			}
		}
		System.out.println();
		return t;
	}

	public int[][] fillMatrixGeneric(int m[][], int[] temp, int l) {
		// testing matrix in temp
		int countTemp = 0;
		for (int t = 0; t < l; t++) {
			for (int d = 0; d < l; d++) {
				m[t][d] = temp[countTemp];
				countTemp++;
			}
		}
		return m;
	}

	public int[][] fillMatrix(int m[][], int[] temp) {
		// testing matrix in temp
		int countTemp = 0;
		for (int t = 0; t < 9; t++) {
			for (int d = 0; d < 9; d++) {
				m[t][d] = temp[countTemp];
				countTemp++;
			}
		}
		return m;
	}

	public int[][] localSubMatrix(int startRangeX, int startRangeY, int endRangeX, int endRangeY) {

		return null;
	}
}