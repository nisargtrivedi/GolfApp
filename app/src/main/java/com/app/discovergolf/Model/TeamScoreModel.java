package com.app.discovergolf.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamScoreModel implements Serializable {

    public int MatrixID;
    public String MatrixName;
    public String MatrixImage;
    public int MatrixValue;
    public int MatrixAchivement;
    public int TotalScore;
    public int TotalStudent;
    public String StudentOneImage;
    public String StudentTwoImage;
    public String StudentThreeImage;
    public String StudentFourImage;
    public ArrayList<StudentScoreModel> studentScoreModels=new ArrayList<>();
}


// "id": 6,
//         "title": "Red Zones",
//         "itm_img": "http://solutioncode.in/admin-asset/images/metrics/red-zone.png",
//         "metrics_val": 40,
//         "metrics_archivement": 40,
//         "totalScore": 0,
//         "teamStd": [
//         {
//         "profile_img": "http://solutioncode.in/admin-asset/images/profile/1544341325-iStock_000018030606Medium.jpg",
//         "name": "Std Fname Lname",
//         "student_id": 20,
//         "myTotalScore": 0
//         },
//         {
//         "profile_img": "http://solutioncode.in/admin-asset/images/profile/1546840368-canstockphoto7435734.jpg",
//         "name": "std1 fname1 std1 lname",
//         "student_id": 24,
//         "myTotalScore": 0
//         }
//         ]