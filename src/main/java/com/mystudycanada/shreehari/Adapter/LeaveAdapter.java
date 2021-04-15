package com.mystudycanada.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.LeaveModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;

import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private final Context mContext;
    private ArrayList<LeaveModel> mDataset;
    private UserSession session;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,name,remark,submit;
        Spinner batch;
        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.remark = (TextView) itemView.findViewById(R.id.remark);
            this.batch = (Spinner) itemView.findViewById(R.id.batch);
            this.submit = (TextView) itemView.findViewById(R.id.submit);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LeaveAdapter(Context context, ArrayList<LeaveModel> categoryModels, OnItemClickListener listener) {
        mDataset = categoryModels;
        this.listener = listener;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_leave, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        session = new UserSession(mContext);
        holder.batch.setVisibility(View.GONE);
        holder.submit.setVisibility(View.GONE);
        holder.date.setText(mDataset.get(position).getBreak_from() +" To "+ mDataset.get(position).getBreak_to());
        if(session.getUserType().equals("admin")){
            holder.name.setText(mDataset.get(position).getFirst_name() + " " + mDataset.get(position).getLast_name() + " (" +mDataset.get(position).getRegisterNumber()+")");

        }else {
            holder.name.setText(mDataset.get(position).getFirst_name() + " " + mDataset.get(position).getLast_name());

        }        holder.remark.setText(mDataset.get(position).getRemarks());
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
