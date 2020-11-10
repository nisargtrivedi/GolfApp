package com.app.discovergolf.Networks;

import com.app.discovergolf.Networks.Requests.LoginRequest;
import com.app.discovergolf.Networks.Response.AllLiveMissionRes.AllLiveResponse;
import com.app.discovergolf.Networks.Response.AllModsResp.AllModResponse;
import com.app.discovergolf.Networks.Response.CloseTimeResp.CloseTimeResponse;
import com.app.discovergolf.Networks.Response.DashboardResponse;
import com.app.discovergolf.Networks.Response.LiveMissionR.LiveMissionResponse;
import com.app.discovergolf.Networks.Response.LoginResponse;
import com.app.discovergolf.Networks.Response.StuDetailsResponse.StudentDetailsListResponse;
import com.app.discovergolf.Parser.ChatMessgageResponse;
import com.app.discovergolf.Parser.FriendResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Simar on 20-01-2019.
 */

public interface API {

    @GET("webApi.php")
    Call<LoginResponse> login(@Query("action") String action, @Query("email") String email, @Query("password") String password, @Query("user_type") String user_type);


    @GET("webApi.php")
    Call<DashboardResponse> getHomeData(@Query("action") String action,
                                        @Query("student_id") String student_id);

    @GET("webApi.php")
    Call<LiveMissionResponse> getLiveMission(@Query("action") String action,
                                             @Query("student_id") String student_id);


    @GET("webApi.php")
    Call<StudentDetailsListResponse> getTeamDetails(@Query("action") String action,
                                                    @Query("team_id") String team_id);


    @GET("webApi.php")
    Call<AllLiveResponse> getAllLiveMissions(@Query("action") String action,
                                             @Query("student_id") String team_id);



    @GET("webApi.php")
    Call<AllModResponse> getAllMods(@Query("action") String action,
                                            @Query("student_id") String team_id);

    @GET("webApi.php")
    Call<CloseTimeResponse> getCloseTime(@Query("action") String action);


    @GET("webApi.php")
    Call<FriendResponse> getFriendList(@Query("action") String action,
                                       @Query("user_id") String user_id);

    @GET("webApi.php")
    Call<ChatMessgageResponse> getChatFriendList(@Query("action") String action,
                                                 @Query("from_user") String from_user, @Query("to_user") String to_user);

    @GET("webApi.php")
    Call<ChatMessgageResponse> sendMessage(@Query("action") String action,
                                                 @Query("from_user") String from_user,
                                                 @Query("to_user") String to_user,
                                                 @Query("message") String message);


}
