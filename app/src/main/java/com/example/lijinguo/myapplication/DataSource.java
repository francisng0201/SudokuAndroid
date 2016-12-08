package com.example.lijinguo.myapplication;

/**
 * Created by lijinguo on 11/9/16.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.widget.Toast;

/**
 *  Helper class to interact with database
 */
public class DataSource {

        // Database fields
        private SQLiteDatabase database;
        private Database dbHelper;
        private String[] allColumns = { Database.COLUMN_ID,
                Database.COLUMN_NAME, Database.COLUMN_SCORE,
                Database.COLUMN_HINTS,Database.COLUMN_PASSWORD };

        public DataSource(Context context) {
            dbHelper = new Database(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
            System.out.println(database);
        }

        public void close() {
            dbHelper.close();
        }

        public void createUser(String name, String password) {
            if(name.length() == 0 || password.length() == 0){
                Toast.makeText(GlobalVariable.getAppContext(), "Username or password can't be null",
                        Toast.LENGTH_LONG).show();
                System.out.println("Username or password can't be null");
            }
            else if(isNameExist(name)){
                Toast.makeText(GlobalVariable.getAppContext(), "Username is already taken",
                        Toast.LENGTH_LONG).show();
                System.out.println("Username is already taken");
            }
            else {
                ContentValues values = new ContentValues();
                values.put(Database.COLUMN_NAME, name);
                values.put(Database.COLUMN_SCORE, 0);
                values.put(Database.COLUMN_HINTS, 0);
                values.put(Database.COLUMN_PASSWORD, password);

                long insertId = database.insert(Database.TABLE_USERS, null,
                        values);
                Toast.makeText(GlobalVariable.getAppContext(), "Successfully added new user",
                        Toast.LENGTH_LONG).show();
                System.out.println("Successfully added new user");
                System.out.println(Long.toString(insertId));
            }

        }

        public boolean isNameExist(String name){
            String whereClause =  Database.COLUMN_NAME + "=?";
            String[] valuePair = new String[]{name};
            Cursor cursor = database.query(Database.TABLE_USERS,
                    allColumns, whereClause, valuePair, null, null, null);
            if(cursor==null || cursor.getCount() == 0)
                return false;
            else
                return true;
        }

        public boolean isUserExist(String name, String password){
            String whereClause =  Database.COLUMN_NAME + "=?"+" and "+ Database.COLUMN_PASSWORD +"=?";
            String[] valuePair = new String[]{name, password};
            Cursor cursor = database.query(Database.TABLE_USERS,
                    allColumns, whereClause, valuePair, null, null, null);
            if(cursor==null || cursor.getCount() == 0)
                return false;
            else
                return true;
        }

        public void deleteUser(String username) {
            System.out.println("user deleted with name: " + username);
            database.delete(Database.TABLE_USERS, Database.COLUMN_NAME
                    + " = " + username, null);
        }

        public void del1eteAll(){
            database.delete(Database.TABLE_USERS, null,null);
        }

        public User getProfileByName(String name){
            String whereClause =  Database.COLUMN_NAME + "=?";
            String[] valuePair = new String[]{name};
            Cursor cursor = database.query(Database.TABLE_USERS,
                    allColumns, whereClause, valuePair, null, null, null);
            User user = new User();
            if (cursor.moveToFirst()) {
                do {
                    user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Database.COLUMN_ID)));
                    user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PASSWORD)));
                    user.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_SCORE)));
                    user.setHints(cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_HINTS)));

                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            return user;

        }

    /**
     * function to increment user's score and #of hints
     * @param username
     */
        public void incrementScoreHints(String username){
            SQLiteStatement stmt = database.compileStatement("UPDATE users SET score = score+1, hints = hints+1 WHERE name = ?");
            stmt.bindString(1, username);
            stmt.execute();
        }

    public void decrementHints(String username){
        SQLiteStatement stmt = database.compileStatement("UPDATE users SET hints = hints-1 WHERE name = ?");
        stmt.bindString(1, username);
        stmt.execute();
    }
    /**
     * function to add sudoku to table
     * @param username
     * @param board
     * @param userfill
     * @param timestamp
     * @param duration
     * @return true if adding sucessfully
     */
        public boolean storeNewSudoku(String username, String board, String userfill, String timestamp, String duration, String originalboard){

            ContentValues values = new ContentValues();
            values.put(Database.SUDOKU_COLUMN_NAME, username);
            values.put(Database.SUDOKU_COLUMN_BOARD, board);
            values.put(Database.SUDOKU_COLUMN_USER_FILL, userfill);
            values.put(Database.SUDOKU_COLUMN_TIMESTAMP, timestamp);
            values.put(Database.SUDOKU_COLUMN_DURATION, duration);
            values.put(Database.SUDOKU_COLUMN_ORIGINAL_BOARD, originalboard);

            long insertId = database.insert(Database.TABLE_SUDOKU, null,
                    values);
            if(insertId >= 0)
                return true;
            return false;
        }

    /**
     * get list of SudokuObject from sudoku table by username
     * @param username
     * @return
     */
     public ArrayList<SudokuObject> getSudoku(String username){
         ArrayList<SudokuObject> listOfSudokuObj = new ArrayList<SudokuObject>();
         String whereClause =  Database.COLUMN_NAME + "=?";
         String[] valuePair = new String[]{username};
         String[] columns = {Database.SUDOKU_COLUMN_ID, Database.SUDOKU_COLUMN_NAME, Database.SUDOKU_COLUMN_BOARD, Database.SUDOKU_COLUMN_USER_FILL,
                 Database.SUDOKU_COLUMN_TIMESTAMP, Database.SUDOKU_COLUMN_DURATION, Database.SUDOKU_COLUMN_ORIGINAL_BOARD};
         Cursor cursor = database.query(Database.TABLE_SUDOKU,
                 columns, whereClause, valuePair, null, null, Database.SUDOKU_COLUMN_TIMESTAMP+" ASC", null);
         if (cursor.moveToFirst()) {
             do {
                 SudokuObject sdkobj = new SudokuObject();
                 sdkobj.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_ID)));
                 sdkobj.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_NAME)));
                 sdkobj.setBoard(cursor.getString(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_BOARD)));
                 sdkobj.setUserfill(cursor.getString(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_USER_FILL)));
                 sdkobj.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_DURATION)));
                 sdkobj.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_TIMESTAMP)));
                 sdkobj.setOriginalboard(cursor.getString(cursor.getColumnIndexOrThrow(Database.SUDOKU_COLUMN_ORIGINAL_BOARD)));
                 listOfSudokuObj.add(sdkobj);

             } while (cursor.moveToNext());
         }
         if (cursor != null && !cursor.isClosed()) {
             cursor.close();
         }
         return listOfSudokuObj;
     }

    /**
     * update user's board and duration in sudoku table,
     * @param id
     * @param userfill
     * @param duration
     */
    public void updateSudoku(int id, String userfill, String duration){
        SQLiteStatement stmt = database.compileStatement("UPDATE sudoku SET userfill = ?,  duration = ? WHERE _id = ?");
        stmt.bindString(1, userfill);
        stmt.bindString(2, duration);
        stmt.bindString(3, Integer.toString(id));
        stmt.execute();
    }




//        public List<Comment> getAllUsers() {
//            List<Comment> comments = new ArrayList<Comment>();
//
//            Cursor cursor = database.query(Database.TABLE_NAME,
//                    allColumns, null, null, null, null, null);
//
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                Comment comment = cursorToComment(cursor);
//                comments.add(comment);
//                cursor.moveToNext();
//            }
//            // make sure to close the cursor
//            cursor.close();
//            return comments;
//        }

//        private Comment setCursor(Cursor cursor) {
//            User user = new User();
//            user.setId(cursor.getLong(0));
//            user.setUsername(cursor.getString(1));
//            user.setScore(cursor.getInt(2));
//            user.setHints(cursor.getString(3));
//            user.setPassword(cursor.getString(4));
//            return user;
//        }

}
