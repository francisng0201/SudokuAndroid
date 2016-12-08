package com.example.lijinguo.myapplication;
import android.widget.Toast;

import java.util.*;
/**
 * Created by lijinguo on 11/13/16.
 */

public class Game {
    /**
     * sudoku game logic
     */
    private String[] puzzle;
    private boolean[] emptyPosition;
    private Sudoku sudoku;
    private Checker checker;
    private DataSource datasource;
    private String[] originalboard;
    private int[][] twoDimenOriginalBoard;


    /**
     * constructor for starting a new game
     * @param difficulty
     */
    public Game(String difficulty){
        this.emptyPosition = new boolean[81];
        this.puzzle = new String[81];
        sudoku = new Sudoku();
        checker = new Checker();
        sudoku.generate();
        int[][] board = sudoku.getBoard();
        this.twoDimenOriginalBoard = deepCopyIntArray(board);
        this.originalboard = intArrayToStringArray(twoDimenOriginalBoard);
        DigHoles dh = new DigHoles(sudoku, difficulty);
        dh.dig(sudoku);
        toOneDimenHelper(board);
    }

    /**
     * constructor for resuming a game
     * @param oneDimenboard
     * @param userfill
     */
    public Game(String[] oneDimenboard, String[] userfill, String[] originalboard){
        checker = new Checker();
        this.emptyPosition = new boolean[81];
        this.puzzle = oneDimenboard;
        toTwoDimenHelper(oneDimenboard, userfill);
        this.originalboard = originalboard;
        twoDimenOriginalBoard = StringArrayToIntArray(originalboard);
    }

    /**
     * to convert 1D String array to 2D int array
     * @param oneDimenboard
     * @param userfill
     */
    public void toTwoDimenHelper(String[] oneDimenboard, String[] userfill){
        int [][] twoDimenBoard = new int[9][9];
        // 2D boolean array to construct sudoku
        boolean[][] canPut = new boolean[9][9];
        for(int i = 0; i < oneDimenboard.length; i++){
            System.out.println(userfill[i]);
            int row = i/twoDimenBoard.length;
            int col = i%twoDimenBoard.length;
            if(userfill[i].equals("  ")){
                twoDimenBoard[row][col] = 0;
            }
            else{
                twoDimenBoard[row][col] = Integer.parseInt(userfill[i]);
            }
            if(oneDimenboard[i].equals("  ")){
                emptyPosition[i] = true;
                canPut[row][col] = true;
            }
            else{
                emptyPosition[i] = false;
                canPut[row][col] = false;
            }
        }
        sudoku = new Sudoku();
        sudoku.setBoard(twoDimenBoard);
        sudoku.setCanPut(canPut);
    }

    /**
     * to convert 2D int array to 1D String array
     * @param twoDimenboard
     */
    public void toOneDimenHelper(int[][] twoDimenboard) {

        for(int row = 0; row < twoDimenboard.length; row++){
            for(int col = 0; col< twoDimenboard.length; col++){
                String number;
                if(twoDimenboard[row][col] == 0) {
                    number = "  ";
                    emptyPosition[row*twoDimenboard.length+col] = true;
                }
                else{
                    number = Integer.toString(twoDimenboard[row][col]);
                    emptyPosition[row*twoDimenboard.length+col] = false;
                }
                puzzle[row*twoDimenboard.length+col] = number;
//                System.out.println(board[row][col]);
            }
        }

    }
//TODO: weird bug in the demo. couldn't detect success. after resuming. check sign out, check clicking instant run.
    /**
     * to check if the user is allowed to fill this number
     * @param position
     * @param number
     * @return
     */
    public boolean validFill(int position, String number){
        int[][] board = sudoku.getBoard();
        int row = position/board.length;
        int col = position%board.length;
        int num = Integer.parseInt(number);
        board[row][col] = num;
        if (!checker.checkRow(board, row) || !checker.checkColumn(board, col)) {
            Toast.makeText(GlobalVariable.getAppContext(), "Number already exists in the same row/column!",
                    Toast.LENGTH_LONG).show();
            System.out.println("Number already exists in the same row/column!");
            board[row][col] = 0;
            return false;
        }
        sudoku.setBoard(board);
        return true;
    }

    /**
     * to check if sudoku is complete
     * @return
     */
    public boolean win() {
        if (checker.isValidSudoku(sudoku)) {
            return true;
        }
        return false;
    }

    /**
     * after winning a game, update user's score and #of hints
     */
    public void updateScoreHints(){
        datasource = new DataSource(GlobalVariable.getAppContext());
        datasource.open();
        datasource.incrementScoreHints(GlobalVariable.getSessionUsername());
        datasource.close();
    }

    public String[] getPuzzle(){
        return puzzle;
    }

    public boolean[] getEmptyPosition(){
        return emptyPosition;
    }

    public String[] deepCopy(String[] puzzle){
        String[] copy = new String[81];
        for(int i = 0; i< puzzle.length; i++){
            copy[i] = puzzle[i];
        }
        return copy;
    }


    public int[] hint() {
        int[] position_number = new int[2];
        // Randomly generate a position that can put a number
        Random xRand = new Random();
        int x = xRand.nextInt(9);
        Random yRand = new Random();
        int y = yRand.nextInt(9);

        while (!this.sudoku.getCanPut(x, y)) {
            x = xRand.nextInt(9);
            y = yRand.nextInt(9);
        }

        // update the board
        this.sudoku.setBoard(twoDimenOriginalBoard[x][y], x, y);
        position_number[0] = x*9+y;
        position_number[1] = twoDimenOriginalBoard[x][y];
        System.out.println("number " + twoDimenOriginalBoard[x][y] + " at " + y + " " + x + " is set");
        return position_number;

    }

    private int[][] deepCopyIntArray(int[][] array){
        int[][] copy = new int[array.length][array[0].length];
        for(int row = 0; row < array.length; row++){
            for(int col = 0; col < array[0].length; col++){
                copy[row][col] = array[row][col];
            }
        }
        return copy;
    }

    private String[] intArrayToStringArray(int[][] array){
        String[] convert = new String[array.length*array[0].length];
        for(int row = 0; row < array.length; row++){
            for(int col = 0; col< array.length; col++){
                String number;
                if(array[row][col] == 0) {
                    number = "  ";
                }
                else{
                    number = Integer.toString(array[row][col]);
                }
                convert[row*array.length+col] = number;
            }
        }
        return convert;
    }

    private int[][] StringArrayToIntArray(String[] array){
        int[][] convert = new int[9][9];
        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
            int row = i/convert.length;
            int col = i%convert.length;
            if(array[i].equals("  ")){
                convert[row][col] = 0;
            }
            else{
                convert[row][col] = Integer.parseInt(array[i]);
            }
        }
        return convert;
    }

    public String[] getOriginalboard(){
        return this.originalboard;
    }
}

