package com.app.android.deal.club.sampledesigns.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
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
import com.app.android.deal.club.sampledesigns.Adapters.ExpandableListAdapter;
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DataModels.MenuModel;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrdouctActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener{
    private Spinner categoryListSpinner,CitySpinner, typeSpinner, stateSpinner;
    ArrayList<String> catSpinList = new ArrayList<>();
    ArrayList<String> catIdList = new ArrayList<>();

    ArrayList<String> typeSpinList = new ArrayList<>();
    ArrayList<String> typeIdList = new ArrayList<>();

    ArrayList<String> stateSpinList = new ArrayList<>();
    ArrayList<String> stateIdList = new ArrayList<>();

    ArrayList<String> citySpinList = new ArrayList<>();
    ArrayList<String> cityIdList = new ArrayList<>();

    ArrayList<RecentPrdocutData> productData = new ArrayList<>();
    RecyclerView list_view_product;
    TextView hintSearch;
    SessionManager session;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();


//    LayoutInflater inflater;
//    View layout;

    int positionCat = 0;
    int positionCity = 0 ;
    int positionType = 0;
    int positionState = 0;

    Button filterBtn ;
    Button sortBtn;

    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdouct);

        session = new SessionManager();

        list_view_product.setLayoutManager(new GridLayoutManager(this, 2));
        catSpinList.add("All");
        catIdList.add("0");

        typeSpinList.add("All");
        typeIdList.add("0");

        citySpinList.add("All");
        cityIdList.add("0");

        stateSpinList.add("All");
        stateIdList.add("0");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.as_above);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)

                    {
                        switch (menuItem.getItemId())
                        {


                        }
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        list_view_product = findViewById(R.id.product_list_view);
        hintSearch = findViewById(R.id.hint_search);

        findViewById(R.id.filter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategorySpinnerValues();
                getCitySpinnerValues();
                getTypeSpinnerValues();
                getStateSpinnerValues();
                onFilterClick();

            }
        });
        findViewById(R.id.sort_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortClick();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_user_name);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_email);
        if(session.getPreferences(PrdouctActivity.this, Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGOUT))
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
        else if(session.getPreferences(PrdouctActivity.this, Constants.LOGIN_STATUS).equalsIgnoreCase(Constants.LOGIN))
        {
            navUsername.setText("Welcome "+session.getPreferences(PrdouctActivity.this,Constants.CURRENT_USER_NAME)+",");
            navEmail.setVisibility(View.VISIBLE);
            navEmail.setText(session.getPreferences(PrdouctActivity.this,Constants.CURRENT_USER_EMAIL));
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

        //getCategoryDetails("","","","","","","");
try {
    if (getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_MENU)) {
        getCategoryDetails(getIntent().getExtras().getString(Constants.CATEGORY_ID), "0",
                "0", "0", "120000", "1000", "");
    } else if (getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_HOME)) {
        getCategoryDetails(getIntent().getExtras().getString(Constants.CATEGORY_ID), "0",
                "0", "0", "120000", "1000", "");
    } else if (getIntent().getExtras().getString(Constants.PAGE_FROM).equalsIgnoreCase(Constants.PAGE_PLACES)) {
        getCategoryDetails("0",
                "0", getIntent().getExtras().getString(Constants.STATE_ID), "0", "120000", "1000", "");
    } else {
        getCategoryDetails("", "", "", "", "", "", "");
    }
}catch (NullPointerException ex)
{
    ex.printStackTrace();
    getCategoryDetails("0", "0", "0", "0", "120000", "1000", "");
}
        //getCategoryDetails("","","","","1000","500000","");

    }



    public  void onFilterClick()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.layout_root));
        categoryListSpinner = (Spinner) layout.findViewById(R.id.spin_category);
        typeSpinner= (Spinner) layout.findViewById(R.id.spin_type);
        CitySpinner = (Spinner) layout.findViewById(R.id.spin_city);
        stateSpinner = (Spinner) layout.findViewById(R.id.spin_state);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionState = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCity = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCat = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final TextView minValue1 = layout.findViewById(R.id.min_val);

        final TextView maxValue1 = layout.findViewById(R.id.max_val);

        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(this);
        rangeSeekBar.setRangeValues(10000, 120000);
        rangeSeekBar.setSelectedMinValue(10000);
        rangeSeekBar.setSelectedMaxValue(120000);
        FrameLayout layout1 = (FrameLayout) layout.findViewById(R.id.seekbar_placeholder);
        layout1.addView(rangeSeekBar);
        RangeSeekBar rangeSeekBarTextColorWithCode = (RangeSeekBar) layout.findViewById(R.id.rangeSeekBarTextColorWithCode);
        rangeSeekBarTextColorWithCode.setTextAboveThumbsColorResource(android.R.color.holo_red_dark);
        rangeSeekBarTextColorWithCode.setRangeValues(1000,120000);

        Log.e("RANGE",""+rangeSeekBarTextColorWithCode.getSelectedMinValue()+"//"+rangeSeekBarTextColorWithCode.getSelectedMaxValue());
        rangeSeekBarTextColorWithCode.getSelectedMinValue();
        rangeSeekBarTextColorWithCode.getSelectedMaxValue();

        rangeSeekBarTextColorWithCode.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                minValue1.setText(minValue.toString());
                maxValue1.setText(maxValue.toString());
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(PrdouctActivity.this);
        builder.setView(layout);
        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.v("VALUESFILTER",""+catIdList.get(positionCat)+"///"+typeIdList.get(positionType)+"///"+stateIdList.get(positionState)+"///"+cityIdList.get(positionCity)
                        +"///"+maxValue1.getText().toString()+"///"+minValue1.getText().toString());
                getCategoryDetails(catIdList.get(positionCat),typeIdList.get(positionType),stateIdList.get(positionState),cityIdList.get(positionCity)
                        ,maxValue1.getText().toString(),minValue1.getText().toString(),"");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

    }
    public void onSortClick() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout11 = inflater.inflate(R.layout.dialog_sort_lyt, (ViewGroup) findViewById(R.id.layout_root));


        final RadioButton lowToHigh = layout11.findViewById(R.id.low_to_high);

        final RadioButton highToLow = layout11.findViewById(R.id.high_to_low);

        final AlertDialog.Builder builder = new AlertDialog.Builder(PrdouctActivity.this);
        builder.setView(layout11);
        builder.setPositiveButton("Sort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(lowToHigh.isChecked())
                {
                    getCategoryDetails("","","","","","","0");
                }
                else
                {
                    getCategoryDetails("","","","","","","1");
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void getCategorySpinnerValues() {
        catSpinList.clear();
        catIdList.clear();
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
                                    catSpinList.add(id+" . "+categoryName);
                                    catIdList.add(id);
                                    ArrayAdapter aa = new ArrayAdapter(PrdouctActivity.this,
                                            android.R.layout.simple_spinner_item,catSpinList);
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

    private void getTypeSpinnerValues() {
        typeSpinList.clear();
        typeIdList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/product-type";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("types");
                                for(int i = 0; i<jsonArray.length();i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String categoryName = object.getString("type");
                                    typeSpinList.add(id+" . "+categoryName);
                                    typeIdList.add(id);
                                    ArrayAdapter aa = new ArrayAdapter(PrdouctActivity.this,
                                            android.R.layout.simple_spinner_item,typeSpinList);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    typeSpinner.setAdapter(aa);
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

    private void getStateSpinnerValues() {
        stateIdList.clear();
        stateSpinList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/state";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("states");
                                for(int i = 0; i<jsonArray.length();i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String categoryName = object.getString("state");
                                    stateSpinList.add(id+" . "+categoryName);
                                    stateIdList.add(id);
                                    ArrayAdapter aa = new ArrayAdapter(PrdouctActivity.this,
                                            android.R.layout.simple_spinner_item,stateSpinList);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    stateSpinner.setAdapter(aa);
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
                        startActivity(new Intent(PrdouctActivity.this,HomeActivity.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Services"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,Services.class));
                    }

                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("Products"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("My Cart"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,CartActivity.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("My Orders"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,OrderDetails.class));
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
                        startActivity(new Intent(PrdouctActivity.this,ContactUs.class));
                    }
                    else
                    if(headerList.get(groupPosition).getName().equalsIgnoreCase("About Us"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,AboutUs.class));
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

                    Toast.makeText(PrdouctActivity.this, "cvcvcvc", Toast.LENGTH_SHORT).show();

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("1"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class).
                                putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"1"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("2"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"2"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("3"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"3"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("4"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"4"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("5"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"5"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("6"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"6"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("7"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"7"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("8"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
                                .putExtra(Constants.PAGE_FROM,Constants.PAGE_MENU).putExtra(Constants.CATEGORY_ID,"8"));
                    }
                    else

                    if(childList.get(headerList.get(groupPosition)).get(childPosition).getuId().equalsIgnoreCase("9"))
                    {
                        startActivity(new Intent(PrdouctActivity.this,PrdouctActivity.class)
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




    private void getCitySpinnerValues() {
        cityIdList.clear();
        citySpinList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://adinn.candyrestaurant.com/api/city";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("cities");
                                for(int i = 0; i<jsonArray.length();i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String categoryName = object.getString("city");
                                    citySpinList.add(id+" . "+categoryName);
                                    cityIdList.add(id);
                                    ArrayAdapter aa = new ArrayAdapter(PrdouctActivity.this,
                                            android.R.layout.simple_spinner_item,citySpinList);
                                    CitySpinner.setAdapter(aa);
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



    private void getCategoryDetails(final String categoryId,final String type_id,final String stateId,
                                    final String cityId,final String max, final String min, final String sort) {

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
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String loginStatus = jsonObject.getString("status");//LOGIN = "1";
                            String stsMessage = jsonObject.getString("message"); //LOGOUT = "0";
                            if(loginStatus.equalsIgnoreCase(Constants.RESULT_SUCCESS))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                if(jsonArray.length()==0)
                                {
                                    Toast.makeText(PrdouctActivity.this, "No Products", Toast.LENGTH_SHORT).show();
                                }else {
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
                                        String image = resObject.getString(Constants.PRODUCT_IMAGE);
//                                    JSONArray jsry = resObject.getJSONArray(Constants.PRODUCT_IMAGES);
//                                    String image = jsry.getJSONObject(0).getString(Constants.PRODUCT_IMAGES);
                                        String stateId = resObject.getString(Constants.STATE_ID);
                                        String city_id = resObject.getString(Constants.CITY_ID);
                                        String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                        String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                        productData.add(new RecentPrdocutData(uId, productName, price, size, sft, type, printingCost, mountingCost
                                                , totalCost, description, image, stateId, city_id, categoryId, sts));
                                        RecentProductAdapter radapter = new RecentProductAdapter(productData);
                                        radapter.notifyDataSetChanged();
                                        list_view_product.setAdapter(radapter);
                                    }
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
//                params.put("category_id", catIdList.get(positionCat));
//                params.put("sort_type",sort);
//                params.put("type_id",typeIdList.get(positionType));
//                params.put("city_id",cityIdList.get(positionCity));
//                params.put("max_price",max);
//                params.put("min_price",min);
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
        queue.add(stringRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
