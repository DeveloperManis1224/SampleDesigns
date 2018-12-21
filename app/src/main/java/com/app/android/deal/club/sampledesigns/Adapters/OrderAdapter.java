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

import com.app.android.deal.club.sampledesigns.Activities.InvoiceWebView;
import com.app.android.deal.club.sampledesigns.Activities.ProductDetails;
import com.app.android.deal.club.sampledesigns.DataModels.OrderData;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    public static ArrayList<OrderData> obj_arr=new ArrayList<>();

    public OrderAdapter(ArrayList<OrderData> objs) {
        this.obj_arr =objs;
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View contentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_lyt,
                parent,false);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new OrderAdapter.MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.MyViewHolder holder, final int position) {
        holder.orderName.setText(obj_arr.get(position).getName());
        holder.serialNumber.setText(""+obj_arr.get(position).getSerialNumber());
        if(obj_arr.get(position).getStatus().equalsIgnoreCase("0"))
        {
            holder.ordeStatus.setText("Pending");
        }
        else
        if(obj_arr.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.ordeStatus.setText("Confirmed");
        }
        else
        if(obj_arr.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.ordeStatus.setText("Processing...");
        }
        else
        if(obj_arr.get(position).getStatus().equalsIgnoreCase("3"))
        {
            holder.ordeStatus.setText("On Hold");
        }
        else
        if(obj_arr.get(position).getStatus().equalsIgnoreCase("4"))
        {
            holder.ordeStatus.setText("Cancelled");
        }
        else
        if(obj_arr.get(position).getStatus().equalsIgnoreCase("5"))
        {
            holder.ordeStatus.setText("Refunded");
        }

        holder.serialNumber.setText(""+obj_arr.get(position).getSerialNumber());
        Glide.with(holder.productImage.getContext()).load(Constants.APP_BASE_URL+obj_arr.get(position).getImage()).into(holder.productImage);
        holder.lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), InvoiceWebView.class)
                        .putExtra(Constants.INVOICE_URL,
                                "http://adinn.candyrestaurant.com/order/invoice/"+
                                        obj_arr.get(position).getOrderId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return obj_arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView orderName,ordeStatus,serialNumber;
        ImageView productImage;
        LinearLayout lyt;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderName = itemView.findViewById(R.id.order_name);
            ordeStatus = itemView.findViewById(R.id.order_sts);
            serialNumber = itemView.findViewById(R.id.order_serial_num);
            productImage = itemView.findViewById(R.id.order_img);
            lyt = itemView.findViewById(R.id.lyt_container_order);
        }
    }
}
