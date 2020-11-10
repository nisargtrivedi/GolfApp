package com.app.discovergolf.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Adapter.FriendAdapter;
import com.app.discovergolf.Model.FriendList;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.Parser.FriendResponse;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.DateUtils;
import com.app.discovergolf.Util.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.act_friendlist)
public class Message extends com.app.discovergolf.Activity.BaseActivity {

    @ViewById
    RecyclerView rvMessage;

    FriendAdapter adapter;
    ArrayList<FriendList> lists=new ArrayList<>();
    @AfterViews
    public void init(){
        loadBaseObject();
        adapter=new FriendAdapter(this,lists);
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        rvMessage.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFriends();
    }

    @Click
    public void RlOne(){
        startActivity(new Intent(this,ChatMessage_.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getFriends() {
        final Context context = this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<FriendResponse> call;
        if(!TextUtils.isEmpty(appPreferences.getString("COACHID")))
            call = api.getFriendList(WebUtility.CHATUSER, appPreferences.getString("COACHID").toString());
        else
            call = api.getFriendList(WebUtility.CHATUSER, appPreferences.getString("STUDENTID").toString());

        call.enqueue(new Callback<FriendResponse>() {
            @Override
            public void onResponse(Call<FriendResponse> call, retrofit2.Response<FriendResponse> response) {
                Utility.hideProgress();
                lists.clear();
                lists.addAll(response.body().data);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<FriendResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }
}
