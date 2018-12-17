package com.app.android.deal.club.sampledesigns.Activities;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.app.android.deal.club.sampledesigns.Adapters.RecentProductAdapter;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrdouctActivity extends AppCompatActivity {
    private Spinner categoryListSpinner,CitySpinner, typeSpinner;
    ArrayList<String> catSpinList = new ArrayList<>();
    ArrayList<String> catIdList = new ArrayList<>();

    ArrayList<String> typeSpinList = new ArrayList<>();
    ArrayList<String> typeIdList = new ArrayList<>();

    ArrayList<String> citySpinList = new ArrayList<>();
    ArrayList<String> cityIdList = new ArrayList<>();

    ArrayList<RecentPrdocutData> productData = new ArrayList<>();
    RecyclerView list_view_product;
    TextView hintSearch;
//    LayoutInflater inflater;
//    View layout;

    int positionCat = 0;
    int positionCity = 0 ;
    int positionType = 0;

    Button filterBtn ;
    Button sortBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdouct);






        list_view_product = findViewById(R.id.product_list_view);
        hintSearch = findViewById(R.id.hint_search);

        findViewById(R.id.filter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategorySpinnerValues();
                getCitySpinnerValues();
                getTypeSpinnerValues();
                onFilterClick();

            }
        });
        findViewById(R.id.sort_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSortClick();
            }
        });

        list_view_product.setLayoutManager(new GridLayoutManager(this, 2));
        catSpinList.add("All");
        catIdList.add("11111");

        typeSpinList.add("All");
        typeIdList.add("11111");

        citySpinList.add("All");
        cityIdList.add("11111");





        getCategoryDetails("","","","","","");

    }



    public  void onFilterClick()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.layout_root));
        categoryListSpinner = (Spinner) layout.findViewById(R.id.spin_category);
        typeSpinner= (Spinner) layout.findViewById(R.id.spin_type);
        CitySpinner = (Spinner) layout.findViewById(R.id.spin_city);
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
                getCategoryDetails(null,null,null
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(PrdouctActivity.this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout11 = inflater.inflate(R.layout.dialog_sort_lyt, (ViewGroup) findViewById(R.id.layout_root));

        final TextView lowToHigh = layout11.findViewById(R.id.low_to_high);

        final TextView highToLow = layout11.findViewById(R.id.high_to_low);


        lowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoryDetails("","","","","","0");
            }
        });
        highToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoryDetails("","","","","","1");
            }
        });

        builder.setView(layout11);
        dialog.setCancelable(true);
        dialog.show();

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

    private void getCitySpinnerValues() {
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




    private void getCategoryDetails(final String categoryId,final String type_id,
                                    final String cityId,final String max, final String min, final String sort) {
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
//                                    JSONArray jsry = resObject.getJSONArray(Constants.PRODUCT_IMAGES);
//                                    String image = jsry.getJSONObject(0).getString(Constants.PRODUCT_IMAGES);
                                    String stateId = resObject.getString(Constants.STATE_ID);
                                    String city_id = resObject.getString(Constants.CITY_ID);
                                    String categoryId = resObject.getString(Constants.CATEGORY_ID);
                                    String sts = resObject.getString(Constants.PRODUCT_STATUS);
                                    productData.add(new RecentPrdocutData(uId,productName,price,size,sft,type,printingCost,mountingCost
                                            ,totalCost,description,"D4W3KdpwFYMc.jpg",stateId,city_id,categoryId,sts));
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
//                params.put("category_id", catIdList.get(positionCat));
//                params.put("sort_type",sort);
//                params.put("type_id",typeIdList.get(positionType));
//                params.put("city_id",cityIdList.get(positionCity));
//                params.put("max_price",max);
//                params.put("min_price",min);
                params.put("category_id", categoryId);
                params.put("sort_type",sort);
                params.put("type_id",type_id);
                params.put("city_id",cityId);
                params.put("max_price",max);
                params.put("min_price",min);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
