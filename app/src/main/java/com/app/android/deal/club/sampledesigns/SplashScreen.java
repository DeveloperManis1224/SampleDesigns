package com.app.android.deal.club.sampledesigns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                if (!prefs.getBoolean("firstTime", false)) {
                    DBHelper db = new DBHelper(SplashScreen.this);
                    startActivity(new Intent(SplashScreen.this, LoginPage.class));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();
                }
                else
                {
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                }

            }
        },3000);
    }
}
