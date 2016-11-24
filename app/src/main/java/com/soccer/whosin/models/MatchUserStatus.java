package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mario A on 23/11/16.
 **/

public class MatchUserStatus {

    @SerializedName("user_status") private boolean mIsUserParticipating;
    @SerializedName("players_list") private List<Member> mPlayers;

    public boolean isIsUserParticipating() {
        return mIsUserParticipating;
    }

    public List<Member> getPlayers() {
        return mPlayers;
    }
}
