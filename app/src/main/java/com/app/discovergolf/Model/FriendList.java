package com.app.discovergolf.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FriendList implements Serializable {


    @SerializedName("id")
    public int SID;

    @SerializedName("name")
    public String Name;

    @SerializedName("email")
    public String email;

    @SerializedName("last_message_time")
    public String last_message_time;

    @SerializedName("last_message")
    public String last_message;

    @SerializedName("profile_img")
    public String profile_img;
}
