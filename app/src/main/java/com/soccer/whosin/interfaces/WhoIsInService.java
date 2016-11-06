package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Mario A on 22/10/2016.
 **/
public interface WhoIsInService {

    // API para pruebas
    @GET("users")
    Call<List<Member>> getMembers();

    // API para la aplicación
    @GET("{group_id}/users")
    Call<List<Member>> getGroupMembers(@Path("group_id") String pGroupId);




    @POST("api/authenticate")
    Call<Member> authenticateUser(@Body Member pMember);

    @GET("groups/{group_code}")
    Call<GroupMember> joinGroup(@Path("group_code") String pGroupCode);

    @POST("groups")
    Call<GroupMember> createGroup(@Field(Constants.GROUP_NAME) String pGroupName);
}
