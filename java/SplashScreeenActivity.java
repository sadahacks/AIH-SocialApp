package com.example.smartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreeenActivity extends AppCompatActivity {

    public static int SPLASH_TIME_OUT =1000;

    SharedPreferences sharedPreferences;

    TextView splashScreen;

    public static final String filename="MainInterface";
    public static final String SplashScreen="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screeen);

        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i=new Intent(SplashScreeenActivity.this,MainInterface.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);*/
    }
}
