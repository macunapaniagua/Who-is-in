package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.MatchRow;
import com.soccer.whosin.models.MatchUserStatus;

import java.util.List;

/**
 * Created by Mario A on 20/11/16.
 **/

public interface IMatchesPresenter {
    void onGetMatchSuccessfully(Match pMatch);
    void onGetMatchesSuccessfully(List<MatchRow> pMatchRows);
    void onApproveAttendanceSuccessfully(MatchUserStatus pMatchUserStatus);
    void onCancelAttendanceSuccessfully(MatchUserStatus pMatchUserStatus);
    void onMatchesRequestFailed(ErrorMessage pErrorMessage);
}
