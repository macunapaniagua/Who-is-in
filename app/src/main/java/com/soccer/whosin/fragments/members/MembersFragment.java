package com.soccer.whosin.fragments.members;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.soccer.whosin.R;
import com.soccer.whosin.adapters.MembersAdapter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.DividerItemDecoration;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RecyclerView vMembersList;
    private RelativeLayout vLoadingIndicator;
    private SegmentedGroup vMembersGroup;
    private RadioButton vAcceptedMembers, vPendingMembers;

    public MembersFragment() {
        // Required empty public constructor
    }

    public static MembersFragment newInstance() {
        return new MembersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_members_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadViews();
        this.initializeViews();
        this.setListeners();
        vAcceptedMembers.setChecked(true);
    }

    private void loadViews() {
        View view = this.getView();
        if (view != null) {
            vMembersList      = (RecyclerView) view.findViewById(R.id.members_list);
            vLoadingIndicator = (RelativeLayout) view.findViewById(R.id.members_loading);
            vPendingMembers   = (RadioButton) view.findViewById(R.id.members_pending_members);
            vAcceptedMembers  = (RadioButton) view.findViewById(R.id.members_accepted_members);
            vMembersGroup     = (SegmentedGroup) view.findViewById(R.id.members_status_control);
        }
    }

    private void initializeViews() {
        vMembersList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        vMembersList.setItemAnimator(new DefaultItemAnimator());
        vMembersList.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        vMembersList.setAdapter(new MembersAdapter(new ArrayList<Member>(), this.getContext()));
    }

    private void setListeners() {
        vMembersGroup.setOnCheckedChangeListener(this);
    }

    private void loadMembers() {
        this.showLoadingIndicator();
        this.deactivateViews();
        // Get the Params to request and filter the list of Users
        boolean acceptedUsers   = vAcceptedMembers.isChecked();
        GroupMember groupMember = LocalStorageHelper.getGroupMember(this.getContext());
        String groupId    = groupMember.getGroup().getGroupId();
        String facebookId = groupMember.getMember().getFacebookId();
        // Start the Members request
        MembersPresenter presenter = new MembersPresenter();
        presenter.getGroupMembers(facebookId, groupId, acceptedUsers);
    }

    protected void deactivateViews() {
        vPendingMembers.setEnabled(false);
        vAcceptedMembers.setEnabled(false);
        vMembersList.setEnabled(false);
    }

    protected void activateViews() {
        vPendingMembers.setEnabled(true);
        vAcceptedMembers.setEnabled(true);
        vMembersList.setEnabled(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.loadMembers();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    protected void showLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void GetMembersRequestSuccessful(ArrayList<Member> pMembers) {
        vMembersList.setAdapter(new MembersAdapter(pMembers, this.getContext()));
        this.activateViews();
        this.hideLoadingIndicator();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void GetGroupMembersFailed(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this.getContext(), pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }
}
