package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;

import java.util.List;

/**
 * Created by Mario A on 22/10/2016.
 **/
public interface IMembersPresenter {
    void onGetMembersSuccessful(List<Member> pMembers);
    void onRemoveMemberSuccessful(Member pMember);
    void onApproveMemberSuccessful(Member pMember);
    void onRequestFailed(ErrorMessage pErrorMessage);
}
