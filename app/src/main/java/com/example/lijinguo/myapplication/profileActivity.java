package com.example.lijinguo.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.Gravity;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;

import java.util.ArrayList;


//public class profileActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener{
public class profileActivity extends AppCompatActivity{
//    GoogleApiClient mGoogleApiClient;
    DataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


//        mGoogleApiClient = ((GlobalVariable) this.getApplication()).getClient();

//        mGoogleApiClient.connect();

        datasource = new DataSource(this);
        datasource.open();
        final User user = datasource.getProfileByName(GlobalVariable.getSessionUsername());

        // fill the view with user info
        TextView nameView = (TextView)findViewById(R.id.username);
        nameView.setText(user.getUsername());

        TextView scoreView = (TextView)findViewById(R.id.score);
        scoreView.setText("Score:  "+Integer.toString(user.getScore()));

        TextView hintView = (TextView)findViewById(R.id.hint);
        hintView.setText("Hints:  "+Integer.toString(user.getHints()));

        // history view
        //from sudoku db, read all the sudoku of this user. sorted by the time, display the time.
        final ArrayList<SudokuObject> listOfSudokuObj = datasource.getSudoku(GlobalVariable.getSessionUsername());
        LinearLayout history = (LinearLayout)findViewById(R.id.history);
        for( int i = 0; i < listOfSudokuObj.size(); i++ )
        {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);
            final SudokuObject sdkobj = listOfSudokuObj.get(i);
            // set progress bar
            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            int padding = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
            progressBar.setPadding(padding, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,LinearLayout.LayoutParams.WRAP_CONTENT );
            progressBar.setLayoutParams(params);
            final int progress = countProgress(sdkobj.getUserfill());
            progressBar.setProgress(progress);
            progressBar.setMax(100);
            TextView timeSpent = new TextView(this);
            timeSpent.setPadding(padding, 0, 0, 0);
            int secs = (int) (Long.parseLong(sdkobj.getDuration()) / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timeSpent.setText(""+ mins + ":" + String.format("%02d", secs));
            TextView timestamp = new TextView(this);
            timestamp.setText(sdkobj.getTimestamp());
            //        txtView.setGravity(Gravity.CENTER);
            //        txtView.setTextSize(20);
            timestamp.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent puzzleIntent = new Intent(profileActivity.this,
                            PuzzleActivity.class);
                    String[] board = sdkobj.getBoard();
                    String[] userfill = sdkobj.getUserfill();
                    String[] originalboard = sdkobj.getOriginalboard();
                    String duration = sdkobj.getDuration();
                    int id = sdkobj.getId();
                    puzzleIntent.putExtra("isResumed", true);
                    puzzleIntent.putExtra("board", board);
                    puzzleIntent.putExtra("userfill", userfill);
                    puzzleIntent.putExtra("id", id);
                    puzzleIntent.putExtra("duration", Long.parseLong(duration));
                    puzzleIntent.putExtra("progress", progress);
                    puzzleIntent.putExtra("hints", user.getHints());
                    puzzleIntent.putExtra("originalboard", originalboard);
                    startActivity(puzzleIntent);
                }
            });

            row.addView(timestamp);
            row.addView(progressBar);
            row.addView(timeSpent);
            history.addView(row);
        }



        // button view
        // new game event
        Button startNewGame = (Button) findViewById(R.id.startNewGame);
        startNewGame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent difficultyIntent = new Intent(profileActivity.this,
                        DifficultyActivity.class);
                startActivity(difficultyIntent);
            }
        });

        // signout event
        Button signout = (Button) findViewById(R.id.signout);
        signout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.signout:
                        GlobalVariable.setSessionUsername("");

                        // sign out but still remember user's account
//                        signOut();

                        // need to select account again
//                        revokeAccess();

//                        if (mGoogleApiClient.isConnected()) {
//                            mGoogleApiClient.disconnect();
//                        }

                        datasource.close();

                        Intent mainIntent = new Intent(profileActivity.this,
                                MainActivity.class);
                        startActivity(mainIntent);

                        break;
                }
            }
        });

    }

//    private void signOut() {
//
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // ...
//                    }
//                });
//    }

//    private void revokeAccess() {
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // ...
//                    }
//                });
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

    protected void onStart() {
        super.onStart();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient= new GoogleApiClient.Builder(this)
//                .enableAutoManage(this ,  this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mGoogleApiClient.connect();

    }

    protected void onStop() {
        super.onStop();
        datasource.close();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
////        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    /**
     * count how many cells out of 81 cells are filled
     * @param userfill
     * @return
     */
    public int countProgress(String[] userfill){
        int count = 0;
        for(String cell: userfill){
            if(!cell.equals("  "))
                count++;
        }
        float percentage = (float) count/81;
        return (int) (percentage*100);
    }
}

