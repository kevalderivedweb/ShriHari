package com.example.shreehari.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetLeaveRequest;
import com.example.shreehari.API.GetPeriodRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.Model.LeaveModel;
import com.example.shreehari.Model.PeriodModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.example.shreehari.ui.StandardModel;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LeaveAdapter2 extends RecyclerView.Adapter<LeaveAdapter2.MyViewHolder> {

    private final OnItemClickListener listener;
    private ArrayList<LeaveModel> mDataset;
    private Context mContext;
    private ArrayList<BatchModel> mDataset_Leave = new ArrayList<>();
    private RequestQueue requestQueue;
    private UserSession session;
    private int BatchPositions = 0;
    private SpinAdapter adapter;

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
    public LeaveAdapter2(Context activity, ArrayList<LeaveModel> categoryModels, OnItemClickListener listener) {
        mDataset = categoryModels;
        this.listener = listener;
        this.mContext = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        // create a new view
        View v = layoutInflater
                .inflate(R.layout.item_leave, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        requestQueue = Volley.newRequestQueue(mContext);//Creating the RequestQueue

        session = new UserSession(mContext);
        GetstandardAndBatch(holder);

        if(session.getUserType().equals("admin")){
            holder.batch.setVisibility(View.VISIBLE);
            holder.submit.setVisibility(View.VISIBLE);
        }else {
            holder.batch.setVisibility(View.GONE);
            holder.submit.setVisibility(View.GONE);
        }


        holder.batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BatchPositions = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.date.setText(mDataset.get(position).getBreak_from() +" To "+ mDataset.get(position).getBreak_to());
        holder.name.setText(mDataset.get(position).getFirst_name() + " " + mDataset.get(position).getLast_name());
        holder.remark.setText(mDataset.get(position).getRemarks());
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,mDataset_Leave.get(BatchPositions).getBatch_id());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos,String item);
    }


    private void GetstandardAndBatch(MyViewHolder  myViewHolder) {

        GetLeaveRequest loginRequest = new GetLeaveRequest(new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");

                mDataset_Leave.clear();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0 ; i<jsonArray.length() ; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        BatchModel BatchModel = new BatchModel();
                        BatchModel.setBatch_id(object.getString("leave_status_id"));
                        BatchModel.setBatch_time(object.getString("leave_status"));
                        BatchModel.setBatch_name(object.getString("leave_status"));
                        BatchModel.setStatus("");
                        BatchModel.setBranch_id("");
                        mDataset_Leave.add(BatchModel);
                    }


                    BatchModel BatchModel = new BatchModel();
                    BatchModel.setBatch_id("123");
                    BatchModel.setBatch_time("Please Select Status");
                    BatchModel.setBatch_name("Please Select Status");
                    BatchModel.setStatus("");
                    BatchModel.setBranch_id("");
                    mDataset_Leave.add(BatchModel);
                    SpinAdapter adapter = new SpinAdapter(mContext,
                            android.R.layout.simple_spinner_item,
                            mDataset_Leave);


                    adapter.setDropDownViewTheme(mContext.getResources().newTheme());
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    myViewHolder.batch.setAdapter(adapter);
                    myViewHolder.batch.setSelection(adapter.getCount());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof ServerError)
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(mContext, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(mContext, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            // params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }


}
