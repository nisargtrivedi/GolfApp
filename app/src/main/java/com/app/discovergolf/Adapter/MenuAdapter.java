package com.app.discovergolf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.Score_;
import com.app.discovergolf.Model.DashboardMenu;
import com.app.discovergolf.Networks.Response.Datum;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class MenuAdapter extends  RecyclerView.Adapter<MenuAdapter.ViewHolder> {


    public List<Datum> list;
    Context context;
    public String name;



    public MenuAdapter(Context context, List<Datum> list){
        this.context=context;
        this.list=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView MenuName,Score;
        public ImageView imgMenu;
        public RelativeLayout menu;
        public ViewHolder(View view) {
            super(view);
            MenuName = (TextView) view.findViewById(R.id.MenuName);
            Score = (TextView) view.findViewById(R.id.Score);
            imgMenu = (ImageView) view.findViewById(R.id.imgMenu);
            menu=view.findViewById(R.id.menu);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_dashboard_menu, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Datum task = list.get(position);
        holder.MenuName.setText(task.getTitle());
        holder.Score.setText(task.getTotalScore().toString());
        holder.imgMenu.setImageResource(R.drawable.ic_menu_sample);
        Glide.with(context)
                .load(task.getItmImg())
                .into(holder.imgMenu);
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(task.getTitle().equalsIgnoreCase("Level")){
                    context.startActivity(new Intent(context, Score_.class));
                }*/
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
