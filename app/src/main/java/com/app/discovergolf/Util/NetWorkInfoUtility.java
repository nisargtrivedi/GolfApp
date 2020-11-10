package com.app.discovergolf.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;

/**
 * Created by Nehal on 2/24/2018.
 */

public class NetWorkInfoUtility {
    public boolean isWifiEnable() {
        return isWifiEnable;
    }

    public void setIsWifiEnable(boolean isWifiEnable) {
        this.isWifiEnable = isWifiEnable;
    }

    public boolean isMobileNetworkAvailable() {
        return isMobileNetworkAvailable;
    }

    public void setIsMobileNetworkAvailable(boolean isMobileNetworkAvailable) {
        this.isMobileNetworkAvailable = isMobileNetworkAvailable;
    }

    private boolean isWifiEnable = false;
    private boolean isMobileNetworkAvailable = false;

    public boolean isNetWorkAvailableNow(Context context) {
        boolean isNetworkAvailable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        setIsWifiEnable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
        setIsMobileNetworkAvailable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());

        if (isWifiEnable() || isMobileNetworkAvailable()) {
        /*Sometime wifi is connected but service provider never connected to internet
        so cross check one more time*/
            if (isOnline())
                isNetworkAvailable = true;
        }

        return isNetworkAvailable;
    }

    public boolean isOnline() {
    /*Just to check Time delay*/
        long t = Calendar.getInstance().getTimeInMillis();

        Runtime runtime = Runtime.getRuntime();
        try {
        /*Pinging to Google server*/
            InetAddress ipAddr = InetAddress.getByName("google.com");
////                //You can replace it with your name
                return !ipAddr.equals("");
//       String command = "ping -c 1 google.com";
//       return (Runtime.getRuntime().exec (command).waitFor() == 0);
//            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long t2 = Calendar.getInstance().getTimeInMillis();
            Log.i("NetWork check Time", (t2 - t) + "");
        }
        return false;
    }
}


