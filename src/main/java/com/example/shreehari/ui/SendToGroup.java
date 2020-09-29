package com.example.shreehari.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.collection.ArraySet;
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
import com.example.shreehari.API.AndroidMultiPartEntity;
import com.example.shreehari.API.FileUtils;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.Adapter.BatchesCheckBoxAdapter;
import com.example.shreehari.Adapter.ExamAdapter;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SendToGroup extends Fragment {
	// Store instance variables

	private KProgressHUD progressDialog;
	private List<Uri> mSelected = new ArrayList<>();
	private EditText Title,Description;
	private ImageView Done;
	private ImageView Attechment;
	private String send_to_group = "all";
	ArrayList<String>  strings = new ArrayList<>();
	private TextView all,studnet,staff,parent,Attechmenttxt;
	private boolean isAllSelected = true;
	private boolean isStudentSelected = false;
	private boolean isStaffSelected = false;
	private boolean isParentSelected = false;
	private static String publish_date;
	private static TextView date;
	private CheckBox checkbox;
	private String DateAndTime = "";
	private ArrayList<BatchModel> mDataset1 = new ArrayList<>();
	private ArrayList<BatchModel> mDatasetFilter = new ArrayList<>();
	private RequestQueue requestQueue;
	private UserSession session;
	private ImageView batch;
	private String[] checkBoxList;
	private boolean[] checkedItems;
	private RecyclerView recyleview;
	private LinearLayoutManager linearlayout;
	private BatchesCheckBoxAdapter mAdapter;
	private LinearLayout batch_layout;



	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_group, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		Title = view.findViewById(R.id.title);
		batch = view.findViewById(R.id.batch);
		Description = view.findViewById(R.id.description);
		Done = view.findViewById(R.id.done);
		Attechment = view.findViewById(R.id.attechment);
		Attechmenttxt = view.findViewById(R.id.attechmenttxt);
		all = view.findViewById(R.id.all);
		staff = view.findViewById(R.id.staff);
		studnet = view.findViewById(R.id.studnet);
		parent = view.findViewById(R.id.parent);
		date = view.findViewById(R.id.date);
		checkbox = view.findViewById(R.id.checkbox);
		batch_layout = view.findViewById(R.id.batch_layout);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
					String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

					DateAndTime = currentDate+" " + currentTime;
				}
			}
		});

		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});

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


		batch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				checkBoxList = new String[mDataset1.size()];
				checkedItems = new boolean[mDataset1.size()];
				for (int i =  0 ; i < mDataset1.size() ; i++){
					checkedItems[i] = false;
					checkBoxList[i] = mDataset1.get(i).getBatch_name();
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
							Batchmodel.setBatch_id(mDataset1.get(which).getBatch_id());
							Batchmodel.setBatch_name(mDataset1.get(which).getBatch_name());
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
		publish_date = getDateTime();
		strings.add("all");
		all.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isAllSelected){
					isAllSelected = false;
					strings.remove("all");
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.clear();
					strings.add("all");
					isAllSelected = true;
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					parent.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					staff.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					studnet.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));

				}
				if(isStudentSelected&&!isStaffSelected&&!isParentSelected){
					batch_layout.setVisibility(View.VISIBLE);
				}else {
					batch_layout.setVisibility(View.GONE);
				}
			}
		});
		GetstandardAndBatch();
		staff.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isStaffSelected){
					strings.remove("staff");
					isStaffSelected = false;
					staff.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("staff");
					isStaffSelected = true;
					staff.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}

				if(isParentSelected&&isStaffSelected&&isStudentSelected){
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					strings.clear();
					strings.add("all");
				}else {
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
					strings.remove("all");
				}
				if(isStudentSelected&&!isStaffSelected&&!isParentSelected){
					batch_layout.setVisibility(View.VISIBLE);
				}else {
					batch_layout.setVisibility(View.GONE);
				}

			}
		});

		studnet.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isStudentSelected){
					strings.remove("student");
					isStudentSelected = false;
					studnet.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("student");
					isStudentSelected = true;
					studnet.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}
				if(isParentSelected&&isStaffSelected&&isStudentSelected){
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					strings.clear();
					strings.add("all");
				}else {
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
					strings.remove("all");
				}
				if(isStudentSelected&&!isStaffSelected&&!isParentSelected){
					batch_layout.setVisibility(View.VISIBLE);
				}else {
					batch_layout.setVisibility(View.GONE);
				}
			}
		});

		parent.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isParentSelected){
					strings.remove("parent");
					isParentSelected = false;
					parent.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("parent");
					isParentSelected = true;
					parent.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}
				if(isParentSelected&&isStaffSelected&&isStudentSelected){
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					strings.clear();
					strings.add("all");
				}else {
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
					strings.remove("all");
				}
				if(isStudentSelected&&!isStaffSelected&&!isParentSelected){
					batch_layout.setVisibility(View.VISIBLE);
				}else {
					batch_layout.setVisibility(View.GONE);
				}
			}
		});

		Attechment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				requestPermission();
			}
		});

		Done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(isParentSelected&&isStaffSelected&&isStudentSelected){
					send_to_group = "all";
				}else if(isParentSelected&&isStaffSelected){
					send_to_group = "parent,staff";
				}else if(isParentSelected&&isStudentSelected){
					send_to_group = "parent,student";
				}else if(isStudentSelected&&isStaffSelected){
					send_to_group = "staff,student";
				}else if(isStudentSelected){
					send_to_group = "student";
				}else if(isStaffSelected){
					send_to_group = "staff";
				}else if(isParentSelected){
					send_to_group = "parent";
				}else {
					send_to_group = "all";
				}
				Log.e("SetndToGroup",send_to_group);
				if(Title.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Please Enter Title", Toast.LENGTH_SHORT).show();
				}else if(Description.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Please Enter Description", Toast.LENGTH_SHORT).show();
				}else if(date.getText().toString().equals("   Select Start Date:")&&!checkbox.isChecked()){
					Toast.makeText(getActivity(), "Please Select Date or Checkbox", Toast.LENGTH_SHORT).show();
				}else if(batch_layout.getVisibility()==View.VISIBLE&&mDatasetFilter.isEmpty()){
					Toast.makeText(getActivity(), "Please Select Batch", Toast.LENGTH_SHORT).show();
				}else {
					new UploadFileToServer().execute();
				}
			}
		});

		view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getActivity().onBackPressed();
			}
		});
		return view;
	}

	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = "http://teqcoder.com/shreeharicrm/api/api/add-announcement-group";

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
				for (int i = 0; i < mSelected.size(); i++) {

					entity.addPart("attachments[]", new FileBody(new File(FileUtils.getPath(getActivity(), mSelected.get(i)))));

				}

				for (int p = 0; p < mDatasetFilter.size(); p++) {
					entity.addPart("batch_ids[]", new StringBody(mDatasetFilter.get(p).getBatch_id()));
				}



				UserSession userSession = new UserSession(getActivity());
				// Extra parameters if you want to pass to server
				entity.addPart("title", new StringBody(Title.getText().toString()));
				entity.addPart("description", new StringBody(Description.getText().toString()));
				entity.addPart("branch_id", new StringBody("1"));
				entity.addPart("publish_datetime ", new StringBody(DateAndTime));
				entity.addPart("send_to_group", new StringBody(send_to_group));
				httppost.setEntity(entity);
				httppost.addHeader("Authorization","Bearer "+userSession.getAPIToken());
				// Making server call
				Log.e("sendto",send_to_group);
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
					builder.setMessage("Updated Successfully!")
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// do LiveVideo_Recording
									replaceFragment(R.id.nav_host_fragment,new Announcements(),"Fragment",null);

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
								replaceFragment(R.id.nav_host_fragment,new Announcements(),"Fragment",null);

							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}


			super.onPostExecute(result);
		}

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

	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{
					Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
		} else {
			openProfilePicPicker();
		}
	}

	private void openProfilePicPicker() {
		Matisse.from(getActivity())
				.choose(MimeType.ofImage())
				.countable(false)
				.maxSelectable(5)
				.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
				.thumbnailScale(0.85f)
				.imageEngine(new PicassoEngine())
				.forResult(REQUEST_CODE_PROFILE_CHOOSE);
	}

	private int REQUEST_CODE_PROFILE_CHOOSE = 909;

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Check which request we're responding to
		if (requestCode == REQUEST_CODE_PROFILE_CHOOSE) {
			// Make sure the request was successful
			if (resultCode == Activity.RESULT_OK && data != null) {
				mSelected = Matisse.obtainResult(data);
				Attechmenttxt.setText("Attechment : "+ mSelected.size() + " File Added");
			}
		}
	}
	private static final int STORAGE_PERMISSION_REQUEST_CODE = 8980;
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[]
			permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case STORAGE_PERMISSION_REQUEST_CODE:
				if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					// check whether storage permission granted or not.
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						//do what you want;
						openProfilePicPicker();
					}
				}
				break;
			default:
				break;
		}
	}


	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
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
			publish_date = dateString;

		}
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

						JSONObject object = jsonArray.getJSONObject(i);
						BatchModel BatchModel = new BatchModel();
						BatchModel.setBatch_id(object.getString("batch_id"));
						BatchModel.setBatch_name(object.getString("batch_name"));
						BatchModel.setBatch_time(object.getString("batch_time"));
						BatchModel.setStatus(object.getString("status"));
						BatchModel.setBranch_id(object.getString("branch_id"));
						mDataset1.add(BatchModel);
					}




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

	}

}