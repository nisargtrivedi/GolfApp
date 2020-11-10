package com.app.discovergolf.Activity.student.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.AppWebView;
import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.student.Adapter.MyTeamAdapter;
import com.app.discovergolf.Model.ModMissionModel;
import com.app.discovergolf.Model.PlayerModel;
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

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_mod)
public class MODActivity extends BaseActivity {

    @ViewById
    com.app.discovergolf.Util.TTextView btnCreateTeam;
    @ViewById
    com.app.discovergolf.Util.TTextView Back;
    @ViewById
    RelativeLayout rl_mission;
    @ViewById
    com.app.discovergolf.Util.TTextView txt_vid_description;
    @ViewById
    com.app.discovergolf.Util.TTextView txt_title;
    @ViewById
    com.app.discovergolf.Util.TTextView txt_schedule_date;
    @ViewById
    RecyclerView rv_team;
    @ViewById
    RelativeLayout rl_player;

    int MissionID;

    ArrayList<ModMissionModel> ModMissionModels = new ArrayList<>();

    @AfterViews
    public void init() {
        rl_mission.setVisibility(View.GONE);
        //MissionApi();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MissionApi();

    }

    private void MissionApi() {
        appPreferences = new AppPreferences(this);
        String StudentId = appPreferences.getString("STUDENTID");
        Log.e("StudentId", StudentId);
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetModMissionList2 + "&student_id=" + StudentId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        ModMissionModels.clear();
                        Log.i("Response--->", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            if (error_code.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    rl_mission.setVisibility(View.VISIBLE);
                                    ModMissionModel ModMissionModel = new ModMissionModel();
                                    ModMissionModel.academy_id = jsonArray.getJSONObject(i).getString("academy_id");
                                    ModMissionModel.id = jsonArray.getJSONObject(i).getInt("id");
                                    ModMissionModel.ageRange = jsonArray.getJSONObject(i).getString("ageRange");
                                    ModMissionModel.cat_id = jsonArray.getJSONObject(i).getString("cat_id");
                                    ModMissionModel.class_id = jsonArray.getJSONObject(i).getString("class_id");
                                    ModMissionModel.course_id = jsonArray.getJSONObject(i).getString("course_id");
                                    ModMissionModel.created_at = jsonArray.getJSONObject(i).getString("created_at");
                                    ModMissionModel.create_team = jsonArray.getJSONObject(i).getInt("create_team");
                                    ModMissionModel.description = jsonArray.getJSONObject(i).getString("description");
                                    ModMissionModel.upload_video=jsonArray.getJSONObject(i).getString("upload_video");
                                    url=jsonArray.getJSONObject(i).getString("upload_video");
                                    txt_vid_description.setText(Html.fromHtml(ModMissionModel.description));
                                    ModMissionModel.gameFrame = jsonArray.getJSONObject(i).getString("gameFrame");
                                    ModMissionModel.metrics = jsonArray.getJSONObject(i).getString("metrics");
                                    ModMissionModel.missionDuration = jsonArray.getJSONObject(i).getString("missionDuration");

                                    JSONArray teamArray = jsonArray.getJSONObject(i).getJSONArray("missionTeam");
                                    ArrayList<ModMissionModel.MissionTeam> teamList = new ArrayList<>();
                                    for (int j = 0; j < teamArray.length(); j++) {
                                        ModMissionModel.MissionTeam team = new ModMissionModel().new MissionTeam();
                                        team.setId(teamArray.getJSONObject(j).optInt("id"));
                                        team.TeamID=teamArray.getJSONObject(j).optInt("id");
                                        team.setScore(teamArray.getJSONObject(j).optString("totalScore"));
                                        team.setTeam(teamArray.getJSONObject(j).optString("title"));
                                        JSONArray t=teamArray.getJSONObject(j).getJSONArray("team_student");
                                        for(int k=0;k<t.length();k++){
                                            PlayerModel model=new PlayerModel();
                                            model.PlayerID=t.getJSONObject(k).getInt("id");
                                            model.PlayerImage=t.getJSONObject(k).getString("profile_img");
                                            model.PlayerName=t.getJSONObject(k).getString("name");
                                            team.playerModels.add(model);
                                        }
                                        teamList.add(team);
                                    }

                                    ModMissionModel.my_team_id = jsonArray.getJSONObject(i).getInt("my_team_id");
                                    if (ModMissionModel.my_team_id == 0) {
                                        btnCreateTeam.setVisibility(View.VISIBLE);
                                    } else {
                                        btnCreateTeam.setVisibility(View.GONE);
                                    }
                                    if (teamList.size() > 0) {
                                        MyTeamAdapter adapter = new MyTeamAdapter(MODActivity.this, teamList, ModMissionModel.my_team_id, ModMissionModel.id);
                                        rv_team.setLayoutManager(new LinearLayoutManager(MODActivity.this));
                                        rv_team.setAdapter(adapter);
                                    }
                                    ModMissionModel.scheduled_date = DateUtils.GolfDate(jsonArray.getJSONObject(i).getString("scheduled_date"));
                                    ModMissionModel.status = jsonArray.getJSONObject(i).getString("status");
                                    ModMissionModel.teamSize = jsonArray.getJSONObject(i).getString("teamSize");
                                    ModMissionModel.team_admin = jsonArray.getJSONObject(i).getInt("team_admin");
                                    ModMissionModel.timeDuration = jsonArray.getJSONObject(i).getString("timeDuration");
                                    ModMissionModel.title = jsonArray.getJSONObject(i).getString("title");
                                    txt_title.setText(ModMissionModel.title);
                                    txt_schedule_date.setText(ModMissionModel.scheduled_date);
                                    ModMissionModel.updated_at = jsonArray.getJSONObject(i).getString("updated_at");
                                    ModMissionModel.upload_video = jsonArray.getJSONObject(i).getString("upload_video");
                                    url = ModMissionModel.upload_video;

                                    ModMissionModels.add(ModMissionModel);
                                    MissionID = ModMissionModel.id;
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    String url = "";

    @Click
    public void rl_player() {
        if(url!=null && !url.isEmpty())
            playVideo(url);
        else
            showErrorMsg("No Video Found");
    }

    private void playVideo(String url) {
        startActivity(new Intent(this, AppWebView.class).putExtra("url", url));
    }

    @Click
    public void back() {
        finish();
    }

    @Click
    public void btnCreateTeam() {
        if (MissionID != 0) {
            startActivity(new Intent(MODActivity.this, StudentCreateTeam_.class).putExtra("MissionID", MissionID + ""));
        } else {
            Utility.showErrorMsg(MODActivity.this, "please select class or mission");
        }
    }
}
