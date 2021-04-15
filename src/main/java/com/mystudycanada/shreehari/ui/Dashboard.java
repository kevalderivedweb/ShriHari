package com.mystudycanada.shreehari.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mystudycanada.shreehari.API.GetDashboardRequest;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.Adapter.ExamDashboardAdapter;
import com.mystudycanada.shreehari.Adapter.ImageAdapter;
import com.mystudycanada.shreehari.Adapter.ScheduleDashboardAdapter;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.ExamDashBoardModel;
import com.mystudycanada.shreehari.Model.LeaveDashBoardModel;
import com.mystudycanada.shreehari.Model.ScheduleDashBoardModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends Fragment {
    // Store instance variablesversionDetails
    private String title;
    private int page;
    private View pd1;
    private RequestQueue requestQueue;
    private UserSession session;
    private ArrayList<ScheduleDashBoardModel> mDataset_Schedule = new ArrayList<>();
    private ArrayList<LeaveDashBoardModel> mDataset_Leave = new ArrayList<>();
    private ArrayList<ExamDashBoardModel> mDataset_Exam = new ArrayList<>();
    private RecyclerView recyleview;
    private ScheduleDashboardAdapter mAdapter;
    private RecyclerView recyleview_exam;
    private ExamDashboardAdapter mAdapter_Exam;
    private ImageView four, three, two, one, add_btn, circle;

    private boolean isShow = false;
    private TabLayout indicator;
    private ImageAdapter adapterView;
    private TextView txt_present, txt_absent, txt_leave;
    private LinearLayout mDashBorad_Schedule, mDashBorad_Exam, mDashBorad_Leave, mDashBorad_Attandace;
    private String VersionDetails;
    private LinearLayout attandacne;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

        session = new UserSession(getActivity());
        Log.e("Notification", session.getFirebaseToken());
        mDashBorad_Schedule = (LinearLayout) view.findViewById(R.id.schedule);
        mDashBorad_Exam = (LinearLayout) view.findViewById(R.id.exam);
        mDashBorad_Leave = (LinearLayout) view.findViewById(R.id.leave);
        mDashBorad_Attandace = (LinearLayout) view.findViewById(R.id.attandace);
        attandacne = (LinearLayout) view.findViewById(R.id.attandacne);

        recyleview = view.findViewById(R.id.recyleview_schedule);
        add_btn = view.findViewById(R.id.add_btn);
        circle = view.findViewById(R.id.circle);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        txt_present = view.findViewById(R.id.txt_present);
        txt_absent = view.findViewById(R.id.txt_absent);
        txt_leave = view.findViewById(R.id.txt_leave);

        circle.setVisibility(View.GONE);
        one.setVisibility(View.GONE);
        three.setVisibility(View.GONE);
        two.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
       // checkPermission();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.getUserType().equals("admin")) {
                    replaceFragment(R.id.nav_host_fragment, new StudentSearch(), "Fragment", null);
                } else {
                    replaceFragment(R.id.nav_host_fragment, new Profile(), "Fragment", null);
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Schedule(), "Fragment", null);

            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (session.getUserType().equals("admin")) {
                    replaceFragment(R.id.nav_host_fragment, new Attendance(), "Fragment", null);
                } else {
                    replaceFragment(R.id.nav_host_fragment, new StudentAttendance(), "Fragment", null);
                }


            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Announcements(), "Fragment", null);

            }
        });
        attandacne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getUserType().equals("admin")){
                    replaceFragment(R.id.nav_host_fragment, new Attendance(), "Fragment", null);

                }else {
                    replaceFragment(R.id.nav_host_fragment, new StudentAttendance(), "Fragment", null);

                }

            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow) {
                    isShow = false;
                    circle.setVisibility(View.GONE);
                    one.setVisibility(View.GONE);
                    three.setVisibility(View.GONE);
                    two.setVisibility(View.GONE);
                    four.setVisibility(View.GONE);
                } else {
                    isShow = true;
                    circle.setVisibility(View.VISIBLE);
                    one.setVisibility(View.VISIBLE);
                    three.setVisibility(View.VISIBLE);
                    two.setVisibility(View.VISIBLE);
                    four.setVisibility(View.VISIBLE);
                }

            }
        });
        recyleview.setHasFixedSize(true);
        recyleview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ScheduleDashboardAdapter(mDataset_Schedule, new ScheduleDashboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                replaceFragment(R.id.nav_host_fragment, new Schedule(), "Fragment", null);


            }
        });
        recyleview.setAdapter(mAdapter);

        recyleview_exam = view.findViewById(R.id.recyleview_exam);
        recyleview_exam.setHasFixedSize(true);
        recyleview_exam.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter_Exam = new ExamDashboardAdapter(mDataset_Exam, new ExamDashboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {


                replaceFragment(R.id.nav_host_fragment, new Exam(), "Fragment", null);


            }
        });
        recyleview_exam.setAdapter(mAdapter_Exam);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        indicator = (TabLayout) view.findViewById(R.id.indicator);
        adapterView = new ImageAdapter(getActivity(), mDataset_Leave, new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {
                replaceFragment(R.id.nav_host_fragment, new Leave(), "Fragment", null);

            }
        });
        mViewPager.setAdapter(adapterView);
        indicator.setupWithViewPager(mViewPager, true);


        LinearLayout batchlayout = view.findViewById(R.id.batchlayout);
        TextView batchtxt = view.findViewById(R.id.batchtxt);
        TextView leveltxt = view.findViewById(R.id.leveltxt);
        if(!session.getUserType().equals("admin")){
            batchlayout.setVisibility(View.VISIBLE);
            batchtxt.setText("Level : " + session.getSlevel());
            leveltxt.setText("Batch : " + session.getSBatch());
        }else {
            batchlayout.setVisibility(View.GONE);
        }

        GetDashboard();
        return view;
    }


    private void GetDashboard() {


        final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        GetDashboardRequest loginRequest = new GetDashboardRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response1", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");


                    VersionDetails = jsonObject1.getString("android_app_version");

                    if (!ServerUtils.CurrentVersionDetails.equals(VersionDetails)) {
                        UpdateDialog(VersionDetails);
                    }

                    txt_present.setText(jsonObject1.getString("total_presence"));
                    txt_absent.setText(jsonObject1.getString("total_absent"));
                    txt_leave.setText(jsonObject1.getString("no_of_leave"));


                    JSONArray jsonArray = jsonObject1.getJSONArray("schedule");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ScheduleDashBoardModel announcementModel = new ScheduleDashBoardModel();
                        announcementModel.setMobile_schedule_id(object.getString("mobile_schedule_id"));
                        announcementModel.setSubject(String.valueOf(object.getString("subject")));
                        announcementModel.setClass_room(object.getString("class_room"));
                        announcementModel.setDate(object.getString("date"));
                        announcementModel.setStart_time(object.getString("start_time"));
                        announcementModel.setEnd_time(object.getString("end_time"));
                        announcementModel.setBatch(object.getString("batch"));
                        announcementModel.setStandard(object.getString("standard"));
                        mDataset_Schedule.add(announcementModel);


                    }

                    JSONArray jsonArray_pending_leave = jsonObject1.getJSONArray("pending_leave");

                    for (int i = 0; i < jsonArray_pending_leave.length(); i++) {
                        JSONObject object = jsonArray_pending_leave.getJSONObject(i);
                        LeaveDashBoardModel announcementModel = new LeaveDashBoardModel();
                        announcementModel.setCoachId(object.getString("coachingbreak_id"));
                        announcementModel.setBreakFrom(String.valueOf(object.getString("break_from")));
                        announcementModel.setBreakTo(object.getString("break_to"));
                        announcementModel.setDays(object.getString("no_of_days"));
                        announcementModel.setStatus(object.getString("leave_status"));
                        announcementModel.setUserId(object.getString("mobile_user_master_id"));
                        announcementModel.setFirstName(object.getString("first_name"));
                        announcementModel.setLastName(object.getString("last_name"));
                        announcementModel.setRegisterNumber(object.getString("coaching_reg_no"));
                        mDataset_Leave.add(announcementModel);

                    }

                    JSONArray jsonArray_upcoming_exam = jsonObject1.getJSONArray("upcoming_exam");

                    for (int i = 0; i < jsonArray_upcoming_exam.length(); i++) {
                        JSONObject object = jsonArray_upcoming_exam.getJSONObject(i);
                        ExamDashBoardModel announcementModel = new ExamDashBoardModel();
                        announcementModel.setTestName(object.getString("mock_test_name"));
                        announcementModel.setDate(object.getString("date"));
                        announcementModel.setTestTime(object.getString("mock_test_time"));
                        announcementModel.setBatch(object.getString("batch"));
                        announcementModel.setStandard(object.getString("standard"));
                        mDataset_Exam.add(announcementModel);

                    }

                    mAdapter.notifyDataSetChanged();
                    mAdapter_Exam.notifyDataSetChanged();
                    adapterView.notifyDataSetChanged();

                    if (session.getUserType().equals("admin")) {
                        mDashBorad_Attandace.setVisibility(View.GONE);
                        mDashBorad_Schedule.setVisibility(View.VISIBLE);
                        mDashBorad_Exam.setVisibility(View.VISIBLE);
                        mDashBorad_Leave.setVisibility(View.VISIBLE);
                    } else {
                        mDashBorad_Attandace.setVisibility(View.VISIBLE);
                        mDashBorad_Schedule.setVisibility(View.VISIBLE);
                        mDashBorad_Exam.setVisibility(View.VISIBLE);
                        mDashBorad_Leave.setVisibility(View.GONE);
                    }

                    if (mDataset_Schedule.isEmpty()) {
                        mDashBorad_Schedule.setVisibility(View.GONE);
                    }
                    if (mDataset_Leave.isEmpty()) {
                        mDashBorad_Leave.setVisibility(View.GONE);
                    }
                    if (mDataset_Exam.isEmpty()) {
                        mDashBorad_Exam.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {

                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPIToken());
                return params;
            }
        };
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

    private void UpdateDialog(String versionDetails) {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.setContentView(R.layout.update_dialog);
        dialog1.setCancelable(false);


        TextView version_name = dialog1.findViewById(R.id.version_name);
        version_name.setText("V" + versionDetails);
        dialog1.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mystudycanada.shreehari"));
                startActivity(intent);
            }
        });

        dialog1.show();
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