package com.app.discovergolf.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.discovergolf.Activity.ChatMessage_;
import com.app.discovergolf.Model.FriendList;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.Datum;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.TeamStd;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by admin on 7/31/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    public List<FriendList> list;
    Context context;
    public String name;

    public FriendAdapter(Context context, List<FriendList> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvMsg,time;
        ImageView user1;
        LinearLayout ll;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            time = (TextView) view.findViewById(R.id.time);
            user1 = (ImageView) view.findViewById(R.id.user1);
            ll = (LinearLayout) view.findViewById(R.id.ll);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.messages, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FriendList task = list.get(position);
        if(task!=null){
                holder.tvTitle.setText(task.Name);
                holder.time.setText(task.last_message_time);
                holder.tvMsg.setText(task.last_message);
                Picasso.with(context).load(task.profile_img).transform(new CircleTransform()).into(holder.user1);

                holder.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context,ChatMessage_.class).putExtra("img",task.profile_img).putExtra("name",task.Name).putExtra("toid",task.SID+""));
                    }
                });
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
