package com.mystudycanada.shreehari.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.AssignmentModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<AssignmentModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,standard,date,date1;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.standard = (TextView) itemView.findViewById(R.id.standard);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.date1 = (TextView) itemView.findViewById(R.id.date2);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AssignmentAdapter(ArrayList<AssignmentModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_assignment, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(mDataset.get(position).getClass_work());
        holder.standard.setText(mDataset.get(position).getStandard());
        holder.date.setText("Due date : "+mDataset.get(position).getDate());
        holder.date1.setText("Date : "+mDataset.get(position).getDue_date());
      // holder.letter.setText(mDataset.get(position).getTitle());
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
