package com.example.shreehari.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.shreehari.API.AddScheduleRequest;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.API.GetSubjectRequest;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Adapter.SpinAdapter3;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.Model.SubjectModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSchedule extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private ArrayList<BatchModel> mDataset = new ArrayList<>();
	private UserSession session;
	private RequestQueue requestQueue;
	private ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private ArrayList<SubjectModel> mDataset3 = new ArrayList<>();
	private Spinner standard,batch,subject;
	private int standard_pos = 0,batch_pos = 0,subject_pos = 0;
	private ImageView done;
	private ImageView period,block;
	private String Type = "period";
	private EditText date,start_time,end_time,classroom;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		standard = view.findViewById(R.id.standard);
		batch = view.findViewById(R.id.batch);
		subject = view.findViewById(R.id.subject);
		period = view.findViewById(R.id.period);
		block = view.findViewById(R.id.block);
		done = view.findViewById(R.id.mdone);
		date = view.findViewById(R.id.date);
		start_time = view.findViewById(R.id.start_time);
		end_time = view.findViewById(R.id.end_time);
		classroom = view.findViewById(R.id.classroom);


		session = new UserSession(getContext());
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Log.e("AddSchdeuleData",mDataset3.get(subject_pos).getSubject_id()+"-----"+
						mDataset2.get(standard_pos).getCoaching_id()+"------"+
						mDataset.get(batch_pos).getBatch_id()+"-----"+Type+"-----"+date.getText().toString()+"-----"+session.getBranchId());
				AddSchedule(mDataset3.get(subject_pos).getSubject_id(),mDataset2.get(standard_pos).getCoaching_id()
				,mDataset.get(batch_pos).getBatch_id(),Type,date.getText().toString(),start_time.getText().toString(),end_time.getText().toString(),session.getBranchId(),classroom.getText().toString());
			}
		});
		period.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				block.setImageDrawable(getResources().getDrawable(R.drawable.bg_round_gray_border));
				period.setImageDrawable(getResources().getDrawable(R.drawable.bg_round_gray));
				Type = "period";
			}
		});

		block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				block.setImageDrawable(getResources().getDrawable(R.drawable.bg_round_gray));
				period.setImageDrawable(getResources().getDrawable(R.drawable.bg_round_gray_border));
				Type = "block";
			}
		});

		GetstandardAndBatch();

		subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				subject_pos = i;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				standard_pos = i;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				batch_pos = i;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		return view;
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
						mDataset.add(BatchModel);
					}

					SpinAdapter adapter = new SpinAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset);
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


		GetSubjectRequest loginRequest3 = new GetSubjectRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						SubjectModel BatchModel = new SubjectModel();
						BatchModel.setSubject_id(String.valueOf(object.getInt("subject_id")));
						BatchModel.setSubject(object.getString("subject"));
						mDataset3.add(BatchModel);
					}

					SpinAdapter3 adapter = new SpinAdapter3(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset3);
					subject.setAdapter(adapter);

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
		loginRequest3.setTag("TAG");
		requestQueue.add(loginRequest3);

	}

	private void AddSchedule(String subject_id,
							 String standard_id,String batch_id,String type,String date,String stime,String etime,String branchid,String classroom) {

		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		AddScheduleRequest loginRequest = new AddScheduleRequest(subject_id,standard_id,batch_id,type,date,stime,etime,branchid,classroom,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				try {
					JSONObject object = new JSONObject(response);
					Toast.makeText(getActivity(),object.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
					replaceFragment(R.id.nav_host_fragment,new Schedule(),"Fragment",null);

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


}