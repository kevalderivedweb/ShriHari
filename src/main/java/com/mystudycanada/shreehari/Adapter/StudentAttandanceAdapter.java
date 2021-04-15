package com.mystudycanada.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.AttandanceStudentModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class StudentAttandanceAdapter extends RecyclerView.Adapter<StudentAttandanceAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<AttandanceStudentModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView status;
        private final TextView date;
        private final LinearLayout originalLayout;


        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.originalLayout = (LinearLayout) itemView.findViewById(R.id.originalLayout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentAttandanceAdapter(ArrayList<AttandanceStudentModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_student_attandace, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.status.setText(mDataset.get(position).getStatus().toUpperCase());
        holder.date.setText(mDataset.get(position).getDate());

        if(mDataset.get(position).getStatus().equals("presence")) {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_green));
        }else if(mDataset.get(position).getStatus().equals("absent")) {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_red));
        }else if(mDataset.get(position).getStatus().equals("leave")) {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_yellow));
        }else {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_gray));

        }

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
