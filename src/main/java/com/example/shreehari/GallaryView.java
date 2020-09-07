package com.example.shreehari;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shreehari.UserSession.UserSession;

public class GallaryView extends AppCompatActivity {

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            data = extras.getString("Img_url"); // re
        }

        ImageView imageView = findViewById(R.id.img);
        Glide.with(this).load(data).placeholder(R.drawable.profile).into(imageView);


    }
}