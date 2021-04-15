package com.mystudycanada.shreehari.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mystudycanada.shreehari.API.AndroidMultiPartEntity;
import com.mystudycanada.shreehari.API.GetBatchRequest;
import com.mystudycanada.shreehari.API.GetCoachingLevelRequest;
import com.mystudycanada.shreehari.API.GetStandardRequest;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.Adapter.BatchesCheckBoxAdapter;
import com.mystudycanada.shreehari.Adapter.CoatchingLevelAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter2;
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
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SendToGroup extends Fragment {
	// Store instance variables

	private KProgressHUD progressDialog;
	private List<File> mSelected = new ArrayList<>();
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
	private ImageView level;
	private String[] checkBoxList;
	private boolean[] checkedItems;
	private RecyclerView recyleview;
	private LinearLayoutManager linearlayout;
	private BatchesCheckBoxAdapter mAdapter;
	private LinearLayout batch_layout;
	private LinearLayout level_layout;

	private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

	private Spinner standard,coaching_level;
	private ArrayList<StandardModel> mDatasetStandard = new ArrayList<>();
	private int standard_pos = 0;
	private ArrayList<CoachingLevelModel> mDatasetLevel = new ArrayList<>();
	private LinearLayout spinner_ids;
	private RecyclerView mLevelRecyleview;
	private BatchesCheckBoxAdapter mLevelAdapter;
	private ArrayList<BatchModel> mLevelDataset = new ArrayList<>();


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
		level = view.findViewById(R.id.level);
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
		level_layout = view.findViewById(R.id.level_layout);

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

		spinner_ids = view.findViewById(R.id.spinner_ids);
		standard = view.findViewById(R.id.standard);
		coaching_level = view.findViewById(R.id.coaching_level);


		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

				standard_pos = i;
				batch_layout.setVisibility(View.GONE);

				mDatasetLevel.clear();
				if(standard_pos!=mDatasetStandard.size()-1){
					try {
						mLevelDataset.clear();
						mDatasetFilter.clear();
						mLevelAdapter.notifyDataSetChanged();
						level_layout.setVisibility(View.VISIBLE);
						GetCoachingLevel(mDatasetStandard.get(standard_pos).getCoaching_id());
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

				mDataset1.clear();

			//	Log.e("batch_pos",""+coachin_pos+"  "  +mDataset.size());
				if(i!=mDatasetLevel.size()-1){
					try {
						batch_layout.setVisibility(View.VISIBLE);
						GetstandardAndBatch(mDatasetLevel.get(i).getCoachinglevel_id());
					}catch (Exception e){
						//	GetStudnet("0","0");

					}
				}


			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		GetStandard();


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


		mLevelRecyleview = view.findViewById(R.id.rcyleviewlevel);
		mLevelRecyleview.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		mLevelRecyleview.setLayoutManager(linearlayout);
		mLevelAdapter = new BatchesCheckBoxAdapter(mLevelDataset, new BatchesCheckBoxAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {
				mLevelDataset.remove(item);
				mLevelAdapter.notifyDataSetChanged();
				mDatasetFilter.clear();
				mAdapter.notifyDataSetChanged();
				new GetBatchArray().execute();
			}
		});
		mLevelRecyleview.setAdapter(mLevelAdapter);


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


		level.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				checkBoxList = new String[mDatasetLevel.size()];
				checkedItems = new boolean[mDatasetLevel.size()];
				for (int i =  0 ; i < mDatasetLevel.size() ; i++){
					checkedItems[i] = false;
					checkBoxList[i] = mDatasetLevel.get(i).getCoachinglevel();
				}


				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose some Batches");

// Add a checkbox list
				builder.setMultiChoiceItems(checkBoxList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// The user checked or unchecked a box

						for (int j = 0 ; j < mLevelDataset.size() ; j++){

							if(mLevelDataset.get(j).getBatch_name().equals(checkBoxList[which])){
								//	Toast.makeText(getActivity(),"You Have Alredy Check this Batches",Toast.LENGTH_SHORT).show();
								checkedItems[which] = false;
								return;
							}
						}

						if(isChecked){
							BatchModel Batchmodel = new BatchModel();
							Batchmodel.setBatch_id(mDatasetLevel.get(which).getCoachinglevel_id());
							Batchmodel.setBatch_name(mDatasetLevel.get(which).getCoachinglevel());
							mLevelDataset.add(Batchmodel);
						}else {
							mLevelDataset.remove(which);
						}

					}
				});

// Add OK and Cancel buttons
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// The user clicked OK
						batch_layout.setVisibility(View.VISIBLE);
						new GetBatchArray().execute();
						mLevelAdapter.notifyDataSetChanged();
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

				spinner_ids.setVisibility(View.GONE);
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
					spinner_ids.setVisibility(View.VISIBLE);
				}else {
					spinner_ids.setVisibility(View.GONE);
				}


			}
		});

		staff.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
				if(isStaffSelected){

					strings.remove("admin");
					isStaffSelected = false;
					staff.setBackground(getActivity().getDrawable(R.drawable.bg_round_bg));
				}else {

					strings.add("admin");
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
					spinner_ids.setVisibility(View.VISIBLE);
				}else {
					spinner_ids.setVisibility(View.GONE);
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
					spinner_ids.setVisibility(View.VISIBLE);
				}else {
					spinner_ids.setVisibility(View.GONE);
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
					spinner_ids.setVisibility(View.VISIBLE);
				}else {
					spinner_ids.setVisibility(View.GONE);
				}




			}
		});

		Attechment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectImage();
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
					send_to_group = "admin,student";
				}else if(isStudentSelected){
					send_to_group = "student";
				}else if(isStaffSelected){
					send_to_group = "admin";
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
				}else if(spinner_ids.getVisibility()==View.VISIBLE&&mDatasetFilter.isEmpty()){
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



	// Select image from camera and gallery
	private void selectImage() {
		try {
			PackageManager pm = getActivity().getPackageManager();
			int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
			if (hasPerm == PackageManager.PERMISSION_GRANTED) {
				final CharSequence[] options = {getString(R.string.gallary),getString(R.string.cancel)};
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
		if(data!=null){
			Uri selectedImage = data.getData();
			if (requestCode == PICK_IMAGE_CAMERA) {
				try {

					Bitmap bitmap = (Bitmap) data.getExtras().get("data");
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

					Log.e("Activity", "Pick from Camera::>>> ");

					File folder = new File(getContext().getExternalFilesDir(null) + "/" + "Shree Hari Take Images");

					if (!folder.exists()) {
						folder.mkdirs();
					}
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
					File destination = new File(getContext().getExternalFilesDir(null) + "/" + "Shree Hari Take Images", "IMG_" + timeStamp + ".jpg");
					FileOutputStream fo;
					try {
						destination.createNewFile();
						fo = new FileOutputStream(destination);
						fo.write(bytes.toByteArray());
						fo.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mSelected.add(destination);
					//txt_injury.setText(imgPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == PICK_IMAGE_GALLERY) {
				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
					Log.e("Activity", "Pick from Gallery::>>> ");

					String imgPath = getRealPathFromURI(selectedImage);
					mSelected.add(new File(imgPath));
				} catch (Exception e) {
					e.printStackTrace();
				}
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

	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = ServerUtils.BASE_URL +"add-announcement-group";

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

					entity.addPart("attachments[]", new FileBody( mSelected.get(i)));

				}

				for (int p = 0; p < mDatasetFilter.size(); p++) {
					entity.addPart("batch_ids[]", new StringBody(mDatasetFilter.get(p).getBatch_id()));
				}

				UserSession userSession = new UserSession(getActivity());
				// Extra parameters if you want to pass to server
				entity.addPart("title", new StringBody(Title.getText().toString()));
				entity.addPart("description", new StringBody(Description.getText().toString()));
				entity.addPart("branch_id", new StringBody(userSession.getBranchId()));
				entity.addPart("publish_datetime ", new StringBody(DateAndTime));
				entity.addPart("send_to_group", new StringBody(send_to_group));
				httppost.setEntity(entity);
				httppost.addHeader("Authorization","Bearer "+userSession.getAPIToken());
				// Making server call
				Log.e("sendto",send_to_group);
                httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
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

	private void GetstandardAndBatch(String level) {

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
				Log.e("getstandardAndBatch", response + " null");
				mDataset1.clear();
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
				mDatasetStandard.clear();
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
						mDatasetStandard.add(BatchModel);
					}

					StandardModel BatchModel = new StandardModel();
					BatchModel.setCoaching_id("");
					BatchModel.setCoaching("Please select standard");
					BatchModel.setStatus("");
					mDatasetStandard.add(BatchModel);
					SpinAdapter2 adapter = new SpinAdapter2(getActivity(),
							android.R.layout.simple_spinner_item,
							mDatasetStandard);
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

				mDatasetLevel.clear();
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
						mDatasetLevel.add(BatchModel);


					}

				/*	CoachingLevelModel BatchModel = new CoachingLevelModel();
					BatchModel.setCoachinglevel_id("");
					BatchModel.setCoachinglevel("Please select level");
					BatchModel.setCoaching_type_id("");
					BatchModel.setBranch_id("");
					BatchModel.setStatus("");
					mDatasetLevel.add(BatchModel);*/
					CoatchingLevelAdapter adapter = new CoatchingLevelAdapter(getActivity(),
							android.R.layout.simple_spinner_item,
							mDatasetLevel);
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
				for (int p = 0; p < mLevelDataset.size(); p++) {
					entity.addPart("coaching_level_id[]", new StringBody(mLevelDataset.get(p).getBatch_id()));
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
			batch_layout.setVisibility(View.VISIBLE);
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




}