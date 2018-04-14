package solverpkg;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

public class Main {
	public static HelperMethods helperMethods = new HelperMethods();
	public static String level[] = { "Easy", "Medium", "Hard" };
	private JFrame frame;
	public static ArrayList<InitialSolutions> randomSolutions = new ArrayList<>();
	public static int temp[] = new int[81];
	final ButtonGroup group = new ButtonGroup();
	public static int sudokuProblemMatrix[][] = new int[9][9];
	public static int[][] finalSol = new int[9][9];
	public static String terms[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };

	public String buildPath(int rand) {
		return "gameMatrix/" + rand + ".txt";
	}

	public int [] readFile(String path, int[] t) {
		try {
			t = new int[81];
			int count = 0;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			String line = "";
			while (line != null) {
				line = bufferedReader.readLine();
				t[count] = Integer.valueOf(line);
				count++;
			}
			bufferedReader.close();
			return t;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return t;
	}

	public int[][] fillMatrix(int m[][], int[] temp) {
		// testing matrix in temp
		int countTemp = 0;
		for (int t = 0; t < 9; t++) {
			for (int d = 0; d < 9; d++) {
				m[t][d] = temp[countTemp];
				countTemp++;
				// System.out.print(m[t][d]+" - ");
			}
			// System.out.println();
		}
		return m;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("new Window: setVisible(true) - 1");
					Main window = new Main();
					System.out.println("new Window: setVisible(true) - 2");
					window.frame.setVisible(true);
					System.out.println("new Window: setVisible(true) - 3");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		
		String path = buildPath(1);
		temp = readFile(path, temp);
		System.out.println("temp: "+temp[3]+" "+temp[80]);
		generateRandomSolutions(10);
		showSol();
		// loop
		// fillMatrix(sudokuProblemMatrix,
		// randomSolutions.get(9).getSolution());
		ArrayList<InitialSolutions> solList = new ArrayList<>();
		double minFitness = 0;
		for (int s = 1; s < randomSolutions.size(); s++) {
			int sudokuProblemMatrixnew[][] = new int[9][9];
			fillMatrix(sudokuProblemMatrixnew, randomSolutions.get(s).getSolution());
			InitialSolutions sol = new InitialSolutions(randomSolutions.get(s).getSolution(),
					fitnessRowSummation(sudokuProblemMatrixnew), fitnessColumnSummation(sudokuProblemMatrixnew),
					fitnessRowMult(sudokuProblemMatrixnew), fitnessColumnMult(sudokuProblemMatrixnew),
					fitnessRowMissing(sudokuProblemMatrixnew), fitnessColMissing(sudokuProblemMatrixnew));
			System.out.println(sol.getAllfitness());
			solList.add(sol);
		}
		double tmp = solList.get(0).getAllfitness();
		int loc = 0;
		int[] fit = new int[solList.size()];
		for (InitialSolutions sol : solList) {
			System.out.println((int) sol.getAllfitness());
			fit[loc] = (int) sol.getAllfitness();
			loc++;
		}
		loc = 0;
		int idx = 0;
		int mn = fit[0], temp = 0;
		for (int e : fit) {
			temp = Integer.min(mn, e);
			if (mn > temp) {
				mn = temp;
				loc = idx;
			}
			idx++;
		}
		System.out.println("current min = " + mn + " ,loc = " + (loc + 1));
		helperMethods.climbProcess(solList.get(loc) , finalSol);
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setSize(300, 300);
		frame.getContentPane().add(panel, BorderLayout.WEST);
		JPanel controlP = new JPanel();
		frame.getContentPane().add(controlP, BorderLayout.SOUTH);
		JButton btnNewButton = new JButton("Solve");
		controlP.add(btnNewButton);
		JButton btnNewGame = new JButton("New Game");
		controlP.add(btnNewGame);
		JComboBox<String> comboBox = new JComboBox<>(level);
		controlP.add(comboBox);
		temp = readFile("gameMatrix/1.txt", temp);
		fillMatrix(sudokuProblemMatrix, temp);
		createGrid(panel, 9, 9);
	}

	public static void createGrid(JPanel panel, int numberOfRows, int numberOfColumns) {
		GridLayout gl_panel = new GridLayout(numberOfRows, numberOfColumns);
		gl_panel.setVgap(1);
		gl_panel.setHgap(1);
		panel.setLayout(gl_panel);
		for (int c = 0; c < numberOfColumns; c++) {
			for (int r = 0; r < numberOfRows; r++) {
				JButton button = new JButton();
				if (sudokuProblemMatrix[c][r] != 0) {
					button.setText(Integer.toString(sudokuProblemMatrix[c][r]));
					// button.setFocusable(false);
					button.setEnabled(false);
				}

				button.setSize(10, 10);
				button.putClientProperty("id", terms[c] + (r + 1));
				button.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						// Add your code here for action event
						if (button.isEnabled()) {
							String t;
							t = (String) button.getClientProperty("id");
							System.out.println(t);
							if (button.getText().equals(null) || button.getText().equals("")) {
								button.setText(Integer.toString(1));
							} else {
								int tem = Integer.valueOf(button.getText()) + 1;
								button.setText(Integer.toString(tem));
							}
							if (Integer.valueOf(button.getText()) > 9) {
								button.setText(null);
							}
						}
						// playerChoice++;
					}
				});
				panel.add(button);
			}
		}
	}

	// initializeSolutionsRandomly

	public void generateRandomSolutions(int size) {
		InitialSolutions initial;
		for (int i = 0; i < size; i++) {
			int tempCopy[] = new int[81];
			//= { 0, 0, 0, 8, 0, 5, 0, 0, 2, 0, 0, 3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 7, 0, 4, 5, 2, 0, 0,
				//	6, 0, 0, 0, 1, 0, 5, 9, 0, 1, 0, 3, 0, 2, 7, 0, 7, 0, 0, 0, 9, 0, 0, 6, 3, 4, 0, 7, 5, 0, 0, 0, 0,
					//7, 0, 0, 0, 0, 0, 9, 0, 0, 1, 0, 0, 4, 0, 6, 0, 0, 0 };
			tempCopy = readFile("gameMatrix/1.txt", tempCopy);
			initial = new InitialSolutions(tempCopy);
			for (int j = 0; j < 81; j++) {
				System.out.print("  " + tempCopy[j]);
				Random rand = new Random();
				if (initial.getSolution()[j] == 0) {
					tempCopy[j] = rand.nextInt((9 - 1) + 1) + 1;
					initial.setSolution(tempCopy);
				}
			}
			// initial.toString();
			randomSolutions.add(initial);
//			System.out.println();
		}
	}

	// show random solutions in 9x9 matrix
	public static void showSol() {
		for (int r = 0; r < randomSolutions.size(); r++) {
			for (int t = 0; t < 81; t++) {
				// System.out.print(randomSolutions.get(r).getSolution()[t]+"
				// ");
				if ((t + 1) % 9 != 0) {
					System.out.print(randomSolutions.get(r).getSolution()[t] + "  ");
				} else {
					System.out.print(randomSolutions.get(r).getSolution()[t] + "  ");
					System.out.println();
				}
			}
			System.out.println("\n solution " + r + ": \n-----------------------------------------");
		}
	}

	// fitness function

	// fillMatrix (sudokuProblemMatrix , "one of the solutions" )
	public int[] fitnessRowSummation(int[][] m) {
		// sudokuProblemMatrix
		// 1- row
		int[] rowSum = new int[9];
		int fit = 45;
		int tmp = 0;
		int count = 0;
		while (count < 9) {
			for (int i = 0; i < 9; i++) {
				tmp += m[count][i];
			}
			System.out.println(tmp + "   fitness: " + String.valueOf(Math.abs(fit - tmp)));
			rowSum[count] = tmp;
			tmp = 0;
			count++;

		}
		return rowSum;
	}

	public int[] fitnessColumnSummation(int[][] m) {

		int fit = 45;
		int tmp = 0;
		int count = 0;
		int[] colSum = new int[9];
		while (count < 9) {
			for (int i = 0; i < 9; i++) {
				tmp += m[i][count];
			}
			System.out.println(tmp + "   fitness: " + String.valueOf(Math.abs(fit - tmp)));
			colSum[count] = tmp;
			tmp = 0;
			count++;
		}
		return colSum;

	}

	public int[] fitnessRowMult(int[][] m) {
		// sudokuProblemMatrix
		// 1- row
		int[] rowMul = new int[9];
		int fit = 362880;
		int tmp = 1;
		int count = 0;
		while (count < 9) {
			for (int i = 0; i < 9; i++) {
				tmp *= m[count][i];
			}
			System.out.println(tmp + "   fitness: " + String.valueOf(Math.abs(fit - tmp)));
			rowMul[count] = tmp;
			tmp = 1;
			count++;
		}
		return rowMul;
	}

	public int[] fitnessColumnMult(int[][] m) {
		int[] colMul = new int[9];
		int fit = 362880;
		int tmp = 1;
		int count = 0;

		while (count < 9) {
			for (int i = 0; i < 9; i++) {
				tmp *= m[i][count];
			}
			System.out.println(tmp + "   fitness: " + String.valueOf(Math.abs(fit - tmp)));
			colMul[count] = tmp;
			tmp = 1;
			count++;
		}
		return colMul;
	}

	public ArrayList<Set<Integer>> fitnessRowMissing(int[][] m) {
		// Set<Integer> A = new HashSet<Integer>();
		ArrayList<Set<Integer>> setA = new ArrayList<>();

		// { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int tmp = 0;
		int count = 0;
		Set<Integer> found = new HashSet<Integer>();
		while (count < 9) {
			Set<Integer> A = new HashSet<Integer>();
			A.add(1);
			A.add(2);
			A.add(3);
			A.add(4);
			A.add(5);
			A.add(6);
			A.add(7);
			A.add(8);
			A.add(9);
			for (int e : A) {
				for (int i = 0; i < 9; i++) {
					found.add(m[count][i]);
				}
			}
			for (Integer f : found)
				for (int g = 0; g < 9; g++)
					if (A.contains(f)) {
						A.remove(f);
					}
			System.out.println(A);
			setA.add(count, A);// index,Set<>
			System.out.println("___________________________________row " + (count + 1));
			found.clear();
			count++;
		}
		return setA;
	}

	public ArrayList<Set<Integer>> fitnessColMissing(int[][] m) {

		ArrayList<Set<Integer>> setA = new ArrayList<>();

		// { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int tmp = 0;
		int count = 0;
		Set<Integer> found = new HashSet<Integer>();
		while (count < 9) {
			Set<Integer> A = new HashSet<Integer>();
			A.add(1);
			A.add(2);
			A.add(3);
			A.add(4);
			A.add(5);
			A.add(6);
			A.add(7);
			A.add(8);
			A.add(9);
			for (int e : A) {
				for (int i = 0; i < 9; i++) {
					found.add(m[i][count]);
				}
			}
			for (Integer f : found)
				for (int g = 0; g < 9; g++)
					if (A.contains(f)) {
						A.remove(f);
					}
			System.out.println(A);
			setA.add(count, A);// index,Set<>
			System.out.println("___________________________________col " + (count + 1));
			found.clear();
			count++;
		}
		return setA;
	}
}