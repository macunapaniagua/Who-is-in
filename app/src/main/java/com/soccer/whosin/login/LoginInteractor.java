package com.soccer.whosin.login;

import com.google.gson.Gson;
import com.soccer.whosin.interfaces.ILoginInteractor;
import com.soccer.whosin.interfaces.ILoginPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.RetrofitHelper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 05/11/16.
 **/

public class LoginInteractor implements ILoginInteractor {

    private ILoginPresenter mPresenter;

    public LoginInteractor(ILoginPresenter pPresenter) {
        this.mPresenter = pPresenter;
    }

    @Override
    public void createUser(Member pMember) {
        WhoIsInService service = RetrofitHelper.getAPI();
        service.authenticateUser(pMember).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful())
                    mPresenter.onMembersRequestSuccessful(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMembersRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMembersRequestFailed(errorMessage);
            }
        });
    }
}
