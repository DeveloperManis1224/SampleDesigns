package com.app.adinn.outdoors.square_brace.adinn_outdoors.Adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adinn.outdoors.square_brace.adinn_outdoors.Activities.ProductDetails;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels.NotificationData;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.DataModels.RecentPrdocutData;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.R;
import com.app.adinn.outdoors.square_brace.adinn_outdoors.Utils.Constants;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    public static ArrayList<NotificationData> obj_arr=new ArrayList<>();

    public NotificationAdapter(ArrayList<NotificationData> objs) {
        this.obj_arr =objs;
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View contentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_product,parent,false);

        return new NotificationAdapter.MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.MyViewHolder holder, final int position) {
        try {
            if(obj_arr.get(position).get_productOffer().equalsIgnoreCase(Constants.PRODUCT_OFFER_AVAILABLE))
            {
                holder.txtOffermsg.setText(obj_arr.get(position).get_offerMessage());
                holder.productAddress.setText(obj_arr.get(position).get_productName());
                if(obj_arr.get(position).get_offerStatus().equalsIgnoreCase("1"))
                {
                    holder.productCostDashed.setText(getIndianRupee(obj_arr.get(position).get_totalCost()));
                    holder.productCostDashed.setPaintFlags(holder.productCostDashed.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.productCost.setText(getIndianRupee(obj_arr.get(position).get_offerTotal()));
                    holder.productCostDashed.setVisibility(View.VISIBLE);
                    holder.textLabelOffer.setVisibility(View.VISIBLE);

                }
                else
                {
                    holder.productCost.setText(getIndianRupee(obj_arr.get(position).get_totalCost()));
                }
                Glide.with(holder.productImage.getContext()).load(Constants.APP_BASE_URL+obj_arr.get(position).get_image()).into(holder.productImage);
                holder.lyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent (holder.lyt.getContext(), ProductDetails.class);
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
                        holder.lyt.getContext().startActivity(in);
                    }
                });
            }
            else
            {

                holder.iconImg.setImageDrawable(holder.iconImg.getResources().getDrawable(R.drawable.icon_notification));
                holder.txtOffermsg.setText(obj_arr.get(position).get_offerMessage());
                holder.threeDotLyt.setVisibility(View.GONE);
                holder.rupeesLyt.setVisibility(View.GONE);
                holder.imgLyt.setVisibility(View.GONE);
                holder.productAddress.setVisibility(View.GONE);
            }

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
        TextView productAddress,productCost, productCostDashed, textLabelOffer;
        ImageView productImage ,iconImg;
        LinearLayout lyt ,rupeesLyt, threeDotLyt;
        RelativeLayout imgLyt;
        TextView txtOffermsg ;


        public MyViewHolder(View itemView) {
            super(itemView);
            productCostDashed = itemView.findViewById(R.id.r_cost_dashed);
            productAddress = itemView.findViewById(R.id.r_address);
            productCost = itemView.findViewById(R.id.r_cost);
            productImage = itemView.findViewById(R.id.r_img);
            lyt = itemView.findViewById(R.id.lyt_card_linear);
            textLabelOffer = itemView.findViewById(R.id.img_offer_label);
            txtOffermsg = itemView.findViewById(R.id.txt_messages);
            imgLyt = itemView.findViewById(R.id.rl_photo);
            rupeesLyt = itemView.findViewById(R.id.rupeess_lyt);
            threeDotLyt = itemView.findViewById(R.id.menu_three_dot);
            iconImg = itemView .findViewById(R.id.icon_notification_img);

        }
    }
    public static String getIndianRupee(String value) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(value));
    }
}


