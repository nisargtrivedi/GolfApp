package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.Coach.Activity.MissionList;
import com.app.discovergolf.Activity.Coach.Fragment.MissionTeam;
import com.app.discovergolf.Activity.Coach.Listeners.MissionSelectedListener;
import com.app.discovergolf.Activity.Coach.Listeners.TeamSelectedListener;
import com.app.discovergolf.Model.MissionHQModel;
import com.app.discovergolf.Model.MissionTeamModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class MissionListAdapter extends RecyclerView.Adapter<MissionListAdapter.ViewHolder> implements Filterable {


    public List<MissionHQModel> list;
    public List<MissionHQModel> Filterlist;
    Context context;
    public String name;
    AppPreferences appPreferences;
    private MissionSelectedListener callback;

    public MissionListAdapter(Context context, List<MissionHQModel> list) {
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
                    List<MissionHQModel> filteredList = new ArrayList<>();
                    for (MissionHQModel row : list) {

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
                Filterlist = (ArrayList<MissionHQModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public com.app.discovergolf.Util.TTextView txtMissionName;
        public RelativeLayout RlData;

        public ViewHolder(View view) {
            super(view);
            txtMissionName = (com.app.discovergolf.Util.TTextView) view.findViewById(R.id.txtMissionName);
            RlData = view.findViewById(R.id.RlData);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.mission_row, parent, false);

        return new ViewHolder(itemView);
    }

    public void attach(MissionList f) {
        try {
            callback = f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }

    public void selectNavItem(String Name) {
        callback.OnMissionClick(Name);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MissionHQModel task = Filterlist.get(position);
        holder.txtMissionName.setText(task.MissionTitle);
        holder.RlData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(task.MissionID);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Filterlist.size();
    }


}
