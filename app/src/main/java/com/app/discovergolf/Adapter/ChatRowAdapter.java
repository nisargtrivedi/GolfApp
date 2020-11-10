package com.app.discovergolf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.ChatMessage_;
import com.app.discovergolf.Model.ChatModel;
import com.app.discovergolf.Model.FriendList;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class ChatRowAdapter extends RecyclerView.Adapter<ChatRowAdapter.ViewHolder> {
    public List<ChatModel> list;
    Context context;
    public String name;

    AppPreferences appPreferences;
    public ChatRowAdapter(Context context, List<ChatModel> list) {
        this.context = context;
        this.list = list;
        appPreferences=new AppPreferences(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRight,tvDate;
        ImageView user2;
        LinearLayout ll;
        RelativeLayout rldata;

        public ViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvRight = (TextView) view.findViewById(R.id.tvRight);
            user2 = (ImageView) view.findViewById(R.id.user2);
            ll = (LinearLayout) view.findViewById(R.id.ll);
            rldata = (RelativeLayout) view.findViewById(R.id.rldata);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.chat_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChatModel task = list.get(position);
        Log.i("LOOP",position+"");
        if(task!=null) {
            if(position>0){
                if(!DateUtils.GetChatDate(task.created_at).equalsIgnoreCase(DateUtils.GetChatDate(list.get(position-1).created_at))){
                        holder.tvDate.setText(DateUtils.getDayNAme(task.created_at));
                        holder.tvDate.setVisibility(View.VISIBLE);
                }else {
                    holder.tvDate.setVisibility(View.GONE);
                }
            }else{
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvDate.setText(DateUtils.getDayNAme(task.created_at));
            }

            Picasso.with(context).load(task.from_profile_img).transform(new CircleTransform()).into(holder.user2);
            if (!TextUtils.isEmpty(appPreferences.getString("COACHID"))){
                if (task.from_user == Integer.parseInt(appPreferences.getString("COACHID"))) {
                    holder.tvRight.setText(task.messages);
                    holder.user2.setVisibility(View.VISIBLE);
                    holder.rldata.setGravity(Gravity.RIGHT);
                    holder.tvRight.setBackgroundColor(Color.parseColor("#57AB49"));
                }else{
                    holder.rldata.setGravity(Gravity.LEFT);
                    holder.tvRight.setText(task.messages);
                    holder.user2.setVisibility(View.GONE);
                    holder.tvRight.setBackgroundColor(Color.parseColor("#C5C5C5"));
                }
            }
            else if (!TextUtils.isEmpty(appPreferences.getString("STUDENTID"))){
                if (task.from_user == Integer.parseInt(appPreferences.getString("STUDENTID"))) {
                    holder.tvRight.setText(task.messages);
                    holder.user2.setVisibility(View.VISIBLE);
                    holder.rldata.setGravity(Gravity.RIGHT);
                    holder.tvRight.setBackgroundColor(Color.parseColor("#57AB49"));
                }else{
                    holder.rldata.setGravity(Gravity.LEFT);
                    holder.tvRight.setText(task.messages);
                    holder.tvRight.setBackgroundColor(Color.parseColor("#C5C5C5"));
                    holder.user2.setVisibility(View.GONE);
                }
            }
        }
    }

    public void addUser(ChatModel chatModel){
        list.add(chatModel);
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
