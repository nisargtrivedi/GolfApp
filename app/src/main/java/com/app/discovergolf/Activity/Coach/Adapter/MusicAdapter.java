package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.discovergolf.Listeners.OnMusicclick;
import com.app.discovergolf.Model.MusicModel;
import com.app.discovergolf.Model.Student;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class MusicAdapter extends  RecyclerView.Adapter<MusicAdapter.ViewHolder> {


    public List<MusicModel> list;
    Context context;
    public String name;

    OnMusicclick musicclick;


    public MusicAdapter(Context context, List<MusicModel> list){
        this.context=context;
        this.list=list;
    }
    public void CLick(OnMusicclick musicclick){
        this.musicclick=musicclick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public RelativeLayout menu;
        public ViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.Name);
            menu=view.findViewById(R.id.menu);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_music_row, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MusicModel task = list.get(position);
            holder.Name.setText(task.title);
        if(task.click==false)
            holder.menu.setBackgroundColor(context.getResources().getColor(R.color.white));
        else
            holder.menu.setBackgroundColor(context.getResources().getColor(R.color.gray11));

        holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ID------->",task.MID+"-----");
                    for(int i=0;i<list.size();i++){
                        list.get(i).click=false;
                    }
                        task.click=true;
                        musicclick.onMusic(task);
                }
            });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
