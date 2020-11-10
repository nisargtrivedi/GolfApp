package com.app.discovergolf.Activity;

import android.content.Intent;

import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.act_mod)
public class MOD extends BaseActivity {

    @AfterViews
    public void init(){
        loadBaseObject();
    }

    @Click
    public void btnCreateTeam(){
            startActivity(new Intent(this,CreateTeam_.class));
    }
}
