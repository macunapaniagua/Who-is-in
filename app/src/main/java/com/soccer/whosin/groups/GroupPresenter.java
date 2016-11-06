package com.soccer.whosin.groups;

import com.soccer.whosin.interfaces.IGroupPresenter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.utils.BusProvider;

/**
 * Created by Mario on 05/11/16.
 **/

public class GroupPresenter implements IGroupPresenter {

    private GroupInteractor mInteractor;

    public GroupPresenter() {
        this.mInteractor = new GroupInteractor(this);
    }

    public void joinGroup(String pFacebookId, String pGroupCode) {
        this.mInteractor.joinGroup(pFacebookId, pGroupCode);
    }

    public void createGroup(String pFacebookId, String pGroupName) {
        mInteractor.createGroup(pFacebookId, pGroupName);
    }

    @Override
    public void onGroupCreatedSuccessfully(GroupMember pGroupMember) {
        if (pGroupMember.getError() == null) {
            BusProvider.getBus().post(pGroupMember);
        } else {
            BusProvider.getBus().post(new ErrorMessage(pGroupMember.getError()));
        }
    }

    @Override
    public void onGroupCreationFailed(String pErrorMessage) {
        BusProvider.getBus().post(new ErrorMessage(pErrorMessage));
    }

    @Override
    public void onJoinGroupSuccessfully(GroupMember pGroupMember) {
        if (pGroupMember.getError() == null) {
            BusProvider.getBus().post(pGroupMember);
        } else {
            BusProvider.getBus().post(new ErrorMessage(pGroupMember.getError()));
        }
    }

    @Override
    public void onJoinGroupFailed(String pErrorMessage) {
        BusProvider.getBus().post(new ErrorMessage(pErrorMessage));
    }
}
