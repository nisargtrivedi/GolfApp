package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Activity.Score_;
import com.app.discovergolf.Model.DashboardMenu;
import com.app.discovergolf.Model.Student;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.CircleTransform;
import com.app.discovergolf.Util.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class FavouriteAdapter extends  RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {


    public List<Student> list;
    Context context;
    public String name;



    public FavouriteAdapter(Context context, List<Student> list){
        this.context=context;
        this.list=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView StudentName;
        public ImageView StudentImage;
        public RelativeLayout menu;
        public ViewHolder(View view) {
            super(view);
            StudentName = (TextView) view.findViewById(R.id.StudentName);
            StudentImage = (ImageView) view.findViewById(R.id.StudentImage);
            menu=view.findViewById(R.id.menu);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.student_favourite_row_, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Student task = list.get(position);

        if(task.IsFavourite.equalsIgnoreCase("1")) {
            holder.StudentName.setText(task.FirstName);
            Picasso.with(context).load(task.ProfileImage).transform(new RoundedCornersTransformation(10, 10)).into(holder.StudentImage);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addStudent(Student student){
        list.add(student);
        notifyDataSetChanged();
    }
    public void removeStudent(Student student){
        for(int i=0;i<list.size();i++){
            if(list.get(i).FullName.equalsIgnoreCase(student.FullName)){
                list.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

}
