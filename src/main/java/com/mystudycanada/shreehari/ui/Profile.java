package com.mystudycanada.shreehari.ui;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mystudycanada.shreehari.API.GetStudentDetailsRequest;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

public class Profile extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View pd1;
	private String Profile_Id;
	private RequestQueue requestQueue;
	private UserSession session;
	private TextView phone_no;
	private TextView no;
	private LinearLayout gr_details;
	private ImageView WhatsApp_Student_call,WhatsApp_Parents_call,Number_Studen_call,Number_Parents_call;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		session = new UserSession(getActivity());
		try {
			Profile_Id = getArguments().getString("User_Id");

		} catch (Exception e) {
			Profile_Id = session.getUserId();
		}

		Log.e("User_id", Profile_Id);

		WhatsApp_Student_call = view.findViewById(R.id.wap1);
		WhatsApp_Parents_call = view.findViewById(R.id.wap2);
		Number_Studen_call = view.findViewById(R.id.no2);
		Number_Parents_call = view.findViewById(R.id.phone_no2);
		no = view.findViewById(R.id.no);

		if(session.getUserType().equals("admin")){
			WhatsApp_Student_call.setVisibility(View.VISIBLE);
			WhatsApp_Parents_call.setVisibility(View.VISIBLE);
			Number_Studen_call.setVisibility(View.VISIBLE);
			Number_Parents_call.setVisibility(View.VISIBLE);
			view.findViewById(R.id.logout).setVisibility(View.GONE);
		}else {
			WhatsApp_Student_call.setVisibility(View.GONE);
			WhatsApp_Parents_call.setVisibility(View.GONE);
			Number_Studen_call.setVisibility(View.GONE);
			Number_Parents_call.setVisibility(View.GONE);
		}
		gr_details = view.findViewById(R.id.gr_details);
		view.findViewById(R.id.no2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FunctionCall(no.getText().toString());
			}
		});

		view.findViewById(R.id.phone_no2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FunctionCall(phone_no.getText().toString());
			}
		});
		requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue


		GetAnnouncementDetails(Profile_Id,view);

		boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");

		view.findViewById(R.id.wap1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				whatsapp(isWhatsappInstalled,no.getText().toString());
			}
		});

		view.findViewById(R.id.wap2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				whatsapp(isWhatsappInstalled,phone_no.getText().toString());
			}
		});

		view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				session.logout();
				clearAppData();
				restartApp();
			}
		});


		return view;
	}




	private void whatsapp(boolean isWhatsappInstalled,String number) {
		if (isWhatsappInstalled) {
			/*Uri uri = Uri.parse("smsto:" +"+91"+ number);
			Intent i = new Intent(Intent.ACTION_SENDTO, uri);
			i.setPackage("com.whatsapp");
			startActivity(Intent.createChooser(i, ""));
*/
			startActivity(
					new Intent(Intent.ACTION_VIEW,
							Uri.parse(
									String.format("https://api.whatsapp.com/send?phone=%s&text=%s", "+91"+number, "")
							)
					)
			);
		} else {
			Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();

		}
	}


	private boolean whatsappInstalledOrNot(String uri) {
		PackageManager pm = getActivity().getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}


	private void FunctionCall(String number){
		Intent intentCallForward = new Intent(Intent.ACTION_DIAL); // ACTION_CALL
		Uri uri2 = Uri.fromParts("tel", number, "#");
		intentCallForward.setData(uri2);
		startActivity(intentCallForward);
	}

	private void GetAnnouncementDetails(String id,View view) {


		final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		GetStudentDetailsRequest loginRequest = new GetStudentDetailsRequest(id,new Response.Listener<String>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(String response) {
				Log.e("Response", response + " null");
				progressDialog.dismiss();

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);

					JSONObject jsonObject1 = jsonObject.getJSONObject("data");


					TextView standard = view.findViewById(R.id.standard);
					TextView coach_no = view.findViewById(R.id.coach_no);
					TextView gr_no = view.findViewById(R.id.gr_no);

					TextView b_day = view.findViewById(R.id.b_day);
					TextView a_day = view.findViewById(R.id.a_day);
					TextView gender = view.findViewById(R.id.gender);
					TextView status = view.findViewById(R.id.status);
					TextView name = view.findViewById(R.id.name);
					TextView coach_name = view.findViewById(R.id.coach_name);
					TextView level_name = view.findViewById(R.id.level_name);
					TextView batch_name = view.findViewById(R.id.batch_name);
					TextView email_name = view.findViewById(R.id.email_name);
					phone_no = view.findViewById(R.id.phone_no);
					TextView main_name = view.findViewById(R.id.main_name);
					standard.setText(jsonObject1.getString("standard"));
					coach_no.setText(jsonObject1.getString("coaching_reg_no"));
					b_day.setText(jsonObject1.getString("birth_date"));
					a_day.setText(jsonObject1.getString("joining_date"));
					status.setText(jsonObject1.getString("status"));
					gr_no.setText(jsonObject1.getString("coaching_reg_no"));
					no.setText(jsonObject1.getString("mobile_no"));
					coach_name.setText(jsonObject1.getString("standard"));
					level_name.setText(jsonObject1.getString("coachinglevel"));
					batch_name.setText(jsonObject1.getString("batch"));
					email_name.setText(jsonObject1.getString("email"));
					main_name.setText(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
					ImageView imageView1 = (ImageView) view.findViewById(R.id.profile_image2);
					Glide.with(getActivity()).load(jsonObject1.getString("profile_pic")).circleCrop().into(imageView1);

					try {
						phone_no.setText(jsonObject1.getJSONObject("guardian_details").getString("mobile_no"));
						name.setText(jsonObject1.getJSONObject("guardian_details").getString("first_name")+" "+jsonObject1.getJSONObject("guardian_details").getString("last_name"));
						gr_details.setVisibility(View.VISIBLE);
					}catch (Exception e){
						gr_details.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
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
		loginRequest.setTag("TAG");        loginRequest.setShouldCache(false);

		requestQueue.add(loginRequest);

	}

	private void clearAppData() {
		try {
			// clearing app data
			if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
				((ActivityManager)getActivity().getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
			} else {
				String packageName = getActivity().getApplicationContext().getPackageName();
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("pm clear "+packageName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void restartApp() {
		Intent intent = new Intent(getActivity().getApplicationContext(), Login_Activity.class);
		int mPendingIntentId = 2;
		PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager mgr = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
		System.exit(0);
	}



}