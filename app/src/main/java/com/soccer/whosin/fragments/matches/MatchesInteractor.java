package com.soccer.whosin.fragments.matches;

import com.google.gson.Gson;
import com.soccer.whosin.interfaces.IMatchesInteractor;
import com.soccer.whosin.interfaces.IMatchesPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.CreateMatch;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupGame;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.MatchRow;
import com.soccer.whosin.models.MatchUserStatus;
import com.soccer.whosin.utils.RetrofitHelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario A on 20/11/16.
 **/

public class MatchesInteractor implements IMatchesInteractor {

    private IMatchesPresenter mPresenter;

    public MatchesInteractor(IMatchesPresenter pPresenter) {
        this.mPresenter = pPresenter;
    }

    @Override
    public void getMatches(String pFacebookId, String pGroupId) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.getMatches(pGroupId).enqueue(new Callback<List<MatchRow>>() {
            @Override
            public void onResponse(Call<List<MatchRow>> call, Response<List<MatchRow>> response) {
                if (response.isSuccessful())
                    mPresenter.onGetMatchesSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMatchesRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<MatchRow>> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMatchesRequestFailed(errorMessage);
            }
        });
    }

    @Override
    public void createMatch(String pFacebookId, CreateMatch pMatch) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.createSoccerMatch(pMatch).enqueue(new Callback<Match>() {
            @Override
            public void onResponse(Call<Match> call, Response<Match> response) {
                if (response.isSuccessful())
                    mPresenter.onMatchCreatedSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMatchesRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Match> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMatchesRequestFailed(errorMessage);
            }
        });
    }

    @Override
    public void cancelMatchAttendance(String pFacebookId, GroupGame pGroupGame) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.cancelMatchAttendance(pGroupGame).enqueue(new Callback<MatchUserStatus>() {
            @Override
            public void onResponse(Call<MatchUserStatus> call, Response<MatchUserStatus> response) {
                if (response.isSuccessful())
                    mPresenter.onCancelAttendanceSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMatchesRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<MatchUserStatus> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMatchesRequestFailed(errorMessage);
            }
        });
    }

    @Override
    public void approveMatchAttendance(String pFacebookId, GroupGame pGroupGame) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.approveMatchAttendance(pGroupGame).enqueue(new Callback<MatchUserStatus>() {
            @Override
            public void onResponse(Call<MatchUserStatus> call, Response<MatchUserStatus> response) {
                if (response.isSuccessful())
                    mPresenter.onApproveAttendanceSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMatchesRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<MatchUserStatus> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMatchesRequestFailed(errorMessage);
            }
        });
    }

    @Override
    public void getMatch(String pFacebookId, String pMatchId) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.getSoccerMatch(pMatchId).enqueue(new Callback<Match>() {
            @Override
            public void onResponse(Call<Match> call, Response<Match> response) {
                if (response.isSuccessful())
                    mPresenter.onGetMatchSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMatchesRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Match> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMatchesRequestFailed(errorMessage);
            }
        });
    }
}
