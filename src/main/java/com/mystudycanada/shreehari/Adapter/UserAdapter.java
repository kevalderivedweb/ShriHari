package com.mystudycanada.shreehari.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mystudycanada.shreehari.Model.UserModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private final Context mContext;
    private ArrayList<UserModel> mDataset;

     OnItemClickListener listener;

    public void filterList(ArrayList<UserModel> filterdNames) {
        mDataset = filterdNames;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvUserName;
        public ImageView imageView;
        public LinearLayout user;

        public MyViewHolder(View v) {
            super(v);
            this.imageView = (ImageView) itemView.findViewById(R.id.imgProfile);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            this.user = (LinearLayout) itemView.findViewById(R.id.user);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(Context listener, ArrayList<UserModel> myDataset,OnItemClickListener listener1) {
        mDataset = myDataset;
        mContext = listener;
        this.listener = listener1;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.row_user, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvName.setText(mDataset.get(position).getFirstName() + " " + mDataset.get(position).getLastName() + " (" + mDataset.get(position).getCoachingNo()+")");
        holder.tvUserName.setText(mDataset.get(position).getFirstName());
        Glide.with(holder.imageView.getContext()).load(mDataset.get(position).getProfile()).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().error(R.drawable.ic_action_name).into(holder.imageView);
        holder.user.setOnClickListener(new View.OnClickListener() {
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
