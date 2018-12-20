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
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    TextInputEditText mUsername, mPassword;
    SessionManager session;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        init();
    }

    private void init() {
        mUsername = findViewById(R.id.edt_username);
        mPassword = findViewById(R.id.edt_password);
        session = new SessionManager();
    }

    private boolean isValid()
    {
        boolean val = true;
        if(mUsername.getText().toString().isEmpty() ||
                !mUsername.getText().toString().matches(emailPattern) )
        {
            val = false;
            mUsername.setError("Invalid Username");
        }
        if(mPassword.getText().toString().isEmpty())
        {
            val = false;
            mPassword.setError("Invalid Password");
        }
        return  val;
    }

    public void onLoginClick(View view) {
        if(isValid())
        {
            checkLoginUser();
        }
    }

    private void checkLoginUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/login";
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
                                JSONObject resObject = jsonObject.getJSONObject("user");
                                session.setPreferences(LoginPage.this,Constants.LOGIN_STATUS,Constants.LOGIN);
                                session.setPreferences(LoginPage.this,Constants.CURRENT_USER_ID,resObject.getString("id"));
                                session.setPreferences(LoginPage.this,Constants.CURRENT_USER_NAME,resObject.getString("name"));
                                session.setPreferences(LoginPage.this,Constants.CURRENT_USER_EMAIL,resObject.getString("email"));
                                session.setPreferences(LoginPage.this,Constants.CURRENT_USER_PHONE,resObject.getString("phone"));
                                session.setPreferences(LoginPage.this,Constants.USER_COMAPNY_NAME,resObject.getString("company_name"));
                                session.setPreferences(LoginPage.this,Constants.USER_ADDRESS,resObject.getString("address"));
                                startActivity(new Intent(LoginPage.this,HomeActivity.class));
                                Toast.makeText(LoginPage.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(LoginPage.this, ""+stsMessage,
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
                Toast.makeText(LoginPage.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mUsername.getText().toString().trim());
                params.put("password", mPassword.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void onRegClick(View view) {
        startActivity(new Intent(LoginPage.this,RegisterPage.class));
    }
}
