package com.mystudycanada.shreehari.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mystudycanada.shreehari.API.GetScheduleRequest;
import com.mystudycanada.shreehari.Adapter.ScheduleAdapter;
import com.mystudycanada.shreehari.EndlessRecyclerViewScrollListener;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.ScheduleModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Schedule extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View details;
	private LinearLayout ln_addschedule;
	private RequestQueue requestQueue;
	private UserSession session;
	private RecyclerView recyleview;
	private ArrayList<ScheduleModel> mDataset = new ArrayList<>();
	private ScheduleAdapter mAdapter;
	private LinearLayoutManager linearlayout;

	private int last_size;
	private String Mpage = "1";
	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule_2, container, false);

		ln_addschedule = view.findViewById(R.id.ln_addschedule);

		ln_addschedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddSchedule(),"Fragment",null);

			}
		});

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());

		if(session.getUserType().equals("admin")){
			view.findViewById(R.id.ln_addschedule).setVisibility(View.VISIBLE);
		}else if(session.getUserType().equals("parent")){
			view.findViewById(R.id.ln_addschedule).setVisibility(View.GONE);
		}else {
			view.findViewById(R.id.ln_addschedule).setVisibility(View.GONE);
		}
		mDataset.clear();
		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview.setLayoutManager(linearlayout);
		mAdapter = new ScheduleAdapter(mDataset, new ScheduleAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {
/*
				Assignment_Single fragobj = new Assignment_Single();
				Bundle bundle = new Bundle();
				bundle.putString("Assignment_Id", mDataset.get(item).getId());
				fragobj.setArguments(bundle);
				replaceFragment(R.id.nav_host_fragment,fragobj,"Fragment",null);*/

			}
		});
		recyleview.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayout) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.e("PageStatus",page + "  " + last_size);
				if (page!=last_size){
					Mpage = String.valueOf(page+1);
					GetSchedule(Mpage);

				}
			}
		});
		recyleview.setAdapter(mAdapter);
		GetSchedule(Mpage);
		return view;
	}
	protected void replaceFragment(@IdRes int containerViewId,
								   @NonNull Fragment fragment,
								   @NonNull String fragmentTag,
								   @Nullable String backStackStateName) {
		getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(containerViewId, fragment, fragmentTag)
				.addToBackStack(backStackStateName)
				.commit();
	}

	private void GetSchedule(String page) {

		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetScheduleRequest loginRequest = new GetScheduleRequest(page,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();


				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);
					last_size = jsonObject.getInt("last_page");

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						ScheduleModel announcementModel = new ScheduleModel();
						announcementModel.setMobile_schedule_id(String.valueOf(object.getInt("mobile_schedule_id")));
						announcementModel.setSubject(object.getString("subject"));
						announcementModel.setBatch(object.getString("batch"));
						announcementModel.setStandard(object.getString("standard"));
						announcementModel.setDate(object.getString("date"));
						announcementModel.setStart_time(object.getString("start_time"));
						announcementModel.setEnd_time(object.getString("end_time"));
						announcementModel.setClass_room(object.getString("class_room"));
						mDataset.add(announcementModel);
					}

					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {

					Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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


}