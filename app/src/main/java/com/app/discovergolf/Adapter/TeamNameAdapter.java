package com.app.discovergolf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.TeamDetailScore_;
import com.app.discovergolf.Networks.Response.LiveMissionR.MissionTeam;
import com.app.discovergolf.R;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class TeamNameAdapter extends RecyclerView.Adapter<TeamNameAdapter.ViewHolder> {


    public List<MissionTeam> list;
    Context context;
    public String name;


    public TeamNameAdapter(Context context, List<MissionTeam> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView team_name, score;
        public LinearLayout ll1;

        public ViewHolder(View view) {
            super(view);
            score = (TextView) view.findViewById(R.id.score);
            team_name = (TextView) view.findViewById(R.id.team_name);
            ll1 = view.findViewById(R.id.ll1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.live_score_row, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MissionTeam task = list.get(position);
        holder.team_name.setText(task.getTitle());
        holder.score.setText(task.getTotalScore().toString());
//        holder.team_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, TeamDetailScore_.class);
//                intent.putExtra("team_id", task.getId().toString());
//                context.startActivity(intent);
//            }
//        });
        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TeamDetailScore_.class);
                intent.putExtra("team_id", task.getId().toString());
                intent.putExtra("team_name", task.getTitle().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
