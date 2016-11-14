package com.soccer.whosin.login;

import com.soccer.whosin.interfaces.ILoginPresenter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.BusProvider;

/**
 * Created by Mario on 05/11/16.
 **/

public class LoginPresenter implements ILoginPresenter {

    private LoginInteractor mInteractor;

    public LoginPresenter() {
        this.mInteractor = new LoginInteractor(this);
    }

    public void createUser(Member pMember) {
        mInteractor.createUser(pMember);
    }

    @Override
    public void onMembersRequestSuccessful(Member pMember) {
        BusProvider.getBus().post(pMember);
    }

    @Override
    public void onMembersRequestFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
