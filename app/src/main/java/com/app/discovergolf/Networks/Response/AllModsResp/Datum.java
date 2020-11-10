
package com.app.discovergolf.Networks.Response.AllModsResp;

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
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("academy_id")
    @Expose
    private String academyId;
    @SerializedName("course_id")
    @Expose
    private String courseId;
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("missionDuration")
    @Expose
    private String missionDuration;
    @SerializedName("ageRange")
    @Expose
    private Object ageRange;
    @SerializedName("teamSize")
    @Expose
    private String teamSize;
    @SerializedName("gameFrame")
    @Expose
    private Object gameFrame;
    @SerializedName("metrics")
    @Expose
    private String metrics;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("upload_video")
    @Expose
    private String uploadVideo;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("timeDuration")
    @Expose
    private String timeDuration;
    @SerializedName("missionSkill")
    @Expose
    private String missionSkill;
    @SerializedName("scheduled_date")
    @Expose
    private String scheduledDate;
    @SerializedName("create_team")
    @Expose
    private Integer createTeam;
    @SerializedName("my_team_id")
    @Expose
    private Integer myTeamId;
    @SerializedName("team_admin")
    @Expose
    private Integer teamAdmin;
    @SerializedName("missionTeam")
    @Expose
    private List<MissionTeam> missionTeam = null;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMissionDuration() {
        return missionDuration;
    }

    public void setMissionDuration(String missionDuration) {
        this.missionDuration = missionDuration;
    }

    public Object getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(Object ageRange) {
        this.ageRange = ageRange;
    }

    public String getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }

    public Object getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(Object gameFrame) {
        this.gameFrame = gameFrame;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadVideo() {
        return uploadVideo;
    }

    public void setUploadVideo(String uploadVideo) {
        this.uploadVideo = uploadVideo;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getMissionSkill() {
        return missionSkill;
    }

    public void setMissionSkill(String missionSkill) {
        this.missionSkill = missionSkill;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Integer getCreateTeam() {
        return createTeam;
    }

    public void setCreateTeam(Integer createTeam) {
        this.createTeam = createTeam;
    }

    public Integer getMyTeamId() {
        return myTeamId;
    }

    public void setMyTeamId(Integer myTeamId) {
        this.myTeamId = myTeamId;
    }

    public Integer getTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(Integer teamAdmin) {
        this.teamAdmin = teamAdmin;
    }

    public List<MissionTeam> getMissionTeam() {
        return missionTeam;
    }

    public void setMissionTeam(List<MissionTeam> missionTeam) {
        this.missionTeam = missionTeam;
    }

}
