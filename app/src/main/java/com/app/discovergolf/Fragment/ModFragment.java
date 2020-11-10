package com.app.discovergolf.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.StudentScorePopUp_;
import com.app.discovergolf.Activity.Coach.Adapter.StudentScoreAdapter;
import com.app.discovergolf.Activity.Coach.Adapter.StudentScoreAdapterNoPop;
import com.app.discovergolf.Activity.Coach.Listeners.MatrixSelectedListener;
import com.app.discovergolf.Adapter.TeamDetailAdapter;
import com.app.discovergolf.Model.StudentScoreModel;
import com.app.discovergolf.Model.TeamScoreModel;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.Datum;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.StudentDetailsListResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class ModFragment extends Fragment {

    public ModFragment() {
        // Required empty public constructor
    }
    ArrayList<TeamScoreModel> teamScoreModels = new ArrayList<>();
    ArrayList<StudentScoreModel> studentScoreModels = new ArrayList<>();
    int TeamID = 0;
    int missionId = 0;
    StudentScoreAdapterNoPop scoreAdapter;
    RecyclerView rv;
    TextView team_name;
    TextView score;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_live, container, false);
        rv=(RecyclerView)v.findViewById(R.id.rv);
        rv=(RecyclerView)v.findViewById(R.id.rv);
        team_name=(TextView) v.findViewById(R.id.team_name);
        score=(TextView) v.findViewById(R.id.score);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        scoreAdapter= new StudentScoreAdapterNoPop(getActivity(),teamScoreModels);
        rv.setAdapter(scoreAdapter);
        scoreAdapter.ModFragment(new MatrixSelectedListener() {
            @Override
            public void onMatrixClick(TeamScoreModel teamScoreModel) {
                if(teamScoreModel.studentScoreModels.size()>0)
                    startActivity(new Intent(getActivity(),StudentScorePopUp_.class).putExtra("StudentScore",teamScoreModel.studentScoreModels));
                else
                    Toasty.info(getActivity(), "No Student Found Yet.", Toast.LENGTH_SHORT, true).show();

            }
        });
            getTeamScoreAPI();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //getTeamScoreAPI();
    }

    @Override
    public void onStop() {
        super.onStop();
        //teamScoreModels.clear();
    }

    public void getTeamScoreAPI() {
        Utility.showProgress(getActivity());
        String url = WebUtility.BASE_URL + "?action=" + WebUtility.MODMatricsStdListWithScore +
                "&team_id=" + getActivity().getIntent().getStringExtra("modTeam_id");
        Log.e("url+++0,",url);
        //Toast.makeText(getActivity(), "called", Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.GET, url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Team Response-->", s);
                        try {
                            Utility.hideProgress();
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            int s1=0;
                            if (error_code.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    TeamScoreModel scoreModel = new TeamScoreModel();
                                    scoreModel.MatrixID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                                    scoreModel.MatrixName = jsonArray.getJSONObject(i).getString("title");
                                    scoreModel.MatrixImage = jsonArray.getJSONObject(i).getString("itm_img");
                                    scoreModel.MatrixValue = Integer.parseInt(jsonArray.getJSONObject(i).getString("metrics_val"));
                                    scoreModel.MatrixAchivement = Integer.parseInt(jsonArray.getJSONObject(i).getString("metrics_archivement"));
                                    scoreModel.TotalScore = Integer.parseInt(jsonArray.getJSONObject(i).getString("totalScore"));
                                    scoreModel.TotalStudent = Integer.parseInt(jsonArray.getJSONObject(i).getString("totalStudent"));

                                    JSONArray array = jsonArray.getJSONObject(i).getJSONArray("teamStd");
                                    for (int j = 0; j < array.length(); j++) {
                                        StudentScoreModel studentScoreModel = new StudentScoreModel();
                                        studentScoreModel.StudentID = Integer.parseInt(array.getJSONObject(j).getString("student_id"));
                                        studentScoreModel.StudentName = array.getJSONObject(j).getString("name");
                                        studentScoreModel.StudentImage = array.getJSONObject(j).getString("profile_img");
                                        studentScoreModel.StudentTotalScore = Integer.parseInt(array.getJSONObject(j).getString("myTotalScore"));
                                        s1=s1+studentScoreModel.StudentTotalScore;
                                        scoreModel.studentScoreModels.add(studentScoreModel);
                                        studentScoreModels.add(studentScoreModel);
                                    }
                                    if (array.length() == 1) {
                                        scoreModel.StudentOneImage = studentScoreModels.get(0).StudentImage;
                                    } else if (array.length() == 2) {
                                        scoreModel.StudentOneImage = studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage = studentScoreModels.get(1).StudentImage;
                                    } else if (array.length() == 3) {
                                        scoreModel.StudentOneImage = studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage = studentScoreModels.get(1).StudentImage;
                                        scoreModel.StudentThreeImage = studentScoreModels.get(2).StudentImage;
                                    } else if (array.length() == 4) {
                                        scoreModel.StudentOneImage = studentScoreModels.get(0).StudentImage;
                                        scoreModel.StudentTwoImage = studentScoreModels.get(1).StudentImage;
                                        scoreModel.StudentThreeImage = studentScoreModels.get(2).StudentImage;
                                        scoreModel.StudentFourImage = studentScoreModels.get(3).StudentImage;
                                    }
                                    teamScoreModels.add(scoreModel);
                                }

                                score.setText(s1+"");
                                team_name.setText(getActivity().getIntent().getStringExtra("modTeam_Name"));

                                scoreAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception ex) {
                            Utility.showProgress(getActivity());
                            Log.e("error","=="+ex.toString());
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
