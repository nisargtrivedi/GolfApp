package com.app.discovergolf.Activity;

import android.content.Intent;

import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.create_team)
public class CreateTeam extends BaseActivity {

    @AfterViews
    public void init(){
        loadBaseObject();
    }
    @Click
    public void SaveTeam(){
        startActivity(new Intent(this,MODClass_.class));
    }
}
