package com.app.discovergolf.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.LiveMissionClassView_;
import com.app.discovergolf.Activity.LiveMission_;
import com.app.discovergolf.Activity.student.Activity.MODActivity_;
import com.app.discovergolf.Adapter.MenuAdapter;
import com.app.discovergolf.Adapter.MenuTwoAdapter;
import com.app.discovergolf.Model.DashBoardMenuTwo;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.CloseTimeResp.CloseTimeResponse;
import com.app.discovergolf.Networks.Response.DashboardResponse;
import com.app.discovergolf.Networks.Response.Datum;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.Utility;
import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

@EFragment(R.layout.fragment_dashboard)
public class DashboardMain extends Fragment {
    @ViewById
    RecyclerView RvMenu;
    @ViewById
    CircleImageView UserImage;
    @ViewById
    TextView UserName;
 @ViewById
    TextView time;

    @ViewById
    LinearLayout live;
    @ViewById
    LinearLayout mod;
    @ViewById
    TextView Country;
    @ViewById
    GridView grid_dashboard;
    MenuAdapter adapter;
    MenuTwoAdapter twoAdapter;
    ArrayList<Datum> menus = new ArrayList<>();
    ArrayList<DashBoardMenuTwo> menuTwos = new ArrayList<>();
    String TIME_EXP = "";
    long seconds=0;
    public static CountDownTimer countDownTimer;
    public static int Stop=0;

    @AfterViews
    public void init() {

      /*  DashboardMenu dashboardMenu = new DashboardMenu();
        dashboardMenu.MenuName = "Level";
        dashboardMenu.Score = "20";
        menus.add(dashboardMenu);*/
        RvMenu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new MenuAdapter(getActivity(), menus);
        RvMenu.setAdapter(adapter);

        DashBoardMenuTwo dashBoardMenuTwo = new DashBoardMenuTwo();
        dashBoardMenuTwo.Name = "Live Mission";
        dashBoardMenuTwo.Image = R.drawable.menu_one;
        menuTwos.add(dashBoardMenuTwo);
        DashBoardMenuTwo dashBoardMenuTwo2 = new DashBoardMenuTwo();
        dashBoardMenuTwo2.Name = "M.O.D";
        dashBoardMenuTwo2.Image = R.drawable.menu_two;
        menuTwos.add(dashBoardMenuTwo2);
        twoAdapter = new MenuTwoAdapter(getActivity(), menuTwos);
        grid_dashboard.setAdapter(twoAdapter);
        Glide.with(getActivity())
                .load(new AppPreferences(getActivity()).getString("STUDENT_PHOTO").toString())
                .into(UserImage);
        UserName.setText(new AppPreferences(getActivity()).getString("STUDENT_FULLNAME").toString());
        Country.setText(new AppPreferences(getActivity()).getString("CITY").toString());
        getdash();

        getTime();

//        this.getView().setOnKeyListener( new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event )
//            {
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    stopCountdown();
//
//                    return true;
//                }
//                return false;
//            }
//        } );

    }
    @Click
    public void live(){
        getLiveMission();
    }
    @Click
    public void mod(){
        startActivity(new Intent(getActivity(), MODActivity_.class));
    }

    public void getdash() {
        Utility.showProgress(getActivity());
        API api = ServerUtility.getApi(getActivity());
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(getActivity());
        Call<DashboardResponse> call = api.getHomeData(WebUtility.HOME_DASHBOARD_, appPreferences.getString("STUDENTID").toString());
        call.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, retrofit2.Response<DashboardResponse> response) {
                Utility.hideProgress();
                getActivity().overridePendingTransition(0, 0);
                menus.addAll(response.body().getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }

    public void getTime() {
     //   Utility.showProgress(getActivity());
        API api = ServerUtility.getApi(getActivity());
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(getActivity());
        Call<CloseTimeResponse> call = api.getCloseTime("getModMissionCloseTime");
        call.enqueue(new Callback<CloseTimeResponse>() {
            @Override
            public void onResponse(Call<CloseTimeResponse> call, retrofit2.Response<CloseTimeResponse> response) {
            //    Utility.hideProgress();
                time.setText(response.body().getData().getCloseTime());
                startTimer(GetSeconds(response.body().getData().getCloseTime().toString()));
            }

            @Override
            public void onFailure(Call<CloseTimeResponse> call, Throwable t) {
            //    Utility.hideProgress();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        stopCountdown();

    }
    public void stopCountdown() {
        if (countDownTimer != null) {
            Stop=1;
            Log.i("CANCEL","CANCEL CALLED");
            countDownTimer.cancel();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Stop=0;
        getTime();
    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                if(Stop==0) {
                    long millis = millisUntilFinished;
                    //Convert milliseconds into hour,minute and seconds
                    //String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    //time.setText(hms);//set text
                    int seconds = (int) (millis / 1000) % 60;
                    int minutes = (int) ((millis / (1000 * 60)) % 60);
                    int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                    String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    if (text != null && time!=null)
                        time.setText(text + "");
                }
            }

            public void onFinish() {
                time.setText("00:00:00"); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
                //startTimer.setText(getString(R.string.start_timer));//Change button text
            }
        }.start();
        Log.i("TOTAL ------>",noOfMinutes+"");
    }


    public void getLiveMission() {
        final Context context = getActivity();
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<LiveMissionResponse> call = api.getLiveMission(WebUtility.LiveMissionDetails, appPreferences.getString("STUDENTID").toString());
        call.enqueue(new Callback<LiveMissionResponse>() {
            @Override
            public void onResponse(Call<LiveMissionResponse> call, retrofit2.Response<LiveMissionResponse> response) {
                Utility.hideProgress();
                //Log.i("RESPONSE--->",response.body().getData().getCreateTeam().toString());
                if (response.body().getData().getCreateTeam().toString().equals("1")) {
                    Intent intent = new Intent(getActivity(), LiveMission_.class);
                    intent.putExtra("title", response.body().getData().getTitle());
                    intent.putExtra("scheduled_date  ", response.body().getData().getScheduledDate());
                    intent.putExtra("description", response.body().getData().getDescription());
                    intent.putExtra("std_video", response.body().getData().getStdVideo());
                    intent.putExtra("student_instructions", response.body().getData().getStudentInstructions());
                    //context.startActivity(new Intent(context, LiveMission_.class));
                    getActivity().startActivity(intent);
                } else if (response.body().getData().getCreateTeam().toString().equals("0")) {
                    Intent intent = new Intent(getActivity(), LiveMissionClassView_.class);
                    intent.putExtra("title", response.body().getData().getTitle());
                    intent.putExtra("scheduled_date  ", response.body().getData().getScheduledDate());
                    intent.putExtra("description", response.body().getData().getDescription());
                    intent.putExtra("std_video", response.body().getData().getStdVideo());
                    intent.putExtra("student_instructions", response.body().getData().getStudentInstructions());
                    intent.putExtra("team_id", response.body().getData().getMissionTeam().get(0).getId().toString());
                    //context.startActivity(new Intent(context, LiveMission_.class));
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LiveMissionResponse> call, Throwable t) {
                Utility.hideProgress();
                Toasty.info(getActivity(),t.getMessage());
            }
        });
    }

    public int GetSeconds(String time){
        int h,m,s;
        int seconds;
        String[] ss=time.split(":");
        h=Integer.parseInt(ss[0]);
        m=Integer.parseInt(ss[1]);
        s=Integer.parseInt(ss[2]);
        //seconds = (h * 3600) + (m * 60) + s;
        seconds=s*1000+m*1000*60+h*1000*60*60;;
        return seconds;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
