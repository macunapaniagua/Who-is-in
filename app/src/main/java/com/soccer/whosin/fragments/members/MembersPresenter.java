package com.soccer.whosin.fragments.members;

import com.soccer.whosin.interfaces.IMembersPresenter;
import com.soccer.whosin.models.ErrorMessage;
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

    public void getGroupMembers(String pFacebookId, String pGroupId, boolean pAcceptedUsers) {
        mInteractor.getMembersFromAPI(pFacebookId, pGroupId, pAcceptedUsers);
    }

    public void approveMember(String pFacebookId, String pGroupId, String pUserId) {
        mInteractor.approveMember(pFacebookId, pGroupId, pUserId);
    }

    public void removeMember(String pFacebookId, String pGroupId, String pUserId) {
        mInteractor.removeMember(pFacebookId, pGroupId, pUserId);
    }

    @Override
    public void onGetMembersSuccessful(List<Member> pMembers) {
        BusProvider.getBus().post(pMembers);
    }

    @Override
    public void onRemoveMemberSuccessful(Member pMember) {
        BusProvider.getBus().post(pMember);
    }

    @Override
    public void onApproveMemberSuccessful(Member pMember) {
        BusProvider.getBus().post(pMember);
    }

    @Override
    public void onRequestFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
