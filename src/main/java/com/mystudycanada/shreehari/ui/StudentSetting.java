package com.mystudycanada.shreehari.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.mystudycanada.shreehari.API.GetSelectedChildRequest;
import com.mystudycanada.shreehari.API.GetStudnetSettingRequest;
import com.mystudycanada.shreehari.Adapter.StudentAdapterSetting;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.StudentModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentSetting extends Fragment {

	private RequestQueue requestQueue;
	private UserSession session;
	private RecyclerView recyleview;
	private StudentAdapterSetting mAdapter;
	private ArrayList<StudentModel> mDataset = new ArrayList<>();
	private LinearLayout spinnerlayout;
	private EditText search;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_student_search, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());


		spinnerlayout = view.findViewById(R.id.spinnerlayout);
		search = view.findViewById(R.id.search);
		spinnerlayout.setVisibility(View.GONE);
		search.setVisibility(View.GONE);
		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		recyleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mAdapter = new StudentAdapterSetting(mDataset, new StudentAdapterSetting.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				if(mDataset.get(item).getSelected().equals("1")){
					Toast.makeText(getContext(),"User Alredy Active...",Toast.LENGTH_SHORT).show();

				}else {
				SetChild(mDataset.get(item).getMobile_user_master_id());
			}
			}
		});
		recyleview.setAdapter(mAdapter);

		GetResult();
		return view;
	}


	private void GetResult() {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetStudnetSettingRequest loginRequest = new GetStudnetSettingRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				mDataset.clear();
				mAdapter.notifyDataSetChanged();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						StudentModel announcementModel = new StudentModel();
						announcementModel.setMobile_user_master_id(object.getString("mobile_user_master_id"));
						announcementModel.setFirst_name(object.getString("first_name"));
						announcementModel.setLast_name(object.getString("last_name"));
						announcementModel.setProfile_pic(object.getString("profile_pic"));
						announcementModel.setSelected(String.valueOf(object.getInt("is_selected")));
						announcementModel.setBatch("");
						announcementModel.setStandard("");
						announcementModel.setCoaching_reg_no("");

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
		loginRequest.setTag("TAG");        loginRequest.setShouldCache(false);

		requestQueue.add(loginRequest);

	}


	private void SetChild(String id) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetSelectedChildRequest loginRequest = new GetSelectedChildRequest(id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				GetResult();
				Toast.makeText(getContext(),"User Active Successfully...",Toast.LENGTH_SHORT).show();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);


				} catch (JSONException e) {

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