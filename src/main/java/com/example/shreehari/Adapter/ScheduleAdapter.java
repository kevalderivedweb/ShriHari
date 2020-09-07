package com.example.shreehari.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreehari.Model.AssignmentModel;
import com.example.shreehari.Model.ScheduleModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<ScheduleModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        // each data item is just a string in this case

        TextView subject,standard,batch,classroom,time,date;

        public MyViewHolder(View v) {
            super(v);
            this.subject = (TextView) itemView.findViewById(R.id.subject);
            this.standard = (TextView) itemView.findViewById(R.id.standard);
            this.time = (TextView) itemView.findViewById(R.id.time);
            this.batch = (TextView) itemView.findViewById(R.id.batch);
            this.classroom = (TextView) itemView.findViewById(R.id.classroom);
            this.date = (TextView) itemView.findViewById(R.id.classroom);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(ArrayList<ScheduleModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_shcedule, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.subject.setText(mDataset.get(position).getSubject());
        holder.standard.setText(mDataset.get(position).getStandard());
        holder.batch.setText(mDataset.get(position).getBatch());
        holder.classroom.setText(mDataset.get(position).getClass_room());
        holder.date.setText(mDataset.get(position).getDate());
        holder.time.setText(mDataset.get(position).getStart_time() + " : " + mDataset.get(position).getEnd_time());
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
