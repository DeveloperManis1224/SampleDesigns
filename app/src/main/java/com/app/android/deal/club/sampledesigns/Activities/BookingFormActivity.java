package com.app.android.deal.club.sampledesigns.Activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.app.android.deal.club.sampledesigns.R;

import java.util.ArrayList;
import java.util.List;

public class BookingFormActivity extends AppCompatActivity {
    private TextInputEditText mFname , mLname, mCompanyName, mAddress, mPostCode,mPhone, mEmail,mNotes;
    List<String> cartIdList = new ArrayList<String>();
    Spinner mCountry, mState, mCity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);
        mFname = findViewById(R.id.edt_f_name);
        mLname = findViewById(R.id.edt_l_name);
        mCompanyName = findViewById(R.id.edt_company_name);
        mAddress = findViewById(R.id.edt_address);
        mPhone = findViewById(R.id.edt_phone);
        mEmail = findViewById(R.id.edt_email);
        mNotes = findViewById(R.id.edt_notes);
        mCountry = findViewById(R.id.spin_country);
        mState = findViewById(R.id.spin_state);
        mCity = findViewById(R.id.spin_city);
    }

    public void onBookClick(View v)
    {

    }
     private boolean isValid()
     {
         boolean val = true;
         if(mFname.getText().toString().isEmpty())
         {
             mFname.setError("Invalid");
         }
         if(mLname.getText().toString().isEmpty())
         {
             mLname.setError("Invalid");
         }
         if(mCompanyName.getText().toString().isEmpty())
         {
             mCompanyName.setError("Invalid");
         }
         if(mAddress.getText().toString().isEmpty())
         {
             mAddress.setError("Invalid");
         }
         if(mFname.getText().toString().isEmpty())
         {
             mFname.setError("Invalid");
         }
         if(mFname.getText().toString().isEmpty())
         {
             mFname.setError("Invalid");
         }
         if(mFname.getText().toString().isEmpty())
         {
             mFname.setError("Invalid");
         }

         return  val;
     }
}
