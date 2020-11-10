package com.app.discovergolf.Activity;

import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.team_player_popup)
public class TeamPlayer extends BaseActivity {

    @AfterViews
    public void init(){
        loadBaseObject();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Click
    public void close(){
        finish();
    }
}
