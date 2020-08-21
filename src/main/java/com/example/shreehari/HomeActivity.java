package com.example.shreehari;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.shreehari.ui.AddAssignment;
import com.example.shreehari.ui.AddSchedule;
import com.example.shreehari.ui.Announcements;
import com.example.shreehari.ui.Assignment;
import com.example.shreehari.ui.Attendance;
import com.example.shreehari.ui.Connect;
import com.example.shreehari.ui.Dashboard;
import com.example.shreehari.ui.EXTRASCREEN;
import com.example.shreehari.ui.Exam;
import com.example.shreehari.ui.Gallery;
import com.example.shreehari.ui.Leave;
import com.example.shreehari.ui.Notification;
import com.example.shreehari.ui.Profile;
import com.example.shreehari.ui.Result;
import com.example.shreehari.ui.Schedule;
import com.example.shreehari.ui.StudentSearch;
import com.example.shreehari.ui.Visa;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView drawer_menu;
    private LinearLayout ln_assignment,ln_assignment1,ln_schedule,ln_schedule_2,ln_announcements;
    private LinearLayout ln_attendance,ln_exam,ln_notification,ln_result;
    private LinearLayout ln_gallery;
    private LinearLayout ln_leave;
    private LinearLayout ln_profile;
    private LinearLayout ln_student;
    private LinearLayout ln_visa;
    private LinearLayout ln_extra;
    private LinearLayout ln_dashboard;
    private LinearLayout ln_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkPermission();
        drawer_menu = findViewById(R.id.menu);
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
        ln_attendance = findViewById(R.id.ln_attendance);
        ln_assignment1 = findViewById(R.id.ln_assignment1);
        ln_gallery = findViewById(R.id.ln_gallery);
        ln_extra = findViewById(R.id.ln_extra);
        ln_leave = findViewById(R.id.ln_leave);
        ln_exam = findViewById(R.id.ln_exam);
        ln_connect = findViewById(R.id.ln_connect);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        replaceFragment(R.id.nav_host_fragment,new Dashboard(),"Fragment",null);

        ln_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Dashboard(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);

            }
        });

        ln_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Connect(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);

            }
        });

        ln_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new AddAssignment(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_assignment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Assignment(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        ln_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Gallery(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Result(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Notification(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Leave(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Profile(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Exam(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Attendance(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new EXTRASCREEN(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        ln_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new AddSchedule(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_schedule_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Schedule(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new StudentSearch(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ln_announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Announcements(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        ln_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.nav_host_fragment,new Visa(),"Fragment",null);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

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
                .addToBackStack(backStackStateName)
                .commit();
  /*      hello*/

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }



}