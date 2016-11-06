package com.soccer.whosin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Member;

import java.security.acl.Group;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class LocalStorageHelper {

    public static void storeLoggedUser(Context pContext, Profile pFacebookUser) {
        SharedPreferences sharedPref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        sharedPref.edit()
                .putString(Constants.USER_FB_KEY, pFacebookUser.getId())
                .putString(Constants.USER_NAME, pFacebookUser.getName())
                .putString(Constants.USER_PICTURE, pFacebookUser.getProfilePictureUri(200, 200).toString())
                .apply();
    }

    public static void storeLoggedUser(Context pContext, Member pMember) {
        SharedPreferences sharedPref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        sharedPref.edit()
                .putString(Constants.USER_FB_KEY, pMember.getFacebookId())
                .putString(Constants.USER_NAME, pMember.getName())
                .putString(Constants.USER_PICTURE, pMember.getAvatar())
                .apply();
    }

    public static String getUserFacebookId(Context pContext) {
        SharedPreferences sharedPref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.USER_FB_KEY, "");
    }

    public static void storeGroupMember(Context pContext, GroupMember pGroupMember) {
        SharedPreferences sharedPref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        sharedPref.edit()
                .putString(Constants.GROUP_MEMBER, new Gson().toJson(pGroupMember))
                .apply();
    }

    public static GroupMember getGroupMember(Context pContext) {
        SharedPreferences sharedPref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        String json = sharedPref.getString(Constants.GROUP_MEMBER, "");
        return new Gson().fromJson(json, GroupMember.class);
    }

    public static String getGroupId(Context pContext) {
        SharedPreferences sharedPref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.GROUP_ID, "");
    }
}
