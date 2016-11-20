package com.soccer.whosin.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario on 19/11/16.
 **/

public class SoccerField {

    @SerializedName("name") private String mName;
    @SerializedName("latitude") private double mLatitude;
    @SerializedName("longitude") private double mLongitude;
    @SerializedName("phone") private String mPhone;
    @SerializedName("total") private int mPrice;
    @SerializedName("players_account") private int mPlayerCount;
    @SerializedName("group_id") private String mGroupId;

    public SoccerField(String mName, LatLng pPosition, String pPhone, String pPrice, String pPlayerCount, String pGroupId) {
        this.mName          = mName;
        this.mLatitude      = pPosition.latitude;
        this.mLongitude     = pPosition.longitude;
        this.mPhone         = pPhone;
        this.mPrice         = Integer.valueOf(pPrice);
        this.mPlayerCount   = Integer.valueOf(pPlayerCount);
        this.mGroupId       = pGroupId;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public String getName() {
        return mName;
    }

    public void setmName(String pName) {
        this.mName = pName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String pPhone) {
        this.mPhone = pPhone;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int pPrice) {
        this.mPrice = pPrice;
    }

    public int getPlayerCount() {
        return mPlayerCount;
    }

    public void setPlayerCount(int pPlayerCount) {
        this.mPlayerCount = pPlayerCount;
    }
}
