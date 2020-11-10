package com.app.discovergolf.Activity.Coach.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Adapter.MissionListAdapter;
import com.app.discovergolf.Activity.Coach.Adapter.MissionTeamAdapter;
import com.app.discovergolf.Activity.Coach.Adapter.TeamListAdapter;
import com.app.discovergolf.Activity.Coach.Listeners.MissionSelectedListener;
import com.app.discovergolf.Activity.Coach.Listeners.TeamListListener;
import com.app.discovergolf.Activity.Coach.Listeners.TeamSelectedListener;
import com.app.discovergolf.Model.MissionHQModel;
import com.app.discovergolf.Model.MissionTeamModel;
import com.app.discovergolf.Model.MissionTeamPlayerModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.teams_list)
public class TeamList extends BaseActivity implements TeamListListener {

    @ViewById
    RecyclerView RvMissions;

    ArrayList<MissionTeamModel> missionHQModelArrayList = new ArrayList<>();
    TeamListAdapter missionTeamAdapter;

    String MissionID="";
    @AfterViews
    public void init(){
        loadBaseObject();
        missionHQModelArrayList.clear();
        MissionID= getIntent().getStringExtra("MissionID");

        getTeamListAPI();

        missionTeamAdapter=new TeamListAdapter(this,missionHQModelArrayList);
        RvMissions.setLayoutManager(new LinearLayoutManager(this));
        RvMissions.setAdapter(missionTeamAdapter);
        missionTeamAdapter.attach(this);


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

    @Override
    public void OnClickTeam(String TeamID,String Name) {

        Log.i("TeamName",TeamID);
        startActivity(new Intent(this,TeamScore_.class).putExtra("TeamID",TeamID).putExtra("TeamName",Name));
    }

    public void getTeamListAPI() {
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetMissionTeams + "&mission_id=" + MissionID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Mission Response-->", s);
                        try {
                            Utility.hideProgress();
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            if (error_code.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MissionTeamModel teamModel = new MissionTeamModel();
                                    teamModel.TeamID = jsonArray.getJSONObject(i).getString("id");
                                    teamModel.CoachID = jsonArray.getJSONObject(i).getString("coach_id");
                                    teamModel.MissionID = jsonArray.getJSONObject(i).getString("mission_id");
                                    teamModel.MissionTitle = jsonArray.getJSONObject(i).getString("title");
                                    missionHQModelArrayList.add(teamModel);
//                                    JSONArray array = jsonArray.getJSONObject(i).getJSONArray("team_student");
//                                    for (int j = 0; j < array.length(); j++) {
//                                        MissionTeamPlayerModel playerModel = new MissionTeamPlayerModel();
//                                        playerModel.TeamID = array.getJSONObject(j).getString("team_id");
//                                        playerModel.MissionID = array.getJSONObject(j).getString("mission_id");
//                                        playerModel.StudentID = array.getJSONObject(j).getString("student_id");
//                                        playerModel.StudentName = array.getJSONObject(j).getString("name");
//                                        playerModel.StudentImage = array.getJSONObject(j).getString("profile_img");
//                                        teamPlayerModels.add(playerModel);
//                                    }
                                }
                                missionTeamAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception ex) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
