package com.app.discovergolf.Activity.Coach.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.ChangePassword_;
import com.app.discovergolf.Activity.Coach.Fragment.CoachMissionHQ_;
import com.app.discovergolf.Activity.Coach.Fragment.DashboardFragment_;
import com.app.discovergolf.Activity.Coach.Fragment.MissionTeam_;
import com.app.discovergolf.Activity.Login_;
import com.app.discovergolf.Activity.MOD_;
import com.app.discovergolf.Activity.Message_;
import com.app.discovergolf.Fragment.DashboardMain;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.TTextView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EActivity(R.layout.dashboard)
public class CoachDashboard extends BaseActivity {


    @ViewById
    DrawerLayout drawer_layout;

    @ViewById
    Toolbar toolbar;

    @ViewById
    android.support.v7.widget.AppCompatImageView UserImage;
    @ViewById
    TextView UserName;
    @ViewById
    TextView Country;
    @ViewById
    LinearLayout LiveMission;
    @ViewById
    LinearLayout Home;
    @ViewById
    LinearLayout SETTINGS;
    @ViewById
    TTextView txtSettings;
    @ViewById
    LinearLayout MOD;
    @ViewById
    LinearLayout Messages;
    @ViewById
    LinearLayout LOGOUT;
    @ViewById
    LinearLayout MISSIONHQ;
    @ViewById
    com.app.discovergolf.Util.TTextView Back;
    @ViewById
    TextView tvMission;


    private static CoachDashboard instance;

    ActionBarDrawerToggle actionBarDrawerToggle;

    boolean mSlideState = false;
    boolean read = false;

    public static CoachDashboard getInstance() {
        return instance;
    }

    @AfterViews
    public void init() {
        loadBaseObject();
        instance = this;
        MOD.setVisibility(View.GONE);
        tvMission.setText("Mission Teams");
        //txtSettings.setText("Change Password");
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setDrawer();

        if (actionBar != null) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu);
            actionBar.setHomeAsUpIndicator(drawable);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        FragmentNavigation(new DashboardFragment_());


    }

    public void setTitleText(String Title) {
        Back.setText(Title);
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
         //Log.i("Fragment",getSupportFragmentManager().getBackStackEntryCount()+"");
//        if(getSupportFragmentManager().getBackStackEntryCount()==0){
//            finish();
//        }
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            //FragmentNavigation(new DashboardFragment_());
//        } else {
//            finish();
//        }



        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for(Fragment f : fragmentList) {
            Log.i("FRAGMENT NAME-->",f.getTag());
            if(f.getTag().equalsIgnoreCase("com.app.discovergolf.Activity.Coach.Fragment.CoachMissionHQ_")){
                FragmentNavigation(new DashboardFragment_());
            }
            else if(f.getTag().equalsIgnoreCase("com.app.discovergolf.Activity.Coach.Fragment.DashboardFragment_")){
                handled=true;
                break;
            }
            else if(f.getTag().equalsIgnoreCase("com.app.discovergolf.Activity.Coach.Fragment.MissionTeam_")){
                FragmentNavigation(new DashboardFragment_());
            }
        }

        if(!handled) {
            finish();
        }

        //
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(0).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    public void FragmentNavigation(android.support.v4.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getName().toString());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShowHeaderData();
    }

    @Click
    public void Home() {
        drawer_layout.closeDrawers();
        Home.setSelected(true);
        SETTINGS.setSelected(false);
        LiveMission.setSelected(false);
        Messages.setSelected(false);
        MISSIONHQ.setSelected(false);
        FragmentNavigation(new DashboardFragment_());

    }

    @Click
    public void LiveMission() {
        drawer_layout.closeDrawers();
        Home.setSelected(false);
        SETTINGS.setSelected(false);
        LiveMission.setSelected(true);
        Messages.setSelected(false);
        MISSIONHQ.setSelected(false);
        FragmentNavigation(new MissionTeam_());
        //startActivity(new Intent(this,LiveMission_.class));

    }

    @Click
    public void Messages() {
        drawer_layout.closeDrawers();
        Home.setSelected(false);
        SETTINGS.setSelected(false);
        LiveMission.setSelected(false);
        Messages.setSelected(true);
        MISSIONHQ.setSelected(false);

    }

    @Click
    public void MOD() {
        drawer_layout.closeDrawers();
        startActivity(new Intent(this, MOD_.class));
    }

    @Click
    public void img_Chat() {
        drawer_layout.closeDrawers();
        startActivity(new Intent(this, Message_.class));
    }

    @Click
    public void SETTINGS() {
        Home.setSelected(false);
        SETTINGS.setSelected(true);
        LiveMission.setSelected(false);
        Messages.setSelected(false);
        MISSIONHQ.setSelected(false);
        drawer_layout.closeDrawers();
//        showWarningMsg("Under Development");
        startActivity(new Intent(this, ChangePassword_.class).putExtra("isCoch", true));
    }

    @Click
    public void MISSIONHQ() {
        drawer_layout.closeDrawers();
        Home.setSelected(false);
        SETTINGS.setSelected(false);
        LiveMission.setSelected(false);
        Messages.setSelected(false);
        MISSIONHQ.setSelected(true);
        FragmentNavigation(new CoachMissionHQ_());
        //startActivity(new Intent(this,MissionHQ_.class));
    }


    // ALL Private Methods
    private void ShowHeaderData() {
        Picasso.with(this).load(appPreferences.getString("COACH_PHOTO")).transform(new CircleTransform()).into(UserImage);
        UserName.setText(appPreferences.getString("COACH_FULLNAME"));
        Country.setText(appPreferences.getString("COACH_CITY"));
    }

    @Click
    public void LOGOUT() {
        appPreferences.set("COACH_PHOTO", "");
        appPreferences.set("COACH_FULLNAME", "");
        appPreferences.set("COACH_CITY", "");
        appPreferences.set("COACHID", "");
        finish();
        startActivity(new Intent(this, Login_.class));
    }

}
