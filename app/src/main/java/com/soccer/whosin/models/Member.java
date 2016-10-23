package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class Member {
    @SerializedName("lgoin")
    private String mName;
    @SerializedName("avatar_url")
    private String mAvatar;

    public String getName() {
        return mName;
    }

    public String getAvatar() {
        return mAvatar;
    }
}