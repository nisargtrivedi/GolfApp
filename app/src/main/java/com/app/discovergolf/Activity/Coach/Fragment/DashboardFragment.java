package com.app.discovergolf.Activity.Coach.Fragment;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.discovergolf.API.WebUtility;
import com.app.discovergolf.Activity.Coach.Activity.CoachDashboard;
import com.app.discovergolf.Activity.Coach.Adapter.AllStudentsAdapter;
import com.app.discovergolf.Activity.Coach.Adapter.FavouriteAdapter;
import com.app.discovergolf.Activity.Login;
import com.app.discovergolf.Adapter.MenuAdapter;
import com.app.discovergolf.Adapter.MenuTwoAdapter;
import com.app.discovergolf.Model.DashBoardMenuTwo;
import com.app.discovergolf.Model.DashboardMenu;
import com.app.discovergolf.Model.PlayerModel;
import com.app.discovergolf.Model.Student;
import com.app.discovergolf.Parser.CoachLoginParser;
import com.app.discovergolf.Parser.FavouriteStudentsParser;
import com.app.discovergolf.R;
import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.EEditText;
import com.app.discovergolf.Util.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EFragment(R.layout.coach_dashboard)
public class DashboardFragment extends Fragment {

    @ViewById
    RecyclerView RvFavStudents;

    @ViewById
    RecyclerView RvStudents;

    @ViewById
    EEditText txtSearch;

    public static FavouriteAdapter adapter;
    AllStudentsAdapter studentsAdapter;

    public static List<Student> FavouriteStudents=new ArrayList<>();
    List<Student> AllStudents=new ArrayList<>();
    AppPreferences appPreferences;

    StringRequest request;
    @AfterViews
    public void init(){
        ((CoachDashboard) getActivity()).setTitleText("Home");
        appPreferences=new AppPreferences(getActivity());
        try {
            FavouriteStudentsAPI();
            search();
        }catch (Exception ex){

        }
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        } );
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    //Search Textbox
    private void search(){
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    studentsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void FavouriteStudentsAPI() {
        Utility.showProgress(getActivity());
        FavouriteStudents.clear();
        request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action="+WebUtility.FavouriteStudents+"&coach_id="+appPreferences.getString("COACHID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("FAVOURITE STUDENT RESPONSE--->",s);
                        Utility.hideProgress();
                        try {
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            FavouriteStudentsParser studentsParser = mGson.fromJson(s, FavouriteStudentsParser.class);
                            FavouriteStudents.addAll(studentsParser.studentArrayList);
                            if(FavouriteStudents.size()>0){
                                RvFavStudents.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                                adapter=new FavouriteAdapter(getActivity(),FavouriteStudents);
                                RvFavStudents.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            AllStudentsAPI();
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(request!=null)
            request.cancel();
    }


    private void AllStudentsAPI() {
       // Utility.showProgress(getActivity());
        request = new StringRequest(Request.Method.GET,
                WebUtility.BASE_URL + "?action="+WebUtility.AllStudents+"&coach_id="+appPreferences.getString("COACHID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("All STUDENT RESPONSE--->",s);
                        //Utility.hideProgress();
                        try {
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            FavouriteStudentsParser studentsParser = mGson.fromJson(s, FavouriteStudentsParser.class);
                            AllStudents.clear();
                            AllStudents.addAll(studentsParser.studentArrayList);
                            if(AllStudents.size()>0){

                                Collections.sort(AllStudents, new Comparator<Student>() {
                                    @Override
                                    public int compare(Student lhs, Student rhs) {
                                        return lhs.FullName.compareTo(rhs.FullName);
                                    }
                                });
                                RvStudents.setLayoutManager(new LinearLayoutManager(getActivity()));
                                studentsAdapter=new AllStudentsAdapter(getActivity(),AllStudents);
                                RvStudents.setAdapter(studentsAdapter);
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Utility.hideProgress();
            }
        }) {

        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }


}
