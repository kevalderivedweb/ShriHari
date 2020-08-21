package com.example.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shreehari.R;
import com.example.shreehari.ui.StandardModel;

import java.util.ArrayList;

public class CustomAdapter2 extends ArrayAdapter<StandardModel> {

    LayoutInflater flater;
    ArrayList<StandardModel> standardModels;
     public CustomAdapter2(Activity context, int resouceId, int textviewId, ArrayList<StandardModel> list){

    super(context,resouceId,textviewId, list);
    flater = context.getLayoutInflater();
         standardModels = list;

}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        @SuppressLint("ViewHolder") View rowview = flater.inflate(R.layout.item_spinner_2,null,true);
        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        txtTitle.setText(standardModels.get(position).getCoaching().toString());

        return rowview;
    }
}