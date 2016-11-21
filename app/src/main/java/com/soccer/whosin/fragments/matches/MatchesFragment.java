package com.soccer.whosin.fragments.matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.soccer.whosin.R;
import com.soccer.whosin.adapters.MatchesAdapter;
import com.soccer.whosin.interfaces.OnListItemEventsListener;
import com.soccer.whosin.matches.ShowMatchActivity;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.MatchRow;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.Constants;
import com.soccer.whosin.utils.DividerItemDecoration;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MatchesFragment extends Fragment implements OnListItemEventsListener {

    private RecyclerView vMatchesList;
    private RelativeLayout vLoadingIndicator;

    public MatchesFragment() {
        // Required empty public constructor
    }

    public static MatchesFragment newInstance() {
        return new MatchesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (LocalStorageHelper.getLoggedUser(this.getContext()).isAdmin())
//            this.setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.matches_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadViews();
        this.initializeViews();
        this.setListeners();
        this.loadMatches();
    }

    private void loadViews() {
        View view = this.getView();
        if (view != null) {
            vMatchesList      = (RecyclerView) view.findViewById(R.id.matches_list);
            vLoadingIndicator = (RelativeLayout) view.findViewById(R.id.matches_loading);
        }
    }

    private void initializeViews() {
        vMatchesList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        vMatchesList.setItemAnimator(new DefaultItemAnimator());
        vMatchesList.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        vMatchesList.setAdapter(new MatchesAdapter(new ArrayList<MatchRow>(), this));
    }

    private void setListeners() {
//        if (LocalStorageHelper.getLoggedUser(this.getContext()).isAdmin()) {
//
//        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }

    private void loadMatches() {
        this.showLoadingIndicator();
        this.deactivateViews();
        // Get the Params to request and filter the list of Matches
        GroupMember groupMember = LocalStorageHelper.getGroupMember(this.getContext());
        String groupId    = groupMember.getGroup().getGroupId();
        String facebookId = groupMember.getMember().getFacebookId();
        // GET Matches
        MatchesPresenter presenter = new MatchesPresenter();
        presenter.getMatches(facebookId, groupId);
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

    protected void deactivateViews() {
        vMatchesList.setEnabled(false);
    }

    protected void activateViews() {
        vMatchesList.setEnabled(true);
    }

    protected void showLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void getMatchesSuccessful(ArrayList<MatchRow> pMatchRows) {
        vMatchesList.setAdapter(new MatchesAdapter(pMatchRows, this));
        this.activateViews();
        this.hideLoadingIndicator();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void matchesRequestFailed(ErrorMessage pErrorMessage) {
        this.activateViews();
        this.hideLoadingIndicator();
        Toast.makeText(this.getContext(), pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClicked(View view, String id) {
        Intent intent = new Intent(this.getContext(), ShowMatchActivity.class);
        intent.putExtra(Constants.MATCH_ID_KEY, id);
        this.startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, String id) {
        Toast.makeText(this.getContext(), id, Toast.LENGTH_SHORT).show();
    }
}
