package com.app.discovergolf.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.CoachCreateTeam;
import com.app.discovergolf.Activity.Coach.Adapter.CoachCreateTeamAdapter;
import com.app.discovergolf.Adapter.TeamNameAdapter;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.LiveMissionR.Data;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.Response.LiveMissionR.MissionTeam;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.DateUtils;
import com.app.discovergolf.Util.TTextView;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.act_live_mission)
public class LiveMission extends BaseActivity {
    @ViewById
    com.app.discovergolf.Util.TTextView title1;

    @ViewById
    com.app.discovergolf.Util.TTextView Description;
    @ViewById
    RecyclerView rev_scores;
    @ViewById
    com.app.discovergolf.Util.TTextView student_instructions;
    @ViewById
    com.app.discovergolf.Util.TTextView Title;
    @ViewById
    ImageView videoPlay;
    @ViewById
    com.app.discovergolf.Util.TTextView Date;

    ArrayList<MissionTeam> dataa = new ArrayList<>();
    TeamNameAdapter teamNameAdapter;

    @AfterViews
    public void init() {
        loadBaseObject();
        Description.setText(Html.fromHtml(getIntent().getStringExtra("student_instructions")));
        title1.setText(getIntent().getStringExtra("title"));
        Date.setText(getIntent().getStringExtra("scheduled_date"));
        //student_instructions.setText(Html.fromHtml(getIntent().getStringExtra("student_instructions")));
        rev_scores.setLayoutManager(new LinearLayoutManager(this));
        teamNameAdapter = new TeamNameAdapter(this, dataa);
        rev_scores.setAdapter(teamNameAdapter);
//        intent.putExtra("std_video",response.body().getData().getStdVideo());
        getLiveMission();
    }

    @Click
    public void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Click
    public void teamSelect() {
        startActivity(new Intent(this, TeamDetailScore_.class));
    }

    public void getLiveMission() {
        final Context context = LiveMission.this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<LiveMissionResponse> call = api.getLiveMission(WebUtility.LiveMissionDetails, appPreferences.getString("STUDENTID").toString());
        call.enqueue(new Callback<LiveMissionResponse>() {
            @Override
            public void onResponse(Call<LiveMissionResponse> call, final retrofit2.Response<LiveMissionResponse> response) {
                Utility.hideProgress();
                if (response.body().getData().getCreateTeam().toString().equals("1")) {
                    dataa.addAll(response.body().getData().getMissionTeam());
                    teamNameAdapter.notifyDataSetChanged();
                    Description.setText(Html.fromHtml(response.body().getData().getStudentInstructions()));
                    title1.setText(response.body().getData().getTitle());
                    Date.setText(DateUtils.GolfDate(response.body().getData().getScheduledDate()));

                    //student_instructions.setText(Html.fromHtml(response.body().getData().getStudentInstructions()));

                    videoPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent= new Intent(LiveMission.this,AppWebView.class);
                            intent.putExtra("url",response.body().getData().getStdVideo());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<LiveMissionResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }

}
