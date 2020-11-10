package com.app.discovergolf.Activity.Coach.Activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.app.discovergolf.Activity.Coach.Adapter.CoachCreateTeamAdapter;
import com.app.discovergolf.Activity.Coach.Adapter.MusicAdapter;
import com.app.discovergolf.Activity.Coach.Fragment.MissionTeam;
import com.app.discovergolf.Listeners.OnMusicclick;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

@EActivity(R.layout.create_team)
public class CoachCreateTeam extends BaseActivity {

    @ViewById
    RecyclerView RvPlayers;
    @ViewById
    AppCompatButton SaveTeam;
    @ViewById
    EditText txtTeamName;
    @ViewById
    ImageView imgMusic;

    int MissionID=0;
    int ClassMission=0;
    static  int MusicID=0;
    ArrayList<PlayerModel> playerModels = new ArrayList<>();
    CoachCreateTeamAdapter createTeamAdapter;

    @AfterViews
    public void init(){
        loadBaseObject();
        try {
            MissionID = Integer.parseInt(getIntent().getStringExtra("MissionID"));
            ClassMission = Integer.parseInt(getIntent().getStringExtra("ClassMission"));
            Log.i("CLASS MISSION-->", ClassMission + "");
            Log.i("MISSION ID-->", MissionID + "");
            getStudentListAPI();
            imgMusic.setVisibility(View.VISIBLE);
            imgMusic.setImageResource(R.drawable.music_note);
        }catch(Exception ex){

        }
    }

    @Click
    public void imgMusic(){
        try {
            openMusicPopUP();
        }catch (Exception ex)
        {

        }
    }
    public void openMusicPopUP() throws Exception{
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
                    if(model.MID!=null)
                        MusicID=Integer.parseInt(model.MID);
                    adapter.notifyDataSetChanged();

                }
            });


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
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }


    }
    //Create Team Player List
    @Click
    public void SaveTeam(){
        try {
            if (!TextUtils.isEmpty(txtTeamName.getText().toString().trim())) {
                ArrayList<Integer> studentlist = new ArrayList<>();
                for (int i = 0; i < createTeamAdapter.Filterlist.size(); i++) {
                    if (createTeamAdapter.Filterlist.get(i).isSelected) {
                        studentlist.add(createTeamAdapter.Filterlist.get(i).PlayerID);
                    }
                }
                if (studentlist.size() > 0) {
                    String csvValues = android.text.TextUtils.join(",", studentlist);
                    Log.i("CSV VALUES--", csvValues);
                    if (MusicID == 0) {
                        showErrorMsg("Please select Music for Team");
                    } else
                        saveTeamAPI(csvValues);
                }
            } else {
                showErrorMsg("please enter team name");
            }
        }catch (Exception ex){

        }
    }


    //API Get All Student List
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
                                Collections.sort(playerModels, new Comparator<PlayerModel>() {
                                    @Override
                                    public int compare(PlayerModel lhs, PlayerModel rhs) {
                                        return lhs.PlayerName.compareTo(rhs.PlayerName);
                                    }
                                });
                                RvPlayers.setLayoutManager(new LinearLayoutManager(CoachCreateTeam.this));
                                createTeamAdapter=new CoachCreateTeamAdapter(CoachCreateTeam.this,playerModels);
                                RvPlayers.setAdapter(createTeamAdapter);
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
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.CreateTeam + "&coach_id="+appPreferences.getString("COACHID")+"&mission_id="+MissionID+"&music_id="+MusicID+"&student_id="+StudentList+"&title="+txtTeamName.getText().toString().trim()+"&team_id=0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Team Response", s);
                        Utility.hideProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            Utility.showSuccessMsg(CoachCreateTeam.this,error_message);
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
