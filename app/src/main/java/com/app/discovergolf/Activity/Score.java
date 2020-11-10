package com.app.discovergolf.Activity;

import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.score)
public class Score extends BaseActivity {

    @AfterViews
    public void init()
    {
        loadBaseObject();
    }
}
