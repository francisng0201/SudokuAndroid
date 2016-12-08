package com.example.lijinguo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

/**
 * the page user sees sudoku. It has textview, timer, grids and buttons.
 */
public class PuzzleActivity extends AppCompatActivity {


    private String selected_number = "";
    private TextView target_view;
    private int last_position;
    private DataSource datasource;
    private Game game;
    private String[] items;
    private String[] board;
    private int sudokuId;
    String formattedDate;
    private boolean isResumed;  //to update entry instead of creating new one
    private TextView timerValue; // timer
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int progress;
    boolean notFinished = true;
    private CustomGridAdapter gridAdapter;
    public final int BOARD_LENGTH = 9;
    private GridView gridView; //need to be final???
    ArrayList<Integer> sameRowCol;
    private Button hintBtn;
    private Button revertBtn;
    private String[] originalboard;
    int numHints;
    Stack<String[]> stack;
    boolean[] emptyPosition;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        String difficulty = "";
        isResumed = false;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle);

        stack =  new Stack<String[]>();

        sameRowCol = new ArrayList<Integer>();

        formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            difficulty = extras.getString("difficulty");
            isResumed = extras.getBoolean("isResumed");
            board= extras.getStringArray("board");
            items = extras.getStringArray("userfill");
            sudokuId = extras.getInt("id");
            originalboard = extras.getStringArray("originalboard");
        }
        // open database
        datasource = new DataSource(this);
        datasource.open();
        // A brand new game
        if(!isResumed) {
            game = new Game(difficulty);
            originalboard = game.getOriginalboard();
            items = game.getPuzzle();
            // make a copy of the original board
            board = game.deepCopy(items);
        }
        // resumed game
        else{
            timeSwapBuff = extras.getLong("duration");
            progress = extras.getInt("progress");
            game = new Game(board, items, originalboard);
            board = game.getPuzzle();
        }

        String[] itemsCopy = game.deepCopy(items);
        stack.push(itemsCopy);

        emptyPosition = game.getEmptyPosition();
        gridView = (GridView) findViewById(R.id.view_root);

        gridAdapter = new CustomGridAdapter(PuzzleActivity.this, board, emptyPosition, items);
        gridView.setAdapter(gridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: only change background color. don't reset the cell.
                if(target_view != null){
                    if(last_position == 21 || last_position == 24 || last_position == 48 || last_position == 51)
                        target_view.setBackgroundResource(R.drawable.border_left_bottom);
                    else if(last_position % 3 == 0 && last_position % 9 != 0)
                        target_view.setBackgroundResource(R.drawable.border_left);
                    else if((18<=last_position && last_position<=26)||(45<=last_position && last_position<=53)){
                        target_view.setBackgroundResource(R.drawable.border_bottom);
                    }
                    else{
                        target_view.setBackgroundResource(R.drawable.custom_button);
                    }

                }
                TextView tv = (TextView)((LinearLayout )view).getChildAt(0);
                target_view = tv;
                if(sameRowCol != null)
                    uncolorSameRowCol();
                colorSameRowCol(position);

                tv.setBackgroundResource(R.drawable.selected_cell);
                last_position = position;

            }

        });

        // set up timer view
        timerValue = (TextView) findViewById(R.id.timerValue);

        // start/pause button
        final Switch switchButton = (Switch) findViewById(R.id.switchButton);
        if(progress!=100){
            switchButton.setChecked(true);
        }
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                } else {
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                }
            }
        });
//
//        if(switchButton.isChecked()){
//            startTime = SystemClock.uptimeMillis();
//            customHandler.postDelayed(updateTimerThread, 0);
//        }
//        else {
//            timeSwapBuff += timeInMilliseconds;
//            customHandler.removeCallbacks(updateTimerThread);
//        }

        User user = datasource.getProfileByName(GlobalVariable.getSessionUsername());
        numHints = user.getHints();
        hintBtn = (Button) findViewById(R.id.hints);
        hintBtn.setText(Integer.toString(numHints));

        hintBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(numHints > 0){
                    //TODO: need to update user table hints as well
                    int[] position_number = game.hint();
                    View hintView = gridView.getChildAt(position_number[0]);
                    TextView tv = (TextView)((LinearLayout )hintView).getChildAt(0);
                    tv.setTextColor(Color.GREEN);
                    tv.setText( Integer.toString(position_number[1]));
                    items[position_number[0]] = Integer.toString(position_number[1]);
                    datasource.decrementHints(GlobalVariable.getSessionUsername());
                    hintBtn.setText(Integer.toString(numHints -1));
                    numHints = numHints -1;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not enough hints!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        revertBtn = (Button) findViewById(R.id.revert);

        revertBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String[] lastboard = null;
                if(stack.size()>1){
                     stack.pop();
                    lastboard = stack.pop();
                }

                if(lastboard != null) {
                    gridAdapter = new CustomGridAdapter(PuzzleActivity.this, board, emptyPosition, lastboard);
                    gridView.setAdapter(gridAdapter);
//                    System.out.println(lastboard);
                    items = lastboard;
                    String[] itemsCopy = game.deepCopy(items);
                    stack.push(itemsCopy);
                }


            }
        });

        Button goBack = (Button) findViewById(R.id.Back);

        // Capture button clicks
        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent backIntent = new Intent(PuzzleActivity.this,
                        DifficultyActivity.class);
                startActivity(backIntent);
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.addNumbers);

        LinearLayout middlelayer = new LinearLayout(this);
        middlelayer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        for(int i = 0; i< 9;i++)
        {
            //set the properties for button
            Button btnTag = new Button(this);

            btnTag.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Button b = (Button)v;
                    String buttonText = b.getText().toString();
                    selected_number = buttonText;

                    boolean valid = false;
                    // pass to controller
                    if(!selected_number.equals(""))
                        valid = game.validFill(last_position, selected_number);

                    if(valid){
                        items[last_position] = selected_number;
                        target_view.setText(selected_number);
                        // update the stack of steps.
                        String[] itemsCopy = game.deepCopy(items);
                        stack.push(itemsCopy);
                    }
                    if(game.win()&& notFinished){
                        Toast.makeText(GlobalVariable.getAppContext(), "Congratualtions!",
                                Toast.LENGTH_LONG).show();
                        System.out.println("Congratualtions!");
                        game.updateScoreHints();
                        switchButton.performClick();
                        notFinished = false;

                    }
                }
            });

            LayoutParams params = new LayoutParams(120, 150);
            int margin = (int) getResources().getDimension(R.dimen.gridMargin);
            params.setMargins(margin, margin, margin, margin);
            btnTag.setLayoutParams(params);
            btnTag.setText(Integer.toString(i+1));

            //add button to the layout
            middlelayer.addView(btnTag);

        }

        layout.addView(middlelayer);

    }

    private void uncolorSameRowCol(){
        for(Integer loc: sameRowCol) {
            View view = gridView.getChildAt(loc);
            view.setBackgroundResource(R.color.cellColor);
        }
    }

    private void colorSameRowCol(int position){

        sameRowCol.clear();
        int row = position/BOARD_LENGTH;
        int col = position%BOARD_LENGTH;
        for(int i = col; i>= 0;i--){
            int pos = row*BOARD_LENGTH+i;
            sameRowCol.add(pos);
        }
        for(int j = col+1; j<=8; j++){
            int pos = row*BOARD_LENGTH+j;
            sameRowCol.add(pos);
        }
        for(int i = row; i>= 0;i--){
            int pos = i*BOARD_LENGTH+col;
            sameRowCol.add(pos);
        }
        for(int j = row+1; j<=8; j++){
            int pos = j*BOARD_LENGTH+col;
            sameRowCol.add(pos);
        }
        for(Integer loc: sameRowCol) {
            View view = gridView.getChildAt(loc);
            view.setBackgroundResource(R.color.hightlight);

        }
    }
    /**
     * timer logic
     */
    private Runnable updateTimerThread = new Runnable(){
        public void run(){
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updatedTime = timeSwapBuff +timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerValue.setText(""+ mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };


    @Override
    protected void onStart(){
        super.onStart();
        if(progress!=100){
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }
        else{
            int secs = (int) (timeSwapBuff / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerValue.setText(""+ mins + ":" + String.format("%02d", secs));

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        String userFill = TextUtils.join("|", items);
        if(!isResumed) {
            String startingboard = TextUtils.join("|", board);
            datasource.storeNewSudoku(GlobalVariable.getSessionUsername(), startingboard, userFill, formattedDate, Long.toString(updatedTime), TextUtils.join("|", originalboard));
        }
        else if(progress!= 100){    // if sudoku already complete, no need to update table
            datasource.updateSudoku(sudokuId,userFill, Long.toString(updatedTime));
        }
        customHandler.removeCallbacks(updateTimerThread);
        datasource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(progress!=100){
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }
        else{
            int secs = (int) (timeSwapBuff / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerValue.setText(""+ mins + ":" + String.format("%02d", secs));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }
}


