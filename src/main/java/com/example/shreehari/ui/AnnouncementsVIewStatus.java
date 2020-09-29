package com.example.shreehari.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.shreehari.API.GetAnnouncementRequestFilter;
import com.example.shreehari.API.GetAnnouncementStatusRequest;
import com.example.shreehari.Adapter.AnnouncementStatusAdapter;
import com.example.shreehari.Adapter.StudentAdapter;
import com.example.shreehari.Model.AnnouncementModel;
import com.example.shreehari.Model.AnnouncementsStaus;
import com.example.shreehari.Model.StudentModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementsVIewStatus extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View pd1;
	private ArrayList<AnnouncementsStaus> mDataset_Parent = new ArrayList<>();
	private ArrayList<AnnouncementsStaus> mDataset_Student = new ArrayList<>();
	private ArrayList<AnnouncementsStaus> mDataset_Staff = new ArrayList<>();
	private RecyclerView recyleview;
	private AnnouncementStatusAdapter mAdapter;
	private RequestQueue requestQueue;
	private UserSession session;
	private String Announcement_Id;
	private String Announcement_Status;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_addannouncement_status, container, false);

		final LinearLayout parent = view.findViewById(R.id.parent);
		LinearLayout student = view.findViewById(R.id.student);
		LinearLayout both = view.findViewById(R.id.both);

		final TextView both_txt = view.findViewById(R.id.both_txt);
		final TextView student_txt = view.findViewById(R.id.student_txt);
		final TextView parent_txt = view.findViewById(R.id.parent_txt);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());

		try {
			Announcement_Id = getArguments().getString("Announcement_Id");
			Announcement_Status = getArguments().getString("Announcement_Status");

		} catch (Exception e) {

		}


		view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getActivity().onBackPressed();
			}
		});
		parent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
				student_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				both_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				mAdapter = new AnnouncementStatusAdapter(mDataset_Parent, new AnnouncementStatusAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(int item) {

					}
				});
				recyleview.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();

			}
		});

		student.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				student_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
				both_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				mAdapter = new AnnouncementStatusAdapter(mDataset_Student, new AnnouncementStatusAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(int item) {

					}
				});
				recyleview.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();

			}
		});

		both.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				student_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				both_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));

				mAdapter = new AnnouncementStatusAdapter(mDataset_Staff, new AnnouncementStatusAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(int item) {

					}
				});
				recyleview.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		});




		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		recyleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		GetAnnouncementStatus(Announcement_Id);

		if(Announcement_Status.equals("1")){
			parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
			student_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
			both_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));

			mAdapter = new AnnouncementStatusAdapter(mDataset_Parent, new AnnouncementStatusAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(int item) {

				}
			});
			recyleview.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

		}else if(Announcement_Status.equals("2")){
			parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
			student_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
			both_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));

			mAdapter = new AnnouncementStatusAdapter(mDataset_Student, new AnnouncementStatusAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(int item) {

				}
			});
			recyleview.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

		}else if(Announcement_Status.equals("3")){
			parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
			student_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
			both_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));


			mAdapter = new AnnouncementStatusAdapter(mDataset_Staff, new AnnouncementStatusAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(int item) {

				}
			});
			recyleview.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

		}else {
			mAdapter = new AnnouncementStatusAdapter(mDataset_Parent, new AnnouncementStatusAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(int item) {

				}
			});
			recyleview.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		}

		return view;
	}

	private void GetAnnouncementStatus(String id) {

		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAnnouncementStatusRequest loginRequest = new GetAnnouncementStatusRequest(id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("ResponseFilter", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");

					JSONArray jsonArray = jsonObject1.getJSONArray("admin");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						AnnouncementsStaus announcementModel = new AnnouncementsStaus();
						announcementModel.setName(object.getString("first_name")+" "+object.getString("last_name"));
						announcementModel.setBatch("");
						Character character = object.getString("first_name").charAt(0);
						announcementModel.setTxt_latter(character.toString().toUpperCase());
						announcementModel.setNumber(object.getString("mobile_no"));
						mDataset_Staff.add(announcementModel);
					}


					JSONArray jsonstudent = jsonObject1.getJSONArray("student");

					for (int i = 0 ; i<jsonstudent.length() ; i++){
						JSONObject object = jsonstudent.getJSONObject(i);
						AnnouncementsStaus announcementModel = new AnnouncementsStaus();
						announcementModel.setName(object.getString("first_name")+" "+object.getString("last_name"));
						announcementModel.setBatch("["+object.getString("coaching_reg_no")+"]"+"["+object.getString("standard")+"]"+" ["+object.getString("batch")+"]");
						Character character = object.getString("first_name").charAt(0);
						announcementModel.setTxt_latter(character.toString().toUpperCase());
						announcementModel.setNumber(object.getString("mobile_no"));
						mDataset_Student.add(announcementModel);
					}


					JSONArray jsonparent = jsonObject1.getJSONArray("parent");

					for (int i = 0 ; i<jsonparent.length() ; i++){
						JSONObject object = jsonparent.getJSONObject(i);
						AnnouncementsStaus announcementModel = new AnnouncementsStaus();
						announcementModel.setName(object.getString("first_name")+" "+object.getString("last_name"));
						announcementModel.setBatch("["+object.getString("coaching_reg_no")+"]"+"["+object.getString("standard")+"]"+" ["+object.getString("batch")+"]");
						Character character = object.getString("first_name").charAt(0);
						announcementModel.setTxt_latter(character.toString().toUpperCase());
						announcementModel.setNumber(object.getString("mobile_no"));
						mDataset_Parent.add(announcementModel);
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