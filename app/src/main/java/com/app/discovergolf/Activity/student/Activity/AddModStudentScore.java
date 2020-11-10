package com.app.discovergolf.Activity.student.Activity;

import android.util.Log;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Adapter.ScoreAdapter;
import com.app.discovergolf.Activity.Coach.Listeners.ScoreListener;
import com.app.discovergolf.Model.MatrixModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


@EActivity(R.layout.score)
public class AddModStudentScore extends BaseActivity implements ScoreListener {

    @ViewById
    GridView grid_Score;

    ScoreAdapter adapter;
    int StudentID = 0;
    int MissionId = 0;
    int TeamID = 0;
    ArrayList<MatrixModel> matrixModels = new ArrayList<>();

    @AfterViews
    public void init() {
        loadBaseObject();
        StudentID = Integer.parseInt(getIntent().getStringExtra("StudentID"));
        MissionId = getIntent().getIntExtra("missionId", 0);
        TeamID = getIntent().getIntExtra("TeamID", 0);
        MatrixAPI();
        adapter = new ScoreAdapter(this, matrixModels);
        grid_Score.setAdapter(adapter);
        adapter.attachMod(this);
    }

    private void MatrixAPI() {
//        http://solutioncode.in/webApi.php?action=getModScoreByMetrics&mission_id=28&student_id=27
        Utility.showProgress(this);
        String url = WebUtility.BASE_URL + "?action=" + WebUtility.GetModScoreByMetrics + "&mission_id=" + MissionId + "&student_id=" + StudentID;
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("Response--->", s);
                        Utility.hideProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String ErrorCode = jsonObject.getString("error_code");
                            String ErrorMsg = jsonObject.getString("error_message");
                            if (ErrorCode.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MatrixModel matrixModel = new MatrixModel();
                                    matrixModel.MatrixID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                                    matrixModel.MatrixTitle = jsonArray.getJSONObject(i).getString("title");
                                    matrixModel.MatrixImage = jsonArray.getJSONObject(i).getString("itm_img");
                                    matrixModel.MatrixScore = Integer.parseInt(jsonArray.getJSONObject(i).getString("totalScore"));
                                    matrixModels.add(matrixModel);
                                }
                                adapter.notifyDataSetChanged();
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

    private void ScoreAPI(int MatrixID,int Score) {
        Utility.showProgress(this);
        String url = WebUtility.BASE_URL + "?action=" + WebUtility.CreateModScore + "&student_id=" + StudentID + "&metrics_id=" + MatrixID + "&team_id=" + TeamID + "&mission_id=" + MissionId+"&scoreCount="+Score;
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("Response--->", s);
                        Utility.hideProgress();
                        if(ModTeamScore.instance!=null){
                            ModTeamScore.instance.getTeamScoreAPI();
                            ModTeamScore.instance.scoreAdapter.notifyDataSetChanged();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(s);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onAddScore(int MatrixID,int Score) {
        ScoreAPI(MatrixID,Score);
    }
}
