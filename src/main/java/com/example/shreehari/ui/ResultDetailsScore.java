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
import com.example.shreehari.API.GetExamDetailsRequest;
import com.example.shreehari.API.GetStudentDetailsRequest;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResultDetailsScore extends Fragment {
	// Store instance variables
	private String MockTestId;
	private String UserId;
	private UserSession session;
	private RequestQueue requestQueue;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_result_details_score, container, false);

		session = new UserSession(getActivity());
		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		try {
			MockTestId = getArguments().getString("Id");
			UserId = getArguments().getString("UserId");

		} catch (Exception e) {

		}

		GetAnnouncementDetails(MockTestId,UserId,view);

		return view;
	}

	private void GetAnnouncementDetails(String id,String userId,View view) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetExamDetailsRequest loginRequest = new GetExamDetailsRequest(id,userId,new Response.Listener<String>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");


					TextView date = view.findViewById(R.id.date);
					TextView total = view.findViewById(R.id.total);
					TextView listening = view.findViewById(R.id.listening);
					TextView reading = view.findViewById(R.id.reading);
					TextView writing = view.findViewById(R.id.writing);
					TextView speaking = view.findViewById(R.id.speaking);
					TextView mock_test_name = view.findViewById(R.id.mock_test_name);
					date.setText(jsonObject1.getString("result_date"));
					total.setText(jsonObject1.getString("total_score"));
					listening.setText(jsonObject1.getString("listening"));
					reading.setText(jsonObject1.getString("reading"));
					writing.setText(jsonObject1.getString("writing"));
					speaking.setText(jsonObject1.getString("speaking"));
					mock_test_name.setText(jsonObject1.getString("mock_test_name"));

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