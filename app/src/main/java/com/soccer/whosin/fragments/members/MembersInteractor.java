package com.soccer.whosin.fragments.members;

import com.google.gson.Gson;
import com.soccer.whosin.interfaces.IMembersInteractor;
import com.soccer.whosin.interfaces.IMembersPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.RetrofitHelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class MembersInteractor implements IMembersInteractor {

    private IMembersPresenter mPresenter;

    public MembersInteractor(IMembersPresenter pPresenter) {
        this.mPresenter = pPresenter;
    }

    @Override
    public void approveMember(String pFacebookId, String pGroupId, String pUserId) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.approveMember(pGroupId, pUserId).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful())
                    mPresenter.onApproveMemberSuccessful(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onRequestFailed(errorMessage);
            }
        });
    }

    @Override
    public void removeMember(String pFacebookId, String pGroupId, String pUserId) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.removeMember(pGroupId, pUserId).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful())
                    mPresenter.onRemoveMemberSuccessful(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onRequestFailed(errorMessage);
            }
        });
    }

    public void getMembersFromAPI(String pFacebookId, String pGroupId, boolean pAcceptedUsers) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.getGroupMembers(pGroupId, pAcceptedUsers).enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if (response.isSuccessful())
                    mPresenter.onGetMembersSuccessful(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onRequestFailed(errorMessage);
            }
        });
    }
}
