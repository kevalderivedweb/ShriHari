package com.example.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<BatchModel> {

    LayoutInflater flater;

    ArrayList<BatchModel> rowItem;
     public CustomAdapter(Activity context, int resouceId, int textviewId, ArrayList<BatchModel> list){

    super(context,resouceId,textviewId, list);
    flater = context.getLayoutInflater();
         rowItem = list;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") View rowview = flater.inflate(R.layout.item_spinner,null,true);
        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        txtTitle.setText(rowItem.get(position).getBatch_time().toString());

       /* TextView txtTitle1 = (TextView) rowview.findViewById(R.id.title_2);
        txtTitle1.setText(rowItem.get(position).getBatch_time().toString());*/
        return rowview;
    }

}