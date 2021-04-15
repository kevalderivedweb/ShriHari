package com.mystudycanada.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mystudycanada.shreehari.Model.ScheduleDashBoardModel;
import com.mystudycanada.shreehari.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleDashboardAdapter extends RecyclerView.Adapter<ScheduleDashboardAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<ScheduleDashBoardModel> mDataset;
    String month = "0", dd = "0", yer = "0";



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView start_end,subject,classroom,batch,standard,year,date;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.subject = (TextView) itemView.findViewById(R.id.subject);
            this.start_end = (TextView) itemView.findViewById(R.id.start_end);
            this.classroom = (TextView) itemView.findViewById(R.id.classroom);
            this.batch = (TextView) itemView.findViewById(R.id.batch);
            this.standard = (TextView) itemView.findViewById(R.id.standard);
            this.year = (TextView) itemView.findViewById(R.id.year);
            this.date = (TextView) itemView.findViewById(R.id.date);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleDashboardAdapter(ArrayList<ScheduleDashBoardModel> categoryModels, OnItemClickListener listener) {
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
                .inflate(R.layout.item_dashboard, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.subject.setText(mDataset.get(position).getSubject());
        holder.start_end.setText(mDataset.get(position).getStart_time() + " : " + mDataset.get(position).getEnd_time());
        holder.classroom.setText(mDataset.get(position).getClass_room());
        holder.standard.setText(mDataset.get(position).getStandard());
        holder.batch.setText(mDataset.get(position).getBatch());


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy");

        try {

            Date d = sdf.parse(mDataset.get(position).getDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            month = checkDigit(cal.get(Calendar.MONTH)+1);
            dd = checkDigit(cal.get(Calendar.DATE));
            yer = checkDigit(cal.get(Calendar.YEAR));

            holder.date.setText(""+dd);
            holder.year.setText(""+month + "-" + yer);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });

    }

    public String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if(mDataset.size()>=2){
            return 2;
        }else {
            return mDataset.size();
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }


}
