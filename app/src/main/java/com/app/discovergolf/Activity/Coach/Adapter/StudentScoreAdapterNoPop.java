package com.app.discovergolf.Activity.Coach.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.discovergolf.Activity.Coach.Activity.TeamScore;
import com.app.discovergolf.Activity.Coach.Listeners.MatrixSelectedListener;
import com.app.discovergolf.Activity.student.Activity.ModTeamScore;
import com.app.discovergolf.Adapter.UsersListPopupAdapter;
import com.app.discovergolf.Fragment.ModFragment;
import com.app.discovergolf.Model.TeamScoreModel;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.TeamStd;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class StudentScoreAdapterNoPop extends  RecyclerView.Adapter<StudentScoreAdapterNoPop.ViewHolder> implements Filterable {


    public List<TeamScoreModel> list;
    public List<TeamScoreModel> Filterlist;

    Context context;
    public String name;

    AppPreferences appPreferences;

    private MatrixSelectedListener callback;

    public StudentScoreAdapterNoPop(Context context, List<TeamScoreModel> list){
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
                    List<TeamScoreModel> filteredList = new ArrayList<>();
                    for (TeamScoreModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.MatrixName.toLowerCase().contains(charString.toLowerCase())) {
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
                Filterlist = (ArrayList<TeamScoreModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public com.app.discovergolf.Util.TTextView MatrixTitle,MatrixComplete;
        public ImageView MatrixImage,StudentOne,StudentTwo,StudentThree,StudentFour;
        public ProgressBar roundProgress;
        public LinearLayout RlData;
        public TextView StudentFive;
        public ViewHolder(View view) {
            super(view);
            MatrixTitle=view.findViewById(R.id.MatrixTitle);
            MatrixComplete=view.findViewById(R.id.MatrixComplete);
            roundProgress=view.findViewById(R.id.roundProgress);

            MatrixImage=view.findViewById(R.id.MatrixImage);
            StudentOne=view.findViewById(R.id.StudentOne);
            StudentTwo=view.findViewById(R.id.StudentTwo);
            StudentThree=view.findViewById(R.id.StudentThree);
            StudentFour=view.findViewById(R.id.StudentFour);
            RlData=view.findViewById(R.id.RlData);
            StudentFive=view.findViewById(R.id.StudentFive);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.score_row, parent, false);

        return new ViewHolder(itemView);
    }


    public void attach(TeamScore f) {
        try {
            callback =  f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }

    public void attachMod(ModTeamScore f) {
        try {
            callback =  f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }
    public void ModFragment(MatrixSelectedListener c) {
        try {
            callback = c;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }

    public void selectNavItem(TeamScoreModel teamScoreModel) {

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TeamScoreModel task = Filterlist.get(position);
        holder.MatrixTitle.setText(task.TotalScore +" "+task.MatrixName);

        if(task.TotalScore>task.MatrixValue) {
            holder.MatrixComplete.setText(task.MatrixValue + "/" + task.MatrixValue + " completed");
        }else{
            holder.MatrixComplete.setText(task.TotalScore + "/" + task.MatrixValue + " completed");
        }
        Picasso.with(context).load(task.MatrixImage).into(holder.MatrixImage);
        int ans=0;
        ans= (task.TotalScore * 100) / task.MatrixValue;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.roundProgress.setOnTouchListener(null);
            holder.roundProgress.setProgress(ans, true);
        }
        holder.roundProgress.setEnabled(false);
        if(task.StudentOneImage!=null && !task.StudentOneImage.isEmpty())
            Picasso.with(context).load(task.StudentOneImage).transform(new CircleTransform()).into(holder.StudentOne);
        if(task.StudentTwoImage!=null && !task.StudentTwoImage.isEmpty())
            Picasso.with(context).load(task.StudentTwoImage).transform(new CircleTransform()).into(holder.StudentTwo);
        if(task.StudentThreeImage!=null && !task.StudentThreeImage.isEmpty())
            Picasso.with(context).load(task.StudentThreeImage).transform(new CircleTransform()).into(holder.StudentThree);
        if(task.StudentFourImage!=null && !task.StudentFourImage.isEmpty())
            Picasso.with(context).load(task.StudentFourImage).transform(new CircleTransform()).into(holder.StudentFour);
        if(task.StudentFourImage!=null && !task.StudentFourImage.isEmpty()) {
            holder.StudentFive.setVisibility(View.VISIBLE);
            int t = task.TotalStudent - 4;
            holder.StudentFive.setText("+" + t);
        }else{
            holder.StudentFive.setVisibility(View.INVISIBLE);
        }

      holder.RlData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectNavItem(task);
                callback.onMatrixClick(task);
            }
        });
    }
    @Override
    public int getItemCount() {
        return Filterlist.size();
    }


}
