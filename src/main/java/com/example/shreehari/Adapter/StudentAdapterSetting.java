package com.example.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreehari.Model.StudentModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class StudentAdapterSetting extends RecyclerView.Adapter<StudentAdapterSetting.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<StudentModel> mDataset;


    public void filterList(ArrayList<StudentModel> filterdNames) {
        mDataset = filterdNames;
        notifyDataSetChanged();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img;
        LinearLayout originalLayout;


        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.img = (ImageView) itemView.findViewById(R.id.img);
            this.originalLayout = (LinearLayout) itemView.findViewById(R.id.originalLayout);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentAdapterSetting(ArrayList<StudentModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_student, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(mDataset.get(position).getFirst_name()+" "+ mDataset.get(position).getLast_name());


        if(mDataset.get(position).getSelected().equals("1")){
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_gray));
        }else {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.backgraound_white_round));

        }
        Glide.with(holder.img.getContext()).load(mDataset.get(position).getProfile_pic()).circleCrop().into(holder.img);
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
