package com.example.lijinguo.myapplication;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DigHoles {
	int numOfHoles;
	int lowerbound;
	
	/**
	 * Constructor
	 * @param sudoku the sudoku being dug
	 * @param difficultly game's difficultly
	 */
	public DigHoles(Sudoku sudoku, String difficultly){
		// Randomly generate number of holes being dug based on the difficultly
		// Number of holes being dug are based on the paper http://zhangroup.aporc.org/images/files/Paper_3485.pdf
		if (difficultly.equals("Easy")){
			this.numOfHoles = ThreadLocalRandom.current().nextInt(36, 50);
			lowerbound = 4;
		}
		else if (difficultly.equals("Medium")){
			this.numOfHoles = ThreadLocalRandom.current().nextInt(32, 35);
			lowerbound = 3;
		}
		else{
			this.numOfHoles = ThreadLocalRandom.current().nextInt(28, 31);
			lowerbound = 2;
		}
	}
	
	/**
	 * Dig some holes from the given sudoku based on the difficultly
	 * @param sudoku
	 */
	public void dig(Sudoku sudoku){		
		int[][] board = sudoku.getBoard();
		int nonEmptyCells = 81;
		
		// Keep digging until condition is met
		while (nonEmptyCells > numOfHoles){
			Random xRand = new Random();
			int x = xRand.nextInt(Sudoku.LENGTH);
			Random yRand = new Random();
			int y = yRand.nextInt(Sudoku.LENGTH);
			
			// Check if the cell being dug is valid
			if (lowerbound < numOfNonEmptyinRow(board, x) && lowerbound < numOfNonEmptyinColumn(board, y) &&  
				lowerbound < numOfNonEmptyinSubGrid(board, x, y) && board[x][y] != 0){
				board[x][y] = 0;
				sudoku.setCanPut(x, y);
				nonEmptyCells--;
			}
		}
		System.out.println("Number of Empty cells: " + (81 - nonEmptyCells));
	}
	
	/**
	 * Helper function to check number of non empty cells in one row
	 * @param board
	 * @param row
	 * @return number of non empty cells
	 */
	public int numOfNonEmptyinRow(int[][] board, int row){
		int count = 0;
		for (int i = 0; i < Sudoku.LENGTH; i++){
			if (board[row][i] != 0){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Helper function to check number of non empty cells in one column
	 * @param board
	 * @param col
	 * @return number of non empty cells
	 */
	public int numOfNonEmptyinColumn(int[][] board, int col){
		int count = 0;
		for (int i = 0; i < Sudoku.LENGTH; i++){
			if (board[i][col] != 0){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Helper function to check number of non empty cells in one sub grid
	 * @param board
	 * @param row
	 * @param col
	 * @return number of non empty cells
	 */
	public int numOfNonEmptyinSubGrid(int[][] board, int row, int col){
    	int count = 0;
		for (int i = 0; i < 3; i++){
    		for (int j = 0; j < 3; j++){
                int m = row / 3 * 3 + i;
                int n = col / 3 * 3 + j;
                if (board[m][n] != 0){
                	count++;
                }
    		}
    	}
		return count;
	}

}
