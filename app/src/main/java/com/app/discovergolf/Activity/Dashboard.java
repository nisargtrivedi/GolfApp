package com.app.discovergolf.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.student.Activity.MODActivity_;
import com.app.discovergolf.Fragment.DashboardMain;
import com.app.discovergolf.Fragment.DashboardMain_;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.DateUtils;
import com.app.discovergolf.Util.Utility;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit2.Call;
import retrofit2.Callback;


@EActivity(R.layout.dashboard)
public class Dashboard extends BaseActivity {

    @ViewById
    DrawerLayout drawer_layout;

    @ViewById
    Toolbar toolbar;

    @ViewById
    ImageView UserImage;

    @ViewById
    TextView UserName;


    @ViewById
    TextView Country;

    @ViewById
    LinearLayout LOGOUT;

    @ViewById
    LinearLayout MOD;
    @ViewById
    ImageView img_Chat;


    private static Dashboard instance;

    ActionBarDrawerToggle actionBarDrawerToggle;

    boolean mSlideState = false;
    boolean read = false;

    public static Dashboard getInstance() {
        return instance;
    }

    @AfterViews
    public void init() {
        loadBaseObject();
        instance = this;
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setDrawer();
        MOD.setVisibility(View.VISIBLE);
        img_Chat.setVisibility(View.INVISIBLE);

        if (actionBar != null) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu);
            actionBar.setHomeAsUpIndicator(drawable);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        FragmentNavigation(new DashboardMain_());
        ShowHeaderData();


    }


    void setDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;//is Closed

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;//is Closed
            }
        };
        drawer_layout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        drawer_layout.setDrawerShadow(android.R.color.white, GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        drawer_layout.closeDrawers();
        DashboardMain dashboardMain= (DashboardMain) getSupportFragmentManager().findFragmentByTag("DashboardMain_");

        finish();
        overridePendingTransition(0,0);
        super.onBackPressed();
//        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
//            finish();
//        }
//        else {
//            super.onBackPressed();
//        }
        //finish();
    }


    public void FragmentNavigation(android.support.v4.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getName().toString());
        fragmentTransaction.addToBackStack(fragment.getClass().getName().toString());
        fragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Click
    public void LiveMission() {
        drawer_layout.closeDrawers();
        //startActivity(new Intent(this, LiveMission_.class));
        getLiveMission();

    }
    public void getLiveMission() {
        final Context context = this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<LiveMissionResponse> call = api.getLiveMission(WebUtility.LiveMissionDetails, appPreferences.getString("STUDENTID").toString());
        call.enqueue(new Callback<LiveMissionResponse>() {
            @Override
            public void onResponse(Call<LiveMissionResponse> call, retrofit2.Response<LiveMissionResponse> response) {
                Utility.hideProgress();
                Log.i("RESPONSE--->",response.body().getData().getTitle().toString());
                if (response.body().getData().getCreateTeam().toString().equals("1")) {
                    Intent intent = new Intent(Dashboard.this, LiveMission_.class);
                    intent.putExtra("title", response.body().getData().getTitle());
                    intent.putExtra("scheduled_date  ", DateUtils.GolfDate(response.body().getData().getScheduledDate()));
                    intent.putExtra("description", response.body().getData().getDescription());
                    intent.putExtra("std_video", response.body().getData().getStdVideo());
                    intent.putExtra("student_instructions", response.body().getData().getStudentInstructions());
                    //context.startActivity(new Intent(context, LiveMission_.class));
                    startActivity(intent);
                } else if (response.body().getData().getCreateTeam().toString().equals("0")) {
                    Intent intent = new Intent(Dashboard.this, LiveMissionClassView_.class);
                    intent.putExtra("title", response.body().getData().getTitle());
                    intent.putExtra("scheduled_date  ", DateUtils.GolfDate(response.body().getData().getScheduledDate()));
                    intent.putExtra("description", response.body().getData().getDescription());
                    intent.putExtra("std_video", response.body().getData().getStdVideo());
                    intent.putExtra("student_instructions", response.body().getData().getStudentInstructions());
                    intent.putExtra("team_id", response.body().getData().getMissionTeam().get(0).getId().toString());
                    //context.startActivity(new Intent(context, LiveMission_.class));
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LiveMissionResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }

    @Click
    public void MOD() {
        drawer_layout.closeDrawers();
        startActivity(new Intent(this, MODActivity_.class));
    }

    @Click
    public void img_Chat() {
        drawer_layout.closeDrawers();
        startActivity(new Intent(this, Message_.class));
    }

    @Click
    public void SETTINGS() {
        drawer_layout.closeDrawers();
        startActivity(new Intent(this, ChangePassword_.class).putExtra("isCoch", false));
    }

    @Click
    public void MISSIONHQ() {
        drawer_layout.closeDrawers();
        startActivity(new Intent(this, MissionHQ_.class));
    }

    @Click
    public void LOGOUT() {

        appPreferences.set("STUDENTID", "");
        appPreferences.set("STUDENT_FULLNAME", "");
        appPreferences.set("CITY", "");
        appPreferences.set("STUDENT_PHOTO", "");
        appPreferences.set("USERTYPE", "");
        appPreferences.set("FIRST_NAME", "");
        appPreferences.set("LAST_NAME", "");
        appPreferences.set("ABOUT", "");
        appPreferences.set("CLASS_ID", "");
        finish();
        startActivity(new Intent(this, Login_.class));
    }

    private void ShowHeaderData() {
        Picasso.with(this).load(appPreferences.getString("STUDENT_PHOTO")).transform(new CircleTransform()).into(UserImage);
        UserName.setText(appPreferences.getString("STUDENT_FULLNAME"));
        Country.setText(appPreferences.getString("CITY"));
    }
}
