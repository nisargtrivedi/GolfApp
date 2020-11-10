package com.app.discovergolf.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.LiveMissionClassView;
import com.app.discovergolf.Adapter.TeamDetailAdapter;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.Datum;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.StudentDetailsListResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class LiveFragment extends Fragment {

    public LiveFragment() {
        // Required empty public constructor
    }
    RecyclerView rv;
    ArrayList<Datum> dataa = new ArrayList<>();
    TeamDetailAdapter teamDetailAdapter;
    TextView team_name;
    TextView score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_live, container, false);
        rv=(RecyclerView)v.findViewById(R.id.rv);
        team_name=(TextView) v.findViewById(R.id.team_name);
        score=(TextView) v.findViewById(R.id.score);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        teamDetailAdapter= new TeamDetailAdapter(getActivity(),dataa);
        rv.setAdapter(teamDetailAdapter);
        Log.i("TEAM ID-->",getActivity().getIntent().getStringExtra("team_id")+"-");
        getLiveMission();


        return v;
    }

    public void getLiveMission() {
        final Context context = getActivity();
       // Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        Call<StudentDetailsListResponse> call = api.getTeamDetails(WebUtility.Matrics_List_Score, getActivity().getIntent().getStringExtra("team_id"));
        call.enqueue(new Callback<StudentDetailsListResponse>() {
            @Override
            public void onResponse(Call<StudentDetailsListResponse> call, final retrofit2.Response<StudentDetailsListResponse> response) {
               // Utility.hideProgress();
                int sco=0;
                dataa.addAll(response.body().getData());
                for(int i=0;i<response.body().getData().size();i++){
                    if(response.body().getData().get(i).teamStd!=null) {
                        for (int j = 0; j < response.body().getData().get(i).teamStd.size(); j++)
                            sco = sco + response.body().getData().get(i).teamStd.get(j).getMyTotalScore();
                    }

                }
                teamDetailAdapter.notifyDataSetChanged();
                score.setText(sco+"");
                team_name.setText(getActivity().getIntent().getStringExtra("name"));
                //score.setText(getActivity().getIntent().getStringExtra("score"));

            }
            @Override
            public void onFailure(Call<StudentDetailsListResponse> call, Throwable t) {
             //   Utility.hideProgress();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
     //   dataa.clear();
    }
}
