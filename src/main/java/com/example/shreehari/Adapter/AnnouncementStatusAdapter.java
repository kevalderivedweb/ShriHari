package com.example.shreehari.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreehari.Model.AnnouncementModel;
import com.example.shreehari.Model.AnnouncementsStaus;
import com.example.shreehari.Model.StudentModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class AnnouncementStatusAdapter extends RecyclerView.Adapter<AnnouncementStatusAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<AnnouncementsStaus> mDataset;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,txt_later,batch,number;

        public MyViewHolder(View v) {
            super(v);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.txt_later = (TextView) itemView.findViewById(R.id.txt_later);
            this.batch = (TextView) itemView.findViewById(R.id.batch);
            this.number = (TextView) itemView.findViewById(R.id.number);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnnouncementStatusAdapter(ArrayList<AnnouncementsStaus> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_announsment_status, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.name.setText(mDataset.get(position).getName());
        holder.txt_later.setText(mDataset.get(position).getTxt_latter());
        if(mDataset.get(position).getBatch().equals("")){
            holder.batch.setVisibility(View.GONE);
        }
        holder.batch.setText(mDataset.get(position).getBatch());
        holder.number.setText(mDataset.get(position).getNumber());

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
