package com.app.discovergolf.Activity;

import android.content.Intent;

import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.act_mod_class)
public class MODClass extends BaseActivity {

    @AfterViews
    public void init(){
        loadBaseObject();
    }
    @Click
    public void EditTeam(){
        finish();
    }

    @Click
    public void btnAddPoint(){
        startActivity(new Intent(this,Score_.class));
    }
    @Click
    public void teamSelect(){
        startActivity(new Intent(this,TeamDetailScore_.class));
    }

}
