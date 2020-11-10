package com.app.discovergolf.Activity.Coach.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.BaseActivity;
import com.app.discovergolf.Activity.Coach.Adapter.ScoreAdapter;
import com.app.discovergolf.Activity.Coach.Listeners.ScoreListener;
import com.app.discovergolf.Activity.Login;
import com.app.discovergolf.Model.MatrixModel;
import com.app.discovergolf.Parser.CoachLoginParser;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


@EActivity(R.layout.score)
public class AddStudentScore extends BaseActivity implements ScoreListener {

    @ViewById
    GridView grid_Score;

    ScoreAdapter adapter;
    int StudentID=0;
    ArrayList<MatrixModel> matrixModels=new ArrayList<>();
    String MusicFile="";
    MediaPlayer mPlayer;
    @AfterViews
    public void init(){
        loadBaseObject();
        StudentID=Integer.parseInt(getIntent().getStringExtra("StudentID"));
        MusicFile=getIntent().getStringExtra("music");
        MatrixAPI();
        adapter=new ScoreAdapter(this,matrixModels);
        grid_Score.setAdapter(adapter);
        adapter.attach(this);
    }
    private void MatrixAPI() {
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action="+WebUtility.GetScorewithMatrix+"&student_id="+StudentID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("Response--->",s);
                        Utility.hideProgress();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            String ErrorCode=jsonObject.getString("error_code");
                            String ErrorMsg=jsonObject.getString("error_message");
                            if(ErrorCode.equalsIgnoreCase("0")){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    MatrixModel matrixModel=new MatrixModel();
                                    matrixModel.MatrixID=Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                                    matrixModel.MatrixTitle=jsonArray.getJSONObject(i).getString("title");
                                    matrixModel.MatrixImage=jsonArray.getJSONObject(i).getString("itm_img");
                                    matrixModel.MatrixScore=Integer.parseInt(jsonArray.getJSONObject(i).getString("totalScore"));
                                    matrixModels.add(matrixModel);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void ScoreAPI(int MatrixID,int Score) {
        Log.i("Score",Score+"");
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action="+WebUtility.CreateScore+"&student_id="+StudentID+"&metrics_id="+MatrixID+"&coach_id="+appPreferences.getString("COACHID")+"&scoreCount="+Score,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("Response--->",s);
                        Utility.hideProgress();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int errorcode=jsonObject.getInt("error_code");
                            if(errorcode==0) {
                                  try {
                                      mPlayer = new MediaPlayer();

                                      // Set the media player audio stream type
                                      mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                      if(!MusicFile.isEmpty()) {
                                          mPlayer.setDataSource(MusicFile);
                                          mPlayer.prepare();

                                          mPlayer.start();
                                      }

                                } catch (IOException e) {
                                    // Catch the exception
                                    e.printStackTrace();
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                    }
                                });
                            }else{
                                Utility.hideProgress();
                            }
                        } catch (Exception e) {
                            Utility.hideProgress();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPlayer!=null) {
            if (mPlayer.isPlaying())
                mPlayer.stop();
                mPlayer.release();
        }
    }

    private long lastClickTime = 0;
    private boolean doubleClick=false;
    @Override
    public void onAddScore(int MatrixID,int Score) {
//        i++;
//        Handler handler=new Handler();
//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//                    i=0;
//            }
//        };
//        handler.postDelayed(runnable,4000);
//        if(i==1){
//            showSuccessMsg("single");
//
//           // ScoreAPI(MatrixID,Score);
//        }
//        else if(i==2){
//            showSuccessMsg("double");
//            //ScoreAPI(MatrixID,Score);
//        }
//        else{
//            i=0;
//        }
//        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
//            doubleClick=true;
//        }
//        lastClickTime = SystemClock.elapsedRealtime();
//        if(doubleClick==true){
//            Score=Score+10;
            ScoreAPI(MatrixID,Score);
//        }else{
//            Score++;
//            ScoreAPI(MatrixID,Score);
//        }
    }
}
