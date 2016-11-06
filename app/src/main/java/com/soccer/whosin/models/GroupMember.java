package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario on 05/11/16.
 **/

public class GroupMember {

    @SerializedName("user_id") private String mUserId;
    @SerializedName("group_id") private String mGroupId;
    @SerializedName("code") private String mGroupCode;
    @SerializedName("is_admin") private String mIsGroupAdmin;
    @SerializedName("name") private String mGroupName;
    @SerializedName("error") private String mError;

    public String getUserId() {
        return mUserId;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public String getGroupCode() {
        return mGroupCode;
    }

    public String getIsGroupAdmin() {
        return mIsGroupAdmin;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public String getError() {
        return mError;
    }
}
