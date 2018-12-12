package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView List_view;
    ArrayList<RecentPrdocutData> cartDataList = new ArrayList<>();
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        session = new SessionManager();
        List_view=(RecyclerView)findViewById(R.id.list_view);
        RecyclerView.LayoutManager lytMgr=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        List_view.setLayoutManager(lytMgr);
        if(session.getPreferences(CartActivity.this,Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGIN))
        {
            getCartList();
        }
        else
        {
            startActivity(new Intent(CartActivity.this,LoginPage.class));
            Toast.makeText(this, "Please login and check your cart list", Toast.LENGTH_SHORT).show();
        }

    }

    private void getCartList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/cart";
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
                                for(int i = 0; i < jsonArray.length(); i++ )
                                {
                                    JSONObject resObject = jsonArray.getJSONObject(i);
                                    String uId = resObject.getString(Constants.PRODUCT_ID);
                                    String productName = resObject.getString(Constants.PRODUCT_NAME);
                                    String price = resObject.getString(Constants.PRODUCT_NAME);
                                    String size = resObject.getString(Constants.PRODUCT_SIZE);
                                    String sft = resObject.getString(Constants.PRODUCT_SFT);
                                    String type = resObject.getString(Constants.PRODUCT_TYPE);
                                    String printingCost = resObject.getString(Constants.PRINTING_COST);
                                    String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                    String totalCost = resObject.getString(Constants.TOTAL_COST);
                                    String description = resObject.getString(Constants.PRODUCT_DESCRIPTION);
                                    String image = resObject.getString(Constants.PRODUCT_IMAGE);
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    cartDataList.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts));
                                    RecentProductAdapter radapter = new RecentProductAdapter(cartDataList);
                                    List_view.setAdapter(radapter);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(CartActivity.this, ""+stsMessage,
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
                Toast.makeText(CartActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
