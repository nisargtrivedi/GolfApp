package com.app.discovergolf.Activity.Coach.Adapter;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.AddStudentScore_;
import com.app.discovergolf.Activity.Coach.Fragment.DashboardFragment;
import com.app.discovergolf.Model.Student;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
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

public class AllStudentsAdapter extends RecyclerView.Adapter<AllStudentsAdapter.ViewHolder> implements Filterable {


    public List<Student> list;
    public List<Student> Filterlist;
    Context context;
    public String name;

    AppPreferences appPreferences;
    FavouriteAdapter adapter;


    public AllStudentsAdapter(Context context, List<Student> list) {
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
                    List<Student> filteredList = new ArrayList<>();
                    for (Student row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.FullName.toLowerCase().contains(charString.toLowerCase()) || row.FirstName.contains(charSequence)) {
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
                Filterlist = (ArrayList<Student>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView StudentName, StudentCountry;
        public ImageView StudentImage, FavouriteImage;
        public RelativeLayout menu;

        public ViewHolder(View view) {
            super(view);
            StudentName = (TTextView) view.findViewById(R.id.StudentName);
            StudentCountry = (TTextView) view.findViewById(R.id.StudentCountry);
            StudentImage = (ImageView) view.findViewById(R.id.StudentImage);
            FavouriteImage = (ImageView) view.findViewById(R.id.FavouriteImage);
            menu = view.findViewById(R.id.menu);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.all_students_single, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Student task = Filterlist.get(position);

        holder.StudentName.setText(task.FullName);
        holder.StudentCountry.setText(task.StudentCity);
        Picasso.with(context).load(task.ProfileImage).transform(new RoundedCornersTransformation(15, 15)).into(holder.StudentImage);
        if (task.IsFavourite.equalsIgnoreCase("1")) {
            holder.FavouriteImage.setImageResource(R.drawable.ic_like);
            holder.FavouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.IsFavourite = "0";
                    holder.FavouriteImage.setImageResource(R.drawable.ic_favourites);
                    StudentLike(task.StudentID, appPreferences.getString("COACHID"), 0);
                    DashboardFragment.adapter.removeStudent(task);
                    DashboardFragment.adapter.notifyDataSetChanged();
                    notifyDataSetChanged();

                }
            });
        } else {
            holder.FavouriteImage.setImageResource(R.drawable.ic_favourites);
            holder.FavouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.IsFavourite = "1";
                    holder.FavouriteImage.setImageResource(R.drawable.ic_like);
                    StudentLike(task.StudentID, appPreferences.getString("COACHID"), 1);
                    DashboardFragment.adapter.addStudent(task);
                    notifyDataSetChanged();

                }
            });
        }
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddStudentScore_.class).putExtra("StudentID", task.StudentID).putExtra("music",task.music_file));
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
