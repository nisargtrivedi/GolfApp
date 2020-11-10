package com.app.discovergolf.Activity.student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Adapter.FavouriteAdapter;
import com.app.discovergolf.Activity.student.Activity.ModTeamScore_;
import com.app.discovergolf.Activity.student.Activity.StudentCreateTeam_;
import com.app.discovergolf.Model.ModMissionModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.TTextView;
import com.app.discovergolf.Util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class MyTeamAdapter extends RecyclerView.Adapter<MyTeamAdapter.ViewHolder> implements Filterable {


    public ArrayList<ModMissionModel.MissionTeam> list;
    public ArrayList<ModMissionModel.MissionTeam> Filterlist;
    Context context;
    public String name;
    private int studentId;
    private int missionId;
    int my_team_id=0;
    AppPreferences appPreferences;
    FavouriteAdapter adapter;


    public MyTeamAdapter(Context context, ArrayList<ModMissionModel.MissionTeam> list, int studentId, int missionId) {
        this.context = context;
        this.list = list;
        this.Filterlist = list;
        this.studentId = studentId;
        this.missionId = missionId;
        this.my_team_id = studentId;
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
                    ArrayList<ModMissionModel.MissionTeam> filteredList = new ArrayList<>();
                    for (ModMissionModel.MissionTeam row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTeam().toLowerCase().contains(charString.toLowerCase()) || row.getTeam().contains(charSequence)) {
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
                Filterlist = (ArrayList<ModMissionModel.MissionTeam>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView StudentName, txt_score;
        public ImageView img_edit;
        public RelativeLayout menu;

        public ViewHolder(View view) {
            super(view);
            StudentName = view.findViewById(R.id.StudentName);
            img_edit = view.findViewById(R.id.img_edit);
            menu = view.findViewById(R.id.menu);
            txt_score = view.findViewById(R.id.txt_score);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_my_team, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ModMissionModel.MissionTeam team = Filterlist.get(position);

        holder.StudentName.setText(team.getTeam());
        holder.txt_score.setText(team.getScore());
        if (team.getId() == studentId) {
            holder.img_edit.setVisibility(View.VISIBLE);
        } else {
            holder.img_edit.setVisibility(View.INVISIBLE);
        }
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(context, team.playerModels.size()+"", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, StudentCreateTeam_.class).putExtra("MissionID", missionId + "").putExtra("isEditing", true)
                        .putExtra("teamId", team.TeamID+"").putExtra("TeamName",team.getTeam()));
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.StudentName.setText(team.getTeam());
                if (team.getId() == studentId) {
                    holder.img_edit.setVisibility(View.VISIBLE);
                } else {
                    holder.img_edit.setVisibility(View.INVISIBLE);
                }

                context.startActivity(new Intent(context, ModTeamScore_.class)
                        .putExtra("TeamName", team.getTeam())
                        .putExtra("TeamID", team.TeamID)
                        .putExtra("my_team_id", my_team_id)
                        .putExtra("missionId", missionId));

            }
        });
    }

    @Override
    public int getItemCount() {
        return Filterlist.size();
    }


    //Favourite Student API Call
    private void StudentLike(String StudentID, String CoachID, int like) {
        Utility.showProgress(context);
        StringRequest request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action=" + WebUtility.UserFavorite + "&coach_id=" + CoachID + "&student_id=" + StudentID + "&is_create=" + like,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.i("LIKE RESPONSE--->", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utility.hideProgress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
