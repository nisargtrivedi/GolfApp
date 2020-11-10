package com.app.discovergolf.Parser;

import com.app.discovergolf.Model.FriendList;
import com.app.discovergolf.Networks.Response.LiveMissionR.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendResponse implements Serializable {
    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_message")
    public String errorMessage;
    @SerializedName("data")
    public ArrayList<FriendList> data;
}
