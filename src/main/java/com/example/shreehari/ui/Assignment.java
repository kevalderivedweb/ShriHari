package com.example.shreehari.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.shreehari.API.GetAssignmentRequest;
import com.example.shreehari.Adapter.AssignmentAdapter;
import com.example.shreehari.EndlessRecyclerViewScrollListener;
import com.example.shreehari.Model.AssignmentModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Assignment extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private RequestQueue requestQueue;
	private UserSession session;
	private RecyclerView recyleview;
	private AssignmentAdapter mAdapter;
	private ArrayList<AssignmentModel> mDataset = new ArrayList<>();
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
		View view = inflater.inflate(R.layout.fragment_assignment_2, container, false);

		view.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddAssignment(),"Fragment",null);

			}
		});

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());

		if(session.getUserType().equals("admin")){
			view.findViewById(R.id.add_btn).setVisibility(View.VISIBLE);
		}else if(session.getUserType().equals("parent")){
			view.findViewById(R.id.add_btn).setVisibility(View.GONE);
		}else {
			view.findViewById(R.id.add_btn).setVisibility(View.GONE);
		}

		mDataset.clear();
		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview.setLayoutManager(linearlayout);
		mAdapter = new AssignmentAdapter(mDataset, new AssignmentAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				Assignment_Single fragobj = new Assignment_Single();
				Bundle bundle = new Bundle();
				bundle.putString("Assignment_Id", mDataset.get(item).getId());
				fragobj.setArguments(bundle);
				replaceFragment(R.id.nav_host_fragment,fragobj,"Fragment",null);

			}
		});
		recyleview.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayout) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.e("PageStatus",page + "  " + last_size);
				if (page!=last_size){
					Mpage = String.valueOf(page+1);
					GetAssignment(Mpage);
				}
			}
		});
		recyleview.setAdapter(mAdapter);
		GetAssignment(Mpage);

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

	private void GetAssignment(String page) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAssignmentRequest loginRequest = new GetAssignmentRequest(page,new Response.Listener<String>() {
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
						AssignmentModel announcementModel = new AssignmentModel();
						announcementModel.setClass_work(object.getString("class_work"));
						announcementModel.setId(String.valueOf(object.getInt("mobile_assignment_id")));
						announcementModel.setHome_work(object.getString("home_work"));
						announcementModel.setDate(object.getString("date"));
						announcementModel.setDue_date(object.getString("due_date"));
						announcementModel.setStandard(object.getString("standard"));
						announcementModel.setBatch(object.getString("batch"));
						announcementModel.setSubject(object.getString("subject"));
						announcementModel.setPeriod(object.getString("period"));
						mDataset.add(announcementModel);
					}

					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				progressDialog.dismiss();
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
		requestQueue.add(loginRequest);

	}

}