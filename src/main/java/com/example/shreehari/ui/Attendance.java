package com.example.shreehari.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.example.shreehari.API.AddAttandanceRequest;
import com.example.shreehari.API.AndroidMultiPartEntity;
import com.example.shreehari.API.FileUtils;
import com.example.shreehari.API.GetAttandanceRequest;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.Adapter.AttandanceAdapter;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Model.AttandanceModel;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Attendance extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private static UserSession session;
	private static RequestQueue requestQueue;
	private RecyclerView recyleview;
	private static ArrayList<AttandanceModel> mDataset = new ArrayList<>();
	private static ArrayList<AttandanceModel> mDataset_new = new ArrayList<>();
	private static AttandanceAdapter mAdapter;
	private Spinner standard,batch;
	private static ArrayList<BatchModel> mDataset1 = new ArrayList<>();
	private static ArrayList<StandardModel> mDataset2 = new ArrayList<>();
	private ArrayList<String> stringArrayList = new ArrayList<>();
	private static int standard_pos = 0;
	private static int batch_pos = 0;
	private static TextView date;
	private ImageView minus,plus;
	private static String formattedDate;
	private ImageView done;
	private static TextView green;
	private static TextView red;
	private static TextView white;
	private static ImageView selector;
	private static boolean selector_bol;
	private static FragmentActivity mContext;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_attendance, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue
		session = new UserSession(getActivity());

		standard = view.findViewById(R.id.standard);
		batch = view.findViewById(R.id.batch);
		date = view.findViewById(R.id.date);
		minus = view.findViewById(R.id.minus);
		plus = view.findViewById(R.id.plus);
		done = view.findViewById(R.id.done);
		green = view.findViewById(R.id.green);
		red = view.findViewById(R.id.red);
		white = view.findViewById(R.id.white);
		selector = view.findViewById(R.id.selector);


		if(session.getUserType().equals("admin")){
			view.findViewById(R.id.done).setVisibility(View.VISIBLE);
			view.findViewById(R.id.selector).setVisibility(View.VISIBLE);
		}else if(session.getUserType().equals("parent")){
			view.findViewById(R.id.done).setVisibility(View.GONE);
			view.findViewById(R.id.selector).setVisibility(View.GONE);
		}else {
			view.findViewById(R.id.done).setVisibility(View.GONE);
			view.findViewById(R.id.selector).setVisibility(View.GONE);
		}
		mDataset.clear();
		mDataset_new.clear();

		mContext = getActivity();

		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		recyleview.setLayoutManager(new GridLayoutManager(getActivity(),3));
		mAdapter = new AttandanceAdapter(mDataset, new AttandanceAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				for (int j = 0 ; j < stringArrayList.size() ; j++){
					if(stringArrayList.get(j).equals(mDataset.get(item).getId())){
						stringArrayList.remove(mDataset.get(item).getId());
						return;
					}
				}
				stringArrayList.add(mDataset.get(item).getId());
			}
		});
		recyleview.setAdapter(mAdapter);

		formattedDate = getDateTime();
		date.setText(getDateTime());

		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});
		/*minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				c.add(Calendar.DATE, -1);
				formattedDate = df.format(c.getTime());

				Log.v("PREVIOUS DATE : ", formattedDate);
				date.setText(formattedDate);
				GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
			}
		});


		plus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				c.add(Calendar.DATE, -1);
				formattedDate = df.format(c.getTime());

				Log.v("PREVIOUS DATE : ", formattedDate);
				date.setText(formattedDate);
				GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
			}
		});
*/
		standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				standard_pos = i;

				try {
					GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());
				}catch (Exception e){
					GetAttandance(formattedDate,"0","0");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		selector.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!mDataset.isEmpty()){
					int Size = mDataset.size();
					if(selector_bol){
						selector.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_right));
						mDataset_new.clear();
						selector_bol = false;
						for(int i = 0 ; i < Size ; i++){
							AttandanceModel announcementModel = new AttandanceModel();
							announcementModel.setFirst_name(mDataset.get(i).getFirst_name());
							announcementModel.setId(String.valueOf(mDataset.get(i).getId()));
							announcementModel.setLast_name(mDataset.get(i).getLast_name());
							announcementModel.setCoaching_reg_no(mDataset.get(i).getCoaching_reg_no());
							announcementModel.setStandard(mDataset.get(i).getStandard());
							announcementModel.setBatch(mDataset.get(i).getBatch());
							announcementModel.setIs_absent("1");
							mDataset_new.add(announcementModel);
							stringArrayList.add(mDataset.get(i).getId());
						}

						mAdapter = new AttandanceAdapter(mDataset_new, new AttandanceAdapter.OnItemClickListener() {
							@Override
							public void onItemClick(int item) {
								stringArrayList.add(mDataset.get(item).getId());
							}
						});
						recyleview.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}else {
						selector.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.close));
						mDataset_new.clear();
						selector_bol = true;
						for(int i = 0 ; i < Size ; i++){
							AttandanceModel announcementModel = new AttandanceModel();
							announcementModel.setFirst_name(mDataset.get(i).getFirst_name());
							announcementModel.setId(String.valueOf(mDataset.get(i).getId()));
							announcementModel.setLast_name(mDataset.get(i).getLast_name());
							announcementModel.setCoaching_reg_no(mDataset.get(i).getCoaching_reg_no());
							announcementModel.setStandard(mDataset.get(i).getStandard());
							announcementModel.setBatch(mDataset.get(i).getBatch());
							announcementModel.setIs_absent("0");
							mDataset_new.add(announcementModel);
							stringArrayList.clear();
						}
						mAdapter = new AttandanceAdapter(mDataset_new, new AttandanceAdapter.OnItemClickListener() {
							@Override
							public void onItemClick(int item) {
								stringArrayList.add(mDataset.get(item).getId());
							}
						});
						recyleview.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}

				}
			}
		});



		batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				batch_pos = i;

				try {
					GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());

				}catch (Exception e){
					GetAttandance(formattedDate,"0","0");

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//AddAttandance(formattedDate,stringArrayList);





				if(batch_pos==mDataset1.size()-1){
					Toast.makeText(getActivity(), "Please Select Batch & Standard", Toast.LENGTH_SHORT).show();
				}else if(standard_pos==mDataset2.size()-1){
					Toast.makeText(getActivity(), "Please Select Standard & Standard", Toast.LENGTH_SHORT).show();
				}else if(stringArrayList.isEmpty()){
					Toast.makeText(getActivity(), "Please Fill Sheet", Toast.LENGTH_SHORT).show();
				}
				else {
					new UploadFileToServer().execute();
				}

			}
		});
		GetstandardAndBatch();
		//GetAttandance(formattedDate,"2","1");
		//GetstandardAndBatch();
		return view;
	}


	private static void GetAttandance(String date, String batch, String standard) {

		Log.e("Response", date + " ----");

		final KProgressHUD progressDialog = KProgressHUD.create(mContext)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAttandanceRequest loginRequest = new GetAttandanceRequest(date,batch,standard,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				mDataset.clear();
				mDataset_new.clear();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");

					green.setText("Presnet : "+jsonObject.getInt("present"));
					red.setText("Absent : "+jsonObject.getInt("absent"));
					white.setText("Total : "+jsonObject.getInt("total"));


					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						AttandanceModel announcementModel = new AttandanceModel();
						announcementModel.setFirst_name(object.getString("first_name"));
						announcementModel.setId(String.valueOf(object.getInt("mobile_user_master_id")));
						announcementModel.setLast_name(object.getString("last_name"));
						announcementModel.setCoaching_reg_no(object.getString("coaching_reg_no"));
						announcementModel.setStandard(object.getString("standard"));
						announcementModel.setBatch(object.getString("batch"));
						announcementModel.setIs_absent(String.valueOf(object.getInt("is_absent")));
						mDataset.add(announcementModel);
					}

					mAdapter.notifyDataSetChanged();

					for(int i = 0 ; i<mDataset.size();i++){
						if(mDataset.get(i).getIs_absent().equals("1")){
							selector_bol= true;
							selector.setImageDrawable(mContext.getResources().getDrawable(R.drawable.close));
							return;
						}else {
							selector_bol= false;
							selector.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_right));
						}
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
					Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
				else if (error instanceof TimeoutError)
					Toast.makeText(mContext, "Connection Timed Out", Toast.LENGTH_SHORT).show();
				else if (error instanceof NetworkError)
					Toast.makeText(mContext, "Bad Network Connection", Toast.LENGTH_SHORT).show();
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
	private void AddAttandance(String date,ArrayList<String> stringArrayList) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		AddAttandanceRequest loginRequest = new AddAttandanceRequest(date,stringArrayList,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					Toast.makeText(getActivity(),jsonObject.getString("ResponseMsg"),Toast.LENGTH_LONG).show();

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


		GetStandardRequest loginRequest1 = new GetStandardRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
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
		requestQueue.add(loginRequest1);

	}

	private String getDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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
			formattedDate = dateString;
			GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());


		}
	}


	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private static final String FILE_UPLOAD_URL = "http://teqcoder.com/shreeharicrm/api/api/add-attendance";
		private KProgressHUD progressDialog;

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
				entity.addPart("date", new StringBody(formattedDate));
				for (int i = 0; i < stringArrayList.size(); i++) {
					entity.addPart("students[]", new StringBody(stringArrayList.get(i)));
				}

				httppost.setEntity(entity);
				httppost.addHeader("Accept", "application/json");

				httppost.addHeader("Authorization","Bearer "+userSession.getAPIToken());
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: " + statusCode;
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
					stringArrayList.clear();
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Updated Successfully!")
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// do LiveVideo_Recording
									GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());

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
								GetAttandance(formattedDate,mDataset1.get(batch_pos).getBatch_id(),mDataset2.get(standard_pos).getCoaching_id());

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


}