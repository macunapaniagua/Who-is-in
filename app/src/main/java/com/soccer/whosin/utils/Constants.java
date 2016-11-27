package com.soccer.whosin.utils;

/**
 * Created by Mario A on 22/10/2016.
 **/

public class Constants {

    public static final String BASE_URL = "http://192.168.1.106:8000/api/";

    // SharedPreferences values
    public static final String STORAGE_NAME = "Who is in?";
    public static final String USER_NAME    = "name";
    public static final String USER_PICTURE = "user_picture";
    public static final String USER_FB_KEY  = "facebook_id";
    public static final String GROUP_MEMBER = "group_member";

    // RETROFIT PARAMS
    public static final String GROUP_NAME = "name";
    public static final String GROUP_ID   = "group_id";
    public static final String USER_ID    = "user_id";

    // REQUEST CODES
    public static final int CALL_PERMISSION_REQUEST_CODE     = 1001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1002;

    // INTENT CODES
    public static final String MATCH_ID_KEY = "match_id";
}
