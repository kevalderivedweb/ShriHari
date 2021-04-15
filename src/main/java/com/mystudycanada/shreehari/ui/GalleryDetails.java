package com.mystudycanada.shreehari.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.mystudycanada.shreehari.API.GetGallaryDetailsRequest;
import com.mystudycanada.shreehari.Adapter.GallaryAdapter;
import com.mystudycanada.shreehari.EndlessRecyclerViewScrollListener;
import com.mystudycanada.shreehari.GallaryView;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.GallaryModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GalleryDetails extends Fragment {

	private RequestQueue requestQueue;
	private UserSession session;
	private RecyclerView recyleview;
	private ArrayList<GallaryModel> mDataset = new ArrayList<>();
	private GallaryAdapter mAdapter;
	private String Id;
	private boolean mSelection = false;
	private ImageView download;
	private ArrayList<String> mImageStringArray = new ArrayList<>();
	private int last_size;
	private String Mpage = "1";
	private String CategoryName;
	private TextView name;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gallery, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());

		mDataset.clear();
		try {
			Id = getArguments().getString("Id");
			CategoryName = getArguments().getString("CategoryName");

		} catch (Exception e) {

		}

		download = view.findViewById(R.id.download);
		name  = view.findViewById(R.id.name);
		name.setText(CategoryName);
		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
		recyleview.setLayoutManager(gridLayoutManager);
		mAdapter = new GallaryAdapter("1",mDataset, new GallaryAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item, ArrayList<String> mImageArray) {
				if(mImageArray.isEmpty()){
					download.setVisibility(View.INVISIBLE);
					mSelection = false;
				}
				if(mSelection){

					download.setVisibility(View.VISIBLE);
					mAdapter.setselection(item);
					mImageStringArray = mImageArray;
					if(mImageArray.isEmpty()){
						download.setVisibility(View.INVISIBLE);
						mSelection = false;
					}

				}else {
					Intent intent = new Intent(getActivity(), GallaryView.class);
					intent.putExtra("Img_url",mDataset.get(item).getCategory_image());
					intent.putExtra("CategoryName",CategoryName);
					startActivity(intent);
				}


			}

			@Override
			public void onLongItemClick(int item, ArrayList<String> mImageArray) {
				download.setVisibility(View.VISIBLE);
				mSelection = true;
				mAdapter.setselection(item);
				mImageStringArray = mImageArray;
			}
		});

		checkPermission();

		download.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				for (int i = 0 ; i < mImageStringArray.size() ; i ++ ){
					if (checkPermission()) {
						new Downloading().execute(mImageStringArray.get(i));
					}
				}

			}
		});
		recyleview.setAdapter(mAdapter);
		recyleview.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (page!=last_size){
					Mpage = String.valueOf(page+1);
					GetResult(Id,Mpage);
				}
			}
		});


		GetResult(Id,Mpage);
		return view;
	}

	private void GetResult(String id,String Page) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetGallaryDetailsRequest loginRequest = new GetGallaryDetailsRequest(Page,id,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();


				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");
					last_size = jsonObject1.getInt("last_page");
					JSONArray jsonArray = jsonObject1.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						GallaryModel announcementModel = new GallaryModel();
						announcementModel.setMobile_gallery_category_id("");
						announcementModel.setCategory_name("");
						announcementModel.setCategory_image(object.getString("image"));
						announcementModel.setSelection("No");

						mDataset.add(announcementModel);

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


	public class Downloading extends AsyncTask<String, Integer, String> {
		KProgressHUD progressDialog;
		@Override
		public void onPreExecute() {
			super .onPreExecute();
			progressDialog = KProgressHUD.create(getActivity())
					.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
					.setLabel("Please wait")
					.setCancellable(false)
					.setAnimationSpeed(2)
					.setDimAmount(0.5f)
					.show();

		}

		@Override
		protected String doInBackground(String... url) {
			File mydir = new File(Environment.getExternalStorageDirectory() + "/ShreeHari");
			if (!mydir.exists()) {
				mydir.mkdirs();
			}

			DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
			Uri downloadUri = Uri.parse(url[0]);
			DownloadManager.Request request = new DownloadManager.Request(downloadUri);

			SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
			String date = dateFormat.format(new Date());

			request.setAllowedNetworkTypes(
					DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
					.setAllowedOverRoaming(false)
					.setTitle("Downloading")
					.setDestinationInExternalPublicDir("/ShreeHari", date + ".jpg");

			manager.enqueue(request);
			return mydir.getAbsolutePath() + File.separator + date + ".jpg";
		}

		@Override
		public void onPostExecute(String s) {
			super .onPostExecute(s);
			progressDialog.dismiss();
			Toast.makeText(getActivity(), "Image Saved... "+ s, Toast.LENGTH_SHORT).show();
		}
	}



	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public boolean checkPermission() {
		int currentAPIVersion = Build.VERSION.SDK_INT;
		if (currentAPIVersion >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
					alertBuilder.setCancelable(true);
					alertBuilder.setTitle("Permission necessary");
					alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
					alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
						}
					});
					AlertDialog alert = alertBuilder.create();
					alert.show();
				} else {
					ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
				}
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
	boolean result;
	//Here you can check App Permission

	public void checkAgain() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
			alertBuilder.setCancelable(true);
			alertBuilder.setTitle("Permission necessary");
			alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
			alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
				}
			});
			AlertDialog alert = alertBuilder.create();
			alert.show();
		} else {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[]
			permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
				//code for deny
				checkAgain();
				break;
			default:
				break;
		}
	}


}