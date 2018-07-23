package com.seanlohman.sean.marketmate.Fragments;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarketDetailsMap extends SupportMapFragment implements OnMapReadyCallback {

    public static MarketDetailsMap newInstance(Double lat, Double lng) {

        Bundle args = new Bundle();
        args.putDouble("Lat", lat);
        args.putDouble("Lng", lng);
        MarketDetailsMap fragment = new MarketDetailsMap();
        fragment.setArguments(args);
        return fragment;
    }

    private GoogleMap mMap;

    private Double mLat;
    private Double mLng;


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        if (getArguments() != null){
            mLat = getArguments().getDouble("Lat");
            mLng = getArguments().getDouble("Lng");
        }

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng markerLatLng = new LatLng(mLat, mLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLatLng, 16);
        mMap.animateCamera(cameraUpdate);

        MarkerOptions options = new MarkerOptions();
        //options.title("Generic Farmers Market");
        options.position(markerLatLng);

        mMap.addMarker(options);
    }
}
