package com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Adapters.NotificationAdapter;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Adapters.RecentProductAdapter;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels.NotificationData;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels.RecentPrdocutData;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.R;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Services.Constant;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.Constants;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView offerListView;
    private RecyclerView.LayoutManager lytMgr ;
    private ArrayList<NotificationData>  offerData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Init();
    }

    private void Init() {
        offerListView = findViewById(R.id.list_offer);
        lytMgr = new LinearLayoutManager(NotificationActivity.this);
        offerListView.setLayoutManager(lytMgr);

        getNotification();
    }

    private void getNotification()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url = "http://adinn.candyrestaurant.com/api/notification-lists";
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_NOTIFICATION",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                    String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                    if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("notifications");
                        if(jsonArray.length() == 0)
                        {
                            new AwesomeSuccessDialog(NotificationActivity.this)
                                    .setTitle("Notification empty!")
                                    .setMessage("There is no Notifications.")
                                    .setColoredCircle(R.color.colorAccent)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                                    .setCancelable(true)
                                    .setPositiveButtonText("Back")
                                    .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            Intent in = new Intent(NotificationActivity.this, HomeActivity.class);
                                            startActivity(in);
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject notificationObject = jsonArray.getJSONObject(i);
                                String productStatus = notificationObject.getString("product_id");
                                String message = notificationObject.getString("message");
                                JSONObject resObject = notificationObject.getJSONObject("product");
                                String uId = resObject.getString(Constants.PRODUCT_ID);
                                String productName = resObject.getString(Constants.PRODUCT_NAME);
                                String price = resObject.getString(Constants.PRODUCT_NAME);
                                String size = resObject.getString(Constants.PRODUCT_SIZE);
                                String sft = resObject.getString(Constants.PRODUCT_SFT);
                                String type = resObject.getString(Constants.PRODUCT_TYPE);
                                Log.e("RESPONSE-HOME_Best", "" + type);
                                String printingCost = resObject.getString(Constants.PRINTING_COST);
                                String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                String totalCost = resObject.getString(Constants.TOTAL_COST);
                                String description = resObject.getString(Constants.PRODUCT_DESCRIPTION);
                                String image = resObject.getString(Constants.PRODUCT_IMAGE);
                                String stateId = resObject.getString(Constants.STATE_ID);
                                String city_id = resObject.getString(Constants.CITY_ID);
                                String offerType = resObject.getString(Constants.OFFER_TYPE);
                                String offerName = resObject.getString(Constants.OFFER_NAME);
                                String offerStatus = resObject.getString(Constants.OFFER_STATUS);
                                String offerQuantity = resObject.getString(Constants.OFFER_QUANTITY);
                                String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                String offerTotal = resObject.getString(Constants.OFFER_TOTAL);
                                offerData.add(new NotificationData(productStatus, message, uId, productName, price, size, sft, type, printingCost, mountingCost
                                        , totalCost, description, image, stateId, city_id, categoryId, sts, offerType, offerQuantity, offerStatus, offerName, offerTotal));
                                NotificationAdapter radapter = new NotificationAdapter(offerData);
                                offerListView.setAdapter(radapter);
                            }
                        }
                    }
                    else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                    {
                        Toast.makeText(NotificationActivity.this, ""+stsMessage,
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
            Log.e(getClass().getSimpleName(),""+error.getMessage());
        }
    }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return super.getParams();
            }
        };
        queue.add(request);

    }

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
            startActivity(new Intent(NotificationActivity.this, CartActivity.class));
            return true;
        }
        if (id == R.id.order_btn_bar) {
            startActivity(new Intent(NotificationActivity.this, OrderDetails.class));
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
