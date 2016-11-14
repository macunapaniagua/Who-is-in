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
        BusProvider.getBus().post(pGroupMember);
    }

    @Override
    public void onGroupCreationFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }

    @Override
    public void onJoinGroupSuccessfully(GroupMember pGroupMember) {
        BusProvider.getBus().post(pGroupMember);
    }

    @Override
    public void onJoinGroupFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
