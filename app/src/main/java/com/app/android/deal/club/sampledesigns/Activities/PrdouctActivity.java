package com.app.android.deal.club.sampledesigns.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrdouctActivity extends AppCompatActivity {
    private Spinner categoryListSpinner;
    ArrayList<String> spinList = new ArrayList<>();
    ArrayList<String> catIdList = new ArrayList<>();
    ArrayList<RecentPrdocutData> productData = new ArrayList<>();
    RecyclerView list_view_product;
    TextView hintSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdouct);
        categoryListSpinner = findViewById(R.id.spin_category);
        list_view_product = findViewById(R.id.product_list_view);
        hintSearch = findViewById(R.id.hint_search);

        if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_BEST_BANNERS))
        {

        }
        else if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_RECENT_BANNERS))
        {

        }
        else if(getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_SERVICE))
        {

        }

        list_view_product.setLayoutManager(new GridLayoutManager(this, 2));
        spinList.add("Select Category");
        catIdList.add("11111");
        getCategorySpinnerValues();
        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("CLICKEDPOSITION",catIdList.size()+"    "+i);
                if(!spinList.get(i).equalsIgnoreCase("Select Category"))
                {
                    getCategoryDetails(catIdList.get(i));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void getCategorySpinnerValues() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/category";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("categories");
                                for(int i = 0; i<jsonArray.length();i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String categoryName = object.getString("category");
                                    spinList.add(id+" . "+categoryName);
                                    catIdList.add(id);
                                    ArrayAdapter aa = new ArrayAdapter(PrdouctActivity.this,android.R.layout.simple_spinner_item,spinList);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    categoryListSpinner.setAdapter(aa);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(PrdouctActivity.this, ""+stsMessage,
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
                Toast.makeText(PrdouctActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    private void getCategoryDetails(final String categoryId) {
       list_view_product.setAdapter(null);
       productData.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/category-product";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-PRODUCT",""+response);
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
                                    String uId = resObject.getString("id");
                                    String productName = resObject.getString("name");
                                    String price = resObject.getString("price");
                                    String size = resObject.getString("size");
                                    String sft = resObject.getString("sft");
                                    String type = resObject.getString("type");
                                    String printingCost = resObject.getString("printing_cost");
                                    String mountingCost = resObject.getString("mounting_cost");
                                    String totalCost = resObject.getString("total_cost");
                                    String description = resObject.getString("Description");
                                    String image = resObject.getString("image");
                                    String stateId = resObject.getString("state_id");
                                    String city_id = resObject.getString("city_id");
                                    String categoryId = resObject.getString("category_id");
                                    String sts = resObject.getString("status");
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts));
                                    RecentProductAdapter radapter = new RecentProductAdapter(productData);
                                    radapter.notifyDataSetChanged();
                                    list_view_product.setAdapter(radapter);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(PrdouctActivity.this, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE-PRODUCT_ERROR",""+error.getMessage());
                Toast.makeText(PrdouctActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_id", categoryId);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
