package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario on 12/11/16.
 **/

public class Group {
    @SerializedName("group_id") private String mGroupId;
    @SerializedName("code") private String mGroupCode;
    @SerializedName("name") private String mGroupName;

    public String getGroupId() {
        return mGroupId;
    }

    public String getGroupCode() {
        return mGroupCode;
    }

    public String getGroupName() {
        return mGroupName;
    }
}
