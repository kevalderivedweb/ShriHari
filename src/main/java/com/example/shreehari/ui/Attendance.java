package com.example.shreehari.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.shreehari.API.AddAttandanceRequest;
import com.example.shreehari.API.GetAttandanceRequest;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.Adapter.AttandanceAdapter;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Model.AttandanceModel;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Attendance extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private UserSession session;
	private RequestQueue requestQueue;
	private RecyclerView recyleview;
	private ArrayList<AttandanceModel> mDataset = new ArrayList<>();
	private AttandanceAdapter mAdapter;
	private Spinner standard,batch;
	private ArrayList<BatchModel> mDataset1 = new ArrayList<>();
	private ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private ArrayList<String> stringArrayList = new ArrayList<>();
	private int standard_pos = 0 , batch_pos = 0;
	private TextView date;
	private ImageView minus,plus;
	private String formattedDate;
	private ImageView done;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_attendance, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue
		session = new UserSession(getActivity());

		standard = view.findViewById(R.id.standard);
		batch = view.findViewById(R.id.batch);
		date = view.findViewById(R.id.date);
		minus = view.findViewById(R.id.minus);
		plus = view.findViewById(R.id.plus);
		done = view.findViewById(R.id.done);



		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		recyleview.setLayoutManager(new GridLayoutManager(getActivity(),3));
		mAdapter = new AttandanceAdapter(mDataset, new AttandanceAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				for (int i = 0 ;  i < stringArrayList.size() ; i++){
					if(stringArrayList.get(i).equals(mDataset.get(item).getId())){
						stringArrayList.remove(mDataset.get(item).getId());
					}else {
						stringArrayList.add(mDataset.get(item).getId());
					}
				}



			}
		});
		recyleview.setAdapter(mAdapter);
		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
		formattedDate = df.format(c.getTime());

		date.setText(formattedDate);

		minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				c.add(Calendar.DATE, -1);
				formattedDate = df.format(c.getTime());

				Log.v("PREVIOUS DATE : ", formattedDate);
				date.setText(formattedDate);
				GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
			}
		});


		plus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				c.add(Calendar.DATE, -1);
				formattedDate = df.format(c.getTime());

				Log.v("PREVIOUS DATE : ", formattedDate);
				date.setText(formattedDate);
				GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
			}
		});

		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				standard_pos = i;
				GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				batch_pos = i;
				GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AddAttandance(formattedDate,stringArrayList);
			}
		});

		GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());

		return view;
	}


	private void GetAttandance(String date,String batch,String standard) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAttandanceRequest loginRequest = new GetAttandanceRequest(date,batch,standard,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				mDataset.clear();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						AttandanceModel announcementModel = new AttandanceModel();
						announcementModel.setFirst_name(object.getString("first_name"));
						announcementModel.setId(String.valueOf(object.getInt("mobile_user_master_id")));
						announcementModel.setLast_name(object.getString("last_name"));
						announcementModel.setCoaching_reg_no(object.getString("coaching_reg_no"));
						announcementModel.setStandard(object.getString("standard"));
						announcementModel.setBatch(object.getString("batch"));
						announcementModel.setIs_absent(String.valueOf(object.getInt("is_absent")));
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
	private void AddAttandance(String date,ArrayList<String> stringArrayList) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		AddAttandanceRequest loginRequest = new AddAttandanceRequest(date,stringArrayList,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					Toast.makeText(getActivity(),jsonObject.getString("ResponseMsg"),Toast.LENGTH_LONG).show();

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

	private void GetstandardAndBatch() {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetBatchRequest loginRequest = new GetBatchRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						BatchModel BatchModel = new BatchModel();
						BatchModel.setBatch_id(object.getString("batch_id"));
						BatchModel.setBatch_name(object.getString("batch_name"));
						BatchModel.setBatch_time(object.getString("batch_time"));
						BatchModel.setStatus(object.getString("status"));
						BatchModel.setBranch_id(object.getString("branch_id"));
						mDataset1.add(BatchModel);
					}

					SpinAdapter adapter = new SpinAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset1);
					batch.setAdapter(adapter);

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


		GetStandardRequest loginRequest1 = new GetStandardRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						StandardModel BatchModel = new StandardModel();
						BatchModel.setCoaching_id(object.getString("coaching_id"));
						BatchModel.setCoaching(object.getString("coaching"));
						BatchModel.setStatus(object.getString("status"));
						mDataset2.add(BatchModel);
					}

					SpinAdapter2 adapter = new SpinAdapter2(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset2);
					standard.setAdapter(adapter);

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
		loginRequest1.setTag("TAG");
		requestQueue.add(loginRequest1);

	}

}