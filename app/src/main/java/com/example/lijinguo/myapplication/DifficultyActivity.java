

package com.example.lijinguo.myapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.Intent;


public class DifficultyActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from select_difficulty.xml
        setContentView(R.layout.select_difficulty);

        // Locate the button in select_difficulty.xml
        Button button_easy = (Button) findViewById(R.id.Easy);
        Intent easyIntent = new Intent(DifficultyActivity.this,
                PuzzleActivity.class);
        easyIntent.putExtra("difficulty","Easy");
        onclickEvent(button_easy, easyIntent);

        Button button_medium = (Button) findViewById(R.id.Medium);
        Intent mediumIntent = new Intent(DifficultyActivity.this,
                PuzzleActivity.class);
        mediumIntent.putExtra("difficulty","Medium");
        onclickEvent(button_medium, mediumIntent);

        Button button_hard = (Button) findViewById(R.id.Hard);
        Intent hardIndent = new Intent(DifficultyActivity.this,
                PuzzleActivity.class);
        hardIndent.putExtra("difficulty","Hard");
        onclickEvent(button_hard, hardIndent);

        Button myProfile = (Button) findViewById(R.id.profile);
        Intent profileIndent = new Intent(DifficultyActivity.this,
                profileActivity.class);

        Button multiplePlayer = (Button) findViewById(R.id.multiple_player);
        Intent multiplePlayerIntent = new Intent(DifficultyActivity.this,
                MultiplePlayer.class);
        onclickEvent(multiplePlayer, multiplePlayerIntent);


        Button button_back = (Button) findViewById(R.id.goBack);
        Intent backIndent = new Intent(DifficultyActivity.this,
                MainActivity.class);
        onclickEvent(button_back, backIndent);

        if(GlobalVariable.getSessionUsername().equals("")){
            myProfile.setVisibility(View.GONE);
        }
        else{
            button_back.setVisibility(View.GONE);
        }
        onclickEvent(myProfile, profileIndent);



    }





    private void onclickEvent(Button btn, final Intent newActivity) {


        // Capture button clicks
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class

                startActivity(newActivity);
            }
        });
    }
}