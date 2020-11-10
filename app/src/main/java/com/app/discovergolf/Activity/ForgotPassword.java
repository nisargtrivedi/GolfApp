package com.app.discovergolf.Activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Parser.CoachLoginParser;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity(R.layout.act_forgotpassword)
public class ForgotPassword extends BaseActivity {

    @ViewById
    AppCompatButton btnSend;
    @ViewById
    AppCompatEditText Email;
    @ViewById
    ImageView close;

    @AfterViews
    public void init(){
        loadBaseObject();
    }
    @Click
    public void close(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Click
    public void btnSend(){
        if(TextUtils.isEmpty(Email.getText().toString())){
            showErrorMsg("please enter email address");
        }else{
            if(Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches())
                ForgotPasswordAPI();
            else
                showErrorMsg("please enter proper email address");
        }
    }

    private void ForgotPasswordAPI() {
        Utility.showProgress(ForgotPassword.this);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action="+WebUtility.ForgotPassword+"&email="+ Email.getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Utility.hideProgress();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            String error_code=jsonObject.getString("error_code");
                            String error_message=jsonObject.getString("error_message");
                            if(error_code.equalsIgnoreCase("0")){
                                showSuccessMsg(error_message);
                                finish();
                            }else{
                                showErrorMsg(error_message);
                                Email.setText("");
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
        RequestQueue queue = Volley.newRequestQueue(ForgotPassword.this);
        queue.add(request);
    }
}
