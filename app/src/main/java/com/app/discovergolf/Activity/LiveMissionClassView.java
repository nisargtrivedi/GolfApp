package com.app.discovergolf.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Adapter.TeamDetailAdapter;
import com.app.discovergolf.Adapter.TeamNameAdapter;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.Response.LiveMissionR.MissionTeam;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.Datum;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.StudentDetailsListResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.act_live_mission)
public class LiveMissionClassView extends BaseActivity {
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
    @ViewById
    RelativeLayout rlTitle;

    ArrayList<Datum> dataa = new ArrayList<>();
    TeamDetailAdapter teamNameAdapter;

    @AfterViews
    public void init() {
        loadBaseObject();
        rlTitle.setVisibility(View.GONE);
        Description.setText(Html.fromHtml(getIntent().getStringExtra("description")));
        title1.setText(getIntent().getStringExtra("title"));
        Date.setText(getIntent().getStringExtra("scheduled_date"));
        student_instructions.setText(Html.fromHtml(getIntent().getStringExtra("student_instructions")));
        rev_scores.setLayoutManager(new LinearLayoutManager(this));
        teamNameAdapter = new TeamDetailAdapter(this, dataa);
        rev_scores.setAdapter(teamNameAdapter);
//        intent.putExtra("std_video",response.body().getData().getStdVideo());
        videoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LiveMissionClassView.this,AppWebView.class);
                intent.putExtra("url",getIntent().getStringExtra("std_video"));
                startActivity(intent);
            }
        });
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

    /*public void getLiveMission() {
        final Context context = LiveMissionClassView.this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<LiveMissionResponse> call = api.getLiveMission(WebUtility.LiveMissionDetails, appPreferences.getString("STUDENT_ID").toString());
        call.enqueue(new Callback<LiveMissionResponse>() {
            @Override
            public void onResponse(Call<LiveMissionResponse> call, final retrofit2.Response<LiveMissionResponse> response) {
                Utility.hideProgress();
                if (response.body().getData().getCreateTeam().toString().equals("0")) {
                    dataa.addAll(response.body().getData().getMissionTeam());
                    teamNameAdapter.notifyDataSetChanged();
                    Log.e("team", dataa.size() + "==");


                }
                //

            }

            @Override
            public void onFailure(Call<LiveMissionResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }*/


    public void getLiveMission() {
        final Context context = LiveMissionClassView.this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        Call<StudentDetailsListResponse> call = api.getTeamDetails(WebUtility.Matrics_List_Score, getIntent().getStringExtra("team_id"));
        call.enqueue(new Callback<StudentDetailsListResponse>() {
            @Override
            public void onResponse(Call<StudentDetailsListResponse> call, final retrofit2.Response<StudentDetailsListResponse> response) {
                Utility.hideProgress();
                dataa.addAll(response.body().getData());
                teamNameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<StudentDetailsListResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }

}
