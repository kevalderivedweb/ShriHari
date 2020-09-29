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
import androidx.collection.ArraySet;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.example.shreehari.API.AddAttandanceRequest;
import com.example.shreehari.API.AndroidMultiPartEntity;
import com.example.shreehari.API.GetAttandanceRequest;
import com.example.shreehari.API.GetBatchRequest;
import com.example.shreehari.API.GetExamRequest;
import com.example.shreehari.API.GetStandardRequest;
import com.example.shreehari.API.GetStudentAttandanceRequest;
import com.example.shreehari.Adapter.AttandanceAdapter;
import com.example.shreehari.Adapter.ExamAdapter;
import com.example.shreehari.Adapter.SpinAdapter;
import com.example.shreehari.Adapter.SpinAdapter2;
import com.example.shreehari.Adapter.SpinAdapterYEAR;
import com.example.shreehari.Adapter.StudentAttandanceAdapter;
import com.example.shreehari.Model.AttandanceModel;
import com.example.shreehari.Model.AttandanceStudentModel;
import com.example.shreehari.Model.BatchModel;
import com.example.shreehari.Model.ExamModel;
import com.example.shreehari.Model.YearModel;
import com.example.shreehari.R;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentAttendance extends Fragment {

    private RequestQueue requestQueue;
    private UserSession session;
    private ArrayList<YearModel> mDatasetYear = new ArrayList<>();
    private ArrayList<YearModel> mDatasetMonth = new ArrayList<>();
    private Spinner year, month;
    private int finalYear = 0;
    private int MONTH_POS = 0, YEAR_POS = 0;
    private TextView txt_present, txt_absent, txt_leave;
	private ArrayList<AttandanceStudentModel> mDataset = new ArrayList<>();
	private RecyclerView recyleview;
	private LinearLayoutManager linearlayout;
	private StudentAttandanceAdapter mAdapter;

	// Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_attendance, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue
        session = new UserSession(getActivity());

        txt_present = view.findViewById(R.id.txt_present);
        txt_absent = view.findViewById(R.id.txt_absent);
        txt_leave = view.findViewById(R.id.txt_leave);
        year = view.findViewById(R.id.year);
        month = view.findViewById(R.id.month);
        int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

        finalYear = CURRENT_YEAR;

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                YearModel BatchModel = new YearModel();
                BatchModel.setId("" + CURRENT_YEAR);
                BatchModel.setYearname("" + CURRENT_YEAR);
                mDatasetYear.add(BatchModel);
            } else {
                finalYear++;
                YearModel BatchModel = new YearModel();
                BatchModel.setId("" + finalYear);
                BatchModel.setYearname("" + finalYear);
                mDatasetYear.add(BatchModel);
            }
        }
        YearModel BatchModel = new YearModel();
        BatchModel.setId("Please select Year");
        BatchModel.setYearname("Please select Year");
        mDatasetYear.add(BatchModel);
        SpinAdapterYEAR adapter = new SpinAdapterYEAR(getActivity(),
                android.R.layout.simple_spinner_item,
                mDatasetYear);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        year.setAdapter(adapter);
        year.setSelection(adapter.getCount());


        YearModel MonthJAN = new YearModel();
        MonthJAN.setId("01");
        MonthJAN.setYearname("January");
        mDatasetMonth.add(MonthJAN);

        YearModel MonthFeb = new YearModel();
        MonthFeb.setId("02");
        MonthFeb.setYearname("February");
        mDatasetMonth.add(MonthFeb);

        YearModel MonthMARCH = new YearModel();
        MonthMARCH.setId("03");
        MonthMARCH.setYearname("March");
        mDatasetMonth.add(MonthMARCH);

        YearModel MonthAPRIL = new YearModel();
        MonthAPRIL.setId("04");
        MonthAPRIL.setYearname("April");
        mDatasetMonth.add(MonthAPRIL);

        YearModel MonthMAY = new YearModel();
        MonthMAY.setId("05");
        MonthMAY.setYearname("May");
        mDatasetMonth.add(MonthMAY);

        YearModel MonthJUNE = new YearModel();
        MonthJUNE.setId("06");
        MonthJUNE.setYearname("June");
        mDatasetMonth.add(MonthJUNE);

        YearModel MonthJULY = new YearModel();
        MonthJULY.setId("07");
        MonthJULY.setYearname("July");
        mDatasetMonth.add(MonthJULY);

        YearModel MonthAUGUST = new YearModel();
        MonthAUGUST.setId("08");
        MonthAUGUST.setYearname("August");
        mDatasetMonth.add(MonthAUGUST);

        YearModel MonthSEPTERMBER = new YearModel();
        MonthSEPTERMBER.setId("09");
        MonthSEPTERMBER.setYearname("September");
        mDatasetMonth.add(MonthSEPTERMBER);

        YearModel MonthOCTOBER = new YearModel();
        MonthOCTOBER.setId("10");
        MonthOCTOBER.setYearname("October");
        mDatasetMonth.add(MonthOCTOBER);

        YearModel MonthNOVEMBER = new YearModel();
        MonthNOVEMBER.setId("11");
        MonthNOVEMBER.setYearname("November");
        mDatasetMonth.add(MonthNOVEMBER);

        YearModel MonthDECEMBER = new YearModel();
        MonthDECEMBER.setId("12");
        MonthDECEMBER.setYearname("December");
        mDatasetMonth.add(MonthDECEMBER);

        YearModel PleaseSelecteMonth = new YearModel();
        PleaseSelecteMonth.setId("Please Select Month");
        PleaseSelecteMonth.setYearname("Please Select Month");
        mDatasetMonth.add(PleaseSelecteMonth);


        SpinAdapterYEAR adaptermonth = new SpinAdapterYEAR(getActivity(),
                android.R.layout.simple_spinner_item,
                mDatasetMonth);
        adaptermonth.setDropDownViewResource(R.layout.spinner_item);
        month.setAdapter(adaptermonth);
        month.setSelection(adaptermonth.getCount());


		recyleview = view.findViewById(R.id.recyleview);
		recyleview.setHasFixedSize(true);
		linearlayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyleview.setLayoutManager(linearlayout);
		mAdapter = new StudentAttandanceAdapter(mDataset, new StudentAttandanceAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

			}
		});
		recyleview.setAdapter(mAdapter);



		year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                YEAR_POS = i;

                if(YEAR_POS!=mDatasetYear.size()-1){
					GetStudnetAttandance(mDatasetMonth.get(MONTH_POS).getId(),mDatasetYear.get(YEAR_POS).getYearname());

				}

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MONTH_POS = i;
				if(MONTH_POS!=mDatasetMonth.size()-1){
					GetStudnetAttandance(mDatasetMonth.get(MONTH_POS).getId(),mDatasetYear.get(YEAR_POS).getYearname());

				}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }


    private void GetStudnetAttandance(String month, String year) {

    	if(month.equals("Please Select Month")){
    		return;
		}else if(year.equals("Please select Year")){
    		return;
		}

    	Log.e("StudentAttandaceData" , month + "---"+year+"---" + session.getAPIToken());

        final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetStudentAttandanceRequest loginRequest = new GetStudentAttandanceRequest(month, year, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

				mDataset.clear();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    txt_absent.setText(""+jsonObject.getInt("absent_cnt"));
                    txt_leave.setText(""+jsonObject.getInt("leave_cnt"));
                    txt_present.setText(""+jsonObject.getInt("presence_cnt"));

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
						AttandanceStudentModel announcementModel = new AttandanceStudentModel();
                        announcementModel.setDate(object.getString("date"));
                        announcementModel.setStatus(object.getString("status"));
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPIToken());
                return params;
            }
        };
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }


}