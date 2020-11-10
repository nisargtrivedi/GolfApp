package com.app.discovergolf.Activity.student.Activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Activity.CoachUpdateTeam;
import com.app.discovergolf.Activity.Coach.Adapter.CoachCreateTeamAdapter;
import com.app.discovergolf.Activity.Coach.Fragment.MissionTeam;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.create_team)
public class StudentCreateTeam extends BaseActivity {

    @ViewById
    RecyclerView RvPlayers;
    @ViewById
    AppCompatButton SaveTeam;
    @ViewById
    EditText txtTeamName;
    @ViewById
    ImageView imgMusic;

    int MissionID = 0;
    String teamId = "0";
    ArrayList<PlayerModel> playerModels = new ArrayList<>();
    CoachCreateTeamAdapter createTeamAdapter;
    private boolean isEditing = false;

    @AfterViews
    public void init() {
        loadBaseObject();
        MissionID = Integer.parseInt(getIntent().getStringExtra("MissionID"));
        isEditing = getIntent().getBooleanExtra("isEditing", false);
        if(getIntent().getStringExtra("TeamName")!=null){
            txtTeamName.setText(getIntent().getStringExtra("TeamName"));
        }
        if (isEditing) {
            teamId = getIntent().getStringExtra("teamId") + "";
            getMissionTeam(MissionID);
//            playerModels= (ArrayList<PlayerModel>) getIntent().getSerializableExtra("Team");
//
//            if(playerModels.size()>0){
//                for(int i=0;i<playerModels.size();i++){
//                    PlayerModel model=new PlayerModel();
//                    model.PlayerID=playerModels.get(i).PlayerID;
//                    model.PlayerName=playerModels.get(i).PlayerName;
//                    model.PlayerImage=playerModels.get(i).PlayerImage;
//                    model.isSelected=true;
//                    playerModels.add(model);
//                }
////                getStudentListAPI();
////                RvPlayers.setLayoutManager(new LinearLayoutManager(StudentCreateTeam.this));
////                createTeamAdapter=new CoachCreateTeamAdapter(StudentCreateTeam.this,playerModels);
////                RvPlayers.setAdapter(createTeamAdapter);
//            }
        }
        else{
            teamId="0";
            getStudentListAPI();
        }

        imgMusic.setVisibility(View.INVISIBLE);
        Log.i("MISSION ID-->", MissionID + "");
        Log.i("TEAM ID-->", teamId + "");
    }

    //Create Team Player List
    @Click
    public void SaveTeam() {
        if (!TextUtils.isEmpty(txtTeamName.getText().toString().trim())) {
            ArrayList<Integer> studentlist = new ArrayList<>();
            for (int i = 0; i < createTeamAdapter.Filterlist.size(); i++) {
                if (createTeamAdapter.Filterlist.get(i).isSelected) {
                    studentlist.add(createTeamAdapter.Filterlist.get(i).PlayerID);
                }
            }
            if (studentlist.size() > 0) {
                String csvValues = TextUtils.join(",", studentlist);
                Log.i("CSV VALUES--", csvValues);
                saveTeamAPI(csvValues);
            }
        } else {
            showErrorMsg("please enter team name");
        }
    }


    //API Get All Student List
    public void getStudentListAPI() {
        //+appPreferences.getString("COACHID")
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetAllStudentListWithoutTeam + "&student_id=" + appPreferences.getString("STUDENTID") + "&mission_id=" + MissionID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Class Response", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            JSONArray jsonArray = object.getJSONArray("data");
                            if (error_code.equalsIgnoreCase("0")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PlayerModel playerModel = new PlayerModel();
                                    playerModel.PlayerID = jsonArray.getJSONObject(i).getInt("id");
                                    playerModel.PlayerImage = jsonArray.getJSONObject(i).getString("profile_img");
                                    playerModel.PlayerName = jsonArray.getJSONObject(i).getString("name");
                                    playerModel.isAddedTeam = jsonArray.getJSONObject(i).getInt("isAddedTeam");
                                    if (playerModel.isAddedTeam == 0) {
                                        playerModels.add(playerModel);
                                    }
                                }
                                if(isEditing){
                                    createTeamAdapter.notifyDataSetChanged();
                                }else {
                                    RvPlayers.setLayoutManager(new LinearLayoutManager(StudentCreateTeam.this));
                                    createTeamAdapter = new CoachCreateTeamAdapter(StudentCreateTeam.this, playerModels);
                                    RvPlayers.setAdapter(createTeamAdapter);
                                }
                             //   createTeamAdapter.notifyDataSetChanged();
                            }


                        } catch (Exception ex) {
                            Utility.hideProgress();
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

    //Save Team API
    public void saveTeamAPI(String StudentList) {

        //+appPreferences.getString("COACHID")
        Utility.showProgress(this);

        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.CreateModTeam + "&main_student_id=" + appPreferences.getString("STUDENTID") + "&mission_id=" + MissionID + "&music_id=2" + "&student_id=" + StudentList + "&title=" + txtTeamName.getText().toString().trim() + "&team_id=" + teamId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Team Response", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            Utility.showSuccessMsg(StudentCreateTeam.this, error_message);
                            //startActivity(new Intent(StudentCreateTeam.this, MODActivity_.class));
                            finish();

                        } catch (Exception ex) {
                            Utility.hideProgress();
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

    @Click
    public void back() {
        MissionTeam.newInstance().shouldRefreshOnResume = true;
        finish();
        overridePendingTransition(0, 0);
    }

    private void getMissionTeam(int missionId) {
//
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetMODMissionTeam + "&mission_id=" + missionId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("Response MISSION TEAM--->", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            if (error_code.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for(int j=0;j<jsonArray.length();j++) {

                                    String title = jsonArray.getJSONObject(j).optString("title");
                                    //txtTeamName.setText(title);
                                    JSONArray dataArr = jsonArray.getJSONObject(j).getJSONArray("team_student");
                                    for (int i = 0; i < dataArr.length(); i++) {
                                        PlayerModel playerModel = new PlayerModel();
                                        playerModel.PlayerID = dataArr.getJSONObject(i).getInt("student_id");
                                        playerModel.PlayerImage = dataArr.getJSONObject(i).getString("profile_img");
                                        playerModel.PlayerName = dataArr.getJSONObject(i).getString("name");
                                        playerModel.isSelected = true;
//                                        if (playerModel.isAddedTeam == 0) {
                                            if(teamId.equalsIgnoreCase(jsonArray.getJSONObject(j).optString("id")))
                                                playerModels.add(playerModel);
                                        //}
                                    }
                                }
                                RvPlayers.setLayoutManager(new LinearLayoutManager(StudentCreateTeam.this));
                                createTeamAdapter = new CoachCreateTeamAdapter(StudentCreateTeam.this, playerModels);
                                RvPlayers.setAdapter(createTeamAdapter);
                                getStudentListAPI();
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

//    private void getNonSelectedTeam(int missionId) {
////        http://solutioncode.in/webApi.php?action=getAllStudentListWithoutTeam&student_id=18&mission_id=28
//        Utility.showProgress(this);
//        StringRequest request = new StringRequest(Request.Method.GET,
//                WebUtility.BASE_URL + "?action=" + WebUtility.GetMODMissionTeam + "&mission_id=" + missionId,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//
//                        Log.i("Response--->", s);
//                        Utility.hideProgress();
//                        try {
//                            JSONObject object = new JSONObject(s);
//                            String error_code = object.getString("error_code");
//                            String error_message = object.getString("error_message");
//                            if (error_code.equalsIgnoreCase("0")) {
//                                JSONArray jsonArray = object.getJSONArray("data");
//                                String title = jsonArray.getJSONObject(0).optString("title");
//                                txtTeamName.setText(title);
//                                JSONArray dataArr = jsonArray.getJSONObject(0).getJSONArray("team_student");
//                                for (int i = 0; i < dataArr.length(); i++) {
//                                    PlayerModel playerModel = new PlayerModel();
//                                    playerModel.PlayerID = dataArr.getJSONObject(i).getInt("id");
//                                    playerModel.PlayerImage = dataArr.getJSONObject(i).getString("profile_img");
//                                    playerModel.PlayerName = dataArr.getJSONObject(i).getString("name");
//                                    playerModel.isSelected = true;
//                                    if (playerModel.isAddedTeam == 0) {
//                                        playerModels.add(playerModel);
//                                    }
//                                }
//                                RvPlayers.setLayoutManager(new LinearLayoutManager(StudentCreateTeam.this));
//                                createTeamAdapter = new CoachCreateTeamAdapter(StudentCreateTeam.this, playerModels);
//                                RvPlayers.setAdapter(createTeamAdapter);
//                            }
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Utility.hideProgress();
//            }
//        }) {
//
//        };
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//    }
}
