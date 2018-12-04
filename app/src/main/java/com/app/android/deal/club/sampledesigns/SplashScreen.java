package com.app.android.deal.club.sampledesigns;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.android.deal.club.sampledesigns.Activities.HomeActivity;
import com.app.android.deal.club.sampledesigns.Activities.LoginPage;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, HomeActivity.class));
            }
        },3000);
    }
}
