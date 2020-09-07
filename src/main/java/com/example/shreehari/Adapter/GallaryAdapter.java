package com.example.shreehari.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreehari.Model.GallaryModel;
import com.example.shreehari.R;
import com.example.shreehari.ui.GalleryDetails;

import java.util.ArrayList;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.MyViewHolder> {

    private final OnItemClickListener listener;
    private final String mValue;
    private ArrayList<GallaryModel> mDataset;
    private ArrayList<String> mImageArray = new ArrayList<>();

    public void setselection(int item) {

        if(mDataset.get(item).getSelection().equals("Yes")){
            mImageArray.remove(mDataset.get(item).getCategory_image());
            mDataset.get(item).setSelection("No");
        }else {
            mImageArray.add(mDataset.get(item).getCategory_image());
            mDataset.get(item).setSelection("Yes");
        }


        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;

        // each data item is just a string in this case


        public MyViewHolder(View v) {
            super(v);
            this.title = (TextView) itemView.findViewById(R.id.txt);
            this.img = (ImageView) itemView.findViewById(R.id.img);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GallaryAdapter(String value,ArrayList<GallaryModel> categoryModels, OnItemClickListener listener) {
        mDataset = categoryModels;
        this.listener = listener;
        this.mValue = value;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_galleryview, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(mValue.equals("1")){
            holder.title.setVisibility(View.GONE);
        }
        if(mDataset.get(position).getSelection().equals("Yes")){
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText("Selected");
        }
        holder.title.setText(mDataset.get(position).getCategory_name());
        Glide.with(holder.img.getContext()).load(mDataset.get(position).getCategory_image()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,mImageArray);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongItemClick(position,mImageArray);
                return false;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int item, ArrayList<String> mImageArray);
        void onLongItemClick(int item, ArrayList<String> mImageArray);
    }


}
