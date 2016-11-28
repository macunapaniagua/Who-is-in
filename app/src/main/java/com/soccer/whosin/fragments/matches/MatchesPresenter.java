package com.soccer.whosin.fragments.matches;

import com.soccer.whosin.interfaces.IMatchesPresenter;
import com.soccer.whosin.models.CreateMatch;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupGame;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.MatchRow;
import com.soccer.whosin.models.MatchUserStatus;
import com.soccer.whosin.utils.BusProvider;

import java.util.List;

/**
 * Created by Mario A on 20/11/16.
 **/

public class MatchesPresenter implements IMatchesPresenter {

    private MatchesInteractor mInteractor;

    public MatchesPresenter() {
        mInteractor = new MatchesInteractor(this);
    }

    public void getMatch(String pFacebookId, String pMatchId) {
        mInteractor.getMatch(pFacebookId, pMatchId);
    }

    public void getMatches(String pFacebookId, String pGroupId) {
        mInteractor.getMatches(pFacebookId, pGroupId);
    }

    public void createMatch(String pFacebookId, CreateMatch pMatch) {
        mInteractor.createMatch(pFacebookId, pMatch);
    }

    public void approveMatchAttendance(String pFacebookId, GroupGame pGroupGame) {
        mInteractor.approveMatchAttendance(pFacebookId, pGroupGame);
    }

    public void cancelMatchAttendance(String pFacebookId, GroupGame pGroupGame) {
        mInteractor.cancelMatchAttendance(pFacebookId, pGroupGame);
    }

    @Override
    public void onGetMatchesSuccessfully(List<MatchRow> pMatchRows) {
        BusProvider.getBus().post(pMatchRows);
    }

    @Override
    public void onGetMatchSuccessfully(Match pMatch) {
        BusProvider.getBus().post(pMatch);
    }

    @Override
    public void onMatchCreatedSuccessfully(Match pMatch) {
        BusProvider.getBus().post(pMatch);
    }

    @Override
    public void onApproveAttendanceSuccessfully(MatchUserStatus pMatchUserStatus) {
        BusProvider.getBus().post(pMatchUserStatus);
    }

    @Override
    public void onCancelAttendanceSuccessfully(MatchUserStatus pMatchUserStatus) {
        BusProvider.getBus().post(pMatchUserStatus);
    }

    @Override
    public void onMatchesRequestFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
