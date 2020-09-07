package com.example.shreehari.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreehari.Model.ExamModel;
import com.example.shreehari.Model.StudentModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<StudentModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,standard,reg_num;
        ImageView img;


        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.standard = (TextView) itemView.findViewById(R.id.standard);
            this.reg_num = (TextView) itemView.findViewById(R.id.reg_num);
            this.img = (ImageView) itemView.findViewById(R.id.img);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentAdapter(ArrayList<StudentModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_student_search, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(mDataset.get(position).getFirst_name()+" "+ mDataset.get(position).getLast_name());
        holder.standard.setText(mDataset.get(position).getStandard());
        holder.reg_num.setText(mDataset.get(position).getCoaching_reg_no());
        Glide.with(holder.img.getContext()).load(mDataset.get(position).getProfile_pic()).into(holder.img);
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
