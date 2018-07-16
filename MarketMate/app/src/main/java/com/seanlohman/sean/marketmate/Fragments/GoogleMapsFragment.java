package com.seanlohman.sean.marketmate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.seanlohman.sean.marketmate.R;

public class GoogleMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static GoogleMapsFragment newInstance() { return new GoogleMapsFragment(); }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
