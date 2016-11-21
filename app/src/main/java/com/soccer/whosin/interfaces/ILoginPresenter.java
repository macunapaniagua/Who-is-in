package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;

/**
 * Created by Mario on 05/11/16.
 **/

public interface ILoginPresenter {
    void onMembersRequestSuccessful(Member pMember);
    void onMembersRequestFailed(ErrorMessage pErrorMessage);
}
