package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mario on 20/11/16.
 **/
public class Match {

    @SerializedName("soccer_field") private SoccerField mSoccerField;
    @SerializedName("date") private String mDate;
    @SerializedName("hour") private String mTime;
    @SerializedName("players_limit") private int mPlayersLimit;
    @SerializedName("players_list") private List<Member> mPlayers;
    @SerializedName("user_status") private boolean mIsUserParticipating;

    public SoccerField getSoccerField() {
        return mSoccerField;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public List<Member> getPlayers() {
        return mPlayers;
    }

    public int getPlayersLimit() {
        return mPlayersLimit;
    }

    public boolean isUserParticipating() {
        return mIsUserParticipating;
    }
}
