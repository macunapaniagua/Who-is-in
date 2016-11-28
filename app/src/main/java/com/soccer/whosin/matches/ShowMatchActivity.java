package com.soccer.whosin.matches;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soccer.whosin.ExpandableGridView;
import com.soccer.whosin.R;
import com.soccer.whosin.adapters.ParticipantsAdapter;
import com.soccer.whosin.fragments.matches.MatchesPresenter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupGame;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.MatchUserStatus;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.Constants;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.List;

public class ShowMatchActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mGoogleMap;
    private RelativeLayout vLoadingIndicator;
    private TextView vPlace, vDate, vTime, vPlayersLimit;
    private LinearLayout vPlayersContainer;
    private ExpandableGridView vPlayersConfirmed;
    private Button vParticipate;

    private String mMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_match);
        mMatchId = getIntent().getStringExtra(Constants.MATCH_ID_KEY);
        this.setToolbar();
        this.loadViews();
        this.setListeners();
        this.initializeMap();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadViews() {
        vLoadingIndicator = (RelativeLayout) this.findViewById(R.id.show_match_loading);
        vPlace            = (TextView) this.findViewById(R.id.show_match_place);
        vDate             = (TextView) this.findViewById(R.id.show_match_date);
        vTime             = (TextView) this.findViewById(R.id.show_match_time);
        vPlayersLimit     = (TextView) this.findViewById(R.id.show_match_players_limit);
        vPlayersContainer = (LinearLayout) this.findViewById(R.id.show_match_participants_confirmed);
        vPlayersConfirmed = (ExpandableGridView) this.findViewById(R.id.show_match_players_confirmed);
        vParticipate      = (Button) this.findViewById(R.id.show_match_participate);
    }

    private void setListeners() {
        vParticipate.setOnClickListener(this);
    }

    private void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.show_match_location);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        this.mGoogleMap = googleMap;
        this.loadSoccerMatch();
    }

    private void loadSoccerMatch() {
        this.showLoadingIndicator();
        String facebookId = LocalStorageHelper.getUserFacebookId(this);
        MatchesPresenter presenter = new MatchesPresenter();
        presenter.getMatch(facebookId, mMatchId);
    }

    @Override
    public void onClick(View view) {
        if (view == vParticipate) {
            if (vParticipate.getText().toString().equals(this.getString(R.string.confirm_attendance)))
                this.confirmAttendance();
            else
                this.cancelAttendance();
        }
    }

    private void confirmAttendance() {
        this.showLoadingIndicator();
        GroupMember groupMember = LocalStorageHelper.getGroupMember(this);
        String groupId = groupMember.getGroup().getGroupId();
        String facebookId = groupMember.getMember().getFacebookId();
        MatchesPresenter presenter = new MatchesPresenter();
        presenter.approveMatchAttendance(facebookId, new GroupGame(groupId, mMatchId));
    }

    private void cancelAttendance() {
        this.showLoadingIndicator();
        GroupMember groupMember = LocalStorageHelper.getGroupMember(this);
        String groupId = groupMember.getGroup().getGroupId();
        String facebookId = groupMember.getMember().getFacebookId();
        MatchesPresenter presenter = new MatchesPresenter();
        presenter.cancelMatchAttendance(facebookId, new GroupGame(groupId, mMatchId));
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
    public void onCancelOrApproveMatchAttendance(MatchUserStatus pMatchUserStatus) {
        if (pMatchUserStatus.isIsUserParticipating())
            vParticipate.setText(this.getString(R.string.cancel_attendance));
        else
            vParticipate.setText(this.getString(R.string.confirm_attendance));
        this.loadPlayersConfirmed(pMatchUserStatus.getPlayers());
        this.hideLoadingIndicator();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void GetMatchSuccessfully(Match pMatch) {
        if (this.mGoogleMap != null) {
            this.mGoogleMap.clear();
            this.createFieldMarker(pMatch.getSoccerField());
        }
        vPlace.setText(pMatch.getSoccerField().getName());
        vDate.setText(pMatch.getDate());
        vTime.setText(pMatch.getTime());
        vPlayersLimit.setText(this.getString(R.string.players, pMatch.getPlayersLimit()));
        if (pMatch.isUserParticipating())
            vParticipate.setText(this.getString(R.string.cancel_attendance));
        else
            vParticipate.setText(this.getString(R.string.confirm_attendance));
        vParticipate.setVisibility(View.VISIBLE);
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.hideLoadingIndicator();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void MatchesRequestFailed(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this, pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void loadPlayersConfirmed(List< Member> pPlayersConfirmed) {
        if (pPlayersConfirmed.size() == 0)
            vPlayersContainer.setVisibility(View.GONE);
        else {
            vPlayersConfirmed.setExpanded(true);
            vPlayersConfirmed.setAdapter(new ParticipantsAdapter(this, pPlayersConfirmed));
            vPlayersContainer.setVisibility(View.VISIBLE);
        }
    }

    protected void createFieldMarker(SoccerField pSoccerField) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(pSoccerField.getLatitude(), pSoccerField.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_field)));
        this.moveCameraToMyLocation(pSoccerField.getLatitude(), pSoccerField.getLongitude());
    }

    public void moveCameraToMyLocation(double pLatitude, double pLongitude) {
        LatLng latLng = new LatLng(pLatitude, pLongitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }
}
