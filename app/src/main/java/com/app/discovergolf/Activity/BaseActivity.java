package com.app.discovergolf.Activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.app.discovergolf.Util.AppPreferences;
import com.app.discovergolf.Util.DataContext;

import org.androidannotations.annotations.EActivity;

import es.dmoral.toasty.Toasty;

@EActivity
public class BaseActivity extends AppCompatActivity {

    public AppPreferences appPreferences;
    public DataContext dataContext;
    public void loadBaseObject(){
        appPreferences=new AppPreferences(this);
        dataContext=new DataContext(this);
    }

    public void showSuccessMsg(String msg){
        Toasty.success(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showErrorMsg(String msg){
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showInfoMsg(String msg){
        Toasty.info(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showWarningMsg(String msg){
        Toasty.warning(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showNormalMsg(String msg){
        Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
