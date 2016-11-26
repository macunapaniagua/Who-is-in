package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.CreateMatch;
import com.soccer.whosin.models.GroupGame;

/**
 * Created by Mario A on 20/11/16.
 **/

public interface IMatchesInteractor {
    void getMatch(String pFacebookId, String pMatchId);
    void getMatches(String pFacebookId, String pGroupId);
    void createMatch(String pFacebookId, CreateMatch pMatch);
    void cancelMatchAttendance(String pFacebookId, GroupGame pGroupGame);
    void approveMatchAttendance(String pFacebookId, GroupGame pGroupGame);
}
