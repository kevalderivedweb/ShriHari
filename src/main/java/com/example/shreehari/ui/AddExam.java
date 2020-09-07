package com.example.shreehari.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.shreehari.API.AddExamRequest;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetExamRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.Model.ExamModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddExam extends Fragment {
	// Store instance variables

	private static int standard_pos = 0;
	private static int batch_pos = 0;
	private Spinner standard,batch;
	private static ArrayList<BatchModel> mDataset1 = new ArrayList<>();
	private static ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private UserSession session;
	private RequestQueue requestQueue;
	private TextView time;
	private static TextView date;
	public static String formattedDate_abc;
	private EditText mock_test_name,topic,essay;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_exam, container, false);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue
		session = new UserSession(getActivity());

		standard = view.findViewById(R.id.standard);
		batch = view.findViewById(R.id.batch);
		time = view.findViewById(R.id.time);
		mock_test_name = view.findViewById(R.id.mock_test_name);
		topic = view.findViewById(R.id.topic);
		essay = view.findViewById(R.id.essay);


		Date c = Calendar.getInstance().getTime();
		System.out.println("Current time => " + c);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate_abc = dateFormat.format(c);




		time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Get Current Time
				final Calendar c = Calendar.getInstance();
				int mHour = c.get(Calendar.HOUR_OF_DAY);
				int mMinute = c.get(Calendar.MINUTE);

				// Launch Time Picker Dialog
				TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay,
												  int minute) {

								time.setText(hourOfDay + ":" + minute);
							}
						}, mHour, mMinute, false);
				timePickerDialog.show();

			}
		});

		date = view.findViewById(R.id.date);
		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});
		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

		view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mock_test_name.getText().toString().isEmpty()){
					Toast.makeText(getActivity(),"Please Enter Mock Test Name",Toast.LENGTH_SHORT).show();
				}else if(topic.getText().toString().isEmpty()){
					Toast.makeText(getActivity(),"Please Enter Topic",Toast.LENGTH_SHORT).show();
				}else if(essay.getText().toString().isEmpty()){
					Toast.makeText(getActivity(),"Please Enter Essay",Toast.LENGTH_SHORT).show();
				}else if(standard_pos==0){
					Toast.makeText(getActivity(),"Please Enter Standard",Toast.LENGTH_SHORT).show();
				}else if(batch_pos==0){
					Toast.makeText(getActivity(),"Please Enter Batch",Toast.LENGTH_SHORT).show();
				}else if((date.getText().toString().equals("   Select  Date:"))){
					Toast.makeText(getActivity(),"Please Select Date",Toast.LENGTH_SHORT).show();
				}else if((time.getText().toString().equals("   Select Time :"))){
					Toast.makeText(getActivity(),"Please Select Time",Toast.LENGTH_SHORT).show();
				}else {
					AddExam(mock_test_name.getText().toString(),date.getText().toString(),time.getText().toString(),topic.getText().toString(),essay.getText().toString(),session.getBranchId(),mDataset2.get(standard_pos).getCoaching_id(),mDataset1.get(batch_pos).getBatch_id());
				}
			}
		});
		GetstandardAndBatch();
		return view;
	}

	private void GetstandardAndBatch() {


		GetBatchRequest loginRequest = new GetBatchRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				mDataset1.clear();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						if(i==0){
							BatchModel BatchModel = new BatchModel();
							BatchModel.setBatch_id("");
							BatchModel.setBatch_name("Please Select Batch");
							BatchModel.setBatch_time("Please Select Batch");
							BatchModel.setStatus("");
							BatchModel.setBranch_id("");
							mDataset1.add(BatchModel);
						}
						JSONObject object = jsonArray.getJSONObject(i);
						BatchModel BatchModel = new BatchModel();
						BatchModel.setBatch_id(object.getString("batch_id"));
						BatchModel.setBatch_name(object.getString("batch_name"));
						BatchModel.setBatch_time(object.getString("batch_time"));
						BatchModel.setStatus(object.getString("status"));
						BatchModel.setBranch_id(object.getString("branch_id"));
						mDataset1.add(BatchModel);
					}


					BatchModel BatchModel = new BatchModel();
					BatchModel.setBatch_id("");
					BatchModel.setBatch_name("Please Select Batch");
					BatchModel.setBatch_time("Please Select Batch");
					BatchModel.setStatus("");
					BatchModel.setBranch_id("");
					mDataset1.add(BatchModel);
					SpinAdapter adapter = new SpinAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset1);


					adapter.setDropDownViewResource(R.layout.spinner_item);
					batch.setAdapter(adapter);
					batch.setSelection(adapter.getCount());


				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
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
				mDataset2.clear();
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

					StandardModel BatchModel = new StandardModel();
					BatchModel.setCoaching_id("");
					BatchModel.setCoaching("Please select standard");
					BatchModel.setStatus("");
					mDataset2.add(BatchModel);
					SpinAdapter2 adapter = new SpinAdapter2(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset2);
					adapter.setDropDownViewResource(R.layout.spinner_item);
					standard.setAdapter(adapter);
					standard.setSelection(adapter.getCount());

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
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

	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {




		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
			//dialog.getDatePicker().setMaxDate(c.getTimeInMillis());/
			return  dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = dateFormat.format(calendar.getTime());
			try {
				if (dateFormat.parse(formattedDate_abc).after(dateFormat.parse(dateString))) {
					Toast.makeText(getActivity(), "Please select correct date", Toast.LENGTH_SHORT).show();
					return;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			date.setText(dateString);


		}
	}

	private void AddExam(String mock_test_name, String mock_test_date,String mock_test_time,String speaking_topic,String essay,String branch_id,String coaching_id,String batch_id) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		AddExamRequest loginRequest = new AddExamRequest(mock_test_name,mock_test_date,mock_test_time,speaking_topic,essay,branch_id,coaching_id,batch_id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				replaceFragment(R.id.nav_host_fragment,new Exam(),"Fragment",null);



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