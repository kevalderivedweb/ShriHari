package com.example.shreehari.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreehari.Model.AttandanceModel;
import com.example.shreehari.R;

import java.util.ArrayList;

public class AttandanceAdapter extends RecyclerView.Adapter<AttandanceAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<AttandanceModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        LinearLayout originalLayout;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.title = (TextView) itemView.findViewById(R.id.name);
            this.originalLayout = (LinearLayout) itemView.findViewById(R.id.originalLayout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AttandanceAdapter(ArrayList<AttandanceModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_attandance, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(mDataset.get(position).getFirst_name());
      // holder.letter.setText(mDataset.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDataset.get(position).getIs_absent().equals("0")){
                    mDataset.get(position).setIs_absent("1");
                    holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_red));

                }else {
                    mDataset.get(position).setIs_absent("0");
                    holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_green));

                }
                listener.onItemClick(position);
            }
        });

        if(mDataset.get(position).getIs_absent().equals("1")){
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_red));
        }else{
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_green));

        }

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
