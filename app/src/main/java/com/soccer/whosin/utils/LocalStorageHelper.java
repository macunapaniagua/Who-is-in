package com.soccer.whosin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class LocalStorageHelper {

    public static String getGroupId(Context pContext) {
        SharedPreferences sharePref = pContext.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE);
        return sharePref.getString(Constants.GROUP_ID, "");
    }
}
