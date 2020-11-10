package com.app.discovergolf.Activity;

import android.support.v7.widget.RecyclerView;

import com.app.discovergolf.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.popup_window)
public class ScorePopup extends BaseActivity {
    @ViewById
    RecyclerView rv_scores;

    @AfterViews
    public void init() {
        loadBaseObject();
    }
}
