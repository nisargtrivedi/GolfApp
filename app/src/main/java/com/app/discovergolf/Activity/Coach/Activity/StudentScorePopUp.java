package com.app.discovergolf.Activity.Coach.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Adapter.IndividualScoreAdapter;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.Model.StudentScoreModel;
import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@EActivity(R.layout.team_player_popup)
public class StudentScorePopUp extends BaseActivity {

    @ViewById
    RecyclerView RvStudents;
    IndividualScoreAdapter scoreAdapter;
    ArrayList<StudentScoreModel> studentScoreModels=new ArrayList<>();

    @AfterViews
    public void init(){
        loadBaseObject();
        studentScoreModels= (ArrayList<StudentScoreModel>) getIntent().getSerializableExtra("StudentScore");

        Collections.sort(studentScoreModels, new Comparator<StudentScoreModel>() {
            @Override
            public int compare(StudentScoreModel lhs, StudentScoreModel rhs) {
                return rhs.StudentTotalScore-lhs.StudentTotalScore;
            }
        });

        RvStudents.setLayoutManager(new LinearLayoutManager(this));
        scoreAdapter=new IndividualScoreAdapter(this,studentScoreModels);
        RvStudents.setAdapter(scoreAdapter);
    }
}
