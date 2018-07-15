package com.seanlohman.sean.marketmate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seanlohman.sean.marketmate.R;

public class MarketMapFragment extends MapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static MarketMapFragment newInstance() { return new MarketMapFragment(); }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_market_map, viewGroup, false);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng fullSail = new LatLng(-34,150);
        mMap.addMarker(new MarkerOptions().position(fullSail).title("Marker at fullSail"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fullSail));
    }
}
