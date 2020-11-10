
package com.app.discovergolf.Networks.Response.StuDetailsResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("itm_img")
    @Expose
    private String itmImg;
    @SerializedName("metrics_val")
    @Expose
    private Integer metricsVal;
    @SerializedName("totalScore")
    @Expose
    private Integer totalScore;
    @SerializedName("totalStudent")
    @Expose
    private Integer totalStudent;
    @SerializedName("metrics_archivement")
    @Expose
    private Integer metricsArchivement;

    @SerializedName("teamStd")
    @Expose
    public List<TeamStd> teamStd = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItmImg() {
        return itmImg;
    }

    public void setItmImg(String itmImg) {
        this.itmImg = itmImg;
    }

    public Integer getMetricsVal() {
        return metricsVal;
    }

    public void setMetricsVal(Integer metricsVal) {
        this.metricsVal = metricsVal;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(Integer totalStudent) {
        this.totalStudent = totalStudent;
    }

    public Integer getMetricsArchivement() {
        return metricsArchivement;
    }

    public void setMetricsArchivement(Integer metricsArchivement) {
        this.metricsArchivement = metricsArchivement;
    }

    public List<TeamStd> getTeamStd() {
        return teamStd;
    }

    public void setTeamStd(List<TeamStd> teamStd) {
        this.teamStd = teamStd;
    }

}
