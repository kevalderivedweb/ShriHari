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
import com.mystudycanada.shreehari.API.AddScheduleRequest;
import com.mystudycanada.shreehari.API.AndroidMultiPartEntity;
import com.mystudycanada.shreehari.API.GetBatchRequest;
import com.mystudycanada.shreehari.API.GetCoachingLevelRequest;
import com.mystudycanada.shreehari.API.GetStandardRequest;
import com.mystudycanada.shreehari.API.GetSubjectRequest;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.Adapter.BatchesCheckBoxAdapter;
import com.mystudycanada.shreehari.Adapter.CoatchingLevelAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter2;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.BatchModel;
import com.mystudycanada.shreehari.Model.CoachingLevelModel;
import com.mystudycanada.shreehari.Model.SubjectModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	private ArrayList<StandardModel> mDatasetSubject = new ArrayList<>();
	private ArrayList<CoachingLevelModel> mDataset4= new ArrayList<>();
	private Spinner standard,batch,subject,coaching_level;
	private int standard_pos = 0,batch_pos = 0,subject_pos = 0,coachin_pos=0;
	private ImageView done;
	private ImageView period,block;
	private String Type = "period";
	private EditText classroom;
	private static TextView date;
	private static TextView start_time;
	private static TextView end_time;
	private static String formattedDate;
	private KProgressHUD progressDialog;
	private static String formattedDate_abc;


	private ImageView batch1,standard_select;
	private LinearLayout batch_layout,standard_layout;
	private String[] checkBoxList,checkBoxList_standard;
	private boolean[] checkedItems,checkedItems_standard;
	private RecyclerView recyleview,recyleview_standard;
	private LinearLayoutManager linearlayout;
	private BatchesCheckBoxAdapter mAdapter;
	private BatchesCheckBoxAdapter mAdapter_standard;
	private ArrayList<BatchModel> mDatasetFilter = new ArrayList<>();
	private ArrayList<BatchModel> mDatasetFilter_standard = new ArrayList<>();

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
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		standard = view.findViewById(R.id.standard);
		batch = view.findViewById(R.id.batch);
		coaching_level = view.findViewById(R.id.coaching_level);
		subject = view.findViewById(R.id.subject);
		period = view.findViewById(R.id.period);
		block = view.findViewById(R.id.block);
		done = view.findViewById(R.id.mdone);
		date = view.findViewById(R.id.date);
		start_time = view.findViewById(R.id.start_time);
		end_time = view.findViewById(R.id.end_time);
		classroom = view.findViewById(R.id.classroom);


		batch1 = view.findViewById(R.id.batch1);
		standard_select = view.findViewById(R.id.standard_select);
		batch_layout = view.findViewById(R.id.batch_layout);
		standard_layout = view.findViewById(R.id.standard_layout);



		recyleview = view.findViewById(R.id.rcyleview);
		recyleview.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview.setLayoutManager(linearlayout);
		mAdapter = new BatchesCheckBoxAdapter(mDatasetFilter, new BatchesCheckBoxAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {
				mDatasetFilter.remove(item);
				mAdapter.notifyDataSetChanged();
			}
		});
		recyleview.setAdapter(mAdapter);



		recyleview_standard = view.findViewById(R.id.rcyleview_standard);
		recyleview_standard.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview_standard.setLayoutManager(linearlayout);
		mAdapter_standard = new BatchesCheckBoxAdapter(mDatasetFilter_standard, new BatchesCheckBoxAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {
				mDatasetFilter_standard.remove(item);
				mAdapter_standard.notifyDataSetChanged();
			}
		});
		recyleview_standard.setAdapter(mAdapter_standard);



		batch1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				checkBoxList = new String[mDataset.size()];
				checkedItems = new boolean[mDataset.size()];
				for (int i =  0 ; i < mDataset.size() ; i++){
					checkedItems[i] = false;
					checkBoxList[i] = mDataset.get(i).getBatch_name();
				}


				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose some Batches");

// Add a checkbox list
				builder.setMultiChoiceItems(checkBoxList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// The user checked or unchecked a box

						for (int j = 0 ; j < mDatasetFilter.size() ; j++){

							if(mDatasetFilter.get(j).getBatch_name().equals(checkBoxList[which])){
								//	Toast.makeText(getActivity(),"You Have Alredy Check this Batches",Toast.LENGTH_SHORT).show();
								checkedItems[which] = false;
								return;
							}
						}

						if(isChecked){
							BatchModel Batchmodel = new BatchModel();
							Batchmodel.setBatch_id(mDataset.get(which).getBatch_id());
							Batchmodel.setBatch_name(mDataset.get(which).getBatch_name());
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
						mAdapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("Cancel", null);

// Create and show the alert dialog
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});



		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});

		Date c = Calendar.getInstance().getTime();
		System.out.println("Current time => " + c);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate_abc = dateFormat.format(c);



		start_time.setOnClickListener(new View.OnClickListener() {
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

								start_time.setText(hourOfDay + ":" + minute);
							}
						}, mHour, mMinute, false);
				timePickerDialog.show();

			}
		});

		end_time.setOnClickListener(new View.OnClickListener() {
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

								end_time.setText(hourOfDay + ":" + minute);
							}
						}, mHour, mMinute, false);
				timePickerDialog.show();

			}
		});

		session = new UserSession(getContext());
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				/*Log.e("AddSchdeuleData",mDataset3.get(subject_pos).getSubject_id()+"-----"+
						mDataset2.get(standard_pos).getCoaching_id()+"------"+
						mDataset.get(batch_pos).getBatch_id()+"-----"+Type+"-----"+date.getText().toString()+"-----"+session.getBranchId());
				AddSchedule1(mDataset3.get(subject_pos).getSubject_id(),mDataset2.get(standard_pos).getCoaching_id()
				,mDataset.get(batch_pos).getBatch_id(),Type,date.getText().toString(),start_time.getText().toString(),end_time.getText().toString(),session.getBranchId(),classroom.getText().toString());
		*/

				Log.e("Subject","null"+subject_pos+"  "+mDataset3.size());
				if(date.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Please Enter Date", Toast.LENGTH_SHORT).show();
				}else if(subject_pos==mDatasetSubject.size()-1){
					Toast.makeText(getActivity(), "Please Select Subject & Batch & Standard", Toast.LENGTH_SHORT).show();
				}else if(standard_pos==mDataset2.size()-1){
					Toast.makeText(getActivity(), "Please Select Subject & Batch & Standard", Toast.LENGTH_SHORT).show();
				}else if(batch_layout.getVisibility()==View.VISIBLE&&mDatasetFilter.isEmpty()){
					Toast.makeText(getActivity(), "Please Select Batch", Toast.LENGTH_SHORT).show();
				}else {
					new UploadFileToServer().execute();
				}

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
				Type = "break";
			}
		});

		GetStandard();


		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

				standard_pos = i;
				mDataset4.clear();
				mDataset.clear();
				batch_layout.setVisibility(View.GONE);
				GetCoachingLevel(mDataset2.get(standard_pos).getCoaching_id());
				Log.e("standard_pos",""+standard_pos+"  "  +mDataset2.size());
				if(standard_pos!=mDataset2.size()-1){
					try {


					}catch (Exception e){
						//GetStudnet("0","0");


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

				mDataset.clear();

				Log.e("batch_pos",""+coachin_pos+"  "  +mDataset.size());
				if(coachin_pos!=mDataset3.size()-1){
					try {
						coachin_pos = i;
						GetBatch(mDataset4.get(coachin_pos).getCoachinglevel_id());
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
				Log.e("batch_pos",""+batch_pos+"  "  +mDataset.size());
				if(batch_pos!=mDataset.size()-1){
					try {
						batch_pos = i;

					}catch (Exception e){

						//	GetStudnet("0","0");

					}
				}


			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		GetSubject();

		subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				subject_pos = i;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		standard_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkBoxList_standard = new String[mDatasetSubject.size()];
				checkedItems_standard = new boolean[mDatasetSubject.size()];

				for (int i =  0 ; i < mDatasetSubject.size() ; i++){
					checkedItems_standard[i] = false;
					checkBoxList_standard[i] = mDatasetSubject.get(i).getCoaching();
				}


				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose some Batches");

// Add a checkbox list
				builder.setMultiChoiceItems(checkBoxList_standard, checkedItems_standard, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// The user checked or unchecked a box

						for (int j = 0 ; j < mDatasetFilter_standard.size() ; j++){

							if(mDatasetFilter_standard.get(j).getBatch_name().equals(checkBoxList_standard[which])){
								//	Toast.makeText(getActivity(),"You Have Alredy Check this Batches",Toast.LENGTH_SHORT).show();
								checkedItems_standard[which] = false;
								return;
							}
						}

						if(isChecked){
							BatchModel Batchmodel = new BatchModel();
							Batchmodel.setBatch_id(mDatasetSubject.get(which).getCoaching_id());
							Batchmodel.setBatch_name(mDatasetSubject.get(which).getCoaching());
							mDatasetFilter_standard.add(Batchmodel);
						}else {
							mDatasetFilter_standard.remove(which);
						}

					}
				});

// Add OK and Cancel buttons
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// The user clicked OK
						mAdapter_standard.notifyDataSetChanged();
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

/*
	private void GetSubject() {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetSubjectRequest loginRequest3 = new GetSubjectRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("ResponseSubject", response + " null");
				mDataset3.clear();
				progressDialog.dismiss();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						SubjectModel BatchModel = new SubjectModel();
						BatchModel.setSubject_id(object.getString("subject_id"));
						BatchModel.setSubject(object.getString("subject"));
						mDataset3.add(BatchModel);
					}

					SubjectModel BatchModel = new SubjectModel();
					BatchModel.setSubject_id("");
					BatchModel.setSubject("Please Select Subject");
					mDataset3.add(BatchModel);

					SpinAdapter3 adapter = new SpinAdapter3(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset3);
					adapter.setDropDownViewResource(R.layout.spinner_item);
					subject.setAdapter(adapter);
					//subject.setSelection(adapter.getCount());
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
*/

	private void AddSchedule1(String subject_id,
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


	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL + "add-schedule";

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


				//entity.addPart("subject_id", new StringBody(mDatasetSubject.get(subject_pos).getCoaching_id()));
				entity.addPart("standard_id", new StringBody(mDataset2.get(standard_pos).getCoaching_id()));
				//entity.addPart("batch_id", new StringBody(mDataset.get(batch_pos).getBatch_id()));
				for (int p = 0; p < mDatasetFilter.size(); p++) {
					entity.addPart("batch_id[]", new StringBody(mDatasetFilter.get(p).getBatch_id()));
				}


				for (int p = 0; p < mDatasetFilter_standard.size(); p++) {
					entity.addPart("subject[]", new StringBody(mDatasetFilter_standard.get(p).getBatch_id()));
				}
				entity.addPart("type", new StringBody(Type));
				entity.addPart("date", new StringBody(date.getText().toString()));
				entity.addPart("start_time", new StringBody(start_time.getText().toString()));
				entity.addPart("end_time", new StringBody(end_time.getText().toString()));
				entity.addPart("branch_id", new StringBody(userSession.getBranchId()));
				entity.addPart("class_room", new StringBody(classroom.getText().toString()));
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
									replaceFragment(R.id.nav_host_fragment,new Schedule(),"Fragment",null);

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
								replaceFragment(R.id.nav_host_fragment,new Schedule(),"Fragment",null);

							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}


			super.onPostExecute(result);
		}

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
			c.add(Calendar.DAY_OF_MONTH,2);
			dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
			return  dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = dateFormat.format(calendar.getTime());
		/*	try {
				*//*if (dateFormat.parse(formattedDate_abc).after(dateFormat.parse(dateString))) {
						Toast.makeText(getActivity(), "Please select correct date", Toast.LENGTH_SHORT).show();
						return;
					}*//*

				    Date oldDate = null;
					oldDate = dateFormat.parse(dateString);
					Date currentDate = new Date();
					String newDate1 = dateFormat.format(currentDate);
					Date newDate = dateFormat.parse(newDate1);

					long diff =  oldDate.getTime() - newDate.getTime();
					long diffInHours = TimeUnit.MILLISECONDS.toDays(diff);

					if(diffInHours>2){
						Toast.makeText(getActivity(), "Please select correct date", Toast.LENGTH_SHORT).show();
						return;
					}


			} catch (ParseException e) {
				e.printStackTrace();
			}*/
			date.setText(dateString);
			formattedDate = dateString;

		}
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

	private void GetSubject(){
		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();


		GetSubjectRequest loginRequest1 = new GetSubjectRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				mDatasetSubject.clear();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){

						JSONObject object = jsonArray.getJSONObject(i);
						StandardModel BatchModel = new StandardModel();
						BatchModel.setCoaching_id(object.getString("subject_id"));
						BatchModel.setCoaching(object.getString("subject"));

						mDatasetSubject.add(BatchModel);
					}

					/*StandardModel BatchModel = new StandardModel();
					BatchModel.setCoaching_id("");
					BatchModel.setCoaching("Please select standard");
					BatchModel.setStatus("");
                    mDatasetSubject.add(BatchModel);*/
					SpinAdapter2 adapter = new SpinAdapter2(getActivity(),
							android.R.layout.simple_spinner_item,
							mDatasetSubject);
					adapter.setDropDownViewResource(R.layout.spinner_item);
					subject.setAdapter(adapter);
					subject.setSelection(adapter.getCount());

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
		loginRequest1.setTag("TAG");        loginRequest1.setShouldCache(false);

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

				mDataset4.clear();
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
						mDataset4.add(BatchModel);


					}

					CoachingLevelModel BatchModel = new CoachingLevelModel();
					BatchModel.setCoachinglevel_id("");
					BatchModel.setCoachinglevel("Please select level");
					BatchModel.setCoaching_type_id("");
					BatchModel.setBranch_id("");
					BatchModel.setStatus("");
					mDataset4.add(BatchModel);
					CoatchingLevelAdapter adapter = new CoatchingLevelAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset4);
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
			// params.put("Accept", "application/json");
			params.put("Authorization","Bearer "+ session.getAPIToken());
			return params;
		}};
		loginRequest1.setTag("TAG");
		loginRequest1.setShouldCache(false);

		requestQueue.add(loginRequest1);
	}
	private void GetBatch(String level) {


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
				mDataset.clear();
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
					batch_layout.setVisibility(View.VISIBLE);
					/*BatchModel BatchModel = new BatchModel();
					BatchModel.setBatch_id("");
					BatchModel.setBatch_name("Please Select Batch");
					BatchModel.setBatch_time("Please Select Batch");
					BatchModel.setStatus("");
					BatchModel.setBranch_id("");
					mDataset.add(BatchModel);*/
					SpinAdapter adapter = new SpinAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset);


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

}