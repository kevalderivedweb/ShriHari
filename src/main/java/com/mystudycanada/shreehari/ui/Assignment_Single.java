package com.mystudycanada.shreehari.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.mystudycanada.shreehari.API.GetAssingmentDetailsRequest;
import com.mystudycanada.shreehari.Adapter.AttachmentAdapter;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.AttachmentModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Assignment_Single extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private String Assignment_Id;
	private RecyclerView recyleview;
	private AttachmentAdapter mAdapter;
	private RequestQueue requestQueue;
	private UserSession session;
	private ArrayList<AttachmentModel> attachmentArray = new ArrayList<>();

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assignment_3, container, false);

		try {
			Assignment_Id = getArguments().getString("Assignment_Id");

		} catch (Exception e) {

		}
		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());


		view.findViewById(R.id.studentview).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AssignmentViewStatus fragobj = new AssignmentViewStatus();
				Bundle bundle = new Bundle();
				bundle.putString("Announcement_Id", Assignment_Id);
				bundle.putString("Announcement_Status", "2");
				fragobj.setArguments(bundle);
				replaceFragment(R.id.nav_host_fragment,fragobj,"Fragment",null);
			}
		});


		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
		recyleview.setLayoutManager(gridLayoutManager);
		mAdapter = new AttachmentAdapter(attachmentArray, new AttachmentAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				DownloadImage(attachmentArray.get(item).getUrl());
			}

		});
		recyleview.setAdapter(mAdapter);


		view.findViewById(R.id.parentview).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AssignmentViewStatus fragobj = new AssignmentViewStatus();
				Bundle bundle = new Bundle();
				bundle.putString("Announcement_Id", Assignment_Id);
				bundle.putString("Announcement_Status", "1");
				fragobj.setArguments(bundle);
				replaceFragment(R.id.nav_host_fragment,fragobj,"Fragment",null);
			}
		});
		GetAnnouncementDetails(Assignment_Id,view);

		view.findViewById(R.id.attechment).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			//	showPopup();
			}
		});
		return view;
	}

	private void GetAnnouncementDetails(String id,View view) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAssingmentDetailsRequest loginRequest = new GetAssingmentDetailsRequest(id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				attachmentArray.clear();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");

					TextView title = view.findViewById(R.id.subject);
					TextView send_by = view.findViewById(R.id.standard);
					TextView date = view.findViewById(R.id.date);
					TextView date2 = view.findViewById(R.id.date2);
					TextView classwork = view.findViewById(R.id.classwork);
					TextView homework = view.findViewById(R.id.homework);
					TextView period = view.findViewById(R.id.period);
					TextView parent = view.findViewById(R.id.parent);
					TextView student = view.findViewById(R.id.student);


					title.setText(jsonObject1.getString("subject"));
                    student.setText(jsonObject1.getString("student_read_count"));
                    parent.setText(jsonObject1.getString("parent_read_count"));
					period.setText(jsonObject1.getString("period"));
					classwork.setText(jsonObject1.getString("class_work"));
					homework.setText(jsonObject1.getString("home_work"));
					send_by.setText(jsonObject1.getString("standard"));
					date.setText(jsonObject1.getString("date"));
					date2.setText(jsonObject1.getString("due_date"));

					JSONArray jsonArray = jsonObject1.getJSONArray("attachments");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						AttachmentModel attachmentModel = new AttachmentModel();
						attachmentModel.setId(String.valueOf(object.getInt("mobile_assignment_attachment_id")));
						attachmentModel.setUrl(object.getString("attachment_url"));
						attachmentArray.add(attachmentModel);
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
		loginRequest.setTag("TAG");
		loginRequest.setShouldCache(false);

		requestQueue.add(loginRequest);

	}

	private void showPopup() {
		// custom dialog
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.attach_dialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		Window window = dialog.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


		RecyclerView recyleview = dialog.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
		recyleview.setLayoutManager(gridLayoutManager);
		AttachmentAdapter mAdapter = new AttachmentAdapter(attachmentArray, new AttachmentAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

			}

		});
		recyleview.setAdapter(mAdapter);



		dialog.show();

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
	void DownloadImage(String ImageUrl) {

		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
			Toast.makeText(getActivity(),"Need Permission to access storage for Downloading Image",Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(),"Downloading Image....",Toast.LENGTH_SHORT).show();
			//Asynctask to create a thread to downlaod image in the background
			new DownloadsImage().execute(ImageUrl);
		}
	}

	class DownloadsImage extends AsyncTask<String, Void,Void> {

		@Override
		protected Void doInBackground(String... strings) {
			URL url = null;
			try {
				url = new URL(strings[0]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			Bitmap bm = null;
			try {
				bm =    BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}


			File myDirectory = new File(Environment.getExternalStorageDirectory(), "ShreeHari");

			if(!myDirectory.exists()) {
				myDirectory.mkdirs();
			}


			File imageFile = new File(myDirectory, String.valueOf("ShreeHari"+System.currentTimeMillis())+".png"); // Imagename.png
			Log.e("imageFile",imageFile.toString());
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(imageFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try{
				bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
				out.flush();
				out.close();
				// Tell the media scanner about the new file so that it is
				// immediately available to the user.
				MediaScannerConnection.scanFile(getActivity(),new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						// Log.i("ExternalStorage", "Scanned " + path + ":");
						//    Log.i("ExternalStorage", "-> uri=" + uri);
					}
				});
			} catch(Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			Toast.makeText(getActivity(),"Image Saved....",Toast.LENGTH_SHORT).show();
		}
	}


}