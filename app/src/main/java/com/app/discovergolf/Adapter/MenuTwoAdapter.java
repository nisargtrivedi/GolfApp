package com.app.discovergolf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.LiveMissionClassView_;
import com.app.discovergolf.Activity.LiveMission_;
import com.app.discovergolf.Activity.MOD_;
import com.app.discovergolf.Activity.student.Activity.MODActivity_;
import com.app.discovergolf.Model.DashBoardMenuTwo;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MenuTwoAdapter extends BaseAdapter {

    ArrayList<DashBoardMenuTwo> dashBoardMenuTwos;
    Context context;
    LayoutInflater inflater;

    public MenuTwoAdapter(Context context, ArrayList<DashBoardMenuTwo> dashBoardMenuTwos) {
        this.context = context;
        this.dashBoardMenuTwos = dashBoardMenuTwos;
        inflater = LayoutInflater.from(context);
    }

    public static class Holder {
        public TextView MenuName;
        public ImageView imgMenu;
        public CardView menu;

        public Holder(View view) {
            MenuName = view.findViewById(R.id.MenuName);
            imgMenu = view.findViewById(R.id.imgMenu);
            menu = view.findViewById(R.id.menu);
        }
    }

    @Override
    public int getCount() {
        return dashBoardMenuTwos.size();
    }

    @Override
    public Object getItem(int position) {
        return dashBoardMenuTwos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.darshboard_menu_two, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final DashBoardMenuTwo dashBoardMenuTwo = dashBoardMenuTwos.get(position);
        holder.MenuName.setText(dashBoardMenuTwo.Name);
        holder.imgMenu.setImageResource(dashBoardMenuTwo.Image);
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dashBoardMenuTwo.Name.equalsIgnoreCase("Live Mission")) {
                    // context.startActivity(new Intent(context, LiveMission_.class));
                    getLiveMission();
                } else if (dashBoardMenuTwo.Name.equalsIgnoreCase("M.O.D")) {
                    context.startActivity(new Intent(context,MODActivity_.class));
                }
            }
        });

        return convertView;
    }

    public void getLiveMission() {
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<LiveMissionResponse> call = api.getLiveMission(WebUtility.LiveMissionDetails, appPreferences.getString("STUDENTID").toString());
        call.enqueue(new Callback<LiveMissionResponse>() {
            @Override
            public void onResponse(Call<LiveMissionResponse> call, retrofit2.Response<LiveMissionResponse> response) {
                Utility.hideProgress();
                Log.i("RESPONSE----->",response.body().getData().getCreateTeam().toString());
                if (response.body().getData().getCreateTeam().toString().equals("1")) {
                    Intent intent = new Intent(context, LiveMission_.class);
                    intent.putExtra("title", response.body().getData().getTitle());
                    intent.putExtra("scheduled_date  ", response.body().getData().getScheduledDate());
                    intent.putExtra("description", response.body().getData().getDescription());
                    intent.putExtra("std_video", response.body().getData().getStdVideo());
                    intent.putExtra("student_instructions", response.body().getData().getStudentInstructions());
                    //context.startActivity(new Intent(context, LiveMission_.class));
                    context.startActivity(intent);
                } else if (response.body().getData().getCreateTeam().equals("0")) {
/*
                    context.startActivity(new Intent(context, LiveMissionClassView.class));
*/
                    Intent intent = new Intent(context, LiveMissionClassView_.class);
                    intent.putExtra("title", response.body().getData().getTitle());
                    intent.putExtra("scheduled_date  ", response.body().getData().getScheduledDate());
                    intent.putExtra("description", response.body().getData().getDescription());
                    intent.putExtra("std_video", response.body().getData().getStdVideo());
                    intent.putExtra("student_instructions", response.body().getData().getStudentInstructions());
                    intent.putExtra("team_id", response.body().getData().getId());
                    //context.startActivity(new Intent(context, LiveMission_.class));
                    context.startActivity(intent);
                }
                //

            }

            @Override
            public void onFailure(Call<LiveMissionResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }
}
