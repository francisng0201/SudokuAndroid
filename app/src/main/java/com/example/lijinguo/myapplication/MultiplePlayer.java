package com.example.lijinguo.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class MultiplePlayer extends AppCompatActivity {
    private Game game;
    private String[] items;
    private GridView gridView;
    private CustomGridAdapter gridAdapter;
    private TextView target_view;
    private int last_position;
    ArrayList<Integer> sameRowCol;
    public final int BOARD_LENGTH = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_player);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();

//            game = new Game("easy");
//            items = game.getPuzzle();
//            boolean[] emptyPosition = game.getEmptyPosition();
//            gridView = (GridView) findViewById(R.id.multi_player_grid);
//            gridAdapter = new CustomGridAdapter(MultiplePlayer.this, items, emptyPosition, items);
//            gridView.setAdapter(gridAdapter);
//
//            sameRowCol = new ArrayList<Integer>();
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                System.out.println(position);
////                Toast.makeText(getApplicationContext(),
////                        (String)(gridView.getItemAtPosition(position)),
////                        Toast.LENGTH_SHORT).show();
//
////                text.setText((String);
////                 cell = (TextView) view;
////                  cell.setText("1");
//
//
//                    if(target_view != null){
//                        if(last_position == 21 || last_position == 24 || last_position == 48 || last_position == 51)
//                            target_view.setBackgroundResource(R.drawable.border_left_bottom);
//                        else if(last_position % 3 == 0 && last_position % 9 != 0)
//                            target_view.setBackgroundResource(R.drawable.border_left);
//                        else if((18<=last_position && last_position<=26)||(45<=last_position && last_position<=53)){
//                            target_view.setBackgroundResource(R.drawable.border_bottom);
//                        }
//                        else{
//                            target_view.setBackgroundResource(R.drawable.custom_button);
//                        }
//
//                    }
//                    TextView tv = (TextView)((LinearLayout)view).getChildAt(0);
//                    target_view = tv;
//                    if(sameRowCol != null)
//                        uncolorSameRowCol();
//                    colorSameRowCol(position);
//
//                    tv.setBackgroundResource(R.drawable.selected_cell);
//                    last_position = position;
//
//                }
//
//            });
//
//            Bundle args = new Bundle();
//            args.putString("board", TextUtils.join("|", items));
//            fragment.setArguments(args);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }





    }
//
//    private void uncolorSameRowCol(){
//        for(Integer loc: sameRowCol) {
//            View view = gridView.getChildAt(loc);
//            view.setBackgroundResource(R.color.cellColor);
//        }
//    }
//
//    private void colorSameRowCol(int position){
//
//        sameRowCol.clear();
//        int row = position/BOARD_LENGTH;
//        int col = position%BOARD_LENGTH;
//        for(int i = col; i>= 0;i--){
//            int pos = row*BOARD_LENGTH+i;
//            sameRowCol.add(pos);
//        }
//        for(int j = col+1; j<=8; j++){
//            int pos = row*BOARD_LENGTH+j;
//            sameRowCol.add(pos);
//        }
//        for(int i = row; i>= 0;i--){
//            int pos = i*BOARD_LENGTH+col;
//            sameRowCol.add(pos);
//        }
//        for(int j = row+1; j<=8; j++){
//            int pos = j*BOARD_LENGTH+col;
//            sameRowCol.add(pos);
//        }
//        for(Integer loc: sameRowCol) {
//            View view = gridView.getChildAt(loc);
////            view.setBackgroundColor(Color.CYAN);
//            view.setBackgroundResource(R.color.hightlight);
////            LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.your_shape)
////            System.out.println("object");
////            System.out.println(loc);
//        }
//    }


}
