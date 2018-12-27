package com.app.android.deal.club.sampledesigns.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.deal.club.sampledesigns.Activities.CartActivity;
import com.app.android.deal.club.sampledesigns.Activities.ProductDetails;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    public static ArrayList<RecentPrdocutData> obj_arr=new ArrayList<>();

    public ProductAdapter(ArrayList<RecentPrdocutData> objs) {
        this.obj_arr =objs;
    }

    SessionManager session = new SessionManager();

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View contentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_lyt_horizontal,parent,false);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new ProductAdapter.MyViewHolder(contentView);
    }



    @Override
    public void onBindViewHolder(final ProductAdapter.MyViewHolder holder, final int position) {
        try {
            holder.productAddress.setText(obj_arr.get(position).get_productName());
            if(obj_arr.get(position).get_offerStatus().equalsIgnoreCase("1"))
            {
                holder.productCost.setText(getIndianRupee(obj_arr.get(position).get_offerTotal()));
            }
            else
            {
                holder.productCost.setText(getIndianRupee(obj_arr.get(position).get_totalCost()));
            }



            Glide.with(holder.productImage.getContext()).load(Constants.APP_BASE_URL+obj_arr.get(position).get_image()).into(holder.productImage);
            holder.lyt_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent in = new Intent (holder.lyt_product.getContext(), ProductDetails.class);
                    in.putExtra(Constants.PRODUCT_ID,obj_arr.get(position).get_productId());
                    in.putExtra(Constants.PRODUCT_NAME,obj_arr.get(position).get_productName());
                    in.putExtra(Constants.PRODUCT_TYPE,obj_arr.get(position).get_productType());
                    in.putExtra(Constants.PRODUCT_SFT,obj_arr.get(position).get_productSqrt());
                    in.putExtra(Constants.PRODUCT_SIZE,obj_arr.get(position).get_productSize());
                    in.putExtra(Constants.PRINTING_COST,obj_arr.get(position).get_printingCost());
                    in.putExtra(Constants.MOUNTING_COST,obj_arr.get(position).get_mountingCost());
                    in.putExtra(Constants.TOTAL_COST,obj_arr.get(position).get_totalCost());
                    in.putExtra(Constants.PRODUCT_DESCRIPTION,obj_arr.get(position).get_description());
                    in.putExtra(Constants.PRODUCT_IMAGE,obj_arr.get(position).get_image());
                    in.putExtra(Constants.STATE_ID,obj_arr.get(position).get_stateId());
                    in.putExtra(Constants.CITY_ID,obj_arr.get(position).get_cityId());
                    in.putExtra(Constants.CATEGORY_ID,obj_arr.get(position).get_categoryId());
                    in.putExtra(Constants.PRODUCT_STATUS,obj_arr.get(position).get_status());
                    in.putExtra(Constants.OFFER_TYPE,obj_arr.get(position).get_offerType());
                    in.putExtra(Constants.OFFER_QUANTITY,obj_arr.get(position).get_offerQuantity());
                    in.putExtra(Constants.OFFER_STATUS,obj_arr.get(position).get_offerStatus());
                    in.putExtra(Constants.OFFER_NAME,obj_arr.get(position).get_offerName());
                    in.putExtra(Constants.OFFER_TOTAL,obj_arr.get(position).get_offerTotal());
                    holder.lyt_product.getContext().startActivity(in);
                }
            });

            holder.readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(holder.readMore.getContext());
                    builder1.setTitle("Remove from Cart");
                    builder1.setMessage("Are you sure want to Remove?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Remove",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    removeCart(holder.readMore.getContext(),position);
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
            });
        }catch (Exception ex)
        {
            Log.e("ERROR_RECENT",""+ex.getMessage());
            ex.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return obj_arr.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView productAddress,productCost, readMore;
        ImageView productImage;
        LinearLayout lyt_product;
        public MyViewHolder(View itemView) {
            super(itemView);
            productAddress = itemView.findViewById(R.id.r_address);
            productCost = itemView.findViewById(R.id.r_cost);
            productImage = itemView.findViewById(R.id.r_img);
            readMore = itemView.findViewById(R.id.r_read_more);
            lyt_product = itemView.findViewById(R.id.lyt_container);
        }
    }
    public static String getIndianRupee(String value) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(value));
    }

    private void removeCart(final Context cntx, final int position)
    {
        RequestQueue queue = Volley.newRequestQueue(cntx);
        String url = "http://adinn.candyrestaurant.com/api/remove-cart";
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
                                Toast.makeText(cntx, "Product removed from Cart...", Toast.LENGTH_SHORT).show();
                                obj_arr.remove(position);
                                notifyDataSetChanged();
                                CartActivity. txtTotalCost.setText(cntx.getResources().getString(R.string.Rs)+" "+formatDecimal(getTotalAmount()));
                            }
                            else if (loginStatus.equalsIgnoreCase(Constants.RESULT_FAILED))
                            {
                                Toast.makeText(cntx, ""+stsMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cntx, "Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", obj_arr.get(position).get_productId());
                params.put("user_id", session.getPreferences(cntx,Constants.CURRENT_USER_ID));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public static String formatDecimal(String value) {
        DecimalFormat df = new DecimalFormat("#,##,##,##0.00");
        return df.format(Double.valueOf(value));
    }

    public String getTotalAmount()
    {
        String amount = "";
        double amountToCalculate = 0.0;
        for (int i = 0 ; i < obj_arr.size(); i++)
        {
            amountToCalculate = amountToCalculate + Double.valueOf(obj_arr.get(i).get_totalCost());
        }
        amount = String.valueOf(amountToCalculate);
        return  amount;
    }
}
