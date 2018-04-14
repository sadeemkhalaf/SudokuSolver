package solverpkg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class InitialSolutions {

	private int solution[];
	// 1-fitnessRowSum
	private int[] rowSum;
	// 2-fitnessColSum
	private int[] colSum;
	// 3-fitnessRowMul
	private int[] rowMul;
	// 4-fitnessColSum
	private int[] colMul;
	// 5-fitnessRowMissing
	private ArrayList<Set<Integer>> rowMis;
	// 6-fitnessColMissing
	private ArrayList<Set<Integer>> colMis;
	private double allfitnessResult;

	public InitialSolutions(int[] solution, int[] rowSum, int[] colSum, int[] rowMul, int[] colMul,
			ArrayList<Set<Integer>> rowMis, ArrayList<Set<Integer>> colMis) {
		// super();
		this.solution = solution;
		this.rowSum = rowSum;
		this.colSum = colSum;
		this.rowMul = rowMul;
		this.colMul = colMul;
		this.rowMis = rowMis;
		this.colMis = colMis;
	}

	public InitialSolutions(int[] solution) {
		this.solution = solution;
	}

	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}

	public void setElementOfSolution(int element, int index) {
		solution[index] = element;
	}

	// fitness in all formulas

	public int[] getRowSum() {
		return rowSum;
	}

	public void setRowSum(int[] rowSum) {
		this.rowSum = rowSum;
	}

	public int[] getColSum() {
		return colSum;
	}

	public void setColSum(int[] colSum) {
		this.colSum = colSum;
	}

	public int[] getRowMul() {
		return rowMul;
	}

	public void setRowMul(int[] rowMul) {
		this.rowMul = rowMul;
	}

	public int[] getColMul() {
		return colMul;
	}

	public void setColMul(int[] colMul) {
		this.colMul = colMul;
	}

	public ArrayList<Set<Integer>> getRowMis() {
		return rowMis;
	}

	public void setRowMis(ArrayList<Set<Integer>> rowMis) {
		this.rowMis = rowMis;
	}

	public ArrayList<Set<Integer>> getColMis() {
		return colMis;
	}

	public void setColMis(ArrayList<Set<Integer>> colMis) {
		this.colMis = colMis;
	}

	public double getAllfitness() {
		double rowAndColSum = 0, colMisSum = 0, rowMisSum = 0;
		double rownAndColMul = 0;
		for (int i = 0; i < 9; i++) {
			rowAndColSum += (double)getColMul()[i] + getRowSum()[i];
			rownAndColMul += (double)Math.sqrt(getColMul()[i]) + Math.sqrt(getRowMul()[i]);
			rowMisSum += (double)getRowMis().get(i).stream().mapToInt(Integer::intValue).sum();
//			System.out.println("rowMisSum: "+rowMisSum);
			colMisSum += (double)getColMis().get(i).stream().mapToInt(Integer::intValue).sum();
//			System.out.println("colMisSum: "+colMisSum);
		}
		double all = (10 * (double)Math.abs(rowAndColSum-90));
		all += (double)Math.abs(rownAndColMul-362880*2);
		all+= (50 * ((double)colMisSum + (double)rowMisSum));
//		System.out.println(all);
		return all;
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
}