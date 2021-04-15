package com.mystudycanada.shreehari.Adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.ColorModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<ColorModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView color,colortype;


        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.color = (TextView) itemView.findViewById(R.id.color);
            this.colortype = (TextView) itemView.findViewById(R.id.colortype);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ColorAdapter(ArrayList<ColorModel> categoryModels, OnItemClickListener listener) {
        mDataset = categoryModels;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_colorview, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        int color = Color.parseColor(mDataset.get(position).getColor());

        holder.color.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        holder.colortype.setText(mDataset.get(position).getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }


}
