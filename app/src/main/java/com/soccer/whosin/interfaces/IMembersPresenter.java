package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;

import java.util.List;

/**
 * Created by Mario A on 22/10/2016.
 **/
public interface IMembersPresenter {
    void onMembersRequestSuccessful(List<Member> pMembers);
    void onMembersRequestFailed(ErrorMessage pErrorMessage);
}
