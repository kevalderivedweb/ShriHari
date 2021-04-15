package com.mystudycanada.shreehari.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.mystudycanada.shreehari.API.GetILTSEXAMRequest;
import com.mystudycanada.shreehari.Adapter.ColorAdapter;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.ColorModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ILTSEXAM extends Fragment {
	// Store instance variables


	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM- yyyy", Locale.getDefault());
	private RequestQueue requestQueue;
	private UserSession session;
	private CompactCalendarView compactCalendarView;
	private ArrayList<ColorModel> mDataset = new ArrayList<>();
	private RecyclerView recyleview;
	private GridLayoutManager linearlayout;
	private ColorAdapter mAdapter;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ilts, container, false);

		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

		session = new UserSession(getActivity());

		TextView DateText = view.findViewById(R.id.month);
		compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
		compactCalendarView.setUseThreeLetterAbbreviation(true);

		Date c = Calendar.getInstance().getTime();
		String formattedDate = dateFormat.format(c);
		DateText.setText(formattedDate);

		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		linearlayout = new GridLayoutManager(getActivity(),2);
		recyleview.setLayoutManager(linearlayout);
		mAdapter = new ColorAdapter(mDataset, new ColorAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

			}
		});
		recyleview.setAdapter(mAdapter);



		compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
			@Override
			public void onDayClick(Date dateClicked) {

			}

			@Override
			public void onMonthScroll(Date firstDayOfNewMonth) {
				String formattedDate = dateFormat.format(firstDayOfNewMonth);
				DateText.setText(formattedDate);
			}
		});

		GetExamDate();
		return view;
	}


	private void GetExamDate() {


		Log.e("Token",session.getAPIToken());
		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();


		GetILTSEXAMRequest loginRequest = new GetILTSEXAMRequest(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("Response1", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONArray jsonArray = jsonObject.getJSONArray("data");
					JSONArray color_details = jsonObject.getJSONArray("color_details");

					for (int i = 0 ; i<jsonArray.length() ; i++){
						JSONObject object = jsonArray.getJSONObject(i);
						int color = Color.parseColor(object.getString("ieltsexamdate_color"));
						long timestamp = milliseconds(object.getString("ieltsexamdate_date"));
						Log.d("miliSecsDate", " = "+timestamp);

						Event event = new Event(color,timestamp,object.get("ieltsexamdate_type"));
						compactCalendarView.addEvent(event);

					}

					Log.e("Colordetails",color_details.toString());

					for (int i = 0 ; i<color_details.length() ; i++){
						JSONObject object = color_details.getJSONObject(i);

						ColorModel colorModel = new ColorModel();
						colorModel.setColor(object.getString("ieltsexamdate_color"));
						colorModel.setType(object.getString("ieltsexamdate_type"));
						mDataset.add(colorModel);

					}

					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {

					Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
			params.put("Accept", "application/json");
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

	public long milliseconds(String date)
	{
		//String date_ = date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date mDate = sdf.parse(date);
			long timeInMilliseconds = mDate.getTime();
			System.out.println("Date in milli :: " + timeInMilliseconds);
			return timeInMilliseconds;
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

}