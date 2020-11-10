package com.app.discovergolf.Activity.Coach.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.CoachDashboard;
import com.app.discovergolf.Activity.Coach.Activity.MissionList_;
import com.app.discovergolf.Component.CalendarListener;
import com.app.discovergolf.Component.CalendarUtils;
import com.app.discovergolf.Component.CustomCalendarView;
import com.app.discovergolf.Component.DayDecorator;
import com.app.discovergolf.Component.DayView;
import com.app.discovergolf.Model.MissionHQModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
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

@EFragment(R.layout.mission_hq)
public class CoachMissionHQ extends Fragment {


    @ViewById
    CustomCalendarView calendarView;
    @ViewById
    LinearLayout Header;

    //ArrayList<Integer> daylist = new ArrayList<>();
    ArrayList<String> daylist = new ArrayList<>();

    ArrayList<MissionHQModel> missionHQModelArrayList = new ArrayList<>();

    ArrayList<MissionHQModel> missionHQModel = new ArrayList<>();

    AppPreferences appPreferences;
    Calendar currentCalendar;

    @AfterViews
    public void init() {

        ((CoachDashboard) getActivity()).setTitleText("Mission HQ");
        Header.setVisibility(View.GONE);
        appPreferences = new AppPreferences(getActivity());
        GetMissionListAPI();
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
                missionHQModel.clear();
                for (int i = 0; i < missionHQModelArrayList.size(); i++) {
                    if (df.format(date).equalsIgnoreCase(missionHQModelArrayList.get(i).ScheduleDate)) {
                        missionHQModel.add(missionHQModelArrayList.get(i));
                    }
                }

                startActivity(new Intent(getActivity(), MissionList_.class).putExtra("MissionList", missionHQModel));
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        //calendarView.refreshCalendar(currentCalendar);
//        init();
//    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {

            Log.i("Day View", dayView.getDate() + "");
            //final SimpleDateFormat df = new SimpleDateFormat("d");
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            int day2 = Integer.parseInt(df.format(dayView.getDate()));
            String day2 = df.format(dayView.getDate());
            //    arrayList.contains(dayView);
//            if (daylist.contains(day2) == true) {
//                int color = Color.parseColor("#123456");
//                dayView.setBackgroundColor(color);
//                dayView.setTextColor(Color.WHITE);
//            }
            if (daylist.contains(day2) == true) {
                //int color = Color.parseColor("#123456");
                //dayView.setBackgroundColor(color);
                dayView.setBackgroundResource(R.drawable.round_fill_color_calander);
                dayView.setTextColor(Color.WHITE);
            }

//            }else{
//                int color = Color.GREEN;
//                dayView.setBackgroundColor(color);
//                dayView.setTextColor(Color.WHITE);
//            }
        }
    }

    //Get All MissionAPI
    private void GetMissionListAPI() {
        Utility.showProgress(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.GetMissionList + "&coach_id=" + appPreferences.getString("COACHID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("MISSION RESPONSE--->", s);
                        Utility.hideProgress();
                        try {

                            JSONObject object = new JSONObject(s);
                            String ErrorCode = object.getString("error_code");
                            String ErrorMessage = object.getString("error_message");
                            if (ErrorCode.equalsIgnoreCase("0")) {
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

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

}
