package com.soccer.whosin.login;

import com.soccer.whosin.interfaces.ILoginInteractor;
import com.soccer.whosin.interfaces.ILoginPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.RetrofitHelper;

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
                mPresenter.onMembersRequestSuccessful(response.body());
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                mPresenter.onMembersRequestFailed(t.getMessage());
            }
        });
    }
}
