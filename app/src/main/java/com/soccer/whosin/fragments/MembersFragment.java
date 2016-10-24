package com.soccer.whosin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.soccer.whosin.R;
import com.soccer.whosin.adapters.MembersAdapter;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.presenters.MembersPresenter;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.DividerItemDecoration;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment {

    private RecyclerView vMembersList;

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
        View view = inflater.inflate(R.layout.fragment_members_list, container, false);
        if (view instanceof RecyclerView)
            vMembersList = (RecyclerView) view;
        else
            vMembersList = (RecyclerView) view.findViewById(R.id.members_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initializeViews();
        this.loadMembers();
    }

    private void initializeViews() {
        vMembersList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        vMembersList.setItemAnimator(new DefaultItemAnimator());
        vMembersList.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        vMembersList.setAdapter(new MembersAdapter(new ArrayList<Member>(), this.getContext()));
    }

    private void loadMembers() {
        MembersPresenter presenter = new MembersPresenter();
        presenter.getGroupMembers(LocalStorageHelper.getGroupId(this.getContext()));
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

    @Subscribe
    @SuppressWarnings("unused")
    public void GetMembersRequestSuccessful(List<Member> pMembers) {
        vMembersList.setAdapter(new MembersAdapter(pMembers, this.getContext()));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void GetMembersRequestFailed(String pErrorsMessage) {
        Toast.makeText(this.getContext(), pErrorsMessage, Toast.LENGTH_LONG).show();
    }
}
