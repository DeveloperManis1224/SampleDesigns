package com.app.android.deal.club.sampledesigns.Activities;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryProductView extends AppCompatActivity {

    ArrayList<RecentPrdocutData> productData = new ArrayList<>();
    RecyclerView list_view_product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product_view);

        list_view_product = findViewById(R.id.product_list_view);

        list_view_product.setLayoutManager(new GridLayoutManager(this, 2));

        if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_RECENT_BANNERS))
        {
            ((TextView)findViewById(R.id.txt_label)).setText("Recently Added");
            getRecentProducts();
        }
        else
        {
            ((TextView)findViewById(R.id.txt_label)).setText("Best Fit");
            getBestFitings();
        }
    }

    private void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/recent-product";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-HOME_Recent",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                int val = 0;
//                                if(jsonArray.length() <10)
//                                {
//                                    val = jsonArray.length();
//                                }
//                                else
//                                {
//                                    val = 10;
//                                }

                                for(int i = 0; i < jsonArray.length(); i++ )
                                {
                                    JSONObject resObject = jsonArray.getJSONObject(i);
                                    String uId = resObject.getString(Constants.PRODUCT_ID);
                                    String productName = resObject.getString(Constants.PRODUCT_NAME);
                                    String price = resObject.getString(Constants.PRODUCT_NAME);
                                    String size = resObject.getString(Constants.PRODUCT_SIZE);
                                    String sft = resObject.getString(Constants.PRODUCT_SFT);
                                    String type = resObject.getJSONObject(Constants.PRODUCT_TYPE).getString(Constants.PRODUCT_TYPE);
                                    String printingCost = resObject.getString(Constants.PRINTING_COST);
                                    String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                    String totalCost = resObject.getString(Constants.TOTAL_COST);
                                    String description = resObject.getString(Constants.PRODUCT_DESCRIPTION);
//                                    JSONArray jsry = resObject.getJSONArray(Constants.PRODUCT_IMAGES);
//                                    String image = jsry.getJSONObject(0).getString(Constants.PRODUCT_IMAGES);
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,"D4W3KdpwFYMc.jpg",stateId,city_id,categoryId,sts));
                                    RecentProductAdapter radapter = new RecentProductAdapter(productData);
                                    list_view_product.setAdapter(radapter);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(CategoryProductView.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("RESPONSE-HOME_Recent",""+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE-HOME_Recent",""+error.getMessage());
                Toast.makeText(CategoryProductView.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void getBestFitings() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/best-product";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-HOME_Best",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                for(int i = 0; i < jsonArray.length(); i++ )
                                {
                                    JSONObject resObject = jsonArray.getJSONObject(i);
                                    String uId = resObject.getString(Constants.PRODUCT_ID);
                                    String productName = resObject.getString(Constants.PRODUCT_NAME);
                                    String price = resObject.getString(Constants.PRODUCT_NAME);
                                    String size = resObject.getString(Constants.PRODUCT_SIZE);
                                    String sft = resObject.getString(Constants.PRODUCT_SFT);
                                    String type = resObject.getJSONObject(Constants.PRODUCT_TYPE).getString(Constants.PRODUCT_TYPE);
                                    String printingCost = resObject.getString(Constants.PRINTING_COST);
                                    String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                    String totalCost = resObject.getString(Constants.TOTAL_COST);
                                    String description = resObject.getString(Constants.PRODUCT_DESCRIPTION);
//                                    String image;
//                                    JSONArray jsry ;
//                                    try {
//                                        jsry = resObject.getJSONArray(Constants.PRODUCT_IMAGES);
//                                         image= jsry.getJSONObject(0).getString(Constants.PRODUCT_IMAGES);
//                                    }catch (IndexOutOfBoundsException ex)
//                                    {
//                                        Log.e("RESPONSE-HOME_BError",""+ex.getMessage());
//                                        ex.printStackTrace();
//                                        image = "not";
//                                    }
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,"D4W3KdpwFYMc.jpg",stateId,city_id,categoryId,sts));
                                    RecentProductAdapter radapter = new RecentProductAdapter(productData);
                                    list_view_product.setAdapter(radapter);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(CategoryProductView.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("RESPONSE-HOME_BError",""+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE-HOME_Recent",""+error.getMessage());
                Toast.makeText(CategoryProductView.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
