package com.app.android.deal.club.sampledesigns.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    String productId, productName, productType, productSFT, productSize, printingCost, mountingCost, totalCost,
    productDescription, productImage, stateId, cityId, categoryId, productSts, offerType, offerQuantity, offerStatus, offerName, offerTotal ;
    private TextView mProductName, mProductType, mProductSft, mProductSize, mPrintingCost, mMountingCost, mDashedText,
    mTotalCost, mDescription, mProductStatus, offerLabel;
    ProgressDialog progressDialog;
    private LikeButton mButton;
    private TextView mCheckAvailable, mAddCart;
    StringBuilder decriptionData = new StringBuilder();
    private LinearLayout lyt;
    SessionManager session;
    SliderLayout sliderLayout;

    ArrayList<String> imgList = new ArrayList<>();

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
            startActivity(new Intent(ProductDetails.this,CartActivity.class));
            return true;
        }
        if (id == R.id.order_btn_bar) {
            startActivity(new Intent(ProductDetails.this,OrderDetails.class));
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }


    private void init()
    {
        session = new SessionManager();
        mButton = findViewById(R.id.star_button);
        mCheckAvailable = findViewById(R.id.check_available);
        mAddCart = findViewById(R.id.add_cart_txt);
        mProductName = findViewById(R.id.d_txt_product_name);
        mProductType = findViewById(R.id.d_txt_type);
        mProductSft = findViewById(R.id.d_txt_sft);
        mProductSize = findViewById(R.id.d_txt_size);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        offerLabel = findViewById(R.id.d_lbl_offer);
//        mPrintingCost = findViewById(R.id.d_txt_printting_cost);
//        mMountingCost = findViewById(R.id.d_txt_mounting_cost);
        mDashedText = findViewById(R.id.d_dashed_text);
        mTotalCost = findViewById(R.id.d_total_cost);
        mDescription = findViewById(R.id.d_txt_description);
//        mProductStatus = findViewById(R.id.d_dis_cost);

        sliderLayout = findViewById(R.id.img_slider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.SLIDE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        //sliderLayout.setScrollTimeInSec(3);
        lyt = findViewById(R.id.lyt_desc);
        offerLabel.setVisibility(View.GONE);
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
        offerType = getIntent().getExtras().getString(Constants.OFFER_TYPE);
        offerQuantity =getIntent().getExtras().getString(Constants.OFFER_QUANTITY);
        offerStatus = getIntent().getExtras().getString(Constants.OFFER_STATUS);
        offerName = getIntent().getExtras().getString(Constants.OFFER_NAME);
        offerTotal = getIntent().getExtras().getString(Constants.OFFER_TOTAL) ;



        mProductName.setText(getString(R.string.filled_bullet) +" Location : "+productName);
        mProductType.setText(getString(R.string.filled_bullet) +" TYPE : "+productType);
        mProductSft.setText(getString(R.string.filled_bullet) +" SFT : "+productSFT);
        mProductSize.setText(getString(R.string.filled_bullet) +" SIZE : "+productSize);
//        mPrintingCost.setText(getString(R.string.filled_bullet) +" Printing Cost   :"+printingCost);
//        mMountingCost.setText(getString(R.string.filled_bullet) +" Mounting Cost   :"+mountingCost);
        mDashedText.setText(getString(R.string.Rs)+" "+formatDecimal(totalCost));
        mTotalCost.setText(getString(R.string.Rs)+" "+formatDecimal(offerTotal));



        if(offerStatus.equalsIgnoreCase("1"))
        {
            mDashedText.setText(getString(R.string.Rs)+" "+formatDecimal(totalCost));
            mTotalCost.setText(getString(R.string.Rs)+" "+formatDecimal(offerTotal));
            mDashedText.setPaintFlags(mTotalCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            mTotalCost.setText(getString(R.string.Rs)+" "+formatDecimal(totalCost));
            mDashedText.setVisibility(View.GONE);
        }
//        mDescription.setText(getString(R.string.filled_bullet) +" Description     :"+productDescription);
//        mProductStatus.setText(getString(R.string.filled_bullet) +" Status          :"+productSts);
        ((TextView)findViewById(R.id.d_txt_product_name)).setText(productName);
//        Glide.with(ProductDetails.this)
//                .load(Constants.APP_BASE_URL+productImage)
//                .into(mProductImage);

        decriptionData.append(getString(R.string.filled_bullet) +" Location : "+productName+"\n \n");
        decriptionData.append(getString(R.string.filled_bullet) +" Type : "+productType+"\n \n");
        decriptionData.append(getString(R.string.filled_bullet) +" Sft : "+productSFT+"\n \n");
        decriptionData.append(getString(R.string.filled_bullet) +" Size : "+productSize+"\n \n");
        decriptionData.append(getString(R.string.filled_bullet) +" Printing Cost : "+printingCost+"\n \n");
        decriptionData.append( getString(R.string.filled_bullet) +" Mounting Cost : "+mountingCost+"\n \n");
        decriptionData.append(getString(R.string.filled_bullet)+" Total Cost : "+formatDecimal(totalCost)+"\n \n");


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
                                addCart();
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
            }
        });
    }

    public static String formatDecimal(String value) {
        String val ="";
        try{
            DecimalFormat df = new DecimalFormat("#,##,##,##0.00");
           val = df.format(Double.valueOf(value));
        }catch (Exception ex)
        {
            ex.printStackTrace();
            DecimalFormat df = new DecimalFormat("#,##,##,##0.00");
            val = df.format(Double.valueOf(value));
        }

        return val ;
    }

    private void removeWishList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/remove-wishlist";
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
                params.put("product_id", productId);
                params.put("user_id", session.getPreferences(ProductDetails.this,Constants.CURRENT_USER_ID));
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
        String url = "http://adinn.candyrestaurant.com/api/wishlist";
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
    /*img,name,price,serial num,status */

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
                params.put("start_date",""+mCheckAvailable.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void getDescription()
    {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/product-detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
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
                                    JSONObject jobj = jsonArray.getJSONObject(i);
                                    String id = jobj.getString("id");
                                    String product_id = jobj.getString("product_id");
                                    String title = jobj.getString("title");
                                    String details = jobj.getString("details");
                                    if(!title.equalsIgnoreCase("")&&!details.equalsIgnoreCase("null")) {
                                        decriptionData.append(getResources().getString(R.string.filled_bullet) + " " + title + " : " + details + "\n \n");
                                    }
                                }
                                JSONArray jry = jsonObject.getJSONArray(Constants.PRODUCT_IMAGES);
                                for (int j = 0; j<jry.length() ; j++ )
                                {
                                    JSONObject jobj1 = jry.getJSONObject(j);
                                    String id = jobj1.getString("id");
                                    String images = jobj1.getString(Constants.PRODUCT_IMAGES);
                                    imgList.add(images);
                                }

                                sliderLayout.setScrollTimeInSec(imgList.size());
                                setSliderViews();
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(ProductDetails.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                            mDescription.setText(decriptionData);
                        } catch (JSONException e) {
                            Log.e("RESPONSE-ERROR_DETAILS",""+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.show();
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
        mMonth = c.get(Calendar.MONTH) +1 ;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Log.e("XXXXXXXXXXXX",mDay+"/"+mMonth+"/"+mYear);
        return mDay+"/"+mMonth+"/"+mYear;
    }


    public void onShareWhatsapp(View v)
    {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT,"Location : "+productName+"\n"
                +" Type            :"+productType+"\n"
                +" Sft             :"+productSFT+"\n"
                +" Size            :"+productSize+"\n"
                +" Total Cost      :"+totalCost);

        try{
            startActivity(whatsappIntent);

        }catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(this, "Whatspp have been installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void setSliderViews() {

        try{
            Log.e("RESPONSE-LENGTH",""+imgList.size());

            for (int i = 0; i <= imgList.size(); i++) {
                SliderView sliderView = new SliderView(this);

                switch (i) {
                    case 0:
                        sliderView.setImageUrl(Constants.APP_BASE_URL+imgList.get(i));
                        sliderView.setDescription("");
                        break;
                    case 1:
                        sliderView.setImageUrl(Constants.APP_BASE_URL+imgList.get(i));
                        sliderView.setDescription("");
                        break;
                }

                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                //sliderView.setDescription("setDescription " + (i + 1));
                sliderView.setDescription("");
                final int finalI = i;
                sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(SliderView sliderView) {
                        //Toast.makeText(HomeActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                    }
                });

                //at last add this view in your layout :
                sliderLayout.addSliderView(sliderView);
            }
        }catch (IndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }

    }
}
