package com.app.discovergolf.Networks.Response.CloseTimeResp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("closeTime")
    @Expose
    private String closeTime;

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

}
