package com.example.lijinguo.myapplication;

import java.util.ArrayList;

/**
 * Created by lijinguo on 11/15/16.
 */

/**
 * Each SudokuObject in sudoku table has an id, username, orignial board, user modified board, a timestamp, and duration
 */
public class  SudokuObject{
    private int id;
    private String username;
    private String[] board;
    private String[] userfill;

    private String timestamp;
    private String duration;
    private String[] originalboard;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setBoard(String board){
        this.board = board.split("\\|");
    }

    public String[] getBoard(){
        return this.board;

    }

    public void setUserfill(String userfill){
        this.userfill = userfill.split("\\|");
    }

    public String[] getUserfill(){
//        for(int i = 0; i< userfill.length; i++) {
//            System.out.println(userfill[i]);
//        }
        return this.userfill;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    public String getDuration(){
        return this.duration;
    }

    public void setOriginalboard(String originalboard){
        this.originalboard = originalboard.split("\\|");
    }

    public String[] getOriginalboard(){
        return this.originalboard;
    }
}
