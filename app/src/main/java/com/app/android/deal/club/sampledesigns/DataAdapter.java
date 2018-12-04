package com.app.android.deal.club.sampledesigns;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    public static ArrayList<DataModel> obj_arr=new ArrayList<>();

    public DataAdapter(ArrayList<DataModel> objs)
    {

        this.obj_arr=objs;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View contentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_list,parent,false);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try{
            holder.newsTitle.setText(obj_arr.get(position).get_title());
            holder.newsTimeDate.setText(obj_arr.get(position).get_time_date());

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return obj_arr.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
      TextView newsTitle,newsTimeDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            newsTitle=(TextView)itemView.findViewById(R.id.txt_header);
            newsTimeDate=(TextView)itemView.findViewById(R.id.txt_time_date);

        }
    }
}
