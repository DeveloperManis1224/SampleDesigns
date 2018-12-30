package com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishList extends AppCompatActivity {
    RecyclerView List_view;
    ArrayList<RecentPrdocutData> cartDataList = new ArrayList<>();
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        session = new SessionManager();

        List_view=(RecyclerView)findViewById(R.id.list_view_wish);
        RecyclerView.LayoutManager lytMgr=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        List_view.setLayoutManager(lytMgr);


        if(session.getPreferences(WishList.this, Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGIN))
        {
            getWishList();
        }
        else
        {
            startActivity(new Intent(WishList.this,LoginPage.class));
            Toast.makeText(this, "Please login and check your Wishlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void getWishList() {
        final ArrayList<String> images = new ArrayList<String>(){};
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/wishlists";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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

                                if(jsonArray.length()==0)
                                {
                                    Toast.makeText(WishList.this, "No Wishlist added", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    for(int i = 0; i < jsonArray.length(); i++ )
                                    {
                                        JSONObject resObject = jsonArray.getJSONObject(i);
                                        String uId = resObject.getString("id");
                                        String productName = resObject.getString("name");
                                        String price = resObject.getString("price");
                                        String size = resObject.getString("size");
                                        String sft = resObject.getString("sft");
                                        JSONObject type_Object =resObject.getJSONObject("type");
                                        String type = type_Object.getString("type");
                                        String printingCost = resObject.getString("printing_cost");
                                        String mountingCost = resObject.getString("mounting_cost");
                                        String totalCost = resObject.getString("total_cost");
                                        String description = resObject.getString("Description");
                                        JSONArray jsry = resObject.getJSONArray(Constants.PRODUCT_IMAGES);
                                        String image = jsry.getJSONObject(0).getString(Constants.PRODUCT_IMAGES);
                                        String stateId = resObject.getString("state_id");
                                        String offerType = resObject.getString(Constants.OFFER_TYPE);
                                        String offerName = resObject.getString(Constants.OFFER_NAME);
                                        String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                        String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                        String city_id = resObject.getString("city_id");
                                        String categoryId = resObject.getString("category_id");
                                        String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                        String sts = resObject.getString("status");
                                        cartDataList.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                                ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName,offerTotal));
                                        RecentProductAdapter radapter = new RecentProductAdapter(cartDataList);
                                        List_view.setAdapter(radapter);
                                    }
                                }

                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(WishList.this, ""+stsMessage,
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
                Toast.makeText(WishList.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getPreferences(WishList.this,Constants.CURRENT_USER_ID));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
