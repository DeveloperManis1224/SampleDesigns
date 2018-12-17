package com.app.android.deal.club.sampledesigns.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingFormActivity extends AppCompatActivity {
    private TextInputEditText mFname , mLname, mCompanyName, mAddress, mPostCode,mPhone, mEmail,mNotes;
    List<String> cartIdList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();
    List<String> cityIdList = new ArrayList<String>();
    List<String> countryList = new ArrayList<String>();
    List<String> stateList = new ArrayList<String>();
    Spinner mCountry, mState, mCity ;
    SessionManager session;
    int pos = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);


        session = new SessionManager();
        mFname = findViewById(R.id.edt_f_name);
        mLname = findViewById(R.id.edt_l_name);
        mCompanyName = findViewById(R.id.edt_company_name);
        mAddress = findViewById(R.id.edt_address);
        mPostCode = findViewById(R.id.edt_post_code);
        mPhone = findViewById(R.id.edt_phone);
        mEmail = findViewById(R.id.edt_email);
        mNotes = findViewById(R.id.edt_notes);
        mCountry = findViewById(R.id.spin_country);
        mState = findViewById(R.id.spin_state);
        mCity = findViewById(R.id.spin_city);
        getCityList();

        countryList.add("India");
        stateList.add("Tamilnadu");
        ArrayAdapter aa = new ArrayAdapter(BookingFormActivity.this,android.R.layout.simple_spinner_dropdown_item,countryList);
        mCountry.setAdapter(aa);
        ArrayAdapter aa1 = new ArrayAdapter(BookingFormActivity.this,android.R.layout.simple_spinner_dropdown_item,stateList);
        mState.setAdapter(aa1);

        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        mCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                pos = i;
//            }
//        });

    }

    public void onBookClick(View v)
    {
        if(isValid())
        {
            bookChecking();
        }
    }

    private void getCityList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/city";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
                                JSONArray jry = jsonObject.getJSONArray("cities");
                                for(int i = 0; i < jry.length(); i++)
                                {
                                    cityList.add(jry.getJSONObject(i).getString("city"));
                                    cityIdList.add(jry.getJSONObject(i).getString("id"));
                                }
                                ArrayAdapter aa = new ArrayAdapter(BookingFormActivity.this,android.R.layout.simple_spinner_dropdown_item,cityList);
                                mCity.setAdapter(aa);
                               // Toast.makeText(BookingFormActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(BookingFormActivity.this, ""+stsMessage,
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
                Toast.makeText(BookingFormActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void bookChecking() {
        Log.e("RESPONSE-HOME_Recent",""+CartActivity.productBuilder.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/order";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-HOME_Recent",""+response+CartActivity.cartIdList.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(BookingFormActivity.this);
                                builder1.setTitle("Thanks for Booking!");
                                builder1.setMessage("Your booking is submitted successfully! we will contact you shortly...");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "Close",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                startActivity(new Intent(BookingFormActivity.this,HomeActivity.class));
                                            }
                                        });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                Toast.makeText(BookingFormActivity.this, "Booking submitted.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(BookingFormActivity.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE-HOME_Recent",""+error.getMessage());
                Toast.makeText(BookingFormActivity.this, "Exception  " + error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",session.getPreferences(BookingFormActivity.this,Constants.CURRENT_USER_ID));
                params.put("first_name",mFname.getText().toString().trim());
                params.put("last_name",mLname.getText().toString().trim());
                params.put("company_name",mCompanyName.getText().toString().trim());
                params.put("address",mAddress.getText().toString().trim());
                params.put("city_id",cityIdList.get(pos));
                params.put("state_id","22");
                params.put("country_id","1");
                params.put("post_code",mPostCode.getText().toString().trim());
                params.put("phone",mPhone.getText().toString().trim());
                params.put("email",mEmail.getText().toString().trim());
                params.put("notes",mNotes.getText().toString().trim());
                params.put("total_amount",CartActivity.totalAmount.toString());
                params.put("products",CartActivity.productBuilder.toString());
                return params;
            }
        };
        queue.add(stringRequest);
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
         if(mPostCode.getText().toString().isEmpty())
         {
             mPostCode.setError("Invalid");
         }
         if(mPhone.getText().toString().isEmpty())
         {
             mPhone.setError("Invalid");
         }
         if(mNotes.getText().toString().isEmpty())
         {
             mNotes.setError("Invalid");
         }
         return  val;
     }
}
