package com.app.discovergolf.Activity.Coach.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Adapter.MissionListAdapter;
import com.app.discovergolf.Activity.Coach.Listeners.MissionSelectedListener;
import com.app.discovergolf.Model.MissionHQModel;
import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.mission_list)
public class MissionList extends BaseActivity implements MissionSelectedListener {

    @ViewById
    RecyclerView RvMissions;

    ArrayList<MissionHQModel> missionHQModelArrayList = new ArrayList<>();
    MissionListAdapter adapter;

    @AfterViews
    public void init() {
        loadBaseObject();
        missionHQModelArrayList.clear();
        missionHQModelArrayList = (ArrayList<MissionHQModel>) getIntent().getSerializableExtra("MissionList");
        adapter = new MissionListAdapter(this, missionHQModelArrayList);
        RvMissions.setLayoutManager(new LinearLayoutManager(this));
        RvMissions.setAdapter(adapter);
        adapter.attach(this);
    }

    @Click
    public void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void OnMissionClick(String MissionID) {
        startActivity(new Intent(MissionList.this, TeamList_.class).putExtra("MissionID", MissionID));
    }
}
