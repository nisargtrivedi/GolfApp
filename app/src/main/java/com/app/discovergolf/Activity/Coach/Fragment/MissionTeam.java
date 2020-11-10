package com.app.discovergolf.Activity.Coach.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.CoachCreateTeam_;
import com.app.discovergolf.Activity.Coach.Activity.CoachDashboard;
import com.app.discovergolf.Activity.Coach.Activity.CoachUpdateTeam_;
import com.app.discovergolf.Activity.Coach.Adapter.MissionTeamAdapter;
import com.app.discovergolf.Activity.Coach.Listeners.CoachDeleteTeamListener;
import com.app.discovergolf.Activity.Coach.Listeners.TeamSelectedListener;
import com.app.discovergolf.Model.ClassMission;
import com.app.discovergolf.Model.MissionTeamModel;
import com.app.discovergolf.Model.MissionTeamPlayerModel;
import com.app.discovergolf.Model.StudentClass;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.TTextView;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

@EFragment(R.layout.coach_mission_team)
public class MissionTeam extends Fragment implements TeamSelectedListener,CoachDeleteTeamListener {


    @ViewById
    AppCompatSpinner ddClass;
    @ViewById
    AppCompatSpinner ddMission;
    @ViewById
    TTextView btnCreateTeam;
    @ViewById
    RecyclerView RvTeams;
    @ViewById
    ImageView ImgNoData;
    @ViewById
    TextView tvNoTeam;


    AppPreferences appPreferences;
    ArrayList<StudentClass> studentClassArrayList = new ArrayList<>();
    ArrayList<ClassMission> classMissions = new ArrayList<>();

    ArrayList<MissionTeamModel> missionTeamModels = new ArrayList<>();
    ArrayList<MissionTeamPlayerModel> teamPlayerModels = new ArrayList<>();

    List<String> classList = new ArrayList<>();
    List<String> missionList = new ArrayList<>();

    MissionTeamAdapter missionTeamAdapter;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    int ClassID = 0;
    int MissionID = 0;
    int ClassMissionID = 0;
    int isTeamCreate=0;
    public boolean shouldRefreshOnResume = false;

    @AfterViews
    public void init() {
        ((CoachDashboard) getActivity()).setTitleText("Mission Teams");
        appPreferences = new AppPreferences(getActivity());

        studentClassArrayList.clear();
        classMissions.clear();
        missionTeamModels.clear();
        teamPlayerModels.clear();
        classList.clear();
        missionList.clear();

        adapter2 = new ArrayAdapter<>(getActivity(), R.layout.singleitem, missionList);
        ddMission.setAdapter(adapter2);

        getClassListAPI();

        RvTeams.setLayoutManager(new LinearLayoutManager(getActivity()));
        missionTeamAdapter = new MissionTeamAdapter(getActivity(), missionTeamModels);
        RvTeams.setAdapter(missionTeamAdapter);
        missionTeamAdapter.attach(this);

    }


    public static MissionTeam newInstance() {
        MissionTeam fragment = new MissionTeam_();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefreshOnResume) {
            studentClassArrayList.clear();
            classMissions.clear();
            missionTeamModels.clear();
            teamPlayerModels.clear();
            classList.clear();
            missionList.clear();

            adapter2 = new ArrayAdapter<>(getActivity(), R.layout.singleitem, missionList);
            ddMission.setAdapter(adapter2);

            getClassListAPI();

            RvTeams.setLayoutManager(new LinearLayoutManager(getActivity()));
            missionTeamAdapter = new MissionTeamAdapter(getActivity(), missionTeamModels);
            RvTeams.setAdapter(missionTeamAdapter);
            missionTeamAdapter.attach(this);
        }
    }

    @Click
    public void btnCreateTeam() {
        if (ClassID != 0 && MissionID != 0) {
            startActivity(new Intent(getActivity(), CoachCreateTeam_.class).putExtra("MissionID", MissionID + "").putExtra("ClassMission", ClassMissionID + ""));
        } else {
            Utility.showErrorMsg(getActivity(), "please select mission");
        }
    }

    //API Get All Class Methods
    public void getClassListAPI() {
        //+appPreferences.getString("COACHID")
//        Utility.showProgress(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetClassList + "&coach_id=" + appPreferences.getString("COACHID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Class Response", s);
                        try {
                            //Utility.hideProgress();
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            if (error_code.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    StudentClass studentClass = new StudentClass();
                                    studentClass.ClassID = jsonArray.getJSONObject(i).getInt("id");
                                    studentClass.ClassTitle = jsonArray.getJSONObject(i).getString("title");
                                    Object missionObject = new JSONTokener(jsonArray.getJSONObject(i).getString("classMission")).nextValue();
                                    if (missionObject instanceof JSONObject) {
                                        //you have an object
                                        Log.i("OBJECT DATA-->", missionObject.toString());
                                        JSONObject missionObj = (JSONObject) missionObject;
                                        ClassMission mission = new ClassMission();
                                        mission.ClassID = studentClass.ClassID;
                                        mission.MissionID = missionObj.getInt("id");
                                        mission.MissionName = missionObj.getString("title");
                                        mission.CreateTeam = missionObj.getInt("create_team");
                                        mission.is_class = missionObj.getString("is_class");
                                        classMissions.add(mission);
                                        studentClass.classMission = mission;
                                    } else if (missionObject instanceof JSONArray) {
                                        //you have an array
                                    }
                                    studentClassArrayList.add(studentClass);
                                }
                                if (studentClassArrayList.size() > 0) {
                                    for (int j = 0; j < studentClassArrayList.size(); j++) {
                                        Log.i("TITLE---", studentClassArrayList.get(j).ClassTitle);
                                        classList.addAll(Collections.singleton(studentClassArrayList.get(j).ClassTitle));
                                    }
                                    adapter = new ArrayAdapter<>(getActivity(), R.layout.singleitem, classList);
                                    ddClass.setAdapter(adapter);
                                    ddClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            // Project.setText(projects.projectDetailList.get(n).name);
                                            int classID = studentClassArrayList.get(position).ClassID;
                                            ClassID = classID;
                                            MissionID = studentClassArrayList.get(position).classMission.MissionID;
                                            if(studentClassArrayList.get(position).classMission!=null) {
                                                if(studentClassArrayList.get(position).classMission.is_class!=null) {
                                                    if (studentClassArrayList.get(position).classMission.is_class.equalsIgnoreCase("0")) {
                                                        ClassMissionID = 1;
//                                                if(studentClassArrayList.get(position).classMission.CreateTeam==1){
//                                                        isTeamCreate=1;
//                                                } else {
//                                                        isTeamCreate=0;
//                                                }
                                                    } else {
                                                        ClassMissionID = 0;
                                                    }
                                                }

                                            }
                                            missionList.clear();
                                            Log.i("CLASS ID-->", classID + "");
                                            Log.i("MISSION ID-->", MissionID + "");
                                            Log.i("IS CREATED -->", ClassMissionID + "");

                                            for (ClassMission mission : classMissions) {
                                                if (mission.ClassID == classID) {
                                                    missionList.addAll(Collections.singleton(mission.MissionName));

                                                }
                                            }
                                            if (missionList.size() > 0) {
                                                adapter2.notifyDataSetChanged();
                                                if (classMissions.size() > 0) {
                                                    getTeamListAPI();
                                                }

                                            } else {
                                                missionList.clear();
                                                adapter2.notifyDataSetChanged();
                                                //Utility.showErrorMsg(getActivity(), "No Mission Found");
                                                MissionID = 0;
                                                missionTeamModels.clear();
                                                missionTeamAdapter.notifyDataSetChanged();
                                                HideRecycleView();


                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });


                                    ddMission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            // Project.setText(projects.projectDetailList.get(n).name);
                                            if (classMissions.size() > 0) {
                                                int missionID = classMissions.get(position).MissionID;
                                                MissionID = missionID;
                                                Log.i("Mission ID", MissionID + "");
                                                Log.i("CLASS ID", ClassMissionID + "");
                                                //getTeamListAPI();
                                            } else {

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    //API Get All Team Based On Mission
    public void getTeamListAPI() {
        Utility.showProgress(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetMissionTeams + "&mission_id=" + MissionID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Mission Response-->", s);
                        try {
                            Utility.hideProgress();
                            missionTeamModels.clear();
                            teamPlayerModels.clear();

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
                                    missionTeamModels.add(teamModel);
                                    JSONArray array = jsonArray.getJSONObject(i).getJSONArray("team_student");
                                    for (int j = 0; j < array.length(); j++) {
                                        MissionTeamPlayerModel playerModel = new MissionTeamPlayerModel();
                                        playerModel.TeamID = array.getJSONObject(j).getString("team_id");
                                        playerModel.MissionID = array.getJSONObject(j).getString("mission_id");
                                        playerModel.StudentID = array.getJSONObject(j).getString("student_id");
                                        playerModel.StudentName = array.getJSONObject(j).getString("name");
                                        playerModel.StudentImage = array.getJSONObject(j).getString("profile_img");
                                        teamModel.teamPlayerModels.add(playerModel);
                                    }
                                }
                                missionTeamAdapter.notifyDataSetChanged();
                                HideRecycleView();
                                if(ClassMissionID==1) {
                                    if(missionTeamModels.size()>0){
                                        btnCreateTeam.setVisibility(View.INVISIBLE);
                                    } else {
                                        //ClassMissionID = 0;
                                        btnCreateTeam.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    btnCreateTeam.setVisibility(View.VISIBLE);
                                }

                            }
                            //HideRecycleView();

                        } catch (Exception ex) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void HideRecycleView() {
        if (missionTeamModels.size() > 0) {
            ImgNoData.setVisibility(View.GONE);
            tvNoTeam.setVisibility(View.GONE);
            RvTeams.setVisibility(View.VISIBLE);
        } else {
            ImgNoData.setVisibility(View.VISIBLE);
            tvNoTeam.setVisibility(View.VISIBLE);
            RvTeams.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnClickTeam(MissionTeamModel Name) {
        startActivity(new Intent(getActivity(), CoachUpdateTeam_.class).putExtra("TeamName", Name.MissionTitle).putExtra("TeamDetails", (ArrayList<MissionTeamPlayerModel>) Name.teamPlayerModels).putExtra("ClassMission", ClassMissionID + ""));
    }

    @Override
    public void OnDelete(final String model) {
        Utility.showProgress(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.DELETE_TEAM + "&team_id=" + model,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Mission Response-->", s);
                        Log.i("URL-->", WebUtility.BASE_URL + "?action=" + WebUtility.DELETE_TEAM + "&team_id=" + model);
                        try {
                            Utility.hideProgress();
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            Toasty.info(getActivity(), error_message, Toast.LENGTH_SHORT, true).show();
                            getTeamListAPI();
                            missionTeamAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}


//            CoachCreateTeam coachCreateTeam=new CoachCreateTeam_();
//            Bundle args = new Bundle();
//            args.putString("MissionID",MissionID+"");
//            coachCreateTeam.setArguments(args);
//
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.frame, coachCreateTeam, coachCreateTeam.getClass().getName().toString());
//            fragmentTransaction.commit();

