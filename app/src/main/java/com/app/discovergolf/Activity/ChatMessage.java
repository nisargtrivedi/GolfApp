package com.app.discovergolf.Activity;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Adapter.ChatRowAdapter;
import com.app.discovergolf.Model.ChatModel;
import com.app.discovergolf.Networks.API;
import com.app.discovergolf.Networks.ServerUtility;
import com.app.discovergolf.Parser.ChatMessgageResponse;
import com.app.discovergolf.Parser.FriendResponse;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.Utility;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.chat_message)
public class ChatMessage extends BaseActivity {

    @ViewById
    ImageView user3;
    @ViewById
    TextView tvName;
    @ViewById
    AppCompatButton btnSend;
    @ViewById
    RecyclerView rvChat;
    @ViewById
    AppCompatEditText edtChat;

    ArrayList<ChatModel> list=new ArrayList<>();
    ChatRowAdapter adapter;

    String name,img,toid;
    String fromUserId;
    @AfterViews
    public void init()
    {
        loadBaseObject();
        name=getIntent().getStringExtra("name");
        img=getIntent().getStringExtra("img");
        toid=getIntent().getStringExtra("toid");
        Picasso.with(this).load(img).transform(new CircleTransform()).into(user3);
        tvName.setText(name);
        if(!TextUtils.isEmpty(appPreferences.getString("COACHID")))
            fromUserId=appPreferences.getString("COACHID");
        else
            fromUserId=appPreferences.getString("STUDENTID");

        adapter=new ChatRowAdapter(this,list);
        rvChat.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        rvChat.setAdapter(adapter);
        getMessage();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getMessage() {
        final Context context = this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<ChatMessgageResponse> call;
        if(!TextUtils.isEmpty(appPreferences.getString("COACHID")))
            call = api.getChatFriendList(WebUtility.MESSAGELIST, appPreferences.getString("COACHID").toString(),toid+"");
        else
            call = api.getChatFriendList(WebUtility.MESSAGELIST, appPreferences.getString("STUDENTID").toString(),toid+"");

        call.enqueue(new Callback<ChatMessgageResponse>() {
            @Override
            public void onResponse(Call<ChatMessgageResponse> call, retrofit2.Response<ChatMessgageResponse> response) {
                Utility.hideProgress();
                list.clear();
                list.addAll(response.body().data);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<ChatMessgageResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }

    @Click
    public void btnSend(){
        if(!TextUtils.isEmpty(edtChat.getText().toString().trim()))
            sendMessage();
        else
            showErrorMsg("please enter some text");
    }

    public void sendMessage() {
        final Context context = this;
        Utility.showProgress(context);
        API api = ServerUtility.getApi(context);
        AppPreferences appPreferences;
        appPreferences = new AppPreferences(context);
        Call<ChatMessgageResponse> call;
        if(!TextUtils.isEmpty(appPreferences.getString("COACHID")))
            call = api.sendMessage(WebUtility.SENDMESSAGE, appPreferences.getString("COACHID").toString(),toid+"",edtChat.getText().toString());
        else
            call = api.sendMessage(WebUtility.SENDMESSAGE, appPreferences.getString("STUDENTID").toString(),toid+"",edtChat.getText().toString());

        call.enqueue(new Callback<ChatMessgageResponse>() {
            @Override
            public void onResponse(Call<ChatMessgageResponse> call, retrofit2.Response<ChatMessgageResponse> response) {
                Utility.hideProgress();
                //getMessage();
//                ChatModel model=new ChatModel();
//                    model.from_user=Integer.parseInt(fromUserId);
//                    model.to_user=Integer.parseInt(toid);
//                    model.messages=edtChat.getText().toString().trim();
//                    model.from_profile_img=img;
//                    adapter.addUser(model);
//                    adapter.notifyDataSetChanged();
                list.clear();
                list.addAll(response.body().data);
                adapter.notifyDataSetChanged();
                edtChat.setText("");

            }
            @Override
            public void onFailure(Call<ChatMessgageResponse> call, Throwable t) {
                Utility.hideProgress();
            }
        });
    }
}
