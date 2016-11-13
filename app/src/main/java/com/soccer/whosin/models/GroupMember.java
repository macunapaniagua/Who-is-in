package com.soccer.whosin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario on 05/11/16.
 **/

public class GroupMember {

    @SerializedName("group") private Group mGroup;
    @SerializedName("user") private Member mMember;

    public Group getGroup() {
        return mGroup;
    }

    public Member getMember() {
        return mMember;
    }
}
