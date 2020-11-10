package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.app.discovergolf.Model.StudentScoreModel;
import com.app.discovergolf.Model.TeamScoreModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class IndividualScoreAdapter extends  RecyclerView.Adapter<IndividualScoreAdapter.ViewHolder> implements Filterable {


    public List<StudentScoreModel> list;
    public List<StudentScoreModel> Filterlist;

    Context context;
    public String name;

    AppPreferences appPreferences;


    public IndividualScoreAdapter(Context context, List<StudentScoreModel> list){
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
                    List<StudentScoreModel> filteredList = new ArrayList<>();
                    for (StudentScoreModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.StudentName.toLowerCase().contains(charString.toLowerCase())) {
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
                Filterlist = (ArrayList<StudentScoreModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public com.app.discovergolf.Util.TTextView StudentName,score;
        public ImageView ic1;
        public LinearLayout RlData;
        public ViewHolder(View view) {
            super(view);
            StudentName=view.findViewById(R.id.StudentName);
            score=view.findViewById(R.id.score);
            ic1=view.findViewById(R.id.ic1);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.team_player_popup_row, parent, false);

        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final StudentScoreModel task = Filterlist.get(position);
        holder.StudentName.setText(task.StudentName);
        Picasso.with(context).load(task.StudentImage).transform(new CircleTransform()).into(holder.ic1);
        holder.score.setText(task.StudentTotalScore+"");

    }
    @Override
    public int getItemCount() {
        return Filterlist.size();
    }



}
