package com.app.discovergolf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.discovergolf.Activity.TeamDetailScore_;
import com.app.discovergolf.Networks.Response.LiveMissionR.MissionTeam;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.TeamStd;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class UsersListPopupAdapter extends RecyclerView.Adapter<UsersListPopupAdapter.ViewHolder> {


    public List<TeamStd> list;
    Context context;
    public String name;


    public UsersListPopupAdapter(Context context, List<TeamStd> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, scores;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            scores = (TextView) view.findViewById(R.id.scores);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_popup_students, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TeamStd task = list.get(position);
        holder.name.setText(task.getName());
        holder.scores.setText(task.getMyTotalScore().toString());
        Picasso.with(context).load(task.getProfileImg()).transform(new CircleTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
