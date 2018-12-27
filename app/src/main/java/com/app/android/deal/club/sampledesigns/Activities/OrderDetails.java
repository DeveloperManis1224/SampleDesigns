package com.app.android.deal.club.sampledesigns.Activities;

import android.app.ProgressDialog;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.Adapters.OrderAdapter;
import com.app.android.deal.club.sampledesigns.Adapters.ProductAdapter;
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DataModels.OrderData;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.SplashScreen;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    ArrayList<OrderData> orderData = new ArrayList<>();
    RecyclerView list_view_order;
    ProgressDialog progressDialog;
    public static ProductAdapter radapter;
    SessionManager session ;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;

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
            startActivity(new Intent(OrderDetails.this,CartActivity.class));
            return true;
        }
        if (id == R.id.order_btn_bar) {
            startActivity(new Intent(OrderDetails.this,OrderDetails.class));
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
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        list_view_order = findViewById(R.id.order_list_view);

        session  = new SessionManager();

        RecyclerView.LayoutManager cart_lyt=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_view_order.setLayoutManager(cart_lyt);
        getOrderProducts();
    }



    private void getOrderProducts() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/order-lists";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("RESPONSE-HOME_Recent",session.getPreferences(OrderDetails.this,Constants.CURRENT_USER_ID)+""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray("orderProducts");
                                if (jsonArray.length() == 0) {
                                    new AwesomeSuccessDialog(OrderDetails.this)
                                            .setTitle("No Products")
                                            .setMessage("Your order history is Empty!")
                                            .setColoredCircle(R.color.colorAccent)
                                            .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                                            .setCancelable(true)
                                            .setPositiveButtonText("Back")
                                            .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                                            .setPositiveButtonTextColor(R.color.white)
                                            .setPositiveButtonClick(new Closure() {
                                                @Override
                                                public void exec() {
                                                    Intent in = new Intent(OrderDetails.this, HomeActivity.class);
                                                    startActivity(in);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                                else
                                {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject resObject = jsonArray.getJSONObject(i);
                                    String id = resObject.getString(Constants.PRODUCT_ID);
                                    String order_id = resObject.getString("order_id");
                                    JSONObject jobjProduct = resObject.getJSONObject("product");
                                    String name = jobjProduct.getString("name");
                                    String image = jobjProduct.getString("image");
                                    JSONObject jobjOrder = resObject.getJSONObject("order");
                                    String serial_number = jobjOrder.getString("serial_no");
                                    String status = jobjOrder.getString("status");
                                    orderData.add(new OrderData(id, image, name, order_id, status, serial_number));

                                    OrderAdapter odad = new OrderAdapter(orderData);
                                    list_view_order.setAdapter(odad);
                                }
                            }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {

                                Toast.makeText(OrderDetails.this, ""+stsMessage,
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
                progressDialog.dismiss();
                Log.e("RESPONSE-HOME_Recent",""+error.getMessage());
                Toast.makeText(OrderDetails.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getPreferences(OrderDetails.this,Constants.CURRENT_USER_ID));
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
