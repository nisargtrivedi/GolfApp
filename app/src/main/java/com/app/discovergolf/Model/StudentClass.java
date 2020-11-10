package com.app.discovergolf.Model;

import java.io.Serializable;

public class StudentClass implements Serializable {

    public int ClassID;
    public String ClassTitle;
    public ClassMission classMission=new ClassMission();


}


//"id": 19,
//        "title": "ALL JUNIORS",
//        "classMission": {
//        "id": 38,
//        "title": "Coach Move Winter Opener_23",
//        "is_class": "0",
//        "scheduled_date": "01/10/2019",
//        "create_team": 0
//        }
