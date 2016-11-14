package com.soccer.whosin.fragments.fields;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.soccer.whosin.R;

public class FieldsFragment extends Fragment implements OnMapReadyCallback {

    private RelativeLayout vLoadingIndicator;
    private GoogleMap googleMap;

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
        this.initializeComponents();
    }

    private void loadViews() {
        if (this.getView() != null)
            vLoadingIndicator = (RelativeLayout) this.getView().findViewById(R.id.fields_loading);
    }

    private void initializeComponents() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                this.getFragmentManager().findFragmentById(R.id.fields_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {




        this.googleMap = googleMap;
        this.loadSoccerFields();
    }

    private void loadSoccerFields() {

    }
}
