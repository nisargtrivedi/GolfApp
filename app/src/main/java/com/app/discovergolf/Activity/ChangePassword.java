package com.app.discovergolf.Activity;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.EEditText;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity(R.layout.act_changepassword)
public class ChangePassword extends BaseActivity {

    @ViewById
    EEditText NewPassword;

    @ViewById
    EEditText ConfirmPassword;
    @ViewById
    EEditText oldPassword;


    @AfterViews
    public void init() {
        loadBaseObject();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Click
    public void btnLogin() {
        if (TextUtils.isEmpty(oldPassword.getText().toString())) {
            showErrorMsg("please enter old password");
        }
        else if (TextUtils.isEmpty(NewPassword.getText().toString())) {
            showErrorMsg("please enter password");
        } else if (TextUtils.isEmpty(ConfirmPassword.getText().toString())) {
            showErrorMsg("please re-enter password");
        } else {
            if (ConfirmPassword.getText().toString().equalsIgnoreCase(NewPassword.getText().toString())) {
                ChangeAPI();
            } else {
                showErrorMsg("confirm password is not match with new password");
            }
        }
    }

    private void ChangeAPI() {
        Utility.showProgress(this);
        String userId = "";
        if (getIntent().getBooleanExtra("isCoch", false)) {
            userId = appPreferences.getString("COACHID");
        }else{
            userId = appPreferences.getString("STUDENTID");
        }
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.ChangePassword + "&user_id=" + userId + "&password=" + NewPassword.getText().toString().trim()+"&old_password=" + oldPassword.getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Utility.hideProgress();
                        Log.i("Change Password --->", s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String ErrorCode = jsonObject.getString("error_code");
                            String ErrorMessage = jsonObject.getString("error_message");
                            if (ErrorCode.equalsIgnoreCase("0")) {
                                showSuccessMsg(ErrorMessage);
                            } else {
                                showErrorMsg(ErrorMessage);
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

}
