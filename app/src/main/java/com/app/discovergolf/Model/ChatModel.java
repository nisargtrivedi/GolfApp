package com.app.discovergolf.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatModel implements Serializable {

    @SerializedName("id")
    public int ChatID;

    @SerializedName("to_user")
    public int to_user;

    @SerializedName("from_user")
    public int from_user;

    @SerializedName("messages")
    public String messages;

    @SerializedName("to_profile_img")
    public String to_profile_img;

    @SerializedName("from_user_name")
    public String from_user_name;

    @SerializedName("from_profile_img")
    public String from_profile_img;

    @SerializedName("created_at")
    public String created_at;
}
