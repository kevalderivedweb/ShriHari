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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.shreehari.API.AndroidMultiPartEntity;
import com.example.shreehari.API.FileUtils;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendToGroup extends Fragment {
	// Store instance variables

	private KProgressHUD progressDialog;
	private List<Uri> mSelected = new ArrayList<>();
	private EditText Title,Description;
	private ImageView Done;
	private TextView Attechment;
	private String send_to_group = "";
	ArrayList<String>  strings = new ArrayList<>();
	private TextView all,studnet,staff,parent;
	private boolean isAllSelected = true;
	private boolean isStudentSelected = false;
	private boolean isStaffSelected = false;
	private boolean isParentSelected = false;
	private static String publish_date;
	private static TextView date;


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
		Description = view.findViewById(R.id.description);
		Done = view.findViewById(R.id.done);
		Attechment = view.findViewById(R.id.attechment);
		all = view.findViewById(R.id.all);
		staff = view.findViewById(R.id.staff);
		studnet = view.findViewById(R.id.studnet);
		parent = view.findViewById(R.id.parent);
		date = view.findViewById(R.id.date);


		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});

		publish_date = getDateTime();
		strings.add("All");
		all.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isAllSelected){
					isAllSelected = false;
					strings.remove("All");
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("All");
					isAllSelected = true;
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}
			}
		});

		staff.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isStaffSelected){
					strings.remove("Staff");
					isStaffSelected = false;
					staff.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("Staff");
					isStaffSelected = true;
					staff.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}

				if(isParentSelected&&isStaffSelected&&isParentSelected){
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					strings.add("All");
				}else {
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
					strings.remove("All");
				}

			}
		});

		studnet.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isStudentSelected){
					strings.remove("Student");
					isStudentSelected = false;
					studnet.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("Student");
					isStudentSelected = true;
					studnet.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}
				if(isParentSelected&&isStaffSelected&&isParentSelected){
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					strings.add("All");
				}else {
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
					strings.remove("All");
				}
			}
		});

		parent.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isParentSelected){
					strings.remove("Parent");
					isParentSelected = false;
					parent.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {
					strings.add("Parent");
					isParentSelected = true;
					parent.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
				}
				if(isParentSelected&&isStaffSelected&&isParentSelected){
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg_yellow));
					strings.add("All");
				}else {
					all.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
					strings.remove("All");
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
				for(int i = 0 ; i < strings.size() ; i ++){
					if(send_to_group.isEmpty()){
						send_to_group =  strings.get(i);
					}else {
						send_to_group = send_to_group +  ","+ strings.get(i);
					}

				}
				if(Title.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Please Enter Title", Toast.LENGTH_SHORT).show();
				}else if(Description.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Please Enter Description", Toast.LENGTH_SHORT).show();
				}else {
					new UploadFileToServer().execute();
				}
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
				entity.addPart("batch_ids[]", new StringBody("1"));
				entity.addPart("batch_ids[]", new StringBody("2"));

				UserSession userSession = new UserSession(getActivity());
				// Extra parameters if you want to pass to server
				entity.addPart("title", new StringBody(Title.getText().toString()));
				entity.addPart("description", new StringBody(Description.getText().toString()));
				entity.addPart("branch_id", new StringBody("1"));
				entity.addPart("publish_datetime ", new StringBody(publish_date));
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
				Attechment.setText("Attechment : "+ mSelected.size() + " File Added");
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
			dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
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
}