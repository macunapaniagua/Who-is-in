package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.Member;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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
}
