package com.seanlohman.sean.marketmate.Fragments;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarketDetailsMap extends SupportMapFragment implements OnMapReadyCallback {

    public static MarketDetailsMap newInstance() { return new MarketDetailsMap(); }

    private GoogleMap mMap;



    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng fullSailLive = new LatLng(28.595898, -81.304400);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(fullSailLive, 16);
        mMap.animateCamera(cameraUpdate);

        MarkerOptions options = new MarkerOptions();
        options.title("Generic Farmers Market");
        options.position(fullSailLive);
        options.snippet("3535 Forsyth Rd\n" +
                "Winter Park, FL 32792");

        mMap.addMarker(options);
    }
}
