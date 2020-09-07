package com.example.shreehari.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.shreehari.API.GetAnnouncementDetailsRequest;
import com.example.shreehari.API.GetStudentDetailsRequest;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View pd1;
	private String Profile_Id;
	private RequestQueue requestQueue;
	private UserSession session;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		session = new UserSession(getActivity());
		try {
			Profile_Id = getArguments().getString("User_Id");

		} catch (Exception e) {
			Profile_Id = session.getUserId();
		}


		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue


		GetAnnouncementDetails(Profile_Id,view);
		return view;
	}


	private void GetAnnouncementDetails(String id,View view) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetStudentDetailsRequest loginRequest = new GetStudentDetailsRequest(id,new Response.Listener<String>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");


					TextView standard = view.findViewById(R.id.standard);
					TextView coach_no = view.findViewById(R.id.coach_no);
					TextView gr_no = view.findViewById(R.id.gr_no);
					TextView no = view.findViewById(R.id.no);
					TextView b_day = view.findViewById(R.id.b_day);
					TextView a_day = view.findViewById(R.id.a_day);
					TextView gender = view.findViewById(R.id.gender);
					TextView status = view.findViewById(R.id.status);
					TextView name = view.findViewById(R.id.name);
					TextView phone_no = view.findViewById(R.id.phone_no);
					TextView main_name = view.findViewById(R.id.main_name);
					standard.setText(jsonObject1.getString("standard"));
					coach_no.setText(jsonObject1.getString("coaching_reg_no"));
					gr_no.setText(jsonObject1.getString("coaching_reg_no"));
					no.setText(jsonObject1.getString("mobile_no"));
					phone_no.setText(jsonObject1.getString("mobile_no"));
					name.setText(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
					main_name.setText(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));

					ImageView imageView1 = (ImageView) view.findViewById(R.id.profile_image2);
					Glide.with(getActivity()).load(jsonObject1.getString("profile_pic")).circleCrop().into(imageView1);
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