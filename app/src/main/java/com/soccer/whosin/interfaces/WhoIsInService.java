package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.CreateMatch;
import com.soccer.whosin.models.GroupGame;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.MatchRow;
import com.soccer.whosin.models.MatchUserStatus;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mario A on 22/10/2016.
 **/
public interface WhoIsInService {

    @POST("authenticate")
    Call<Member> authenticateUser(@Body Member pMember);

    @GET("groups/{group_code}")
    Call<GroupMember> joinGroup(@Path("group_code") String pGroupCode);

    @FormUrlEncoded
    @POST("groups")
    Call<GroupMember> createGroup(@Field(Constants.GROUP_NAME) String pGroupName);

    @GET("users_group/{group_id}")
    Call<List<Member>> getGroupMembers(@Path("group_id") String pGroupId, @Query("status") boolean pAccepted);

    @GET("soccer_fields/{group_id}")
    Call<List<SoccerField>> getFields(@Path("group_id") String pGroupId);

    @POST("soccer_fields")
    Call<SoccerField> createSoccerField(@Body SoccerField pSoccerField);

    @FormUrlEncoded
    @POST("user_group_status")
    Call<Member> approveMember(@Field(Constants.GROUP_ID) String pGroupId, @Field(Constants.USER_ID) String pUserId);

    @FormUrlEncoded
    @POST("user_group_remove")
    Call<Member> removeMember(@Field(Constants.GROUP_ID) String pGroupId, @Field(Constants.USER_ID) String pUserId);

    @GET("all_soccer_games/{group_id}")
    Call<List<MatchRow>> getMatches(@Path("group_id") String pGroupId);

    @GET("soccer_games/{game_id}")
    Call<Match> getSoccerMatch(@Path("game_id") String pMatchId);

    @POST("leave_soccer_game")
    Call<MatchUserStatus> cancelMatchAttendance(@Body GroupGame pGroupGame);

    @POST("join_soccer_game")
    Call<MatchUserStatus> approveMatchAttendance(@Body GroupGame pGroupGame);

    @POST("soccer_games")
    Call<Match> createSoccerMatch(@Body CreateMatch pMatch);
}
