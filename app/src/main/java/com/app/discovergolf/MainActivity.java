package com.app.discovergolf;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.CoachDashboard_;
import com.app.discovergolf.Activity.Dashboard_;
import com.app.discovergolf.Activity.Login;
import com.app.discovergolf.Activity.Login_;
import com.app.discovergolf.Model.MusicModel;
import com.app.discovergolf.Parser.CoachLoginParser;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.DataContext;
import com.app.discovergolf.Util.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobandme.ada.Entity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    AppPreferences appPreferences;

    ArrayList<MusicModel> musicModels=new ArrayList<>();
    DataContext dataContext;
    @AfterViews
    public void init() {
        appPreferences = new AppPreferences(this);
        dataContext=new DataContext(this);
        try {
            musicAPI();
        }catch (Exception ex){

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0,0);
                if (!TextUtils.isEmpty(appPreferences.getString("COACHID"))) {
                    startActivity(new Intent(MainActivity.this, CoachDashboard_.class));
                } else if (!TextUtils.isEmpty(appPreferences.getString("STUDENTID"))) {
                    startActivity(new Intent(MainActivity.this, Dashboard_.class));
                } else {
                    startActivity(new Intent(MainActivity.this, Login_.class));
                }
/*

                if (TextUtils.isEmpty(appPreferences.getString("COACHID")))
                    startActivity(new Intent(MainActivity.this, Login_.class));
                else
                    startActivity(new Intent(MainActivity.this, CoachDashboard_.class));*/
            }
        }, 4000);
    }

    private void musicAPI() {
//        Utility.showProgress(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetMUSIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Response--->", s);
                        //Utility.hideProgress();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONArray array=jsonObject.getJSONArray("data");
                            musicModels.clear();
                            for(int i=0;i<array.length();i++){
                                MusicModel musicModel=new MusicModel();
                                Log.i("MID--->",array.getJSONObject(i).getInt("id")+"");
                                musicModel.MID=array.getJSONObject(i).getInt("id")+"";
                                musicModel.file_path=array.getJSONObject(i).getString("file_path");
                                musicModel.title=array.getJSONObject(i).getString("title");
                                musicModels.add(musicModel);
                            }
                            dataContext.musicModelObjectSet.fill();
                            for(int i=0;i<dataContext.musicModelObjectSet.size();i++)
                                dataContext.musicModelObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                            dataContext.musicModelObjectSet.save();
                            dataContext.musicModelObjectSet.addAll(musicModels);
                            dataContext.musicModelObjectSet.save();
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }
}
