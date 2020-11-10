package com.app.discovergolf.API;


import java.util.logging.StreamHandler;

public class WebUtility {


//    NEW URL UPDATION 1-8-2019
//    http://discoversports.co/masteradmin/webApi.php

//    public static String BASE_URL = "http://solutioncode.in/webApi.php";
//    public static String BASE_URL1 = "http://solutioncode.in";


    public static String BASE_URL = "https://discoversports.co/masteradmin/webApi.php";
    public static String BASE_URL1 = "https://discoversports.co/masteradmin/";


    public static String Login="createLogin";
    public static String ForgotPassword="forgotPassword";
    // Coach API
    public static String GetMatrixList="getMetricsList";
    public static String GetMUSIC="getMusicList";
    public static String GetScorewithMatrix="getScoreByMetrics";
    public static String FavouriteStudents="coachFavUserList";
    public static String AllStudents="coachDashboardUserList";
    public static String UserFavorite="createUserFav";
    public static String GetClassList="getClassList";
    public static String GetAllStudentList="getStudentListWithoutTeam";
    public static String CreateTeam="createTeam";
    public static String GetMissionTeams="getMissionTeam";
    public static String GetMissionList="getCustomMissionList";
    public static String GetMatrixScore="getMatricsStdListWithScore";
    public static String ChangePassword="changePassword";
    public static String CreateScore="createScoreByMetricsWithDateWise";
    public static String LiveMissionDetails="getLiveMissionDetail";
    public static String ALL_LIVE_MISSION="getAllLiveMissionList";
    public static String ALL_LIVE_MISSION_V2="getAllLiveMissionList_V2";
    public static String HOME_DASHBOARD_="getScoreByMetrics";
    public static String Matrics_List_Score="getMatricsStdListWithScore";
    public static String DELETE_TEAM="deleteTeam";

    //Student Api
    public static String GetModMissionList2 = "getModMissionList";
    public static String GetModMissionList = "getAllMissionsLists";

    public static String GetModMissionList_V2 = "getModMissionList_V2";
    public static String GetModScoreByMetrics = "getModScoreByMetrics";
    public static String CreateModScore = "createModScore";
    public static String MODMatricsStdListWithScore = "getMODMatricsStdListWithScore";
    public static String GetAllStudentListWithoutTeam = "getAllStudentListWithoutTeam";
    public static String CreateModTeam = "createModTeam";
    public static String GetMODMissionTeam = "getMODMissionTeam";


    public static String CHATUSER="friendList";
    public static String MESSAGELIST="messageList";
    public static String SENDMESSAGE="sendMessage";


}
