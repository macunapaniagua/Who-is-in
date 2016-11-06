package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class Member {

    @SerializedName("facebook_id") private String mFacebookId;
    @SerializedName("name") private String mName;
    @SerializedName("picture") private String mAvatar;
    @SerializedName("error") private String mError;

    public Member(String mFacebookId, String mName, String mAvatar) {
        this.mFacebookId = mFacebookId;
        this.mName = mName;
        this.mAvatar = mAvatar;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public String getName() {
        return mName;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getError() {
        return mError;
    }
}