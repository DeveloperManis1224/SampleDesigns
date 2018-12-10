package com.app.android.deal.club.sampledesigns.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.android.deal.club.sampledesigns.Activities.ProductDetails;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    public static ArrayList<RecentPrdocutData> obj_arr=new ArrayList<>();

    public ProductAdapter(ArrayList<RecentPrdocutData> objs) {
        this.obj_arr =objs;
    }

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
            holder.productCost.setText(getIndianRupee(obj_arr.get(position).get_totalCost()));
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
                    holder.lyt_product.getContext().startActivity(in);
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
        TextView productAddress,productCost;
        ImageView productImage;
        LinearLayout lyt_product;
        public MyViewHolder(View itemView) {
            super(itemView);
            productAddress = itemView.findViewById(R.id.r_address);
            productCost = itemView.findViewById(R.id.r_cost);
            productImage = itemView.findViewById(R.id.r_img);
            lyt_product = itemView.findViewById(R.id.lyt_container);
        }
    }
    public static String getIndianRupee(String value) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(value));
    }
}
