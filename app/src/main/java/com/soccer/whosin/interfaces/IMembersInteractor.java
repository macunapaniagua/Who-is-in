package com.soccer.whosin.interfaces;

/**
 * Created by Mario A on 20/11/16.
 **/
public interface IMembersInteractor {
    void removeMember(String pFacebookId, String pGroupId, String pUserId);
    void approveMember(String pFacebookId, String pGroupId, String pUserId);
    void getMembersFromAPI(String pFacebookId, String pGroupId, boolean pAcceptedUsers);
}
