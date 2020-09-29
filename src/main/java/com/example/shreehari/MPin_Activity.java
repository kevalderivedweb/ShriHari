package com.example.shreehari;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.shreehari.API.CheckMPINlRequest;
import com.example.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MPin_Activity extends AppCompatActivity {

    private EditText et1,et2,et3,et4;
    private RequestQueue requestQueue;
    private UserSession session;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mpin);

        requestQueue = Volley.newRequestQueue(MPin_Activity.this);//Creating the RequestQueue
        final String Email = getIntent().getStringExtra("Email");
        session = new UserSession(getApplicationContext());
        et1 = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        TextView textView = findViewById(R.id.forgot);



        UserSession userSession = new UserSession(getApplicationContext());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MPin_Activity.this, SetMPin_Activity.class);
                intent.putExtra("profile_pic",userSession.getProfile());
                startActivity(intent);
                finish();
            }
        });

        name = findViewById(R.id.name);

        name.setText(userSession.getName()+" "+userSession.getLastName() );

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString().isEmpty()){
                    Toast.makeText(MPin_Activity.this,"Please enter mPin",Toast.LENGTH_SHORT).show();

                }else if(et2.getText().toString().isEmpty()){
                    Toast.makeText(MPin_Activity.this,"Please enter mPin",Toast.LENGTH_SHORT).show();

                }else if(et3.getText().toString().isEmpty()){
                    Toast.makeText(MPin_Activity.this,"Please enter mPin",Toast.LENGTH_SHORT).show();

                }else if(et4.getText().toString().isEmpty()){
                    Toast.makeText(MPin_Activity.this,"Please enter mPin",Toast.LENGTH_SHORT).show();

                }else {

                    String Password = et1.getText().toString()
                            + et2.getText().toString()
                            + et3.getText().toString()
                            + et4.getText().toString();
                    Check_MPIN(userSession.getFirebaseToken(), Password);
                }
            }
        });

        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));
        CircleImageView imageView1 = (CircleImageView) findViewById(R.id.profile_image2);
        Glide.with(this).load(userSession.getProfile()).into(imageView1);


    }
    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.editText1:
                    if(text.length()==1)
                        et2.requestFocus();
                    break;
                case R.id.editText2:
                    if(text.length()==1)
                        et3.requestFocus();
                    else if(text.length()==0)
                        et1.requestFocus();
                    break;
                case R.id.editText3:
                    if(text.length()==1)
                        et4.requestFocus();
                    else if(text.length()==0)
                        et2.requestFocus();
                    break;
                case R.id.editText4:
                    if(text.length()==0)
                        et3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
    public void Check_MPIN(final String device_token,final  String mPin) {

        final KProgressHUD progressDialog = KProgressHUD.create(MPin_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();



        CheckMPINlRequest loginRequest = new CheckMPINlRequest(device_token,mPin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                   // Toast.makeText(MPin_Activity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("ResponseCode")==200) {

                        JSONObject object = jsonObject.getJSONObject("data");
                        String mobile_user_master_id = String.valueOf(object.getInt("mobile_user_master_id"));
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        String profile_pic = object.getString("profile_pic");
                        String mobile_no = object.getString("mobile_no");
                        String email = object.getString("email");
                        String user_type = object.getString("user_type");
                        String coaching_student_id = String.valueOf(object.getInt("coaching_student_id"));
                        String branch_id = String.valueOf(object.getInt("branch_id"));
                        String parent_id = String.valueOf(object.getInt("parent_id"));
                        String api_token = object.getString("api_token");
                        String coaching_reg_no = "4/20";
                       // String coaching_reg_no = object.getString("coaching_reg_no");
                      //  String registered_date = object.getString("registered_date");
                        String registered_date = "24 August,2020";

                        session.createLoginSession(mobile_user_master_id,
                                first_name
                                ,last_name
                                ,profile_pic
                                ,mobile_no
                                ,email
                                ,user_type
                                ,coaching_student_id
                                ,branch_id
                                ,parent_id
                                ,api_token);

                        Intent intent=new Intent(MPin_Activity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                    }else {
                        Toast.makeText(MPin_Activity.this,"Please enter valid mPin",Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(MPin_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(MPin_Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(MPin_Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
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

}