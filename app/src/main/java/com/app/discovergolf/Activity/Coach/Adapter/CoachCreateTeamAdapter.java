package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Fragment.DashboardFragment;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.Model.Student;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.RoundedCornersTransformation;
import com.app.discovergolf.Util.TTextView;
import com.app.discovergolf.Util.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class CoachCreateTeamAdapter extends  RecyclerView.Adapter<CoachCreateTeamAdapter.ViewHolder> implements Filterable {


    public List<PlayerModel> list;
    public List<PlayerModel> Filterlist;

    Context context;
    public String name;

    AppPreferences appPreferences;


    public CoachCreateTeamAdapter(Context context, List<PlayerModel> list){
        this.context=context;
        this.list=list;
        this.Filterlist=list;
        appPreferences=new AppPreferences(context);
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
                    List<PlayerModel> filteredList = new ArrayList<>();
                    for (PlayerModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.PlayerName.toLowerCase().contains(charString.toLowerCase())) {
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
                Filterlist = (ArrayList<PlayerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView PLayerName;
        public ImageView PlayerImage,PlayerSelection;
        public RelativeLayout RlPlayerData;
        public ViewHolder(View view) {
            super(view);
            PLayerName = (TextView) view.findViewById(R.id.PLayerName);
            PlayerImage = (ImageView) view.findViewById(R.id.PlayerImage);
            PlayerSelection = (ImageView) view.findViewById(R.id.PlayerSelection);
            RlPlayerData=view.findViewById(R.id.RlPlayerData);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_team_player, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PlayerModel task = Filterlist.get(position);
        holder.PLayerName.setText(task.PlayerName);
        Picasso.with(context).load(task.PlayerImage).transform(new CircleTransform()).into(holder.PlayerImage);

        if(task.isClassMission==1){
            task.isSelected = true;
            holder.PlayerSelection.setSelected(true);
            holder.PlayerSelection.setImageResource(R.drawable.player_selected);
        }else {
            if (task.isSelected) {
                holder.PlayerSelection.setSelected(true);
                holder.PlayerSelection.setImageResource(R.drawable.player_selected);
            } else {
                holder.PlayerSelection.setSelected(false);
                holder.PlayerSelection.setImageResource(R.drawable.player_selected);
            }

            holder.RlPlayerData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (task.isSelected == false) {
                        task.isSelected = true;
                        Log.i("Selected Called", "Selected");
                        holder.PlayerSelection.setSelected(true);
                        holder.PlayerSelection.setImageResource(R.drawable.player_selected);
                    } else {
                        task.isSelected = false;
                        Log.i("Selected Called", "UnSelected");
                        holder.PlayerSelection.setSelected(false);
                        holder.PlayerSelection.setImageResource(R.drawable.player_selected);
                        if(task.PlayerID==Integer.parseInt(appPreferences.getString("STUDENT_ID"))){
                            holder.PlayerSelection.setEnabled(false);
                            holder.PlayerSelection.setSelected(true);
                            holder.PlayerSelection.setImageResource(R.drawable.player_selected);
                        }
                    }
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return Filterlist.size();
    }



}
