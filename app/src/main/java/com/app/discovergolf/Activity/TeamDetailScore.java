package com.app.discovergolf.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Adapter.MissionListAdapter;
import com.app.discovergolf.Adapter.TeamDetailAdapter;
import com.app.discovergolf.Model.MissionHQModel;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.Datum;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.StudentDetailsListResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.TTextView;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.selected_team_details)
public class TeamDetailScore extends BaseActivity {
    @ViewById
    RecyclerView RvTeamsMatrix;
    @ViewById
    TTextView TeamName;

    TeamDetailAdapter teamDetailAdapter;
    ArrayList<Datum>dataList= new ArrayList();
    @AfterViews
    public void init(){
        loadBaseObject();
        RvTeamsMatrix.setLayoutManager(new LinearLayoutManager(this));
        teamDetailAdapter= new TeamDetailAdapter(this,dataList);
        RvTeamsMatrix.setAdapter(teamDetailAdapter);
        TeamName.setText(getIntent().getStringExtra("team_name"));
        getLiveMission();
    }

    public void getLiveMission() {
        final Context context = TeamDetailScore.this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        Call<StudentDetailsListResponse> call = api.getTeamDetails(WebUtility.Matrics_List_Score, getIntent().getStringExtra("team_id"));
        call.enqueue(new Callback<StudentDetailsListResponse>() {
            @Override
            public void onResponse(Call<StudentDetailsListResponse> call, final retrofit2.Response<StudentDetailsListResponse> response) {
                Utility.hideProgress();
                dataList.addAll(response.body().getData());
                teamDetailAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<StudentDetailsListResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }
    @Click
    public void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Click
    public void TeamName(){
        finish();
    }
}
