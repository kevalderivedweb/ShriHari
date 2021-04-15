package com.mystudycanada.shreehari;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mystudycanada.shreehari.API.CheckEmailRequest;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.mystudycanada.shreehari.UserSession.UserSessionFirebase;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private UserSession userSession;
    private String msg;
    private String token;

    /*
        qwertyuiop@1234567890
    */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        try {
            InstanceID instanceID = InstanceID.getInstance(this);

            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e("Token", "GCM Registration Token: " + token);

        } catch (Exception e) {
            Log.e("Token", "Failed to complete token refresh", e);
        }

        requestQueue = Volley.newRequestQueue(Login_Activity.this);//Creating the RequestQueue


        final EditText etName = findViewById(R.id.etName);


        userSession = new UserSession(getApplicationContext());

        userSession.logout();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//To do//
                            return;
                        }

// Get the Instance ID token//
                        token = task.getResult().getToken();
                        msg = getString(R.string.fcm_token, token);
                        userSession.FirebaseToken(token);
                        Log.e("Notification", token);

                    }
                });


        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(Login_Activity.this, "Please enter your mobile number or email", Toast.LENGTH_SHORT).show();
                } else {
                    Check_Email(etName.getText().toString(), token);
                }

            }
        });

      /*  VideoView videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(Login_Activity.this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        Uri uri = Uri.parse("https://pagalstatus.com/wp-content/uploads/2021/02/Bhala-Pae-Tote-Mu-Emiti-New-Odia-4k-Hd-Trending-Fullscreen-Status-Video-video-status-market.mp4");

        // Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setMediaController(null);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); //need to make transition seamless.
            }
        });
    }*/

    }

    public void Check_Email(final String Email,final  String device_token) {

        final KProgressHUD progressDialog = KProgressHUD.create(Login_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        CheckEmailRequest loginRequest = new CheckEmailRequest(Email, device_token,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(Login_Activity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("ResponseCode")==200) {


                        userSession.setRegistrationDate(jsonObject.getJSONObject("data").getString("registered_date"));
                        userSession.setRegistrationNumber(jsonObject.getJSONObject("data").getString("coaching_reg_no"));

                        userSession.setSlevel(jsonObject.getJSONObject("data").getString("coachinglevel"));
                        userSession.setSBatch(jsonObject.getJSONObject("data").getString("batch"));
                        if(jsonObject.getJSONObject("data").getInt("is_exists_mpin")==0){
                            Intent intent=new Intent(Login_Activity.this, OTP_Activity.class);
                            intent.putExtra("Email",Email);
                            intent.putExtra("Name",jsonObject.getJSONObject("data").getString("first_name"));
                            intent.putExtra("LastName",jsonObject.getJSONObject("data").getString("last_name"));
                            intent.putExtra("profile_pic",jsonObject.getJSONObject("data").getString("profile_pic"));
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                            finish();
                        }else {
                            Intent intent=new Intent(Login_Activity.this, SetMPin_Activity.class);
                            intent.putExtra("Name",jsonObject.getJSONObject("data").getString("first_name"));
                            intent.putExtra("LastName",jsonObject.getJSONObject("data").getString("last_name"));
                            intent.putExtra("profile_pic",jsonObject.getJSONObject("data").getString("profile_pic"));
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                            finish();
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
                    Toast.makeText(Login_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(Login_Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(Login_Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            return params;
        }};
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);
        requestQueue.add(loginRequest);

    }

}