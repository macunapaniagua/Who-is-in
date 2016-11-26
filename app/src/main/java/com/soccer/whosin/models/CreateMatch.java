package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario A on 26/11/16.
 **/

public class CreateMatch {

    @SerializedName("soccer_field_id") private String mSoccerFieldId;
    @SerializedName("group_id") private String mGroupId;
    @SerializedName("hour") private String mTime;
    @SerializedName("date") private String mDate;
    @SerializedName("players_limit") private String mPlayersLimit;

    public CreateMatch(String mSoccerFieldId, String mGroupId, String mTime, String mDate, String mPlayersLimit) {
        this.mTime          = mTime;
        this.mDate          = mDate;
        this.mGroupId       = mGroupId;
        this.mPlayersLimit  = mPlayersLimit;
        this.mSoccerFieldId = mSoccerFieldId;
    }
}
