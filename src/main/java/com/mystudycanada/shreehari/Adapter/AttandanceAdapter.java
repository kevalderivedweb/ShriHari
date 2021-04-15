package com.mystudycanada.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.AttandanceModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class AttandanceAdapter extends RecyclerView.Adapter<AttandanceAdapter.MyViewHolder> {

    private OnItemClickListener listener;
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
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(mDataset.get(position).getFirst_name() + " " + mDataset.get(position).getLast_name() + " (" + mDataset.get(position).getCoaching_reg_no()+")");
      // holder.letter.setText(mDataset.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                if(!mDataset.get(position).getIs_absent().equals("3")){
                    if(mDataset.get(position).getIs_absent().equals("1")){
                        mDataset.get(position).setIs_absent("0");
                        holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_red));
                        Log.e("AttandaceIds", "Is number " +  mDataset.get(position).getId()+ " Set as  0");
                    }else {
                        mDataset.get(position).setIs_absent("1");
                        holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_green));
                        Log.e("AttandaceIds", "Is number " +  mDataset.get(position).getId()+ " Set as  1");
                    }

                }
                listener.onItemClick(position);

            }
        });

        if(mDataset.get(position).getIs_absent().equals("0")){
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_red));
        }else if(mDataset.get(position).getIs_absent().equals("1")){
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_green));
        }else if(mDataset.get(position).getIs_absent().equals("3"))  {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_yellow));
        }else  {
            holder.originalLayout.setBackground(holder.originalLayout.getContext().getResources().getDrawable(R.drawable.corner_background_gray));

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
