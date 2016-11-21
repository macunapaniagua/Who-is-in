package com.soccer.whosin.matches;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soccer.whosin.R;
import com.soccer.whosin.fragments.fields.FieldsPresenter;
import com.soccer.whosin.fragments.matches.MatchesPresenter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.Constants;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowMatchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private RelativeLayout vLoadingIndicator;
    private TextView vDate, vTime, vPlayersLimit;
    private LinearLayout vPlayersConfirmed;

    private String mMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_match);
        mMatchId = getIntent().getStringExtra(Constants.MATCH_ID_KEY);
        this.setToolbar();
        this.loadViews();
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
        vDate             = (TextView) this.findViewById(R.id.show_match_date);
        vTime             = (TextView) this.findViewById(R.id.show_match_time);
        vPlayersLimit     = (TextView) this.findViewById(R.id.show_match_players_limit);
        vPlayersConfirmed = (LinearLayout) this.findViewById(R.id.show_match_players_confirmed);
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
    public void GetMatchSuccessfully(Match pMatch) {
        if (this.mGoogleMap != null) {
            this.mGoogleMap.clear();
            this.createFieldMarker(pMatch.getSoccerField());
        }
        vDate.setText(pMatch.getDate());
        vTime.setText(pMatch.getTime());
        vPlayersLimit.setText(String.valueOf(pMatch.getPlayersLimit()));
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
        this.loadPlayersConfirmed(pMatch.getPlayers());
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
        for(Member member : pPlayersConfirmed) {
            View view = LayoutInflater.from(this).inflate(R.layout.match_participant, null);
            CircleImageView image = (CircleImageView) view.findViewById(R.id.image);
            Glide.with(this)
                    .load(member.getAvatar())
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .dontAnimate()
                    .into(image);
            // Adds the view to the layout
            vPlayersConfirmed.addView(view);
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
