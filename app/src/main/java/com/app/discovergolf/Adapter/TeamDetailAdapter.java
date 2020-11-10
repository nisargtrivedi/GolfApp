package com.app.discovergolf.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.discovergolf.Networks.Response.StuDetailsResponse.Datum;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.TeamStd;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.CircleTransform;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by admin on 7/31/2017.
 */

public class TeamDetailAdapter extends RecyclerView.Adapter<TeamDetailAdapter.ViewHolder> {
    public List<Datum> list;
    Context context;
    public String name;

    public TeamDetailAdapter(Context context, List<Datum> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView MatrixTitle, MatrixComplete;
        ProgressBar roundProgress;
        ImageView MatrixImage;
        ImageView StudentOne;
        ImageView StudentTwo;
        ImageView StudentThree;
        ImageView StudentFour;
        LinearLayout RlData;

        public ViewHolder(View view) {
            super(view);
            MatrixTitle = (TextView) view.findViewById(R.id.MatrixTitle);
            MatrixComplete = (TextView) view.findViewById(R.id.MatrixComplete);
            roundProgress = (ProgressBar) view.findViewById(R.id.roundProgress);
            StudentOne = (ImageView) view.findViewById(R.id.StudentOne);
            StudentTwo = (ImageView) view.findViewById(R.id.StudentTwo);
            StudentThree = (ImageView) view.findViewById(R.id.StudentThree);
            StudentFour = (ImageView) view.findViewById(R.id.StudentFour);
            MatrixImage = (ImageView) view.findViewById(R.id.MatrixImage);
            RlData = (LinearLayout) view.findViewById(R.id.RlData);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.score_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Datum task = list.get(position);
        holder.MatrixTitle.setText(task.getTotalScore().toString() + " " + task.getTitle());
        if(Integer.parseInt(task.getTotalScore().toString())>Integer.parseInt(task.getMetricsVal().toString())){
            holder.MatrixComplete.setText(task.getMetricsVal().toString() + "/" + task.getMetricsVal() + " COMPLETED");
        }else
            holder.MatrixComplete.setText(task.getTotalScore().toString() + "/" + task.getMetricsVal() + " COMPLETED");

        int ans=0;
        ans= (task.getTotalScore() * 100) / task.getMetricsVal();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.roundProgress.setOnTouchListener(null);
            holder.roundProgress.setProgress(ans, true);
        }
        holder.roundProgress.setEnabled(false);
        Glide.with(context)
                .load(task.getItmImg())
                .into(holder.MatrixImage);

        try {
            if (task.getTeamStd().get(0).getProfileImg() != null && !task.getTeamStd().get(0).getProfileImg().isEmpty())
                Picasso.with(context).load(task.getTeamStd().get(0).getProfileImg()).transform(new CircleTransform()).into(holder.StudentOne);
        } catch (Exception e) {
        }
        try {
            if (task.getTeamStd().get(1).getProfileImg() != null && !task.getTeamStd().get(1).getProfileImg().isEmpty())
                Picasso.with(context).load(task.getTeamStd().get(1).getProfileImg()).transform(new CircleTransform()).into(holder.StudentTwo);
        } catch (Exception e) {
        }
        try {
            if (task.getTeamStd().get(2).getProfileImg() != null && !task.getTeamStd().get(2).getProfileImg().isEmpty())
                Picasso.with(context).load(task.getTeamStd().get(2).getProfileImg()).transform(new CircleTransform()).into(holder.StudentThree);
        } catch (Exception e) {
        }
        try {
            if (task.getTeamStd().get(3).getProfileImg() != null && !task.getTeamStd().get(0).getProfileImg().isEmpty())
                Picasso.with(context).load(task.getTeamStd().get(3).getProfileImg()).transform(new CircleTransform()).into(holder.StudentFour);
        } catch (Exception e) {
        }

        holder.RlData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task.getTeamStd().size()>0)
                    ShowPopup(task.getTeamStd());
                else
                    Toasty.info(context, "No Students Achieve Score Yet.", Toast.LENGTH_SHORT, true).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ShowPopup(List<TeamStd> teamStd) {
        Dialog myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.popup_window);
        RecyclerView rv_scores = myDialog.findViewById(R.id.rv_scores);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rv_scores.setLayoutManager(linearLayoutManager);
        UsersListPopupAdapter usersListPopupAdapter = new UsersListPopupAdapter(context, teamStd);
        rv_scores.setAdapter(usersListPopupAdapter);
        usersListPopupAdapter.notifyDataSetChanged();
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
    }
}
