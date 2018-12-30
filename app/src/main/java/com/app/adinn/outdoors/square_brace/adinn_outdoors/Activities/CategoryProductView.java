package com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Adapters.RecentProductAdapter;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels.RecentPrdocutData;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.R;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CategoryProductView extends AppCompatActivity {

    ArrayList<RecentPrdocutData> productData = new ArrayList<>();
    ArrayList<String> listLoc = new ArrayList<String>();
    ArrayList<String> listType = new ArrayList<String>();
    ArrayList<String> listId = new ArrayList<String>();
    RecyclerView list_view_product;
    LinearLayout lyt ;
    AutoCompleteTextView mSearchInput ;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product_view);
        lyt = findViewById(R.id.lyt_container);
        mSearchInput = findViewById(R.id.auto_search_edt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                int pos = listLoc.indexOf(selected);

                if(listType.get(pos).equalsIgnoreCase("1"))
                {
                    //state
                    getType1Locations(listId.get(pos));
                }
                else {
                    //city
                    getType2Locations(listId.get(pos));
                }

                Log.v("asasasas",""+selected+"///"+listLoc.get(pos)+"///"+listId.get(pos)+"///"+listType.get(pos));
            }
        });

        list_view_product = findViewById(R.id.product_list_view);

        list_view_product.setLayoutManager(new GridLayoutManager(this, 2));

        if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_RECENT_BANNERS))
        {
            ((TextView)findViewById(R.id.txt_label)).setText("Recently Added");
            lyt.setVisibility(View.GONE);
            getRecentProducts();
        }
        else if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_BEST_BANNERS))
        {
            lyt.setVisibility(View.GONE);
            ((TextView)findViewById(R.id.txt_label)).setText("Best Fit");
            getBestFitings();
        }
        else if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_SEARCH))
        {
            lyt.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txt_label)).setText("Search Results");
            getLocationList();
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
                                    String image = resObject.getString(Constants.PRODUCT_IMAGE);
//                                    JSONArray jsry = resObject.getJSONArray(Constants.PRODUCT_IMAGES);
//                                    String image = jsry.getJSONObject(0).getString(Constants.PRODUCT_IMAGES);
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String offerType = resObject.getString(Constants.OFFER_TYPE);
                                    String offerName = resObject.getString(Constants.OFFER_NAME);
                                    String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                    String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName,offerTotal));
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
                                    String image = resObject.getString(Constants.PRODUCT_IMAGE);
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
                                    String offerType = resObject.getString(Constants.OFFER_TYPE);
                                    String offerName = resObject.getString(Constants.OFFER_NAME);
                                    String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                    String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName,offerTotal));
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

    private void getLocationList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/search-locations";
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
                                JSONArray jry = jsonObject.getJSONArray("locations");

                                for (int i = 0; i < jry.length() ; i++)
                                {
                                    JSONObject jobj = jry.getJSONObject(i);
                                    String id = jobj.getString("id");
                                    String name  = jobj.getString("name");
                                    String type = jobj.getString("type");

                                    listId.add(id);
                                    listLoc.add(name);
                                    listType.add(type);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryProductView.this,
                                        android.R.layout.simple_spinner_dropdown_item,listLoc);
                                mSearchInput.setThreshold(2);
                                mSearchInput.setAdapter(adapter);
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

    private void getType1Locations(final String name) {
        productData.clear();
        list_view_product.setAdapter(null);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/state-product";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
                                    String image = resObject.getString(Constants.PRODUCT_IMAGE);
                                    String printingCost = resObject.getString(Constants.PRINTING_COST);
                                    String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                    String totalCost = resObject.getString(Constants.TOTAL_COST);
                                    String description = resObject.getString(Constants.PRODUCT_DESCRIPTION);
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String offerType = resObject.getString(Constants.OFFER_TYPE);
                                    String offerName = resObject.getString(Constants.OFFER_NAME);
                                    String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                    String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName,offerTotal));
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               params.put("state_id",name);
                return params;
            }
        };
        queue.add(stringRequest);
    }


    private void getType2Locations(final String name) {
        productData.clear();
        list_view_product.setAdapter(null);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/city-product";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
                                    String image = resObject.getString(Constants.PRODUCT_IMAGE);
                                    String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                    String totalCost = resObject.getString(Constants.TOTAL_COST);
                                    String description = resObject.getString(Constants.PRODUCT_DESCRIPTION);
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String offerType = resObject.getString(Constants.OFFER_TYPE);
                                    String offerName = resObject.getString(Constants.OFFER_NAME);
                                    String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                    String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName,offerTotal));
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               params.put("city_id",name);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
