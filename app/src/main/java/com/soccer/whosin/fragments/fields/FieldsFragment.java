package com.soccer.whosin.fragments.fields;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soccer.whosin.R;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapLoadedCallback, View.OnClickListener {

    private AlertDialog vAlertDialog;
    private RelativeLayout vLoadingIndicator;
    private EditText vFieldName, vFieldPhone, vFieldPrice, vFieldCapacity;

    private GoogleMap mGoogleMap;
    private LatLng mlastLocation;
    private Map<String, SoccerField> mSoccerFields;

    public FieldsFragment() {
        // Required empty public constructor
    }

    public static FieldsFragment newInstance() {
        return new FieldsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fields, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadViews();
        this.initializeMap();
    }

    private void loadViews() {
        if (this.getView() != null)
            vLoadingIndicator = (RelativeLayout) this.getView().findViewById(R.id.fields_loading);
        this.createAlertDialog();
    }

    private void createAlertDialog() {
        View alertView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_create_soccer_field, null);
        vFieldName     = (EditText) alertView.findViewById(R.id.create_soccer_field_name);
        vFieldPhone    = (EditText) alertView.findViewById(R.id.create_soccer_field_phone);
        vFieldPrice    = (EditText) alertView.findViewById(R.id.create_soccer_field_price);
        vFieldCapacity = (EditText) alertView.findViewById(R.id.create_soccer_field_capacity);
        vAlertDialog = new AlertDialog.Builder(this.getContext())
                .setTitle(R.string.create_soccer_field)
                .setView(alertView)
                .setCustomTitle(LayoutInflater.from(this.getContext()).inflate(R.layout.alert_dialog_title, null))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.create, null).create();
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
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMapLoadedCallback(this);
        this.mGoogleMap = googleMap;
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
        BusProvider.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mlastLocation = latLng;
        createNewSoccerField();
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

    @Override
    public void onClick(View v) {
        if (v == vAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)) {
            String name     = vFieldName.getText().toString().trim();
            String phone    = vFieldPhone.getText().toString().trim();
            String price    = vFieldPrice.getText().toString().trim();
            String capacity = vFieldCapacity.getText().toString().trim();
            if (name.isEmpty() || phone.isEmpty() || price.isEmpty() || capacity.isEmpty())
                Toast.makeText(getContext(), R.string.all_fields_required, Toast.LENGTH_LONG).show();
            else {
                FieldsPresenter presenter = new FieldsPresenter();
                String groupId = LocalStorageHelper.getGroupId(this.getContext());
                String facebookId = LocalStorageHelper.getUserFacebookId(this.getContext());
                SoccerField newSoccerField = new SoccerField(name, mlastLocation, phone, price, capacity, groupId);
                presenter.createSoccerField(facebookId, newSoccerField);
            }
        }
    }

    @Override
    public void onMapLoaded() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.34, -81.22), 16));
        this.loadSoccerFields();
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
}
