package com.soccer.whosin.fragments.matches;

import com.soccer.whosin.interfaces.IMatchesPresenter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.MatchRow;
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

    @Override
    public void onGetMatchesSuccessfully(List<MatchRow> pMatchRows) {
        BusProvider.getBus().post(pMatchRows);
    }

    @Override
    public void onGetMatchSuccessfully(Match pMatch) {
        BusProvider.getBus().post(pMatch);
    }

    @Override
    public void onMatchesRequestFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
