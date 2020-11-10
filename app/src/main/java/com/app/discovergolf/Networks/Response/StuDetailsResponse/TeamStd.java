
package com.app.discovergolf.Networks.Response.StuDetailsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamStd {

    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("myTotalScore")
    @Expose
    private Integer myTotalScore;

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getMyTotalScore() {
        return myTotalScore;
    }

    public void setMyTotalScore(Integer myTotalScore) {
        this.myTotalScore = myTotalScore;
    }

}
