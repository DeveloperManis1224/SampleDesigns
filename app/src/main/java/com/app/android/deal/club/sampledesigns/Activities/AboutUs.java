package com.app.android.deal.club.sampledesigns.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.bumptech.glide.Glide;

public class AboutUs extends AppCompatActivity {

    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        imgView = findViewById(R.id.about_gif);

        Glide.with(AboutUs.this)
                .load(getResources().getDrawable(R.drawable.about_us_gif))
                .into(imgView);
    }
}
