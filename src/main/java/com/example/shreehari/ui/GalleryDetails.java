package com.example.shreehari.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.shreehari.API.GetGallaryDetailsRequest;
import com.example.shreehari.Adapter.GallaryAdapter;
import com.example.shreehari.EndlessRecyclerViewScrollListener;
import com.example.shreehari.GallaryView;
import com.example.shreehari.Model.GallaryModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
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

		download.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				for (int i = 0 ; i < mImageStringArray.size() ; i ++ ){
					Log.e("Url",mImageStringArray.get(i));
					DownloadImage(mImageStringArray.get(i));
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