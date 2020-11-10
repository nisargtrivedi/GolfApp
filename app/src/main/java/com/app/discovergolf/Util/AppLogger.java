package com.app.discovergolf.Util;


import java.util.logging.Level;
import java.util.logging.Logger;

public  class AppLogger {

    public static void info(String str) {
        Logger.getLogger("GOLF APP").info(str);
    }

    public static void err(String str, Exception ex) {
        Logger.getLogger("GOLF APP").log(Level.INFO,str,ex);
    }

    public static void err(Exception ex) {
        Logger.getLogger("GOLF APP").log(Level.INFO,"",ex);
    }
}
