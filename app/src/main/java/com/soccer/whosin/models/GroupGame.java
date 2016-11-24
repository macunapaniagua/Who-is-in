package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario A on 23/11/16.
 **/

public class GroupGame {

    @SerializedName("group_id") private String mGroupId;
    @SerializedName("soccer_game_id") private String mSoccerGameId;

    public GroupGame(String pGroupId, String pSoccerGameId) {
        this.mGroupId      = pGroupId;
        this.mSoccerGameId = pSoccerGameId;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public String getSoccerGameId() {
        return mSoccerGameId;
    }
}
