package com.soccer.whosin.utils;

import com.soccer.whosin.interfaces.WhoIsInService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class RetrofitHelper {

    public static WhoIsInService getAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WhoIsInService.class);
    }
}
