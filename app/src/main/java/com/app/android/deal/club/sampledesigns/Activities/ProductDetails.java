package com.app.android.deal.club.sampledesigns.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    String productId, productName, productType, productSFT, productSize, printingCost, mountingCost, totalCost,
    productDescription, productImage, stateId, cityId, categoryId, productSts ;
    private TextView mProductName, mProductType, mProductSft, mProductSize, mPrintingCost, mMountingCost,
    mTotalCost, mDescription, mProductStatus;
    private ImageView mProductImage;
    private LikeButton mButton;
    private TextView mCheckAvailable, mAddCart;
    StringBuilder decriptionData = new StringBuilder();

    private LinearLayout lyt;

    SessionManager session;

    String startDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
    }
    private void init()
    {
        session = new SessionManager();
        mButton = findViewById(R.id.star_button);
        mCheckAvailable = findViewById(R.id.check_available);
        mAddCart = findViewById(R.id.add_cart_txt);
        mProductName = findViewById(R.id.d_txt_name_product);
        mProductType = findViewById(R.id.d_txt_type);
        mProductSft = findViewById(R.id.d_txt_sft);
        mProductSize = findViewById(R.id.d_txt_size);
        mPrintingCost = findViewById(R.id.d_txt_printting_cost);
        mMountingCost = findViewById(R.id.d_txt_mounting_cost);
        mTotalCost = findViewById(R.id.d_total_cost);
        mDescription = findViewById(R.id.d_txt_description);
        mProductStatus = findViewById(R.id.d_dis_cost);
        mProductImage = findViewById(R.id.produ_img);
        lyt = findViewById(R.id.lyt_desc);


        getDescription();

        productId = getIntent().getExtras().getString(Constants.PRODUCT_ID);
        productName = getIntent().getExtras().getString(Constants.PRODUCT_NAME);
        productType = getIntent().getExtras().getString(Constants.PRODUCT_TYPE);
        productSFT = getIntent().getExtras().getString(Constants.PRODUCT_SFT);
        productSize = getIntent().getExtras().getString(Constants.PRODUCT_SIZE);
        printingCost = getIntent().getExtras().getString(Constants.PRINTING_COST);
        mountingCost = getIntent().getExtras().getString(Constants.MOUNTING_COST);
        totalCost = getIntent().getExtras().getString(Constants.TOTAL_COST);
        productDescription = getIntent().getExtras().getString(Constants.PRODUCT_DESCRIPTION);
        productImage = getIntent().getExtras().getString(Constants.PRODUCT_IMAGE);
        stateId = getIntent().getExtras().getString(Constants.STATE_ID);
        cityId = getIntent().getExtras().getString(Constants.CITY_ID);
        categoryId = getIntent().getExtras().getString(Constants.CATEGORY_ID);
        productSts = getIntent().getExtras().getString(Constants.PRODUCT_STATUS);

        mProductName.setText(getString(R.string.filled_bullet) +" Name            :"+productName);
        mProductType.setText(getString(R.string.filled_bullet) +" Type            :"+productType);
        mProductSft.setText(getString(R.string.filled_bullet) +" Sft             :"+productSFT);
        mProductSize.setText(getString(R.string.filled_bullet) +" Size            :"+productSize);
        mPrintingCost.setText(getString(R.string.filled_bullet) +" Printing Cost   :"+printingCost);
        mMountingCost.setText(getString(R.string.filled_bullet) +" Mounting Cost   :"+mountingCost);
        mTotalCost.setText(getString(R.string.filled_bullet) +" Total Cost      :"+totalCost);
        mDescription.setText(getString(R.string.filled_bullet) +" Description     :"+productDescription);
        mProductStatus.setText(getString(R.string.filled_bullet) +" Status          :"+productSts);
        ((TextView)findViewById(R.id.d_txt_product_name)).setText(productName);
        Glide.with(ProductDetails.this)
                .load(Constants.APP_BASE_URL+productImage)
                .into(mProductImage);

        mButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addWishList();
                likeButton.setLiked(true);
               // Toast.makeText(ProductDetails.this, "Product added to wishlist...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                removeWishList();
                likeButton.setLiked(false);
               // Toast.makeText(ProductDetails.this, "Product removed from wishlist...", Toast.LENGTH_SHORT).show();
            }
        });

        mCheckAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int mYear = 0;
               int mMonth = 0 ;
               int mDay = 0;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                checkAvailability();
                                mCheckAvailable.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart();
                Toast.makeText(ProductDetails.this, "" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeWishList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-REGISTER",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                Toast.makeText(ProductDetails.this, "Product removed from Wishlist...", Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ProductDetails.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetails.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "");
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void checkAvailability()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/check-availability";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-REGISTER",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductDetails.this);
                                builder1.setTitle(""+productName);
                                builder1.setMessage(""+stsMessage+"\n Price : "+getResources().getString(R.string.Rs)+" "+totalCost);
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "Add to Wishlist",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                addWishList();
                                            }
                                        });

//                                builder1.setNegativeButton(
//                                        "Add to Cart",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.dismiss();
//                                            }
//                                        });

                                builder1.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ProductDetails.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetails.this, "Product Added Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", productId);
                params.put("start_date",""+mCheckAvailable.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void addWishList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/add-wishlist";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-REGISTER",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                Toast.makeText(ProductDetails.this, "Product added to Wishlist", Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ProductDetails.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ProductDetails.this, "Product Added Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", productId);
                params.put("user_id", session.getPreferences(ProductDetails.this,Constants.CURRENT_USER_ID));
                params.put("start_date",getStartDate());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void addCart()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/cart";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-REGISTER",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                Toast.makeText(ProductDetails.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ProductDetails.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetails.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", productId);
                params.put("user_id", session.getPreferences(ProductDetails.this,Constants.CURRENT_USER_ID));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void getDescription()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/product-detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-REGISTER",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jobj = new JSONObject();
                                    String id = jobj.getString("id");
                                    String product_id = jobj.getString("product_id");
                                    String title = jobj.getString("title");
                                    String details = jobj.getString("details");

                                    decriptionData.append(getResources().getString(R.string.filled_bullet)+" "+title+" : "+details+"\n");

//                                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    TextView tv=new TextView(ProductDetails.this);
//                                    tv.setLayoutParams(lparams);
//                                    tv.setText(getResources().getString(R.string.filled_bullet)+" "+title+" : "+details);
//                                    lyt.addView(tv);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ProductDetails.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                            ((TextView)findViewById(R.id.d_txt_hoarding_id)).setText(decriptionData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetails.this, "Failed"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", productId);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private String getStartDate()
    {
        int mYear = 0;
        int mMonth = 0 ;
        int mDay = 0;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        return mDay+"/"+mMonth+1+"/"+mYear;
    }

}
