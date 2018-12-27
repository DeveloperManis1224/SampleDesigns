package com.app.android.deal.club.sampledesigns.Activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.Adapters.ExpandableListAdapter;
import com.app.android.deal.club.sampledesigns.Adapters.PlaceAdapter;
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DataModel;
import com.app.android.deal.club.sampledesigns.DataModels.MenuModel;
import com.app.android.deal.club.sampledesigns.DataModels.PlaceModel;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.SplashScreen;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<DataModel> dataModal=new ArrayList<DataModel>();
    ArrayList<RecentPrdocutData> productDataList = new ArrayList<>();
    ArrayList<RecentPrdocutData> bestDataList = new ArrayList<>();

    ArrayList<PlaceModel> stateList = new ArrayList<>();
    ArrayList<PlaceModel> cityList = new ArrayList<>();

    RecyclerView List_view, category_listview, menu_listview;
    RecyclerView List_view1, stateListview, cityListview;
    SessionManager session;
    SliderLayout sliderLayout;
    TextView mRecentBanner, mBestBanner;
    ProgressDialog progressDialog;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(Constants.isOnline(HomeActivity.this))
        {
            Init();
        }
        else{
            new AwesomeSuccessDialog(HomeActivity.this)
                    .setTitle("No Internet Available!")
                    .setMessage("There is no internet connection.please turn on your internet connection.")
                    .setColoredCircle(R.color.colorAccent)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText("Refresh")
                    .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                    .setPositiveButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            Intent in = new Intent(HomeActivity.this, SplashScreen.class);
                            startActivity(in);
                            finish();
                        }
                    })
                    .show();
        }

    }

    private void Init()
    {
        session = new SessionManager();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        expandableListView = findViewById(R.id.expandableListView);
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
        stateListview = findViewById(R.id.state_list);
        cityListview = findViewById(R.id.city_list);

        stateListview.setLayoutManager(new GridLayoutManager(this, 3));
        cityListview.setLayoutManager(new GridLayoutManager(this, 3));

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
        prepareMenuData();

        getStates();
        //getCities();
        populateExpandableList();

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
        if (id == R.id.cart_btn_bar) {
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this,CartActivity.class));
            return true;
        }
        if (id == R.id.order_btn_bar) {
            startActivity(new Intent(HomeActivity.this,OrderDetails.class));
//            session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
//            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//            Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
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
        }else if (id == R.id.nav_orders) {
            startActivity(new Intent(HomeActivity.this,OrderDetails.class));
        } else if (id == R.id.nav_products) {

            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class));
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
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/recent-product";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //offer_type, offer, offer_status, offer_name
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
                                    Log.e("RESPONSE-HOME_Best",""+type);
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

                                    productDataList.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                    ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName, offerTotal));
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                    Log.e("RESPONSE-HOME_Best",""+type);
                                    String printingCost = resObject.getString(Constants.PRINTING_COST);
                                    String mountingCost = resObject.getString(Constants.MOUNTING_COST);
                                    String totalCost = resObject.getString(Constants.TOTAL_COST);
                                    String image = resObject.getString(Constants.PRODUCT_IMAGE);
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
                                    bestDataList.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,image,stateId,city_id,categoryId,sts,offerType,offerQuantity,offerStatus,offerName,offerTotal));
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void onCategoryClick(View v)
    {
        if(v.getId() == R.id.cat1)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"1"));
        }
        if(v.getId() == R.id.cat2)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"2"));
        }
        if(v.getId() == R.id.cat3)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"3"));
        }
        if(v.getId() == R.id.cat4)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"4"));
        }
        if(v.getId() == R.id.cat5)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"5"));
        }
        if(v.getId() == R.id.cat6)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"6"));
        }
        if(v.getId() == R.id.cat7)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"7"));
        }
        if(v.getId() == R.id.cat8)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"8"));
        }
        if(v.getId() == R.id.cat9)
        {
            startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                    putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"9"));
        }


    }
    public void onSearchClick(View v)
    {
        startActivity(new Intent(HomeActivity.this,CategoryProductView.class).
                putExtra(Constants.PAGE_FROM,Constants.PAGE_SEARCH));
    }


    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel(getResources().getDrawable(R.drawable.icon_home),"Home", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.icon_services),"Services", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.category_icon),"Category", true, true, ""); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel("1. HOARDINGS / BILLBOARDS", false, false, "1");
        childModelsList.add(childModel);
        childModel = new MenuModel("2. UNIPOLES / MONOPOLES", false, false, "2");
        childModelsList.add(childModel);
        childModel = new MenuModel("3. CENTRALMEDIAN / POLE KIOSKS", false, false, "3");
        childModelsList.add(childModel);
        childModel = new MenuModel("4. BUS SHELTERS / BUS BAYS", false, false, "4");
        childModelsList.add(childModel);
        childModel = new MenuModel("5. ARCHES / GANTRIES / PANELS", false, false, "5");
        childModelsList.add(childModel);
        childModel = new MenuModel("6. FOOT OVER BRIDGES", false, false, "6");
        childModelsList.add(childModel);
        childModel = new MenuModel("7. TRAFFIC SIGNS / TRAFFIC SHELTERS", false, false, "7");
        childModelsList.add(childModel);
        childModel = new MenuModel("8. AUTO / CAB / BUS / TRAIN", false, false, "8");
        childModelsList.add(childModel);
        childModel = new MenuModel("9. OTHER OOH", false, false, "9");
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            Log.d("API123","here");
            childList.put(menuModel, childModelsList);
        }
        childModelsList = new ArrayList<>();

        menuModel = new MenuModel(getResources().getDrawable(R.drawable.icon_products),"Products", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.add_cart),"My Cart", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.shop_cart),"My Orders", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.icon_share),"Share", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.contact_icon),"Contact Us", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        menuModel = new MenuModel(getResources().getDrawable(R.drawable.icon_about),"About Us", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);

        if(session.getPreferences(HomeActivity.this,Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGIN)) {
            menuModel = new MenuModel(getResources().getDrawable(R.drawable.logout_icon), "Logout", true, true, ""); //Menu of Python Tutorials
            headerList.add(menuModel);
        }
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    //Toast.makeText(HomeActivity.this, ""+headerList.get(groupPosition).getName(), Toast.LENGTH_SHORT).show();

                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Home"))
                    {
                        startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Services"))
                    {
                        startActivity(new Intent(HomeActivity.this,Services.class));
                    }

                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Products"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("My Cart"))
                    {
                        startActivity(new Intent(HomeActivity.this,CartActivity.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("My Orders"))
                    {
                        startActivity(new Intent(HomeActivity.this,OrderDetails.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Share"))
                    {
                        Intent i=new Intent(android.content.Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Playstore link");
                        i.putExtra(android.content.Intent.EXTRA_TEXT, ": Coming soon...");
                        startActivity(Intent.createChooser(i,"Share via"));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Contact Us"))
                    {
                        startActivity(new Intent(HomeActivity.this,ContactUs.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("About Us"))
                    {
                        startActivity(new Intent(HomeActivity.this,AboutUs.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Logout"))
                    {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                        builder1.setTitle("Logout");
                        builder1.setMessage("Are you sure want to Logout?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Logout",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Toast.makeText(HomeActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                                        session.setPreferences(HomeActivity.this,Constants.LOGIN_STATUS,Constants.LOGOUT);
                                        startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                                    }
                                });
                        builder1.setNegativeButton(
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }

//                    if (!headerList.get(groupPosition).hasChildren) {
//                        Toast.makeText(HomeActivity.this, ""+headerList.get(groupPosition).getName(), Toast.LENGTH_SHORT).show();
//                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                   // Toast.makeText(HomeActivity.this, "cvcvcvc", Toast.LENGTH_SHORT).show();

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("1"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class).
                                putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"1"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("2"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"2"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("3"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"3"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("4"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"4"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("5"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"5"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("6"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"6"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("7"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"7"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("8"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"8"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("9"))
                    {
                        startActivity(new Intent(HomeActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"9"));
                    }
//                    if (model.getuId().length() > 0) {
//                            onBackPressed();
//                    }
                }
                return false;
            }
        });
    }


    private void getStates()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/state";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("RESPONSE-HOME_Best",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("states");
                                for(int i = 0; i < jsonArray.length(); i++ )
                                {
                                    JSONObject resObject = jsonArray.getJSONObject(i);
                                    String uId = resObject.getString(Constants.PRODUCT_ID);
                                    String stateName = resObject.getString("state");
                                    stateList.add(new PlaceModel(uId,stateName));
                                    PlaceAdapter radapter = new PlaceAdapter(stateList);
                                    stateListview.setAdapter(radapter);
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
                progressDialog.dismiss();
                Log.e("RESPONSE-HOME_Recent",""+error.getMessage());
                Toast.makeText(HomeActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }
//    private void getCities()
//    {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://adinn.candyrestaurant.com/api/cities";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("RESPONSE-HOME_Best",""+response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
//                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
//                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
//                            {
//                                JSONArray jsonArray = jsonObject.getJSONArray("products");
//                                for(int i = 0; i < jsonArray.length(); i++ )
//                                {
//                                    JSONObject resObject = jsonArray.getJSONObject(i);
//                                    String uId = resObject.getString(Constants.PRODUCT_ID);
//                                    String cityName = resObject.getString("city");
//                                    cityList.add(new PlaceModel(uId,cityName));
//                                    PlaceAdapter radapter = new PlaceAdapter(stateList);
//                                    cityListview.setAdapter(radapter);
//                                }
//                            }
//                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
//                            {
//                                Toast.makeText(HomeActivity.this, ""+stsMessage,
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            Log.e("RESPONSE-HOME_BError",""+e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("RESPONSE-HOME_Recent",""+error.getMessage());
//                Toast.makeText(HomeActivity.this, "" + error, Toast.LENGTH_SHORT).show();
//            }
//        });
//        queue.add(stringRequest);
//    }




}
