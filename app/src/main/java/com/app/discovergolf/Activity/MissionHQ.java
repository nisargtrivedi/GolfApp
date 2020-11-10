package com.app.discovergolf.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.MissionList;

import com.app.discovergolf.Activity.Coach.Fragment.CoachMissionHQ;
import com.app.discovergolf.Activity.student.Activity.MODActivity;
import com.app.discovergolf.Activity.student.Adapter.MyTeamAdapter;
import com.app.discovergolf.Component.CalendarListener;
import com.app.discovergolf.Component.CalendarUtils;
import com.app.discovergolf.Component.CustomCalendarView;
import com.app.discovergolf.Component.DayDecorator;
import com.app.discovergolf.Component.DayView;
import com.app.discovergolf.Model.MissionHQModel;
import com.app.discovergolf.Model.ModMissionModel;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.AllLiveMissionRes.AllLiveResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.mission_hq)
public class MissionHQ extends BaseActivity {
    @ViewById
    CustomCalendarView calendarView;
    //ArrayList<Integer> daylist = new ArrayList<>();
    ArrayList<String> daylist = new ArrayList<>();
    ArrayList<String> missionhq = new ArrayList<>();
    ArrayList<ModMissionModel> missionHQModelArrayList = new ArrayList<>();

    ArrayList<MissionHQModel> missionHQModel = new ArrayList<>();
    ArrayList<ModMissionModel.MissionTeam> teamList = new ArrayList<>();



    Calendar currentCalendar;
    Context context;
    String team_id ="";
    String name ="";
    String score ="";
    String modTeam_id="";
    String modTeam_Name="";
    String mission_type="";



    @AfterViews
    public void init() {
        loadBaseObject();
        context = MissionHQ.this;

        //getLiveMission();
        MissionApi();
        currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Log.i("Select Date is -->", df.format(date));
                //missionHQModel.clear();
                team_id="";
                name="";
                score="";
                modTeam_id="";
                modTeam_Name="";
                mission_type="";
                for (int i = 0; i < missionHQModelArrayList.size(); i++) {
                    Log.i("DATE-->",missionHQModelArrayList.get(i).scheduled_date+"---");
                    if (df.format(date).toString().equalsIgnoreCase(missionHQModelArrayList.get(i).scheduled_date)) {
                        //missionHQModel.add(missionHQModelArrayList.get(i));
                        mission_type=missionHQModelArrayList.get(i).mission_type;
                        Log.i("DATE-->",mission_type+"---");
                        if(mission_type.equalsIgnoreCase("0")){
                            team_id=missionHQModelArrayList.get(i).TeamID;
                            for(int j=0;j<teamList.size();j++){
                                if(teamList.get(j).TeamID==Integer.parseInt(team_id)) {
                                    name = teamList.get(j).team;
                                }
                            }
                        }else{
                            modTeam_id=missionHQModelArrayList.get(i).my_team_id+"";
                            for(int j=0;j<teamList.size();j++){
                                if(teamList.get(j).TeamID==Integer.parseInt(modTeam_id)){
                                    modTeam_Name=teamList.get(j).team;
                                }
                            }
                        }

                        //score=missionHQModelArrayList.get(i).;

                    }
                }
                /*  if (!appPreferences.getString("STUDENT_ID").isEmpty()){*/
                Intent intent = new Intent(MissionHQ.this,DateScoreActivity.class);
                intent.putExtra("team_id",team_id);
                intent.putExtra("name",name);
                intent.putExtra("score",score);
                intent.putExtra("modTeam_id",modTeam_id);
                intent.putExtra("modTeam_Name",modTeam_Name);
                intent.putExtra("mission_type",mission_type);
                /**/ String dd=new SimpleDateFormat("dd MMMM yyyy").format(date);
                intent.putExtra("date",dd);
                startActivity(intent);
               /* }else {
                    startActivity(new Intent(context, MissionList.class).putExtra("MissionList", missionHQModel));

                }*/
                if (!CalendarUtils.isPastDay(date)) {

                    //selectedDateTv.setText("Selected date is " + df.format(date));
                } else {
                    //selectedDateTv.setText("Selected date is disabled!");
                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                //Toast.makeText(CalendarDayDecoratorActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<DayDecorator> decorators = new ArrayList<>();
                decorators.add(new DisabledColorDecorator());
                calendarView.setDecorators(decorators);
                calendarView.refreshCalendar(currentCalendar);
                calendarView.refreshCalendar(currentCalendar);
            }
        }, 2000);


    }

    @Click
    public void Header(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    public void getLiveMission() {
//        final Context context = MissionHQ.this;
//        Utility.showProgress(context);
//        API api = ServerUtility.getApi(context);
//        AppPreferences appPreferences;
//        appPreferences = new AppPreferences(context);
//        Call<AllLiveResponse> call = api.getAllLiveMissions(WebUtility.ALL_LIVE_MISSION,
//                appPreferences.getString("STUDENTID"));
//        call.enqueue(new Callback<AllLiveResponse>() {
//            @Override
//            public void onResponse(Call<AllLiveResponse> call, final retrofit2.Response<AllLiveResponse> response) {
//                Utility.hideProgress();
//                MissionHQModel hqModel = new MissionHQModel();
//                try{
//                    hqModel.MissionID = response.body().getData().get2().getId().toString();
//                    hqModel.MissionTitle = response.body().getData().get2().getTitle();
//                    hqModel.MissionDescription = response.body().getData().get2().getDescription();
//                    hqModel.MissionDuration = response.body().getData().get2().getMissionDuration();
//                    hqModel.ScheduleDate = response.body().getData().get2().getScheduledDate();
//                    hqModel.MissionType = response.body().getData().get2().getMissionType();
//                }catch (Exception e){}
//                if(response.body().getData().get2().getMissionTeam().size()>0) {
//                    for (int i = 0; i < response.body().getData().get2().getMissionTeam().size(); i++) {
//                        team_id = response.body().getData().get2().getMissionTeam().get(i).getId().toString();
//                        name = response.body().getData().get2().getMissionTeam().get(i).getTitle().toString();
//                        score = response.body().getData().get2().getMissionTeam().get(i).getTotalScore().toString();
//                    }
//                }
//                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
//                SimpleDateFormat df = new SimpleDateFormat("d");
//                Date startDate = null;
//                //Toast.makeText(context, response.body().getData().get2().getScheduledDate(), Toast.LENGTH_SHORT).show();
//                try {
//                    startDate = df2.parse(hqModel.ScheduleDate);
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                //  int newDateString = Integer.parseInt(df.format(startDate));
//                //daylist.add(newDateString);
//                live_date=response.body().getData().get2().getScheduledDate();
//
//                daylist.add(response.body().getData().get2().getScheduledDate());
//                missionHQModelArrayList.add(hqModel);
//
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<DayDecorator> decorators = new ArrayList<>();
//                        decorators.add(new DisabledColorDecorator());
//                        calendarView.setDecorators(decorators);
//                        calendarView.refreshCalendar(currentCalendar);
//                        calendarView.refreshCalendar(currentCalendar);
//                    }
//                }, 2000);
//            }
//
//            @Override
//            public void onFailure(Call<AllLiveResponse> call, Throwable t) {
//                Utility.hideProgress();
//            }
//        });
//    }


    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {

            Log.i("Day View", dayView.getDate() + "");

            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//            int day2 = Integer.parseInt(df.format(dayView.getDate()));
            String day2 = df.format(dayView.getDate());
            //    arrayList.contains(dayView);
//            if (daylist.contains(day2) == true) {
//                int color = Color.parseColor("#123456");
//                dayView.setBackgroundColor(color);
//                dayView.setTextColor(Color.WHITE);
//            }
            Log.i("DayLIST", day2);
            Log.i("LIVE MISSION DATE", daylist.toString());
            Log.i("MOD MISSION DATE", missionhq.toString());
            if (daylist.contains(day2)){
                dayView.setBackgroundResource(R.drawable.round_fill_color_calander);
                dayView.setTextColor(Color.WHITE);
            }
            if (missionhq.contains(day2)){
                dayView.setBackgroundResource(R.drawable.red_round_fill);
                dayView.setTextColor(Color.WHITE);
            }
            if (daylist.contains(day2)&&missionhq.contains(day2)){
                dayView.setBackgroundResource(R.drawable.half_ic);
                dayView.setTextColor(Color.WHITE);
            }
          /*  if (daylist.contains(day2) == true) {
                //int color = Color.parseColor("#123456");
                //dayView.setBackgroundColor(color);
                dayView.setBackgroundResource(R.drawable.round_fill_color_calander);
                dayView.setTextColor(Color.WHITE);
            }*/


//            }else{
//                int color = Color.GREEN;
//                dayView.setBackgroundColor(color);
//                dayView.setTextColor(Color.WHITE);
//            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        calendarView.refreshCalendar(currentCalendar);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void MissionApi() {

       // String StudentId =
        //Log.e("StudentId", StudentId);
        Utility.showProgress(this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetModMissionList + "&student_id=" + appPreferences.getString("STUDENTID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("Response--->", s.toString());
                                        Utility.hideProgress();
                        missionHQModelArrayList.clear();
                        try {
                            JSONObject object = new JSONObject(s);
                            String error_code = object.getString("error_code");
                            String error_message = object.getString("error_message");
                            if (error_code.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ModMissionModel ModMissionModel = new ModMissionModel();
                                    ModMissionModel.id = jsonArray.getJSONObject(i).getInt("id");
                                    ModMissionModel.cat_id = jsonArray.getJSONObject(i).getString("cat_id");
                                    ModMissionModel.class_id = jsonArray.getJSONObject(i).getString("class_id");
                                    ModMissionModel.create_team = jsonArray.getJSONObject(i).getInt("create_team");
                                    ModMissionModel.is_Class = jsonArray.getJSONObject(i).optString("is_class");
                                    ModMissionModel.description = jsonArray.getJSONObject(i).getString("description");
                                    ModMissionModel.mission_type = jsonArray.getJSONObject(i).getString("mission_live_mod");
                                    ModMissionModel.scheduled_date = jsonArray.getJSONObject(i).getString("scheduled_date");
                                    if(ModMissionModel.mission_type.toString().equalsIgnoreCase("0"))
                                        daylist.add(jsonArray.getJSONObject(i).getString("scheduled_date"));
                                    else
                                        missionhq.add(jsonArray.getJSONObject(i).getString("scheduled_date"));

                                    JSONArray teamArray = jsonArray.getJSONObject(i).getJSONArray("missionTeam");
                                    for (int j = 0; j < teamArray.length(); j++) {
                                        ModMissionModel.MissionTeam team = new ModMissionModel().new MissionTeam();
                                        team.TeamID=teamArray.getJSONObject(j).optInt("id");
                                        team.setId(teamArray.getJSONObject(j).optInt("id"));
                                        team.setScore(teamArray.getJSONObject(j).optString("totalScore"));
                                        team.setTeam(teamArray.getJSONObject(j).optString("title"));
                                        teamList.add(team);
                                    }
                                    //modTeam_id=String.valueOf(jsonArray.getJSONObject(i).getInt("my_team_id"));
                                    ModMissionModel.TeamID=String.valueOf(jsonArray.getJSONObject(i).getInt("team_id"));
                                    ModMissionModel.my_team_id = jsonArray.getJSONObject(i).getInt("my_team_id");
                                    if (teamList.size() > 0) {
                                        MyTeamAdapter adapter = new MyTeamAdapter(MissionHQ.this, teamList, ModMissionModel.my_team_id, ModMissionModel.id);
                                    }
                                    //ModMissionModel.scheduled_date = jsonArray.getJSONObject(i).getString("scheduled_date");

//                                    mod_date=jsonArray.getJSONObject(i).getString("scheduled_date");
//                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
//                                    Date parsedDate = simpleDateFormat2.parse(mod_date);
//                                    simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                                   // mod_date = simpleDateFormat2.format(parsedDate);
                                    Log.i("SCHEDULE DATE-->",jsonArray.getJSONObject(i).getString("scheduled_date"));


                                    ModMissionModel.team_admin = jsonArray.getJSONObject(i).getInt("team_admin");
                                    ModMissionModel.title = jsonArray.getJSONObject(i).getString("title");
                                    ModMissionModel.updated_at = jsonArray.getJSONObject(i).getString("updated_at");
                                    ModMissionModel.upload_video = jsonArray.getJSONObject(i).getString("upload_video");
                                    missionHQModelArrayList.add(ModMissionModel);
                                    Log.i("SIZE-->",missionHQModelArrayList.size()+"");
                                }
                            }
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<DayDecorator> decorators = new ArrayList<>();
                                    decorators.add(new DisabledColorDecorator());
                                    calendarView.setDecorators(decorators);
                                    calendarView.refreshCalendar(currentCalendar);
                                    calendarView.refreshCalendar(currentCalendar);
                                }
                            }, 2000);
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




























    //Get All MissionAPI
   /* private void getLiveMission() {
        Utility.showProgress(MissionHQ.this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action="+WebUtility.ALL_LIVE_MISSION+"&student_id="+appPreferences.getString("STUDENT_ID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("MISSION RESPONSE--->",s);
                        Utility.hideProgress();
                        try {

                            JSONObject object=new JSONObject(s);
                            String ErrorCode=object.getString("error_code");
                            String ErrorMessage=object.getString("error_message");
                            if(ErrorCode.equalsIgnoreCase("0")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MissionHQModel hqModel = new MissionHQModel();
                                    hqModel.MissionID = jsonArray.getJSONObject(i).getString("id");
                                    hqModel.MissionTitle = jsonArray.getJSONObject(i).getString("title");
                                    hqModel.MissionDescription = jsonArray.getJSONObject(i).getString("description");
                                    hqModel.MissionDuration = jsonArray.getJSONObject(i).getString("missionDuration");
                                    hqModel.ScheduleDate = jsonArray.getJSONObject(i).getString("scheduled_date");
                                    hqModel.MissionType = jsonArray.getJSONObject(i).getString("mission_type");

                                    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat df = new SimpleDateFormat("d");
                                    Date startDate = null;
                                    Toast.makeText(context,  jsonArray.getJSONObject(i).getString("scheduled_date"), Toast.LENGTH_SHORT).show();
                                    try {
                                        startDate = df2.parse(hqModel.ScheduleDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    int newDateString = Integer.parseInt(df.format(startDate));
                                    //daylist.add(newDateString);
                                    daylist.add(hqModel.ScheduleDate);
                                    missionHQModelArrayList.add(hqModel);

                                }
                            }
                        } catch (Exception e) {
                            Log.e("error","error"+e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(MissionHQ.this);
        queue.add(request);
    }*/


}
