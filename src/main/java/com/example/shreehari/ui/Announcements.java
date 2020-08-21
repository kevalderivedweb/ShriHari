package com.example.shreehari.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.shreehari.API.GetAnnouncementRequest;
import com.example.shreehari.API.GetAnnouncementRequestFilter;
import com.example.shreehari.Adapter.AnnouncementAdapter;
import com.example.shreehari.Adapter.SpinAdapter5;
import com.example.shreehari.Model.AnnouncementModel;
import com.example.shreehari.Model.TypeModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Announcements extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View details;
	private LinearLayout mview;
	private ImageView madd;
	private UserSession session;
	private RequestQueue requestQueue;
	private RecyclerView recyleview;
	private AnnouncementAdapter mAdapter;
	private ArrayList<AnnouncementModel> mDataset = new ArrayList<>();
	private ArrayList<TypeModel> mDataset2 = new ArrayList<>();
	private int type_pos = 0;
	private TextView start_date,due_date;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_announcements, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());

		view.findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopup();
			}
		});
		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		recyleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mAdapter = new AnnouncementAdapter(mDataset, new AnnouncementAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				AnnouncementsDetails fragobj = new AnnouncementsDetails();
				Bundle bundle = new Bundle();
				bundle.putString("Announcement_Id", mDataset.get(item).getId());
				fragobj.setArguments(bundle);
				replaceFragment(R.id.nav_host_fragment,fragobj,"Fragment",null);

			}
		});
		recyleview.setAdapter(mAdapter);

		mview = view.findViewById(R.id.view);
		madd = view.findViewById(R.id.add);
		madd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mview.getVisibility()==View.VISIBLE){
					madd.setImageDrawable(getResources().getDrawable(R.drawable.add2));
					mview.setVisibility(View.GONE);
				}else {
					madd.setImageDrawable(getResources().getDrawable(R.drawable.add_ano));
					mview.setVisibility(View.VISIBLE);
				}


			}
		});

		ImageView indi = view.findViewById(R.id.indi);
		ImageView group = view.findViewById(R.id.group);

		indi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddAnnouncements(),"Fragment",null);

			}
		});

		group.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new SendToGroup(),"Fragment",null);

			}
		});



		GetAnnouncement();
		return view;
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

	private void GetAnnouncement() {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAnnouncementRequest loginRequest = new GetAnnouncementRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");

					JSONArray jsonArray = jsonObject1.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						AnnouncementModel announcementModel = new AnnouncementModel();
						announcementModel.setTitle(object.getString("title"));
						announcementModel.setId(String.valueOf(object.getInt("mobile_announcement_id")));
						announcementModel.setDescription(object.getString("description"));
						announcementModel.setSend_by(object.getString("send_by"));
						announcementModel.setDate(object.getString("date"));
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

	private void showPopup() {
		// custom dialog
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.announcement_filter_dialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		Window window = dialog.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

		Spinner type = dialog.findViewById(R.id.type);
		start_date = dialog.findViewById(R.id.start_date);
		due_date = dialog.findViewById(R.id.due_date);
		TextView done = dialog.findViewById(R.id.done);

		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GetAnnouncement2(mDataset2.get(type_pos).getTitle(),start_date.getText().toString(),due_date.getText().toString());
			}
		});

		start_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});

		due_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogFragment newFragment1 = new DatePickerFragment1();
				newFragment1.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});

		type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				type_pos = i;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		TypeModel typeModel1 = new TypeModel();
		typeModel1.setTitle("parent");
		mDataset2.add(typeModel1);

		TypeModel typeModel2 = new TypeModel();
		typeModel2.setTitle("student");
		mDataset2.add(typeModel2);

		SpinAdapter5 adapter = new SpinAdapter5(getActivity(),
				android.R.layout.simple_spinner_item,
				mDataset2);
		type.setAdapter(adapter);
		dialog.show();

	}


	public class DatePickerFragment extends DialogFragment
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
			start_date.setText(day+"-"+month+1+"-"+year);
		}
	}

	public class DatePickerFragment1 extends DialogFragment
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
			due_date.setText(day+"-"+month+1+"-"+year);
		}
	}

	private void GetAnnouncement2(String type,String start_date,String end_date) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetAnnouncementRequestFilter loginRequest = new GetAnnouncementRequestFilter(type,start_date,end_date,new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();
				mDataset.clear();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");

					JSONArray jsonArray = jsonObject1.getJSONArray("data");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						AnnouncementModel announcementModel = new AnnouncementModel();
						announcementModel.setTitle(object.getString("title"));
						announcementModel.setId(String.valueOf(object.getInt("mobile_announcement_id")));
						announcementModel.setDescription(object.getString("description"));
						announcementModel.setSend_by(object.getString("send_by"));
						announcementModel.setDate(object.getString("date"));
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



}