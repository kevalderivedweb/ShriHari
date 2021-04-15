package com.mystudycanada.shreehari;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.mystudycanada.shreehari.UserSession.UserSession;

public class SplashScreen extends AppCompatActivity {

    private CountDownTimer countDownTimer;

    UserSession userSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        userSession = new UserSession(getApplicationContext());

        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                if(userSession.isLoggedIn()){
                    Intent intent=new Intent(SplashScreen.this, MPin_Activity.class);
                    if(getIntent().getExtras()!=null) {
                        intent.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }else {
                    Intent intent=new Intent(SplashScreen.this, Login_Activity.class);
                    if(getIntent().getExtras()!=null) {
                        intent.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }



            }
        }.start();

    }
}