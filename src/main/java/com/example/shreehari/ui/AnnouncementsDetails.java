package com.example.shreehari.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.shreehari.API.GetAnnouncementDetailsRequest;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnnouncementsDetails extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private RequestQueue requestQueue;
	private UserSession session;
	private String Announcement_Id;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_details, container, false);

		try {
			Announcement_Id = getArguments().getString("Announcement_Id");

		} catch (Exception e) {

		}


		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());
		GetAnnouncementDetails(Announcement_Id,view);
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

		GetAnnouncementDetailsRequest loginRequest = new GetAnnouncementDetailsRequest(id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");

					TextView title = view.findViewById(R.id.title);
					TextView send_by = view.findViewById(R.id.send_by);
					TextView date = view.findViewById(R.id.date);
					TextView description = view.findViewById(R.id.description);
					TextView admin = view.findViewById(R.id.admin);
					TextView student = view.findViewById(R.id.student);
					TextView parent = view.findViewById(R.id.parent);

					title.setText(jsonObject1.getString("title"));
					description.setText(jsonObject1.getString("description"));
					send_by.setText("Send By : "+jsonObject1.getString("send_by"));
					date.setText(jsonObject1.getString("date"));
					admin.setText(jsonObject1.getString("admin_read_count"));
					student.setText(jsonObject1.getString("student_read_count"));
					parent.setText(jsonObject1.getString("parent_read_count"));
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