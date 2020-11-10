package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.Coach.Fragment.MissionTeam;
import com.app.discovergolf.Activity.Coach.Listeners.CoachDeleteTeamListener;
import com.app.discovergolf.Activity.Coach.Listeners.TeamSelectedListener;
import com.app.discovergolf.Model.MissionTeamModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class MissionTeamAdapter extends RecyclerView.Adapter<MissionTeamAdapter.ViewHolder> implements Filterable {


    public List<MissionTeamModel> list;
    public List<MissionTeamModel> Filterlist;
    Context context;
    public String name;
    AppPreferences appPreferences;
    private TeamSelectedListener callback;
    private CoachDeleteTeamListener listener;

    public MissionTeamAdapter(Context context, List<MissionTeamModel> list) {
        this.context = context;
        this.list = list;
        this.Filterlist = list;
        appPreferences = new AppPreferences(context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Filterlist = list;
                } else {
                    List<MissionTeamModel> filteredList = new ArrayList<>();
                    for (MissionTeamModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.MissionTitle.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    Filterlist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Filterlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Filterlist = (ArrayList<MissionTeamModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTeamName;
        public ImageView imgEditTeam,imgDeleteTeam;
        public RelativeLayout RlPlayerData;

        public ViewHolder(View view) {
            super(view);
            txtTeamName = (TextView) view.findViewById(R.id.txtTeamName);
            imgEditTeam = (ImageView) view.findViewById(R.id.imgEditTeam);
            RlPlayerData = view.findViewById(R.id.RlPlayerData);
            imgDeleteTeam=view.findViewById(R.id.imgDeleteTeam);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_team_row, parent, false);

        return new ViewHolder(itemView);
    }

    public void attach(MissionTeam f) {
        try {
            callback = f;
            listener=f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }

    public void selectNavItem(MissionTeamModel Name) {
        callback.OnClickTeam(Name);
    }
    public void DeleteTeam(String Name) {
        listener.OnDelete(Name);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MissionTeamModel task = Filterlist.get(position);
        holder.txtTeamName.setText(task.MissionTitle);
        if(!TextUtils.isEmpty(appPreferences.getString("COACHID"))){
            holder.imgDeleteTeam.setVisibility(View.VISIBLE);
        }else{
            holder.imgDeleteTeam.setVisibility(View.INVISIBLE);
        }
        holder.imgEditTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(task);
            }
        });
        holder.imgDeleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteTeam(task.TeamID);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Filterlist.size();
    }


}
