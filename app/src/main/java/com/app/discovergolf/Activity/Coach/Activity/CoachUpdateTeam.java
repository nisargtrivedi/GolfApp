package com.app.discovergolf.Activity.Coach.Activity;

import android.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Adapter.CoachCreateTeamAdapter;
import com.app.discovergolf.Activity.Coach.Adapter.MusicAdapter;
import com.app.discovergolf.Activity.Coach.Fragment.MissionTeam;
import com.app.discovergolf.Listeners.OnMusicclick;
import com.app.discovergolf.Model.MissionTeamPlayerModel;
import com.app.discovergolf.Model.MusicModel;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.create_team)
public class CoachUpdateTeam extends BaseActivity {

    @ViewById
    RecyclerView RvPlayers;
    @ViewById
    AppCompatButton SaveTeam;
    @ViewById
    EditText txtTeamName;
    @ViewById
    com.app.discovergolf.Component.TTextview TeamTitle;

    static  int MusicID=0;
    int TeamID=0;
    int MissionID=0;
    int ClassMission=0;
    ArrayList<PlayerModel> playerModels = new ArrayList<>();
    CoachCreateTeamAdapter createTeamAdapter;

    ArrayList<MissionTeamPlayerModel> teamPlayerModels = new ArrayList<>();

    @AfterViews
    public void init(){
        teamPlayerModels.clear();
        teamPlayerModels= (ArrayList<MissionTeamPlayerModel>)getIntent().getSerializableExtra("TeamDetails");
        txtTeamName.setText(getIntent().getStringExtra("TeamName"));
        SaveTeam.setText("Update Team");
        TeamTitle.setText("Update Current Team");
        loadBaseObject();

        if(teamPlayerModels.size()>0){
            for(int i=0;i<teamPlayerModels.size();i++){
                PlayerModel model=new PlayerModel();
                model.PlayerID=Integer.parseInt(teamPlayerModels.get(i).StudentID);
                model.PlayerName=teamPlayerModels.get(i).StudentName;
                model.PlayerImage=teamPlayerModels.get(i).StudentImage;
                model.isSelected=true;
                TeamID=Integer.parseInt(teamPlayerModels.get(i).TeamID);
                MissionID=Integer.parseInt(teamPlayerModels.get(i).MissionID);
                playerModels.add(model);
                Log.i("PLAYER ID",teamPlayerModels.get(i).StudentID+"");
                Log.i("PLAYER Name",model.PlayerName);
                Log.i("PLAYER SELECTED",model.isSelected+"");
            }
            getStudentListAPI();
            RvPlayers.setLayoutManager(new LinearLayoutManager(CoachUpdateTeam.this));
            createTeamAdapter=new CoachCreateTeamAdapter(CoachUpdateTeam.this,playerModels);
            RvPlayers.setAdapter(createTeamAdapter);
        }

    }
    @Click
    public void imgMusic(){
        openMusicPopUP();
    }
    public void openMusicPopUP(){
        View view=LayoutInflater.from(this).inflate(R.layout.music_select_dialog,null,false);
        RecyclerView RvMusic=view.findViewById(R.id.RvMusic);
        AppCompatButton DoneMusic=view.findViewById(R.id.DoneMusic);
        final MusicAdapter adapter;
        try {
            dataContext.musicModelObjectSet.fill();
            adapter=new MusicAdapter(this,dataContext.musicModelObjectSet);
            RvMusic.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            RvMusic.setAdapter(adapter);

            adapter.CLick(new OnMusicclick() {
                @Override
                public void onMusic(MusicModel model) {
                    if(model.MID!=null) {
                        MusicID = Integer.parseInt(model.MID);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        final AlertDialog dialog;
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setView(view);
        builder.create();
        dialog=builder.show();
        DoneMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void getStudentListAPI() {
        //+appPreferences.getString("COACHID")
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetAllStudentList + "&coach_id="+appPreferences.getString("COACHID")+"&mission_id="+MissionID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Class Response", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            JSONArray jsonArray=object.getJSONArray("data");
                            if(error_code.equalsIgnoreCase("0")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PlayerModel playerModel = new PlayerModel();
                                    playerModel.PlayerID = jsonArray.getJSONObject(i).getInt("id");
                                    playerModel.PlayerImage = jsonArray.getJSONObject(i).getString("profile_img");
                                    playerModel.PlayerName = jsonArray.getJSONObject(i).getString("name");
                                    playerModel.isAddedTeam=Integer.parseInt(jsonArray.getJSONObject(i).getString("isAddedTeam"));
                                    playerModel.isClassMission=ClassMission;
                                    if(playerModel.isAddedTeam==0) {
                                        playerModels.add(playerModel);
                                    }
                                }
                                createTeamAdapter.notifyDataSetChanged();
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


    //Create Team Player List
    @Click
    public void SaveTeam(){
        ArrayList<Integer> studentlist=new ArrayList<>();
        for(int i=0;i<createTeamAdapter.Filterlist.size();i++){
            if(createTeamAdapter.Filterlist.get(i).isSelected){
                studentlist.add(createTeamAdapter.Filterlist.get(i).PlayerID);
            }
        }
        if(studentlist.size()>0) {
            String csvValues = android.text.TextUtils.join(",", studentlist);
            Log.i("CSV VALUES--",csvValues);
            if(MusicID==0){
                showErrorMsg("Please select Music for Team");
            }else
                saveTeamAPI(csvValues);
        }
    }



    //Save Team API
    public void saveTeamAPI(String StudentList) {
        //+appPreferences.getString("COACHID")
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.CreateTeam + "&coach_id="+appPreferences.getString("COACHID")+"&mission_id="+MissionID+"&music_id="+MusicID+"&student_id="+StudentList+"&title="+txtTeamName.getText().toString().trim()+"&team_id="+TeamID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Team Response", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            Utility.showSuccessMsg(CoachUpdateTeam.this,error_message);
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
    public void back(){
        MissionTeam.newInstance().shouldRefreshOnResume=true;
        finish();
        overridePendingTransition(0,0);
    }
}
