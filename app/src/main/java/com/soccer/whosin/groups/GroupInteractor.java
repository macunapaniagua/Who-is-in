package com.soccer.whosin.groups;

import com.google.gson.Gson;
import com.soccer.whosin.interfaces.IGroupInteractor;
import com.soccer.whosin.interfaces.IGroupPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.utils.RetrofitHelper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 05/11/16.
 **/

public class GroupInteractor implements IGroupInteractor {

    private IGroupPresenter mPresenter;

    public GroupInteractor(IGroupPresenter pPresenter) {
        this.mPresenter = pPresenter;
    }

    @Override
    public void joinGroup(String pFacebookId, String pGroupCode) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.joinGroup(pGroupCode).enqueue(new Callback<GroupMember>() {
            @Override
            public void onResponse(Call<GroupMember> call, Response<GroupMember> response) {
                if (response.isSuccessful())
                    mPresenter.onGroupCreatedSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onJoinGroupFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<GroupMember> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onGroupCreationFailed(errorMessage);
            }
        });
    }

    @Override
    public void createGroup(String pFacebookId, String pGroupName) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.createGroup(pGroupName).enqueue(new Callback<GroupMember>() {
            @Override
            public void onResponse(Call<GroupMember> call, Response<GroupMember> response) {
                if (response.isSuccessful())
                    mPresenter.onGroupCreatedSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onGroupCreationFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<GroupMember> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onGroupCreationFailed(errorMessage);
            }
        });
    }
}
