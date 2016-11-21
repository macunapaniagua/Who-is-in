package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario A on 20/11/16.
 **/

public class MatchRow {

    @SerializedName("id") private String mId;
    @SerializedName("user_status") private boolean isConfirmed;
    @SerializedName("soccer_field") private String mSoccerField;
    @SerializedName("datetime") private String mDateTime;
    @SerializedName("confirmations") private String mConfirmations;

    public String getId() {
        return mId;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getSoccerField() {
        return mSoccerField;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public String getConfirmations() {
        return mConfirmations;
    }
}
