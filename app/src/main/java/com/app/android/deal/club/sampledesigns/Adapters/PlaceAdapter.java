package com.app.android.deal.club.sampledesigns.Adapters;

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

import com.app.android.deal.club.sampledesigns.Activities.HomeActivity;
import com.app.android.deal.club.sampledesigns.Activities.PrdouctActivity;
import com.app.android.deal.club.sampledesigns.Activities.ProductDetails;
import com.app.android.deal.club.sampledesigns.DataModels.PlaceModel;
import com.app.android.deal.club.sampledesigns.DataModels.RecentPrdocutData;
import com.app.android.deal.club.sampledesigns.R;
import com.app.android.deal.club.sampledesigns.Utils.Constants;
import com.app.android.deal.club.sampledesigns.Utils.SessionManager;

import java.util.ArrayList;

public class PlaceAdapter  extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder>  {

    public static ArrayList<PlaceModel> obj_arr=new ArrayList<>();

    public PlaceAdapter(ArrayList<PlaceModel> objs) {
        this.obj_arr =objs;
    }

    SessionManager session = new SessionManager();

    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View contentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.place_name_lyt,parent,false);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        return new PlaceAdapter.MyViewHolder(contentView);
    }



    @Override
    public void onBindViewHolder(final PlaceAdapter.MyViewHolder holder, final int position) {
        try {
            holder.placeName.setText(obj_arr.get(position).getPlace());

            holder.placeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(),PrdouctActivity.class).
                            putExtra(Constants.PAGE_FROM,Constants.PAGE_PLACES).putExtra(Constants.STATE_ID,
                            obj_arr.get(position).getId()));
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
        TextView placeName;
        LinearLayout lyt ;
        public MyViewHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.txt_places);
            lyt = itemView.findViewById(R.id.root_place);
        }
    }
}
