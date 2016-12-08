package com.example.lijinguo.myapplication;

/**
 * Created by lijinguo on 11/9/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper{

        public static final String TABLE_USERS = "users";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_HINTS = "hints";
        public static final String COLUMN_PASSWORD = "password";

        private static final String DATABASE_NAME = "sudoku.db";
        private static final int DATABASE_VERSION = 3;

        // a table to store sudoku
        public static final String TABLE_SUDOKU = "sudoku";
        public static final String SUDOKU_COLUMN_ID = "_id";
        public static final String SUDOKU_COLUMN_NAME = "name";
        public static final String SUDOKU_COLUMN_BOARD = "board";
        public static final String SUDOKU_COLUMN_USER_FILL = "userfill";
        public static final String SUDOKU_COLUMN_TIMESTAMP = "timestamp";
        public static final String SUDOKU_COLUMN_DURATION = "duration";
        public static final String SUDOKU_COLUMN_ORIGINAL_BOARD = "originalboard";

        // Database creation sql statement
        private static final String DATABASE_CREATE = "create table "
                + TABLE_USERS + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_NAME
                + " text not null, " + COLUMN_SCORE
                + " integer, " + COLUMN_HINTS
                + " integer, " + COLUMN_PASSWORD
                + " text not null);";

        // sudoku table
        private static final String DATABASE_CREATE_SUDOKU = "create table "
                + TABLE_SUDOKU + "( " + SUDOKU_COLUMN_ID
                + " integer primary key autoincrement, " + SUDOKU_COLUMN_NAME
                + " text not null, " + SUDOKU_COLUMN_BOARD
                + " text not null, " + SUDOKU_COLUMN_USER_FILL
                + " text not null, " + SUDOKU_COLUMN_TIMESTAMP
                + " text not null, " + SUDOKU_COLUMN_DURATION
                + " text not null, " + SUDOKU_COLUMN_ORIGINAL_BOARD
                + " text not null);";


        public Database(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
            database.execSQL(DATABASE_CREATE_SUDOKU);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(Database.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUDOKU);
            onCreate(db);
        }


}
