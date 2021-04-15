package com.mystudycanada.shreehari.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.mystudycanada.shreehari.Model.LeaveDashBoardModel;
import com.mystudycanada.shreehari.R;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<LeaveDashBoardModel> McategoryModels;
    private final OnItemClickListener listener;
    public ImageAdapter(Context context, ArrayList<LeaveDashBoardModel> categoryModels,OnItemClickListener listener) {
        this.mContext = context;  
        this.McategoryModels = categoryModels;
        this.listener = listener;
    }
  
    @Override  
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }  

    @Override  
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_dashboard_leave, container, false);

        TextView subject = layout.findViewById(R.id.subject);
        TextView start_end = layout.findViewById(R.id.start_end);
        TextView classroom = layout.findViewById(R.id.classroom);
        TextView batch = layout.findViewById(R.id.batch);
        TextView standard = layout.findViewById(R.id.standard);

        subject.setText(McategoryModels.get(position).getFirstName() + " " + McategoryModels.get(position).getLastName() + "  ("+McategoryModels.get(position).getRegisterNumber()+")");
        start_end.setText("Break From : " + McategoryModels.get(position).getBreakFrom());
        classroom.setText("Break To : " + McategoryModels.get(position).getBreakTo());
        batch.setText("Number Of Days: " + McategoryModels.get(position).getDays());
        standard.setText("Status : " + McategoryModels.get(position).getStatus());


        container.addView(layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

        return layout;
    }
  
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }  
  
    @Override  
    public int getCount() {  
        return McategoryModels.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int item);
    }
}  