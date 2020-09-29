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
import com.example.shreehari.API.AddLeaveRequest;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Model.BatchModel;
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

public class AddLeave extends Fragment {
	// Store instance variables

	private UserSession session;
	private RequestQueue requestQueue;
	private static TextView time;
	private static TextView date;
	public static String formattedDate_abc;
	private EditText topic,essay;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_leave, container, false);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue
		session = new UserSession(getActivity());

		time = view.findViewById(R.id.time);
		topic = view.findViewById(R.id.topic);
		essay = view.findViewById(R.id.essay);


		Date c = Calendar.getInstance().getTime();
		System.out.println("Current time => " + c);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate_abc = dateFormat.format(c);




		time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(date.getText().toString().equals("   Select  Date:")){
					Toast.makeText(getActivity(),"Please Select start date",Toast.LENGTH_SHORT).show();
					return;
				}
				DialogFragment newFragment = new DatePickerFragment2();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

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

		view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				 if(topic.getText().toString().isEmpty()){
					Toast.makeText(getActivity(),"Please Enter Number of Days",Toast.LENGTH_SHORT).show();
				}else if(essay.getText().toString().isEmpty()){
					Toast.makeText(getActivity(),"Please Enter Remark",Toast.LENGTH_SHORT).show();
				}else if((date.getText().toString().equals("   Select  Date:"))){
					Toast.makeText(getActivity(),"Please Select Date",Toast.LENGTH_SHORT).show();
				}else if((time.getText().toString().equals("   Select Date:"))){
					Toast.makeText(getActivity(),"Please Select Date",Toast.LENGTH_SHORT).show();
				}else {
					AddLeave(date.getText().toString(),time.getText().toString(),topic.getText().toString(),essay.getText().toString());
				}
			}
		});

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

	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {




		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
			dialog.getDatePicker().setMinDate(c.getTimeInMillis());
			return  dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = dateFormat.format(calendar.getTime());

			date.setText(dateString);


		}
	}


	public static class DatePickerFragment2 extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {




		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
			//dialog.getDatePicker().setMaxDate(c.getTimeInMillis());/
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date datetostring = dateFormat.parse(date.getText().toString());
				dialog.getDatePicker().setMinDate(datetostring.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return  dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = dateFormat.format(calendar.getTime());
			time.setText(dateString);


		}
	}

	private void AddLeave(String mock_test_date,String mock_test_time,String speaking_topic,String essay) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		AddLeaveRequest loginRequest = new AddLeaveRequest(mock_test_date,mock_test_time,speaking_topic,essay,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				replaceFragment(R.id.nav_host_fragment,new Leave(),"Fragment",null);



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
			 params.put("Accept", "application/json");
			params.put("Authorization","Bearer "+ session.getAPIToken());
			return params;
		}};
		loginRequest.setTag("TAG");
		requestQueue.add(loginRequest);

	}

}