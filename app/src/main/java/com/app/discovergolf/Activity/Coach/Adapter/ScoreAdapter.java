package com.app.discovergolf.Activity.Coach.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.AddStudentScore;
import com.app.discovergolf.Activity.Coach.Activity.TeamList;
import com.app.discovergolf.Activity.Coach.Listeners.ScoreListener;
import com.app.discovergolf.Activity.Coach.Listeners.TeamListListener;
import com.app.discovergolf.Activity.student.Activity.AddModStudentScore;
import com.app.discovergolf.Model.MatrixModel;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.TTextView;
import com.app.discovergolf.Util.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 5/11/2017.
 */

public class ScoreAdapter extends BaseAdapter {


    ArrayList<MatrixModel> list;
    Context context;

    LayoutInflater inflater;
    AppPreferences appPreferences;
    private ScoreListener callback;

    public ScoreAdapter(Context context, ArrayList<MatrixModel> list){
        this.context=context;
        appPreferences=new AppPreferences(context);
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    static class ViewHolder {
        TTextView Score,MenuName;
        ImageView imgMenu;
        RelativeLayout RlData;

    }

    public void selectNavItem(int ID,int Score) {
        callback.onAddScore(ID,Score);
    }
    public void attach(AddStudentScore f) {
        try {
            callback =  f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }

    public void attachMod(AddModStudentScore f) {
        try {
            callback =  f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MatrixModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MatrixModel  item  = list.get(position);

        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.single_dashboard_menu, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgMenu = (ImageView) view.findViewById(R.id.imgMenu);
            viewHolder.Score = view.findViewById(R.id.Score);
            viewHolder.MenuName = view.findViewById(R.id.MenuName);
            viewHolder.RlData=view.findViewById(R.id.menu);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.MenuName.setText(item.MatrixTitle);
        viewHolder.Score.setText(item.MatrixScore+"");
        Picasso.with(context).load(item.MatrixImage).into(viewHolder.imgMenu);
        viewHolder.RlData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.MatrixScore++;
                selectNavItem(item.MatrixID,1);
                notifyDataSetChanged();
            }
        });
        viewHolder.RlData.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                item.MatrixScore=item.MatrixScore+10;
                selectNavItem(item.MatrixID,10);
                notifyDataSetChanged();
                return false;
            }
        });

        return view;
    }


}

