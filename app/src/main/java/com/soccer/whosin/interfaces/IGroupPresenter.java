package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;

/**
 * Created by Mario on 05/11/16.
 **/

public interface IGroupPresenter {
    void onGroupCreatedSuccessfully(GroupMember pGroupMember);
    void onGroupCreationFailed(ErrorMessage pErrorMessage);
    void onJoinGroupSuccessfully(GroupMember pGroupMember);
    void onJoinGroupFailed(ErrorMessage pErrorMessage);
}
