package com.soccer.whosin.models;

/**
 * Created by Mario A on 20/11/16.
 **/

public class MemberAction {

    private Member mMember;
    private boolean mIsApproving;

    public MemberAction (boolean pIsApproving, Member pMember) {
        this.mMember        = pMember;
        this.mIsApproving   = pIsApproving;
    }

    public Member getMember() {
        return mMember;
    }

    public boolean isIsApproving() {
        return mIsApproving;
    }
}
