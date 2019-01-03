package com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Adapters.OfferProductAdapter;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels.RecentPrdocutData;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.R;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.Constants;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfferActivity extends AppCompatActivity {
    RecyclerView list_view_product;
    ProgressDialog progressDialog;
    ArrayList<RecentPrdocutData> productData = new ArrayList<>();

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
            startActivity(new Intent(OfferActivity.this, CartActivity.class));
            return true;
        }
        if (id == R.id.order_btn_bar) {
            startActivity(new Intent(OfferActivity.this, OrderDetails.class));
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        list_view_product = findViewById(R.id.product_list_view);
        list_view_product.setLayoutManager(new GridLayoutManager(this, 2));
        getCategoryDetails("0", "0", "0", "0", "120000", "1000", "");
    }


    private void getCategoryDetails(final String categoryId,final String type_id,final String stateId,
                                    final String cityId,final String max, final String min, final String sort) {
        progressDialog.show();

        Log.e("VALUESFILTER",categoryId+"///"+type_id+"///"+stateId+"///"+cityId+"///"+max+"///"+min+"///"+sort);
        list_view_product.setAdapter(null);
        productData.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/sort-order-product";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE-PRODUCT",""+response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                if(jsonArray.length()==0)
                                {
                                    new AwesomeSuccessDialog(OfferActivity.this)
                                            .setTitle("No Product!")
                                            .setMessage("Your search did not match any products")
                                            .setColoredCircle(R.color.colorAccent)
                                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                            .setCancelable(true)
                                            .setPositiveButtonText("Refresh")
                                            .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                                            .setPositiveButtonTextColor(R.color.white)
                                            .setPositiveButtonClick(new Closure() {
                                                @Override
                                                public void exec() {

                                                }
                                            })
                                            .show();
                                    //Toast.makeText(PrdouctActivity.this, "No Products", Toast.LENGTH_SHORT).show();
                                }else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resObject = jsonArray.getJSONObject(i);

                                        if(resObject.getString(Constants.OFFER_STATUS).equalsIgnoreCase("1"))
                                        {
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
                                            String offerType = resObject.getString(Constants.OFFER_TYPE);
                                            String offerName = resObject.getString(Constants.OFFER_NAME);
                                            String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                            String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                            String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                            String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                            String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                            productData.add(new RecentPrdocutData(uId, productName, price, size, sft, type, printingCost, mountingCost
                                                    , totalCost, description, image, stateId, city_id, categoryId, sts,offerType,
                                                    offerQuantity,offerStatus,offerName,offerTotal));
                                            OfferProductAdapter radapter = new OfferProductAdapter(productData);
                                            radapter.notifyDataSetChanged();
                                            list_view_product.setAdapter(radapter);
                                        }

                                    }
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(OfferActivity.this, ""+stsMessage,
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
                Log.e("RESPONSE-PRODUCT_ERROR",""+error.getMessage());
                Toast.makeText(OfferActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_id", categoryId);
                params.put("sort_type",sort);
                params.put("type_id",type_id);
                params.put("state_id",stateId);
                params.put("city_id",cityId);
                params.put("max_price",max);
                params.put("min_price",min);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
