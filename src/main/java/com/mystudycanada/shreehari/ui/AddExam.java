package com.mystudycanada.shreehari.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
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
import com.mystudycanada.shreehari.API.AddExamRequest;
import com.mystudycanada.shreehari.API.AndroidMultiPartEntity;
import com.mystudycanada.shreehari.API.GetBatchRequest;
import com.mystudycanada.shreehari.API.GetCoachingLevelRequest;
import com.mystudycanada.shreehari.API.GetStandardRequest;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.Adapter.BatchesCheckBoxAdapter;
import com.mystudycanada.shreehari.Adapter.CoatchingLevelAdapter;
import com.mystudycanada.shreehari.Adapter.LevelCheckBoxAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter2;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.BatchModel;
import com.mystudycanada.shreehari.Model.CoachingLevelModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
	private ArrayList<BatchModel> mDataset1 = new ArrayList<>();
	private ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private UserSession session;
	private RequestQueue requestQueue;
	private TextView time;
	private static TextView date;
	public static String formattedDate_abc;
	private EditText mock_test_name,topic,essay;
	private ArrayList<CoachingLevelModel> mDataset3 = new ArrayList<>();
	private Spinner coaching_level;
	private int coachin_pos = 0;


	private ImageView batch1;
	private LinearLayout batch_layout;
	private String[] checkBoxList;
	private boolean[] checkedItems;
	private RecyclerView recyleview;
	private LinearLayoutManager linearlayout;
	private LevelCheckBoxAdapter mAdapter;
	private ArrayList<CoachingLevelModel> mDatasetFilter = new ArrayList<>();




	private ImageView batch2;
	private LinearLayout batch_layout2;
	private String[] checkBoxList2;
	private boolean[] checkedItems2;
	private RecyclerView recyleview2;
	private LinearLayoutManager linearlayout2;
	private BatchesCheckBoxAdapter mAdapter2;
	private ArrayList<BatchModel> mDatasetFilter2 = new ArrayList<>();
	private KProgressHUD progressDialog;


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
		coaching_level = view.findViewById(R.id.coaching_level);
		time = view.findViewById(R.id.time);
		mock_test_name = view.findViewById(R.id.mock_test_name);
		topic = view.findViewById(R.id.topic);
		essay = view.findViewById(R.id.essay);


		Date c = Calendar.getInstance().getTime();
		System.out.println("Current time => " + c);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate_abc = dateFormat.format(c);


		mDataset1.clear();
		mDataset3.clear();
		mDataset2.clear();

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
				mDataset3.clear();
				standard_pos = i;
				mDataset1.clear();

				if(standard_pos!=mDataset2.size()-1){
					try {
						GetCoachingLevel(mDataset2.get(standard_pos).getCoaching_id());
					}catch (Exception e){
						//	GetStudnet("0","0");

					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		coaching_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				mDataset1.clear();
				coachin_pos = i;
				Log.e("batch_pos",""+coachin_pos+"  "  +mDataset3.size());
				if(coachin_pos!=mDataset3.size()-1){
					try {
						mDataset1.clear();
						GetBatch(mDataset3.get(coachin_pos).getCoachinglevel_id());
					}catch (Exception e){
						//	GetStudnet("0","0");

					}
				}


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
				}else if((date.getText().toString().equals("   Select  Date:"))){
					Toast.makeText(getActivity(),"Please Select Date",Toast.LENGTH_SHORT).show();
				}else if((time.getText().toString().equals("   Select Time :"))){
					Toast.makeText(getActivity(),"Please Select Time",Toast.LENGTH_SHORT).show();
				}else if(standard_pos==mDataset2.size()-1){
					Toast.makeText(getActivity(),"Please Select Standard",Toast.LENGTH_SHORT).show();
				}else if(batch_layout.getVisibility()==View.VISIBLE&&mDatasetFilter.isEmpty()){
					Toast.makeText(getActivity(), "Please Select Leval", Toast.LENGTH_SHORT).show();
				}else if(batch_layout2.getVisibility()==View.VISIBLE&&mDatasetFilter2.isEmpty()){
					Toast.makeText(getActivity(), "Please Select Batch", Toast.LENGTH_SHORT).show();
				}else {
				//	AddExam(mock_test_name.getText().toString(),date.getText().toString(),time.getText().toString(),topic.getText().toString(),essay.getText().toString(),session.getBranchId(),mDataset2.get(standard_pos).getCoaching_id(),mDataset1.get(batch_pos).getBatch_id());

					new UploadFileToServer().execute();
				}
			}
		});
		GetStandard();

		batch1 = view.findViewById(R.id.batch1);
		batch_layout = view.findViewById(R.id.batch_layout);


		recyleview = view.findViewById(R.id.rcyleview);
		recyleview.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview.setLayoutManager(linearlayout);
		mAdapter = new LevelCheckBoxAdapter(mDatasetFilter, new LevelCheckBoxAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {
				mDatasetFilter.remove(item);
				mAdapter.notifyDataSetChanged();
				//new GetBatchArray().execute();
			}
		});
		recyleview.setAdapter(mAdapter);




		batch1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				checkBoxList = new String[mDataset3.size()];
				checkedItems = new boolean[mDataset3.size()];
				for (int i =  0 ; i < mDataset3.size() ; i++){
					checkedItems[i] = false;
					checkBoxList[i] = mDataset3.get(i).getCoachinglevel();
				}


				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose some Batches");

// Add a checkbox list
				builder.setMultiChoiceItems(checkBoxList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// The user checked or unchecked a box

						for (int j = 0 ; j < mDatasetFilter.size() ; j++){

							if(mDatasetFilter.get(j).getCoachinglevel().equals(checkBoxList[which])){
								//	Toast.makeText(getActivity(),"You Have Alredy Check this Batches",Toast.LENGTH_SHORT).show();
								checkedItems[which] = false;
								return;
							}
						}

						if(isChecked){
							CoachingLevelModel Batchmodel = new CoachingLevelModel();
							Batchmodel.setCoachinglevel_id(mDataset3.get(which).getCoachinglevel_id());
							Batchmodel.setCoachinglevel(mDataset3.get(which).getCoachinglevel());
							mDatasetFilter.add(Batchmodel);
						}else {
							mDatasetFilter.remove(which);
						}

					}
				});

// Add OK and Cancel buttons
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// The user clicked OK
						new GetBatchArray().execute();
						mAdapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("Cancel", null);

// Create and show the alert dialog
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});



		batch2 = view.findViewById(R.id.batch2);
		batch_layout2 = view.findViewById(R.id.batch_layout2);


		recyleview2 = view.findViewById(R.id.rcyleview2);
		recyleview2.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview2.setLayoutManager(linearlayout);
		mAdapter2 = new BatchesCheckBoxAdapter(mDatasetFilter2, new BatchesCheckBoxAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {
				mDatasetFilter2.remove(item);
				mAdapter2.notifyDataSetChanged();
			}
		});
		recyleview2.setAdapter(mAdapter2);




		batch2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				checkBoxList2 = new String[mDataset1.size()];
				checkedItems2 = new boolean[mDataset1.size()];
				for (int i =  0 ; i < mDataset1.size() ; i++){
					checkedItems2[i] = false;
					checkBoxList2[i] = mDataset1.get(i).getBatch_name();
				}


				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose some Batches");

// Add a checkbox list
				builder.setMultiChoiceItems(checkBoxList2, checkedItems2, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// The user checked or unchecked a box

						for (int j = 0 ; j < mDatasetFilter2.size() ; j++){

							if(mDatasetFilter2.get(j).getBatch_name().equals(checkBoxList2[which])){
								//	Toast.makeText(getActivity(),"You Have Alredy Check this Batches",Toast.LENGTH_SHORT).show();
								checkedItems2[which] = false;
								return;
							}
						}

						if(isChecked){
							BatchModel Batchmodel = new BatchModel();
							Batchmodel.setBatch_id(mDataset1.get(which).getBatch_id());
							Batchmodel.setBatch_name(mDataset1.get(which).getBatch_name());
							mDatasetFilter2.add(Batchmodel);
						}else {
							mDatasetFilter2.remove(which);
						}

					}
				});

// Add OK and Cancel buttons
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// The user clicked OK
						mAdapter2.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("Cancel", null);

// Create and show the alert dialog
				AlertDialog dialog = builder.create();
				dialog.show();
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

		Log.e("ExamValue","mock_test_name : "+mock_test_name+"---"+
				"mock_test_date : "+mock_test_date+"---"+
				"mock_test_time : "+mock_test_time+"---"+
				"speaking_topic : "+speaking_topic+"---"+
				"essay : "+essay+"---"+
				"branch_id : "+branch_id+"---"+
				"coaching_id : "+coaching_id+"---"+
				"batch_id : "+batch_id+"---"+"Authorization" + "Bearer "+ session.getAPIToken() );

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
				Log.e("ResponseExam", response + " null");

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);
					Toast.makeText(getActivity(),jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}


				progressDialog.dismiss();
				replaceFragment(R.id.nav_host_fragment,new Exam(),"Fragment",null);



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
			params.put("Accept", "application/json");
			params.put("Authorization","Bearer "+ session.getAPIToken());
			return params;
		}};
		loginRequest.setTag("TAG");
		loginRequest.setShouldCache(false);

		requestQueue.add(loginRequest);

	}
	private void GetStandard(){
		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();


		GetStandardRequest loginRequest1 = new GetStandardRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
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
		loginRequest1.setTag("TAG");		loginRequest1.setShouldCache(false);

		requestQueue.add(loginRequest1);
	}
	private void GetCoachingLevel(String Standard_id){
		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();


		GetCoachingLevelRequest loginRequest1 = new GetCoachingLevelRequest(Standard_id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response_coach", response + " null");

				mDataset3.clear();
				progressDialog.dismiss();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){

						JSONObject object = jsonArray.getJSONObject(i);
						CoachingLevelModel BatchModel = new CoachingLevelModel();
						BatchModel.setCoachinglevel_id(object.getString("coachinglevel_id"));
						BatchModel.setCoachinglevel(object.getString("coachinglevel"));
						BatchModel.setCoaching_type_id(object.getString("coaching_type_id"));
						BatchModel.setBranch_id(object.getString("branch_id"));
						BatchModel.setStatus(object.getString("status"));
						mDataset3.add(BatchModel);


					}

					batch_layout.setVisibility(View.VISIBLE);

				/*	CoachingLevelModel BatchModel = new CoachingLevelModel();
					BatchModel.setCoachinglevel_id("");
					BatchModel.setCoachinglevel("Please select level");
					BatchModel.setCoaching_type_id("");
					BatchModel.setBranch_id("");
					BatchModel.setStatus("");
					mDataset3.add(BatchModel);*/
					CoatchingLevelAdapter adapter = new CoatchingLevelAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset3);
					adapter.setDropDownViewResource(R.layout.spinner_item);
					coaching_level.setAdapter(adapter);
					coaching_level.setSelection(adapter.getCount());

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
			params.put("Accept", "application/json");
			params.put("Authorization","Bearer "+ session.getAPIToken());
			return params;
		}};
		loginRequest1.setTag("TAG");

		loginRequest1.setShouldCache(false);

		requestQueue.add(loginRequest1);
	}
	private void GetBatch(String level) {
		mDataset1.clear();

		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetBatchRequest loginRequest = new GetBatchRequest(level,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				mDataset1.clear();
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

					/*BatchModel BatchModel = new BatchModel();
					BatchModel.setBatch_id("");
					BatchModel.setBatch_name("Please Select Batch");
					BatchModel.setBatch_time("");
					BatchModel.setStatus("");
					BatchModel.setBranch_id("");
					mDataset1.add(BatchModel);*/

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
		loginRequest.setShouldCache(false);

		requestQueue.add(loginRequest);



	}



	private class GetBatchArray extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL +"get-batch-from-multiple-level";

		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			super.onPreExecute();
			progressDialog = KProgressHUD.create(getActivity())
					.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
					.setLabel("Please wait")
					.setCancellable(false)
					.setAnimationSpeed(2)
					.setDimAmount(0.5f);

			progressDialog.show();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);
			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new AndroidMultiPartEntity.ProgressListener() {
							@Override
							public void transferred(long num) {}
						});

				UserSession userSession = new UserSession(getActivity());
				// Extra parameters if you want to pass to server


				//entity.addPart("batch_id", new StringBody(mDataset.get(batch_pos).getBatch_id()));
				for (int p = 0; p < mDatasetFilter.size(); p++) {
					entity.addPart("coaching_level_id[]", new StringBody(mDatasetFilter.get(p).getCoachinglevel_id()));
				}
				httppost.setEntity(entity);
				httppost.addHeader("Authorization","Bearer "+userSession.getAPIToken());
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e("TAG", "Response from server: " + result);
			progressDialog.dismiss();
			// showing the server response in an alert categoryDialog
			//showAlert(result);
			progressDialog.dismiss();
			mDataset1.clear();
			batch_layout2.setVisibility(View.VISIBLE);
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(result);

				JSONArray jsonArray = jsonObject.getJSONArray("data");

				for (int i = 0; i < jsonArray.length(); i++) {


					JSONObject object = jsonArray.getJSONObject(i);
					BatchModel BatchModel = new BatchModel();
					BatchModel.setBatch_id(object.getString("batch_id"));
					BatchModel.setBatch_name(object.getString("batch_name"));
					BatchModel.setBatch_time(object.getString("batch_time"));
					BatchModel.setStatus(object.getString("status"));
					BatchModel.setBranch_id(object.getString("branch_id"));
					mDataset1.add(BatchModel);


				}
				super.onPostExecute(result);
			} catch (JSONException e) {


			}

		}
	}


	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL+"add-exam";

		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			super.onPreExecute();
			progressDialog = KProgressHUD.create(getActivity())
					.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
					.setLabel("Please wait")
					.setCancellable(false)
					.setAnimationSpeed(2)
					.setDimAmount(0.5f);

			progressDialog.show();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);
			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new AndroidMultiPartEntity.ProgressListener() {
							@Override
							public void transferred(long num) {}
						});

				UserSession userSession = new UserSession(getActivity());
				// Extra parameters if you want to pass to server


				entity.addPart("mock_test_name", new StringBody(mock_test_name.getText().toString()));
				entity.addPart("mock_test_date", new StringBody(date.getText().toString()));
				entity.addPart("mock_test_time", new StringBody(time.getText().toString()));
				entity.addPart("speaking_topic", new StringBody(topic.getText().toString()));
				entity.addPart("essay", new StringBody(essay.getText().toString()));
				entity.addPart("branch_id", new StringBody(userSession.getBranchId()));
				entity.addPart("standard_id", new StringBody(mDataset2.get(standard_pos).getCoaching_id()));
				for (int p = 0; p < mDatasetFilter2.size(); p++) {
					entity.addPart("batch_id[]", new StringBody(mDatasetFilter2.get(p).getBatch_id()));
				}
				httppost.setEntity(entity);
				httppost.addHeader("Authorization","Bearer "+userSession.getAPIToken());
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e("TAG", "Response from server: " + result);
			progressDialog.dismiss();
			// showing the server response in an alert categoryDialog
			//showAlert(result);
			try {
				JSONObject jsonObject = new JSONObject(result);

				if (jsonObject.getInt("ResponseCode")==200) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(jsonObject.getString("ResponseMsg"))
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// do LiveVideo_Recording
									replaceFragment(R.id.nav_host_fragment,new Exam(),"Fragment",null);

								}
							});
					AlertDialog alert = builder.create();
					alert.show();

				}

			} catch (JSONException e) {
				e.printStackTrace();
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Something went wrong please try again later!").setTitle("Error!")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								// do nothing
								replaceFragment(R.id.nav_host_fragment,new Exam(),"Fragment",null);

							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}


			super.onPostExecute(result);
		}

	}


}