package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.Adapters.ProductAdapter;
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    RecyclerView List_view;
    ArrayList<RecentPrdocutData> cartDataList = new ArrayList<>();
    public static ArrayList<String> cartIdList = new ArrayList<String>();
    ArrayList<String> costList = new ArrayList<>();
    public static StringBuilder costBuilder = new StringBuilder();
    SessionManager session;
    public static String totalAmount= "";
    public static TextView txtTotalCost;
    public static ProductAdapter radapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        session = new SessionManager();
        txtTotalCost = findViewById(R.id.txt_total_cost);

        List_view=(RecyclerView)findViewById(R.id.list_view);
        RecyclerView.LayoutManager cart_lyt=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        List_view.setLayoutManager(cart_lyt);
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

    public static String formatDecimal(String value) {
        DecimalFormat df = new DecimalFormat("#,##,##,##0.00");
        return df.format(Double.valueOf(value));
    }

    public String getTotalAmount()
    {
        String amount = "";
        double amountToCalculate = 0.0;
        for (int i = 0 ; i < costList.size(); i++)
        {
            amountToCalculate = amountToCalculate + Double.valueOf(costList.get(i));
        }
        amount = String.valueOf(amountToCalculate);
        return  amount;
    }

    private void getCartList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/carts";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-HOME_Recent", "" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if (loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                if(jsonArray.length() == 0)
                                {
                                    Toast.makeText(CartActivity.this, "No Cart Added", Toast.LENGTH_SHORT).show();
                                    txtTotalCost.setText(getResources().getString(R.string.Rs)+" 0.00");
                                    totalAmount = "0.00";
                                }
                                else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
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
                                        costList.add(totalCost);
                                        cartIdList.add(uId);
                                        if(i == 0 )
                                        {
                                            costBuilder.append(uId);
                                        }
                                        costBuilder.append(","+uId);
                                        cartDataList.add(new RecentPrdocutData(uId, productName, price, size, sft, type, printingCost, mountingCost
                                                , totalCost, description, "D4W3KdpwFYMc.jpg", stateId, city_id, categoryId, sts));
                                        radapter = new ProductAdapter(cartDataList);
                                        List_view.setAdapter(radapter);
                                    }
                                    txtTotalCost.setText(getResources().getString(R.string.Rs)+" "+formatDecimal(getTotalAmount()));
                                    totalAmount =  getTotalAmount();
                                }

                            } else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED)) {
                                Toast.makeText(CartActivity.this, "" + stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE-HOME_Recent", "" + error.getMessage());
                Toast.makeText(CartActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        @Override
        protected Map<String, String> getParams () throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", session.getPreferences(CartActivity.this,Constants.CURRENT_USER_ID));
            return params;
        }
    };
        queue.add(stringRequest);
    }


    public void onCheckoutClick(View v) {
        if(cartDataList.size()==0)
        {
            Toast.makeText(this, "Cart is Empty!", Toast.LENGTH_SHORT).show();
        }
        else {
           startActivity(new Intent(CartActivity.this,BookingFormActivity.class));
            Log.e("RESPONSE-REGISTER",""+cartIdList.toString());
        }
    }

    private void checkOutProduct()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/checkout";
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
                                //Toast.makeText(CartActivity.this, "Product added to Wishlist", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(CartActivity.this, BookingFormActivity.class);
                                in.putExtra("cart_list",cartDataList);
                                in.putExtra("cart_size",cartDataList.size());
                                startActivity(in);
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

                Toast.makeText(CartActivity.this, "Product Added Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getPreferences(CartActivity.this,Constants.CURRENT_USER_ID));
                params.put("products",cartIdList.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
