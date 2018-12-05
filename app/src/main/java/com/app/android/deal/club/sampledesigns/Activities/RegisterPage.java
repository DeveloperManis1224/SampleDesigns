package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {
    private TextInputEditText mName,mPhone,mCompanyName, mEmail, mAddress,mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Init();
    }

    private void Init()
    {
        mName = findViewById(R.id.reg_edt_name);
        mPhone = findViewById(R.id.reg_edt_phone);
        mCompanyName = findViewById(R.id.reg_edt_company_name);
        mEmail = findViewById(R.id.reg_edt_email);
        mAddress = findViewById(R.id.reg_edt_address);
        mPassword = findViewById(R.id.reg_password);
    }
    private boolean isValid()
    {
        boolean val = true;
        if(mName.getText().toString().isEmpty())
        {
            val = false;
            mName.setError("Invalid Name");
        }
        if(mEmail.getText().toString().isEmpty())
        {
            val = false;
            mEmail.setError("Invalid Email");
        }
        if(mPhone.getText().toString().isEmpty())
        {
            val = false;
            mPhone.setError("Invalid Phone");
        }
        if(mCompanyName.getText().toString().isEmpty())
        {
            val = false;
            mCompanyName.setError("Invalid Company Name");
        }
        if(mAddress.getText().toString().isEmpty())
        {
            val = false;
            mAddress.setError("Invalid Address");
        }
        if(mPassword.getText().toString().isEmpty())
        {
            val = false;
            mPassword.setError("Invalid Password");
        }
        return  val;
    }
    public void onRegisterClick(View view)
    {
        if(isValid())
        {
            addUserData();
        }
    }
    private void addUserData()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-REGISTER",""+response);
                        Toast.makeText(RegisterPage.this, ""+response, Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(RegisterPage.this,HomeActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterPage.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mName.getText().toString().trim());
                params.put("email", mEmail.getText().toString().trim());
                params.put("phone", mPhone.getText().toString().trim());
                params.put("company_name", mCompanyName.getText().toString().trim());
                params.put("address", mAddress.getText().toString().trim());
                params.put("password", mPassword.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
