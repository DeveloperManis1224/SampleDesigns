package com.app.android.deal.club.sampledesigns.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecentProductAdapter  extends RecyclerView.Adapter<RecentProductAdapter.MyViewHolder> {
    public static ArrayList<RecentPrdocutData> obj_arr=new ArrayList<>();

    public RecentProductAdapter(ArrayList<RecentPrdocutData> objs) {
        this.obj_arr =objs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View contentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_lyt_card,parent,false);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            holder.productAddress.setText(obj_arr.get(position).get_productName());
            holder.productCost.setText(getIndianRupee(obj_arr.get(position).get_totalCost()));
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

        public MyViewHolder(View itemView) {
            super(itemView);
           productAddress = itemView.findViewById(R.id.r_address);
           productCost = itemView.findViewById(R.id.r_cost);
           productImage = itemView.findViewById(R.id.r_img);
        }
    }
    public static String getIndianRupee(String value) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(value));
    }
}
