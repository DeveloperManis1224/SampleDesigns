package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {
    private TextInputEditText mFname,mLname,mEmail,mDescription ;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_btn_bar) {
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ContactUs.this,CartActivity.class));
            return true;
        }
        if (id == R.id.order_btn_bar) {
            startActivity(new Intent(ContactUs.this,OrderDetails.class));
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFname = findViewById(R.id.edt_fname);
        mLname = findViewById(R.id.edt_lname);
        mEmail = findViewById(R.id.edt_email);
        mDescription = findViewById(R.id.edt_desc);
    }
    public void onContactClick(View v)
    {
        if(isValid())
        {
            ContactUsUpload();
        }
    }

    private boolean isValid()
    {
        boolean val = true;
        if(mFname.getText().toString().trim().isEmpty())
        {
            val = false;
            mFname.setError("Invalid");
        }
        if(mLname.getText().toString().trim().isEmpty())
        {
            val = false;
            mLname.setError("Invalid");
        }

        if(mEmail.getText().toString().trim().isEmpty())
        {
            val = false;
            mEmail.setError("Invalid");
        }

        if(mDescription.getText().toString().trim().isEmpty())
        {
            val = false;
            mDescription.setError("Invalid");
        }

        return val;
    }

    private void ContactUsUpload() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/contact";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-LOGIN",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                startActivity(new Intent(ContactUs.this,HomeActivity.class));
                                Toast.makeText(ContactUs.this, ""+stsMessage, Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ContactUs.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE-LOGIN_ERROR",""+error.getMessage());
                Toast.makeText(ContactUs.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", mFname.getText().toString().trim());
                params.put("last_name", mLname.getText().toString().trim());
                params.put("email", mEmail.getText().toString().trim());
                params.put("description", mDescription.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
