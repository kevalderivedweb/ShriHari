package com.example.shreehari;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.shreehari.API.CheckEmailRequest;
import com.example.shreehari.UserSession.UserSession;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class Login_Activity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private UserSession userSession;

    /*
        qwertyuiop@1234567890
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        try {
            InstanceID instanceID = InstanceID.getInstance(this);

            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e("Token", "GCM Registration Token: " + token);

        }catch (Exception e) {
            Log.e("Token", "Failed to complete token refresh", e);
        }

        requestQueue = Volley.newRequestQueue(Login_Activity.this);//Creating the RequestQueue


        final EditText etName = findViewById(R.id.etName);


        userSession = new UserSession(getApplicationContext());

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etName.getText().toString().isEmpty()){
                    Toast.makeText(Login_Activity.this,"Please enter your mobile number or email",Toast.LENGTH_SHORT).show();
                }else {
                    Check_Email(etName.getText().toString(),userSession.getFirebaseToken());
                }

            }
        });
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
        });
        loginRequest.setTag("TAG");
        requestQueue.add(loginRequest);

    }

}