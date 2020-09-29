package com.example.shreehari.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.shreehari.API.GetGallaryRequest;
import com.example.shreehari.Adapter.GallaryAdapter;
import com.example.shreehari.EndlessRecyclerViewScrollListener;
import com.example.shreehari.Model.GallaryModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gallery extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View pd1;
	private RequestQueue requestQueue;
	private UserSession session;
	private RecyclerView recyleview;
	private ArrayList<GallaryModel> mDataset = new ArrayList<>();
	private GallaryAdapter mAdapter;
	private int last_size;
	private String Mpage = "1";


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
		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
		recyleview.setLayoutManager(gridLayoutManager);
		mAdapter = new GallaryAdapter("0",mDataset, new GallaryAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item, ArrayList<String> mImageArray) {

				GalleryDetails fragobj = new GalleryDetails();
				Bundle bundle = new Bundle();
				bundle.putString("Id", mDataset.get(item).getMobile_gallery_category_id());
				bundle.putString("CategoryName", mDataset.get(item).getCategory_name());
				fragobj.setArguments(bundle);
				replaceFragment(R.id.nav_host_fragment,fragobj,"Fragment",null);

			}

			@Override
			public void onLongItemClick(int item, ArrayList<String> mImageArray) {

			}
		});
		recyleview.setAdapter(mAdapter);
		recyleview.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (page!=last_size){
					 Mpage = String.valueOf(page+1);
					GetImages(Mpage);
				}
			}
		});


		GetImages(Mpage);
		return view;
	}

	private void GetImages(String mpage){
		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();
		AndroidNetworking.get("http://teqcoder.com/shreeharicrm/api/api/get-gallery-category?page="+mpage)
				.addHeaders("Authorization", "Bearer "+ session.getAPIToken())
				.setTag("test")
				.setPriority(Priority.LOW)
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						Log.e("Response",response);

						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(response);

							JSONObject jsonObject1 = jsonObject.getJSONObject("data");
							last_size = jsonObject1.getInt("last_page");
							JSONArray jsonArray = jsonObject1.getJSONArray("data");

							for (int i = 0 ; i<jsonArray.length() ; i++){
								JSONObject object = jsonArray.getJSONObject(i);
								GallaryModel announcementModel = new GallaryModel();
								announcementModel.setMobile_gallery_category_id(String.valueOf(object.getInt("mobile_gallery_category_id")));
								announcementModel.setCategory_name(object.getString("category_name"));
								announcementModel.setCategory_image(object.getString("category_image"));
								announcementModel.setSelection("No");

								mDataset.add(announcementModel);

							}

							mAdapter.notifyDataSetChanged();

						} catch (JSONException e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onError(ANError anError) {
						progressDialog.dismiss();
						Log.e("Response",anError.getErrorBody());
					}
				});

	}

	private void GetResult(String mpage) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetGallaryRequest loginRequest = new GetGallaryRequest(mpage,new Response.Listener<String>() {
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
						announcementModel.setMobile_gallery_category_id(String.valueOf(object.getInt("mobile_gallery_category_id")));
						announcementModel.setCategory_name(object.getString("category_name"));
						announcementModel.setCategory_image(object.getString("category_image"));
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
			params.put("Accept", "application/json");
			params.put("Authorization","Bearer "+ session.getAPIToken());
			return params;
		}};
		loginRequest.setTag("TAG");
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

}