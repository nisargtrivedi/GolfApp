package com.app.discovergolf.Parser;

import com.app.discovergolf.Model.BaseParser;
import com.app.discovergolf.Model.Coach;
import com.google.gson.annotations.SerializedName;

public class CoachLoginParser extends BaseParser {

    @SerializedName("data")
    public Coach coach;

    @Override
    public boolean isValid() {
        if(ErrorCode.equalsIgnoreCase("0")){
            return true;
        }
        return false;
    }
}
