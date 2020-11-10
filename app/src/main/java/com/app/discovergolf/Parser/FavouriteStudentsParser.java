package com.app.discovergolf.Parser;

import com.app.discovergolf.Model.BaseParser;
import com.app.discovergolf.Model.Coach;
import com.app.discovergolf.Model.Student;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FavouriteStudentsParser extends BaseParser {

    @SerializedName("data")
    public List<Student> studentArrayList;

    @Override
    public boolean isValid() {
        if(ErrorCode.equalsIgnoreCase("0")){
            return true;
        }
        return false;
    }
}
