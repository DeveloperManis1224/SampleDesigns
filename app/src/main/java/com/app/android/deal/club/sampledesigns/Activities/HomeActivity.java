package com.app.android.deal.club.sampledesigns.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DBHelper;
import com.app.android.deal.club.sampledesigns.DataAdapter;
import com.app.android.deal.club.sampledesigns.DataModel;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<DataModel> dataModal=new ArrayList<DataModel>();
    ArrayList<RecentPrdocutData> productDataList = new ArrayList<>();
    ArrayList<RecentPrdocutData> bestDataList = new ArrayList<>();
    RecyclerView List_view, category_listview, menu_listview;
    RecyclerView List_view1;
    SessionManager session;
    SliderLayout sliderLayout;
    TextView mRecentBanner, mBestBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        session = new SessionManager();
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.SLIDE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(3);

        mRecentBanner = findViewById(R.id.btn_recent_banners);
        mBestBanner  = findViewById(R.id.btn_best_banners);

        mRecentBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,CategoryProductView.class).putExtra(Constants.PAGE_FROM,Constants.PAGE_RECENT_BANNERS));
            }
        });
        mBestBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,CategoryProductView.class).putExtra(Constants.PAGE_FROM,Constants.PAGE_BEST_BANNERS));
            }
        });
        //set scroll delay in seconds :

        setSliderViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
        List_view=(RecyclerView)findViewById(R.id.list_view);
        List_view1 =(RecyclerView) findViewById(R.id.list_view1);
        category_listview = findViewById(R.id.category_list);
        menu_listview = findViewById(R.id.menu_list);

        RecyclerView.LayoutManager lytMgr=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lytMgr1=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //category
        RecyclerView.LayoutManager cat_lytmgr=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //menu
        RecyclerView.LayoutManager menu_lytMgr=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_listview.setLayoutManager(cat_lytmgr);
        menu_listview.setLayoutManager(menu_lytMgr);


        List_view.setLayoutManager(lytMgr);
        List_view1.setLayoutManager(lytMgr1);

        Log.e("UserId",""+session.getPreferences(HomeActivity.this,Constants.CURRENT_USER_ID));
        getRecentProducts();
        getBestFitings();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_user_name);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_email);
        if(session.getPreferences(HomeActivity.this, Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGOUT))
        {
            navUsername.setText("Login");
            navEmail.setVisibility(View.GONE);
            navUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(),LoginPage.class));
                }
            });
        }
        else if(session.getPreferences(HomeActivity.this, Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGIN))
        {
            navUsername.setText("Welcome "+session.getPreferences(HomeActivity.this,Constants.CURRENT_USER_NAME)+",");
            navEmail.setVisibility(View.VISIBLE);
            navEmail.setText(session.getPreferences(HomeActivity.this,Constants.CURRENT_USER_EMAIL));
        }
        else
        {
            navUsername.setText("Login");
            navEmail.setVisibility(View.GONE);
            navUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(),LoginPage.class));
                }
            });
        }
    }

    private void setSliderViews() {

        for (int i = 0; i <= 2; i++) {

            SliderView sliderView = new SliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.slider1);
                    sliderView.setDescription("");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.slider2);
                    sliderView.setDescription("");
                   break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.slider3);
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
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
        if (id == R.id.action_settings) {
            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
        } else if (id == R.id.nav_services) {
            startActivity(new Intent(HomeActivity.this,Services.class));
        } else if (id == R.id.nav_products) {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_HOME));
        } else if (id == R.id.nav_share) {
            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Playstore link");
            i.putExtra(android.content.Intent.EXTRA_TEXT, ": Coming soon...");
            startActivity(Intent.createChooser(i,"Share via"));
        }  else if (id == R.id.nav_cart) {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        }
//        } else if (id == R.id.nav_wishlist) {
//            startActivity(new Intent(HomeActivity.this,WishList.class));
//        }
        else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(HomeActivity.this,ContactUs.class));
        }else if (id == R.id.nav_about_us) {
            startActivity(new Intent(HomeActivity.this,AboutUs.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                                    productDataList.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                    ,totalCost,description,"D4W3KdpwFYMc.jpg",stateId,city_id,categoryId,sts));
                                    RecentProductAdapter radapter = new RecentProductAdapter(productDataList);
                                    List_view.setAdapter(radapter);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(HomeActivity.this, ""+stsMessage,
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
                Toast.makeText(HomeActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    bestDataList.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,"D4W3KdpwFYMc.jpg",stateId,city_id,categoryId,sts));
                                    RecentProductAdapter radapter = new RecentProductAdapter(bestDataList);
                                    List_view1.setAdapter(radapter);
                                }
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(HomeActivity.this, ""+stsMessage,
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
                Toast.makeText(HomeActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void onCategoryClick(View v)
    {
        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class));
    }

}
