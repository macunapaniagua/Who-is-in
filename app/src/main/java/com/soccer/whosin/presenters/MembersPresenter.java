package com.soccer.whosin.presenters;

import com.soccer.whosin.interactors.MembersInteractor;
import com.soccer.whosin.interfaces.IMembersPresenter;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.BusProvider;

import java.util.List;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class MembersPresenter implements IMembersPresenter {

    private MembersInteractor mInteractor;

    public MembersPresenter() {
        mInteractor = new MembersInteractor(this);
    }

    public void getGroupMembers(String pGroupId) {
        mInteractor.getMembersFromAPI(pGroupId);
    }

    @Override
    public void onMembersRequestSuccessful(List<Member> pMembers) {
        BusProvider.getBus().post(pMembers);
    }

    @Override
    public void onMembersRequestFailed(String pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
