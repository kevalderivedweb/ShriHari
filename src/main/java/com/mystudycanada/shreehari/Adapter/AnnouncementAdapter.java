package com.mystudycanada.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.AnnouncementModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<AnnouncementModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView sendby;
        private final TextView date;
        private final TextView letter;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.sendby = (TextView) itemView.findViewById(R.id.send_by);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.letter = (TextView) itemView.findViewById(R.id.letter);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnnouncementAdapter(ArrayList<AnnouncementModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_announcement, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(mDataset.get(position).getTitle());
        holder.sendby.setText(mDataset.get(position).getSend_by());
        holder.date.setText(mDataset.get(position).getDate());
        Character character = mDataset.get(position).getTitle().charAt(0);
       holder.letter.setText(""+character.toString().toUpperCase()
               );
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
