package com.app.discovergolf.Model;

import java.util.ArrayList;

public class MissionTeamModel {
    public String MissionID;
    public String TeamID;
    public String CoachID;
    public String MissionTitle;
    public ArrayList<MissionTeamPlayerModel> teamPlayerModels=new ArrayList<>();
}