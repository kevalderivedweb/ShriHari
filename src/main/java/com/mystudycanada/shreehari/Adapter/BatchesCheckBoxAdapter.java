package com.mystudycanada.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.BatchModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class BatchesCheckBoxAdapter extends RecyclerView.Adapter<BatchesCheckBoxAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<BatchModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cancel;
        private final TextView bachesname;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.cancel = (ImageView) itemView.findViewById(R.id.cancel);
            this.bachesname = (TextView) itemView.findViewById(R.id.bachesname);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BatchesCheckBoxAdapter(ArrayList<BatchModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_batches, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.bachesname.setText(mDataset.get(position).getBatch_name());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
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
