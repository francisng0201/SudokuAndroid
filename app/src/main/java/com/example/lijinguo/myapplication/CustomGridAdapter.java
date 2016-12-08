package com.example.lijinguo.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.graphics.Color;

/**
 * Created by lijinguo on 11/3/16.
 */

/**
 * Adapter to help showing 1D String array on gridlayout. It is used in puzzleActivity.
 */
class CustomGridAdapter extends BaseAdapter {

    private Context context;
    private String[] board;
    private String[] userfill;
    LayoutInflater inflater;
    private TextView textbox;
    private boolean updated;

    private boolean emptyPosition[];

    public CustomGridAdapter(Context context, String[] board, boolean[] emptyPosition, String[] userfill) {
        this.context = context;
        this.board = board;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.updated = false;
        this.emptyPosition = emptyPosition;
        this.userfill = userfill;
    }

    public void update(){
        this.updated = true;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid, parent, false);
        }

        textbox = (TextView) convertView.findViewById(R.id.grid_item);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
//        params.setMargins(-1, -1, -1, -1);

        // cells have different border
        if(position == 21 || position == 24 || position == 48 || position == 51)
            textbox.setBackground(context.getResources().getDrawable(R.drawable.border_left_bottom));
        else if(position % 3 == 0 && position % 9 != 0)
            textbox.setBackground(context.getResources().getDrawable(R.drawable.border_left));
        else if((18<=position && position<=26)||(45<=position && position<=53)){
            textbox.setBackground(context.getResources().getDrawable(R.drawable.border_bottom));
        }
        else{
            textbox.setBackground(context.getResources().getDrawable(R.drawable.custom_button));
        }

        // set userfill cell text
        if(userfill != null && !board[position].equals(userfill[position])) {
            textbox.setText(userfill[position]);
            textbox.setTextColor(Color.BLUE);
        }
        else {
            textbox.setText(board[position]);
            if (!emptyPosition[position]) {
                textbox.setTextColor(Color.BLACK);
            }
        }
        convertView.setBackgroundResource(R.color.cellColor);
        textbox.setLayoutParams(params);

        // set original board cell text


        return convertView;
    }

    @Override
    public int getCount() {
        return board.length;
    }

    @Override
    public Object getItem(int position) {
        return board[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // Return true for clickable, false for not
        return emptyPosition[position];
    }
}

