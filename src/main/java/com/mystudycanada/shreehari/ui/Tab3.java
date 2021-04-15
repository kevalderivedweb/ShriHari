package com.mystudycanada.shreehari.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mystudycanada.shreehari.API.GetLeaveDetailsRequest;
import com.mystudycanada.shreehari.Adapter.LeaveAdapter;
import com.mystudycanada.shreehari.EndlessRecyclerViewScrollListener;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.LeaveModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tab3 extends Fragment {

    private RecyclerView recyleview;
    private ArrayList<LeaveModel> mDataset = new ArrayList<>();
    private LeaveAdapter mAdapter;
    private RequestQueue requestQueue;
    private UserSession session;
    private TextView textView;
    private LinearLayoutManager linearlayout;
    private int last_size;
    private String Mpage = "1";

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        //Returning the layout file after inflating 
        //Change R.layout.tab1 in you classes 
        View view = inflater.inflate(R.layout.tab1, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

        session = new UserSession(getActivity());

        mDataset.clear();
        recyleview = view.findViewById(R.id.recyleview);
        textView = view.findViewById(R.id.textView);
        recyleview.setHasFixedSize(true);
        linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyleview.setLayoutManager(linearlayout);
        mAdapter = new LeaveAdapter(getActivity(),mDataset, new LeaveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

            }
        });
        recyleview.setAdapter(mAdapter);
        recyleview.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.e("PageStatus",page + "  " + last_size);
                if (page!=last_size){
                    Mpage = String.valueOf(page+1);
                    GetResult(Mpage);
                }
            }
        });

        GetResult(Mpage);

        return view;
    }

    private void GetResult(String page) {


        final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetLeaveDetailsRequest loginRequest = new GetLeaveDetailsRequest(page,"3",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ResponseTab3", response + " null");
                progressDialog.dismiss();


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    last_size = jsonObject.getInt("last_page");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0 ; i<jsonArray.length() ; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        LeaveModel announcementModel = new LeaveModel();
                        announcementModel.setCoachingbreak_id(object.getString("coachingbreak_id"));
                        announcementModel.setInquiry_id(object.getString("inquiry_id"));
                        announcementModel.setBatch_id(object.getString("batch_id"));
                        announcementModel.setCoachinglevel_id(object.getString("coachinglevel_id"));
                        announcementModel.setCoachingfaculty_id(object.getString("coachingfaculty_id"));
                        announcementModel.setBreak_from(object.getString("break_from"));
                        announcementModel.setBreak_to(object.getString("break_to"));
                        announcementModel.setNo_of_days(object.getString("no_of_days"));


                        announcementModel.setLeave_status(object.getString("leave_status"));


                        if(session.getUserType().equals("admin")){
                            announcementModel.setRemarks(object.getString("remarks"));
                            announcementModel.setFirst_name(object.getString("first_name"));
                            announcementModel.setLast_name(object.getString("last_name"));
                            announcementModel.setRegisterNumber(object.getString("coaching_reg_no"));
                        }else {
                            announcementModel.setRemarks(object.getString("no_of_days"));
                            announcementModel.setFirst_name(object.getString("remarks"));
                            announcementModel.setLast_name("");
                            announcementModel.setRegisterNumber("");
                        }
                        mDataset.add(announcementModel);

                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                session.logout();
                Intent intent = new Intent(getActivity(), Login_Activity.class);
                startActivity(intent);
                getActivity().finish();
                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            // params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("IsRefresh", "Yes");
        }
    }
}