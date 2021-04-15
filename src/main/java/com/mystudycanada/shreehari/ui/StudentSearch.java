package com.mystudycanada.shreehari.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mystudycanada.shreehari.API.GetBatchRequest;
import com.mystudycanada.shreehari.API.GetCoachingLevelRequest;
import com.mystudycanada.shreehari.API.GetStandardRequest;
import com.mystudycanada.shreehari.API.GetStudentRequest;
import com.mystudycanada.shreehari.Adapter.CoatchingLevelAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter;
import com.mystudycanada.shreehari.Adapter.SpinAdapter2;
import com.mystudycanada.shreehari.Adapter.StudentAdapter;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.BatchModel;
import com.mystudycanada.shreehari.Model.CoachingLevelModel;
import com.mystudycanada.shreehari.Model.StudentModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentSearch extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View pd1;
	private RequestQueue requestQueue;
	private UserSession session;
	private Spinner standard,batch;
	private ArrayList<BatchModel> mDataset1 = new ArrayList<>();
	private ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private int standard_pos= 0;
	private int batch_pos= 0;
	private RecyclerView recyleview;
	private StudentAdapter mAdapter;
	private ArrayList<StudentModel> mDataset = new ArrayList<>();
	private ArrayList<StudentModel> filterdNames;
	private boolean isFilter = false;
	private ArrayList<CoachingLevelModel> mDataset3 = new ArrayList<>();
	private Spinner coaching_level;
	private int coachin_pos = 0;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_student_search, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());
		standard = view.findViewById(R.id.standard);
		batch = view.findViewById(R.id.batch);
		coaching_level = view.findViewById(R.id.coaching_level);
		recyleview = view.findViewById(R.id.recyleview);
		mDataset.clear();
		recyleview.setHasFixedSize(true);
		recyleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mAdapter = new StudentAdapter(mDataset, new StudentAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				if(isFilter){
					Profile fragobj = new Profile();
					Bundle bundle = new Bundle();
					bundle.putString("User_Id", filterdNames.get(item).getMobile_user_master_id());
					fragobj.setArguments(bundle);
					replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
				}else {
					Profile fragobj = new Profile();
					Bundle bundle = new Bundle();
					bundle.putString("User_Id", mDataset.get(item).getMobile_user_master_id());
					fragobj.setArguments(bundle);
					replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
				}
			}
		});
		recyleview.setAdapter(mAdapter);
		GetStandard();
		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				standard_pos = i;


				if(standard_pos!=mDataset2.size()-1) {
					try {
						mDataset3.clear();
						mDataset1.clear();
						mDataset.clear();
						GetCoachingLevel(mDataset2.get(standard_pos).getCoaching_id());
						//GetResult(mDataset2.get(standard_pos).getCoaching_id(), mDataset1.get(batch_pos).getBatch_id());
					} catch (Exception e) {
						GetResult("0", "0");
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
				mDataset.clear();
				if(batch_pos!=mDataset1.size()-1) {
					try {
						GetResult(mDataset2.get(standard_pos).getCoaching_id(), mDataset1.get(batch_pos).getBatch_id());

					} catch (Exception e) {
						GetResult("0", "0");

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
				coachin_pos = i;
				Log.e("batch_pos",""+coachin_pos+"  "  +mDataset.size());
				if(coachin_pos!=mDataset3.size()-1){
					try {
						mDataset1.clear();
						GetBatch(mDataset3.get(coachin_pos).getCoachinglevel_id());
					//	GetResult(mDataset2.get(standard_pos).getCoaching_id(),mDataset1.get(batch_pos).getBatch_id());

					}catch (Exception e){
						//	GetStudnet("0","0");

					}
				}


			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		EditText search = view.findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				filter(charSequence.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		GetResult("", "");
		return view;
	}


	private void filter(String text) {
		//new array list that will hold the filtered data
		filterdNames = new ArrayList<>();
		isFilter = true;
		//looping through existing elements
		for (StudentModel wp : mDataset) {
			//if the existing elements contains the search input
			if (wp.getFirst_name().toLowerCase().contains(text.toLowerCase())||wp.getCoaching_reg_no().toLowerCase().contains(text.toLowerCase())) {
				//adding the element to filtered list
				filterdNames.add(wp);
			}
		}

		//calling a method of the adapter class and passing the filtered list
		mAdapter.filterList(filterdNames);
		mAdapter.notifyDataSetChanged();
	}

	private void GetResult(String standard_id,String batch_id) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetStudentRequest loginRequest = new GetStudentRequest(standard_id,batch_id,new Response.Listener<String>() {
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
						StudentModel announcementModel = new StudentModel();
						announcementModel.setMobile_user_master_id(object.getString("mobile_user_master_id"));
						announcementModel.setBatch(object.getString("batch"));
						announcementModel.setStandard(object.getString("standard"));
						announcementModel.setCoaching_reg_no(object.getString("coaching_reg_no"));
						announcementModel.setFirst_name(object.getString("first_name"));
						announcementModel.setLast_name(object.getString("last_name"));
						announcementModel.setProfile_pic(object.getString("profile_pic"));

						mDataset.add(announcementModel);
					}

					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {

					e.printStackTrace();

					Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG);
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
		loginRequest.setTag("TAG");        loginRequest.setShouldCache(false);

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

					CoachingLevelModel BatchModel = new CoachingLevelModel();
					BatchModel.setCoachinglevel_id("");
					BatchModel.setCoachinglevel("Please select level");
					BatchModel.setCoaching_type_id("");
					BatchModel.setBranch_id("");
					BatchModel.setStatus("");
					mDataset3.add(BatchModel);
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
						mDataset1.add(BatchModel);



					}

					BatchModel BatchModel = new BatchModel();
					BatchModel.setBatch_id("");
					BatchModel.setBatch_name("Please Select Batch");
					BatchModel.setBatch_time("Please Select Batch");
					BatchModel.setStatus("");
					BatchModel.setBranch_id("");
					mDataset1.add(BatchModel);
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

	@Override
	public void onResume() {
		super.onResume();
		isFilter = false;
		mAdapter = new StudentAdapter(mDataset, new StudentAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				if(isFilter){
					Profile fragobj = new Profile();
					Bundle bundle = new Bundle();
					bundle.putString("User_Id", filterdNames.get(item).getMobile_user_master_id());
					fragobj.setArguments(bundle);
					replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
				}else {
					Profile fragobj = new Profile();
					Bundle bundle = new Bundle();
					bundle.putString("User_Id", mDataset.get(item).getMobile_user_master_id());
					fragobj.setArguments(bundle);
					replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
				}
			}
		});
		recyleview.setAdapter(mAdapter);
	}
}