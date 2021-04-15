package com.mystudycanada.shreehari.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.mystudycanada.shreehari.API.AndroidMultiPartEntity;
import com.mystudycanada.shreehari.API.FileUtils;
import com.mystudycanada.shreehari.API.GetBatchRequest;
import com.mystudycanada.shreehari.API.GetCoachingLevelRequest;
import com.mystudycanada.shreehari.API.GetPeriodRequest;
import com.mystudycanada.shreehari.API.GetStandardRequest;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.Adapter.CoatchingLevelAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter2;
import com.mystudycanada.shreehari.Adapter.SpinAdapter4;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.BatchModel;
import com.mystudycanada.shreehari.Model.CoachingLevelModel;
import com.mystudycanada.shreehari.Model.PeriodModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAssignment extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private KProgressHUD progressDialog;
	private ImageView Attechment;
	private List<Uri> mSelected = new ArrayList<>();
	private static TextView date;
	private static TextView date2;
	private EditText homework;
	private EditText classwork;
	private ArrayList<BatchModel> mDataset = new ArrayList<>();
	private UserSession session;
	private RequestQueue requestQueue;
	private ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private ArrayList<PeriodModel> mDataset3 = new ArrayList<>();
	private ArrayList<CoachingLevelModel> mDataset4 = new ArrayList<>();
	private String[] country ;
	private Spinner standard,batch,period,coaching_level;
	private int standard_pos = 0,batch_pos = 0,period_pos=0;
	private ImageView done;
	private ArrayList<String> wareHouseList = new ArrayList<>();
	private int coachin_pos = 0;
	private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assignment, container, false);
		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		Attechment = view.findViewById(R.id.attechment);
		standard = view.findViewById(R.id.standard);
		coaching_level = view.findViewById(R.id.coaching_level);
		period = view.findViewById(R.id.period);
		batch = view.findViewById(R.id.batch);
		date = view.findViewById(R.id.date);
		date2 = view.findViewById(R.id.date2);
		homework = view.findViewById(R.id.homework);
		classwork = view.findViewById(R.id.classwork);
		done = view.findViewById(R.id.done);
		session = new UserSession(getActivity());

		Log.e("token",session.getAPIToken());

		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Log.e("AssignmentBatch",batch_pos + " " + mDataset.size());
				Log.e("AssignmentBatch2",standard_pos + " " + mDataset2.size());
				Log.e("AssignmentBatch",batch_pos + " " + mDataset.size());
				if(date.getText().toString().equals("   Select Start Date:")){
					Toast.makeText(getActivity(),"Please select Start date",Toast.LENGTH_SHORT).show();

				}else if(batch_pos==mDataset.size()-1){
					Toast.makeText(getActivity(), "Please Select Period & Batch & Standard", Toast.LENGTH_SHORT).show();
				}else if(standard_pos==mDataset2.size()-1){
					Toast.makeText(getActivity(), "Please Select Period & Batch & Standard", Toast.LENGTH_SHORT).show();
				}else if(period_pos==mDataset3.size()-1){
					Toast.makeText(getActivity(), "Please Select Period & Batch & Standard", Toast.LENGTH_SHORT).show();
				}else if(date2.getText().toString().equals("   Select End Date:")){
					Toast.makeText(getActivity(),"Please select End date",Toast.LENGTH_SHORT).show();

				}else if(homework.getText().toString().isEmpty()||classwork.getText().toString().isEmpty()){
					Toast.makeText(getActivity(),"Please enter homework and classwork",Toast.LENGTH_SHORT).show();
				}else {
					new UploadFileToServer().execute();
				}

			}
		});

		Attechment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectImage();
			}
		});

		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment1();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});


		date2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(date.getText().toString().equals("   Select Start Date:")){
					Toast.makeText(getActivity(),"Please Select Start Date First...",Toast.LENGTH_SHORT).show();
					return;
				}
				DialogFragment newFragment = new DatePickerFragment2();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});

		GetStandard();


		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				standard_pos = i;
				try {
					mDataset4.clear();
					mDataset.clear();
					GetCoachingLevel(mDataset2.get(standard_pos).getCoaching_id());
					//getPeriod(mDataset.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
				}catch (Exception e){
					Log.e("Eroor",e.getMessage());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		coaching_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				coachin_pos = i;
				Log.e("batch_pos",""+coachin_pos+"  "  +mDataset.size());
				if(coachin_pos!=mDataset4.size()-1){
					try {
						mDataset.clear();
						GetBatch(mDataset4.get(coachin_pos).getCoachinglevel_id());
						//getPeriod(mDataset.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());

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

				try {
					getPeriod(mDataset.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
				}catch (Exception e){
					Log.e("Eroor",e.getMessage());
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				period_pos = i;

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});



		return view;
	}



	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL +"add-assignment";

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

				UserSession userSession = new UserSession(getActivity());
				// Extra parameters if you want to pass to server
				entity.addPart("date", new StringBody(date.getText().toString()));
				entity.addPart("due_date", new StringBody(date2.getText().toString()));
				entity.addPart("standard_id", new StringBody(mDataset2.get(standard_pos).getCoaching_id()));
				entity.addPart("batch_id", new StringBody(mDataset.get(batch_pos).getBatch_id()));
				entity.addPart("branch_id", new StringBody(userSession.getBranchId()));
				entity.addPart("period", new StringBody(mDataset3.get(period_pos).getId()));
				entity.addPart("class_work", new StringBody(classwork.getText().toString()));
				entity.addPart("home_work", new StringBody(homework.getText().toString()));
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
					builder.setMessage("Updated Successfully!")
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// do LiveVideo_Recording
									replaceFragment(R.id.nav_host_fragment,new Assignment(),"Fragment",null);

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
								replaceFragment(R.id.nav_host_fragment,new Assignment(),"Fragment",null);

							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}


			super.onPostExecute(result);
		}

	}



	// Select image from camera and gallery
	private void selectImage() {
		try {
			PackageManager pm = getActivity().getPackageManager();
			int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
			if (hasPerm == PackageManager.PERMISSION_GRANTED) {
				final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.gallary),getString(R.string.cancel)};
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.select_option);
				builder.setItems(options, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (options[item].equals(getResources().getString(R.string.take_photo))) {
							dialog.dismiss();
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, PICK_IMAGE_CAMERA);
						} else if (options[item].equals(getResources().getString(R.string.gallary))) {
							dialog.dismiss();
							Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
						} else if (options[item].equals(getResources().getString(R.string.cancel))) {
							dialog.dismiss();
						}
					}
				});
				builder.show();
			} else
				checkAndroidVersion();
			//Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			checkAndroidVersion();
			//Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Uri selectedImage = data.getData();
		if (requestCode == PICK_IMAGE_CAMERA) {
			try {

				mSelected.add(selectedImage);

				//txt_injury.setText(imgPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (requestCode == PICK_IMAGE_GALLERY) {
			try {
				mSelected.add(selectedImage);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = {MediaStore.Audio.Media.DATA};
		Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void checkAndroidVersion() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checkAndRequestPermissions();

		} else {
			// code for lollipop and pre-lollipop devices
		}

	}

	private boolean checkAndRequestPermissions() {
		int camera = ContextCompat.checkSelfPermission(getActivity(),
				Manifest.permission.CAMERA);
		int wtite = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
		List<String> listPermissionsNeeded = new ArrayList<>();
		if (wtite != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		if (camera != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.CAMERA);
		}
		if (read != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
			return false;
		}
		return true;
	}
	public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		Log.d("in fragment on request", "Permission callback called-------");
		switch (requestCode) {
			case REQUEST_ID_MULTIPLE_PERMISSIONS: {

				Map<String, Integer> perms = new HashMap<>();
				// Initialize the map with both permissions
				perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				// Fill with actual results from user
				if (grantResults.length > 0) {
					for (int i = 0; i < permissions.length; i++)
						perms.put(permissions[i], grantResults[i]);
					// Check for both permissions
					if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
							&& perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
						Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
						// process the normal flow
						//else any one or both the permissions are not granted
					} else {
						Log.d("in fragment on request", "Some permissions are not granted ask again ");
						//permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
						//show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
						if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
							showDialogOK("Camera and Storage Permission required for this app",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											switch (which) {
												case DialogInterface.BUTTON_POSITIVE:
													checkAndRequestPermissions();
													break;
												case DialogInterface.BUTTON_NEGATIVE:
													// proceed with logic by disabling the related features or quit the app.
													break;
											}
										}
									});
						}
						//permission is denied (and never ask again is  checked)
						//shouldShowRequestPermissionRationale will return false
						else {
							Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
									.show();
							//                            //proceed with logic by disabling the related features or quit the app.
						}
					}
				}
			}
		}

	}
	private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
		new android.app.AlertDialog.Builder(getActivity())
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", okListener)
				.create()
				.show();
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

					BatchModel BatchModel = new BatchModel();
					BatchModel.setBatch_id("");
					BatchModel.setBatch_name("Please Select Batch");
					BatchModel.setBatch_time("Please Select Batch");
					BatchModel.setStatus("");
					BatchModel.setBranch_id("");
					mDataset.add(BatchModel);
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





	public void getPeriod(String standard, String batch){

		Log.e("StandardAndBatch",standard+ "  "+ batch);

		GetPeriodRequest loginRequest2 = new GetPeriodRequest(standard,batch,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				mDataset3.clear();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){


						JSONObject object = jsonArray.getJSONObject(i);
						PeriodModel BatchModel = new PeriodModel();
						BatchModel.setId(String.valueOf(object.getInt("mobile_schedule_id")));
						BatchModel.setPeriod(object.getString("period"));
						mDataset3.add(BatchModel);
					}


					PeriodModel BatchModel = new PeriodModel();
					BatchModel.setId(String.valueOf(""));
					BatchModel.setPeriod("Please select period");
					mDataset3.add(BatchModel);

					SpinAdapter4 adapter = new SpinAdapter4(getActivity(),
							android.R.layout.simple_spinner_item,
							mDataset3);
					adapter.setDropDownViewResource(R.layout.spinner_item);
					period.setAdapter(adapter);
					period.setSelection(adapter.getCount());
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
			// params.put("Accept", "application/json");
			params.put("Authorization","Bearer "+ session.getAPIToken());
			return params;
		}};
		loginRequest2.setTag("TAG");
		loginRequest2.setShouldCache(false);

		requestQueue.add(loginRequest2);
	}

	public static class DatePickerFragment1 extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {

		private String formattedDate;

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

		private String formattedDate;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			try {
				Date datetostring = dateFormat.parse(date.getText().toString());
				dialog.getDatePicker().setMinDate(datetostring.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			/*try {
				dialog.getDatePicker().setMinDate(dateFormat.parse(date.getText().toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}*/
			return  dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = dateFormat.format(calendar.getTime());

			date2.setText(dateString);
		}
	}


}