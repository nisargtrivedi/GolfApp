package com.app.discovergolf.Parser;

import com.app.discovergolf.Model.ChatModel;
import com.app.discovergolf.Model.FriendList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatMessgageResponse implements Serializable {
    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_message")
    public String errorMessage;
    @SerializedName("data")
    public ArrayList<ChatModel> data;
}
