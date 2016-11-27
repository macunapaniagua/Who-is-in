package com.soccer.whosin.fragments.fields;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soccer.whosin.R;
import com.soccer.whosin.interfaces.PermissionsCallback;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.AndroidPermissionsHelper;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.Constants;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        View.OnClickListener, PermissionsCallback, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, GoogleMap.OnMapClickListener {

    private AlertDialog vAlertDialog;
    private RelativeLayout vLoadingIndicator;
    private TextView vShowName, vShowPhone, vShowPrice, vShowCapacity;
    private EditText vFieldName, vFieldPhone, vFieldPrice, vFieldCapacity;

    private LinearLayout vFieldBottomSheet;

    private LatLng mLastLocation;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Map<String, SoccerField> mSoccerFields;
    private BottomSheetBehavior mBottomSheetBehavior;

    public FieldsFragment() {
        // Required empty public constructor
    }

    public static FieldsFragment newInstance() {
        return new FieldsFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fields, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadViews();
        this.buildGoogleApiClient();
        this.initializeMap();
    }

    private void loadViews() {
        View view = this.getView();
        if (this.getView() != null) {
            vLoadingIndicator = (RelativeLayout) view.findViewById(R.id.fields_loading);
            vFieldBottomSheet = (LinearLayout) view.findViewById(R.id.show_soccer_field_sheet);
            vShowName         = (TextView) view.findViewById(R.id.show_soccer_field_name);
            vShowPhone        = (TextView) view.findViewById(R.id.show_soccer_field_phone);
            vShowPrice        = (TextView) view.findViewById(R.id.show_soccer_field_price);
            vShowCapacity     = (TextView) view.findViewById(R.id.show_soccer_field_capacity);
        }
        this.createAlertDialog();
    }

    private void createAlertDialog() {
        View alertView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_create_soccer_field, null);
        vFieldName = (EditText) alertView.findViewById(R.id.create_soccer_field_name);
        vFieldPhone = (EditText) alertView.findViewById(R.id.create_soccer_field_phone);
        vFieldPrice = (EditText) alertView.findViewById(R.id.create_soccer_field_price);
        vFieldCapacity = (EditText) alertView.findViewById(R.id.create_soccer_field_capacity);
        vAlertDialog = new AlertDialog.Builder(this.getContext())
                .setTitle(R.string.create_soccer_field)
                .setView(alertView)
                .setCustomTitle(LayoutInflater.from(this.getContext()).inflate(R.layout.alert_dialog_title, null))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.create, null).create();
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fields_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            this.getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fields_map, mapFragment)
                    .commit();

        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (LocalStorageHelper.getLoggedUser(this.getContext()).isAdmin())
            googleMap.setOnMapLongClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(vFieldBottomSheet);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        this.mGoogleMap = googleMap;
        this.loadSoccerFields();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        SoccerField soccerField = mSoccerFields.get(marker.getId());
        if (soccerField != null) {
            vShowName.setText(soccerField.getName());
            vShowPhone.setText(soccerField.getPhone());
            vShowPrice.setText(String.valueOf(soccerField.getPrice()));
            vShowCapacity.setText(this.getContext().getString(R.string.players, soccerField.getPlayerCount()));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        return true;
    }

    private void loadSoccerFields() {
        this.showLoadingIndicator();
        // Get the Params to request and filter the list of Users
        GroupMember groupMember = LocalStorageHelper.getGroupMember(this.getContext());
        String groupId = groupMember.getGroup().getGroupId();
        String facebookId = groupMember.getMember().getFacebookId();
        // Start the Members request
        FieldsPresenter presenter = new FieldsPresenter();
        presenter.getSoccerFields(facebookId, groupId);
    }

    protected void showLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mLastLocation = latLng;
        createNewSoccerField();
    }

    @Override
    public void onClick(View v) {
        if (v == vAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)) {
            String name = vFieldName.getText().toString().trim();
            String phone = vFieldPhone.getText().toString().trim();
            String price = vFieldPrice.getText().toString().trim();
            String capacity = vFieldCapacity.getText().toString().trim();
            if (name.isEmpty() || phone.isEmpty() || price.isEmpty() || capacity.isEmpty())
                Toast.makeText(getContext(), R.string.all_fields_required, Toast.LENGTH_LONG).show();
            else {
                FieldsPresenter presenter = new FieldsPresenter();
                String groupId = LocalStorageHelper.getGroupId(this.getContext());
                String facebookId = LocalStorageHelper.getUserFacebookId(this.getContext());
                SoccerField newSoccerField = new SoccerField(name, mLastLocation, phone, price, capacity, groupId);
                presenter.createSoccerField(facebookId, newSoccerField);
            }
        }
    }

    protected void createNewSoccerField() {
        // Clean Inputs
        vFieldName.setText(null);
        vFieldPhone.setText(null);
        vFieldPrice.setText(null);
        vFieldCapacity.setText(null);
        vAlertDialog.show();
        vAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void GetSoccerFieldsRequestSuccessful(ArrayList<SoccerField> pSoccerFields) {
        if (this.mGoogleMap != null) {
            this.mGoogleMap.clear();
            this.mSoccerFields = new HashMap<>();
            for (SoccerField soccerField : pSoccerFields)
                createFieldMarker(soccerField);
        }
        this.hideLoadingIndicator();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void CreateSoccerFieldRequestSuccessful(SoccerField pSoccerField) {
        if (this.mGoogleMap != null) {
            if (this.mSoccerFields == null)
                this.mSoccerFields = new HashMap<>();
            this.createFieldMarker(pSoccerField);
            vAlertDialog.dismiss();
        }
        this.hideLoadingIndicator();
    }

    protected void createFieldMarker(SoccerField pSoccerField) {
        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                .title(pSoccerField.getName())
                .position(new LatLng(pSoccerField.getLatitude(), pSoccerField.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_field)));
        mSoccerFields.put(marker.getId(), pSoccerField);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void SoccerFieldsRequestFailed(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this.getContext(), pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        AndroidPermissionsHelper.requestPermissions(this, Constants.LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndroidPermissionsHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onAndroidPermissionsGranted(int requestCode) {
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE)
            this.checkLocationSettings();
    }

    protected void checkLocationSettings() {
        LocationSettingsRequest locationSR = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
                .build();
        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSR).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        final int statusCode = status.getStatusCode();
        if (statusCode == LocationSettingsStatusCodes.SUCCESS)
            this.moveCameraToMyLocation();
        else if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
            Toast.makeText(this.getContext(), R.string.improve_map_experience, Toast.LENGTH_LONG).show();
    }

    public void moveCameraToMyLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mGoogleMap != null && lastLocation != null) {
            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mGoogleMap.setMyLocationEnabled(true);
        }
    }
}
