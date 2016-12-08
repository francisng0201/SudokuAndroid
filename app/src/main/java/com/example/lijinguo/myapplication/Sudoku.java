package com.example.lijinguo.myapplication;

import java.util.Random;

public class Sudoku {
	
	public static final int LENGTH = 9;
	private int[][] board;
	private boolean[][] canPut;
	
	/**
	 * Sudoku constructor
	 */
	public Sudoku(){
		board = new int[LENGTH][LENGTH];
		canPut = new boolean[LENGTH][LENGTH];
	}

	public void setCanPut(boolean[][] canPut){
		this.canPut = canPut;
	}
	/**
	 * generate a valid sudoku
	 */
	public void generate(){
		putNumbers(0, 0);
		for (int i = 0; i < LENGTH; i++){
			for (int j = 0; j < LENGTH; j++){
				canPut[i][j] = false;
			}
		}
	}
	
	/**
	 * put numbers in blocks recursively
	 * @param x horizon position of the number 
	 * @param y vertical position of the number
	 * @return true if the sudoku is valid, false otherwise
	 */
	private boolean putNumbers(int x, int y){
		Random random = new Random();
		int currentNum = 0;
		int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};

		//generate random numbers
		for (int i = LENGTH - 1; i > 0; i--){
			currentNum = random.nextInt(i);
			int tmp = numbers[currentNum];
			numbers[currentNum] = numbers[i];
			numbers[i] = tmp;
		}

		//generate sudoku recursively
		for (int i = 0; i < LENGTH; i++){
			if (isLegal(x, y, numbers[i])){
				board[x][y] = numbers[i];
				if (x != LENGTH-1){
					x++;
				}
				else{
					//at the end of the board
					if (y == LENGTH-1){
						return true;
					}
					else{
						x = 0;
						y++;
					}
				}
				if (putNumbers(x, y)){
					return true;
				}
			}
		}
		
		board[x][y] = 0;
		return false;
	}
	
	/**
	 * Check if the number put is valid
	 * @param x horizon position of the number 
	 * @param y vertical position of the number
	 * @param number number being put to the box
	 * @return true if valid, false otherwise
	 */
	protected boolean isLegal(int x, int y, int number){
		// Check if same number exists in the same row
		for (int i = 0; i < LENGTH; i++){
			if (board[x][i] == number){
				return false;
			}
		}
		
		// Check if same number exists in the same column
		for (int i = 0; i < LENGTH; i++){
			if (board[i][y] == number){
				return false;
			}
		}
		
		// Check if same number exists in the same sub grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                int row = x / 3 * 3 + i;
                int column = y / 3 * 3 + j;
                if (board[row][column] == number){
					return false;
				}
            } 
        }
		return true;
	}
	
	/**
	 * get the sudoku
	 * @return a 2d array containing the sudoku
	 */
	public int[][] getBoard(){
		return this.board;
	}
	
	/**
	 * print out the sudoku
	 */
	public void print(){
		for(int i = 0; i < LENGTH; i++){
			for(int j = 0;j < LENGTH; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * update the whole board
	 * @param board
	 */
	public void setBoard(int[][] board){
		for (int i = 0; i < LENGTH; i++){
			for (int j = 0; j < LENGTH; j++){
				this.board[i][j] = board[i][j];
			}
		}
	}
	
	/**
	 * update one box of the board
	 * @param value the updated value
	 * @param x horizon position of the number 
	 * @param y vertical position of the number
	 */
	public void setBoard(int value, int x, int y){
		if (value > 9 || value < 0){
			System.out.println("invalid number");
			return;
		}
		if (x < 0 || y < 0 || x > 8 || y > 8){
			System.out.println("invalid position");
			return;
		}
		this.board[x][y] = value;
	}
	
	/**
	 * Set certain cell to "can be put" status
	 * @param x
	 * @param y
	 */
	public void setCanPut(int x, int y){
		this.canPut[x][y] = true;
	}
	
	/**
	 * Helper function for debugging
	 * Prints the array that shows the cells can be dug or not
	 */
	public void printCanPut(){
		for (int i = 0; i < LENGTH; i++){
			for (int j = 0; j < LENGTH; j++){
				System.out.print(canPut[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * get certain cell to check if it can be put a number
	 * @param x
	 * @param y
	 * @return true if it can be; false otherwise
	 */
	public boolean getCanPut(int x, int y){
		return this.canPut[x][y];
	}
}
