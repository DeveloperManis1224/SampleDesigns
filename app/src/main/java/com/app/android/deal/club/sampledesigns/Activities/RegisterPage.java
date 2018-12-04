package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.android.deal.club.sampledesigns.R;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }


    public void onRegisterClick(View view) {
        startActivity(new Intent(RegisterPage.this,HomeActivity.class));
    }
}
