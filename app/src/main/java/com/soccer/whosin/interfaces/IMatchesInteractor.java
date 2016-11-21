package com.soccer.whosin.interfaces;

/**
 * Created by Mario A on 20/11/16.
 **/

public interface IMatchesInteractor {
    void getMatch(String pFacebookId, String pMatchId);
    void getMatches(String pFacebookId, String pGroupId);
}
