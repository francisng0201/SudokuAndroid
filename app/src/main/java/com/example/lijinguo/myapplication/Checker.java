package com.example.lijinguo.myapplication;

/**
 * A class that checks if a complete given sudoku is valid
 * @author Franc
 *
 */
public class Checker {
	/**
	 * Check if a given sudoku is valid
	 * @param sudoku 
	 * @return true if valid, false otherwise
	 */
	public boolean isValidSudoku(Sudoku sudoku){
		int[][] board = sudoku.getBoard();
		
		if (!isCompleted(board)){
			return false;
		}
		
		for (int i = 0; i < Sudoku.LENGTH; i++){
			if (!checkRow(board, i)){
				return false;
			}
		}
		
		for (int i = 0; i < Sudoku.LENGTH; i++){
			if (!checkColumn(board, i)){
				return false;
			}
		}
		
		for(int i = 0; i < 9;i += 3){
		 	for(int j = 0; j < 9;j += 3){
				if(!checkSubGrid(board, i, j)){
					return false;
				}
			}
		}
		
		return true;
	}
    
	/**
	 * Check if the given sudoku is completed; i.e. all values are between 1 to 9
	 * @param board
	 * @return true if valid, false otherwise
	 */
	protected boolean isCompleted(int[][] board){
		for (int i = 0; i < Sudoku.LENGTH; i++){
			for (int j = 0; j < Sudoku.LENGTH; j++){
				if (board[i][j] > 9 || board[i][j] < 1){
					
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if a given column in sudoku is valid, i.e. no duplicate value
	 * @param board
	 * @param row
	 * @return true if valid, false otherwise
	 */
	protected boolean checkRow(int[][] board, int row){
		if (row < 0 || row > 8){
			return false;
		}
		
		for (int i = 0; i < Sudoku.LENGTH; i++){
			for (int j = i + 1; j < Sudoku.LENGTH; j++){
				if (i != j && board[row][i] == board[row][j] && board[row][i] != 0){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if a given row in sudoku is valid, i.e. no duplicate value
	 * @param board
	 * @param column
	 * @return true if valid, false otherwise
	 */
	protected boolean checkColumn(int[][] board, int column){
		if (column < 0 || column > 8){
			return false;
		}
		
		for (int i = 0; i < Sudoku.LENGTH; i++){
			for (int j = i + 1; j < Sudoku.LENGTH; j++){
				if (i != j && board[i][column] == board[j][column] && board[i][column] != 0){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if a given sub grid in sudoku is valid, i.e. no duplicate value
	 * @param board
	 * @param row
	 * @return true if valid, false otherwise
	 */
	protected boolean checkSubGrid(int[][] board, int row, int column){
		if (row < 0 || row > 8 || column < 0 || column > 8){
			return false;
		}
		
		int[] oneGrid = new int[Sudoku.LENGTH];
		int k = 0;
		for(int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				oneGrid[k] = board[i + row][j + column];
				k++;
			}
		}

		for (int i = 0; i < Sudoku.LENGTH; i++){
			for (int j = i + 1; j < Sudoku.LENGTH; j++){
				if (i != j && oneGrid[i] == oneGrid[j] && oneGrid[i] != 0){
					return false;
				}
			}
		}
		return true;
	}
    
}
