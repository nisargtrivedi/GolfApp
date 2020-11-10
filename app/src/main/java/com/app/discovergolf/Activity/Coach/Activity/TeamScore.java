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
import com.app.discovergolf.Activity.Coach.Adapter.StudentScoreAdapter;
import com.app.discovergolf.Activity.Coach.Listeners.MatrixSelectedListener;
import com.app.discovergolf.Model.MissionTeamModel;
import com.app.discovergolf.Model.StudentScoreModel;
import com.app.discovergolf.Model.TeamScoreModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.selected_team_details)
public class TeamScore extends BaseActivity implements MatrixSelectedListener {

    ArrayList<TeamScoreModel> teamScoreModels=new ArrayList<>();
    ArrayList<StudentScoreModel> studentScoreModels=new ArrayList<>();
    int TeamID=0;
    StudentScoreAdapter scoreAdapter;

    @ViewById
    RecyclerView RvTeamsMatrix;
    @ViewById
    com.app.discovergolf.Util.TTextView TeamName;

    @AfterViews
    public void init(){
        TeamID=Integer.parseInt(getIntent().getStringExtra("TeamID"));
        TeamName.setText(getIntent().getStringExtra("TeamName"));
        loadBaseObject();
        getTeamScoreAPI();
        scoreAdapter=new StudentScoreAdapter(this,teamScoreModels);
        RvTeamsMatrix.setLayoutManager(new LinearLayoutManager(this));
        RvTeamsMatrix.setAdapter(scoreAdapter);
        scoreAdapter.attach(this);
    }

    public void getTeamScoreAPI() {
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetMatrixScore + "&team_id=" + TeamID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Team Response-->", s);
                        try {
                            Utility.hideProgress();
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");

                            if(error_code.equalsIgnoreCase("0")){
                                JSONArray jsonArray=object.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    TeamScoreModel scoreModel=new TeamScoreModel();
                                    scoreModel.MatrixID=Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                                    scoreModel.MatrixName=jsonArray.getJSONObject(i).getString("title");
                                    scoreModel.MatrixImage=jsonArray.getJSONObject(i).getString("itm_img");
                                    scoreModel.MatrixValue=Integer.parseInt(jsonArray.getJSONObject(i).getString("metrics_val"));
                                    scoreModel.MatrixAchivement=Integer.parseInt(jsonArray.getJSONObject(i).getString("metrics_archivement"));
                                    scoreModel.TotalScore=Integer.parseInt(jsonArray.getJSONObject(i).getString("totalScore"));
                                    scoreModel.TotalStudent=Integer.parseInt(jsonArray.getJSONObject(i).getString("totalStudent"));

                                    JSONArray array=jsonArray.getJSONObject(i).getJSONArray("teamStd");
                                    for(int j=0;j<array.length();j++){
                                        StudentScoreModel studentScoreModel=new StudentScoreModel();
                                        studentScoreModel.StudentID=Integer.parseInt(array.getJSONObject(j).getString("student_id"));
                                        studentScoreModel.StudentName=array.getJSONObject(j).getString("name");
                                        studentScoreModel.StudentImage=array.getJSONObject(j).getString("profile_img");
                                        studentScoreModel.StudentTotalScore=Integer.parseInt(array.getJSONObject(j).getString("myTotalScore"));
                                        scoreModel.studentScoreModels.add(studentScoreModel);
                                        studentScoreModels.add(studentScoreModel);
                                    }
                                    if(array.length()==1){
                                        scoreModel.StudentOneImage=studentScoreModels.get(0).StudentImage;
                                    }
                                    else if(array.length()==2){
                                        scoreModel.StudentOneImage=studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage=studentScoreModels.get(1).StudentImage;
                                    }
                                    else if(array.length()==3){
                                        scoreModel.StudentOneImage=studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage=studentScoreModels.get(1).StudentImage;
                                        scoreModel.StudentThreeImage=studentScoreModels.get(2).StudentImage;
                                    }
                                    else if(array.length()==4){
                                        scoreModel.StudentOneImage=studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage=studentScoreModels.get(1).StudentImage;
                                        scoreModel.StudentThreeImage=studentScoreModels.get(2).StudentImage;
                                        scoreModel.StudentFourImage=studentScoreModels.get(3).StudentImage;
                                    }
                                    else if(array.length()>4){
                                        scoreModel.StudentOneImage=studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage=studentScoreModels.get(1).StudentImage;
                                        scoreModel.StudentThreeImage=studentScoreModels.get(2).StudentImage;
                                        scoreModel.StudentFourImage=studentScoreModels.get(3).StudentImage;
                                    }
                                    teamScoreModels.add(scoreModel);
                                }
                                scoreAdapter.notifyDataSetChanged();
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

    @Override
    public void onMatrixClick(TeamScoreModel teamScoreModel) {

        if(teamScoreModel.studentScoreModels.size()>0)
            startActivity(new Intent(this,StudentScorePopUp_.class).putExtra("StudentScore",teamScoreModel.studentScoreModels));
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
}
