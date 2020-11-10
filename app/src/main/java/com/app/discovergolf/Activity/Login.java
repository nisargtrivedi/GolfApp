package com.app.discovergolf.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.CoachDashboard_;
import com.app.discovergolf.Model.Coach;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Requests.LoginRequest;
import com.app.discovergolf.Networks.Response.LoginResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.Parser.CoachLoginParser;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.CheckNetworkConnection;
import com.app.discovergolf.Util.Utility;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;


@EActivity(R.layout.act_login)
public class Login extends BaseActivity {

    @ViewById
    TextView TabStudent;

    @ViewById
    TextView TabParent;

    @ViewById
    TextView TabCoach;

    @ViewById
    AppCompatEditText Email;

    @ViewById
    AppCompatEditText Password;

    static String USERTYPE = "student";


    @AfterViews
    public void init() {
        loadBaseObject();
        Fabric.with(this, new Crashlytics());
        logUser();
        tabSelected(TabStudent, true);
        USERTYPE = "student";
    }
    private void logUser() {
        Crashlytics.setUserIdentifier("goldy_987");
        Crashlytics.setUserEmail("nisarg.trivedi@vovance.com");
        Crashlytics.setUserName("Nisarg");
    }

    @Click
    public void TabStudent() {
        tabSelected(TabStudent, true);
        tabSelected(TabCoach, false);
        tabSelected(TabParent, false);
        USERTYPE = "student";
    }

    @Click
    public void TabCoach() {
        tabSelected(TabCoach, true);
        tabSelected(TabStudent, false);
        tabSelected(TabParent, false);
        USERTYPE = "coach";
    }

    @Click
    public void TabParent() {
        tabSelected(TabParent, true);
        tabSelected(TabCoach, false);
        tabSelected(TabStudent, false);
        USERTYPE = "parent";

    }

    @Click
    public void btnLogin() {
        //showSuccessMsg("Successs");
        //finish();
        //startActivity(new Intent(this,Dashboard_.class));
        if (USERTYPE != null && !USERTYPE.isEmpty()) {
            if (TextUtils.isEmpty(Email.getText().toString().trim())) {
                showErrorMsg("please enter email address");
            } else if (TextUtils.isEmpty(Password.getText().toString().trim())) {
                showErrorMsg("please enter password");
            } else {
                if (Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()) {
                    if(!USERTYPE.isEmpty()) {
                        if (USERTYPE.equals("coach")) {
                            LoginAPI();
                        } else if (USERTYPE.equals("student")) {
                            loginStudent();
                        }
                    }else{
                        showErrorMsg("please select UserType");
                    }
                } else {
                    showErrorMsg("please enter proper email address");
                }
            }
        }
    }

    private void tabSelected(TextView tv, boolean b) {
        if (b) {
            tv.setBackgroundResource(R.drawable.login_selected_tab);
            tv.setTextColor(getResources().getColor(R.color.white));
        } else {
            tv.setBackgroundResource(R.drawable.login_tab);
            tv.setTextColor(getResources().getColor(R.color.login_tab_color));
        }
    }

    @Click
    public void forgotPassword() {
        startActivity(new Intent(Login.this, ForgotPassword_.class));
    }

    private void LoginAPI() {
        Utility.showProgress(Login.this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.Login + "&email=" + Email.getText().toString().trim() + "&password=" + Password.getText().toString().trim() + "&user_type=" + USERTYPE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Login Response--->", s);
                        Utility.hideProgress();
                        try {
                            CoachLoginParser coach;
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            coach = mGson.fromJson(s, CoachLoginParser.class);
                            if (coach.isValid()) {
                                showSuccessMsg(coach.coach.FirstName);
                                if (USERTYPE.equalsIgnoreCase("coach")) {
                                    appPreferences.set("COACHID", coach.coach.CoachID);
                                    appPreferences.set("COACH_FULLNAME", coach.coach.FullName);
                                    appPreferences.set("COACH_CITY", coach.coach.City);
                                    appPreferences.set("COACH_PHOTO", coach.coach.ProfilePhoto);
                                    appPreferences.set("USERTYPE", USERTYPE);
                                    startActivity(new Intent(Login.this, CoachDashboard_.class));
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            } else {
                                showErrorMsg(coach.ErrorMsg);
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
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(request);
    }


    public void loginStudent() {
        Utility.showProgress(Login.this);
        API api = ServerUtility.getApi(Login.this);
        Call<LoginResponse> call = api.login( WebUtility.Login,Email.getText().toString(),Password.getText().toString(),USERTYPE);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                Utility.hideProgress();
                appPreferences.set("STUDENTID", response.body().getData().getId().toString());
                appPreferences.set("STUDENT_ID", response.body().getData().getId().toString());
                appPreferences.set("STUDENT_FULLNAME", response.body().getData().getName());
                appPreferences.set("CITY", response.body().getData().getCity());
                appPreferences.set("STUDENT_PHOTO", response.body().getData().getProfileImg());
                appPreferences.set("USERTYPE", USERTYPE);
                appPreferences.set("FIRST_NAME", response.body().getData().getFirstName());
                appPreferences.set("LAST_NAME", response.body().getData().getLastName());
                appPreferences.set("ABOUT", response.body().getData().getAboutMe());
                appPreferences.set("CLASS_ID", response.body().getData().getClassId());

                Log.i("STUDENT ID-->",response.body().getData().getId().toString());

                startActivity(new Intent(Login.this, Dashboard_.class));
                finish();
                overridePendingTransition(0, 0);
                //Toast.makeText(Login.this, response.body().getErrorMessage(), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Utility.hideProgress();
                showErrorMsg("Wrong Username or password");
            }
        });
    }
}
