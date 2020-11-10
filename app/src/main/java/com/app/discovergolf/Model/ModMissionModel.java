package com.app.discovergolf.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ModMissionModel implements Serializable {
    public int id;
    public String academy_id;
    public String ageRange;
    public String cat_id;
    public String class_id;
    public String course_id;
    public String created_at;
    public int create_team;
    public String description;
    public String gameFrame;
    public String metrics;
    public String missionDuration;
    public int my_team_id;
    public String scheduled_date;
    public String status;
    public String teamSize;
    public int team_admin;
    public String timeDuration;
    public String title;
    public String updated_at;
    public String upload_video;
    public String mission_type;
    public String is_Class;
    public String TeamID;
    public MissionTeam missionTeam;



    public MissionTeam getMissionTeam() {
        return missionTeam;
    }

    public void setMissionTeam(MissionTeam missionTeam) {
        this.missionTeam = missionTeam;
    }

    public class MissionTeam implements Serializable {
        public int id;
        public int TeamID;
        public String team;
        public String score;
        public ArrayList<PlayerModel> playerModels=new ArrayList<>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTeam() {
            return team;
        }
        public void setTeam(String team) {
            this.team = team;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

}
