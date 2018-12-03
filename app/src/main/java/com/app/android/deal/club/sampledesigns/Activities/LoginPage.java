package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.android.deal.club.sampledesigns.R;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(LoginPage.this,HomeActivity.class));
    }

    public void onRegClick(View view) {
        startActivity(new Intent(LoginPage.this,RegisterPage.class));
    }
}
