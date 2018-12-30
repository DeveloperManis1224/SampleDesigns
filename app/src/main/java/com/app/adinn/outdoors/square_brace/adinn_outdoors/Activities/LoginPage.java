package com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.R;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.SplashScreen;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.Constants;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.SessionManager;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {
    ProgressDialog progressDialog;
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
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
        if(Constants.isOnline(LoginPage.this))
        {
            if(isValid())
            {
                progressDialog.show();
                checkLoginUser();
            }
        }
        else
        {
            new AwesomeSuccessDialog(LoginPage.this)
                    .setTitle("No Internet Available!")
                    .setMessage("There is no internet connection.please turn on your internet connection.")
                    .setColoredCircle(R.color.colorAccent)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText("Refresh")
                    .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                    .setPositiveButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            Intent in = new Intent(LoginPage.this, SplashScreen.class);
                            startActivity(in);
                            finish();
                        }
                    })
                    .show();
        }

    }

    private void checkLoginUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
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
                progressDialog.dismiss();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void onRegClick(View view) {
        startActivity(new Intent(LoginPage.this,RegisterPage.class));
    }
}
