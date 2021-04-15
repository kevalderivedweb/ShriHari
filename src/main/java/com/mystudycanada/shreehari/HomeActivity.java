package com.mystudycanada.shreehari;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.mystudycanada.shreehari.ui.AddAssignment;
import com.mystudycanada.shreehari.ui.AddSchedule;
import com.mystudycanada.shreehari.ui.Announcements;
import com.mystudycanada.shreehari.ui.Assignment;
import com.mystudycanada.shreehari.ui.Attendance;
import com.mystudycanada.shreehari.ui.Connect;
import com.mystudycanada.shreehari.ui.Dashboard;
import com.mystudycanada.shreehari.ui.DownloadImages;
import com.mystudycanada.shreehari.ui.EXTRASCREEN;
import com.mystudycanada.shreehari.ui.Exam;
import com.mystudycanada.shreehari.ui.Gallery;
import com.mystudycanada.shreehari.ui.ILTSEXAM;
import com.mystudycanada.shreehari.ui.Leave;
import com.mystudycanada.shreehari.ui.Notification;
import com.mystudycanada.shreehari.ui.Profile;
import com.mystudycanada.shreehari.ui.Result;
import com.mystudycanada.shreehari.ui.ResultExamName;
import com.mystudycanada.shreehari.ui.Schedule;
import com.mystudycanada.shreehari.ui.StudentAttendance;
import com.mystudycanada.shreehari.ui.StudentSearch;
import com.mystudycanada.shreehari.ui.StudentSetting;
import com.mystudycanada.shreehari.ui.Visa;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.RingtoneManager.getDefaultUri;

public class HomeActivity extends AppCompatActivity {


    private static final int NOTIFICATION_ID = 112;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView drawer_menu;
    private LinearLayout ln_assignment, ln_assignment1, ln_schedule, ln_schedule_2, ln_announcements;
    private LinearLayout ln_attendance, ln_exam, ln_notification, ln_result;
    private LinearLayout ln_gallery;
    private LinearLayout ln_leave;
    private LinearLayout ln_profile;
    private LinearLayout ln_student;
    private LinearLayout ln_visa;
    private LinearLayout ln_extra;
    private LinearLayout ln_dashboard;
    private LinearLayout ln_connect;
    private LinearLayout ln_setting;
    private View ln_setting_view;
    private TextView NAME1, NAME2, NAME3, NAME4;
    private TextView reg_no;
    private TextView regdate;
    private View ln_profile_view;
    private View ln_student_view;
    private LinearLayout ln_logout;
    private LinearLayout ln_ilts;
    private NotificationManagerCompat mNotificationManager;
    private String Type;
    private TextView txt_setting;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private LinearLayout ln_downloads;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);


        UserSession userSession = new UserSession(getApplicationContext());
        drawer_menu = findViewById(R.id.menu);
        NAME1 = findViewById(R.id.name1);
        NAME2 = findViewById(R.id.name2);
        NAME3 = findViewById(R.id.name3);
        NAME4 = findViewById(R.id.name_ds);
        reg_no = findViewById(R.id.reg_no);
        regdate = findViewById(R.id.regdate);
        ln_assignment = findViewById(R.id.ln_assignment);
        ln_dashboard = findViewById(R.id.ln_dashboard);
        ln_visa = findViewById(R.id.ln_visa);
        ln_student = findViewById(R.id.ln_student);
        ln_result = findViewById(R.id.ln_result);
        ln_notification = findViewById(R.id.ln_notification);
        ln_schedule = findViewById(R.id.ln_schedule);
        ln_schedule_2 = findViewById(R.id.ln_schedule2);
        ln_announcements = findViewById(R.id.ln_announcements);
        ln_profile = findViewById(R.id.ln_profile);
        ln_downloads = findViewById(R.id.ln_downloads);
        ln_profile_view = findViewById(R.id.ln_profile_view);
        ln_student_view = findViewById(R.id.ln_student_view);
        ln_attendance = findViewById(R.id.ln_attendance);
        ln_assignment1 = findViewById(R.id.ln_assignment1);
        ln_gallery = findViewById(R.id.ln_gallery);
        ln_extra = findViewById(R.id.ln_extra);
        ln_leave = findViewById(R.id.ln_leave);
        ln_exam = findViewById(R.id.ln_exam);
        ln_connect = findViewById(R.id.ln_connect);
        ln_setting = findViewById(R.id.ln_setting);
        ln_ilts = findViewById(R.id.ln_ilts);
        ln_logout = findViewById(R.id.ln_logout);
        ln_setting_view = findViewById(R.id.ln_setting_view);
        txt_setting = findViewById(R.id.txt_setting);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        Log.e("Type", ServerUtils.TYPE);


        if (ServerUtils.TYPE.equalsIgnoreCase("Announcement")) {
            ServerUtils.TYPE = "";
            replaceFragment(R.id.nav_host_fragment, new Announcements(), "Fragment", null);
        } else if (ServerUtils.TYPE.equalsIgnoreCase("Schedule")) {
            ServerUtils.TYPE = "";
            replaceFragment(R.id.nav_host_fragment, new Schedule(), "Fragment", null);
        } else if (ServerUtils.TYPE.equalsIgnoreCase("Result")) {
            ServerUtils.TYPE = "";
            if (userSession.getUserType().equalsIgnoreCase("admin")) {
                replaceFragment(R.id.nav_host_fragment, new Result(), "Fragment", null);
            } else if (userSession.getUserType().equalsIgnoreCase("parent")) {
                ResultExamName fragobj = new ResultExamName();
                Bundle bundle = new Bundle();
                bundle.putString("Id", userSession.getUserId());
                fragobj.setArguments(bundle);
                replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
            } else {
                ResultExamName fragobj = new ResultExamName();
                Bundle bundle = new Bundle();
                bundle.putString("Id", userSession.getUserId());
                fragobj.setArguments(bundle);
                replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
            }
        } else if (ServerUtils.TYPE.equalsIgnoreCase("Attendance")) {
            ServerUtils.TYPE = "";
            if (userSession.getUserType().equalsIgnoreCase("admin")) {
                replaceFragment(R.id.nav_host_fragment, new Attendance(), "Fragment", null);
            } else {
                replaceFragment(R.id.nav_host_fragment, new StudentAttendance(), "Fragment", null);
            }
        } else if (ServerUtils.TYPE.equalsIgnoreCase("Exam")) {
            ServerUtils.TYPE = "";
            replaceFragment(R.id.nav_host_fragment, new Exam(), "Fragment", null);
        } else if (ServerUtils.TYPE.equalsIgnoreCase("Leave")) {
            ServerUtils.TYPE = "";
            replaceFragment(R.id.nav_host_fragment, new Leave(), "Fragment", null);
        } else {
            addFragment(R.id.nav_host_fragment, new Dashboard(), "Fragment");

        }

        ln_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Dashboard(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);

            }
        });
        Log.e("token", userSession.getFirebaseToken());

        ln_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Connect(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);

            }
        });

        ln_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new AddAssignment(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_assignment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Assignment(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        ln_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Gallery(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_ilts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new ILTSEXAM(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new StudentSetting(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userSession.getUserType().equals("admin")) {
                    replaceFragment(R.id.nav_host_fragment, new Result(), "Fragment", null);
                    drawer.closeDrawer(Gravity.LEFT);
                } else if (userSession.getUserType().equals("parent")) {
                    ResultExamName fragobj = new ResultExamName();
                    Bundle bundle = new Bundle();
                    bundle.putString("Id", userSession.getUserId());
                    fragobj.setArguments(bundle);
                    replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    ResultExamName fragobj = new ResultExamName();
                    Bundle bundle = new Bundle();
                    bundle.putString("Id", userSession.getUserId());
                    fragobj.setArguments(bundle);
                    replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
                    drawer.closeDrawer(Gravity.LEFT);
                }

            }
        });

        ln_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Notification(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Leave(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.logout();
                clearAppData();
                restartApp();

           /*     Intent intent = new Intent(HomeActivity.this,Login_Activity.class);
                startActivity(intent);
                finish();*/
            }
        });

        if (userSession.getUserType().equals("parent")) {
            ln_setting.setVisibility(View.VISIBLE);
            ln_setting_view.setVisibility(View.VISIBLE);
        } else {
            ln_setting.setVisibility(View.GONE);
            ln_setting_view.setVisibility(View.GONE);
        }
        if (userSession.getUserType().equals("admin")) {
            ln_profile.setVisibility(View.GONE);
            ln_profile_view.setVisibility(View.GONE);
            ln_logout.setVisibility(View.VISIBLE);
            regdate.setText("You are Login as " + userSession.getUserType());
            reg_no.setVisibility(View.GONE);
        } else {
            ln_student.setVisibility(View.GONE);
            ln_logout.setVisibility(View.GONE);
            ln_student_view.setVisibility(View.GONE);
            regdate.setText("Registered since " + userSession.getRegistrationDate() + "\nYou are Login as " + userSession.getUserType());

        }


        if (userSession.getUserType().equals("parent")) {
            ln_profile.setVisibility(View.GONE);
            ln_profile_view.setVisibility(View.GONE);
            txt_setting.setText("Switch Student");
        }

        ln_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Profile(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        ln_downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

                replaceFragment(R.id.nav_host_fragment, new DownloadImages(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Exam(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userSession.getUserType().equals("admin")) {
                    replaceFragment(R.id.nav_host_fragment, new Attendance(), "Fragment", null);
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    replaceFragment(R.id.nav_host_fragment, new StudentAttendance(), "Fragment", null);
                    drawer.closeDrawer(Gravity.LEFT);
                }

            }
        });

        ln_extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new EXTRASCREEN(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        ln_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new AddSchedule(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_schedule_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Schedule(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new StudentSearch(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Announcements(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        ln_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment, new Visa(), "Fragment", null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        NAME1.setText(userSession.getName() + " " + userSession.getLastName());
        NAME2.setText(userSession.getName() + " " + userSession.getLastName());
        NAME3.setText(userSession.getName() + " " + userSession.getLastName());
        NAME4.setText(userSession.getName() + " " + userSession.getLastName());
        reg_no.setText("Registration No : " + userSession.getRegistrationNumber());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image1);
        CircleImageView imageView1 = (CircleImageView) findViewById(R.id.profile_image2);
        Glide.with(this).load(userSession.getProfile()).into(imageView1);
        Glide.with(this).load(userSession.getProfile()).into(imageView);
        // checkPermission();
    }


    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HomeActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HomeActivity.this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
        int mPendingIntentId = 2;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public void checkPermission(String permission, int requestCode) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                HomeActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            HomeActivity.this,
                            new String[]{permission},
                            requestCode);
        } else {
           /* Toast
                    .makeText(HomeActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();*/
        }
    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(HomeActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();


            } else {
                Toast.makeText(HomeActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();

                File myDirectory = new File(Environment.getRootDirectory(), "ShreeHari");

                if(!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }
            } else {
                Toast.makeText(HomeActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


}