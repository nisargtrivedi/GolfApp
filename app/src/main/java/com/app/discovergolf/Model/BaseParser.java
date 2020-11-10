package com.app.discovergolf.Model;

import com.google.gson.annotations.SerializedName;

public abstract class BaseParser {

    @SerializedName("error_code")
    public String ErrorCode;

    @SerializedName("error_message")
    public String ErrorMsg;

    public abstract boolean isValid();
}
