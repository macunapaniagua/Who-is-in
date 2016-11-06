package com.soccer.whosin.interfaces;

/**
 * Created by Mario on 05/11/16.
 **/

public interface IGroupInteractor {
    void createGroup(String pFacebookId, String pGroupName);
    void joinGroup(String pFacebookId, String pGroupCode);
}
