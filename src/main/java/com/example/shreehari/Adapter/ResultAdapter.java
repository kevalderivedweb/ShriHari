package com.example.shreehari.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreehari.Model.AssignmentModel;
import com.example.shreehari.Model.ResultModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<ResultModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,batch;
        ImageView img;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.batch = (TextView) itemView.findViewById(R.id.batch);
            this.img = (ImageView) itemView.findViewById(R.id.img);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ResultAdapter(ArrayList<ResultModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_resultdetails, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(mDataset.get(position).getFirstName() + " "+ mDataset.get(position).getLastName());
        holder.batch.setText(mDataset.get(position).getBatch());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
        Glide.with(holder.img.getContext()).load(mDataset.get(position).getProfile()).circleCrop().into(holder.img);
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
